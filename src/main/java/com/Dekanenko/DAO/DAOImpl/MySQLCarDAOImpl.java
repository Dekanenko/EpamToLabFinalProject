package com.Dekanenko.DAO.DAOImpl;

import com.Dekanenko.DAO.DAOInterfaces.CarDAO;
import com.Dekanenko.DAO.entity.Car;
import com.Dekanenko.DBUtils;
import com.Dekanenko.exceptions.DAOException;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import static com.Dekanenko.DAO.queries.MySQLQuery.CarQuery.*;

public class MySQLCarDAOImpl implements CarDAO {

    private static MySQLCarDAOImpl instance;

    private MySQLCarDAOImpl() {
    }

    public static synchronized MySQLCarDAOImpl getInstance(){
        if(instance == null)
            instance = new MySQLCarDAOImpl();
        return instance;
    }

    @Override
    public boolean insertCar(Connection connection, Car car) throws SQLException {
        int k = 0;
        PreparedStatement preparedStatement = null;

        try {
            preparedStatement = connection.prepareStatement(INSERT_CAR);
            preparedStatement.setString(++k, car.getBrand());
            preparedStatement.setString(++k, car.getQualityClass());
            preparedStatement.setString(++k, car.getName());
            preparedStatement.setDouble(++k, car.getCost());

            preparedStatement.execute();
        }finally {
            if(preparedStatement != null)
                DBUtils.preparedStatementClose(preparedStatement);
        }

        return true;
    }

    @Override
    public boolean updateCar(Connection connection, Car car) throws SQLException {
        int k = 0;
        PreparedStatement preparedStatement = null;
        try{
            preparedStatement = connection.prepareStatement(UPDATE_CAR);
            preparedStatement.setString(++k, car.getBrand());
            preparedStatement.setString(++k, car.getQualityClass());
            preparedStatement.setString(++k, car.getName());
            preparedStatement.setDouble(++k, car.getCost());
            preparedStatement.setBoolean(++k, car.isUsed());
            preparedStatement.setBoolean(++k, car.isDamaged());
            preparedStatement.setInt(++k, car.getId());

            preparedStatement.executeUpdate();
        }finally {
            if(preparedStatement != null)
                DBUtils.preparedStatementClose(preparedStatement);
        }
        return true;
    }

    @Override
    public boolean changeCarUsage(Connection connection, int id, boolean inUsage) throws SQLException {
        int k = 0;
        PreparedStatement preparedStatement = null;
        try {
            preparedStatement = connection.prepareStatement(CHANGE_CAR_USAGE);
            preparedStatement.setBoolean(++k, inUsage);
            preparedStatement.setInt(++k, id);
            preparedStatement.executeUpdate();
        }finally {
            if(preparedStatement != null)
                DBUtils.preparedStatementClose(preparedStatement);
        }
        return true;
    }

    @Override
    public boolean changeCarDamage(Connection connection, int id, boolean damaged) throws SQLException {
        int k = 0;
        PreparedStatement preparedStatement = null;
        try {
            preparedStatement = connection.prepareStatement(CHANGE_CAR_DAMAGE);
            preparedStatement.setBoolean(++k, damaged);
            preparedStatement.setInt(++k, id);
            preparedStatement.executeUpdate();
        }finally {
            if(preparedStatement != null)
                DBUtils.preparedStatementClose(preparedStatement);
        }
        return true;
    }

    @Override
    public List<Car> getAllCars(Connection connection) throws SQLException, DAOException {
        List<Car> list = new ArrayList<>();
        PreparedStatement preparedStatement = null;
        try {
            preparedStatement = connection.prepareStatement(SELECT_ALL_CARS);
            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()){
                Car car = new Car();
                carSet(car, rs);
                list.add(car);
            }
            if(list.size() == 0)
                throw new DAOException("No cars");
        }finally {
            if(preparedStatement != null)
                DBUtils.preparedStatementClose(preparedStatement);
        }
        return list;
    }

    @Override
    public List<Car> searchCars(Connection connection, String brand, String qualityClass,
                                String carDamageOption, String carUsageOption) throws SQLException {
        List<Car> list = new ArrayList<>();
        int k = 0;
        PreparedStatement preparedStatement = null;
        ResultSet rs = null;
        try {
            preparedStatement = connection.prepareStatement(SEARCH_CAR);

            preparedStatement.setString(++k, "%"+brand+"%");
            preparedStatement.setString(++k, "%"+qualityClass+"%");
            preparedStatement.setString(++k, "%"+carDamageOption+"%");
            preparedStatement.setString(++k, "%"+carUsageOption+"%");

            rs = preparedStatement.executeQuery();
            while (rs.next()){
                Car car = new Car();
                carSet(car, rs);
                list.add(car);
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
    public boolean deleteCar(Connection connection, int id) throws SQLException {
        PreparedStatement preparedStatement = null;
        try {
            preparedStatement = connection.prepareStatement(DELETE_CAR);
            preparedStatement.setInt(1, id);

            preparedStatement.execute();
        }finally {
            if(preparedStatement != null)
                DBUtils.preparedStatementClose(preparedStatement);
        }

        return true;
    }

    @Override
    public Car getCar(Connection connection, int id) throws SQLException {
        Car car = new Car();
        PreparedStatement preparedStatement = null;
        ResultSet rs = null;
        try {
            preparedStatement = connection.prepareStatement(GET_CAR_BY_ID);
            preparedStatement.setInt(1, id);
            rs = preparedStatement.executeQuery();
            if(rs.next()){
                carSet(car, rs);
            }
        }finally {
            if(rs != null)
                DBUtils.resultSetClose(rs);
            if(preparedStatement != null)
                DBUtils.preparedStatementClose(preparedStatement);
        }

        return car;
    }


    //method for constructing Car object using ResultSet
    private void carSet(Car car, ResultSet rs) throws SQLException {
        car.setId(rs.getInt("id"));
        car.setBrand(rs.getString("brand"));
        car.setQualityClass(rs.getString("class"));
        car.setName(rs.getString("name"));
        car.setCost(rs.getDouble("cost"));
        car.setUsed(rs.getBoolean("used"));
        car.setDamaged(rs.getBoolean("damaged"));
    }

}
