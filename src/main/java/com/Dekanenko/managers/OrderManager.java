package com.Dekanenko.managers;

import com.Dekanenko.DAO.DAOFactory.DAOFactory;
import com.Dekanenko.DAO.DAOInterfaces.CarDAO;
import com.Dekanenko.DAO.DAOInterfaces.OrderDAO;
import com.Dekanenko.DAO.DAOInterfaces.PassportDAO;
import com.Dekanenko.DAO.DAOInterfaces.UserDAO;
import com.Dekanenko.DAO.entity.Car;
import com.Dekanenko.DAO.entity.Order;
import com.Dekanenko.DAO.entity.Passport;
import com.Dekanenko.DAO.entity.User;
import com.Dekanenko.DBUtils;
import com.Dekanenko.exceptions.DAOException;
import com.Dekanenko.helpClass.Helper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class OrderManager {

    private static final Logger log = LogManager.getLogger(OrderManager.class);

    private static OrderManager instance;
    private static DBUtils dbUtils;
    private static UserDAO userDAO;
    private static CarDAO carDAO;
    private static PassportDAO passportDAO;
    private static OrderDAO orderDAO;


    public static synchronized OrderManager getInstance(){
        if(instance == null)
            instance = new OrderManager();
        return instance;
    }

    private OrderManager() {
        dbUtils = DBUtils.getInstance();
        DAOFactory daoFactory = DAOFactory.getInstance();
        userDAO = daoFactory.getUserDAO();
        carDAO = daoFactory.getCarDAO();
        passportDAO = daoFactory.getPassportDAO();
        orderDAO = daoFactory.getOrderDAO();
    }

    public void insertOrder(HttpServletRequest req, HttpServletResponse resp){
        Order order = new Order();
        Connection connection = null;
        int passportId = Integer.parseInt(req.getParameter("passportId"));
        double orderCost;
        try {

            //setting order
            orderSet(order, req);
            log.debug("method:insertOrder\ttransaction started");
            connection = dbUtils.getConnection();
            connection.setAutoCommit(false);
            connection.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);

            //create new passport if it is not attached to user
            if(passportId == 0){
                Passport passport = Helper.passportCreator(req);
                log.debug("Create new passport");

                //add passport to passport table
                passportId = passportDAO.addPassport(connection, passport);
            }

            //add passport to order
            log.debug("Set passport id to order");
            order.setPassportId(passportId);

            //change user balance
            log.debug("Change user balance");
            orderCost = (-1) * order.getCost();
            userDAO.changeUserCash(connection, order.getUserId(), orderCost);

            //block chosen car
            log.debug("Block chosen car");
            carDAO.changeCarUsage(connection, order.getCarId(), true);

            //add order
            log.debug("Insert order");
            order.setStatusId(1);
            int orderId = orderDAO.insertOrder(connection, order);

            //add info about user and order to user_order table
            log.debug("Add info to user_order table");
            if(orderId !=0 && order.getUserId()!=0)
                orderDAO.insertToUserOrder(connection, order.getUserId(), orderId);
            else
                throw new SQLException("Cannot insert order");

            //update session
            log.debug("Update session");
            User currentUser = userDAO.getUser(connection, order.getUserId());

            if(currentUser.getCash() < 0){
                throw new SQLException("Invalid balance");
            }

            req.getSession().setAttribute("currentUser", currentUser);
            if(!currentUser.isAffordable())
                throw new SQLException("User banned");

            log.debug("method:insertOrder\ttransaction commit");
            connection.commit();
            resp.sendRedirect("/managePage");
        } catch (ParseException ex) {
            log.error(ex.getMessage());
        } catch (SQLException ex) {
            if(connection != null)
                dbUtils.rollBack(connection);

            log.error(ex.getMessage());
            req.setAttribute("errorMessage", "Cannot make an order");
            Helper.forwarder(req, resp, "/errorPage.jsp");
        } catch (IOException ex) {
            log.error(ex.getMessage());
        } finally {
            dbUtils.close(connection);
        }
    }

    public void showUserOrders(HttpServletRequest req, HttpServletResponse resp) {
        int userId = Integer.parseInt(req.getParameter("userId"));
        List<Integer> orderIdList = new ArrayList<>();
        List<Order> orderList = new ArrayList<>();
        Map<Integer, String > statusMap = new TreeMap<>();
        Map<Integer, Car> carMap = new TreeMap<>();
        try(Connection connection = dbUtils.getConnection()){

            //obtain all user orders id
            log.debug("Get all orders id from user_order");
            orderIdList = orderDAO.getUserOrders(connection, userId);

            //get all orders and their statuses
            log.debug("Get all needed orders and statuses names");
            for(int id : orderIdList){
                orderList.add(orderDAO.getOrder(connection, id, statusMap));
            }

            //get all needed cars
            log.debug("Get cars info");
            for(Order order : orderList){
                Car car = carDAO.getCar(connection, order.getCarId());
                carMap.put(order.getCarId(), car);
            }

            //put order list, status map and car map to request
            req.setAttribute("orderList", orderList);
            req.setAttribute("statusMap", statusMap);
            req.setAttribute("carMap", carMap);

            //update session
            log.debug("Session update");
            User user = userDAO.getUser(connection, userId);
            req.getSession().setAttribute("currentUser", user);

            req.getRequestDispatcher("/clientPage.jsp").forward(req, resp);
        }catch (SQLException ex){
            log.error(ex.getMessage());
        } catch (ServletException ex) {
            log.error(ex.getMessage());
        } catch (IOException ex) {
            log.error(ex.getMessage());
        } catch (DAOException ex) {
            log.error(ex.getMessage());

            //just to show on view that it is a problem. Message will be internationalized
            req.setAttribute("noOrdersError", 1);
            Helper.forwarder(req, resp, "/clientPage.jsp");
        }
    }

    public void showAllOrders(HttpServletRequest req, HttpServletResponse resp) {

        Map<Integer, Integer> orderUser = new TreeMap<>();

        List<Order> orderList = new ArrayList<>();
        List<User> driverList = new ArrayList<>();
        Map<Integer, String > statusMap = new TreeMap<>();
        Map<Integer, Car> carMap = new TreeMap<>();
        try(Connection connection = dbUtils.getConnection()){

            //get all users id and orders id
            log.debug("Get all users and orders id from user_order");
            orderUser = orderDAO.getAllUsersAndOrders(connection);

            //get all orders and their statuses names
            log.debug("Get all needed orders and statuses names");
            for(int id : orderUser.keySet()){
                orderList.add(orderDAO.getOrder(connection, id, statusMap));

                //put user info into order obj
                User user = userDAO.getUser(connection, orderUser.get(id));
                orderList.get(orderList.size()-1).setUserId(user.getId());
                orderList.get(orderList.size()-1).setUserLogin(user.getLogin());
            }

            //get all drivers
            log.debug("Get all drivers");
            driverList = userDAO.getAllDrivers(connection);

            //get ordered cars
            log.debug("Get ordered cars");
            for(Order order : orderList){
                Car car = carDAO.getCar(connection, order.getCarId());
                carMap.put(order.getCarId(), car);
            }

            int counter = 0;
            for(User user : driverList){
                if(!user.isAffordable())
                    counter++;
            }
            if(counter == driverList.size()){
                driverList = new ArrayList<>();
            }

            //put all obtained info to request
            req.setAttribute("orderList", orderList);
            req.setAttribute("statusMap", statusMap);
            req.setAttribute("carMap", carMap);
            req.setAttribute("driverList", driverList);

            req.getRequestDispatcher("/managerPage.jsp").forward(req, resp);
        }catch (SQLException ex){
            log.error(ex.getMessage());
        } catch (ServletException ex) {
            log.error(ex.getMessage());
        } catch (IOException ex) {
            log.error(ex.getMessage());
        } catch (DAOException ex) {
            log.error(ex.getMessage());

            //just to show on view that it is a problem. Message will be internationalized
            req.setAttribute("noOrdersError", 1);
            Helper.forwarder(req, resp, "/managerPage.jsp");
        }
    }

    public void showDriverOrder(HttpServletRequest req, HttpServletResponse resp) {
        int driverId = Integer.parseInt(req.getParameter("driverId"));
        int orderId = 0;
        int userId = 0;
        Car car = new Car();
        Order order = new Order();
        Map<Integer, String> statusMap = new TreeMap<>();

        try (Connection connection = dbUtils.getConnection()) {

            //get order id where current driver appears
            log.debug("Get driver order id");
            orderId = orderDAO.getOrderIdByUserId(connection, driverId);
            if(orderId == 0)
                throw new SQLException("Cannot find order for driver");

            //get needed order
            log.debug("Get order");
            order = orderDAO.getOrder(connection, orderId, statusMap);

            //get ordered car
            log.debug("Get ordered car");
            car = carDAO.getCar(connection, order.getCarId());

            //get client info
            log.debug("Get client");
            userId = orderDAO.getUserIdFromOrderByRole(connection, orderId, 4);
            User user = userDAO.getUser(connection, userId);
            order.setUserLogin(user.getLogin());

            //put order and car to request
            req.setAttribute("order", order);
            req.setAttribute("car", car);

            req.getRequestDispatcher("/driverPage.jsp").forward(req, resp);
        } catch (SQLException ex){
            log.error(ex.getMessage());
        } catch (DAOException ex){
            log.error(ex.getMessage());

            //just to show on view that it is a problem. Message will be internationalized
            req.setAttribute("orderError", 1);
            Helper.forwarder(req, resp, "/driverPage.jsp");
        } catch (ServletException ex) {
            log.error(ex.getMessage());
        } catch (IOException ex) {
            log.error(ex.getMessage());
        }
    }


    public void orderDeny(HttpServletRequest req, HttpServletResponse resp) {
        int denyStatus = 3;//denied
        int orderId = Integer.parseInt(req.getParameter("orderId"));
        int carId = Integer.parseInt(req.getParameter("carId"));
        double orderCost = Double.parseDouble(req.getParameter("orderCost"));
        int userId = Integer.parseInt(req.getParameter("userId"));
        String message = req.getParameter("message");

        Connection connection = null;

        try {
            log.debug("method:orderDeny\ttransaction started");
            connection = dbUtils.getConnection();
            connection.setAutoCommit(false);
            connection.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);

            //update order status to denied
            log.debug("Update order status");
            orderDAO.changeOrderStatus(connection, orderId, denyStatus);

            //update denying message
            log.debug("Update order message");
            orderDAO.changeOrderMessage(connection, orderId, message);

            //free (unlock) ordered car
            log.debug("Free ordered car");
            carDAO.changeCarUsage(connection, carId, false);

            //return money to user
            log.debug("Return money to user");
            userDAO.changeUserCash(connection, userId, orderCost);

            log.debug("method:orderDeny\ttransaction commit");
            connection.commit();
            showAllOrders(req, resp);
        } catch (SQLException ex) {
            if(connection != null)
                dbUtils.rollBack(connection);

            log.error(ex.getMessage());
            req.setAttribute("errorMessage", "Deny failed");
            Helper.forwarder(req, resp, "/errorPage.jsp");
        } finally {
            dbUtils.close(connection);
        }
    }

    public void confirmOrderDeny(HttpServletRequest req, HttpServletResponse resp) {
        int orderId = Integer.parseInt(req.getParameter("orderId"));
        int passportId = Integer.parseInt(req.getParameter("orderPassport"));
        int userId = Integer.parseInt(req.getParameter("userId"));
        Connection connection = null;
        try{
            log.debug("method:confirmOrderDeny\ttransaction started");
            connection = dbUtils.getConnection();
            connection.setAutoCommit(false);
            connection.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);

            //delete order
            log.debug("Delete order");
            orderDAO.deleteOrder(connection, orderId);

            User user = userDAO.getUser(connection, userId);
            //if passport is not attached to user delete it
            if(user.getPassportId() != passportId){
                log.debug("Delete passport");
                passportDAO.deletePassport(connection, passportId);
            }

            log.debug("method:confirmOrderDeny\ttransaction commit");
            connection.commit();
            showUserOrders(req, resp);
        } catch (SQLException ex) {
            if(connection != null)
                dbUtils.rollBack(connection);

            log.error(ex.getMessage());
            req.setAttribute("errorMessage", "Deny confirm failed");
            Helper.forwarder(req, resp, "/errorPage.jsp");
        } finally {
            dbUtils.close(connection);
        }
    }

    public void acceptOrder(HttpServletRequest req, HttpServletResponse resp, int driverId) {
        int orderStatus = 2; //accepted
        int orderId = Integer.parseInt(req.getParameter("orderId"));
        Connection connection = null;

        try {
            log.debug("method:acceptOrder\ttransaction started");
            connection = dbUtils.getConnection();
            connection.setAutoCommit(false);
            connection.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);

            //if order was with driver option
            if(driverId != 0){
                //block chosen driver
                log.debug("Block chosen driver");
                userDAO.updateUserStatus(connection, driverId, false);
                //add driver id to user_order table
                log.debug("Attach chosen driver to order");
                orderDAO.insertToUserOrder(connection, driverId, orderId);
            }

            //change order status
            log.debug("Change order status");
            orderDAO.changeOrderStatus(connection, orderId, orderStatus);

            log.debug("method:acceptOrder\ttransaction commit");
            connection.commit();
            showAllOrders(req, resp);
        } catch (SQLException ex) {
            if(connection != null)
                dbUtils.rollBack(connection);

            log.error(ex.getMessage());
            req.setAttribute("errorMessage", "Accept failed");
            Helper.forwarder(req, resp, "/errorPage.jsp");
        } finally {
            dbUtils.close(connection);
        }

    }


    public void returnOrder(HttpServletRequest req, HttpServletResponse resp) {
        int orderStatus = 4;//finished
        int orderId = Integer.parseInt(req.getParameter("orderId"));
        String date = req.getParameter("returnDate");
        Connection connection = null;

        try {
            log.debug("method:returnOrder\ttransaction started");
            connection = dbUtils.getConnection();
            connection.setAutoCommit(false);
            connection.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);

            //Change order status
            log.debug("Change order status");
            orderDAO.changeOrderStatus(connection, orderId, orderStatus);

            //add date of return to order
            log.debug("Add date of car return to order");
            orderDAO.addReturnDataToOrder(connection, orderId, date);

            log.debug("method:returnOrder\ttransaction commit");
            connection.commit();
            showUserOrders(req, resp);
        } catch (SQLException ex) {
            if(connection != null)
                dbUtils.rollBack(connection);

            log.error(ex.getMessage());
            req.setAttribute("errorMessage", "Return failed");
            Helper.forwarder(req, resp, "/errorPage.jsp");
        } finally {
            dbUtils.close(connection);
        }
    }

    public void confirmFinishOrder(HttpServletRequest req, HttpServletResponse resp) {
        int driverId = 0;
        int orderId = Integer.parseInt(req.getParameter("orderId"));
        int carId = Integer.parseInt(req.getParameter("carId"));
        int userId = Integer.parseInt(req.getParameter("userId"));
        int passportId = Integer.parseInt(req.getParameter("passportId"));

        boolean damaged = Boolean.parseBoolean(req.getParameter("damaged"));
        boolean driverOption = Boolean.parseBoolean(req.getParameter("driverOption"));

        int fine = 0;
        if(req.getParameter("fine")!=null)
            fine = Integer.parseInt(req.getParameter("fine"));
        Connection connection = null;

        try {
            log.debug("method:confirmFinishOrder\ttransaction started");
            connection = dbUtils.getConnection();
            connection.setAutoCommit(false);
            connection.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);

            //unlock ordered car
            log.debug("Free ordered car");
            carDAO.changeCarUsage(connection, carId, false);

            //if car is damaged change its damaged status
            if(damaged){
                log.debug("Update damaged car ");
                carDAO.changeCarDamage(connection, carId, damaged);
            }

            //if order was with driver, free (unlock) the driver
            if(driverOption){
                log.debug("Get driver id");
                driverId = orderDAO.getUserIdFromOrderByRole(connection, orderId, 3);
                if(driverId == 0)
                    throw new SQLException("Cannot find order driver");

                log.debug("Free driver");
                userDAO.updateUserStatus(connection, driverId, true);
            }

            if(fine > 0){
                //if order was with driver option, fine goes to driver
                if(driverOption){
                    log.debug("Apply fine to driver");
                    userDAO.changeUserCash(connection, driverId, (-1)*fine);
                }else {
                    //if order was without driver option, fine goes to client
                    log.debug("Apply fine to user");
                    userDAO.changeUserCash(connection, userId, (-1)*fine);
                }
            }

            //delete order
            log.debug("Delete order");
            orderDAO.deleteOrder(connection, orderId);

            //delete passport if it is not attached to user
            User user = userDAO.getUser(connection, userId);
            if(user.getPassportId() != passportId){
                log.debug("Delete passport");
                passportDAO.deletePassport(connection, passportId);
            }

            log.debug("method:confirmFinishOrder\ttransaction commit");
            connection.commit();
            showAllOrders(req, resp);
        } catch (SQLException ex) {
            if(connection != null)
                dbUtils.rollBack(connection);

            log.error(ex.getMessage());
            req.setAttribute("errorMessage", "Finish confirm failed");
            Helper.forwarder(req, resp, "/errorPage.jsp");
        } finally {
            dbUtils.close(connection);
        }
    }

    public void orderSearch(HttpServletRequest req, HttpServletResponse resp){
        List<Order> orderList = new ArrayList<>();
        List<Order> newOrderList = new ArrayList<>();
        List<User> driverList = new ArrayList<>();
        Map<Integer, String> statusMap = new TreeMap<>();
        Map<Integer, Car> carMap = new TreeMap<>();
        User user;
        int userId;
        int userRoleId = 4;

        String orderStatus = req.getParameter("status");
        String firstDate = req.getParameter("firstDate");
        String lastDate = req.getParameter("lastDate");
        String driverOption = "";

        if(!req.getParameter("driver").equals("all")){
            driverOption = "1";
            if(!Boolean.parseBoolean(req.getParameter("driver")))
                driverOption = "0";
        }

        int option = Integer.parseInt(req.getParameter("option"));

        if(orderStatus.length()<1){
            orderStatus = "";
        }

        try(Connection connection = dbUtils.getConnection()){

            //get all needed orders
            log.debug("Search orders");
            orderList = orderDAO.orderSearch(connection, statusMap, orderStatus, firstDate, lastDate, driverOption);

            if(option == -1){
                //get users info
                for(Order order : orderList){
                    userId = orderDAO.getUserIdFromOrderByRole(connection, order.getId(), userRoleId);
                    user = userDAO.getUser(connection, userId);
                    order.setUserId(userId);
                    order.setUserLogin(user.getLogin());
                }
                //get all drivers
                log.debug("Get all drivers");
                driverList = userDAO.getAllDrivers(connection);

                req.setAttribute("driverList", driverList);
            }else {
                System.out.println(orderList.size());
                for(int i = 0; i<orderList.size(); i++){
                    userId = orderDAO.getUserIdFromOrderByRole(connection, orderList.get(i).getId(), userRoleId);
                    System.out.println(userId+"\t"+option);
                    if(userId == option){
                        newOrderList.add(orderList.get(i));
                    }
                }
                orderList = newOrderList;
            }

            //get ordered cars
            log.debug("Get ordered cars");
            for(Order order : orderList){
                Car car = carDAO.getCar(connection, order.getCarId());
                carMap.put(order.getCarId(), car);
            }

            //put all obtained info to request
            req.setAttribute("orderList", orderList);
            req.setAttribute("statusMap", statusMap);
            req.setAttribute("carMap", carMap);

            req.getRequestDispatcher("/managePage").forward(req, resp);

        }catch (SQLException ex){
            log.error(ex.getMessage());
        } catch (ServletException ex) {
            log.error(ex.getMessage());
        } catch (IOException ex) {
            log.error(ex.getMessage());
        }
    }

    //method set Order object using info from request
    private void orderSet(Order order, HttpServletRequest req) throws ParseException {
        order.setUserId(Integer.parseInt(req.getParameter("userId")));
        order.setDriverOption(Boolean.parseBoolean(req.getParameter("driver")));
        order.setUserLogin(req.getParameter("userLog"));
        order.setCarId(Integer.parseInt(req.getParameter("carId")));
        order.setFirstDate(req.getParameter("firstDate"));
        order.setLastDate(req.getParameter("lastDate"));
        order.setCost(Double.parseDouble(req.getParameter("cost")));
    }
}
