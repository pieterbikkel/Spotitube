package nl.han.oose.dea.datasource.dao;

import jakarta.inject.Inject;
import nl.han.oose.dea.controllers.dto.request.LoginRequestDTO;
import nl.han.oose.dea.datasource.databaseConnection.ConnectionManager;
import nl.han.oose.dea.datasource.exceptions.DatabaseConnectionException;
import nl.han.oose.dea.datasource.exceptions.InvalidCredentialsException;
import org.apache.commons.codec.digest.DigestUtils;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;

public class LoginDAO {
    @Inject
    private ConnectionManager connectionManager;

    public void generateToken(LoginRequestDTO loginRequestDTO, String token) throws Exception {
        connectionManager.initConnection();
        var sql = "UPDATE eigenaar SET token = ? WHERE gebruikersnaam = ?;";
        var preparedStatement = connectionManager.getConnection().prepareStatement(sql);
        preparedStatement.setString(1, token);
        preparedStatement.setString(2, loginRequestDTO.getUser());
        preparedStatement.executeUpdate();
        connectionManager.closeConnection();
    }
    public boolean userExists(LoginRequestDTO loginRequestDTO) throws Exception {
        connectionManager.initConnection();
        var sql = "SELECT * FROM eigenaar WHERE gebruikersnaam = ? AND wachtwoord = ?;";
        var preparedStatement = connectionManager.getConnection().prepareStatement(sql);
        preparedStatement.setString(1, loginRequestDTO.getUser());
        preparedStatement.setString(2, stringToHash(loginRequestDTO.getPassword()));
        boolean userExists = getResults(preparedStatement.executeQuery());
        connectionManager.closeConnection();
        return userExists;
    }

    public void verifyToken(String token) throws Exception {

        connectionManager.initConnection();
        PreparedStatement statement = connectionManager.getConnection().prepareStatement("SELECT token FROM eigenaar WHERE token = ?");
        statement.setString(1, token);
        ResultSet result = statement.executeQuery();

        if (!result.isBeforeFirst()) {
            throw new InvalidCredentialsException();
        }
        connectionManager.closeConnection();

    }

    private boolean getResults(ResultSet rs) throws Exception {
        int nResults = 0;
        while (rs.next()) {
            nResults += 1;
        }
        return nResults == 1;
    }

    private String stringToHash(String string) {
        return DigestUtils.sha256Hex(string);
    }

    private ResultSet getUser(LoginRequestDTO loginRequestDTO) throws Exception {
        var sql = "SELECT 1 FROM eigenaar WHERE gebruikersnaam = ?;";
        var preparedStatement = connectionManager.getConnection().prepareStatement(sql);
        preparedStatement.setString(1, loginRequestDTO.getUser());
        return preparedStatement.executeQuery();
    }
}
