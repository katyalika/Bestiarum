package com.mycompany.bestiarum;

import com.mycompany.bestiarum.controller.MonsterController;
import com.mycompany.bestiarum.view.MonsterView;
import javax.swing.SwingUtilities;

/**
 *
 * @author lihac
 */
public class Main {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            MonsterController controller = new MonsterController();
            MonsterView view = new MonsterView(controller);
            view.setVisible(true);
        });
    }
}
