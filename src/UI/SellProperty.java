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
import java.util.Date;

public class SellProperty {
    private Agent agent;
    private int property_id;
    private JFrame frame;
    private boolean created;

    public SellProperty(int property_id, Agent agent) {
        this.property_id = property_id;
        this.agent = agent;
        frame = new MyFrame("Sell Property", 300, 250);
        frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        created = false;
    }

    private void createSellPropertyDetailForm() {
        JPanel panel = new JPanel(new SpringLayout());

        String[] labelHeads = {"Property ID", "Agent ID", "Selling Price", "Sale Date"};
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

                case "Sale Date":
                    Date date = new Date();
                    SimpleDateFormat sdf = new SimpleDateFormat("YYYY-MM-dd");
                    JTextField startDateField = new JTextField(sdf.format(date));
                    startDateField.setEditable(false);
                    startDateField.setEnabled(false);
                    panel.add(startDateField);
                    labels[i].setLabelFor(startDateField);
                    break;

                default:
                    JTextField field = new JTextField();
                    panel.add(field);
                    labels[i].setLabelFor(field);
                    break;
            }
        }

        JLabel dummyLabel = new JLabel("");
        JButton registerButton = new JButton("Sell Property");
        registerButton.addActionListener(new SellPropertyAction(labels, frame));
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
            createSellPropertyDetailForm();
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

    static class SellPropertyAction implements ActionListener {
        private Property property;
        private Agent agent;
        private JLabel[] labels;
        private JFrame frame;

        public SellPropertyAction(JLabel[] labels, JFrame frame) {
            this.labels = labels;
            this.frame = frame;
        }

        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            SimpleDateFormat sdf = new SimpleDateFormat("YYYY-MM-dd");
            try {
                int property_id = Integer.parseInt(((JTextField) labels[0].getLabelFor()).getText().trim());
                int agent_id = Integer.parseInt(((JTextField) labels[1].getLabelFor()).getText().trim());
                double selling_price = Double.parseDouble(((JTextField) labels[2].getLabelFor()).getText().trim());
                Date date = sdf.parse(((JTextField) labels[3].getLabelFor()).getText().trim());

                boolean ret = SQLHelper.getInstance().sellProperty(property_id, agent_id, selling_price, date);
                if (ret)
                    JOptionPane.showMessageDialog(null, "Sold Property!", "Success", JOptionPane.INFORMATION_MESSAGE, null);
                else
                    JOptionPane.showMessageDialog(null, "Could not sell Property!", "Failure", JOptionPane.ERROR_MESSAGE, null);
                frame.dispose();
            } catch (ParseException e) {

            }
        }
    }
}