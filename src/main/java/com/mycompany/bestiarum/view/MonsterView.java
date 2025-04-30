package com.mycompany.bestiarum.view;

import com.mycompany.bestiarum.controller.MonsterController;
import com.mycompany.bestiarum.model.Monster;
import java.awt.BorderLayout;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;

/**
 *
 * @author lihac
 */
public class MonsterView extends JFrame {

    private final MonsterController controller;
    private JTree monsterTree;
    private JTextArea monsterDetails;

    public MonsterView(MonsterController controller) {
        this.controller = controller;
        initializeUI();
    }

    private void initializeUI() {
        setTitle("Bestiarum");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        splitPane.setDividerLocation(300);

        monsterTree = new JTree();
        JScrollPane treeScroll = new JScrollPane(monsterTree);
        splitPane.setLeftComponent(treeScroll);

        monsterDetails = new JTextArea();
        monsterDetails.setEditable(false);
        JScrollPane detailsScroll = new JScrollPane(monsterDetails);
        splitPane.setRightComponent(detailsScroll);

        JPanel buttonPanel = new JPanel();
        JButton importButton = new JButton("Import Files");
        importButton.addActionListener(e -> importFiles());
        buttonPanel.add(importButton);

        JButton exportButton = new JButton("Export Selected");
        exportButton.addActionListener(e -> exportSelected());
        buttonPanel.add(exportButton);

        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(splitPane, BorderLayout.CENTER);
        getContentPane().add(buttonPanel, BorderLayout.SOUTH);

        updateTree();
    }

    private void importFiles() {
        controller.importFiles(this, monsters -> {
            updateTree();
            JOptionPane.showMessageDialog(this,
                    "Imported " + monsters.size() + " monsters",
                    "Import Complete", JOptionPane.INFORMATION_MESSAGE);
        });
    }

    private void exportSelected() {
        // Реализация экспорта выбранных монстров
    }

    private void updateTree() {
        DefaultMutableTreeNode root = new DefaultMutableTreeNode("Monsters");

        Map<String, List<Monster>> monstersBySource = new HashMap<>();
        for (Monster monster : controller.getAllMonsters()) {
            monstersBySource.computeIfAbsent(monster.getSource(), k -> new ArrayList<>()).add(monster);
        }

        for (Map.Entry<String, List<Monster>> entry : monstersBySource.entrySet()) {
            DefaultMutableTreeNode sourceNode = new DefaultMutableTreeNode(entry.getKey());
            for (Monster monster : entry.getValue()) {
                sourceNode.add(new DefaultMutableTreeNode(monster));
            }
            root.add(sourceNode);
        }

        monsterTree.setModel(new DefaultTreeModel(root));
        monsterTree.addTreeSelectionListener(e -> {
            DefaultMutableTreeNode node = (DefaultMutableTreeNode) monsterTree.getLastSelectedPathComponent();
            if (node != null && node.getUserObject() instanceof Monster) {
                showMonsterDetails((Monster) node.getUserObject());
            }
        });
    }

    private void showMonsterDetails(Monster monster) {
        StringBuilder sb = new StringBuilder();
        sb.append("Name: ").append(monster.getName()).append("\n");
        sb.append("Danger Level: ").append(monster.getDangerLevel()).append("\n");
        sb.append("Source: ").append(monster.getSource()).append("\n");
        sb.append("Habitats: ").append(String.join(", ", monster.getHabitats())).append("\n");
        sb.append("First Mentioned: ").append(monster.getFirstMentioned()).append("\n");
        sb.append("Vulnerabilities: ").append(String.join(", ", monster.getVulnerabilities())).append("\n");

        monsterDetails.setText(sb.toString());
    }
}
