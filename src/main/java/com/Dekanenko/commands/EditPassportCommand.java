package com.Dekanenko.commands;

import com.Dekanenko.managers.PassportManager;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class EditPassportCommand implements Command{
    @Override
    public void execute(HttpServletRequest req, HttpServletResponse resp) {
        PassportManager passportManager = PassportManager.getInstance();
        passportManager.editPassport(req, resp);
    }
}
