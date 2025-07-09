/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Model;

import java.net.InetAddress;
import org.pcap4j.packet.IpV4Packet;
import org.pcap4j.packet.IpV6Packet;
import org.pcap4j.packet.namednumber.IpNumber;

/**
 *
 * @author George
 */
public class ExtractedPacket {

    private IpNumber protocol;
    private InetAddress srcIp;
    private InetAddress dstIp;
    private int totalLength;
    private IpV4Packet originalIpV4Packet;
    private IpV6Packet originalIpV6Packet;

    public ExtractedPacket() {
        this.protocol = null;
        this.srcIp = null;
        this.dstIp = null;
        this.totalLength = 0;
    }

    public ExtractedPacket(IpNumber protocol, InetAddress srcIp, InetAddress dstIp, int totalLength) {
        this.protocol = protocol;
        this.srcIp = srcIp;
        this.dstIp = dstIp;
        this.totalLength = totalLength;
    }

    public IpNumber getProtocol() {
        return protocol;
    }

    public void setProtocol(IpNumber protocol) {
        this.protocol = protocol;
    }

    public InetAddress getSrcIp() {
        return srcIp;
    }

    public void setSrcIp(InetAddress srcIp) {
        this.srcIp = srcIp;
    }

    public InetAddress getDstIp() {
        return dstIp;
    }

    public void setDstIp(InetAddress dstIp) {
        this.dstIp = dstIp;
    }

    public int getTotalLength() {
        return totalLength;
    }

    public void setTotalLength(int totalLength) {
        this.totalLength = totalLength;
    }

    public IpV4Packet getOriginalIpV4Packet() {
        return originalIpV4Packet;
    }

    public void setOriginalIpV4Packet(IpV4Packet originalIpV4Packet) {
        this.originalIpV4Packet = originalIpV4Packet;
    }

    public IpV6Packet getOriginalIpV6Packet() {
        return originalIpV6Packet;
    }

    public void setOriginalIpV6Packet(IpV6Packet originalIpV6Packet) {
        this.originalIpV6Packet = originalIpV6Packet;
    }
}
