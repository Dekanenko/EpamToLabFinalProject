package com.Dekanenko.filters;

import com.Dekanenko.DAO.entity.User;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.DispatcherType;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

//this filter sorts users with different roles to their main pages, depending on their roleId
@WebFilter(urlPatterns = "/managePage",
        dispatcherTypes = {
                DispatcherType.FORWARD,
                DispatcherType.INCLUDE,
                DispatcherType.REQUEST})
public class PageManageFilter extends HttpFilter {

    private static final Logger log = LogManager.getLogger(PageManageFilter.class);

    @Override
    protected void doFilter(HttpServletRequest req, HttpServletResponse res, FilterChain chain) throws IOException, ServletException {
        int roleId;
        User user = (User)req.getSession().getAttribute("currentUser");
        if(user != null)
            roleId = user.getRoleId();
        else
            roleId = -1;
        log.debug("Page Manager Filter: roleId: "+roleId);
        switch (roleId){
            case 1: req.getRequestDispatcher(req.getContextPath()+"/adminPage.jsp").forward(req, res);break;
            case 2: req.getRequestDispatcher(req.getContextPath()+"/managerPage.jsp").forward(req, res);break;
            case 3: req.getRequestDispatcher(req.getContextPath()+"/driverPage.jsp").forward(req, res);break;
            case 4: req.getRequestDispatcher(req.getContextPath()+"/clientPage.jsp").forward(req, res);break;
            default: req.getRequestDispatcher(req.getContextPath()+"/login.jsp").forward(req, res);
        }
        chain.doFilter(req, res);
    }
}
