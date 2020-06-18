import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.desktop.SystemSleepEvent;
import java.io.*;
import java.sql.*;
import java.util.*;

public class DBConnection {
    private static Connection conn = null;
    private static PreparedStatement prep = null;
    private static String url = "";

   static
   {
       Properties props = new Properties();
        InputStream in = DBConnection.class.getResourceAsStream("ConfigFile.txt");
        try {
        props.load(in);
        } catch (IOException e) {
             System.out.println("Properties file not found");
        }

        url = props.getProperty("db.url");
   }


    public static Connection getConnection() {
        try {
            //conn = DriverManager.getConnection(url, user, password);
            conn = DriverManager.getConnection(url,"root","");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return conn;
    }

    public static void insertData(String Query) {
        conn = getConnection();
        try {
            prep = conn.prepareStatement(Query);
            prep.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static String selectId(String Query) {
        conn = getConnection();
        String id = "";
        try {
            prep = conn.prepareStatement(Query);
            ResultSet rs = prep.executeQuery();
            while (rs.next()) {
                id = rs.getString(1);
                break;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return id;
    }

    public static DefaultComboBoxModel fillDropDown(DefaultComboBoxModel model,String Query) {
        conn = getConnection();
        try {
            prep = conn.prepareStatement(Query);
            ResultSet rs = prep.executeQuery();

            while (rs.next()) {
                model.addElement(new ComboBoxItem(rs.getInt(1), rs.getString(2)));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return model;
    }

    public static ArrayList<String> SelectColumns(String Query) {
        conn = getConnection();
        ArrayList<String> column = new ArrayList<String>();
        try {
            prep = conn.prepareStatement(Query);
            ResultSet rs = prep.executeQuery();
            ResultSetMetaData rsmd = rs.getMetaData();
            int columnsNumber = rsmd.getColumnCount();

            while (rs.next()) {
                for (int i = 1; i < columnsNumber + 1; i++) {

                    column.add(rs.getString(i));
                }

            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return column;
    }
    public static void update(String query)
    {
        conn = getConnection();
        try {
            prep = conn.prepareStatement(query);
            prep.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public static MyModel filltable(MyModel model,String Query){
        conn = getConnection();
        try {
            prep = conn.prepareStatement(Query);
            ResultSet rs = prep.executeQuery();
            ResultSetMetaData metaData = rs.getMetaData();
            int columnsNumber = metaData.getColumnCount();
            while (rs.next())
            {
                Object[] data = new Object[columnsNumber];
                for (int i = 0; i < columnsNumber; i++) {
                    data[i] = rs.getObject(i+1);
                }
                    model.AddRow(data);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return model;
    }
    public  static void delete(String Query)
    {
        conn = getConnection();
        try {
            prep = conn.prepareStatement(Query);
            prep.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
