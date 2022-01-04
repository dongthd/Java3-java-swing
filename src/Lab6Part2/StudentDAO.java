/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Lab6Part2;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 *
 * @author HP
 */
public class StudentDAO {

    int insert(DBConnect connect, Student st) {
        int r = 0;
        try {
            PreparedStatement ps = connect.getConnect().prepareStatement("insert into student values (?,?,?,?,?,?,?)");
            ps.setInt(1, st.getRegId()); // tương tự 6 thuộc tính
            ps.setString(2, st.getName());
            ps.setString(3, st.getAddress());
            ps.setString(4, st.getParentName());
            ps.setString(5, st.getPhone());
            ps.setInt(6, st.getStandID());
            ps.setDate(7, st.getRegDate());
            r = ps.executeUpdate();
            ps.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return r;
    }

    int update(DBConnect connect, Student st) {
        int r = 0;
        try {
            PreparedStatement ps = connect.getConnect().prepareStatement
            ("update student set regID=?,where name=?, address=?, parentName=?, phone=?, standID=?, regDate=?");
            ps.setInt(1, st.getRegId()); // tương tự 6 thuộc tính
            ps.setString(2, st.getName());
            ps.setString(3, st.getAddress());
            ps.setString(4, st.getParentName());
            ps.setString(5, st.getPhone());
            ps.setInt(6, st.getStandID());
            ps.setDate(7, st.getRegDate());
            r = ps.executeUpdate();
            ps.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return r;
    }

    int delete(DBConnect connect, int stuID) {
        int r = 0;
        try {
            PreparedStatement ps = connect.getConnect().prepareStatement("delete from student where regID = ?");
            ps.setInt(1, stuID);// tương tự 6 thuộc tính
            r = ps.executeUpdate();
            ps.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return r;
    }

    ArrayList<Student> getAllStudent(DBConnect db) {
        ArrayList<Student> kq = new ArrayList<>();
        try {
            PreparedStatement ps = db.getConnect().prepareStatement("select * from student");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                kq.add(new Student(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5), rs.getInt(6), rs.getDate(7)));
            }
            ps.close();
            rs.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return kq;
    }

}
