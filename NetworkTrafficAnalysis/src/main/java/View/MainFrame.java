/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package View;

import Model.*;
import Controller.*;
import Model.ExtractedPacket;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import javax.swing.JButton;
import javax.swing.JComboBox;
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
    private JComboBox<String> networkCards;
    private int packetIndex = 1;
    private GridBagConstraints gbc;
    private int packetCount = 0;

    public MainFrame(EventController controller) {
        this.setTitle("Network Traffic Analyser");
        this.controller = controller;
        this.setSize(800, 600);
        this.setLocationRelativeTo(null);  //Place in middle of monitor
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        gbc = new GridBagConstraints();
        initUI();

        this.setVisible(true);
    }

    public static void main(String[] args) {
        String[][] testPackets = {
            {"1", "192.168.0.1", "8.8.8.8"},
            {"2", "192.168.0.2", "1.1.1.1"}
        };
        //new MainFrame(testPackets);
    }

    private void initUI() {
        initTable();
        initButtons();

    }

    public void initTable() {
        setLayout(new GridBagLayout());
        String[] columnNames = {"Packet#", "Source IP", "Destination IP", "Protocol", "Length"};

        tableModel = new DefaultTableModel(null, columnNames) {
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
        gbc.insets = new Insets(2, 6, 2, 6);
        gbc.anchor = GridBagConstraints.NORTHWEST;  // Top-left anchor
        gbc.fill = GridBagConstraints.BOTH;         // Fill both directions
        gbc.weightx = 1.0;                          // Take horizontal space
        gbc.weighty = 1.0;
        gbc.gridy = 1;
        JScrollPane scrollPane = new JScrollPane(packetTable);
        add(scrollPane, gbc);
    }

    //Buttons
    private void initButtons() {
        gbc.insets = new Insets(8, 12, 8, 12);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        startBtn = new JButton("Start");
        stopBtn = new JButton("Stop");
        networkCards = new JComboBox<>();
        
        btnPanel = new JPanel();

        startBtn.addActionListener(controller);
        startBtn.setActionCommand(Command.START.name());
        stopBtn.addActionListener(controller);
        stopBtn.setActionCommand(Command.STOP.name()); 
        gbc.gridy = 2;
        btnPanel.add(startBtn);
        btnPanel.add(stopBtn);

        add(btnPanel, gbc);
    }

    @Override
    public void onPacketCaptured(ExtractedPacket packet) {
        javax.swing.SwingUtilities.invokeLater(() -> {
            tableModel.insertRow(0, new Object[]{
                packetIndex++,
                packet.getSrcIp().getHostAddress(),
                packet.getDstIp().getHostAddress(),
                packet.getProtocol().name(),
                packet.getTotalLength()
            });
            packetTable.scrollRectToVisible(packetTable.getCellRect(0, 0, true)); // keep scroll bar at top of screen - 
        });
    }
    
    public void setNetworkCards(){
        
        
        
        networkCards.addItem("Ethernet");
        
        String selected = (String) networkCards.getSelectedItem();
    }
    
}
