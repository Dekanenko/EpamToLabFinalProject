package com.Dekanenko.commands;

import com.Dekanenko.helpClass.Helper;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class LogOutCommand implements Command{

    @Override
    public void execute(HttpServletRequest req, HttpServletResponse resp) {
        req.getSession().removeAttribute("currentUser");
        Helper.forwarder(req, resp, "/changeLocale.jsp?page=login.jsp&locale=en");
    }
}
