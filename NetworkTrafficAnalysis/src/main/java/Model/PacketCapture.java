/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Model;

import java.io.EOFException;
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
import org.pcap4j.packet.IpV4Packet.IpV4Header;
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
    private static HashMap<Integer, ExtractedPacket> packets;

    public PacketCapture(String nicAddress) {
        packets = new HashMap<>();
        //home 192.168.0.155
        //away 
        // String nicAddress = "192.168.0.155"; // Current ethernet NIC address, in future can replace and get dynamically depending on chosen NIC

        getInetAddress(nicAddress);

        //Open Pcap Handle
        openHandle();

        //Capture packets (10 total)
        capturePackets(10);

        //Close handle after capture
        handle.close();
       
    }

    private static void getInetAddress(String nicAddress) {
        try {
            addr = InetAddress.getByName(nicAddress); // get address to pass to pcap
        } catch (UnknownHostException e) {
            System.out.println(e.getMessage());
        }
    }

    private static void openHandle() {
        try {
            PcapNetworkInterface nif = Pcaps.getDevByAddress(addr); // Find the network interface that you want to capture packets
            int snapLen = 65536;
            PromiscuousMode mode = PromiscuousMode.PROMISCUOUS;
            handle = nif.openLive(snapLen, mode, timeout);
        } catch (PcapNativeException e) {
            System.out.println(e.getMessage());
        }
    }

    private void capturePackets(int packetLimit) {
        packetLimit = 10;
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
                    ExtractedPacket extractedPacket = extractPacket(ipV4Packet); // Format with my own packet class
                    packets.put(i, extractedPacket); // store in packets map
                    displayCapturedPacket(ipV4Packet);
                } else {
                    System.out.println("Captured non-IPv4 packet " );
                }
            }

        }

    }

    private ExtractedPacket extractPacket(IpV4Packet ippac){
        ExtractedPacket exP = new ExtractedPacket();
        IpV4Header header = ippac.getHeader();
        
        exP.setProtocol(header.getProtocol());
        exP.setDstIp(header.getDstAddr());
        exP.setSrcIp(header.getSrcAddr());
        exP.setTotalLength(header.getTotalLengthAsInt());
        
        return exP;
    }
    
    private static void displayCapturedPacket(IpV4Packet packet) {
        System.out.println("Packet Captured!");
        System.out.print("  SRC: " + packet.getHeader().getSrcAddr());
        System.out.print("  DST: " + packet.getHeader().getDstAddr());
        System.out.print("  Protocol: " + packet.getHeader().getProtocol());
        System.out.print("  Length: "+packet.getHeader().getTotalLengthAsInt());
        System.out.println("");

        //System.out.println("Build: " + packet.getBuilder().build());

    }
    
      public String[][] packetsTo2dArray() {
        String[][] packetArr = new String[packets.size()][];
        int i = 0;
        for (HashMap.Entry<Integer, ExtractedPacket> entry : packets.entrySet()) {
            ExtractedPacket currPacket = entry.getValue();
            String index = String.valueOf(i); 
           packetArr[i] = new String[]{
                index,
                currPacket.getSrcIp().toString(),
                currPacket.getDstIp().toString(),
                currPacket.getProtocol().toString(),
                String.valueOf(currPacket.getTotalLength()) + ""
                //(String)currPacket.getHeader().getTotalLengthAsInt()+ ""
            };
            //packetArr[i] = currPacket.toStringArray();  // Convert Packet to String[]
            i++;
        }
        return packetArr;
    }
}
