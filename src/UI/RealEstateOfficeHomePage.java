package UI;

import DBHelper.SQLHelper;
import Models.Agent;
import Models.AgentAddress;
import Models.Property;
import Models.PropertyAddress;
import Utility.SpringUtilities;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class RealEstateOfficeHomePage {
    private JFrame frame;
    private Dimension dim;
    private DefaultTableModel model, soldModel, rentModel;
    private JTable table, soldTable, rentTable;
    private JLabel soldLabel, rentLabel, averageSellingPrice, averageRentAmount;
    private JLabel heading, property_id, no_of_bathrooms, no_of_bedrooms, no_of_balconies, carpet_area, elevator, storey, type_of_property, size_of_kitchen, terrace_access, address, country, state, city, pincode, locality, landmark, dateAdded;
    private JLabel agentHeading, agentID, agentFirstName, agentLastName, agentAddress, agentCountry, agentState, agentCity, agentPincode, agentLocality, agentLandmark, agentPropertiesAddedCount;

    private int aid, pid;

    public RealEstateOfficeHomePage() {
        dim = Toolkit.getDefaultToolkit().getScreenSize();
        frame = new MyFrame("Real Estate Office Home Page", dim.width, dim.height);
        model = new DefaultTableModel();

        SQLHelper helper = SQLHelper.getInstance();
        helper.populateAllAgents(model);
    }

    private void createUserTable() {
        JPanel panel = new JPanel(new SpringLayout());

        table = new JTable(model);
        table.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent mouseEvent) {

            }

            @Override
            public void mousePressed(MouseEvent mouseEvent) {
                String s = table.getValueAt(table.getSelectionModel().getLeadSelectionIndex(), 0).toString();
                aid = Integer.parseInt(s.trim());
                Thread t1 = new Thread(() -> {
                    SQLHelper.getInstance().populateAllSoldPropertiesWithID(aid, soldModel);
                    soldLabel.setText("Properties Sold (" + soldModel.getRowCount() + ")");
                    soldTable.setEnabled(true);
                });

                Thread t2 = new Thread(() -> {
                    SQLHelper.getInstance().populateAllRentedPropertiesWithID(aid, rentModel);
                    rentLabel.setText("Properties Rented (" + soldModel.getRowCount() + ")");
                    rentTable.setEnabled(true);
                });

                Thread t3 = new Thread(() -> {
                    double sp = SQLHelper.getInstance().getSoldPropertyAvgSellingPrice(aid);
                    averageSellingPrice.setText("<html>Average Selling Price : <b>Rs." + sp + "</b></html>");
                });

                Thread t4 = new Thread(() -> {
                    double ra = SQLHelper.getInstance().getRentedPropertyAvgRentAmount(aid);
                    averageRentAmount.setText("<html>Average Rent Amount : <b>Rs." + ra + "</b></html>");
                });

                Thread t5 = new Thread(() -> {
                    heading.setText("Property Details");
                    property_id.setText("");
                    no_of_bathrooms.setText("");
                    no_of_bedrooms.setText("");
                    no_of_balconies.setText("");
                    carpet_area.setText("");
                    elevator.setText("");
                    storey.setText("");
                    type_of_property.setText("");
                    size_of_kitchen.setText("");
                    terrace_access.setText("");
                    address.setText("");

                    country.setText("");
                    state.setText("");
                    city.setText("");
                    pincode.setText("");
                    locality.setText("");
                    landmark.setText("");

                    dateAdded.setText("");
                });

                Thread t6 = new Thread(() -> {
                    Agent agent = SQLHelper.getInstance().agentFromID(aid);
                    agentHeading.setText("<html><b><u>Agent Details</u></b></html>");
                    agentID.setText("Agent ID : " + agent.getAgent_id());
                    agentFirstName.setText("Agent First Name : " + agent.getFirstName());
                    agentLastName.setText("Agent Last Name : " + agent.getLastName());
                    agentAddress.setText("<html><br><b><u>Agent Address</u></b></br></html>");

                    AgentAddress address = agent.getAddress();
                    agentCountry.setText("Country : " + address.getCountry());
                    agentState.setText("State : " + address.getState());
                    agentCity.setText("City : " + address.getCity());
                    agentPincode.setText("Pincode : " + address.getPincode());
                    agentLocality.setText("Locality : " + address.getLocality());
                    agentLandmark.setText("Landmark : " + address.getLandmark());

                    agentPropertiesAddedCount.setText("Number of properties added : " + SQLHelper.getInstance().getPropertiesAddedCount(aid));
                });

                t6.start();
                t1.start();
                t2.start();
                t3.start();
                t4.start();
                t5.start();

                try {
                    t5.join();
                    t1.join();
                    t3.join();
                    t2.join();
                    t4.join();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
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
        panel.add(scrollPane);
        SpringUtilities.makeCompactGrid(panel, 1, 1,6,6,6,6);

        agentHeading = new JLabel("", SwingConstants.CENTER);
        agentID = new JLabel();
        agentFirstName = new JLabel();
        agentLastName = new JLabel();
        agentAddress = new JLabel();
        agentAddress.setHorizontalAlignment(SwingConstants.CENTER);
        agentCountry = new JLabel();
        agentState = new JLabel();
        agentCity = new JLabel();
        agentPincode = new JLabel();
        agentLocality = new JLabel();
        agentLandmark = new JLabel();
        agentPropertiesAddedCount = new JLabel();

        JLabel[] labels = {agentHeading, agentID, agentFirstName, agentLastName, agentAddress, agentCountry, agentState, agentCity, agentPincode, agentLocality, agentLandmark, agentPropertiesAddedCount};

        JPanel subPanel = new JPanel(new SpringLayout());
        subPanel.setPreferredSize(new Dimension((int) (dim.width * 0.3), (int) (dim.height * 0.95 * 0.5)));
        for(JLabel label : labels) subPanel.add(label);
        SpringUtilities.makeCompactGrid(subPanel, labels.length, 1, 6,6,6,6);
        panel.add(subPanel);

        SpringUtilities.makeCompactGrid(panel, 2, 1, 6,6,6,6);

        panel.setPreferredSize(new Dimension((int) (dim.width * 0.3), (int) (dim.height * 0.95)));
        frame.getContentPane().add(panel, BorderLayout.WEST);
    }

    private void setPropertyDetails(Property property) {
        heading.setText("<html><br><b><u>Property Details</u></b></br></html>");
        property_id.setText("<html>Property ID : <i><b>" + property.getProperty_id() + "</b></i></html>");
        no_of_bathrooms.setText("<html>Number of Bathrooms : <i><b>" + property.getNo_of_bathrooms() + "</b></i></html>");
        no_of_bedrooms.setText("<html>Number of Bedrooms : <i><b>" + property.getNo_of_bedrooms() + "</b></i></html>");
        no_of_balconies.setText("<html>Number of Balconies : <i><b>" + property.getNo_of_balconies() + "</b></i></html>");
        carpet_area.setText("<html>Carpet Area : <i><b>" + property.getCarpet_area() + " square ft." + "</b></i></html>");
        elevator.setText("<html>Elevator present : <i><b>" + (property.isElevator() ? "Present" : "Not Present") + "</b></i></html>");
        storey.setText("<html>Number of storey : <i><b>" + property.getStorey() + "</b></i></html>");
        type_of_property.setText("<html>Type of Property : <i><b>" + property.getType_of_property() + "</b></i></html>");
        size_of_kitchen.setText("<html>Size of Kitchen : <i><b>" + property.getSize_of_kitchen() + " square ft." + "</b></i></html>");
        terrace_access.setText("<html>Access to Terrace : <i><b>" + (property.isTerrace_access() ? "Yes" : "No") + "</b></i></html>");
        address.setText("<html><br><b><u>Address Details</u></b></br></html>");

        PropertyAddress address = property.getAddress();
        country.setText("<html>Country : <i><b>" + address.getCountry() + "</b></i></html>");
        state.setText("<html>State : <i><b>" + address.getState() + "</b></i></html>");
        city.setText("<html>City : <i><b>" +  address.getCity() + "</b></i></html>");
        pincode.setText("<html>Pincode : <i><b>" + address.getPincode() + "</b></i></html>");
        locality.setText("<html>Locality : <i><b>" + address.getLocality() + "</b></i></html>");
        landmark.setText("<html>Landmark : <i><b>" + address.getLandmark() + "</b></i></html>");

        dateAdded.setText("<html>Date Added To Market : <i><b>" + property.getDateAdded() + "</b></i></html>");
    }

    private void createCenterPanel() {
        JPanel panel = new JPanel(new SpringLayout());

        soldLabel = new JLabel("Properties Sold");
        panel.add(soldLabel);

        soldModel = new DefaultTableModel();
        soldTable = new JTable(soldModel);
        soldTable.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent mouseEvent) {

            }

            @Override
            public void mousePressed(MouseEvent mouseEvent) {
                Thread t = new Thread(() -> {
                    String s = soldTable.getValueAt(soldTable.getSelectionModel().getLeadSelectionIndex(), 0).toString();
                    pid = Integer.parseInt(s.trim());
                    System.out.println("Agent ID : " + aid + ", Property ID : " + pid);
                    Property property = SQLHelper.getInstance().getProperty(aid, pid);
                    if(property != null) setPropertyDetails(property);
                });
                t.start();
                try {
                    t.join();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
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
        soldTable.setEnabled(false);
        JScrollPane soldPane = new JScrollPane(soldTable);
        panel.add(soldPane);

        averageSellingPrice = new JLabel();
        averageSellingPrice.setHorizontalAlignment(SwingConstants.RIGHT);
        panel.add(averageSellingPrice);

        rentLabel = new JLabel("Properties Rented");
        panel.add(rentLabel);

        rentModel = new DefaultTableModel();
        rentTable = new JTable(rentModel);
        rentTable.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent mouseEvent) {

            }

            @Override
            public void mousePressed(MouseEvent mouseEvent) {
                Thread t = new Thread(() -> {
                    String s = rentTable.getValueAt(rentTable.getSelectionModel().getLeadSelectionIndex(), 0).toString();
                    pid = Integer.parseInt(s.trim());
                    Property property = SQLHelper.getInstance().getProperty(aid, pid);
                    if(property != null) setPropertyDetails(property);
                });
                t.start();
                try {
                    t.join();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
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
        rentTable.setEnabled(false);
        JScrollPane rentPane = new JScrollPane(rentTable);
        panel.add(rentPane);

        averageRentAmount = new JLabel();
        averageRentAmount.setHorizontalAlignment(SwingConstants.RIGHT);
        panel.add(averageRentAmount);

        panel.setPreferredSize(new Dimension((int) (dim.width * 0.4), (int) (dim.height * 0.95)));
        SpringUtilities.makeCompactGrid(panel, 6, 1, 6, 6, 6, 6);
        frame.add(panel, BorderLayout.CENTER);
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
        frame.getContentPane().add(logoutButton, BorderLayout.SOUTH);
    }

    private void createPropertyInfoPanel() {
        JPanel panel = new JPanel(new SpringLayout());

        heading = new JLabel("", SwingConstants.CENTER);
        property_id = new JLabel();
        no_of_bathrooms = new JLabel();
        no_of_bedrooms = new JLabel();
        no_of_balconies = new JLabel();
        carpet_area = new JLabel();
        elevator = new JLabel();
        storey = new JLabel();
        type_of_property = new JLabel();
        size_of_kitchen = new JLabel();
        terrace_access = new JLabel();
        address = new JLabel();
        address.setHorizontalAlignment(SwingConstants.CENTER);
        country = new JLabel();
        state = new JLabel();
        city = new JLabel();
        pincode = new JLabel();
        locality = new JLabel();
        landmark = new JLabel();
        dateAdded = new JLabel();

        JLabel[] labels = { heading, property_id, no_of_bathrooms, no_of_bedrooms, no_of_balconies, carpet_area, elevator, storey, type_of_property, size_of_kitchen, terrace_access, address, country, state, city, pincode, locality, landmark, dateAdded};
        for(JLabel label : labels) panel.add(label);

        SpringUtilities.makeCompactGrid(panel, labels.length, 1, 6,6,6,6);
        panel.setPreferredSize(new Dimension((int) (dim.width * 0.3), (int) (dim.height * 0.95)));
        frame.getContentPane().add(panel, BorderLayout.EAST);
    }

    public void show() {
        createUserTable();
        createPropertyInfoPanel();
        createCenterPanel();
        createLogoutButton();
        frame.setVisible(true);
        JOptionPane.showMessageDialog(null, "Welcome, Admin", "Greetings", JOptionPane.INFORMATION_MESSAGE, null);
    }
}

