package com.mavenforge.Templates;

import java.util.Map;

public class Default {
    public static String render(String type, Map<String, String> data) {
        switch (type) {
            case "error":
                return Default.error(data);
            default:
                return "<h1> Template With Name " + type + " Not Found </h1>";
        }
    }

    private static String error(Map<String, String> data) {

        String error_message = (String) data.get("error_message");
        String error_details = (String) data.get("error_details");

        String html = "<!DOCTYPE html>\n" +
                "<html lang=\"en\">\n" +
                "\n" +
                "<head>\n" +
                "    <meta charset=\"UTF-8\">\n" +
                "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n" +
                "    <title>Error - " + error_message + "</title>\n" +
                "</head>\n" +
                "\n" +
                "<body>\n" +
                "    <h1>Encountered an error !!</h1>\n" +
                "    <h4>" + error_message + "</h4>\n" +
                "    <p>\n" +
                "        Details: " + error_details + "\n" +
                "    </p>\n" +
                "</body>\n" +
                "\n" +
                "</html>";

        return html;
    }
}
