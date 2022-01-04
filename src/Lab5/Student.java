/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Lab5;

/**
 *
 * @author HP
 */
public class Student {

    private String MaSV;
    private String HoTen;
    private String Email;
    private String DienThoai;
    private int GioiTinh;
    private String DiaChi;

    public Student() {
    }

    public Student(String MaSV, String HoTen, String Email, String DienThoai, int GioiTinh, String DiaChi) {
        this.MaSV = MaSV;
        this.HoTen = HoTen;
        this.Email = Email;
        this.DienThoai = DienThoai;
        this.GioiTinh = GioiTinh;
        this.DiaChi = DiaChi;
    }

    public String getMaSV() {
        return MaSV;
    }

    public void setMaSV(String MaSV) {
        this.MaSV = MaSV;
    }

    public String getHoTen() {
        return HoTen;
    }

    public void setHoTen(String HoTen) {
        this.HoTen = HoTen;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String Email) {
        this.Email = Email;
    }

    public String getDienThoai() {
        return DienThoai;
    }

    public void setDienThoai(String DienThoai) {
        this.DienThoai = DienThoai;
    }

    public int getGioiTinh() {
        return GioiTinh;
    }

    public void setGioiTinh(int GioiTinh) {
        this.GioiTinh = GioiTinh;
    }

    public String getDiaChi() {
        return DiaChi;
    }

    public void setDiaChi(String DiaChi) {
        this.DiaChi = DiaChi;
    }

}
