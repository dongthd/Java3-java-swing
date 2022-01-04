/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Lab6Part2;

/**
 *
 * @author HP
 */
public class Standard {

    private int idStand;
    private String standardName;
    private double fee;

    public Standard() {
    }

    public Standard(int idStand, String standardName, double fee) {
        this.idStand = idStand;
        this.standardName = standardName;
        this.fee = fee;
    }

    public int getIdStand() {
        return idStand;
    }

    public void setIdStand(int idStand) {
        this.idStand = idStand;
    }

    public String getStandardName() {
        return standardName;
    }

    public void setStandardName(String standardName) {
        this.standardName = standardName;
    }

    public double getFee() {
        return fee;
    }

    public void setFee(double fee) {
        this.fee = fee;
    }

}
