/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package View;

import Model.*;
import Controller.EventController;
import Model.ExtractedPacket;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;
import org.pcap4j.packet.IpV4Packet;

/**
 *
 * @author gcoll
 */
public class MainFrame extends JFrame implements PacketListenerCallback {

    private JTable packetTable;
    DefaultTableModel tableModel;
    private JButton startBtn;
    private JButton stopBtn;
    private JPanel btnPanel;
    private EventController controller;
    private int packetIndex = 1;

    public MainFrame(String[][] packets, EventController controller) {

        this.setTitle("Network Traffic Analyser");
        this.setSize(800, 600);
        this.setLocationRelativeTo(null);  //Place in middle of monitor
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        initUI(packets);

        this.setVisible(true);
    }

    public static void main(String[] args) {
        String[][] testPackets = {
            {"1", "192.168.0.1", "8.8.8.8"},
            {"2", "192.168.0.2", "1.1.1.1"}
        };
        //new MainFrame(testPackets);
    }

    private void initUI(String[][] packets) {
        initTable(packets);
        initButtons();

    }

    public void initTable(String[][] packets) {
        String[] columnNames = {"Packet#", "Source IP", "Destination IP", "Protocol", "Length"};

        tableModel = new DefaultTableModel(packets, columnNames) {
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

    //Buttons
    private void initButtons() {
        startBtn = new JButton("Start");
        stopBtn = new JButton("Stop");
        btnPanel = new JPanel();

        startBtn.addActionListener(controller);
        stopBtn.addActionListener(controller);

        btnPanel.add(startBtn);
        btnPanel.add(stopBtn);

    }

    @Override
    public void onPacketCaptured(ExtractedPacket packet) {
        javax.swing.SwingUtilities.invokeLater(() -> {
            tableModel.addRow(new Object[]{
                packetIndex++,
                packet.getSrcIp().getHostAddress(),
                packet.getDstIp().getHostAddress(),
                packet.getProtocol().name(),
                packet.getTotalLength()
            });

            packetTable.scrollRectToVisible(packetTable.getCellRect(tableModel.getRowCount() - 1, 0, true));
        });
    }

}
