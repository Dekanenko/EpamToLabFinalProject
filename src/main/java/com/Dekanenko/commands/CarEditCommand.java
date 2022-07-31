package com.Dekanenko.commands;

import com.Dekanenko.managers.CarManager;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class CarEditCommand implements Command{
    @Override
    public void execute(HttpServletRequest req, HttpServletResponse resp) {
        CarManager carManager = CarManager.getInstance();
        carManager.executeUpdateCar(req, resp);
    }
}
