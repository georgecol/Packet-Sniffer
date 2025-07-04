/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controller;

import Model.PacketCapture;
import View.MainFrame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


/**
 *
 * @author George
 */
public class EventController implements ActionListener {

    private PacketCapture pc;
    private MainFrame mainFrame;
    private String protocolFilter = "All";
    private String selectedProtocol;

    public EventController(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Command cmd = Command.valueOf(e.getActionCommand());

        switch (cmd) {
            case START:
                selectedProtocol = mainFrame.getFilterPanel().getSelectedProtocol(); // Get protocol
                setProtocolFilter(selectedProtocol); // Set it in controller
                System.out.println("Selected Protocol: " + selectedProtocol); // Debug print

                String nic = "192.168.0.155";
                pc = new PacketCapture(nic, this);
                PacketCapture.setPacketListener(mainFrame);
                pc.startCapture();
                break;
            case STOP:
                if (pc != null) {
                    pc.stopCapture();
                }
                break;
            case RESET:
                if (mainFrame != null) {
                    mainFrame.resetTable();
                }
                //Clear table? or Stop and start
                break;
            case APPLY:
                selectedProtocol = mainFrame.getFilterPanel().getSelectedProtocol(); // Get protocol
                setProtocolFilter(selectedProtocol); // Set it in controller
                System.out.println("Selected Protocol: " + selectedProtocol); // Debug print
                
                
                break;
        }
    }

    public void setProtocolFilter(String protocol) {
        this.protocolFilter = protocol;
    }

    public String getProtocolFilter() {
        return this.protocolFilter;
    }

    public void setMainFrame(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
    }
}
