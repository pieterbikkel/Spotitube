package nl.han.oose.dea.datasource.dao;

import nl.han.oose.dea.controllers.dto.request.LoginRequestDTO;
import nl.han.oose.dea.controllers.dto.response.LoginResponseDTO;
import nl.han.oose.dea.datasource.databaseConnection.ConnectionManager;
import nl.han.oose.dea.datasource.datamappers.LoginResponseDataMapper;
import org.apache.commons.codec.digest.DigestUtils;

import java.security.SecureRandom;
import java.sql.ResultSet;
import java.util.Base64;

public class LoginDAO {

    private static final int TOKEN_LENGTH = 16;
    private ConnectionManager connectionManager;
    private LoginResponseDataMapper loginResponseDataMapper;

    public void generateToken(LoginRequestDTO loginRequestDTO) throws Exception {
        connectionManager.initConnection();
        var sql = "INSERT INTO eigenaar(token) VALUES(?) WHERE gebruikersnaam = ?;";
        var preparedStatement = connectionManager.getConnection().prepareStatement(sql);
        preparedStatement.setString(1, generateToken());
        preparedStatement.setString(2, loginRequestDTO.getGebruikersnaam());
        preparedStatement.executeUpdate();
        connectionManager.closeConnection();
    }
    public boolean userExists(LoginRequestDTO loginRequestDTO) throws Exception {
        connectionManager.initConnection();
        var sql = "SELECT 1 FROM eigenaar WHERE gebruikersnaam = ? AND wachtwoord = ?;";
        var preparedStatement = connectionManager.getConnection().prepareStatement(sql);
        preparedStatement.setString(1, loginRequestDTO.getGebruikersnaam());
        preparedStatement.setString(2, stringToHash(loginRequestDTO.getWachtwoord()));
        boolean userExists = getResults(preparedStatement.executeQuery());
        connectionManager.closeConnection();
        return userExists;
    }

    public LoginResponseDTO checkUserCredentials(LoginRequestDTO loginRequestDTO) throws Exception {
        LoginResponseDTO loginResponseDTO;
        connectionManager.initConnection();
        loginResponseDTO = loginResponseDataMapper.mapTo(getUser(loginRequestDTO));
        connectionManager.closeConnection();
        return loginResponseDTO;
    }

    private boolean getResults(ResultSet rs) throws Exception {
        int nResults = 0;
        while (rs.next()) {
            nResults = rs.getInt("nUsers");
        }
        return nResults == 1;
    }

    private String generateToken() {
        byte[] randomBytes = new byte[TOKEN_LENGTH];
        SecureRandom secureRandom = new SecureRandom();
        secureRandom.nextBytes(randomBytes);

        return Base64.getUrlEncoder().withoutPadding().encodeToString(randomBytes);
    }

    private String stringToHash(String string) {
        return DigestUtils.sha256Hex(string);
    }

    private ResultSet getUser(LoginRequestDTO loginRequestDTO) throws Exception {
        var sql = "SELECT 1 FROM eigenaar WHERE gebruikersnaam = ?;";
        var preparedStatement = connectionManager.getConnection().prepareStatement(sql);
        preparedStatement.setString(1, loginRequestDTO.getGebruikersnaam());
        return preparedStatement.executeQuery();
    }
}
