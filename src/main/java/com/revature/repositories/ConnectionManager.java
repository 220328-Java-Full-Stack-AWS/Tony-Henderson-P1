package com.revature.repositories;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class ConnectionManager{

    private static Connection conn;

    private ConnectionManager(){}

    public static Connection getConnection() {
        if(conn == null){
            Properties props = new Properties();
            ClassLoader loader = Thread.currentThread().getContextClassLoader();
            InputStream input = loader.getResourceAsStream("application.properties");

            try{
                props.load(input);

                conn = DriverManager.getConnection(getConnString(props), props.getProperty("username"), props.getProperty("pass"));

                return conn;
            } catch (SQLException e) {
                System.out.println("Couldn't get the connection to the database");
                e.printStackTrace();
                System.exit(1);
            } catch (IOException e) {
                System.out.println("Could not find the file at the specified location");
                e.printStackTrace();
                System.exit(1);
            }
        }
        return conn;
    }

    public static void closeConnection(){
        try {
            conn.close();
            conn = null;
        } catch (SQLException e) {
            System.out.println("Couldn't close connection.");
            e.printStackTrace();
        }
    }

    private static String getConnString(Properties props){
        StringBuilder connString = new StringBuilder("jdbc:postgresql://");
        connString.append(props.getProperty("hostname"));
        connString.append(':');
        connString.append(props.getProperty("port"));
        connString.append('/');
        connString.append(props.getProperty("dbname"));

        return connString.toString();
    }
}
