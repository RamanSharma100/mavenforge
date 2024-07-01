package com.mavenforge.Contracts;

import java.sql.Connection;
import java.sql.SQLException;

public abstract class DatabaseContract {
    public String connectionString;

    public DatabaseContract(String connectionString){
        this.connectionString = connectionString;
    }

    public abstract Connection connect() throws SQLException;

}
