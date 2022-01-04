/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Lab6Part2;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 *
 * @author HP
 */
public class DBConnect {

    Connection connect;
    String urlSQL;
    String userName;
    String password;

    public DBConnect() {
        urlSQL = "jdbc:sqlserver://localhost:1433;databaseName = Books;userName=sa;password=123456";
        try {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            connect = DriverManager.getConnection(urlSQL);
            System.out.println("Connect OK !");
        } catch (ClassNotFoundException ex) {
            System.out.println("Lỗi không tìm thấy Driver !!!");
            ex.printStackTrace();
        } catch (SQLException ex) {
            ex.printStackTrace();
            System.out.println("Lỗi kết nối");
        }
    }

    public Connection getConnect() {
        return connect;
    }

    public void setConnect(Connection connect) {
        this.connect = connect;
    }

    // đóng kết nối
    public void disconnect() {
        try {
            connect.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

    }
}
