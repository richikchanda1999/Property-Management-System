package UI;

import DBHelper.SQLHelper;
import Models.Agent;
import Utility.SpringUtilities;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Login {
    private JFrame frame;
    private boolean created;

    public Login() {
        frame = new MyFrame("User Login", 300, 184);
        created = false;
    }

    public void createLoginForm() {
        JPanel panel = new JPanel(new SpringLayout());

        JLabel usernameLabel = new JLabel("Username : ");
        JTextField usernameField = new JTextField();
        usernameLabel.setLabelFor(usernameField);
        panel.add(usernameLabel);
        panel.add(usernameField);

        JLabel passwordLabel = new JLabel("Password : ");
        JPasswordField passwordField = new JPasswordField();
        passwordLabel.setLabelFor(passwordField);
        panel.add(passwordLabel);
        panel.add(passwordField);

        JLabel dummyLabel = new JLabel("");
        JButton loginButton = new JButton("Login");
        loginButton.addActionListener(new LoginAction(usernameField, passwordField, frame));
        dummyLabel.setLabelFor(loginButton);
        panel.add(dummyLabel);
        panel.add(loginButton);

        SpringUtilities.makeCompactGrid(panel, 3, 2, 6, 6, 6, 6);
        frame.add(panel, BorderLayout.CENTER);
    }

    public void createRegistrationOption() {
        JPanel panel = new JPanel(new SpringLayout());

        panel.add(new JSeparator());
        JButton registerButton = new JButton("OR, Register");
        registerButton.addActionListener(new RegisterAction(frame));
        panel.add(registerButton);

        SpringUtilities.makeCompactGrid(panel,
                2, 1, //rows, cols
                6, 6,        //initX, initY
                6, 6);       //xPad, yPad

        frame.add(panel, BorderLayout.SOUTH);
    }

    public void show() {
        if (!created) {
            createLoginForm();
            createRegistrationOption();
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
        private JTextField usernameField;
        private JPasswordField passwordField;
        private JFrame frame;

        public LoginAction(JTextField usernameField, JPasswordField passwordField, JFrame frame) {
            this.usernameField = usernameField;
            this.passwordField = passwordField;
            this.frame = frame;
        }

        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            String username = usernameField.getText();
            String password = new String(passwordField.getPassword());

//            ProgressIndicator progressIndicator = new ProgressIndicator(1000);
//            Thread t = new Thread(progressIndicator);
//            t.start();

            SQLHelper helper = SQLHelper.getInstance();
            int id = helper.login(username, password);
            if (id == -1)
                JOptionPane.showMessageDialog(null, "Login unsuccessful", "User not found", JOptionPane.ERROR_MESSAGE, null);
            else {
                if (id == 0) {
                    //Real Estate Office
                    RealEstateOfficeHomePage reohp = new RealEstateOfficeHomePage();
                    reohp.show();
                    frame.dispose();
                } else if (id > 0) {
                    //Agent
                    AgentHomePage ahp = new AgentHomePage(helper.agentFromUsername(username));
                    ahp.show();
                    frame.dispose();
                }
            }
        }
    }

    static class RegisterAction implements ActionListener {
        private JFrame frame;

        public RegisterAction(JFrame frame) {
            this.frame = frame;
        }

        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            Register r = new Register();
            r.show();
            r.toFront();
            frame.dispose();
        }
    }
}