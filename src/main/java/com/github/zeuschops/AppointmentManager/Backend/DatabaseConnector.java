package com.github.zeuschops.AppointmentManager.Backend;

import java.io.File;
import java.io.FileNotFoundException;
import java.sql.*;
import java.util.Scanner;

import com.github.zeuschops.AppointmentManager.Main;

public class DatabaseConnector {

    private static DatabaseConnector INSTANCE;
    public static DatabaseConnector getInstance() {
        return INSTANCE == null ? INSTANCE = new DatabaseConnector() : INSTANCE;
    }

    private Connection connection;

    public DatabaseConnector() {
        try {
            //Reading from a file now...
            File file = new File("src/main/java/config.txt");
            Scanner scan = new Scanner(file);
            String url = "";
            String tableName = "";
            String username = "";
            String password = "";
            String[] spl;
            while(scan.hasNextLine()) {
                spl = scan.nextLine().toLowerCase().split(":");
                switch(spl[0]) {
                    case "url":
                        url = spl[1];
                        break;
                    case "username":
                        username = spl[1];
                        break;
                    case "table":
                        tableName = spl[1];
                        break;
                    case "password":
                        password = spl[1];
                        break;
                    default:
                        break;
                }
                /*if(spl[0].startsWith("url")) {
                    url = spl[1];
                } else if(spl[0].startsWith("username")) {
                    username = spl[1];
                } else if(spl[0].startsWith("table")) {
                    tableName = spl[1];
                } else if(spl[0].startsWith("password")) {
                    password = spl[1];
                }*/
            }
            scan.close();
            connection = DriverManager.getConnection("jdbc:mysql://" + url + ":3306/" + tableName, username, password);
        } catch(SQLException | FileNotFoundException e) {
            if(Main.langIdent == LanguageIdentifier.FRENCH) {
                Main.newError("Erreur de connexion à la base de données", "En quittant l'application, veuillez rouvrir avec une connexion stable au serveur.");
            } else if(Main.langIdent == LanguageIdentifier.SPANISH) {
                Main.newError("Error de conexión a la base de datos","Salida de la aplicación, vuelva a abrir con una conexión estable al servidor.");
            } else {
                Main.newError("Database connection error", "Application Exiting, please reopen with a stable connection to the server.");
            }
        	System.exit(-404);
        }
    }

    public ResultSet request(String reqStr) {
        if(connection != null) {
            try {
                Statement statement = connection.createStatement();
                if(reqStr.contains(";")) { //Over-glorified input validation
                   throw new SQLException("Unexpected character found.");
                }
                if(statement.execute(reqStr)) {
                    return statement.getResultSet();
                }
            } catch(SQLException ex) {
                if(Main.langIdent == LanguageIdentifier.FRENCH) {
                    Main.newError("Erreur de Demande", "Erreur de demande d'application, réessayez plus tard.");
                } else if(Main.langIdent == LanguageIdentifier.SPANISH) {
                    Main.newError("Solicitud de error", "Error de solicitud de aplicación, intente nuevamente más tarde.");
                } else {
                    Main.newError("Request Error", "Application request error, try request again later.");
                }
                ex.printStackTrace();
            }
        }
        return null;
    }
}
