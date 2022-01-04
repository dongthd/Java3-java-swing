/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Lab5;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 *
 * @author HP
 */
public class TestConnectDB {

    public static void main(String[] args) {
        // bước 1 : nạp Driver
        String urlSQL = "jdbc:sqlserver://localhost:1433;databaseName = DemoJava3"; // SQL Server
        // String urlSQL = "jdbc:sqlserver://localhost:1433;databaseName=DemoJava3;userName=sa;password=123456";
        try {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            //Connection connec = DriverManager.getConnection(url, "", "");
            try (
                    // bước 2 : kết nối đến DB
                    // Tên CSDL, địa chỉ ip của Sever, username, password (sa)
                    Connection connec = DriverManager.getConnection(urlSQL, "sa", "123456")) {
                //Connection connec = DriverManager.getConnection(url, "", "");
                System.out.println("Kết nối thành công !");
                //bước 3: gửi câu lệnh SQL
                Statement sql = connec.createStatement();
                ResultSet resul = sql.executeQuery("Select * from users");
                while (resul.next()) {
                    System.out.println(resul.getString(1) + " " + resul.getString(2));
                }
                // đóng kết nối
                resul.close();
                sql.close();
                connec.close();
            }
        } catch (ClassNotFoundException ex) {
            System.out.println("Lỗi không tìm thấy Driver !!!");
        } catch (SQLException ex1) {
            ex1.printStackTrace();
            System.out.println("Lỗi kết nối");
        }

    }

}
