/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Model;

import Controller.EventController;
import java.io.EOFException;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.concurrent.TimeoutException;
import org.pcap4j.core.*;
import org.pcap4j.core.PcapNetworkInterface.PromiscuousMode;
import org.pcap4j.packet.IpV4Packet;
import org.pcap4j.packet.IpV4Packet.IpV4Header;
import org.pcap4j.packet.Packet;

/**
 *
 * @author George
 */
public class PacketCapture {

    private final EventController controller;
    private static Thread captureThread;
    public static InetAddress addr;
    public static PcapHandle handle;
    public static Packet packet;
    public static int timeout = 1000;
    public static boolean packetCaptured;
    private static HashMap<Integer, ExtractedPacket> packets;
    private static PacketListenerCallback listener;

    //private boolean captured;
    public PacketCapture(String nicAddress, EventController controller) {
        this.controller = controller;
        packets = new HashMap<>();
        getInetAddress(nicAddress);

    }

    public void startCapture() {
        System.out.println("Starting capture");

        try {
            PcapNetworkInterface nif = Pcaps.getDevByAddress(addr); // Find the network interface that you want to capture packets
            int snapLen = 65536;
            PromiscuousMode mode = PromiscuousMode.PROMISCUOUS;
            handle = nif.openLive(snapLen, mode, timeout);

            //Live Capture
            captureThread = new Thread(() -> {
                try {
                    handle.loop(-1, new PacketListener() {
                        int packetCount = 0;

                        @Override
                        public void gotPacket(Packet packet) {
                            IpV4Packet ipV4Packet = packet.get(IpV4Packet.class);
                            if (ipV4Packet != null) { // Check if packet captured
                                ExtractedPacket extractedPacket = extractPacket(ipV4Packet); // Extract packet into my own format
                                String selectedProtocol = controller.getProtocolFilter(); // Check for filter each time
                                if (selectedProtocol.equals("All") || extractedPacket.getProtocol().name().equalsIgnoreCase(selectedProtocol)) { // Check whether we are filtering the packet protocol out
                                    packets.put(packetCount, extractedPacket);
                                    packetCount++;
                                    //Notify listeners (eventcontroller)
                                    if (listener != null) {
                                        listener.onPacketCaptured(extractedPacket);
                                    }
                                }
                                // Optional: stop after 100 packets for testing
                                //Hard coded, not changeable
                                if (packetCount >= 100) {
                                    stopCapture();
                                }
                            }
                        }
                    });

                } catch (InterruptedException e) {
                    System.out.println("Capture thread was interrupted. Stopping capture.");
                } catch (PcapNativeException | NotOpenException e) {
                    System.out.println("Unexpected error during capture:");
                    e.printStackTrace();
                }
            });

        } catch (PcapNativeException e) {
            System.out.println(e.getMessage());
        }
        captureThread.start();
    }

    public void stopCapture() {

        if (handle.isOpen()) {
            System.out.println("Stopping Capture");
            try {
                handle.breakLoop();
            } catch (NotOpenException e) {
                System.out.println("Already stopped");
            }
        }
        /*
        if (captureThread != null && captureThread.isAlive()) {
            captureThread.interrupt();
            try {
                captureThread.join(1000); // Waits a second for thread to terminate
                System.out.println("Capture thread stopped");
            } catch (InterruptedException e) {
                System.out.println("Interrupted while waiting for thread to stop");
            }
        }*/
    }

    //Not used
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

                } else {
                    System.out.println("Captured non-IPv4 packet ");
                }
            }

        }

    }

    private static ExtractedPacket extractPacket(IpV4Packet ippac) {
        ExtractedPacket exP = new ExtractedPacket();
        IpV4Header header = ippac.getHeader();

        exP.setOriginalPacket(ippac);
        exP.setProtocol(header.getProtocol());
        exP.setDstIp(header.getDstAddr());
        exP.setSrcIp(header.getSrcAddr());
        exP.setTotalLength(header.getTotalLengthAsInt());
        return exP;
    }

    public String[][] packetsTo2dArray() {
        String[][] packetArr = new String[packets.size()][];
        int i = 1;
        for (HashMap.Entry<Integer, ExtractedPacket> entry : packets.entrySet()) {
            ExtractedPacket currPacket = entry.getValue();
            String index = String.valueOf(i);
            packetArr[i - 1] = new String[]{
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

    public static void setPacketListener(PacketListenerCallback callback) {
        listener = callback;
    }

    private static void getInetAddress(String nicAddress) {
        try {
            addr = InetAddress.getByName(nicAddress); // get address to pass to pcap

        } catch (UnknownHostException e) {
            System.out.println(e.getMessage());
        }
    }

}
