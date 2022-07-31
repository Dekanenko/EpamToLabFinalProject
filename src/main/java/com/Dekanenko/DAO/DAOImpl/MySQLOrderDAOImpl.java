package com.Dekanenko.DAO.DAOImpl;

import com.Dekanenko.DAO.DAOInterfaces.OrderDAO;
import com.Dekanenko.DAO.entity.Order;
import com.Dekanenko.DBUtils;
import com.Dekanenko.exceptions.DAOException;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import static com.Dekanenko.DAO.queries.MySQLQuery.OrderQuery.*;

public class MySQLOrderDAOImpl implements OrderDAO {

    private static MySQLOrderDAOImpl instance;

    private MySQLOrderDAOImpl() {
    }

    public static synchronized MySQLOrderDAOImpl getInstance(){
        if(instance == null)
            instance = new MySQLOrderDAOImpl();
        return instance;
    }

    @Override
    public int insertOrder(Connection connection, Order order) throws SQLException {
        int k = 0;
        PreparedStatement preparedStatement = null;
        ResultSet rs = null;

        try {
            preparedStatement = connection.prepareStatement(INSERT_ORDER, Statement.RETURN_GENERATED_KEYS);

            preparedStatement.setBoolean(++k, order.getDriverOption());
            preparedStatement.setString(++k, order.getFirstDate());
            preparedStatement.setString(++k, order.getLastDate());
            preparedStatement.setDouble(++k, order.getCost());
            preparedStatement.setInt(++k, order.getCarId());
            preparedStatement.setInt(++k, order.getStatusId());
            preparedStatement.setInt(++k, order.getPassportId());

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
    public boolean insertToUserOrder(Connection connection, int userId, int orderId) throws SQLException {
        int k = 0;
        PreparedStatement preparedStatement = null;

        try {
            preparedStatement = connection.prepareStatement(INSERT_USER_ORDER);
            preparedStatement.setInt(++k, userId);
            preparedStatement.setInt(++k, orderId);

            preparedStatement.execute();
        }finally {
            if(preparedStatement != null)
                DBUtils.preparedStatementClose(preparedStatement);
        }

        return true;
    }

    @Override
    public List<Integer> getUserOrders(Connection connection, int userId) throws SQLException, DAOException {
        List<Integer> list = new ArrayList<>();
        PreparedStatement preparedStatement = null;
        ResultSet rs = null;

        try {
            preparedStatement = connection.prepareStatement(GET_USER_ORDERS);
            preparedStatement.setInt(1, userId);
            rs = preparedStatement.executeQuery();

            while (rs.next()){
                list.add(rs.getInt(2));
            }
            if(list.size() == 0)
                throw new DAOException("No orders for user");
        }finally {
            if(rs != null)
                DBUtils.resultSetClose(rs);

            if(preparedStatement != null)
                DBUtils.preparedStatementClose(preparedStatement);
        }

        return list;
    }

    @Override
    public int getUserIdFromOrderByRole(Connection connection, int orderId, int role) throws SQLException {
        int k = 0;
        PreparedStatement preparedStatement = null;
        ResultSet rs = null;

        try {
            preparedStatement = connection.prepareStatement(GET_USER_ID_BY_ROLE);
            preparedStatement.setInt(++k, role);
            preparedStatement.setInt(++k, orderId);
            rs = preparedStatement.executeQuery();
            if(rs.next()){
                return rs.getInt("user_id");
            }
        }finally {
            if(rs != null)
                DBUtils.resultSetClose(rs);

            if(preparedStatement != null)
                DBUtils.preparedStatementClose(preparedStatement);
        }

        return 0;
    }

    @Override
    public int getOrderIdByUserId(Connection connection, int userId) throws SQLException, DAOException{
        PreparedStatement preparedStatement = null;
        ResultSet rs = null;
        try {
            preparedStatement = connection.prepareStatement(GET_ORDER_BY_USER_ID);
            preparedStatement.setInt(1, userId);
            rs = preparedStatement.executeQuery();
            if(rs.next()){
                return rs.getInt("order_id");
            }else{
                throw new DAOException("No order for user");
            }
        }finally {
            if(rs != null)
                DBUtils.resultSetClose(rs);

            if(preparedStatement != null)
                DBUtils.preparedStatementClose(preparedStatement);
        }

    }

    @Override
    public Map<Integer, Integer> getAllUsersAndOrders(Connection connection) throws SQLException, DAOException {
        Map<Integer, Integer> map = new TreeMap<>();
        PreparedStatement preparedStatement = null;
        ResultSet rs = null;

        try {
            preparedStatement = connection.prepareStatement(GET_ALL_ORDERS);
            rs = preparedStatement.executeQuery();

            while (rs.next()){
                map.put(rs.getInt("order_id"), rs.getInt("user_id"));
            }
            if(map.size() == 0)
                throw new DAOException("No info from user_order table");
        }finally {
            if(rs != null)
                DBUtils.resultSetClose(rs);

            if(preparedStatement != null)
                DBUtils.preparedStatementClose(preparedStatement);
        }

        return map;
    }

    @Override
    public Order getOrder(Connection connection, int orderId, Map<Integer, String> map) throws SQLException {
        PreparedStatement preparedStatement = null;
        ResultSet rs = null;

        try {
            preparedStatement = connection.prepareStatement(GET_ORDER);
            preparedStatement.setInt(1, orderId);
            rs = preparedStatement.executeQuery();
            if(rs.next()){
                Order order = orderCreator(rs);

                //map is used for getting statuses and their id from status table
                map.put(order.getStatusId(), rs.getString("status_name"));
                return order;
            }
        }finally {
            if(rs != null)
                DBUtils.resultSetClose(rs);

            if(preparedStatement != null)
                DBUtils.preparedStatementClose(preparedStatement);
        }

        return null;
    }

    @Override
    public boolean changeOrderStatus(Connection connection, int orderId, int statusId) throws SQLException {
        int k = 0;
        PreparedStatement preparedStatement = null;

        try {
            preparedStatement = connection.prepareStatement(CHANGE_ORDER_STATUS);
            preparedStatement.setInt(++k, statusId);
            preparedStatement.setInt(++k, orderId);
            preparedStatement.executeUpdate();
        }finally {
            if(preparedStatement != null)
                DBUtils.preparedStatementClose(preparedStatement);
        }

        return true;
    }

    @Override
    public boolean changeOrderMessage(Connection connection, int orderId, String message) throws SQLException {
        int k = 0;
        PreparedStatement preparedStatement = null;

        try {
            preparedStatement = connection.prepareStatement(CHANGE_ORDER_MESSAGE);
            preparedStatement.setString(++k, message);
            preparedStatement.setInt(++k, orderId);
            preparedStatement.executeUpdate();
        }finally {
            if(preparedStatement != null)
                DBUtils.preparedStatementClose(preparedStatement);
        }

        return true;
    }

    @Override
    public boolean deleteOrder(Connection connection, int orderId) throws SQLException {
        PreparedStatement preparedStatement = null;

        try {
            preparedStatement = connection.prepareStatement(DELETE_ORDER);
            preparedStatement.setInt(1, orderId);
            preparedStatement.execute();
        }finally {
            if(preparedStatement != null)
                DBUtils.preparedStatementClose(preparedStatement);
        }

        return true;
    }

    @Override
    public boolean addReturnDataToOrder(Connection connection, int orderId,  String date) throws SQLException {
        int k = 0;
        PreparedStatement preparedStatement = null;

        try {
            preparedStatement = connection.prepareStatement(ADD_RETURN_DATE);
            preparedStatement.setString(++k, date);
            preparedStatement.setInt(++k, orderId);
            preparedStatement.executeUpdate();
        }finally {
            if(preparedStatement != null)
                DBUtils.preparedStatementClose(preparedStatement);
        }

        return true;
    }

    @Override
    public List<Order> orderSearch(Connection connection, Map<Integer, String> statusMap,
                                   String status, String firstDate, String lastDate, String driverOption) throws SQLException {

        int k = 0;
        List<Order> orderList = new ArrayList<>();
        PreparedStatement preparedStatement = connection.prepareStatement(SEARCH_ORDER);
        preparedStatement.setString(++k, "%"+firstDate+"%");
        preparedStatement.setString(++k, "%"+lastDate+"%");
        preparedStatement.setString(++k, "%"+status+"%");
        preparedStatement.setString(++k, "%"+driverOption+"%");

        ResultSet rs = preparedStatement.executeQuery();
        while (rs.next()){
            orderList.add(orderCreator(rs));
            statusMap.put(orderList.get(orderList.size()-1).getStatusId(), rs.getString("status_name"));
        }

        return orderList;
    }

    //method for constructing Order object using ResultSet
    private Order orderCreator(ResultSet rs) throws SQLException {
        String date;
        Order order = new Order();

        order.setId(rs.getInt("id"));
        order.setDriverOption(rs.getBoolean("driver"));

        date = rs.getString("first_date");
        order.setFirstDate(date.substring(0, 10));

        date = rs.getString("last_date");
        order.setLastDate(date.substring(0, 10));

        date = rs.getString("return_date");
        if(date!=null)
            order.setReturnDate(date.substring(0, 10));

        order.setCost(rs.getDouble("cost"));
        order.setCarId(rs.getInt("car_id"));
        order.setStatusId(rs.getInt("status_id"));
        order.setPassportId(rs.getInt("passport_id"));
        order.setMessage(rs.getString("message"));

        return order;
    }
}
