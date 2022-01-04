/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Lab6;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

/**
 *
 * @author HP
 */
public class BookDAO { // data access object

    Connection connec; // đối tượng kết nối với database

    public BookDAO() {
        try {
            //b1: Nạp driver
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            String urlSQL = "jdbc:sqlserver://localhost:1433;databaseName = Books;userName=sa;password=123456"; // SQL Server
            //b2 : Định nghĩa connection URL
            connec = DriverManager.getConnection(urlSQL);
            System.out.println("Connect OK !");
        } catch (ClassNotFoundException ex) {
            System.out.println("Lỗi không tìm thấy Driver !!!");
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Lỗi kết nối");
        }
    }

    public void disconnect() { // đóng kết nối
        try {
            connec.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // truy vẫn tất cả sách
    ArrayList<Book> getAllBook() {
        ArrayList<Book> kq = new ArrayList<>();
        // gửi câu lệnh truy vấn đến server
        try {
            Statement st = connec.createStatement();
            ResultSet rs = st.executeQuery("Select * from book");
            while (rs.next()) { //rs.next() -->boolean --> false: duyệt qua hết các hàng  
                Book b = new Book(rs.getInt(1), rs.getString(2), rs.getDouble(3));
                kq.add(b);
            }
            rs.close();
            st.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return kq;
    }

    //tìm kiếm sách theo tên
    ArrayList<Book> getBookByTitle(String title) {
        ArrayList<Book> kq = new ArrayList<>();
        // gửi câu lệnh truy vẫn đến server
        try {
            Statement st = connec.createStatement();
            ResultSet rs = st.executeQuery("Select * from book where title like N'%" + title + "%'"); // select
            while (rs.next()) { //rs.next() -->boolean --> false: duyệt qua hết các hàng
                Book b = new Book(rs.getInt(1), rs.getString(2), rs.getDouble(3));
                kq.add(b);
            }
            rs.close();
            st.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return kq;
    }

    public int Delete(int id) {
        int r = 0;
        try {
            PreparedStatement ps = connec.prepareStatement("delete from book where id =?");
            ps.setInt(1, id);
            r = ps.executeUpdate(); //Update, insert, delete --> int
            ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return r;
    }
}
