package com.mavenforge.Schemas;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.mavenforge.Application;
import com.mavenforge.Database.Database;
import com.mavenforge.Database.MySQLDatabase;

public class MySQLSchema {
    private String table;
    private StringBuilder sql = new StringBuilder();
    private List<String> constraints = new ArrayList<>();
    private List<Map<String, Object>> columns = new ArrayList<>();
    private List<Map<String, Object>> foreignKeys = new ArrayList<>();

    public MySQLSchema(String table) {
        this.table = table;
    }

    public MySQLSchema id() {
        return this.id("id");
    }

    public MySQLSchema id(String columnName) {
        Map<String, Object> column = new HashMap<>();
        column.put("name", columnName);
        column.put("type", "INT");
        column.put("length", 11);
        column.put("auto_increment", true);
        column.put("primary_key", true);
        column.put("nullable", false);
        this.columns.add(column);
        return this;
    }

    public MySQLSchema autoIncrement() {
        if (this.columns.size() == 0) {
            System.out.println("You need to create a column first");
            return this;
        }

        if (this.columns.get(this.columns.size() - 1).get("type") != "INT") {
            System.out.println("Auto increment can only be applied to an INT column");
            return this;
        }

        this.columns.get(this.columns.size() - 1).put("auto_increment", true);
        return this;
    }

    public MySQLSchema primary() {
        if (this.columns.size() == 0) {
            System.out.println("You need to create a column first");
            return this;
        }

        this.columns.get(this.columns.size() - 1).put("primary_key", true);
        return this;
    }

    public MySQLSchema nullable() {
        this.columns.get(this.columns.size() - 1).put("nullable", true);
        return this;
    }

    public MySQLSchema string(String columnName) {
        Map<String, Object> column = new HashMap<>();
        column.put("name", columnName);
        column.put("type", "VARCHAR");
        column.put("length", 255);
        column.put("nullable", false);
        this.columns.add(column);

        return this;
    }

    public MySQLSchema string(String columnName, int length) {
        Map<String, Object> column = new HashMap<>();
        column.put("name", columnName);
        column.put("type", "VARCHAR");
        column.put("length", length);
        column.put("nullable", false);
        this.columns.add(column);

        return this;
    }

    public MySQLSchema integer(String columnName) {
        Map<String, Object> column = new HashMap<>();
        column.put("name", columnName);
        column.put("type", "INT");
        column.put("length", 11);
        column.put("nullable", false);
        this.columns.add(column);

        return this;
    }

    public MySQLSchema integer(String columnName, int length) {
        Map<String, Object> column = new HashMap<>();
        column.put("name", columnName);
        column.put("type", "INT");
        column.put("length", length);
        column.put("nullable", false);
        this.columns.add(column);

        return this;
    }

    public MySQLSchema text(String columnName) {
        Map<String, Object> column = new HashMap<>();
        column.put("name", columnName);
        column.put("type", "TEXT");
        column.put("length", 255);
        column.put("nullable", false);
        this.columns.add(column);

        return this;
    }

    public MySQLSchema text(String columnName, int length) {
        Map<String, Object> column = new HashMap<>();
        column.put("name", columnName);
        column.put("type", "TEXT");
        column.put("length", length);
        column.put("nullable", false);
        this.columns.add(column);

        return this;
    }

    public MySQLSchema longText(String columnName) {
        Map<String, Object> column = new HashMap<>();
        column.put("name", columnName);
        column.put("type", "LONGTEXT");
        column.put("length", 255);
        column.put("nullable", false);
        this.columns.add(column);

        return this;
    }

    public MySQLSchema longText(String columnName, int length) {
        Map<String, Object> column = new HashMap<>();
        column.put("name", columnName);
        column.put("type", "LONGTEXT");
        column.put("length", length);
        column.put("nullable", false);
        this.columns.add(column);

        return this;
    }

    public MySQLSchema enumValue(String columnName, String[] values) {
        Map<String, Object> column = new HashMap<>();
        column.put("name", columnName);
        column.put("type", "ENUM");
        column.put("values", values);
        column.put("nullable", false);
        this.columns.add(column);

        return this;
    }

    public MySQLSchema defaultValue(String value) {
        this.columns.get(this.columns.size() - 1).put("default", value);
        return this;
    }

