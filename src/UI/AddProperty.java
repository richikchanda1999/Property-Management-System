package UI;

import DBHelper.SQLHelper;
import Models.Agent;
import Models.Property;
import Models.PropertyAddress;
import Utility.SpringUtilities;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Date;

public class AddProperty {
    private JFrame frame;
    private boolean created;
    private Agent agent;
    private DefaultTableModel model;

    public AddProperty(Agent agent, DefaultTableModel model) {
        this.agent = agent;
        this.model = model;
        frame = new MyFrame("Add Property", 300, 614);
        frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        created = false;
    }

    private void createPropertyDetailForm() {
        JPanel panel = new JPanel(new SpringLayout());

        String[] labelHeads = {"No. of bathrooms", "No. of bedrooms", "No. of balconies", "Carpet Area", "Elevator", "Storey",
                "Type of Property", "Size of kitchen", "Terrace Access", "Country",
                "State", "City", "Pincode", "Locality", "Landmark"};
        JLabel[] labels = new JLabel[labelHeads.length];
        for (int i = 0; i < labelHeads.length; ++i) {
            String label = labelHeads[i];
            labels[i] = new JLabel(label);

            panel.add(labels[i]);
            switch(label) {
                case "No. of bathrooms":
                case "No. of bedrooms":
                case "No. of balconies":
                case "Storey":
                    SpinnerModel model = new SpinnerNumberModel(0, 0, 10, 1);
                    JSpinner spinner = new JSpinner(model);
                    labels[i].setLabelFor(spinner);
                    panel.add(spinner);
                    break;

                case "Type of Property":
                    String[] types = {"House", "Flat"};
                    JComboBox<String> comboBox = new JComboBox<>(types);
                    labels[i].setLabelFor(comboBox);
                    panel.add(comboBox);
                    break;

                case "Elevator":
                case "Terrace Access":
                    JRadioButton yes = new JRadioButton("Yes");
                    JRadioButton no = new JRadioButton("No");
                    ButtonGroup group = new ButtonGroup();
                    group.add(yes);
                    group.add(no);
                    JPanel p = new JPanel(new SpringLayout());
                    p.add(yes);
                    p.add(no);
                    SpringUtilities.makeCompactGrid(p, 1, 2, 5,5,5,5);
                    labels[i].setLabelFor(yes);
                    panel.add(p);
                    break;

                default:
                    JTextField field = new JTextField();
                    panel.add(field);
                    labels[i].setLabelFor(field);
                    break;
            }
        }

        JLabel dummyLabel = new JLabel("");
        JButton registerButton = new JButton("Add Property");
        registerButton.addActionListener(new AddPropertyAction(labels, frame, agent, model));
        dummyLabel.setLabelFor(registerButton);
        panel.add(dummyLabel);
        panel.add(registerButton);

        SpringUtilities.makeCompactGrid(panel, labelHeads.length + 1, 2, 6, 6, 6, 6);
        frame.add(panel, BorderLayout.CENTER);
    }

    private void createCancelOption() {
        JPanel panel = new JPanel(new SpringLayout());

        panel.add(new JSeparator());
        JButton cancelButton = new JButton("Cancel");
        cancelButton.addActionListener(new CancelAction(frame));
        panel.add(cancelButton);

        SpringUtilities.makeCompactGrid(panel,
                2, 1, //rows, cols
                6, 6,        //initX, initY
                6, 6);       //xPad, yPad

        frame.add(panel, BorderLayout.SOUTH);
    }

    public void show() {
        if (!created) {
            createPropertyDetailForm();
            createCancelOption();
            created = true;
        }
        frame.setVisible(true);
    }

    public void hide() {
        frame.setVisible(false);
    }

    public void toFront() {
        frame.toFront();
    }

    static class CancelAction implements ActionListener {
        private JFrame frame;

        public CancelAction(JFrame frame) {
            this.frame = frame;
        }

        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            frame.dispose();
        }
    }

    static class AddPropertyAction implements ActionListener {
        private JLabel[] labels;
        private JFrame frame;
        private Agent agent;
        private DefaultTableModel model;

        public AddPropertyAction(JLabel[] labels, JFrame frame, Agent agent, DefaultTableModel model) {
            this.labels = labels;
            this.frame = frame;
            this.agent = agent;
            this.model = model;
        }

        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            SQLHelper helper = SQLHelper.getInstance();

            JSpinner noOfBathroomsSpinner = (JSpinner) labels[0].getLabelFor();
            JSpinner noOfBedroomsSpinner = (JSpinner) labels[1].getLabelFor();
            JSpinner noOfBalconiesSpinner = (JSpinner) labels[2].getLabelFor();
            JTextField carpetAreaField = (JTextField) labels[3].getLabelFor();
            JRadioButton elevatorRadioButton = (JRadioButton) labels[4].getLabelFor();
            JSpinner storeySpinner = (JSpinner) labels[5].getLabelFor();
            JComboBox<String> typeOfHouseComboBox  = (JComboBox<String>) labels[6].getLabelFor();
            JTextField sizeOfKitchenField = (JTextField) labels[7].getLabelFor();
            JRadioButton terraceAccessRadioButton = (JRadioButton) labels[8].getLabelFor();
            JTextField countryField = (JTextField) labels[9].getLabelFor();
            JTextField stateField = (JTextField) labels[10].getLabelFor();
            JTextField cityField = (JTextField) labels[11].getLabelFor();
            JTextField pincodeField = (JTextField) labels[12].getLabelFor();
            JTextField localityField = (JTextField) labels[13].getLabelFor();
            JTextField landmarkField = (JTextField) labels[14].getLabelFor();

            int property_id = helper.getPropertyID();
            int no_of_bathrooms = (Integer) noOfBathroomsSpinner.getModel().getValue();
            int no_of_bedrooms = (Integer) noOfBedroomsSpinner.getModel().getValue();
            int no_of_balconies = (Integer) noOfBalconiesSpinner.getModel().getValue();
            double carpet_area = Double.parseDouble(carpetAreaField.getText().trim());
            boolean elevator = elevatorRadioButton.getModel().isSelected();
            int storey = (Integer) storeySpinner.getModel().getValue();
            String type_of_property = typeOfHouseComboBox.getModel().getSelectedItem().toString();
            double size_of_kitchen = Double.parseDouble(sizeOfKitchenField.getText().trim());
            boolean terrace_access = terraceAccessRadioButton.getModel().isSelected();
            String country = countryField.getText();
            String state = stateField.getText();
            String city = cityField.getText();
            int pincode = Integer.parseInt(pincodeField.getText().trim());
            String locality = localityField.getText();
            String landmark = landmarkField.getText();

            Date dateAdded = new Date();
            PropertyAddress address = new PropertyAddress(property_id, country, state, city, pincode, locality, landmark);
            Property property = new Property(property_id, no_of_bathrooms, no_of_bedrooms, no_of_balconies, carpet_area, elevator, storey, type_of_property, size_of_kitchen, terrace_access, address, dateAdded);

            boolean b = helper.addProperty(property, agent);
            if(b) {
                JOptionPane.showMessageDialog(null, "Successfully added Property!", "Success", JOptionPane.INFORMATION_MESSAGE, null);
                SQLHelper.getInstance().populateModelForAgent(agent.getAgent_id(), model);
            }
            else JOptionPane.showMessageDialog(null, "Error adding Property!", "Failure", JOptionPane.ERROR_MESSAGE, null);

            frame.dispose();
        }
    }
}