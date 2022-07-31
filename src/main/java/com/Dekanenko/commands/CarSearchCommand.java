package com.Dekanenko.commands;

import com.Dekanenko.managers.CarManager;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class CarSearchCommand implements Command{
    @Override
    public void execute(HttpServletRequest req, HttpServletResponse resp) {
        CarManager carManager = CarManager.getInstance();
        carManager.executeCarSearch(req, resp);
    }
}
