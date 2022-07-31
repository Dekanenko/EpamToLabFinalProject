package com.Dekanenko.commands;

import com.Dekanenko.managers.OrderManager;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ConfirmDenyCommand implements Command{
    @Override
    public void execute(HttpServletRequest req, HttpServletResponse resp) {
        OrderManager orderManager = OrderManager.getInstance();
        orderManager.confirmOrderDeny(req, resp);
    }
}
