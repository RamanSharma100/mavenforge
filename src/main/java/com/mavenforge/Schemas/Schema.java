package com.mavenforge.Schemas;

import java.util.function.Consumer;

import com.mavenforge.Contracts.SchemaContract;
import com.mavenforge.Utils.Constants;

public class Schema extends SchemaContract {

    public static void create(String table, Consumer<MySQLSchema> callback) {
        String db = Constants.env.get("DATABASE_TYPE", "mysql");

        switch (db) {
            case "mysql":
                MySQLSchema blueprint = new MySQLSchema(table);

                Consumer<MySQLSchema> consumer = (Consumer<MySQLSchema>) callback;
                consumer.accept(blueprint);
                blueprint.build();
                blueprint.execute();
                break;
            default:
                System.out.println("Database type not supported");
                break;
        }
    }

}