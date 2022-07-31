package com.Dekanenko.commands;

import com.Dekanenko.managers.UserManager;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class DeletePassportCommand implements Command{
    @Override
    public void execute(HttpServletRequest req, HttpServletResponse resp) {
        UserManager userManager = UserManager.getInstance();
        userManager.deletePassport(req, resp);
    }
}
