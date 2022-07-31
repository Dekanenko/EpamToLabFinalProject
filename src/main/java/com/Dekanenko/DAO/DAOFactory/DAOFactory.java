package com.Dekanenko.DAO.DAOFactory;

import com.Dekanenko.DAO.DAOInterfaces.CarDAO;
import com.Dekanenko.DAO.DAOInterfaces.OrderDAO;
import com.Dekanenko.DAO.DAOInterfaces.PassportDAO;
import com.Dekanenko.DAO.DAOInterfaces.UserDAO;

public abstract class DAOFactory {

    private static DAOFactory instance;

    public static synchronized DAOFactory getInstance(){
        if(instance == null){
            //distinguish database
            if(System.getProperty("database").equals("MySQL"))
                instance = new MySQLDAOFactory();
        }
        return instance;
    }

    public abstract UserDAO getUserDAO();
    public abstract CarDAO getCarDAO();
    public abstract PassportDAO getPassportDAO();
    public abstract OrderDAO getOrderDAO();

}
