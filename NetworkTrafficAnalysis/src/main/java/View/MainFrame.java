/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package View;

import Model.*;
import Controller.*;
import Model.ExtractedPacket;
import java.awt.Color;
import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import org.pcap4j.packet.IpV4Packet;

/**
 *
 * @author gcoll
 */
public class MainFrame extends JFrame implements PacketListenerCallback {

    private FilterPanel filterPanel;
    private JTable packetTable;
    private DefaultTableModel tableModel;
    private JButton startBtn;
    private JButton stopBtn;
    private JButton resetBtn;
    private JPanel btnPanel;
    private EventController controller;
    private JComboBox<String> networkCards;
    private int packetIndex = 1;
    private GridBagConstraints gbc;
    private int packetCount = 0;

    public MainFrame() {
        this.setTitle("Network Traffic Analyser");
        this.controller = new EventController(this);
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
        setLayout(new GridBagLayout());

        filterPanel = new FilterPanel();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        add(filterPanel, gbc);

        // Listen to the FilterPanel start button
        filterPanel.setFilterStartListener(e -> {
            controller.actionPerformed(
                    new java.awt.event.ActionEvent(e.getSource(), java.awt.event.ActionEvent.ACTION_PERFORMED, Command.APPLY.name())
            );
        });

        initTable();
        setupTableCellRenderer();
        initButtons();

    }

    private void setupTableCellRenderer() {
        packetTable.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                    boolean isSelected, boolean hasFocus,
                    int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value,
                        isSelected, hasFocus, row, column);

                // Get the protocol and IP address of the current row
                String protocol = (String) table.getValueAt(row, 3); // column 3 = protocol
                String srcIp = (String) table.getValueAt(row, 1);    // column 1 = source IP
                String dstIp = (String) table.getValueAt(row, 2);    // column 2 = destination IP

                // Determine if either IP is IPv6
                boolean isIPv6 = srcIp.contains(":") || dstIp.contains(":");

                // Check protocol value and set background color accordingly
                //Set different background for v6 packet
                if (isSelected) {
                    c.setBackground(table.getSelectionBackground());
                } else if (isIPv6) {
                    c.setBackground(new Color(200, 255, 200)); // Light green for IPv6
                } else {
                    // Color based on protocol (example)
                    switch (protocol.toUpperCase()) {
                        case "TCP":
                            c.setBackground(new Color(255, 200, 200)); // Light red for TCP
                            break;
                        case "UDP":
                            c.setBackground(new Color(200, 200, 255)); // Light blue for UDP
                            break;
                        default:
                            c.setBackground(table.getBackground()); // Default
                            break;
                    }
                }

                return c;
            }
        });
    }

    public void initTable() {

        String[] columnNames = {"Packet#", "Source IP", "Destination IP", "Protocol", "Length"};

        tableModel = new DefaultTableModel(null, columnNames) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Make table read-only
            }
        };
        packetTable = new JTable(tableModel);

        packetTable.getColumnModel().getColumn(0).setPreferredWidth(5);
        packetTable.getColumnModel().getColumn(1).setPreferredWidth(30); // src ip
        packetTable.getColumnModel().getColumn(2).setPreferredWidth(30); // dst ip
        packetTable.getColumnModel().getColumn(3).setPreferredWidth(10); // protocol
        packetTable.getColumnModel().getColumn(4).setPreferredWidth(10); // length

        packetTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION); // Only select one at a time
        packetTable.getTableHeader().setResizingAllowed(false); // Cant resize the columns
        packetTable.getTableHeader().setReorderingAllowed(false); // no reorder of columns

        gbc.insets = new Insets(2, 6, 2, 6);
        gbc.anchor = GridBagConstraints.NORTHWEST;  // Top-left anchor
        gbc.fill = GridBagConstraints.VERTICAL;         // Fill vertially
        gbc.weightx = 1.0;                          // Take horizontal space
        gbc.weighty = 1.0;
        gbc.gridy = 1;
        JScrollPane scrollPane = new JScrollPane(packetTable);
        add(scrollPane, gbc);
    }

    // Start stop reset buttons
    private void initButtons() {
        gbc.insets = new Insets(8, 12, 8, 12);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        startBtn = new JButton("Start");
        stopBtn = new JButton("Stop");
        resetBtn = new JButton("Reset");
        networkCards = new JComboBox<>();

        btnPanel = new JPanel();

        startBtn.addActionListener(controller);
        startBtn.setActionCommand(Command.START.name());
        stopBtn.addActionListener(controller);
        stopBtn.setActionCommand(Command.STOP.name());
        resetBtn.addActionListener(controller);
        resetBtn.setActionCommand(Command.RESET.name());

        gbc.gridy = 2;
        gbc.gridx = 0;
        gbc.weightx = 1.0;
        gbc.anchor = GridBagConstraints.WEST;
        btnPanel.add(startBtn);
        btnPanel.add(stopBtn);
        btnPanel.add(resetBtn);

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

    public void setNetworkCards() {

        networkCards.addItem("Ethernet");

        String selected = (String) networkCards.getSelectedItem();
    }

    public void resetTable() {
        SwingUtilities.invokeLater(() -> {
            tableModel.setRowCount(0);  // Clear all rows from the table
            packetIndex = 1;            // Reset packet index counter
        });
    }

    public FilterPanel getFilterPanel() {
        return filterPanel;
    }
}
