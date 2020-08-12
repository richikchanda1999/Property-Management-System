package UI;

import DBHelper.SQLHelper;
import Models.Agent;
import Models.AgentAddress;
import Models.Property;
import Models.PropertyAddress;
import Utility.SpringUtilities;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;

public class Register {
    private JFrame frame;
    private boolean created;

    public Register() {
        frame = new MyFrame("Register User", 300, 514);
        created = false;
    }

    private void createRegistrationForm() {
        JPanel panel = new JPanel(new SpringLayout());

        String[] labelHeads = {"First name", "Last name", "Username", "Password", "Confirm Password",
                "Country", "State", "City", "Pincode", "Locality", "Landmark", "Phone Number(s)"};
        JLabel[] labels = new JLabel[labelHeads.length];
        for (int i = 0; i < labelHeads.length; ++i) {
            String label = labelHeads[i];
            labels[i] = new JLabel(label);

            if (label.equals("Password") || label.equals("Confirm Password"))
                labels[i].setLabelFor(new JPasswordField());
            else if (label.equals("Phone Number(s)")) labels[i].setLabelFor(new JTextArea());
            else labels[i].setLabelFor(new JTextField());

            panel.add(labels[i]);
            panel.add(labels[i].getLabelFor());
        }

        JLabel dummyLabel = new JLabel("");
        JButton registerButton = new JButton("Register");
        registerButton.addActionListener(new RegisterAction(labels, frame));
        dummyLabel.setLabelFor(registerButton);
        panel.add(dummyLabel);
        panel.add(registerButton);

        SpringUtilities.makeCompactGrid(panel, labelHeads.length + 1, 2, 6, 6, 6, 6);
        frame.add(panel, BorderLayout.CENTER);
    }

    private void createLoginOption() {
        JPanel panel = new JPanel(new SpringLayout());

        panel.add(new JSeparator());
        JButton loginButton = new JButton("BACK TO Login");
        loginButton.addActionListener(new LoginAction(frame));
        panel.add(loginButton);

        SpringUtilities.makeCompactGrid(panel,
                2, 1, //rows, cols
                6, 6,        //initX, initY
                6, 6);       //xPad, yPad

        frame.add(panel, BorderLayout.SOUTH);
    }

    public void show() {
        if (!created) {
            createRegistrationForm();
            createLoginOption();
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

    static class LoginAction implements ActionListener {
        private JFrame frame;

        public LoginAction(JFrame frame) {
            this.frame = frame;
        }

        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            Login l = new Login();
            l.show();
            l.toFront();
            frame.dispose();
        }
    }

    static class RegisterAction implements ActionListener {
        private JLabel[] labels;
        private JFrame frame;

        public RegisterAction(JLabel[] labels, JFrame frame) {
            this.labels = labels;
            this.frame = frame;
        }

        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            JTextField firstNameField = (JTextField) labels[0].getLabelFor();
            JTextField lastNameField = (JTextField) labels[1].getLabelFor();
            JTextField usernameField = (JTextField) labels[2].getLabelFor();
            JPasswordField passwordField = (JPasswordField) labels[3].getLabelFor();
            JPasswordField confirmPasswordField = (JPasswordField) labels[4].getLabelFor();
            JTextField countryField = (JTextField) labels[5].getLabelFor();
            JTextField stateField = (JTextField) labels[6].getLabelFor();
            JTextField cityField = (JTextField) labels[7].getLabelFor();
            JTextField pincodeField = (JTextField) labels[8].getLabelFor();
            JTextField localityField = (JTextField) labels[9].getLabelFor();
            JTextField landmarkField = (JTextField) labels[10].getLabelFor();
            JTextArea phoneNumbersField = (JTextArea) labels[11].getLabelFor();

            if(!Arrays.equals(passwordField.getPassword(), confirmPasswordField.getPassword())) {
                JOptionPane.showMessageDialog(null, "The Passwords don't match", "Password mismatch", JOptionPane.ERROR_MESSAGE);
                return;
            }

            SQLHelper helper = SQLHelper.getInstance();

            String[] phoneNumbers = phoneNumbersField.getText().split("[^0-9]");
            int agentID = helper.getAgentID();
            AgentAddress address = new AgentAddress(agentID, countryField.getText(), stateField.getText(), cityField.getText(), Integer.parseInt(pincodeField.getText()), localityField.getText(), landmarkField.getText());
            Agent agent = new Agent(agentID, firstNameField.getText(), lastNameField.getText(), usernameField.getText(), address, phoneNumbers);

            boolean b1 = helper.addUser(usernameField.getText(), new String(passwordField.getPassword()));
            boolean b2 = helper.addAgent(agent);

            if(b1 && b2) {
                JOptionPane.showMessageDialog(null, "Registration Succesful!", "Registration DONE", JOptionPane.INFORMATION_MESSAGE, null);
            } else {
                JOptionPane.showMessageDialog(null, "Registration could not be completed!", "Registration Error", JOptionPane.ERROR_MESSAGE, null);
            }
            frame.dispose();
            Login login = new Login();
            login.show();
        }
    }
}