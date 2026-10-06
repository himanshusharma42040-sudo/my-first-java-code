package com.example.lms;

import com.example.lms.ui.LoginFrame;
import javax.swing.*;
import java.awt.*;

public final class App {
    private App() {}
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                    if ("Nimbus".equals(info.getName())) {
                        UIManager.setLookAndFeel(info.getClassName());
                        break;
                    }
                }
            } catch (Exception ignored) {}
            UIManager.put("nimbusBase", new Color(38, 78, 140));
            new LoginFrame().setVisible(true);
        });
    }
}
