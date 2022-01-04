/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Lab2;

import java.awt.Color;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;

/**
 *
 * @author HP
 */
public class Demo {

    public static void main(String[] args) {
        JFrame frame = new JFrame("My Frame");
        frame.setLocation(200, 200);
        frame.setSize(550, 250);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
        ImageIcon vnIcon = new ImageIcon("D:\\images\\Vietnam.gif");
        vnIcon = setSizeImage(vnIcon);
        ImageIcon usIcon = new ImageIcon("D:\\images\\my.png");
        usIcon = setSizeImage(usIcon);
        JPanel p = new JPanel();
        JButton b1 = new JButton("Click it", usIcon);
        JButton b2 = new JButton("Click it ", vnIcon);
        b1.setPressedIcon(usIcon);
        b1.setRolloverIcon(usIcon);
        b2.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(null, "bạn chọn Việt Nam");
            }
        });
        p.add(b1);
        p.add(b2);
        p.setBorder(new TitledBorder(new LineBorder(Color.blue), "Demo"));
        frame.add(p);
    }

    public static ImageIcon setSizeImage(ImageIcon vnIcon) {
        Image image = vnIcon.getImage(); // transform it vnIcon
        Image newimg = image.getScaledInstance(85, 65, java.awt.Image.SCALE_SMOOTH); // scale it the smooth way  
        vnIcon = new ImageIcon(newimg);  // transform it back
        return vnIcon;
    }
}
