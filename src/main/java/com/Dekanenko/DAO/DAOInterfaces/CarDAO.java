package com.Dekanenko.DAO.DAOInterfaces;
import com.Dekanenko.DAO.entity.Car;
import com.Dekanenko.exceptions.DAOException;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public interface CarDAO {

    public boolean insertCar(Connection connection, Car car) throws SQLException;
    public boolean updateCar(Connection connection, Car car) throws SQLException;
    public boolean changeCarUsage(Connection connection, int id, boolean inUsage) throws SQLException;
    public boolean changeCarDamage(Connection connection, int id, boolean damaged) throws SQLException;
    public List<Car> getAllCars(Connection connection) throws SQLException, DAOException;
    public List<Car> searchCars(Connection connection, String brand, String qualityClass,
                                String carDamageOption, String carUsageOption) throws SQLException;
    public boolean deleteCar(Connection connection, int id) throws SQLException;
    public Car getCar(Connection connection, int id) throws SQLException;
}
