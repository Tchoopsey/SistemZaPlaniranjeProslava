package com.dbutil;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DBTest {

    public static void main(String[] args) {
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SHOW TABLES")) {
            System.out.println("Tables in database: ");
            while (rs.next()) {
                String tableName = rs.getString(1);
                System.out.println("- " + tableName);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
