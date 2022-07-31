package com.Dekanenko.DAO.DAOImpl;

import com.Dekanenko.DAO.DAOInterfaces.PassportDAO;
import com.Dekanenko.DAO.entity.Passport;
import com.Dekanenko.DBUtils;
import com.Dekanenko.exceptions.DAOException;

import java.sql.*;

import static com.Dekanenko.DAO.queries.MySQLQuery.PassportQuery.*;

public class MySQLPassportDAOImpl implements PassportDAO {

    private static MySQLPassportDAOImpl instance;

    private MySQLPassportDAOImpl() {
    }

    public static synchronized MySQLPassportDAOImpl getInstance(){
        if(instance == null)
            instance = new MySQLPassportDAOImpl();
        return instance;
    }

    @Override
    public int addPassport(Connection connection, Passport passport) throws SQLException{
        int k = 0;
        PreparedStatement preparedStatement = null;
        ResultSet rs = null;
        try{
            preparedStatement = connection.prepareStatement(INSERT_PASSPORT,
                    Statement.RETURN_GENERATED_KEYS);

            preparedStatement.setString(++k, passport.getName());
            preparedStatement.setString(++k, passport.getSurname());
            preparedStatement.setString(++k, passport.getUniqueNum());

            preparedStatement.execute();

            rs = preparedStatement.getGeneratedKeys();
            if(rs.next()){
                return rs.getInt(1);
            }
        }finally {
            if(rs != null)
                DBUtils.resultSetClose(rs);

            if(preparedStatement != null)
                DBUtils.preparedStatementClose(preparedStatement);
        }

        return -1;
    }

    @Override
    public Passport getPassport(Connection connection, int passportId) throws SQLException, DAOException{
        Passport passport = null;
        PreparedStatement preparedStatement = null;
        ResultSet rs = null;

        try {
            preparedStatement = connection.prepareStatement(SELECT_PASSPORT_BY_ID);
            preparedStatement.setInt(1, passportId);
            rs = preparedStatement.executeQuery();
            if(rs.next()){
                passport = new Passport();
                passport.setName(rs.getString("name"));
                passport.setSurname(rs.getString("surname"));
                passport.setUniqueNum(rs.getString("unique_num"));
            }else {
                throw new DAOException("Cannot get needed passport");
            }
        }finally {
            if(rs != null)
                DBUtils.resultSetClose(rs);

            if(preparedStatement != null)
                DBUtils.preparedStatementClose(preparedStatement);
        }

        return passport;
    }

    @Override
    public boolean updatePassport(Connection connection, Passport passport) throws SQLException {
        int k = 0;
        PreparedStatement preparedStatement = null;

        try {
            preparedStatement = connection.prepareStatement(UPDATE_PASSPORT);
            preparedStatement.setString(++k, passport.getName());
            preparedStatement.setString(++k, passport.getSurname());
            preparedStatement.setString(++k, passport.getUniqueNum());
            preparedStatement.setInt(++k, passport.getId());
            preparedStatement.executeUpdate();
        }finally {
            if(preparedStatement != null)
                DBUtils.preparedStatementClose(preparedStatement);
        }

        return true;
    }

    @Override
    public boolean deletePassport(Connection connection, int passportId) throws SQLException {
        PreparedStatement preparedStatement = null;

        try {
            preparedStatement = connection.prepareStatement(DELETE_PASSPORT);
            preparedStatement.setInt(1, passportId);
            preparedStatement.execute();
        }finally {
            if(preparedStatement != null)
                DBUtils.preparedStatementClose(preparedStatement);
        }

        return true;
    }
}
