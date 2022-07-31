package com.Dekanenko;

import com.Dekanenko.DAO.DAOFactory.DAOFactory;
import com.Dekanenko.DAO.DAOInterfaces.OrderDAO;
import com.Dekanenko.DAO.entity.Order;
import com.Dekanenko.exceptions.DAOException;
import org.junit.jupiter.api.*;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import static com.Dekanenko.DAO.queries.MySQLQuery.OrderQuery.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class OrderDAOTest {

    private static OrderDAO orderDAO;
    private Connection mockConnection;
    private PreparedStatement mockPreparedStatement;
    private ResultSet mockResultSet;

    @BeforeAll
    public static void setup(){
        System.err.close();
        System.setErr(System.out);
        System.setProperty("database", "MySQL");
        orderDAO = DAOFactory.getInstance().getOrderDAO();
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
    public void insertOrderTrueTest() throws SQLException {
        Order order = orderMaker(1, 1, "user1", 1,
                1, false, 1, "2022-12-12", "2022-12-14",
                "2022-12-14", 100, "message");

        when(mockConnection.prepareStatement(INSERT_ORDER, Statement.RETURN_GENERATED_KEYS)).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.getGeneratedKeys()).thenReturn(mockResultSet);
        when(mockResultSet.next()).thenReturn(true);
        when(mockResultSet.getInt(1)).thenReturn(order.getId());

        int actualId = orderDAO.insertOrder(mockConnection, order);

        Assertions.assertEquals(order.getId(), actualId);
    }

    @Test
    public void insertOrderFalseTest() throws SQLException {
        Order order = orderMaker(1, 1, "user1", 1,
                1, false, 1, "2022-12-12", "2022-12-14",
                "2022-12-14", 100, "message");

        when(mockConnection.prepareStatement(INSERT_ORDER, Statement.RETURN_GENERATED_KEYS)).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.getGeneratedKeys()).thenReturn(mockResultSet);
        when(mockResultSet.next()).thenReturn(false);
        when(mockResultSet.getInt(1)).thenReturn(order.getId());

        int actualId = orderDAO.insertOrder(mockConnection, order);
        int expectedId = -1;
        Assertions.assertEquals(expectedId, actualId);
    }

    @Test
    public void getOrderTest() throws SQLException {
        Map<Integer,String> actualStatusMap = new TreeMap<>();
        Map<Integer, String> expectedStatusMap = new TreeMap<>();
        expectedStatusMap.put(1, "in process");

        Order expectedOrder = orderMaker(1, 1, "user1", 1,
                1, false, 1, "2022-12-12", "2022-12-14",
                "2022-12-14", 100, "message");

        when(mockConnection.prepareStatement(GET_ORDER)).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeQuery()).thenReturn(mockResultSet);
        when(mockResultSet.next()).thenReturn(true);
        mockOrderResultSet(mockResultSet, expectedOrder);

        when(mockResultSet.getString("status_name")).thenReturn("in process");

        Order actualOrder = orderDAO.getOrder(mockConnection, 1, actualStatusMap);
        actualOrder.setUserLogin(expectedOrder.getUserLogin());
        actualOrder.setUserId(expectedOrder.getUserId());

        Assertions.assertEquals(expectedOrder, actualOrder);
        Assertions.assertEquals(expectedStatusMap, actualStatusMap);
    }

    @Test
    public void getOrderNullTest() throws SQLException {
        Map<Integer,String> actualStatusMap = new TreeMap<>();

        when(mockConnection.prepareStatement(GET_ORDER)).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeQuery()).thenReturn(mockResultSet);
        when(mockResultSet.next()).thenReturn(false);

        Order actualOrder = orderDAO.getOrder(mockConnection, 1, actualStatusMap);
        Order expectedOrder = null;

        Assertions.assertEquals(expectedOrder, actualOrder);
    }

    @Test
    public void getUserOrdersTest() throws SQLException, DAOException {
        List<Integer> expectedOrderIdList = new ArrayList<>();
        expectedOrderIdList.add(1);
        expectedOrderIdList.add(2);

        when(mockConnection.prepareStatement(GET_USER_ORDERS)).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeQuery()).thenReturn(mockResultSet);
        when(mockResultSet.next()).thenReturn(true).thenReturn(true).thenReturn(false);
        when(mockResultSet.getInt(2)).thenReturn(expectedOrderIdList.get(0)).thenReturn(expectedOrderIdList.get(1));

        List<Integer> actualOrderIdList = orderDAO.getUserOrders(mockConnection, 1);
        Assertions.assertEquals(expectedOrderIdList, actualOrderIdList);
    }

    @Test
    public void getUserOrdersDAOExceptionTest() throws SQLException {

        when(mockConnection.prepareStatement(GET_USER_ORDERS)).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeQuery()).thenReturn(mockResultSet);
        when(mockResultSet.next()).thenReturn(false);

        Exception exception = Assertions.assertThrows(DAOException.class, ()->{
            orderDAO.getUserOrders(mockConnection, 1);
        });

        String expectedMessage = "No orders for user";
        String actualMessage = exception.getMessage();

        Assertions.assertTrue(expectedMessage.contains(actualMessage));
    }

    @Test
    public void getUserIdFromOrderByRoleTest() throws SQLException {
        int expectedId = 1;
        when(mockConnection.prepareStatement(GET_USER_ID_BY_ROLE)).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeQuery()).thenReturn(mockResultSet);
        when(mockResultSet.next()).thenReturn(true);
        when(mockResultSet.getInt("user_id")).thenReturn(expectedId);

        int actualId = orderDAO.getUserIdFromOrderByRole(mockConnection, 0, 0);

        Assertions.assertEquals(expectedId, actualId);
    }

    @Test
    public void getUserIdFromOrderByRoleNullTest() throws SQLException {
        int expectedId = 0;
        when(mockConnection.prepareStatement(GET_USER_ID_BY_ROLE)).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeQuery()).thenReturn(mockResultSet);
        when(mockResultSet.next()).thenReturn(false);

        int actualId = orderDAO.getUserIdFromOrderByRole(mockConnection, 0, 0);

        Assertions.assertEquals(expectedId, actualId);
    }

    @Test
    public void getOrderIdByUserIdTest() throws SQLException, DAOException {
        int expectedId = 1;
        when(mockConnection.prepareStatement(GET_ORDER_BY_USER_ID)).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeQuery()).thenReturn(mockResultSet);
        when(mockResultSet.next()).thenReturn(true);
        when(mockResultSet.getInt("order_id")).thenReturn(expectedId);

        int actualId = orderDAO.getOrderIdByUserId(mockConnection, 0);

        Assertions.assertEquals(expectedId, actualId);
    }

    @Test
    public void getOrderIdByUserIdDAOExceptionTest() throws SQLException, DAOException {
        when(mockConnection.prepareStatement(GET_ORDER_BY_USER_ID)).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeQuery()).thenReturn(mockResultSet);
        when(mockResultSet.next()).thenReturn(false);

        Exception exception = Assertions.assertThrows(DAOException.class, ()->{
            orderDAO.getOrderIdByUserId(mockConnection, 0);
        });

        String expectedMessage = "No order for user";
        String actualMessage = exception.getMessage();

        Assertions.assertTrue(expectedMessage.contains(actualMessage));
    }

    @Test
    public void getAllUsersAndOrdersTest() throws SQLException, DAOException {
        Map<Integer, Integer> expectedMap = new TreeMap<>();
        expectedMap.put(1, 2);
        expectedMap.put(2, 1);

        when(mockConnection.prepareStatement(GET_ALL_ORDERS)).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeQuery()).thenReturn(mockResultSet);
        when(mockResultSet.next()).thenReturn(true).thenReturn(true).thenReturn(false);

        when(mockResultSet.getInt("order_id")).thenReturn(2).thenReturn(1);
        when(mockResultSet.getInt("user_id")).thenReturn(1).thenReturn(2);

        Map<Integer, Integer> actualMap = orderDAO.getAllUsersAndOrders(mockConnection);

        Assertions.assertEquals(expectedMap, actualMap);
    }

    @Test
    public void getAllUsersAndOrdersDAOExceptionTest() throws SQLException, DAOException {

        when(mockConnection.prepareStatement(GET_ALL_ORDERS)).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeQuery()).thenReturn(mockResultSet);
        when(mockResultSet.next()).thenReturn(false);

        Exception exception = Assertions.assertThrows(DAOException.class, ()->{
            orderDAO.getAllUsersAndOrders(mockConnection);
        });

        String expectedMessage = "No info from user_order table";
        String actualMessage = exception.getMessage();

        Assertions.assertEquals(expectedMessage, actualMessage);
    }

    @Test
    public void orderSearchTest() throws SQLException{
        Map<Integer,String> actualStatusMap = new TreeMap<>();
        Map<Integer, String> expectedStatusMap = new TreeMap<>();
        expectedStatusMap.put(1, "in process");
        List<Order> expectedList = new ArrayList<>();

        expectedList.add(orderMaker(1, 1, "user1", 1,
                1, false, 1, "2022-12-12", "2022-12-14",
                "2022-12-14", 100, "message"));

        when(mockConnection.prepareStatement(SEARCH_ORDER)).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeQuery()).thenReturn(mockResultSet);
        when(mockResultSet.next()).thenReturn(true).thenReturn(false);
        mockOrderResultSet(mockResultSet, expectedList.get(0));

        when(mockResultSet.getString("status_name")).thenReturn("in process");

        List<Order> actualList = orderDAO.orderSearch(mockConnection, actualStatusMap, "", "", "", "");
        actualList.get(0).setUserLogin(expectedList.get(0).getUserLogin());
        actualList.get(0).setUserId(expectedList.get(0).getUserId());

        Assertions.assertEquals(expectedList, actualList);
        Assertions.assertEquals(expectedStatusMap, actualStatusMap);
    }

    private Order orderMaker(int id, int userId, String userLogin, int passportId, int carId,
                             boolean driverOption, int statusId, String firstDate, String lastDate,
                             String returnDate, double cost, String message){

        Order order = new Order();

        order.setId(id);
        order.setUserId(userId);
        order.setUserLogin(userLogin);
        order.setPassportId(passportId);
        order.setCarId(carId);
        order.setDriverOption(driverOption);
        order.setStatusId(statusId);
        order.setFirstDate(firstDate);
        order.setLastDate(lastDate);
        order.setReturnDate(returnDate);
        order.setCost(cost);
        order.setMessage(message);

        return order;
    }

    private void mockOrderResultSet(ResultSet rs, Order order) throws SQLException {
        when(rs.getInt("id")).thenReturn(order.getId());
        when(rs.getBoolean("driver")).thenReturn(order.getDriverOption());
        when(rs.getString("first_date")).thenReturn(order.getFirstDate());
        when(rs.getString("last_date")).thenReturn(order.getLastDate());
        when(rs.getString("return_date")).thenReturn(order.getReturnDate());
        when(rs.getDouble("cost")).thenReturn(order.getCost());
        when(rs.getInt("car_id")).thenReturn(order.getCarId());
        when(rs.getInt("status_id")).thenReturn(order.getStatusId());
        when(rs.getInt("passport_id")).thenReturn(order.getPassportId());
        when(rs.getString("message")).thenReturn(order.getMessage());
    }
}
