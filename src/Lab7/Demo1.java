/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Lab7;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 *
 * @author HP
 */
public class Demo1 {

    public static void main(String[] args) {
        try {
            InetAddress myHost = InetAddress.getLocalHost();
            System.out.println("Host address: " + myHost.getHostAddress());
            System.out.println("Host name: " + myHost.getHostName());
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
    }

}
