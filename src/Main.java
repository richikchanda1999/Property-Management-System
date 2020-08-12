import UI.Login;

import javax.swing.*;
import java.awt.*;

public class Main {
    public JFrame getFrame() {
        JFrame frame = new JFrame();

        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        frame.setSize(new Dimension(dim.width, dim.height));
        frame.setLocation(0, 0);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        return frame;
    }

    public static void main(String[] args) {
        for(UIManager.LookAndFeelInfo lf : UIManager.getInstalledLookAndFeels()) if(lf.getName().equals("Nimbus")) {
            try {
                UIManager.setLookAndFeel(lf.getClassName());
            } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException e) {
                e.printStackTrace();
            }
        }

        Login login = new Login();
        login.show();
        login.toFront();
    }
}
