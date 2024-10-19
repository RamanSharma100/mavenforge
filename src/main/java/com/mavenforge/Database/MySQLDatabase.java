package com.mavenforge.Database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.mavenforge.Contracts.Databases.SQLDatabaseContract;

public class MySQLDatabase extends SQLDatabaseContract {
    Connection connection = null;
    String table = "";
    String query = "";
    String primaryKey = "id";
    String columns = "";
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

    public ResultSet select(String columns, String condition) {
        query = "SELECT ? FROM ? WHERE ?";
        ResultSet result = null;
        try {
            PreparedStatement preparedQuery = connection.prepareStatement(query);
            preparedQuery.setString(1, columns);
            preparedQuery.setString(2, table);
            preparedQuery.setString(3, condition);
            preparedQuery.execute();
            result = preparedQuery.getResultSet();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return result;
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

    public ResultSet customQuery(String query, List<Object> parameters) {
        this.query = query;
        try {
            PreparedStatement preparedQuery = connection.prepareStatement(query);
            for (int i = 0; i < parameters.size(); i++) {
                preparedQuery.setObject(i + 1, parameters.get(i));
            }
            return preparedQuery.executeQuery();
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

    private List<Map<String, Object>> resultToMap(ResultSet result) {
        List<Map<String, Object>> resultList = new ArrayList<>();

        try {
            ResultSetMetaData metaData = result.getMetaData();
            int columnCount = metaData.getColumnCount();

            while (result.next()) {
                Map<String, Object> rowMap = new HashMap<>();
                for (int i = 1; i <= columnCount; i++) {
                    rowMap.put(metaData.getColumnName(i), result.getObject(i));
                }
                resultList.add(rowMap);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return resultList;
    }

    // /*
    // * Database main queries functions
    // */

    public List<Map<String, Object>> all() {
        String query = "SELECT * FROM ?";
        ResultSet result = this.customQuery(query, List.of(this.table));
        return this.resultToMap(result);
    }

    public List<Map<String, Object>> find() {
        return this.all();
    }

    public List<Map<String, Object>> find(String id) {
        ResultSet result = this.select("*", primaryKey + " = " + id);
        return this.resultToMap(result);
    }

    public Map<String, Object> findOne(String id) {
        ResultSet result = this.select("*", primaryKey + " = " + id);
        return this.resultToMap(result).get(0);
    }

    public Map<String, Object> first() {
        ResultSet result = this.select("*", "1");
        return this.resultToMap(result).get(0);
    }

    public List<Map<String, Object>> where(String column, String value) {
        ResultSet result = this.select("*", column + " = " + value);
        return this.resultToMap(result);
    }

    public List<Map<String, Object>> where(String column, String operator, String value) {
        ResultSet result = this.select("*", column + " " + operator + " " + value);
        return this.resultToMap(result);
    }

    public List<Map<String, Object>> where(String column, String operator, String value, String column2,
            String operator2,
            String value2) {
        ResultSet result = this.select("*", column + " " + operator + " " + value + " AND " + column2 + " " + operator2
                + " " + value2);
        return this.resultToMap(result);
    }

    public List<Map<String, Object>> where(Map<String, String> conditions) {
        String conds = "";
        for (Map.Entry<String, String> entry : conditions.entrySet()) {
            conds += entry.getKey() + " = " + entry.getValue() + " AND ";
        }
        return this.resultToMap(this.select("*", conds.substring(0, conds.length() - 5)));
    }

    public boolean insert(Map<String, Object> data) {
        String columns = "";
        String values = "";
        for (Map.Entry<String, Object> entry : data.entrySet()) {
            columns += entry.getKey() + ", ";
            values += entry.getValue() + ", ";
        }
        columns = columns.substring(0, columns.length() - 2);
        values = values.substring(0, values.length() - 2);
        this.insert(this.table, columns, values.split(", "));
        return true;
    }

    public boolean update(Map<String, Object> data, String id) {
        String columns = "";
        for (Map.Entry<String, Object> entry : data.entrySet()) {
            columns += entry.getKey() + " = ? , ";
        }
        columns = columns.substring(0, columns.length() - 2);
        this.update(this.table, columns, data.values().toArray(), primaryKey + " = " + id);
        return true;
    }

    public boolean delete(String id) {
        this.delete(this.table, primaryKey + " = " + id);
        return true;
    }

    public boolean delete(Map<String, Object> conditions) {
        String conds = "";
        for (Map.Entry<String, Object> entry : conditions.entrySet()) {
            conds += entry.getKey() + " = " + entry.getValue() + " AND ";
        }
        this.delete(this.table, conds.substring(0, conds.length() - 5));
        return true;
    }
}
