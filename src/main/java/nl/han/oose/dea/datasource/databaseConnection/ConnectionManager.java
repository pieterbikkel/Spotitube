package nl.han.oose.dea.datasource.databaseConnection;

import jakarta.inject.Inject;
import nl.han.oose.dea.datasource.exceptions.DatabaseConnectionException;

import java.sql.Connection;
import java.sql.DriverManager;

public class ConnectionManager {

    private Connection connection;
    private DatabaseProperties databaseProperties;

    public Connection getConnection() {
        return connection;
    }

    public void startConnection() {
        try {
            Class.forName(databaseProperties.driverString());
            connection = DriverManager.getConnection(databaseProperties.connectionString());
        } catch (Exception e) {
            e.printStackTrace();
            throw new DatabaseConnectionException();
        }
    }

    public void stopConnection() {
        try {
            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
            throw new DatabaseConnectionException();
        }
    }

    @Inject
    public void setDatabaseProperties(DatabaseProperties databaseProperties)
    { this.databaseProperties = databaseProperties; }

}
