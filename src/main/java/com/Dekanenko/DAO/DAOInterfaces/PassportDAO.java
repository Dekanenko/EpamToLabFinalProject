package com.Dekanenko.DAO.DAOInterfaces;

import com.Dekanenko.DAO.entity.Passport;
import com.Dekanenko.exceptions.DAOException;

import java.sql.Connection;
import java.sql.SQLException;

public interface PassportDAO {
    public int addPassport(Connection connection, Passport passport) throws SQLException;
    public Passport getPassport(Connection connection, int id) throws SQLException, DAOException;
    public boolean updatePassport(Connection connection, Passport passport) throws SQLException;
    public boolean deletePassport(Connection connection, int passportId) throws SQLException;
}
