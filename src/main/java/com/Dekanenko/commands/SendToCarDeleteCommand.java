package com.Dekanenko.commands;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class SendToCarDeleteCommand implements Command{

    private static final Logger log = LogManager.getLogger(SendToCarDeleteCommand.class);

    @Override
    public void execute(HttpServletRequest req, HttpServletResponse resp) {
        req.setAttribute("mark", 1);
        req.setAttribute("delId", req.getParameter("delCarId"));
        try {
            req.getRequestDispatcher("/deleteCar.jsp").forward(req, resp);
        } catch (ServletException | IOException ex) {
            log.error(ex.getMessage());
        }
    }
}
