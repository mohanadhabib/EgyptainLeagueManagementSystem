package com.example.project.controllers;

import java.sql.*;

public abstract class DatabaseOperations {

    public static Connection con;
    public void setCon() throws SQLException{
        con = DriverManager.getConnection("jdbc:mariadb://localhost:3306/egyptianleague",
                "mohanad", "mohanad14112001");
    }
    public ResultSet getData(String sqlQuery) throws SQLException{
        Statement stmt = con.createStatement();
        return stmt.executeQuery(sqlQuery);
    }
    public ResultSet searchById(String sqlQuery,int id) throws SQLException{
        Statement stmt = con.createStatement();
        return stmt.executeQuery(sqlQuery);
    }
    public ResultSet searchByName(String sqlQuery ,String name) throws SQLException{
        Statement stmt = con.createStatement();
        return stmt.executeQuery(sqlQuery);
    }
    public ResultSet searchByDate(String sqlQuery , Date date) throws SQLException{
        Statement stmt = con.createStatement();
        return stmt.executeQuery(sqlQuery);
    }
}
