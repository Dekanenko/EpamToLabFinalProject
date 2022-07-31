package com.Dekanenko.managers;

import com.Dekanenko.DAO.DAOFactory.DAOFactory;
import com.Dekanenko.DAO.DAOInterfaces.CarDAO;
import com.Dekanenko.DAO.entity.Car;
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
import java.util.ArrayList;
import java.util.List;

public class CarManager {

    private static final Logger log = LogManager.getLogger(CarManager.class);

    private static CarManager instance;
    private static DBUtils dbUtils;
    private static CarDAO carDAO;

    public static synchronized CarManager getInstance(){
        if(instance == null)
            instance = new CarManager();
        return instance;
    }

    private CarManager() {
        dbUtils = DBUtils.getInstance();
        DAOFactory daoFactory = DAOFactory.getInstance();
        carDAO = daoFactory.getCarDAO();
    }

    public void executeRegistration(HttpServletRequest req, HttpServletResponse resp){
        Car car = new Car();
        carSet(car, req);

        try(Connection con = dbUtils.getConnection()){
            if(carDAO.insertCar(con, car)){
                resp.sendRedirect("/managePage");
            }
        } catch (SQLException ex) {
            log.error(ex.getMessage());
            req.setAttribute("errorMessage", "Cannot register car");
            Helper.forwarder(req, resp, "/errorPage.jsp");
        } catch (IOException ex) {
            log.error(ex.getMessage());
        }
    }

    public void executeGetCar(HttpServletRequest req, HttpServletResponse resp){
        int carId = Integer.parseInt(req.getParameter("carId"));
        try (Connection connection = dbUtils.getConnection()) {
            Car car = carDAO.getCar(connection, carId);
            if(car == null)
                throw new SQLException("Cannot get car");

            //put car into request
            req.setAttribute("car", car);
        } catch (SQLException ex) {
            log.error(ex.getMessage());
        }
    }

    public void executeCarSearch(HttpServletRequest req, HttpServletResponse resp){
        List<Car> carList = new ArrayList<>();
        String brand = req.getParameter("brand");
        String qualityClass = req.getParameter("qualityClass");
        String carDamageOption = "";
        String carUsageOption = "";

        if(!req.getParameter("carDamageOption").equals("all")){
            carDamageOption = "1";
            if(!Boolean.parseBoolean(req.getParameter("carDamageOption")))
                carDamageOption = "0";
        }

        if(!req.getParameter("carUsageOption").equals("all")){
            carUsageOption = "1";
            if(!Boolean.parseBoolean(req.getParameter("carUsageOption")))
                carUsageOption = "0";
        }

        //if class equals to all -> sql LIKE % (get all classes)
        if(qualityClass.equals("all"))
            qualityClass = "";

        try (Connection connection = dbUtils.getConnection()){
            carList = carDAO.searchCars(connection, brand, qualityClass, carDamageOption, carUsageOption);

            //put all found cars into request
            req.setAttribute("carList", carList);
            req.getRequestDispatcher("/managePage").forward(req, resp);
        } catch (SQLException ex){
            log.error(ex.getMessage());
        } catch (ServletException ex) {
            log.error(ex.getMessage());
        } catch (IOException ex) {
            log.error(ex.getMessage());
        }
    }

    public void executeShowAllCars(HttpServletRequest req, HttpServletResponse resp){
        try(Connection connection = dbUtils.getConnection()) {
            List<Car> list = carDAO.getAllCars(connection);

            //put list of all cars into request
            if(list.size()>0)
                req.setAttribute("carList", list);

            req.getRequestDispatcher("/managePage").forward(req, resp);
        }catch (SQLException ex){
            log.error(ex.getMessage());
        } catch (IOException | ServletException ex) {
            log.error(ex.getMessage());
        } catch (DAOException ex) {
            log.error(ex.getMessage());

            //just to show on view that it is a problem. Message will be internationalized
            req.setAttribute("noCarsError", 1);
            Helper.forwarder(req, resp, "/managePage");
        }
    }

    public void executeUpdateCar(HttpServletRequest req, HttpServletResponse resp){
        Car car = new Car();
        carSet(car, req);
        car.setId(Integer.parseInt(req.getParameter("id")));

        car.setUsed(Boolean.parseBoolean(req.getParameter("inUsage")));
        car.setDamaged(Boolean.parseBoolean(req.getParameter("damaged")));

        try(Connection connection = dbUtils.getConnection()){
            carDAO.updateCar(connection, car);
            executeShowAllCars(req, resp);
        }catch (SQLException ex){
            log.error(ex.getMessage());
            req.setAttribute("errorMessage", "Cannot edit car");
            Helper.forwarder(req, resp, "/errorPage.jsp");
        }
    }

    public void executeDelete(HttpServletRequest req, HttpServletResponse resp) {
        int id = Integer.parseInt(req.getParameter("id"));
        try(Connection connection = dbUtils.getConnection()){
            carDAO.deleteCar(connection, id);
            resp.sendRedirect("/deleteCar.jsp");
        }catch (SQLException ex){
            log.error(ex.getMessage());
            req.setAttribute("errorMessage", "Cannot delete car. This car might take part in order.");
            Helper.forwarder(req, resp, "/errorPage.jsp");
        } catch (IOException ex) {
            log.error(ex.getMessage());
        }
    }

    //method set Car object using info from request
    private void carSet(Car car, HttpServletRequest req){
        car.setBrand(req.getParameter("brand"));
        car.setQualityClass(req.getParameter("qualityClass"));
        car.setName(req.getParameter("name"));
        car.setCost(Double.parseDouble(req.getParameter("cost")));
    }
}