    public MySQLSchema timestamps() {
        this.columns.add(new HashMap<String, Object>() {
            {
                put("name", "created_at");
                put("type", "TIMESTAMP");
                put("nullable", false);
                put("default", "CURRENT_TIMESTAMP");
            }
        });

        this.columns.add(new HashMap<String, Object>() {
            {
                put("name", "updated_at");
                put("type", "TIMESTAMP");
                put("nullable", false);
                put("default", "CURRENT_TIMESTAMP");
                put("on_update", "CURRENT_TIMESTAMP");
            }
        });

        return this;
    }

    public MySQLSchema timestamp(String columnName) {
        Map<String, Object> column = new HashMap<>();
        column.put("name", columnName);
        column.put("type", "TIMESTAMP");
        column.put("nullable", false);
        this.columns.add(column);

        return this;
    }

    public MySQLSchema timestamp(String columnName, String on_update) {
        Map<String, Object> column = new HashMap<>();
        column.put("name", columnName);
        column.put("type", "TIMESTAMP");
        column.put("nullable", false);
        column.put("on_update", on_update);
        this.columns.add(column);

        return this;
    }

    public MySQLSchema datetime(String columnName) {
        Map<String, Object> column = new HashMap<>();
        column.put("name", columnName);
        column.put("type", "DATETIME");
        column.put("nullable", false);
        this.columns.add(column);

        return this;
    }

    public MySQLSchema datetime(String columnName, String on_update) {
        Map<String, Object> column = new HashMap<>();
        column.put("name", columnName);
        column.put("type", "DATETIME");
        column.put("nullable", false);
        column.put("on_update", on_update);
        this.columns.add(column);

        return this;
    }

    public MySQLSchema date(String columnName) {
        Map<String, Object> column = new HashMap<>();
        column.put("name", columnName);
        column.put("type", "DATE");
        column.put("nullable", false);
        this.columns.add(column);

        return this;
    }

    public MySQLSchema floatValue(String columnName) {
        Map<String, Object> column = new HashMap<>();
        column.put("name", columnName);
        column.put("type", "FLOAT");
        column.put("total_digits", 8);
        column.put("decimal_digits", 2);
        column.put("nullable", false);
        this.columns.add(column);

        return this;
    }

    public MySQLSchema floatValue(String columnName, int total_digits) {
        Map<String, Object> column = new HashMap<>();
        column.put("name", columnName);
        column.put("type", "FLOAT");
        column.put("total_digits", total_digits);
        column.put("decimal_digits", 2);
        column.put("nullable", false);
        this.columns.add(column);

        return this;
    }

    public MySQLSchema floatValue(String columnName, int total_digits, int decimal_digits) {
        Map<String, Object> column = new HashMap<>();
        column.put("name", columnName);
        column.put("type", "FLOAT");
        column.put("total_digits", total_digits);
        column.put("decimal_digits", decimal_digits);
        column.put("nullable", false);
        this.columns.add(column);

        return this;
    }

    public MySQLSchema doubleValue(String columnName) {
        Map<String, Object> column = new HashMap<>();
        column.put("name", columnName);
        column.put("type", "DOUBLE");
        column.put("total_digits", 16);
        column.put("decimal_digits", 2);
        column.put("nullable", false);
        this.columns.add(column);

        return this;
    }

    public MySQLSchema doubleValue(String columnName, int total_digits) {
        Map<String, Object> column = new HashMap<>();
        column.put("name", columnName);
        column.put("type", "DOUBLE");
        column.put("total_digits", total_digits);
        column.put("decimal_digits", 2);
        column.put("nullable", false);
        this.columns.add(column);

        return this;
    }

    public MySQLSchema doubleValue(String columnName, int total_digits, int decimal_digits) {
        Map<String, Object> column = new HashMap<>();
        column.put("name", columnName);
        column.put("type", "DOUBLE");
        column.put("total_digits", total_digits);
        column.put("decimal_digits", decimal_digits);
        column.put("nullable", false);
        this.columns.add(column);

        return this;
    }

    public MySQLSchema booleanValue(String columnName) {
        Map<String, Object> column = new HashMap<>();
        column.put("name", columnName);
        column.put("type", "BOOLEAN");
        column.put("nullable", false);
        this.columns.add(column);

        return this;
    }

