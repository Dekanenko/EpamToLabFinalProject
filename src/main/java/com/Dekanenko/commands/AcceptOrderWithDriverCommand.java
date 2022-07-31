package com.Dekanenko.commands;

import com.Dekanenko.managers.OrderManager;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class AcceptOrderWithDriverCommand implements Command{
    @Override
    public void execute(HttpServletRequest req, HttpServletResponse resp) {
        OrderManager orderManager = OrderManager.getInstance();
        orderManager.acceptOrder(req, resp, Integer.parseInt(req.getParameter("driverId")));
    }
}
