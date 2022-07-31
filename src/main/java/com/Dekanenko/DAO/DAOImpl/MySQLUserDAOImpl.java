package com.Dekanenko.DAO.DAOImpl;

import com.Dekanenko.DAO.DAOInterfaces.UserDAO;
import com.Dekanenko.DAO.entity.User;
import com.Dekanenko.DBUtils;
import com.Dekanenko.exceptions.DAOException;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.Dekanenko.DAO.queries.MySQLQuery.UserQuery.*;

public class MySQLUserDAOImpl implements UserDAO {

    private static MySQLUserDAOImpl instance;

    private MySQLUserDAOImpl() {
    }

    public static synchronized MySQLUserDAOImpl getInstance(){
        if(instance == null)
            instance = new MySQLUserDAOImpl();
        return instance;
    }

    @Override
    public User getUserWithPassword(Connection connection, String login, StringBuilder pass) throws SQLException, DAOException {
        User user = null;
        PreparedStatement preparedStatement = null;
        ResultSet rs = null;

        try {
            preparedStatement = connection.prepareStatement(SELECT_USER_BY_LOGIN);
            preparedStatement.setString(1, login);
            rs = preparedStatement.executeQuery();
            if(rs.next()){
                user = userSet(rs);

                //separate work with password
                pass.append(rs.getString("password"));
            } else {
                throw new DAOException("Cannot find user");
            }
        }finally {
            if(rs != null)
                DBUtils.resultSetClose(rs);
            if(preparedStatement != null)
                DBUtils.preparedStatementClose(preparedStatement);
        }

        return user;
    }

    @Override
    public User getUser(Connection connection, int id) throws SQLException {
        User user = null;
        PreparedStatement preparedStatement = null;
        try {
            preparedStatement = connection.prepareStatement(SELECT_USER_BY_ID);
            preparedStatement.setInt(1, id);
            ResultSet rs = preparedStatement.executeQuery();
            if(rs.next()){
                user = userSet(rs);
            }
        }finally {
            if(preparedStatement != null)
                DBUtils.preparedStatementClose(preparedStatement);
        }

        return user;
    }

    @Override
    public List<User> getAllUsers(Connection connection, Map<Integer, String> map) throws SQLException, DAOException {
        List<User> list = new ArrayList<>();
        User user = null;
        PreparedStatement preparedStatement = null;
        ResultSet rs = null;

        try {
            preparedStatement = connection.prepareStatement(SELECT_ALL_USERS_WITH_ROLE);
            rs = preparedStatement.executeQuery();
            while (rs.next()){
                user = userSet(rs);

                //map is used for getting roles and their id from role table
                map.put(rs.getInt("role_id"), rs.getString("role_status"));
                list.add(user);
            }
            if(list.size() == 0)
                throw new DAOException("No users");
        }finally {
            if(rs != null)
                DBUtils.resultSetClose(rs);
            if(preparedStatement != null)
                DBUtils.preparedStatementClose(preparedStatement);
        }

        return list;
    }

    @Override
    public List<User> getAllDrivers(Connection connection) throws SQLException {
        List<User> list = new ArrayList<>();
        PreparedStatement preparedStatement = null;
        ResultSet rs = null;

        try {
            preparedStatement = connection.prepareStatement(SELECT_ALL_DRIVERS);
            rs = preparedStatement.executeQuery();
            while (rs.next()){
                list.add(userSet(rs));
            }
        }finally {
            if(rs != null)
                DBUtils.resultSetClose(rs);
            if(preparedStatement != null)
                DBUtils.preparedStatementClose(preparedStatement);
        }

        return list;
    }

