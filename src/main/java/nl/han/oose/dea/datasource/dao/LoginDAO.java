package nl.han.oose.dea.datasource.dao;

import jakarta.inject.Inject;
import nl.han.oose.dea.controllers.dto.request.LoginRequestDTO;
import nl.han.oose.dea.datasource.databaseConnection.ConnectionManager;
import nl.han.oose.dea.datasource.exceptions.ForbiddenResourceException;
import org.apache.commons.codec.digest.DigestUtils;

import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class LoginDAO {
    @Inject
    private ConnectionManager connectionManager;

    public void updateToken(LoginRequestDTO loginRequestDTO, String token) throws Exception {
        connectionManager.startConnection();
        var sql = "UPDATE eigenaar SET token = ? WHERE gebruikersnaam = ?;";
        var preparedStatement = connectionManager.getConnection().prepareStatement(sql);
        preparedStatement.setString(1, token);
        preparedStatement.setString(2, loginRequestDTO.getUser());
        preparedStatement.executeUpdate();
        connectionManager.stopConnection();
    }
    public boolean userExists(LoginRequestDTO loginRequestDTO) throws Exception {
        connectionManager.startConnection();
        var sql = "SELECT * FROM eigenaar WHERE gebruikersnaam = ? AND wachtwoord = ?;";
        var preparedStatement = connectionManager.getConnection().prepareStatement(sql);
        preparedStatement.setString(1, loginRequestDTO.getUser());
        preparedStatement.setString(2, stringToHash(loginRequestDTO.getPassword()));
        boolean userExists = getResults(preparedStatement.executeQuery());
        connectionManager.stopConnection();
        return userExists;
    }

    public void verifyToken(String token) throws ForbiddenResourceException {
        try {
            connectionManager.startConnection();
            PreparedStatement statement = connectionManager.getConnection().prepareStatement("SELECT token FROM eigenaar WHERE token = ?");
            statement.setString(1, token);
            ResultSet result = statement.executeQuery();

            if (!result.isBeforeFirst()) {
                throw new ForbiddenResourceException();
            }
            connectionManager.stopConnection();
        } catch (Exception e) {
            throw new ForbiddenResourceException();
        }


    }

    public boolean getResults(ResultSet rs) throws Exception {
        int nResults = 0;
        while (rs.next()) {
            nResults += 1;
        }
        return nResults == 1;
    }

    public String stringToHash(String string) {
        return DigestUtils.sha256Hex(string);
    }

    @Inject
    public void setConnectionManager(ConnectionManager connectionManager)
    { this.connectionManager = connectionManager; }
}
