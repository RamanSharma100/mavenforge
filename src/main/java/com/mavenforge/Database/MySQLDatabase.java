package com.mavenforge.Database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.mavenforge.Contracts.DatabaseContract;

public class MySQLDatabase extends DatabaseContract {
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
        } catch (Exception e) {
            throw new RuntimeException(e.toString());
        }
    }

    public MySQLDatabase(String connectionString, boolean isCLI) {
        super(connectionString, "com.mysql.cj.jdbc.Driver");
        try {
            connection = this.connect();
        } catch (Exception e) {
            throw new RuntimeException(e.toString());
        }
    }

    private Connection connect() throws Exception {
        this.checkDriverPresence();
        try {
            return getConnection();
        } catch (SQLException e) {
            System.err.println(e.toString());
            throw new SQLException(e.toString());
        }
    }

    private Connection getConnection() throws SQLException {
        return DriverManager.getConnection(this.connectionString);
    }

    public void setTable(String table) {
        this.table = table;
    }

    public boolean tableExists() {
        query = "SELECT * FROM information_schema.tables WHERE table_schema = ? AND table_name = ?";
        try {
            PreparedStatement preparedQuery = connection.prepareStatement(query);
            preparedQuery.setString(1, this.databaseName);
            preparedQuery.setString(2, this.table);
            ResultSet result = preparedQuery.executeQuery();
            return result.next();
        } catch (SQLException e) {
            throw new RuntimeException("Could not check if the table exists. Please check your query.");
        }
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
        query = "DESCRIBE " + tableName;
        try {
            connection.createStatement().execute(query);
        } catch (SQLException e) {
            throw new RuntimeException("Could not describe the table. Please check your query.");
        }
    }

    public void executeQuery(String query) {
        this.query = query;
        try {
            connection.createStatement().execute(query);
        } catch (SQLException e) {
            throw new RuntimeException("Could not execute the query. Please check your query. Error:" + e.toString());
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
        }
    }

    public void createTable(String tableName, String columns) {
        query = "CREATE TABLE IF NOT EXISTS " + tableName + " (" + columns + ")";
        try {
            connection.createStatement().execute(query);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public ResultSet select(String columns, String condition) {
        query = "SELECT " + columns + " FROM " + table + " WHERE " + condition;
        ResultSet result = null;
        try {
            PreparedStatement preparedQuery = connection.prepareStatement(query);
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
        query = "INSERT INTO " + tableName + " (" + columns + ") VALUES (" + String.join(",", new String[values.length])
                + ")";
        try {
            PreparedStatement preparedQuery = connection.prepareStatement(query);
            for (int i = 0; i < values.length; i++) {
                preparedQuery.setObject(i + 1, values[i]);
            }
            preparedQuery.execute();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void update(String tableName, String columns, Object[] values, String condition) {
        query = "UPDATE " + tableName + " SET " + columns + " WHERE " + condition;
        try {
            PreparedStatement preparedQuery = connection.prepareStatement(query);
            for (int i = 0; i < values.length; i++) {
                preparedQuery.setObject(i + 1, values[i]);
            }
            preparedQuery.execute();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void delete(String tableName, String condition) {
        query = "DELETE FROM " + tableName + " WHERE " + condition;
        try {
            PreparedStatement preparedQuery = connection.prepareStatement(query);
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

    public List<Map<String, Object>> all() {
        String query = "SELECT * FROM " + table;
        ResultSet result = this.customQuery(query, List.of());
        return this.resultToMap(result);
    }

    public List<Map<String, Object>> find() {
        return this.all();
    }

    public <T> List<Map<String, Object>> find(T id) {
        ResultSet result = this.select("*", primaryKey + " = ?");
        return this.resultToMap(result);
    }

    public List<Map<String, Object>> find(String condition) {
        String query = "SELECT * FROM " + table + " WHERE " + condition;
        ResultSet result = this.customQuery(query, List.of());
        return this.resultToMap(result);
    }

    public List<Map<String, Object>> find(String condition, String value) {
        String query = "SELECT * FROM " + table + " WHERE " + condition;
        ResultSet result = this.customQuery(query, List.of(value));
        return this.resultToMap(result);
    }

    public Map<String, Object> findOne(String id) {
        ResultSet result = this.select("*", primaryKey + " = ?");
        return this.resultToMap(result).get(0);
    }

    public Map<String, Object> first() {
        String query = "SELECT * FROM " + table + " LIMIT 1";
        ResultSet result = this.customQuery(query, List.of());
        return this.resultToMap(result).get(0);
    }

    public List<Map<String, Object>> where(String column, String value) {
        String condition = column + " = ?";
        return this.find(condition, value);
    }

    public List<Map<String, Object>> where(String column, String operator, String value) {
        String condition = column + " " + operator + " ?";
        return this.find(condition, value);
    }

    public List<Map<String, Object>> where(String column, String operator, String value, String column2,
            String operator2, String value2) {
        Map<String, String> conditions = new LinkedHashMap<>();
        conditions.put(column + " " + operator, value);
        conditions.put(column2 + " " + operator2, value2);
        return this.buildFindQuery(conditions, "AND");
    }

    public List<Map<String, Object>> where(Map<String, String> conditions) {
        return this.buildFindQuery(conditions, "AND");
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

        String query = "INSERT INTO " + table + " (" + columns + ") VALUES (" + values + ")";

        try {
            PreparedStatement preparedQuery = connection.prepareStatement(query);
            preparedQuery.execute();
            return true;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private List<Map<String, Object>> buildFindQuery(Map<String, String> conditions, String operator) {
        String whereCondition = buildWhereCondition(conditions, operator);
        return this.find(whereCondition);
    }

    private String buildWhereCondition(Map<String, String> conditions, String operator) {
        StringBuilder condition = new StringBuilder();
        for (Map.Entry<String, String> entry : conditions.entrySet()) {
            condition.append(entry.getKey()).append(" = ? ");
            if (conditions.size() > 1)
                condition.append(operator);
        }
        return condition.substring(0, condition.length() - operator.length());
    }
}
