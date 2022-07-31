package com.Dekanenko.DAO.DAOInterfaces;

import com.Dekanenko.DAO.entity.User;
import com.Dekanenko.exceptions.DAOException;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public interface UserDAO {

    public User getUserWithPassword(Connection connection, String login, StringBuilder pass) throws SQLException, DAOException;
    public User getUser(Connection connection, int id) throws SQLException;
    public List<User> getAllUsers(Connection connection, Map<Integer, String> map) throws SQLException, DAOException;
    public List<User> getAllDrivers(Connection connection) throws SQLException;
    public boolean insertUser(Connection connection, User user, String pass) throws SQLException;
    public boolean updateUserStatus(Connection connection, int id, boolean status) throws SQLException;
    public boolean deleteUser(Connection connection, int id) throws SQLException;
    public List<User> searchUser(Connection connection, Map<Integer, String> map, String login, String roleId) throws SQLException;
    public boolean updateUser(Connection connection, User user, String pass) throws SQLException;
    public boolean changeUserCash(Connection connection, int id, double cash) throws SQLException;
    public boolean configureUserPassport(Connection connection, int userId, int passportId) throws SQLException;
}
