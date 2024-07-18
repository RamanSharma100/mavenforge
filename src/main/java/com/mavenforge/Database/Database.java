package com.mavenforge.Database;

import io.github.cdimascio.dotenv.Dotenv;

import com.mavenforge.Utils.Constants.DatabaseType;

public class Database {
    public Object db = null;
    public DatabaseType databaseType = DatabaseType.mysql;
    private Dotenv env = Dotenv.configure().load();

    public Database() {
        boolean databaseEnabled = Boolean.parseBoolean(this.env.get("ENABLE_DATABASE", "false"));

        if (!databaseEnabled) {
            System.out.println("Database is not enabled. Please enable the database in the .env file.");
            System.out.println("Exiting...");
            System.exit(0);
            return;
        }

        String databaseUser = this.env.get("DATABASE_USER", "root");
        String databasePassword = this.env.get("DATABASE_PASSWORD", "");
        String databaseHost = this.env.get("DATABASE_HOST", "localhost");
        String databaseName = this.env.get("DATABASE_NAME", "mavenforge");
        int databasePort = Integer.parseInt(this.env.get("DATABASE_PORT", "3306"));
        String databaseType = this.env.get("DATABASE_TYPE", DatabaseType.mysql.toString());

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
