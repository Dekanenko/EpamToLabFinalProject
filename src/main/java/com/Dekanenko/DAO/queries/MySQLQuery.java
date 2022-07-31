package com.Dekanenko.DAO.queries;

public class MySQLQuery {

    private MySQLQuery() {
    }

    public static class UserQuery{
        public static final String SELECT_USER_BY_LOGIN = "SELECT * FROM user WHERE login=?";
        public static final String SELECT_USER_BY_ID = "SELECT * FROM user WHERE id=?";
        public static final String SELECT_ALL_DRIVERS = "SELECT * FROM user WHERE role_id=3";
        public static final String SELECT_ALL_USERS_WITH_ROLE = "SELECT user.*, role.role_status FROM user INNER JOIN role ON user.role_id = role.id;";
        public static final String INSERT_USER = "INSERT INTO user (id, login, email, password, cash, affordable, role_id) VALUES (DEFAULT, ?, ?, ?, ?, ?, ?);";
        public static final String CHANGE_USER_STATUS = "UPDATE user SET affordable = ? WHERE id = ?";
        public static final String UPDATE_USER_PASSPORT = "UPDATE user SET passport_id = ? WHERE id = ?";
        public static final String CHANGE_USER_CASH = "UPDATE user SET user.cash = user.cash+(?) WHERE id=?;";
        public static final String UPDATE_USER = "UPDATE user SET login=?, email=?, password=?, cash=?, affordable=?, role_id=?  WHERE id = ?";
        public static final String DELETE_USER = "DELETE FROM user WHERE id = ?";
        public static final String SEARCH_USER = "SELECT user.*, role.role_status FROM user INNER JOIN role ON user.role_id = role.id WHERE user.login LIKE ? AND user.role_id LIKE ?;";
    }

    public static class CarQuery{
        public static final String INSERT_CAR = "INSERT INTO car VALUES (DEFAULT, ?, ?, ?, ?, false, false);";
        public static final String SELECT_ALL_CARS = "SELECT * FROM car";
        public static final String UPDATE_CAR = "UPDATE car SET brand=?, class=?, name=?, cost=?, used=?, damaged=? WHERE id = ?";
        public static final String CHANGE_CAR_USAGE = "UPDATE car SET used = ? WHERE id = ?";
        public static final String CHANGE_CAR_DAMAGE = "UPDATE car SET damaged=? WHERE id = ?";
        public static final String DELETE_CAR = "DELETE FROM car WHERE id = ?";
        public static final String GET_CAR_BY_ID = "SELECT * FROM car WHERE id=?";
        public static final String SEARCH_CAR = "SELECT * FROM car WHERE brand LIKE ? AND class LIKE ? AND damaged LIKE ? AND used LIKE ?;";
    }

    public static class PassportQuery{
        public static final String INSERT_PASSPORT = "INSERT INTO passport VALUES (DEFAULT, ?, ?, ?);";
        public static final String SELECT_PASSPORT_BY_ID = "SELECT * FROM passport WHERE id=?";
        public static final String UPDATE_PASSPORT = "UPDATE passport SET name=?, surname=?, unique_num=? WHERE id = ?";
        public static final String DELETE_PASSPORT = "DELETE FROM passport WHERE id=?";
    }

    public static class OrderQuery{
        public static final  String INSERT_ORDER = "INSERT INTO `enterpriseDB`.`order` VALUES(DEFAULT, ?, ?, ?, NULL, ?, ?, ?, ?, NULL);";
        public static final String INSERT_USER_ORDER= "INSERT INTO user_order VALUES (?, ?);";
        public static final String GET_USER_ORDERS= "SELECT * FROM user_order WHERE user_id=?;";
        public static final String GET_ORDER= "SELECT `enterpriseDB`.`order`.*, status.status_name FROM `enterpriseDB`.`order` INNER JOIN status ON `enterpriseDB`.`order`.status_id = status.id  WHERE `enterpriseDB`.`order`.id=?;";
        public static final String GET_ALL_ORDERS = "SELECT user_order.* FROM user_order INNER JOIN user ON user_order.user_id = user.id AND user.role_id = 4;";
        public static final String GET_ORDER_BY_USER_ID = "SELECT user_order.order_id FROM user_order WHERE user_order.user_id=?";
        public static final String CHANGE_ORDER_STATUS = "UPDATE `enterpriseDB`.`order` SET status_id=? WHERE id=?";
        public static final String CHANGE_ORDER_MESSAGE = "UPDATE `enterpriseDB`.`order` SET message=? WHERE id=?";
        public static final String DELETE_ORDER = "DELETE FROM `enterpriseDB`.`order` WHERE id=?";
        public static final String ADD_RETURN_DATE = "UPDATE `enterpriseDB`.`order` SET return_date=? WHERE id=?";
        public static final String GET_USER_ID_BY_ROLE = "SELECT user_order.user_id FROM user_order INNER JOIN user ON user_order.user_id = user.id AND user.role_id=? WHERE user_order.order_id=?;";
        public static final String SEARCH_ORDER = "SELECT `enterpriseDB`.`order`.*, status.status_name FROM `enterpriseDB`.`order` INNER JOIN status ON `enterpriseDB`.`order`.status_id = status.id WHERE first_date LIKE ? AND last_date LIKE ? AND status_id LIKE ? AND driver LIKE ?";
    }
}