    public MySQLSchema foreignKey(String referenceTable, String referenceColumn) {
        Map<String, Object> currentColumn = this.columns.get(this.columns.size() - 1);

        Map<String, Object> foreignKey = new HashMap<>();
        foreignKey.put("column", currentColumn.get("name"));
        foreignKey.put("reference_table", referenceTable);
        foreignKey.put("reference_column", referenceColumn);
        this.foreignKeys.add(foreignKey);

        return this;
    }

    public MySQLSchema onDeleteCascade() {

        if (this.foreignKeys.size() == 0) {
            System.out.println("You need to create a foreign key first");
            return this;
        }

        Map<String, Object> currentForeignKey = this.foreignKeys.get(this.foreignKeys.size() - 1);

        if (!currentForeignKey.containsKey("column")) {
            System.out.println("You need to create a foreign key first");
            return this;
        }

        this.foreignKeys.get(this.foreignKeys.size() - 1).put("on_delete", "CASCADE");
        return this;
    }

    public MySQLSchema onDeleteSetNull() {

        if (this.foreignKeys.size() == 0) {
            System.out.println("You need to create a foreign key first");
            return this;
        }

        Map<String, Object> currentForeignKey = this.foreignKeys.get(this.foreignKeys.size() - 1);

        if (!currentForeignKey.containsKey("column")) {
            System.out.println("You need to create a foreign key first");
            return this;
        }

        this.foreignKeys.get(this.foreignKeys.size() - 1).put("on_delete", "SET NULL");
        return this;
    }

    public MySQLSchema onDeleteNoAction() {

        if (this.foreignKeys.size() == 0) {
            System.out.println("You need to create a foreign key first");
            return this;
        }

        Map<String, Object> currentForeignKey = this.foreignKeys.get(this.foreignKeys.size() - 1);

        if (!currentForeignKey.containsKey("column")) {
            System.out.println("You need to create a foreign key first");
            return this;
        }

        this.foreignKeys.get(this.foreignKeys.size() - 1).put("on_delete", "NO ACTION");
        return this;
    }

    public MySQLSchema onDeleteRestrict() {

        if (this.foreignKeys.size() == 0) {
            System.out.println("You need to create a foreign key first");
            return this;
        }

        Map<String, Object> currentForeignKey = this.foreignKeys.get(this.foreignKeys.size() - 1);

        if (!currentForeignKey.containsKey("column")) {
            System.out.println("You need to create a foreign key first");
            return this;
        }

        this.foreignKeys.get(this.foreignKeys.size() - 1).put("on_delete", "RESTRICT");
        return this;
    }

    public MySQLSchema onUpdateCascade() {

        if (this.foreignKeys.size() == 0) {
            System.out.println("You need to create a foreign key first");
            return this;
        }

        Map<String, Object> currentForeignKey = this.foreignKeys.get(this.foreignKeys.size() - 1);

        if (!currentForeignKey.containsKey("column")) {
            System.out.println("You need to create a foreign key first");
            return this;
        }

        this.foreignKeys.get(this.foreignKeys.size() - 1).put("on_update", "CASCADE");
        return this;
    }

    public MySQLSchema onUpdateSetNull() {

        if (this.foreignKeys.size() == 0) {
            System.out.println("You need to create a foreign key first");
            return this;
        }

        Map<String, Object> currentForeignKey = this.foreignKeys.get(this.foreignKeys.size() - 1);

        if (!currentForeignKey.containsKey("column")) {
            System.out.println("You need to create a foreign key first");
            return this;
        }

        this.foreignKeys.get(this.foreignKeys.size() - 1).put("on_update", "SET NULL");
        return this;
    }

    public MySQLSchema onUpdateNoAction() {

        if (this.foreignKeys.size() == 0) {
            System.out.println("You need to create a foreign key first");
            return this;
        }

        Map<String, Object> currentForeignKey = this.foreignKeys.get(this.foreignKeys.size() - 1);

        if (!currentForeignKey.containsKey("column")) {
            System.out.println("You need to create a foreign key first");
            return this;
        }

        this.foreignKeys.get(this.foreignKeys.size() - 1).put("on_update", "NO ACTION");
        return this;
    }

