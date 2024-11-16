package com.mavenforge.Contracts;

public abstract class DatabaseContract {
    protected String connectionString;
    protected String driverClass;

    public DatabaseContract(String connectionString, final String driverClass) {
        this.connectionString = connectionString;
        this.driverClass = driverClass;
    }

    protected void checkDriverPresence() throws Exception {
        if (!isDriverPresent()) {
            throw this.throwDriverNotFoundException();
        }
    };

    private boolean isDriverPresent() {
        try {
            Class.forName(this.driverClass);
            return true;
        } catch (ClassNotFoundException e) {
            return false;
        }
    }

    private Exception throwDriverNotFoundException() {
        return new Exception(
                "Database Driver not found, please add the driver " + this.driverClass + " to your project.");
    }

    public abstract void setTable(String table);

}
