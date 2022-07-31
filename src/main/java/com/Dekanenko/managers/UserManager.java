package com.Dekanenko.managers;

import com.Dekanenko.DAO.DAOFactory.DAOFactory;
import com.Dekanenko.DAO.DAOInterfaces.OrderDAO;
import com.Dekanenko.DAO.DAOInterfaces.UserDAO;
import com.Dekanenko.DAO.entity.User;
import com.Dekanenko.DBUtils;
import com.Dekanenko.exceptions.DAOException;
import com.Dekanenko.helpClass.Helper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class UserManager {

    private static final Logger log = LogManager.getLogger(UserManager.class);

    private static UserManager instance;
    private static DBUtils dbUtils;
    private static UserDAO userDAO;
    private static OrderDAO orderDAO;

    public static synchronized UserManager getInstance(){
        if(instance == null)
            instance = new UserManager();
        return instance;
    }

    private UserManager() {
        dbUtils = DBUtils.getInstance();
        DAOFactory daoFactory = DAOFactory.getInstance();
        userDAO = daoFactory.getUserDAO();
        orderDAO = daoFactory.getOrderDAO();
    }

    public void executeLogin(HttpServletRequest req, HttpServletResponse resp) {
        try(Connection connection = dbUtils.getConnection()){
            User user = null;
            String login = req.getParameter("login");
            StringBuilder pass = new StringBuilder();
            if((user = userDAO.getUserWithPassword(connection, login, pass))!= null){
                //compare passwords after hashing
                if(passwordHash(req.getParameter("password")).equals(pass.toString())){

                    //if login is done, create a new session for current user
                    HttpSession session = req.getSession();
                    session.setAttribute("currentUser", user);
                    req.getRequestDispatcher("/managePage").forward(req, resp);
                    return;
                }else {
                    throw new DAOException("Cannot login: incorrect password");
                }
            }
        }catch (SQLException ex){
            log.error(ex.getMessage());
        } catch (ServletException ex) {
            log.error(ex.getMessage());
        } catch (IOException ex) {
            log.error(ex.getMessage());
        } catch (DAOException ex) {
            log.error(ex.getMessage());

            //just to show on view that it is a problem. Message will be internationalized
            req.setAttribute("error", 1);
            Helper.forwarder(req, resp, "/login.jsp");
        }
    }

    public void executeRegistration(HttpServletRequest req, HttpServletResponse resp){
        User user = new User();
        user.setLogin(req.getParameter("login"));
        user.setEmail(req.getParameter("email"));
        String pass = passwordHash(req.getParameter("password"));
        user.setRoleId(Integer.parseInt(req.getParameter("roleId")));

        try(Connection connection = dbUtils.getConnection()){
            if(userDAO.insertUser(connection, user, pass)){
                resp.sendRedirect("/managePage");
            }else {
                throw new SQLException("Cannot insert user");
            }
        } catch (SQLException ex) {
            log.error(ex.getMessage());
            req.setAttribute("errorMessage", "Registration failed. Login or Email are already used");
            Helper.forwarder(req, resp, "/errorPage.jsp");
        } catch (IOException ex) {
            log.error(ex.getMessage());
        }
    }

    public void executeShowUsers(HttpServletRequest req, HttpServletResponse resp) {
        try(Connection connection = dbUtils.getConnection()){
            Map<Integer, String> map = new TreeMap<>();
            List<User> list = userDAO.getAllUsers(connection, map);

            //put all users and their role names to request
            if(list.size()>0){
                req.setAttribute("userList", list);
                req.setAttribute("roleMap", map);
            }
            req.getRequestDispatcher("/adminPage.jsp").forward(req, resp);
        }catch (SQLException ex) {
            log.error(ex.getMessage());
        }catch (DAOException ex){
            log.error(ex.getMessage());

            //just to show on view that it is a problem. Message will be internationalized
            req.setAttribute("noUsersError", 1);
            Helper.forwarder(req, resp, "/adminPage.jsp");
        } catch (ServletException ex) {
            log.error(ex.getMessage());
        } catch (IOException ex) {
            log.error(ex.getMessage());
        }
    }

    public void executeChangeUserStatus(HttpServletRequest req, HttpServletResponse resp) {
        int id = Integer.parseInt(req.getParameter("userId"));

        boolean status = Boolean.parseBoolean(req.getParameter("aff"));

        try(Connection connection = dbUtils.getConnection()){
            if(userDAO.updateUserStatus(connection, id, !status)) {
                executeShowUsers(req, resp);
            }
        }catch (SQLException ex){
            log.error(ex.getMessage());
            req.setAttribute("errorMessage", "Cannot change user status");
            Helper.forwarder(req, resp, "/errorPage.jsp");
        }
    }

    public void executeDelete(HttpServletRequest req, HttpServletResponse resp) {
        Connection connection = null;
        boolean deleteApproval = false;
        int passportId = Integer.parseInt(req.getParameter("passportId"));
        int id = Integer.parseInt(req.getParameter("id"));
        User user = (User)req.getSession().getAttribute("currentUser");
        int currentId = user.getId();
        try{
            log.debug("method:executeDelete\ttransaction started");
            connection = dbUtils.getConnection();
            connection.setAutoCommit(false);
            connection.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);

            //check whether this user appears in user_order table
            try{
                orderDAO.getUserOrders(connection, id);
            }catch (DAOException ex){
                //if exception is thrown, it means that user does not have any orders
                deleteApproval = true;
            }

            if(!deleteApproval)
                throw new SQLException("User appears in order");

            //if user has passport
            if(passportId != 0) {
                PassportManager passportManager = PassportManager.getInstance();

                //clear passport id from user before delete passport
                log.debug("Clear passport id from user");
                if(!userDAO.configureUserPassport(connection, id, 0))
                    throw new SQLException("User Update failed");

                //delete passport
                log.debug("Delete user passport");
                if(!passportManager.deletePassport(connection, passportId))
                    throw new SQLException("Cannot delete passport");
            }

            //delete user
            log.debug("Delete user");
            userDAO.deleteUser(connection, id);
            log.debug("method:executeDelete\ttransaction commit");
            connection.commit();

            //if it was current user, clear the session
            if(id == currentId){
                log.debug("Clear session");
                req.getSession().removeAttribute("currentUser");
                resp.sendRedirect("/changeLocale.jsp?page=login.jsp&locale=en");
            }else {
                resp.sendRedirect("/managePage");
            }

        }catch (SQLException ex){
            if(connection!=null)
                dbUtils.rollBack(connection);

            log.error(ex.getMessage());
            req.setAttribute("errorMessage", "Cannot delete user. This user has at least one order");
            Helper.forwarder(req, resp, "/errorPage.jsp");
        } catch (IOException ex) {
            log.error(ex.getMessage());
        }finally {
            dbUtils.close(connection);
        }
    }

    public void executeUserUpdate(HttpServletRequest req, HttpServletResponse resp) {
        User user = new User();
        user.setId(Integer.parseInt(req.getParameter("id")));
        user.setPassportId(Integer.parseInt(req.getParameter("passportId")));
        user.setLogin(req.getParameter("login"));
        user.setEmail(req.getParameter("email"));
        String pass = passwordHash(req.getParameter("password"));
        user.setRoleId(Integer.parseInt(req.getParameter("roleId")));
        user.setCash(Double.parseDouble(req.getParameter("firstCash"))+Double.parseDouble(req.getParameter("cash")));
        user.setAffordable(Boolean.parseBoolean(req.getParameter("aff")));

        try(Connection connection = dbUtils.getConnection()){
            userDAO.updateUser(connection, user, pass);

            //update session
            req.removeAttribute("currentUser");
            log.debug("Session update");
            HttpSession session = req.getSession();
            session.setAttribute("currentUser", user);

            resp.sendRedirect("/userEdit.jsp");
        }catch (SQLException ex){
            log.error(ex.getMessage());

            //just to show on view that it is a problem. Message will be internationalized
            req.setAttribute("userEditError", 1);
            Helper.forwarder(req, resp, "/userEdit.jsp");
        } catch (IOException ex) {
            log.error(ex.getMessage());
        }
    }

    public void executeUserSearch(HttpServletRequest req, HttpServletResponse resp){
        List<User> userList = new ArrayList<>();
        Map<Integer, String> roleMap = new TreeMap<>();
        String login = req.getParameter("login");
        String roleId = req.getParameter("role");

        //if roleId equals 0 -> sql LIKE % (get all roles)
        if(roleId.equals("0"))
            roleId = "";

        try (Connection connection = dbUtils.getConnection()){
            userList = userDAO.searchUser(connection, roleMap, login, roleId);

            //put found users and their role names to request
            req.setAttribute("userList", userList);
            req.setAttribute("roleMap", roleMap);

            req.getRequestDispatcher("/adminPage.jsp").forward(req, resp);
        } catch (SQLException ex){
            log.error(ex.getMessage());
        } catch (ServletException ex) {
            log.error(ex.getMessage());
        } catch (IOException ex) {
            log.error(ex.getMessage());
        }
    }

    public void addPassportToUser(HttpServletRequest req, HttpServletResponse resp){
        Connection connection = null;
        try {
            log.debug("method:addPassportToUser\ttransaction started");
            connection = dbUtils.getConnection();
            connection.setAutoCommit(false);
            connection.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);

            //add passport to passport table
            log.debug("Add passport to passport table");
            PassportManager passportManager = PassportManager.getInstance();
            int passportId = passportManager.executeAdd(connection, req);

            //add passport to user
            int userId = Integer.parseInt(req.getParameter("userId"));
            log.debug("Update user passport id");
            userDAO.configureUserPassport(connection, userId, passportId);

            //update session
            log.debug("Update session");
            User user = (User)req.getSession().getAttribute("currentUser");
            user.setPassportId(passportId);
            resp.sendRedirect("userEdit.jsp");

            log.debug("method:addPassportToUser\ttransaction commit");
            connection.commit();
        } catch (SQLException ex) {
            if(connection != null)
                dbUtils.rollBack(connection);

            log.error(ex.getMessage());

            //just to show on view that it is a problem. Message will be internationalized
            req.setAttribute("passportError", 1);
            Helper.forwarder(req, resp, "/userEdit.jsp");
        } catch (IOException ex) {
            log.error(ex.getMessage());
        } finally {
            dbUtils.close(connection);
        }
    }

    public boolean deletePassport(HttpServletRequest req, HttpServletResponse resp){
        Connection connection = null;
        try {
            log.debug("method:deletePassport\ttransaction started");
            connection = dbUtils.getConnection();
            connection.setAutoCommit(false);
            connection.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);

            PassportManager passportManager = PassportManager.getInstance();
            int passportId = Integer.parseInt(req.getParameter("passportId"));
            int userId = Integer.parseInt(req.getParameter("userId"));

            //delete passportId from user before delete passport
            log.debug("Clear user passport id");
            if(!userDAO.configureUserPassport(connection, userId, 0))
                throw new SQLException("User Update failed");

            //delete passport
            log.debug("Delete passport");
            if(!passportManager.deletePassport(connection, passportId)){
                throw new SQLException("Cannot delete passport");
            }else{
                //update session
                log.debug("Update session");
                User user = (User)req.getSession().getAttribute("currentUser");
                user.setPassportId(0);
                resp.sendRedirect("userEdit.jsp");
            }

            log.debug("method:deletePassport\ttransaction commit");
            connection.commit();
        } catch (SQLException ex) {
            if(connection != null)
                dbUtils.rollBack(connection);

            log.error(ex.getMessage());
            req.setAttribute("errorMessage", "Cannot delete passport. This passport appears at least in one order");
            Helper.forwarder(req, resp, "/errorPage.jsp");
        } catch (IOException ex) {
            log.error(ex.getMessage());
        }finally {
            dbUtils.close(connection);
        }
        return true;
    }

    //method for password hashing (for hashing SHA-256 is used)
    private String passwordHash(String pass){
        String out = null;
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] encodedhash = digest.digest(
                    pass.getBytes(StandardCharsets.UTF_8));
            out = new String(encodedhash);
        } catch (NoSuchAlgorithmException ex) {
            log.error(ex.getMessage());
        }
        return out;
    }
}
