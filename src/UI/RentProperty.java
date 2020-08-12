package UI;

import DBHelper.SQLHelper;
import Models.Agent;
import Models.Property;
import Utility.SpringUtilities;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

public class RentProperty {
    private Agent agent;
    private int property_id;
    private JFrame frame;
    private boolean created;

    public RentProperty(int property_id, Agent agent) {
        this.property_id = property_id;
        this.agent = agent;
        frame = new MyFrame("Rent Property", 300, 280);
        frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        created = false;
    }

    private void createRentPropertyDetailForm() {
        JPanel panel = new JPanel(new SpringLayout());

        String[] labelHeads = {"Property ID", "Agent ID", "Agreement Start Date", "Agreement End Date", "Monthly Rent Amount"};
        JLabel[] labels = new JLabel[labelHeads.length];
        for (int i = 0; i < labelHeads.length; ++i) {
            String label = labelHeads[i];
            labels[i] = new JLabel(label);

            panel.add(labels[i]);
            switch (label) {
                case "Property ID":
                    JTextField property_idField = new JTextField(property_id + "");
                    property_idField.setEditable(false);
                    property_idField.setEnabled(false);
                    panel.add(property_idField);
                    labels[i].setLabelFor(property_idField);
                    break;

                case "Agent ID":
                    JTextField agentNameField = new JTextField(agent.getAgent_id() + "");
                    agentNameField.setEditable(false);
                    agentNameField.setEnabled(false);
                    panel.add(agentNameField);
                    labels[i].setLabelFor(agentNameField);
                    break;

                case "Agreement Start Date":
                    Date date = new Date();
                    SimpleDateFormat sdf = new SimpleDateFormat("YYYY-MM-dd");
                    JTextField startDateField = new JTextField(sdf.format(date));
                    startDateField.setEditable(false);
                    startDateField.setEnabled(false);
                    panel.add(startDateField);
                    labels[i].setLabelFor(startDateField);
                    break;

                case "Agreement End Date":
                    date = new Date();
                    LocalDateTime newTime = LocalDateTime.now().plusMonths(11);
                    date = Date.from( newTime.atZone(ZoneId.systemDefault()).toInstant());
                    sdf = new SimpleDateFormat("YYYY-MM-dd");
                    JTextField endDateField = new JTextField(sdf.format(date));
                    endDateField.setEditable(false);
                    endDateField.setEnabled(false);
                    labels[i].setLabelFor(endDateField);
                    panel.add(endDateField);
                    break;

                default:
                    JTextField field = new JTextField();
                    labels[i].setLabelFor(field);
                    panel.add(field);
                    break;
            }
        }

        JLabel dummyLabel = new JLabel("");
        JButton registerButton = new JButton("Rent Property");
        registerButton.addActionListener(new RentPropertyAction(labels, frame));
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
            createRentPropertyDetailForm();
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

    static class RentPropertyAction implements ActionListener {
        private JLabel[] labels;
        private JFrame frame;

        public RentPropertyAction(JLabel[] labels, JFrame frame) {
            this.labels = labels;
            this.frame = frame;
        }

        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            SimpleDateFormat sdf = new SimpleDateFormat("YYYY-MM-dd");
            try {
                int property_id = Integer.parseInt(((JTextField) labels[0].getLabelFor()).getText().trim());
                int agent_id = Integer.parseInt(((JTextField) labels[1].getLabelFor()).getText().trim());
                Date start_date = sdf.parse(((JTextField) labels[2].getLabelFor()).getText().trim());
                Date end_date = sdf.parse(((JTextField) labels[3].getLabelFor()).getText().trim());
                double rent_amount = Double.parseDouble(((JTextField) labels[4].getLabelFor()).getText().trim());

                boolean ret = SQLHelper.getInstance().rentProperty(property_id, agent_id, rent_amount, start_date, end_date);
                if(ret) JOptionPane.showMessageDialog(null, "Rented out Property!", "Success", JOptionPane.INFORMATION_MESSAGE, null);
                else JOptionPane.showMessageDialog(null, "Could not rent out Property!", "Failure", JOptionPane.ERROR_MESSAGE, null);
                frame.dispose();
            } catch (ParseException e) {

            }
        }
    }
}