    public MySQLSchema onUpdateRestrict() {

        if (this.foreignKeys.size() == 0) {
            System.out.println("You need to create a foreign key first");
            return this;
        }

        Map<String, Object> currentForeignKey = this.foreignKeys.get(this.foreignKeys.size() - 1);

        if (!currentForeignKey.containsKey("column")) {
            System.out.println("You need to create a foreign key first");
            return this;
        }

        this.foreignKeys.get(this.foreignKeys.size() - 1).put("on_update", "RESTRICT");
        return this;
    }

    public MySQLSchema compositeKey(String... columns) {
        this.constraints.add("PRIMARY KEY (" + String.join(",", columns) + ")");
        return this;
    }

    public MySQLSchema unique() {
        this.columns.get(this.columns.size() - 1).put("unique", true);
        return this;
    }

    public String getSchema() {
        return this.sql.toString();
    }

    public String build() {

        this.sql.append("CREATE TABLE IF NOT EXISTS `" + this.table + "` (");

        for (Map<String, Object> column : this.columns) {

            this.sql.append("`" + column.get("name") + "` " + column.get("type"));

            if (column.containsKey("length")) {
                this.sql.append("(" + column.get("length") + ")");
            }

            if (column.containsKey("total_digits") && column.containsKey("decimal_digits")) {
                this.sql.append("(" + column.get("total_digits") + "," + column.get("decimal_digits") + ")");
            }

            if (column.containsKey("nullable")) {
                if ((boolean) column.get("nullable") == false) {
                    this.sql.append(" NOT NULL");
                } else {
                    this.sql.append(" NULL");
                }
            }

            if (column.containsKey("auto_increment")) {
                if ((boolean) column.get("auto_increment") == true) {
                    this.sql.append(" AUTO_INCREMENT");
                }
            }

            if (column.containsKey("primary_key")) {
                if ((boolean) column.get("primary_key") == true) {
                    this.sql.append(" PRIMARY KEY");
                }
            }

            if (column.containsKey("unique")) {
                if ((boolean) column.get("unique") == true) {
                    this.sql.append(" UNIQUE");
                }
            }

            if (column.containsKey("default")) {
                if (column.get("default") == "CURRENT_TIMESTAMP") {
                    this.sql.append(" DEFAULT " + column.get("default"));
                } else {
                    this.sql.append(" DEFAULT '" + column.get("default") + "'");
                }
            }

            if (column.containsKey("values")) {
                this.sql.append("(");
                String[] values = (String[]) column.get("values");

                for (int i = 0; i < values.length; i++) {
                    this.sql.append("'" + values[i] + "'");
                    if (i != values.length - 1) {
                        this.sql.append(",");
                    }
                }

                this.sql.append(")");

            }

            if (column.containsKey("on_update")) {
                this.sql.append(" ON UPDATE " + column.get("on_update"));
            }

            this.sql.append(",");

        }
        // this.sql.setLength(this.sql.length() - 1);

        this.sql = new StringBuilder(this.sql.substring(0, this.sql.length() - 1));

        for (Map<String, Object> foreignKey : this.foreignKeys) {
            this.sql.append(", FOREIGN KEY (`" + foreignKey.get("column") + "`) REFERENCES `"
                    + foreignKey.get("reference_table") + "`(`" + foreignKey.get("reference_column") + "`)");

            if (foreignKey.containsKey("on_delete")) {
                this.sql.append(" ON DELETE " + foreignKey.get("on_delete"));
            }

            if (foreignKey.containsKey("on_update")) {
                this.sql.append(" ON UPDATE " + foreignKey.get("on_update"));
            }

        }

        for (String constraint : this.constraints) {
            this.sql.append(", " + constraint);
        }

        this.sql.append(") ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;");

        return this.sql.toString();
    }

    public void drop() {
        this.sql.append("DROP TABLE IF EXISTS `" + this.table + "`;");
        this.execute();
    }

    public void execute() {
        Database database = Application.database;

        if (Application.database == null) {
            database = new Database();
            Application.database = database;
        }

        MySQLDatabase db = (MySQLDatabase) database.db;
        db.executeQuery(this.sql.toString());
        this.sql = new StringBuilder();
    }

}