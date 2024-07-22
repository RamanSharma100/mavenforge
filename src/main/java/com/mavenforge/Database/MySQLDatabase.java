package com.mavenforge.Database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.mavenforge.Contracts.Databases.SQLDatabaseContract;

public class MySQLDatabase extends SQLDatabaseContract {
    Connection connection = null;
    String table = "";
    String query = "";
    private String databaseName = "";

    public MySQLDatabase(String connectionString) {
        super(connectionString, "com.mysql.cj.jdbc.Driver");
        try {

            this.databaseName = connectionString.substring(connectionString.lastIndexOf("/") + 1).split("\\?")[0];

            connection = this.connect();
            System.out.println("Connected to the database " + connection.getCatalog());
        } catch (SQLException e) {
            throw new RuntimeException(e.toString());
        }
    }

    public MySQLDatabase(String connectionString, boolean isCLI) {
        super(connectionString, "com.mysql.cj.jdbc.Driver");
        try {

            connection = this.connect();

        } catch (SQLException e) {
            throw new RuntimeException(e.toString());
        }
    }

    public void setTable(String table) {
        this.table = table;
    }

    public ResultSet getSchema(String table) {
        query = "SELECT * FROM information_schema.columns WHERE table_name = ? AND table_schema = ?";
        try {
            PreparedStatement preparedQuery = connection.prepareStatement(query);
            preparedQuery.setString(1, table);
            preparedQuery.setString(2, this.databaseName);
            return preparedQuery.executeQuery();
        } catch (SQLException e) {
            throw new RuntimeException("Could not get the schema. Please check your query.");
        }
    }

    public void describeTable(String tableName) {
        query = "DESCRIBE ?";
        try {
            PreparedStatement preparedQuery = connection.prepareStatement(query);
            preparedQuery.setString(1, tableName);
            preparedQuery.execute();
        } catch (SQLException e) {
            throw new RuntimeException("Could not describe the table. Please check your query.");
        } finally {
            try {
                connection.close();
            } catch (SQLException e) {
                throw new RuntimeException("Could not close the connection.");
            }
        }
    }

    public void executeQuery(String query) {
        this.query = query;
        try {
            connection.createStatement().execute(query);

        } catch (SQLException e) {
            throw new RuntimeException("Could not execute the query. Please check your query. Error:" + e.toString());
        } finally {
            try {
                connection.close();
            } catch (SQLException e) {
                throw new RuntimeException("Could not close the connection.");
            }
        }
    }

    public void executeParameterizedQuery(String query, Object[] parameters) {
        this.query = query;
        try {
            PreparedStatement preparedQuery = connection.prepareStatement(query);
            for (int i = 0; i < parameters.length; i++) {
                preparedQuery.setObject(i + 1, parameters[i]);
            }

            preparedQuery.execute();
        } catch (SQLException e) {
            throw new RuntimeException("Could not execute the query. Please check your query.");
        } finally {
            try {
                connection.close();
            } catch (SQLException e) {
                throw new RuntimeException("Could not close the connection.");
            }
        }
    }

    public void createTable(String tableName, String columns) {
        query = "CREATE TABLE IF NOT EXISTS ? (?)";
        try {
            PreparedStatement preparedQuery = connection.prepareStatement(query);
            preparedQuery.setString(1, tableName);
            preparedQuery.setString(2, columns);
            preparedQuery.execute();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                connection.close();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void select(String columns, String condition) {
        query = "SELECT ? FROM ? WHERE ?";
        try {
            PreparedStatement preparedQuery = connection.prepareStatement(query);
            preparedQuery.setString(1, columns);
            preparedQuery.setString(2, table);
            preparedQuery.setString(3, condition);
            preparedQuery.execute();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void dropTable(String tableName) {
        if (!tableName.matches("[a-zA-Z0-9_]+")) {
            throw new IllegalArgumentException("Invalid table name");
        } else {
            query = "DROP TABLE IF EXISTS " + tableName;
            try {
                connection.createStatement().execute(query);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void insert(String tableName, String columns, Object[] values) {
        query = "INSERT INTO ? (?) VALUES (?)";
        try {
            PreparedStatement preparedQuery = connection.prepareStatement(query);
            preparedQuery.setString(1, tableName);
            preparedQuery.setString(2, columns);
            for (int i = 0; i < values.length; i++) {
                preparedQuery.setObject(i + 3, values[i]);
            }
            preparedQuery.execute();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void update(String tableName, String columns, Object[] values, String condition) {
        query = "UPDATE ? SET ? WHERE ?";
        try {
            PreparedStatement preparedQuery = connection.prepareStatement(query);
            preparedQuery.setString(1, tableName);
            preparedQuery.setString(2, columns);
            for (int i = 0; i < values.length; i++) {
                preparedQuery.setObject(i + 3, values[i]);
            }
            preparedQuery.setString(values.length + 3, condition);
            preparedQuery.execute();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void delete(String tableName, String condition) {
        query = "DELETE FROM ? WHERE ?";
        try {
            PreparedStatement preparedQuery = connection.prepareStatement(query);
            preparedQuery.setString(1, tableName);
            preparedQuery.setString(2, condition);
            preparedQuery.execute();
        } catch (SQLException e) {

            throw new RuntimeException(e);
        }
    }

    public String customQuery(String query) {
        this.query = query;
        try {
            return connection.createStatement().executeQuery(query).toString();
        } catch (SQLException e) {
            throw new RuntimeException("Could not execute the query. Please check your query.");
        }
    }

    public void closeConnection() {
        try {
            connection.close();
        } catch (SQLException e) {
            throw new RuntimeException("Could not close the connection.");
        }
    }

    // /*
    // * Database main queries functions
    // */

    // public Object all() {
    // String query = "SELECT * FROM " + table;
    // return customQuery(query);
    // }

}
