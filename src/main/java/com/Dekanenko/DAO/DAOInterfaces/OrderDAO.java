package com.Dekanenko.DAO.DAOInterfaces;

import com.Dekanenko.DAO.entity.Order;
import com.Dekanenko.exceptions.DAOException;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public interface OrderDAO {

    public int insertOrder(Connection connection, Order order) throws SQLException;
    public boolean insertToUserOrder(Connection connection, int userId, int orderId) throws SQLException;
    public List<Integer> getUserOrders(Connection connection, int userId) throws SQLException, DAOException;
    public int getUserIdFromOrderByRole(Connection connection, int orderId, int role) throws SQLException;
    public int getOrderIdByUserId(Connection connection, int userId) throws SQLException, DAOException;
    public Map<Integer, Integer> getAllUsersAndOrders(Connection connection) throws SQLException, DAOException;
    public Order getOrder(Connection connection, int orderId, Map<Integer, String> map) throws SQLException;
    public boolean changeOrderStatus(Connection connection, int orderId, int statusId) throws SQLException;
    public boolean changeOrderMessage(Connection connection, int orderId, String message) throws SQLException;
    public boolean deleteOrder(Connection connection, int orderId) throws SQLException;
    public boolean addReturnDataToOrder(Connection connection, int orderId, String date) throws SQLException;
    public List<Order> orderSearch(Connection connection, Map<Integer, String> statusMap,
                                   String status, String firstDate, String lastDate, String driverOption) throws SQLException;
}
