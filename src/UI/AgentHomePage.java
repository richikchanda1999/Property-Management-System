package UI;

import DBHelper.SQLHelper;
import Models.Agent;
import Models.Property;
import Utility.SpringUtilities;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;

public class AgentHomePage {
    private JFrame frame;
    private DefaultTableModel model;
    private JButton addPropertyButton, sellPropertyButton, rentPropertyButton;
    private Agent agent;
    private JTable table;
    private JLabel statusText;

    public AgentHomePage(Agent agent) {
        this.agent = agent;
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        frame = new MyFrame("Agent Home Page", dim.width, dim.height);
        model = new DefaultTableModel();

        SQLHelper helper = SQLHelper.getInstance();
        helper.populateModelForAgent(agent.getAgent_id(), model);
    }

    private void createTable() {
        table = new JTable(model);
        table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        table.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent mouseEvent) {

            }

            @Override
            public void mousePressed(MouseEvent mouseEvent) {
                String s = table.getValueAt(table.getSelectionModel().getLeadSelectionIndex(), 0).toString();
                int id;
                try {
                    id = Integer.parseInt(s.trim());
                } catch(NumberFormatException ex) {
                    id = -1;
                }
                int status = SQLHelper.getInstance().getPropertyStatus(id, agent.getAgent_id());
                String message = "";
                switch(status) {
                    case Property.SOLD:
                        rentPropertyButton.setEnabled(false);
                        sellPropertyButton.setEnabled(false);
                        message = "Sold";
                        break;

                    case Property.RENTED:
                        rentPropertyButton.setEnabled(false);
                        sellPropertyButton.setEnabled(false);
                        message = "Rented";
                        break;

                    case Property.IN_MARKET:
                        rentPropertyButton.setEnabled(true);
                        sellPropertyButton.setEnabled(true);
                        message = "Available";
                        break;

                    case Property.ERROR:
                        rentPropertyButton.setEnabled(false);
                        sellPropertyButton.setEnabled(false);
                        message = "Error!";
                        break;
                }
                statusText.setText("Status : " + message);
            }

            @Override
            public void mouseReleased(MouseEvent mouseEvent) {

            }

            @Override
            public void mouseEntered(MouseEvent mouseEvent) {

            }

            @Override
            public void mouseExited(MouseEvent mouseEvent) {

            }
        });

        JScrollPane scrollPane = new JScrollPane(table, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        frame.add(scrollPane, BorderLayout.CENTER);
    }

    private void createSidePanel() {
        JPanel panel = new JPanel(new SpringLayout());

        addPropertyButton = new JButton("Add Property");
        addPropertyButton.addActionListener(new AddPropertyAction(agent, model));
        panel.add(addPropertyButton);

        sellPropertyButton = new JButton("Sell Property");
        sellPropertyButton.setEnabled(false);
        sellPropertyButton.addActionListener(new SellPropertyAction(model, table, agent));
        panel.add(sellPropertyButton);

        rentPropertyButton = new JButton("Rent Property");
        rentPropertyButton.setEnabled(false);
        rentPropertyButton.addActionListener(new RentPropertyAction(model, table, agent));
        panel.add(rentPropertyButton);

        statusText = new JLabel();
        panel.add(statusText);

        SpringUtilities.makeCompactGrid(panel, 4, 1, 6, 6, 6, 6);
        frame.add(panel, BorderLayout.WEST);
    }

    private void createLogoutButton() {
        JButton logoutButton = new JButton("Logout");
        logoutButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                frame.dispose();
                Login login = new Login();
                login.show();
                login.toFront();
            }
        });
        frame.add(logoutButton, BorderLayout.SOUTH);
    }

    private void createPersonalInfoPanel() {

    }

    public void show() {
        createTable();
        createSidePanel();
        createLogoutButton();
        createPersonalInfoPanel();
        frame.setVisible(true);
        JOptionPane.showMessageDialog(null, "Welcome, " + agent.getFirstName(), "Greetings", JOptionPane.INFORMATION_MESSAGE, null);
    }

    static class AddPropertyAction implements ActionListener {
        private Agent agent;
        private DefaultTableModel model;
        public AddPropertyAction(Agent agent, DefaultTableModel model) {
            this.model = model;
            this.agent = agent;
        }
        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            AddProperty addProperty = new AddProperty(agent, model);
            addProperty.show();
            addProperty.toFront();
        }
    }

    static class SellPropertyAction implements ActionListener {
        DefaultTableModel model;
        private Agent agent;
        private JTable table;

        public SellPropertyAction(DefaultTableModel model, JTable table, Agent agent) {
            this.model = model;
            this.table = table;
            this.agent = agent;
        }

        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            String s = table.getValueAt(table.getSelectionModel().getLeadSelectionIndex(), 0).toString();
            int id;
            try {
                id = Integer.parseInt(s.trim());
            } catch(NumberFormatException ex) {
                id = -1;
            }
            if(id != -1) {
                SellProperty sp = new SellProperty(id, agent);
                sp.show();
                sp.toFront();
            }
        }
    }

    static class RentPropertyAction implements ActionListener {
        DefaultTableModel model;
        private Agent agent;
        private JTable table;

        public RentPropertyAction(DefaultTableModel model, JTable table, Agent agent) {
            this.model = model;
            this.table = table;
            this.agent = agent;
        }

        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            String s = table.getValueAt(table.getSelectionModel().getLeadSelectionIndex(), 0).toString();
            int id;
            try {
                id = Integer.parseInt(s.trim());
            } catch(NumberFormatException ex) {
                id = -1;
            }
            if(id != -1) {
                RentProperty rp = new RentProperty(id, agent);
                rp.show();
                rp.toFront();
            }
        }
    }
}

