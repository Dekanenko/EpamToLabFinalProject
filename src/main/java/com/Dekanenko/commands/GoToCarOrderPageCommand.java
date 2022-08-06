package com.Dekanenko.commands;

import com.Dekanenko.helpClass.Helper;
import com.Dekanenko.managers.CarManager;
import com.Dekanenko.managers.PassportManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class GoToCarOrderPageCommand implements Command{

    private static final Logger log = LogManager.getLogger(GoToCarOrderPageCommand.class);

    @Override
    public void execute(HttpServletRequest req, HttpServletResponse resp) {
        PassportManager passportManager = PassportManager.getInstance();
        passportManager.getPassport(req, resp);
        CarManager carManager = CarManager.getInstance();
        carManager.executeGetCar(req, resp);
        Helper.forwarder(req, resp, "/order.jsp");
    }
}
