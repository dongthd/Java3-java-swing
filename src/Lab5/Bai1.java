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
public class Bai1 {

    public static void main(String[] args) {
        // bước 1 : nạp Driver
        String urlSQL = "jdbc:sqlserver://localhost:1433;databaseName = QLSinhVien"; // SQL Server
        // String urlSQL = "jdbc:sqlserver://localhost:1433;databaseName=DemoJava3;userName=sa;password=123456";
        try {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            //Connection connec = DriverManager.getConnection(url, "", "");
            try (
                    // bước 2 : kết nối đến DB
                    // Tên CSDL, địa chỉ ip của Sever, username, password (sa)
                    Connection connec = DriverManager.getConnection(urlSQL, "sa", "123456")) {
                System.out.println("Kết nối thành công !");
                //bước 3: gửi câu lệnh SQL
                Statement sql = connec.createStatement();
                ResultSet rs = sql.executeQuery("Select * from student");
                while (rs.next()) {
                    System.out.print(rs.getString("MaSV") + ", ");
                    System.out.print(rs.getString("Hoten") + ", ");
                    System.out.print(rs.getString("Email") + ", ");
                    System.out.print(rs.getString("SoDT") + ", ");
                    System.out.println(rs.getString("Gioitinh"));
                }
                // đóng kết nối
                rs.close();
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
