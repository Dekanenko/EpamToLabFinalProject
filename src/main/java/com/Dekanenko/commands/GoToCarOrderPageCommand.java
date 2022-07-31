package com.Dekanenko.commands;

import com.Dekanenko.helpClass.Helper;
import com.Dekanenko.managers.CarManager;
import com.Dekanenko.managers.PassportManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class GoToCarOrderPageCommand implements Command{

    private static final Logger log = LogManager.getLogger(GoToCarOrderPageCommand.class);

    @Override
    public void execute(HttpServletRequest req, HttpServletResponse resp) {
        PassportManager passportManager = PassportManager.getInstance();
        passportManager.getPassport(req, resp);
        CarManager carManager = CarManager.getInstance();
        carManager.executeGetCar(req, resp);
        try {
            req.getRequestDispatcher("/order.jsp").forward(req, resp);
        }catch (ServletException | IOException ex){
            log.error(ex.getMessage());
            Helper.forwarder(req, resp, "/managePage");
        }
    }
}
