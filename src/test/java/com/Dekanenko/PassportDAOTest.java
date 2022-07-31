package com.Dekanenko;

import com.Dekanenko.DAO.DAOFactory.DAOFactory;
import com.Dekanenko.DAO.DAOInterfaces.PassportDAO;
import com.Dekanenko.DAO.entity.Passport;
import com.Dekanenko.exceptions.DAOException;
import org.junit.jupiter.api.*;

import java.sql.*;

import static com.Dekanenko.DAO.queries.MySQLQuery.PassportQuery.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class PassportDAOTest {
    private static PassportDAO passportDAO;
    private Connection mockConnection;
    private PreparedStatement mockPreparedStatement;
    private ResultSet mockResultSet;

    @BeforeAll
    public static void setup(){
        System.err.close();
        System.setErr(System.out);
        System.setProperty("database", "MySQL");
        passportDAO = DAOFactory.getInstance().getPassportDAO();
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
    public void addPassportTest() throws SQLException {
        Passport passport = passportCreator(1, "Name1", "Surname1", "1234567");

        when(mockConnection.prepareStatement(INSERT_PASSPORT, Statement.RETURN_GENERATED_KEYS)).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.getGeneratedKeys()).thenReturn(mockResultSet);
        when(mockResultSet.next()).thenReturn(true);
        when(mockResultSet.getInt(1)).thenReturn(passport.getId());

        int actualId = passportDAO.addPassport(mockConnection, passport);

        Assertions.assertEquals(passport.getId(), actualId);
    }

    @Test
    public void addPassportNoIdTest() throws SQLException {
        Passport passport = passportCreator(1, "Name1", "Surname1", "1234567");

        when(mockConnection.prepareStatement(INSERT_PASSPORT, Statement.RETURN_GENERATED_KEYS)).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.getGeneratedKeys()).thenReturn(mockResultSet);
        when(mockResultSet.next()).thenReturn(false);
        when(mockResultSet.getInt(1)).thenReturn(passport.getId());

        int expectedId = -1;
        int actualId = passportDAO.addPassport(mockConnection, passport);

        Assertions.assertEquals(expectedId, actualId);
    }

    @Test
    public void getPassportTest() throws SQLException, DAOException {
        Passport expectedPassport = passportCreator(0, "Name1", "Surname1", "1234567");

        when(mockConnection.prepareStatement(SELECT_PASSPORT_BY_ID)).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeQuery()).thenReturn(mockResultSet);

        when(mockResultSet.next()).thenReturn(true);
        when(mockResultSet.getString("name")).thenReturn(expectedPassport.getName());
        when(mockResultSet.getString("surname")).thenReturn(expectedPassport.getSurname());
        when(mockResultSet.getString("unique_num")).thenReturn(expectedPassport.getUniqueNum());

        Passport actualPassport = passportDAO.getPassport(mockConnection, expectedPassport.getId());

        Assertions.assertEquals(expectedPassport, actualPassport);
    }

    @Test
    public void getPassportDAOExceptionTest() throws SQLException, DAOException {
        Passport expectedPassport = passportCreator(0, "Name1", "Surname1", "1234567");

        when(mockConnection.prepareStatement(SELECT_PASSPORT_BY_ID)).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeQuery()).thenReturn(mockResultSet);

        when(mockResultSet.next()).thenReturn(false);

        Exception exception = Assertions.assertThrows(DAOException.class, ()->{
            passportDAO.getPassport(mockConnection, expectedPassport.getId());
        });

        String expectedMessage = "Cannot get needed passport";
        String actualMessage = exception.getMessage();

        Assertions.assertTrue(expectedMessage.contains(actualMessage));
    }

    private Passport passportCreator(int id, String name, String surname, String uniqueNum){
        Passport passport = new Passport();
        passport.setId(id);
        passport.setName(name);
        passport.setSurname(surname);
        passport.setUniqueNum(uniqueNum);
        return passport;
    }

}
