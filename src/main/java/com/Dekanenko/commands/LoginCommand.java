package com.Dekanenko.commands;

import com.Dekanenko.helpClass.Helper;
import com.Dekanenko.helpClass.VerifyRecaptcha;
import com.Dekanenko.managers.UserManager;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


public class LoginCommand implements Command {

    @Override
    public void execute(HttpServletRequest req, HttpServletResponse resp) {
        String recaptchaResponse = req.getParameter("g-recaptcha-response");
        boolean verify = VerifyRecaptcha.verify(recaptchaResponse);
        if(!verify){
            req.setAttribute("recaptchaError", 1);
            Helper.forwarder(req, resp, "/login.jsp");
        }

        UserManager userManager = UserManager.getInstance();
        userManager.executeLogin(req, resp);
    }

}
