/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package View;

import Controller.Command;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

/**
 *
 * @author George
 */
public class FilterPanel extends JPanel {

    private JLabel filterLabel;
    private JComboBox<String> protocolBox;
    private JButton filterStartBtn;

    public FilterPanel() {
        initUI();
    }

    private void initUI() {
        setLayout(new FlowLayout(FlowLayout.LEFT));

        filterLabel = new JLabel("Filter by Protocol:");
        String[] protocols = {"All", "TCP", "UDP", "ICMP"}; // Add more if needed
        protocolBox = new JComboBox<>(protocols);
        filterStartBtn = new JButton("Apply Filter");
        filterStartBtn.setActionCommand(Command.RESET.name());
        
        add(filterLabel);
        add(protocolBox);
        add(filterStartBtn);
    }

    public String getSelectedProtocol() {
        return (String) protocolBox.getSelectedItem();
    }

    public void setFilterStartListener(ActionListener listener) {
        filterStartBtn.addActionListener(listener);
    }
}