/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package View;

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
       
    public MainFrame(String[][] packets){
        
        this.setTitle("Hotel Booking System");
        this.setSize(800, 600);
        this.setLocationRelativeTo(null);  //Place in middle of monitro   
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        //this.pack();        //Pack paenl snug
        this.setVisible(true);
        initTable(packets);
    }
    

    
    public static void main(String[] args) {
        MainFrame f = new MainFrame();
    }
    
    public void initTable(String[][] packets){
         String[] columnNames = {"Packet#", "Source IP", "Destination IP"};
         
         
        
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
