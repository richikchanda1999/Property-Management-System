package UI;


import javax.swing.*;
import java.awt.*;

public class MyFrame extends JFrame {
    private String title;
    private double width, height;

    public MyFrame(String title, int width, int height) {
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        this.setTitle(title);
        this.setSize(width, height);
        this.setLocation(dim.width / 2 - this.getSize().width / 2, dim.height / 2 - this.getSize().height / 2);
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
//        this.setResizable(false);

        BorderLayout borderLayout = new BorderLayout();
        this.setLayout(borderLayout);
    }
}
