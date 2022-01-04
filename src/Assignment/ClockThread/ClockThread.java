/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Assignment.ClockThread;

import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.JLabel;

/**
 *
 * @author HP
 */
public class ClockThread extends Thread {

    private JLabel tbl;

    public ClockThread(JLabel tbl) {
        this.tbl = tbl;
    }

    public void run() {
        SimpleDateFormat sdf = new SimpleDateFormat("hh:mm:ss aa");
        while (true) {
            try {
                Date now = new Date();
                String st = sdf.format(now);
                tbl.setText(st);
                Thread.sleep(1000);
            } catch (InterruptedException ex) {
            }
        }
    }

}
