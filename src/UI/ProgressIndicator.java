package UI;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;

import static java.lang.Thread.sleep;

public class ProgressIndicator implements Runnable{
    private int milliseconds;

    public ProgressIndicator(int milliseconds) {
        this.milliseconds = milliseconds;
    }

    @Override
    public void run() {
        JFrame frame = new MyFrame("Loading...", 300, 60);
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        JProgressBar progressBar = new JProgressBar();
        progressBar.setMinimum(0);
        progressBar.setMaximum(100);
        progressBar.setValue(0);
        progressBar.setIndeterminate(false);

        frame.add(progressBar, BorderLayout.CENTER);
        frame.setVisible(true);

        final Timer t = new Timer(50, new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                progressBar.setValue(progressBar.getValue() + 1);
                if (progressBar.getValue() == 100) {
                    ((Timer) e.getSource()).stop();
                }
            }
        });
        t.start();

//        int i = progressBar.getMinimum();
//        while (i <= progressBar.getMaximum()) {
//            progressBar.setValue(i);
//            System.out.println(i);
//            try {
//                sleep(milliseconds / progressBar.getMaximum());
//            } catch (InterruptedException ignored) {
//
//            }
//            ++i;
//        }
//        progressBar.setValue(progressBar.getMaximum());
        frame.dispose();
    }
}
