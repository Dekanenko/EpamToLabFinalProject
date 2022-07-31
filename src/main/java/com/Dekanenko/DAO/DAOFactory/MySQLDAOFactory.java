package com.Dekanenko.DAO.DAOFactory;

import com.Dekanenko.DAO.DAOImpl.MySQLCarDAOImpl;
import com.Dekanenko.DAO.DAOImpl.MySQLOrderDAOImpl;
import com.Dekanenko.DAO.DAOImpl.MySQLPassportDAOImpl;
import com.Dekanenko.DAO.DAOImpl.MySQLUserDAOImpl;
import com.Dekanenko.DAO.DAOInterfaces.CarDAO;
import com.Dekanenko.DAO.DAOInterfaces.OrderDAO;
import com.Dekanenko.DAO.DAOInterfaces.PassportDAO;
import com.Dekanenko.DAO.DAOInterfaces.UserDAO;

public class MySQLDAOFactory extends DAOFactory {

    @Override
    public UserDAO getUserDAO() {
        return MySQLUserDAOImpl.getInstance();
    }

    @Override
    public CarDAO getCarDAO() {
        return MySQLCarDAOImpl.getInstance();
    }

    @Override
    public PassportDAO getPassportDAO() {
        return MySQLPassportDAOImpl.getInstance();
    }

    @Override
    public OrderDAO getOrderDAO() {
        return MySQLOrderDAOImpl.getInstance();
    }
}
