package com.mavenforge.Controllers;

public class Controller extends com.mavenforge.Contracts.ControllerContract {
    public void view(String viewName, Object data) {
        System.out.println("View: " + viewName + " Data: " + data);
    }
}