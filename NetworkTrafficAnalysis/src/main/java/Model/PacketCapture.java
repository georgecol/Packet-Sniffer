/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Model;

import java.io.EOFException;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.concurrent.TimeoutException;
import org.pcap4j.core.NotOpenException;
import org.pcap4j.core.PcapNetworkInterface;
import org.pcap4j.core.Pcaps;
import org.pcap4j.core.PcapHandle;
import org.pcap4j.core.PcapNativeException;
import org.pcap4j.core.PcapNetworkInterface.PromiscuousMode;
import org.pcap4j.packet.IpV4Packet;
import org.pcap4j.packet.Packet;

/**
 *
 * @author George
 */
public class PacketCapture {

    public static InetAddress addr;
    public static PcapHandle handle;
    public static Packet packet;
    public static int timeout;
    public static boolean packetCaptured;

    public static void main(String[] args) throws PcapNativeException, NotOpenException, EOFException {
        String nicAddress = "192.168.0.155"; // Current ethernet NIC address, in future can replace and get dynamically depending on chosen NIC
        try {
            addr = InetAddress.getByName(nicAddress); // get address to pass to pcap

        } catch (UnknownHostException e) {
            System.out.println(e.getMessage());
        }

        PcapNetworkInterface nif = Pcaps.getDevByAddress(addr); // Find the network interface that you want to capture packets

        //Open Pcap Handle
        try {
            int snapLen = 65536;
            PromiscuousMode mode = PromiscuousMode.PROMISCUOUS;
            timeout = 30;
            handle = nif.openLive(snapLen, mode, timeout);
        } catch (PcapNativeException e) {
            System.out.println(e.getMessage());
        }
        //Capture packet
        packetCaptured = true;
        try {
            packet = handle.getNextPacketEx(); //throws PcapNativeException, EOFException, NotOpenException
            handle.close();
        } catch (TimeoutException e) {
            packetCaptured = false;
            System.out.println("No packet captured in: " + timeout+"ms");
            System.out.println(e.getMessage());
        }

        //Print Packet Information
        if (packetCaptured) {
            IpV4Packet ipV4Packet = packet.get(IpV4Packet.class);
            Inet4Address srcAddr = ipV4Packet.getHeader().getSrcAddr();
            displayCapturedPacket(ipV4Packet);
        }

    }

    public static void displayCapturedPacket(IpV4Packet packet) {
        System.out.println("Packet Captured!");
        System.out.print("  SRC: " + packet.getHeader().getSrcAddr());
        System.out.print("  DST: " + packet.getHeader().getDstAddr());
        System.out.print("  Protocol: " + packet.getHeader().getProtocol());
    }
}
