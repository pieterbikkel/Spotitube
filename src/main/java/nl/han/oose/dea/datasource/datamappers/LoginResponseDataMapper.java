package nl.han.oose.dea.datasource.datamappers;

import nl.han.oose.dea.controllers.dto.response.LoginResponseDTO;

import java.sql.ResultSet;
import java.sql.SQLException;

public class LoginResponseDataMapper {

    public LoginResponseDTO mapTo(ResultSet rs) throws SQLException {
        rs.next();
        return new LoginResponseDTO(rs.getString("gebruikersnaam"), rs.getString("token"));
    }
}
