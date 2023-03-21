package com.ipc2.convenience_stores.data;

import java.sql.SQLException;
import java.sql.DriverManager;
import java.sql.Connection;

public class ConnectionDB {

    private Connection connectionDB = null;
    private final String URL = "jdbc:mysql://localhost:3306/convenience_stores";
    private final String USER = "root";
    private final String PASSWORD = "4321";

    public Connection getConnection() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connectionDB = DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (SQLException | ClassNotFoundException e) {
            System.out.println("Error al registrar el driver de MySQL: " + e);
        }
        return connectionDB;
    }

    public boolean isDBstarted(){
        try (var preparedStatement = connectionDB.prepareStatement("SELECT * FROM admin_user");
             var resultSet = preparedStatement.executeQuery()){
                return resultSet.next();
        }catch (SQLException e){
            e.printStackTrace();
        }
        return false;
    }

    public void disconnect() {
        if (connectionDB != null) {
            try {
                connectionDB.close();
            } catch (SQLException e) {
                System.out.println("No se pudo cerrar la conexion" + e);
                e.printStackTrace();
            }
        }
    }
}

