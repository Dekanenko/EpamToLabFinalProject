package com.Dekanenko;

import com.Dekanenko.DAO.DAOFactory.DAOFactory;
import com.Dekanenko.DAO.DAOInterfaces.UserDAO;
import com.Dekanenko.DAO.entity.User;
import com.Dekanenko.exceptions.DAOException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import static com.Dekanenko.DAO.queries.MySQLQuery.UserQuery.*;


public class UserDAOTest {

    private static UserDAO userDAO;
    private Connection mockConnection;
    private PreparedStatement mockPreparedStatement;
    private ResultSet mockResultSet;

    @BeforeAll
    public static void setup(){
        System.err.close();
        System.setErr(System.out);
        System.setProperty("database", "MySQL");
        userDAO = DAOFactory.getInstance().getUserDAO();
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
    public void getUserTest() throws SQLException {
        User expectedUser = userCreator(1, "client1", "client1@gmail.com", 100, true, 4, 1);

        when(mockConnection.prepareStatement(SELECT_USER_BY_ID)).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeQuery()).thenReturn(mockResultSet);
        when(mockResultSet.next()).thenReturn(true).thenReturn(false);
        mockUserResultSet(mockResultSet, expectedUser);

        User actualUser = userDAO.getUser(mockConnection, 1);

        Assertions.assertEquals(actualUser, expectedUser);
    }

    @Test
    public void getUserWithPasswordTest() throws SQLException, DAOException {
        User expectedUser = userCreator(1, "client1", "client1@gmail.com", 100, true, 4, 1);
        StringBuilder password = new StringBuilder();
        String expectedPsd = "password1";

        when(mockConnection.prepareStatement(SELECT_USER_BY_LOGIN)).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeQuery()).thenReturn(mockResultSet);
        when(mockResultSet.next()).thenReturn(true).thenReturn(false);
        mockUserResultSet(mockResultSet, expectedUser);
        when(mockResultSet.getString("password")).thenReturn("password1");

        User actualUser = userDAO.getUserWithPassword(mockConnection, "client1", password);

