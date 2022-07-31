package com.Dekanenko.controller;

import com.Dekanenko.CommandMaker;
import com.Dekanenko.commands.Command;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/controller")
public class Controller extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        String commandName = req.getParameter("command");

        //construct command
        Command command = CommandMaker.makeCommand(commandName);
        if(command == null){
            req.setAttribute("errorMessage", "Command not found");
            req.getRequestDispatcher("/errorPage.jsp").forward(req, resp);
        }else {
            command.execute(req, resp);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        String commandName = req.getParameter("command");

        //construct command
        Command command = CommandMaker.makeCommand(commandName);
        if(command == null){
            req.setAttribute("errorMessage", "Command not found");
            req.getRequestDispatcher("/errorPage.jsp").forward(req, resp);
        }else {
            command.execute(req, resp);
        }
    }
}
