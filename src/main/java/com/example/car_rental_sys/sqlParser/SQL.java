package com.example.car_rental_sys.sqlParser;

import com.example.car_rental_sys.ConfigFile;
import com.example.car_rental_sys.ToolsLib.PlatformTools;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;
import java.util.ArrayList;

public class SQL {

    private static Connection getConnection() throws Exception {
        return DriverManager.getConnection(ConfigFile.dbUrl, ConfigFile.dbUser, ConfigFile.dbPassword);
    }

    public static ArrayList<String[]> query(String sql) {
        ArrayList<String[]> result = new ArrayList<>();
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            ResultSetMetaData rsmd = rs.getMetaData();
            int columnCount = rsmd.getColumnCount();

            while (rs.next()) {
                String[] row = new String[columnCount];
                for (int i = 1; i <= columnCount; i++) {
                    Object val = rs.getObject(i);
                    row[i - 1] = (val == null) ? "null" : val.toString();
                }
                result.add(row);
            }
        } catch (Exception e) {
            System.err.println("SQL Query Error: " + sql);
            e.printStackTrace();
        }
        return result;
    }

    public static boolean execute(String sql) {
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement()) {

            int rowsAffected = stmt.executeUpdate(sql);
            return rowsAffected > 0;
        } catch (Exception e) {
            System.err.println("SQL Execute Error: " + sql);
            e.printStackTrace();
            return false;
        }
    }
}
