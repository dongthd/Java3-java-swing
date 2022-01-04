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
public class StandardDAO {

    ArrayList<Standard> getAllStandard(DBConnect db) {
        ArrayList<Standard> kq = new ArrayList<>();
        try {
            PreparedStatement ps = db.getConnect().prepareStatement("select * from standard");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                kq.add(new Standard(rs.getInt(1), rs.getString(2), rs.getDouble(3)));
            }
            ps.close();
            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return kq;
    }

    double getFeeByName(DBConnect connect, String standName) {
        double kq = 0;
        try {
            PreparedStatement ps = connect.getConnect().prepareStatement("select fee from standard where standardName = ?");
            ps.setString(1, standName);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                kq = rs.getDouble(1);
            }
            ps.close();
            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return kq;
    }

    int getIDByName(DBConnect connect, String standName) {
        int kq = 0;
        try {
            PreparedStatement ps = connect.getConnect().prepareStatement("select id_stand from standard where standardName = ?");
            ps.setString(1, standName);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                kq = rs.getInt(1);
            }
            ps.close();
            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return kq;
    }

    ArrayList<Student> getAllStudent(DBConnect dbConnect) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
