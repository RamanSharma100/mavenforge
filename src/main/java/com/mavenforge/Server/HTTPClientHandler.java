package com.mavenforge.Server;

import java.net.Socket;
import java.lang.reflect.Constructor;

import com.mavenforge.Http.Router;
import com.mavenforge.Application;
import com.mavenforge.Http.HTTPRequest;
import com.mavenforge.Http.HTTPResponse;
import com.mavenforge.Utils.ImportClass;

public class HTTPClientHandler implements Runnable {
    private Socket clientSocket;
    private HTTPResponse response;
    private HTTPRequest request;

    public HTTPClientHandler(Socket clientSocket) {
        this.clientSocket = clientSocket;

    }

    private void handleRequest() {
        try {

            this.request = new HTTPRequest(this.clientSocket);
            this.response = new HTTPResponse(this.clientSocket, this.request);

            Application.router = new Router(this.request, this.response);

            if (this.request.getPath() == null) {
                clientSocket.close();
                return;
            }

            this.loadRoutes();

            Application.router.resolve();

            clientSocket.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        handleRequest();
    }

    private void loadRoutes() {
        try {
            String routesPackage = Application.rootClassPackage + ".routes.Web";
            Class<?> RoutesClass = ImportClass.fromPackage(routesPackage);

            if (RoutesClass != null) {
                try {
                    Constructor<?> RoutesConstructor = RoutesClass.getDeclaredConstructor();
                    Object Routes = RoutesConstructor.newInstance();

                    RoutesClass.getMethod("init").invoke(Routes);

                } catch (Exception e) {
                    e.printStackTrace();
                }

            } else {
                System.out.println("Routes class not found");
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

}
