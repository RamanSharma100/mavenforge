package com.mavenforge.Contracts.Databases;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import com.mavenforge.Contracts.DatabaseContract;

public class SQLDatabaseContract extends DatabaseContract {
    private String driverClass;

    public SQLDatabaseContract(String connectionString, final String driverClass) {
        super(connectionString);
        this.driverClass = driverClass;
    }

    @Override
    public Connection connect() throws SQLException {
        if (!isDriverPresent()) {
            throw new SQLException(
                    "SQL Driver not found, please add the driver 'mysql-connector-java' to your project.");
        }

        Connection connection = null;

        try {
            connection = getConnection();
            return connection;
        } catch (SQLException e) {
            System.err.println(e.toString());
            throw new SQLException(e.toString());
        } finally {
            if (connection != null) {
                connection.close();
            }

        }

    }

    private Connection getConnection() throws SQLException {
        String connectionString = this.connectionString;

        return DriverManager.getConnection(connectionString);
    }

    private boolean isDriverPresent() {
        try {
            Class.forName(this.driverClass);
            return true;
        } catch (ClassNotFoundException e) {
            return false;
        }
    }

}
