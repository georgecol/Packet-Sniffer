/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controller;

import Model.ExtractedPacket;
import View.MainFrame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.EventListener;
import Model.PacketListenerCallback;

/**
 *
 * @author George
 */
public class EventController implements ActionListener,PacketListenerCallback{
    
 

    @Override
    public void actionPerformed(ActionEvent e) {
        Command cmd = Command.valueOf(e.getActionCommand());
        
        switch(cmd){
            case START:
                
                break;
            case STOP:
                
                break;
        }
    }

    //When 
    @Override
    public void onPacketCaptured(ExtractedPacket packet) {
        
    }
}
