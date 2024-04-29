package com.mavenforge.Web;

import com.mavenforge.Http.Route;
import com.mavenforge.Contracts.RoutesContract;

public class Routes extends RoutesContract {
    public void init() {
        Route.get("/", "HomeController@index");
        Route.get("/about", "HomeController@about");
        Route.get("/contact", "HomeController@contact");
    }
}
