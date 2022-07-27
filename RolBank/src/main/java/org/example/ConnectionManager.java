package org.example;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionManager {

    //variable declarations
    private String url;
    private String username;
    private String password;

    private Driver driver;
    private boolean driverReady;

    //constructor to set the credentials for database connectivity
    public ConnectionManager(String url, String username, String password, Driver driver) {
        this.url = url;
        this.username = username;
        this.password = password;
        this.driver = driver;

        this.driverReady = false;

    }

    //method to register the driver
    private void registerDriver() throws SQLException {
        if(!driverReady) {
            DriverManager.registerDriver(this.driver);
        }
    }

    //method to connect to the driver
    public Connection getConnection() throws SQLException{
        if(!driverReady) {
            registerDriver();
        }

        return DriverManager.getConnection(url, username, password);
    }
}