
import Controller.EventController;
import Model.PacketCapture;
import View.MainFrame;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
/**
 *
 * @author George
 */
public class Main {

    public static void main(String[] args) {
        String nic = "192.168.0.155";
        PacketCapture pc = new PacketCapture(nic);
        String[][] packetArr = pc.packetsTo2dArray();
        EventController controller = new EventController();
        MainFrame f = new MainFrame(packetArr, controller);
        PacketCapture.setPacketListener(f);
        pc.startCapture();

    }
}
