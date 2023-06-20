package nl.han.oose.dea.datasource;

import nl.han.oose.dea.controllers.dto.request.LoginRequestDTO;
import nl.han.oose.dea.controllers.dto.response.LoginResponseDTO;
import nl.han.oose.dea.datasource.dao.LoginDAO;
import nl.han.oose.dea.datasource.databaseConnection.ConnectionManager;
import nl.han.oose.dea.datasource.exceptions.ForbiddenResourceException;
import org.apache.commons.codec.digest.DigestUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

public class LoginDAOTest {

    private String user = "user";
    private String password = "password";
    private String token = "token";

    private LoginDAO sut;
    private ConnectionManager connectionManager;
    private Connection mockedConnection;
    private PreparedStatement preparedStatement;

    private ResultSet mockedResultSet;

    private LoginRequestDTO loginRequestDTO = new LoginRequestDTO(user, password);

    @BeforeEach
    public void setup() throws SQLException {
        sut = new LoginDAO();
        connectionManager = mock(ConnectionManager.class);
        mockedResultSet = mock(ResultSet.class);
        mockedConnection = mock(Connection.class);
        preparedStatement = mock(PreparedStatement.class);
        sut.setConnectionManager(connectionManager);

        when(connectionManager.getConnection()).thenReturn(mockedConnection);
        when(mockedConnection.prepareStatement(anyString())).thenReturn(preparedStatement);
        when(preparedStatement.executeQuery()).thenReturn(mockedResultSet);
    }

    @Test
    void userExistsTest() throws Exception {
        sut.userExists(loginRequestDTO);

        verify(preparedStatement).executeQuery();
    }

    @Test
    void getUserFromResultSet() throws Exception {
        assertNotNull(sut.getResults(mockedResultSet));
    }

    @Test
    void stringToHashSuccess() {
        // Arrange
        String inputString = "yourString";
        String expectedHash = DigestUtils.sha256Hex(inputString);

        // Act
        String actualHash = sut.stringToHash(inputString);

        // Assert
        Assertions.assertEquals(expectedHash, actualHash);
    }

    @Test
    void verifyTokenTest() throws Exception {
        when(mockedResultSet.isBeforeFirst()).thenReturn(true);
        sut.verifyToken(token);

        verify(preparedStatement).executeQuery();
    }

    @Test
    void verifyTokenForbiddenResource() throws Exception {
        when(mockedResultSet.isBeforeFirst()).thenReturn(false);

        // Act and Assert
        Assertions.assertThrows(ForbiddenResourceException.class, () -> {
            sut.verifyToken(token);
        });
    }

}
