/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Lab5;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

/**
 *
 * @author HP
 */
public class StudentDAO {

    Connection connec;

    public StudentDAO() {
        String urlSQL = "jdbc:sqlserver://localhost:1433;databaseName = QLSinhVien"; // SQL Server
        try {
            //b1
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            //b2
            connec = DriverManager.getConnection(urlSQL, "sa", "123456");
            //Connection connec = DriverManager.getConnection(url, "", "");
            System.out.println("Kết nối DB thành công !");
        } catch (ClassNotFoundException ex) {
            System.out.println("Lỗi không tìm thấy Driver !!!");
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Lỗi kết nối");
        }

    }

    public void closeConnec() {
        try {
            connec.close();
        } catch (SQLException e) {
            System.out.println("Lỗi đóng kết nối");
        }
    }

    public int insert(Student obj) {
        int r = 0;
        try {
            Statement st = connec.createStatement();
            r = st.executeUpdate("Insert into Student values ( '" + obj.getMaSV()
                    + "',N'" + obj.getHoTen() + "',N'" + obj.getEmail()
                    + "','" + obj.getDienThoai() + "','" + obj.getGioiTinh()
                    + "',N'" + obj.getDiaChi() + "') ");

        } catch (SQLException ex) {
            ex.printStackTrace();
            System.out.println("Lỗi đóng kết nối");
        }
        return r;
    }

    public int update(Student obj) {
        int r = 0;
        try {

            PreparedStatement ps = connec.prepareStatement("update Student set Hoten =? ,"
                    + "Email=?, SoDT=?, Gioitinh=?, Diachi=? where MaSV=?");
            ps.setString(1, obj.getHoTen());
            ps.setString(2, obj.getEmail());
            ps.setString(3, obj.getDienThoai());
            ps.setInt(4, obj.getGioiTinh());
            ps.setString(5, obj.getDiaChi());
            ps.setString(6, obj.getMaSV());

            System.out.println(obj.getMaSV());
            r = ps.executeUpdate();
        } catch (SQLException ex) {
            ex.printStackTrace();
            System.out.println("Lỗi đóng kết nối");
        }
        return r;
    }

    public int delete(String masv) {
        int r = 0;
        try {
            PreparedStatement ps = connec.prepareStatement("Delete from Student where MaSV = ?");
            ps.setString(1, masv);
            r = ps.executeUpdate();
        } catch (SQLException ex) {
            ex.printStackTrace();
            System.out.println("Lỗi đóng kết nối");
        }
        return r;
    }
}
