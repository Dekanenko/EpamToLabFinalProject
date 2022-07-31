package com.Dekanenko.managers;

import com.Dekanenko.DAO.DAOFactory.DAOFactory;
import com.Dekanenko.DAO.DAOInterfaces.PassportDAO;
import com.Dekanenko.DAO.entity.Passport;
import com.Dekanenko.DBUtils;
import com.Dekanenko.exceptions.DAOException;
import com.Dekanenko.helpClass.Helper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

public class PassportManager {

    private static final Logger log = LogManager.getLogger(PassportManager.class);

    private static PassportManager instance;
    private static DBUtils dbUtils;
    private static PassportDAO passportDAO;

    public static synchronized PassportManager getInstance(){
        if(instance == null)
            instance = new PassportManager();
        return instance;
    }

    private PassportManager() {
        dbUtils = DBUtils.getInstance();
        DAOFactory daoFactory = DAOFactory.getInstance();
        passportDAO = daoFactory.getPassportDAO();
    }

    public int executeAdd(Connection connection, HttpServletRequest req) throws SQLException {
        Passport passport = Helper.passportCreator(req);
        return passportDAO.addPassport(connection, passport);
    }

    public void getPassport(HttpServletRequest req, HttpServletResponse resp){
        try (Connection connection = dbUtils.getConnection()){
            int passportId = Integer.parseInt(req.getParameter("passportId"));
            Passport passport = passportDAO.getPassport(connection, passportId);
            if(passport != null)
                req.setAttribute("passport", passport);
        } catch (SQLException ex) {
            log.error(ex.getMessage());
        } catch (DAOException ex) {
            log.error(ex.getMessage());

            //just to show on view that it is a problem. Message will be internationalized
            req.setAttribute("getPassportError", 1);
        }
    }

    public void editPassport(HttpServletRequest req, HttpServletResponse resp){
        Passport passport = new Passport();
        passport.setId(Integer.parseInt(req.getParameter("passportId")));
        passport.setName(req.getParameter("name"));
        passport.setSurname(req.getParameter("surname"));
        passport.setUniqueNum(req.getParameter("uniqueNum"));

        try(Connection connection = dbUtils.getConnection()){
            if(passportDAO.updatePassport(connection, passport))
                resp.sendRedirect("/userEdit.jsp");
        }catch (SQLException ex){
            log.error(ex.getMessage());

            //just to show on view that it is a problem. Message will be internationalized
            req.setAttribute("passportError", 1);
            Helper.forwarder(req, resp, "/userEdit.jsp");
        } catch (IOException ex) {
            log.error(ex.getMessage());
        }
    }

    public boolean deletePassport(Connection connection, int passportId) throws SQLException {
        return passportDAO.deletePassport(connection, passportId);
    }
}
