package com.Dekanenko.commands;

import com.Dekanenko.helpClass.Helper;
import com.Dekanenko.managers.PassportManager;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ShowPassportCommand implements Command{

    @Override
    public void execute(HttpServletRequest req, HttpServletResponse resp) {
        PassportManager passportManager = PassportManager.getInstance();

        //put passport into request
        passportManager.getPassport(req, resp);

        Helper.forwarder(req, resp, "/userEdit.jsp");
    }
}
