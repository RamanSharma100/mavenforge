package com.mavenforge.Database;

import com.mavenforge.Utils.Constants;
import com.mavenforge.Utils.Constants.DatabaseType;

public class Database {
    public Object db = null;
    public DatabaseType databaseType = DatabaseType.mysql;

    public Database() {
        String databaseUser = Constants.env.get("DATABASE_USER", "root");
        String databasePassword = Constants.env.get("DATABASE_PASSWORD", "");
        String databaseHost = Constants.env.get("DATABASE_HOST", "localhost");
        String databaseName = Constants.env.get("DATABASE_NAME", "mavenforge");
        int databasePort = Integer.parseInt(Constants.env.get("DATABASE_PORT", "3306"));
        String databaseType = Constants.env.get("DATABASE_TYPE", Constants.DatabaseType.mysql.toString());

        switch (databaseType) {
            case "mysql":
                String connectionString = "jdbc:mysql://" + databaseHost + ":" + databasePort + "/" + databaseName
                        + "?user=" + databaseUser;
                if (databasePassword != null && !databasePassword.isEmpty()) {
                    connectionString += "&password=" + databasePassword;
                }
                this.db = new MySQLDatabase(connectionString);
                this.databaseType = DatabaseType.mysql;
                break;
            default:
                String defaultConnectionString = "jdbc:mysql://" + databaseHost + ":" + databasePort + "/"
                        + databaseName + "?user=" + databaseUser;
                if (databasePassword != null && !databasePassword.isEmpty()) {
                    defaultConnectionString += "&password=" + databasePassword;
                }
                this.db = new MySQLDatabase(defaultConnectionString);
                this.databaseType = DatabaseType.mysql;
                break;
        }
    }
}
