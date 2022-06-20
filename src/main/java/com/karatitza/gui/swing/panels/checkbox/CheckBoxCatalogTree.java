package com.karatitza.gui.swing.panels.checkbox;

import com.karatitza.project.catalog.DecksCatalog;
import com.karatitza.project.catalog.Selectable;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeSelectionModel;
import javax.swing.tree.TreeCellRenderer;
import javax.swing.tree.TreePath;
import java.awt.*;

public class CheckBoxCatalogTree extends JTree {

    public CheckBoxCatalogTree(DecksCatalog catalog) {
        super(convertSelectableToTreeNode(catalog));
        this.setToggleClickCount(0);
        this.setCellRenderer(new CheckBoxCellRenderer());
        this.setSelectionModel(new CheckBoxSelectionModel());
    }

    private static DefaultMutableTreeNode convertSelectableToTreeNode(Selectable root) {
        DefaultMutableTreeNode parentNode = new DefaultMutableTreeNode(root);
        for (Selectable child : root.children()) {
            DefaultMutableTreeNode childNode = convertSelectableToTreeNode(child);
            parentNode.add(childNode);
        }
        return parentNode;
    }

    private static class CheckBoxCellRenderer extends JPanel implements TreeCellRenderer {

        private final JCheckBox checkBox;

        public CheckBoxCellRenderer() {
            super();
            this.setLayout(new BorderLayout());
            this.checkBox = new JCheckBox();
            this.add(checkBox, BorderLayout.CENTER);
            this.setOpaque(false);
        }

        @Override
        public Component getTreeCellRendererComponent(JTree tree, Object value,
                                                      boolean selected, boolean expanded, boolean leaf, int row,
                                                      boolean hasFocus) {
            DefaultMutableTreeNode node = (DefaultMutableTreeNode) value;
            Selectable selectable = (Selectable) node.getUserObject();
            checkBox.setSelected(selectable.isSelected());
            checkBox.setText(selectable.getName());
            checkBox.setOpaque(selectable.isSelected());
            return this;
        }
    }

    private class CheckBoxSelectionModel extends DefaultTreeSelectionModel {
        public void setSelectionPath(TreePath path) {
            DefaultMutableTreeNode node = (DefaultMutableTreeNode) path.getLastPathComponent();
            Selectable selectable = (Selectable) node.getUserObject();
            if (selectable.isSelected()) {
                selectable.unselect();
            } else {
                selectable.select();
            }
            CheckBoxCatalogTree.this.repaint();
        }

        public void addSelectionPath(TreePath path) {
        }

        public void removeSelectionPath(TreePath path) {
        }

        public void setSelectionPaths(TreePath[] pPaths) {
        }
    }
}