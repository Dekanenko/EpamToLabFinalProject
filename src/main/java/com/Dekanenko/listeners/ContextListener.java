package com.Dekanenko.listeners;

import com.Dekanenko.DBUtils;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Properties;

import org.apache.logging.log4j.*;


@WebListener
public class ContextListener implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        ServletContext ctx = sce.getServletContext();

        //configure logger
        String path = ctx.getRealPath("/WEB-INF/log4j2.log");
        System.setProperty("logFile", path);

        final Logger log = LogManager.getLogger(ContextListener.class);
        log.info("Context initialized");
        log.debug("path = " + path);

        //configure used database
        System.setProperty("database", "MySQL");

        //check the connection
        try {
            DBUtils.getInstance().getConnection().close();
        } catch (SQLException ex) {
            throw new IllegalStateException("Cannot obtain a connection", ex);
        }

        //configure locales
        String localesFileName = ctx.getInitParameter("locales");

        // obtain real path on server
        String localesFileRealPath = ctx.getRealPath(localesFileName);

        // local descriptions
        Properties locales = new Properties();
        try {
            locales.load(new FileInputStream(localesFileRealPath));
        } catch (IOException ex) {
            log.error(ex.getMessage());
        }

        // save descriptions to servlet context
        ctx.setAttribute("locales", locales);
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        final Logger log = LogManager.getLogger(ContextListener.class);
        log.info("Context destroyed");
    }
}