    @Override
    public boolean insertUser(Connection connection, User user, String pass) throws SQLException {
        int k = 0;
        PreparedStatement preparedStatement = null;
        ResultSet rs = null;

        try {
            preparedStatement = connection.prepareStatement(INSERT_USER, Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(++k, user.getLogin());
            preparedStatement.setString(++k, user.getEmail());
            preparedStatement.setString(++k, pass);
            preparedStatement.setDouble(++k, user.getCash());
            preparedStatement.setBoolean(++k, user.isAffordable());
            preparedStatement.setInt(++k, user.getRoleId());

            preparedStatement.execute();

            rs = preparedStatement.getGeneratedKeys();
            if(rs.next()){
                user.setId(rs.getInt(1));
                return true;
            }
        }finally {
            if(rs != null)
                DBUtils.resultSetClose(rs);
            if(preparedStatement != null)
                DBUtils.preparedStatementClose(preparedStatement);
        }

        return false;
    }

    @Override
    public boolean updateUserStatus(Connection connection, int id, boolean status) throws SQLException {
        int k = 0;
        PreparedStatement preparedStatement = null;

        try {
            preparedStatement = connection.prepareStatement(CHANGE_USER_STATUS);
            preparedStatement.setBoolean(++k, status);
            preparedStatement.setInt(++k, id);
            preparedStatement.executeUpdate();
        }finally {
            if(preparedStatement != null)
                DBUtils.preparedStatementClose(preparedStatement);
        }

        return true;
    }

    @Override
    public List<User> searchUser(Connection connection, Map<Integer, String> roleMap, String login, String roleId) throws SQLException {
        int k = 0;
        List<User> userList = new ArrayList<>();
        PreparedStatement preparedStatement = null;
        ResultSet rs = null;

        try {
            preparedStatement = connection.prepareStatement(SEARCH_USER);
            preparedStatement.setString(++k, "%"+login+"%");
            preparedStatement.setString(++k, "%"+roleId+"%");
            rs = preparedStatement.executeQuery();
            while (rs.next()){
                userList.add(userSet(rs));
                roleMap.put(rs.getInt("role_id"), rs.getString("role_status"));
            }

        }finally {
            if(rs != null)
                DBUtils.resultSetClose(rs);
            if(preparedStatement != null)
                DBUtils.preparedStatementClose(preparedStatement);
        }

        return userList;
    }

    @Override
    public boolean deleteUser(Connection connection, int id) throws SQLException {
        PreparedStatement preparedStatement = null;

        try {
            preparedStatement = connection.prepareStatement(DELETE_USER);
            preparedStatement.setInt(1, id);
            preparedStatement.execute();
        }finally {
            if(preparedStatement != null)
                DBUtils.preparedStatementClose(preparedStatement);
        }

        return true;
    }

    @Override
    public boolean updateUser(Connection connection, User user, String pass) throws SQLException {
        int k=0;
        PreparedStatement preparedStatement = null;

        try {
            preparedStatement = connection.prepareStatement(UPDATE_USER);
            preparedStatement.setString(++k, user.getLogin());
            preparedStatement.setString(++k, user.getEmail());
            preparedStatement.setString(++k, pass);
            preparedStatement.setDouble(++k, user.getCash());
            preparedStatement.setBoolean(++k, user.isAffordable());
            preparedStatement.setInt(++k, user.getRoleId());
            preparedStatement.setInt(++k, user.getId());

            preparedStatement.executeUpdate();
        }finally {
            if(preparedStatement != null)
                DBUtils.preparedStatementClose(preparedStatement);
        }

        return true;
    }

    @Override
    public boolean changeUserCash(Connection connection, int id, double cash) throws SQLException {
        int k = 0;
        PreparedStatement preparedStatement = null;

        try {
            preparedStatement = connection.prepareStatement(CHANGE_USER_CASH);
            preparedStatement.setDouble(++k, cash);
            preparedStatement.setInt(++k, id);
            preparedStatement.executeUpdate();
        }finally {
            if(preparedStatement != null)
                DBUtils.preparedStatementClose(preparedStatement);
        }

        return true;
    }

    @Override
    public boolean configureUserPassport(Connection connection, int userId, int passportId) throws SQLException {
        int k = 0;
        PreparedStatement preparedStatement = null;

        try {
            preparedStatement = connection.prepareStatement(UPDATE_USER_PASSPORT);
            if(passportId != 0)
                preparedStatement.setInt(++k, passportId);
            else
                preparedStatement.setNull(++k, Types.INTEGER);
            preparedStatement.setInt(++k, userId);
            preparedStatement.executeUpdate();
        }finally {
            if(preparedStatement != null)
                DBUtils.preparedStatementClose(preparedStatement);
        }

        return true;
    }

    //method for constructing User object using ResultSet
    private User userSet(ResultSet rs) throws SQLException {
        User user = new User();
        user.setId(rs.getInt("id"));
        user.setLogin(rs.getString("login"));
        user.setEmail(rs.getString("email"));
        user.setCash(rs.getDouble("cash"));
        user.setAffordable(rs.getBoolean("affordable"));
        user.setRoleId(rs.getInt("role_id"));
        user.setPassportId(rs.getInt("passport_id"));
        return user;
    }
}
