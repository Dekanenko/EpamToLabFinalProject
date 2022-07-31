package com.Dekanenko.filters;

import com.Dekanenko.DAO.DAOFactory.DAOFactory;
import com.Dekanenko.DAO.DAOInterfaces.UserDAO;
import com.Dekanenko.DAO.entity.User;
import com.Dekanenko.DBUtils;
import com.Dekanenko.helpClass.Helper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.DispatcherType;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

@WebFilter(urlPatterns = {"/userEdit.jsp", "/order.jsp"},
        dispatcherTypes = {
                DispatcherType.FORWARD,
                DispatcherType.INCLUDE,
                DispatcherType.REQUEST})
//class updates user session, in order to check whether this user banned, and update its balance as well
public class UserSessionUpdateFilter extends HttpFilter {

    private static final Logger log = LogManager.getLogger(UserSessionUpdateFilter.class);

    @Override
    protected void doFilter(HttpServletRequest req, HttpServletResponse res, FilterChain chain) throws IOException, ServletException {

        log.debug("User session update filter");
        User currentUser = (User)req.getSession().getAttribute("currentUser");
        if(currentUser == null){
            Helper.forwarder(req, res, "/managePage");
        }
        UserDAO userDAO = DAOFactory.getInstance().getUserDAO();

        try(Connection connection = DBUtils.getInstance().getConnection()) {
            User user = userDAO.getUser(connection, currentUser.getId());
            HttpSession session = req.getSession();
            session.setAttribute("currentUser", user);
        } catch (SQLException ex) {
            log.error(ex.getMessage());
            Helper.forwarder(req, res, "/errorPage");
        }

        chain.doFilter(req, res);
    }
}
