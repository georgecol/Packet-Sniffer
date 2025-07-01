/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package View;

import Model.ExtractedPacket;
import Model.PacketCapture;
import java.awt.BorderLayout;
import java.util.HashMap;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;
import org.pcap4j.packet.Packet;

/**
 *
 * @author gcoll
 */
public class MainFrame extends JFrame {

    JTable packetTable;

    public MainFrame(String[][] packets) {

        this.setTitle("Network Traffic Analyser");
        this.setSize(800, 600);
        this.setLocationRelativeTo(null);  //Place in middle of monitor
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
    
        initTable(packets);
        this.setVisible(true);
    }

    public static void main(String[] args) {
        String[][] testPackets = {
            {"1", "192.168.0.1", "8.8.8.8"},
            {"2", "192.168.0.2", "1.1.1.1"}
        };
        new MainFrame(testPackets);
    }

    public void initTable(String[][] packets) {
        String[] columnNames = {"Packet#", "Source IP", "Destination IP","Protocol","Length"};

        DefaultTableModel tableModel = new DefaultTableModel(packets, columnNames) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Make table read-only
            }
        };
        packetTable = new JTable(tableModel);

        packetTable.getColumnModel().getColumn(0).setPreferredWidth(5);
        packetTable.getColumnModel().getColumn(1).setPreferredWidth(100);
        packetTable.getColumnModel().getColumn(2).setPreferredWidth(100);
        packetTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION); // Only select one at a time

        JScrollPane scrollPane = new JScrollPane(packetTable);
        this.add(scrollPane);
    }

}
