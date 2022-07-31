package com.Dekanenko.helpClass;

import com.Dekanenko.DAO.entity.Passport;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class Helper {

    private static final Logger log = LogManager.getLogger(Helper.class);

    //method for creating Passport object, using info from request
    //this method is used in different DAOs
    public static Passport passportCreator(HttpServletRequest request){
        Passport passport = new Passport();
        passport.setName(request.getParameter("name"));
        passport.setSurname(request.getParameter("surname"));
        passport.setUniqueNum(request.getParameter("uniqueNum"));
        return passport;
    }

    //method forwards to mainPage filter
    public static void forwarder(HttpServletRequest req, HttpServletResponse resp, String page){
        try{
            req.getRequestDispatcher(page).forward(req, resp);
        }catch (ServletException | IOException ex){
            log.error(ex.getMessage());
            try {
                resp.sendRedirect("/managePage");
            }catch (IOException e){
                log.error(e.getMessage());
            }
        }
    }
}
