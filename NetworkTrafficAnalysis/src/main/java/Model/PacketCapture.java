/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Model;

import java.io.EOFException;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.HashMap;
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
    public static int timeout = 1000;
    public static boolean packetCaptured;
    public static HashMap<Integer, Packet> packets;

    public static void main(String[] args) {

        packets = new HashMap<>();

        String nicAddress = "192.168.0.155"; // Current ethernet NIC address, in future can replace and get dynamically depending on chosen NIC
        try {
            addr = InetAddress.getByName(nicAddress); // get address to pass to pcap

        } catch (UnknownHostException e) {
            System.out.println(e.getMessage());
        }

        //Open Pcap Handle
        try {
            PcapNetworkInterface nif = Pcaps.getDevByAddress(addr); // Find the network interface that you want to capture packets
            int snapLen = 65536;
            PromiscuousMode mode = PromiscuousMode.PROMISCUOUS;
            handle = nif.openLive(snapLen, mode, timeout);
        } catch (PcapNativeException e) {
            System.out.println(e.getMessage());
        }
        //Capture packet
        int packetLimit = 10;
        for (int i = 1; i <= packetLimit; i++) {
            System.out.println("Packet #" + i);
            packetCaptured = true;
            try {
                packet = handle.getNextPacketEx();

            } catch (EOFException | TimeoutException | NotOpenException | PcapNativeException e) {
                packetCaptured = false;
                System.out.println("No packet captured in: " + timeout + "ms");
                System.out.println(e.getMessage());
            }

            //Print Packet Information for each packet
            if (packetCaptured) {
                IpV4Packet ipV4Packet = packet.get(IpV4Packet.class);

                if (ipV4Packet != null) { // Checks if packet is Ip , if packetcaptured and not ip , dont attempt to display , because build is only for ipv4, will give error
                    packets.put(i, packet); // store in packets map
                    displayCapturedPacket(ipV4Packet);
                } else {
                    System.out.println("Captured non-IPv4 packet: " + packet);
                }
            }

        }
        handle.close();
    }

    public static void displayCapturedPacket(IpV4Packet packet) {
//        System.out.println("Packet Captured!");
//        System.out.print("  SRC: " + packet.getHeader().getSrcAddr());
//        System.out.print("  DST: " + packet.getHeader().getDstAddr());
//        System.out.print("  Protocol: " + packet.getHeader().getProtocol());

        System.out.println("Build: " + packet.getBuilder().build());

    }
}
