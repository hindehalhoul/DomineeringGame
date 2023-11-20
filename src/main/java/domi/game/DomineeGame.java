/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package domi.game;
import java.io.*;
import javax.swing.*;
import java.awt.*;

/**
 *
 * @author ASUS
 */
public class DomineeGame {
     public static void main(String[] args) throws IOException {
         JFrame frame = new JFrame();
        frame.setBounds(10, 10, 512, 512);
        frame.setUndecorated(true);
        JPanel pn=new JPanel(){
            @Override
            public void paint(Graphics g) {
            boolean white=true;
                for(int y= 0;y<8;y++){
            for(int x= 0;x<8;x++){
                if(white){
                    g.setColor(new Color(200, 200, 200));
                }else{
                    g.setColor(new Color(100, 100, 100));
                    
                }
                g.fillRect(x*64, y*64, 64, 64);
           white=!white;
            }
            white=!white;
            }}};
                
        frame.add(pn);
        frame.setDefaultCloseOperation(3);
        frame.setVisible(true);
     }
    
}