        Assertions.assertEquals(expectedUser, actualUser);
        Assertions.assertEquals(expectedPsd, password.toString());
    }

    @Test
    public void getUserWithPasswordDAOExceptionTest() throws SQLException, DAOException {
        User expectedUser = userCreator(1, "client1", "client1@gmail.com", 100, true, 4, 1);
        StringBuilder password = new StringBuilder();

        when(mockConnection.prepareStatement(SELECT_USER_BY_LOGIN)).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeQuery()).thenReturn(mockResultSet);
        when(mockResultSet.next()).thenReturn(false);
        mockUserResultSet(mockResultSet, expectedUser);
        when(mockResultSet.getString("password")).thenReturn("password1");

        Exception exception = Assertions.assertThrows(DAOException.class, ()->{
            userDAO.getUserWithPassword(mockConnection, "client1", password);
        });

        String expectedMessage = "Cannot find user";
        String actualMessage = exception.getMessage();

        Assertions.assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    public void getAllUsersTest() throws SQLException, DAOException {
        List<User> expectedUserList = new ArrayList<>();
        Map<Integer, String> actualRoleMap = new TreeMap<>();
        Map<Integer, String> expectedRoleMap = new TreeMap<>();
        expectedRoleMap.put(1, "admin");
        expectedRoleMap.put(2, "manager");
        expectedRoleMap.put(3, "driver");
        expectedRoleMap.put(4, "client");

        User user1 = userCreator(1, "admin1", "admin1@gmail.com", 100, true, 1, 1);
        User user2 = userCreator(2, "manager1", "manager1@gmail.com", 100, true, 2, 2);
        User user3 = userCreator(3, "driver1", "driver1@gmail.com", 100, true, 3, 3);
        User user4 = userCreator(4, "client1", "client1@gmail.com", 100, true, 4, 4);

        expectedUserList.add(user1);
        expectedUserList.add(user2);
        expectedUserList.add(user3);
        expectedUserList.add(user4);

        when(mockConnection.prepareStatement(SELECT_ALL_USERS_WITH_ROLE)).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeQuery()).thenReturn(mockResultSet);
        when(mockResultSet.next()).thenReturn(true).thenReturn(true).thenReturn(true).thenReturn(true).thenReturn(false);

        mockUserListResultSet(mockResultSet, expectedUserList);

        when(mockResultSet.getString("role_status")).thenReturn("admin").thenReturn("manager").thenReturn("driver").thenReturn("client");

        List<User> actualUserList = userDAO.getAllUsers(mockConnection, actualRoleMap);

        Assertions.assertEquals(expectedUserList, actualUserList);
        Assertions.assertEquals(expectedRoleMap, actualRoleMap);
    }

    @Test
    public void searchUsersTest() throws SQLException {
        List<User> expectedUserList = new ArrayList<>();
        Map<Integer, String> actualRoleMap = new TreeMap<>();
        Map<Integer, String> expectedRoleMap = new TreeMap<>();
        expectedRoleMap.put(1, "admin");
        expectedRoleMap.put(2, "manager");
        expectedRoleMap.put(3, "driver");
        expectedRoleMap.put(4, "client");

        User user1 = userCreator(1, "admin1", "admin1@gmail.com", 100, true, 1, 1);
        User user2 = userCreator(2, "manager1", "manager1@gmail.com", 100, true, 2, 2);
        User user3 = userCreator(3, "driver1", "driver1@gmail.com", 100, true, 3, 3);
        User user4 = userCreator(4, "client1", "client1@gmail.com", 100, true, 4, 4);

        expectedUserList.add(user1);
        expectedUserList.add(user2);
        expectedUserList.add(user3);
        expectedUserList.add(user4);

        when(mockConnection.prepareStatement(SEARCH_USER)).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeQuery()).thenReturn(mockResultSet);
        when(mockResultSet.next()).thenReturn(true).thenReturn(true).thenReturn(true).thenReturn(true).thenReturn(false);

        mockUserListResultSet(mockResultSet, expectedUserList);

        when(mockResultSet.getString("role_status")).thenReturn("admin").thenReturn("manager").thenReturn("driver").thenReturn("client");

        List<User> actualUserList = userDAO.searchUser(mockConnection, actualRoleMap, "", "");

        Assertions.assertEquals(expectedUserList, actualUserList);
        Assertions.assertEquals(expectedRoleMap, actualRoleMap);
    }

    @Test
    public void getAllUsersDAOExceptionTest() throws SQLException, DAOException {
        Map<Integer, String> actualRoleMap = new TreeMap<>();

        when(mockConnection.prepareStatement(SELECT_ALL_USERS_WITH_ROLE)).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeQuery()).thenReturn(mockResultSet);
        when(mockResultSet.next()).thenReturn(false);

        Exception exception = Assertions.assertThrows(DAOException.class, ()->{
            userDAO.getAllUsers(mockConnection, actualRoleMap);
        });

        String expectedMessage = "No users";
        String actualMessage = exception.getMessage();

        Assertions.assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    public void getAllDriversTest() throws SQLException {
        User driver1 = userCreator(1, "driver1", "driver1@gmail.com", 100, true, 3, 1);
        List<User> expectedDriverList = new ArrayList<>();
        expectedDriverList.add(driver1);

        when(mockConnection.prepareStatement(SELECT_ALL_DRIVERS)).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeQuery()).thenReturn(mockResultSet);
        when(mockResultSet.next()).thenReturn(true).thenReturn(false);
        mockUserResultSet(mockResultSet, driver1);

        List<User> actualUserList = userDAO.getAllDrivers(mockConnection);

        Assertions.assertEquals(expectedDriverList, actualUserList);
    }

    @Test
    public void insertUserTrueTest() throws SQLException {
        User user = userCreator(1, "client1", "client1@gmail.com", 100, true, 4, 1);

        when(mockConnection.prepareStatement(INSERT_USER, Statement.RETURN_GENERATED_KEYS)).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.getGeneratedKeys()).thenReturn(mockResultSet);
        mockUserResultSet(mockResultSet, user);
        when(mockResultSet.next()).thenReturn(true);

        Assertions.assertTrue(userDAO.insertUser(mockConnection, user, "password1"));
    }

    @Test
    public void insertUserFalseTest() throws SQLException {
        User user = userCreator(1, "client1", "client1@gmail.com", 100, true, 4, 1);

        when(mockConnection.prepareStatement(INSERT_USER, Statement.RETURN_GENERATED_KEYS)).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.getGeneratedKeys()).thenReturn(mockResultSet);
        mockUserResultSet(mockResultSet, user);
        when(mockResultSet.next()).thenReturn(false);

        Assertions.assertFalse(userDAO.insertUser(mockConnection, user, "password1"));
    }

    private User userCreator(int id, String login, String email, double cash, boolean affordable, int roleId, int passportId){
        User user = new User();
        user.setId(id);
        user.setLogin(login);
        user.setEmail(email);
        user.setCash(cash);
        user.setAffordable(affordable);
        user.setRoleId(roleId);
        user.setPassportId(passportId);
        return user;
    }

    private void mockUserResultSet(ResultSet rs, User user) throws SQLException {
        when(rs.getInt("id")).thenReturn(user.getId());
        when(rs.getString("login")).thenReturn(user.getLogin());
        when(rs.getString("email")).thenReturn(user.getEmail());
        when(rs.getDouble("cash")).thenReturn(user.getCash());
        when(rs.getBoolean("affordable")).thenReturn(user.isAffordable());
        when(rs.getInt("role_id")).thenReturn(user.getRoleId());
        when(rs.getInt("passport_id")).thenReturn(user.getPassportId());
    }

    private void mockUserListResultSet(ResultSet rs, List<User> userList) throws SQLException {
        when(rs.getInt("id")).thenReturn(userList.get(0).getId()).thenReturn(userList.get(1).getId()).thenReturn(userList.get(2).getId()).thenReturn(userList.get(3).getId());
        when(rs.getString("login")).thenReturn(userList.get(0).getLogin()).thenReturn(userList.get(1).getLogin()).thenReturn(userList.get(2).getLogin()).thenReturn(userList.get(3).getLogin());
        when(rs.getString("email")).thenReturn(userList.get(0).getEmail()).thenReturn(userList.get(1).getEmail()).thenReturn(userList.get(2).getEmail()).thenReturn(userList.get(3).getEmail());
        when(rs.getDouble("cash")).thenReturn(userList.get(0).getCash()).thenReturn(userList.get(1).getCash()).thenReturn(userList.get(2).getCash()).thenReturn(userList.get(3).getCash());
        when(rs.getBoolean("affordable")).thenReturn(userList.get(0).isAffordable()).thenReturn(userList.get(1).isAffordable()).thenReturn(userList.get(2).isAffordable()).thenReturn(userList.get(3).isAffordable());
        when(rs.getInt("role_id")).thenReturn(userList.get(0).getRoleId()).thenReturn(userList.get(0).getRoleId()).thenReturn(userList.get(1).getRoleId()).thenReturn(userList.get(1).getRoleId()).thenReturn(userList.get(2).getRoleId()).thenReturn(userList.get(2).getRoleId()).thenReturn(userList.get(3).getRoleId()).thenReturn(userList.get(3).getRoleId());
        when(rs.getInt("passport_id")).thenReturn(userList.get(0).getPassportId()).thenReturn(userList.get(1).getPassportId()).thenReturn(userList.get(2).getPassportId()).thenReturn(userList.get(3).getPassportId());
    }
}
