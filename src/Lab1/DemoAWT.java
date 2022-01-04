/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Lab1;

import java.awt.Button;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.Label;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 *
 * @author Lehait
 */
public class DemoAWT extends Frame implements ActionListener {

    Button btnYellow, btnRed;
    Label labe1 = new Label();

    public DemoAWT(String msg) {
        setTitle(msg);
        setLayout(new FlowLayout());
        btnYellow = new Button("Yellow");
        btnRed = new Button("Red");
        add(btnYellow);
        add(btnRed);
        add(labe1);
        btnYellow.addActionListener(this);
        btnRed.addActionListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String str = e.getActionCommand();
        if (str.equals("Yellow")) {
            labe1.setText("Ban nhan button mau vang");
            this.setBackground(Color.yellow);
        }
        if (str.equals("Red")) {
            labe1.setText("Ban nhan button mau do");
            this.setBackground(Color.red);
        }
    }

    public static void main(String[] args) {
        DemoAWT ab = new DemoAWT("Fpoly");
        ab.setSize(450, 200);
        ab.show();
    }
}
