/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package devj130_lab4;

import java.awt.Container;
import java.awt.GridLayout;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JTextField;
import static javax.swing.WindowConstants.DISPOSE_ON_CLOSE;

/**
 *
 * @author bezdetk0@mail.ru
 */
public class Main {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {


        
        new SimpleChat(LaunchEnum.CLIENT);
                new SimpleChat(LaunchEnum.SERVER);

//    SimpleChat chat1 = new SimpleChat();
//    SimpleChat chat2 = new SimpleChat();
//    
//    new Thread(() -> {
//        try {
//            chat1.server();
//        } catch (ChatException ex) {
//            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
//        }
//    }).start();
//    
//    new Thread(() -> {
//        try {
//            chat2.client();
//        } catch (ChatException ex) {
//            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
//        }
//    }).start();

    }

    
}
