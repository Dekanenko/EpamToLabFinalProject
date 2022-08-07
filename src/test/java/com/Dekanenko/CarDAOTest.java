package com.Dekanenko;

import com.Dekanenko.DAO.DAOFactory.DAOFactory;
import com.Dekanenko.DAO.DAOInterfaces.CarDAO;
import com.Dekanenko.DAO.entity.Car;
import com.Dekanenko.exceptions.DAOException;
import org.junit.jupiter.api.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static com.Dekanenko.DAO.queries.MySQLQuery.CarQuery.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class CarDAOTest {
    private static CarDAO carDAO;
    private Connection mockConnection;
    private PreparedStatement mockPreparedStatement;
    private ResultSet mockResultSet;

    @BeforeAll
    public static void setup(){
        System.err.close();
        System.setErr(System.out);
        System.setProperty("database", "MySQL");
        carDAO = DAOFactory.getInstance().getCarDAO();
    }

    @BeforeEach
    public void eachSetup(){
        mockConnection = mock(Connection.class);
        mockPreparedStatement = mock(PreparedStatement.class);
        mockResultSet = mock(ResultSet.class);
    }

    @AfterEach
    public void close(){
        DBUtils.close(mockConnection);
    }

    @Test
    public void getCarTest() throws SQLException {
        Car expectedCar = carCreator(1, "brand1", "A", "name1", 100, false, false);

        when(mockConnection.prepareStatement(GET_CAR_BY_ID)).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeQuery()).thenReturn(mockResultSet);
        when(mockResultSet.next()).thenReturn(true);

        mockCarResultSet(mockResultSet, expectedCar);

        Car actualCar = carDAO.getCar(mockConnection, expectedCar.getId());

        Assertions.assertEquals(expectedCar, actualCar);
    }

    @Test
    public void getCarNullTest() throws SQLException {
        when(mockConnection.prepareStatement(GET_CAR_BY_ID)).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeQuery()).thenReturn(mockResultSet);
        when(mockResultSet.next()).thenReturn(false);

        Car actualCar = carDAO.getCar(mockConnection, 1);

        Assertions.assertTrue(actualCar.getBrand() == null);
    }

    @Test
    public void getAllCarsTest() throws SQLException, DAOException {
        Car car1 = carCreator(1, "brand1", "A", "name1", 100, false, false);
        Car car2 = carCreator(2, "brand2", "B", "name2", 100, true, true);
        List<Car> expectedCarList = new ArrayList<>();
        expectedCarList.add(car1);
        expectedCarList.add(car2);

        when(mockConnection.prepareStatement(SELECT_ALL_CARS)).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeQuery()).thenReturn(mockResultSet);
        when(mockResultSet.next()).thenReturn(true).thenReturn(true).thenReturn(false);

        when(mockResultSet.getInt("id")).thenReturn(expectedCarList.get(0).getId()).thenReturn(expectedCarList.get(1).getId());
        when(mockResultSet.getString("brand")).thenReturn(expectedCarList.get(0).getBrand()).thenReturn(expectedCarList.get(1).getBrand());
        when(mockResultSet.getString("class")).thenReturn(expectedCarList.get(0).getQualityClass()).thenReturn(expectedCarList.get(1).getQualityClass());
        when(mockResultSet.getString("name")).thenReturn(expectedCarList.get(0).getName()).thenReturn(expectedCarList.get(1).getName());
        when(mockResultSet.getDouble("cost")).thenReturn(expectedCarList.get(0).getCost()).thenReturn(expectedCarList.get(1).getCost());
        when(mockResultSet.getBoolean("used")).thenReturn(expectedCarList.get(0).isUsed()).thenReturn(expectedCarList.get(1).isUsed());
        when(mockResultSet.getBoolean("damaged")).thenReturn(expectedCarList.get(0).isDamaged()).thenReturn(expectedCarList.get(1).isDamaged());

        List<Car> actualCarList = carDAO.getAllCars(mockConnection);

        Assertions.assertEquals(expectedCarList, actualCarList);
    }

    @Test
    public void getAllCarsDAOExceptionTest() throws SQLException{
        when(mockConnection.prepareStatement(SELECT_ALL_CARS)).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeQuery()).thenReturn(mockResultSet);
        when(mockResultSet.next()).thenReturn(false);

        Exception exception = Assertions.assertThrows(DAOException.class, ()->{
            carDAO.getAllCars(mockConnection);
        });

        String actualMessage = exception.getMessage();
        String expectedMessage = "No cars";
        Assertions.assertTrue(expectedMessage.contains(actualMessage));
    }

    @Test
    public void searchCarsTest() throws SQLException, DAOException {
        Car car = carCreator(1, "brand1", "A", "name1", 100, false, false);
        List<Car> expectedCarList = new ArrayList<>();
        expectedCarList.add(car);

        when(mockConnection.prepareStatement(SEARCH_CAR)).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeQuery()).thenReturn(mockResultSet);
        when(mockResultSet.next()).thenReturn(true).thenReturn(false);

        mockCarResultSet(mockResultSet, car);
        List<Car> actualCarList = carDAO.searchCars(mockConnection, car.getBrand(),
                car.getQualityClass(), "", "");

        Assertions.assertEquals(expectedCarList, actualCarList);
    }

    private Car carCreator(int id, String brand, String qualityClass, String name, double cost, boolean used, boolean damaged){
        Car car = new Car();
        car.setId(id);
        car.setBrand(brand);
        car.setQualityClass(qualityClass);
        car.setName(name);
        car.setCost(cost);
        car.setUsed(used);
        car.setDamaged(damaged);
        return car;
    }

    private void mockCarResultSet(ResultSet rs, Car car) throws SQLException {
        when(mockResultSet.getInt("id")).thenReturn(car.getId());
        when(mockResultSet.getString("brand")).thenReturn(car.getBrand());
        when(mockResultSet.getString("class")).thenReturn(car.getQualityClass());
        when(mockResultSet.getString("name")).thenReturn(car.getName());
        when(mockResultSet.getDouble("cost")).thenReturn(car.getCost());
        when(mockResultSet.getBoolean("used")).thenReturn(car.isUsed());
        when(mockResultSet.getBoolean("damaged")).thenReturn(car.isDamaged());
        when(mockResultSet.getDouble("repairCost")).thenReturn(car.getRepairCost());
    }

}
