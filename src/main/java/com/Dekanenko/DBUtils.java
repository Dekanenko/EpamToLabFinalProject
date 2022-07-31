package com.Dekanenko;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DBUtils {

    private static final Logger log = LogManager.getLogger(DBUtils.class);

    private static DBUtils instance;
    private static DataSource ds;

    public static synchronized DBUtils getInstance(){
        if(instance == null)
            instance = new DBUtils();
        return instance;
    }

    private DBUtils() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            ds = null;
            Context initContext = new InitialContext();
            Context envContext  = (Context)initContext.lookup("java:/comp/env");
            ds = (DataSource)envContext.lookup("jdbc/db");
        }catch (ClassNotFoundException | NamingException ex){
            log.debug(ex.getMessage());
        }
    }

    public Connection getConnection() {
        Connection connection = null;
        try{
            connection = ds.getConnection();
        }catch (SQLException ex){
            log.debug(ex.getMessage());
        }
        return connection;
    }

    public static void close(Connection con){
        try {
            con.close();
        } catch (SQLException ex) {
            log.debug(ex.getMessage());
        }
    }

    public static void preparedStatementClose(PreparedStatement preparedStatement){
        try {
            preparedStatement.close();
        } catch (SQLException ex) {
            log.debug(ex.getMessage());
        }
    }

    public static void resultSetClose(ResultSet resultSet){
        try {
            resultSet.close();
        } catch (SQLException ex) {
            log.debug(ex.getMessage());
        }
    }

    public static void rollBack(Connection con){
        try {
            con.rollback();
        } catch (SQLException ex) {
            log.debug(ex.getMessage());
        }
    }
}
