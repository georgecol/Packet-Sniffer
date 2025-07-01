/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controller;

import Model.PacketCapture;

/**
 *
 * @author George
 */
public class CaptureStart {
    
    public static void main(String[] args) {
        String nic = "192.168.0.155";
        PacketCapture pc = new PacketCapture(nic);
    }
}
