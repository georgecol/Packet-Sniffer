
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
/**
 *
 * @author George
 */
public class Test {

    public static void main(String[] args) {
        listAllNetworkInterfaces();
    }

    public static void listAllNetworkInterfaces() {
        try {

            Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
            while (interfaces.hasMoreElements()) {
                NetworkInterface ni = interfaces.nextElement();

                if (ni.isUp() && !ni.isLoopback()) {
                    Enumeration<InetAddress> addresses = ni.getInetAddresses();
                    boolean hasIP = false;

                    while (addresses.hasMoreElements()) {
                        InetAddress address = addresses.nextElement();

                        if (!hasIP) {
                            System.out.println("Interface: " + ni.getName() + " (" + ni.getDisplayName() + ")");
                            hasIP = true;
                        }
                        System.out.println("  IP: " + address.getHostAddress());

                    }
                    if (hasIP) {
                        System.out.println();
                    }

                }
            }
        } catch (SocketException e) {
            e.printStackTrace();
        }

    }

}
