package com.Dekanenko.commands;

import com.Dekanenko.helpClass.Helper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class SendToCarDeleteCommand implements Command{

    private static final Logger log = LogManager.getLogger(SendToCarDeleteCommand.class);

    @Override
    public void execute(HttpServletRequest req, HttpServletResponse resp) {
        req.setAttribute("mark", 1);
        req.setAttribute("delId", req.getParameter("delCarId"));
        Helper.forwarder(req, resp, "/deleteCar.jsp");
    }
}
