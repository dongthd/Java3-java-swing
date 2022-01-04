/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Assignment;

/**
 *
 * @author HP
 */
public class DiemSV {

    int ID = 1;
    String maSV = "";
    String hoTen = "";
    float  tiengAnh = 0;
    float  tinHoc = 0;
    float  GDTC = 0;
    double DiemTB = 0;

    public DiemSV() {
    }

    public DiemSV(int ID, String maSV, String hoTen, float tiengAnh, float tinHoc, float GDTC, double DiemTB) {
        this.ID = ID;
        this.maSV = maSV;
        this.hoTen = hoTen;
        this.tiengAnh = tiengAnh;
        this.tinHoc = tinHoc;
        this.GDTC = GDTC;
        this.DiemTB = DiemTB;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getMaSV() {
        return maSV;
    }

    public void setMaSV(String maSV) {
        this.maSV = maSV;
    }

    public String getHoTen() {
        return hoTen;
    }

    public void setHoTen(String hoTen) {
        this.hoTen = hoTen;
    }

    public float getTiengAnh() {
        return tiengAnh;
    }

    public void setTiengAnh(float tiengAnh) {
        this.tiengAnh = tiengAnh;
    }

    public float getTinHoc() {
        return tinHoc;
    }

    public void setTinHoc(float tinHoc) {
        this.tinHoc = tinHoc;
    }

    public float getGDTC() {
        return GDTC;
    }

    public void setGDTC(float GDTC) {
        this.GDTC = GDTC;
    }

    public double getDiemTB() {
        return DiemTB;
    }

    public void setDiemTB(double DiemTB) {
        this.DiemTB = DiemTB;
    }

}
