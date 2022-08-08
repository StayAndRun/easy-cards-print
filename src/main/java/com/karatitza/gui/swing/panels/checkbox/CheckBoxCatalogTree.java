package com.karatitza.gui.swing.panels.checkbox;

import com.karatitza.project.catalog.DecksCatalog;
import com.karatitza.project.catalog.Selectable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import javax.swing.event.CellEditorListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeCellEditor;
import javax.swing.tree.TreeCellRenderer;
import java.awt.*;
import java.util.ArrayList;
import java.util.EventObject;
import java.util.List;
import java.util.function.Consumer;

public class CheckBoxCatalogTree extends JTree {
    private static final Logger LOG = LoggerFactory.getLogger(CheckBoxCatalogTree.class);

    private Consumer<Selectable> listener = selectable -> {
    };

    public CheckBoxCatalogTree(DecksCatalog catalog) {
        super(convertSelectableToTreeNode(catalog));
        this.setToggleClickCount(0);
        this.setCellRenderer(new CheckBoxCellRenderer());
        this.setCellEditor(new CheckBoxCellEditor());
        this.setEditable(true);
        this.setInvokesStopCellEditing(true);
    }

    public void subscribe(Consumer<Selectable> listener) {
        this.listener = listener;
    }

    private static DefaultMutableTreeNode convertSelectableToTreeNode(Selectable root) {
        DefaultMutableTreeNode parentNode = new DefaultMutableTreeNode(root);
        for (Selectable child : root.children()) {
            DefaultMutableTreeNode childNode = convertSelectableToTreeNode(child);
            parentNode.add(childNode);
        }
        return parentNode;
    }

    private class CheckBoxCellRenderer extends JPanel implements TreeCellRenderer {

        private final JCheckBox checkBox;
        private final JLabel amount;

        public CheckBoxCellRenderer() {
            super();
            this.setLayout(new BorderLayout());
            this.checkBox = new JCheckBox();
            this.checkBox.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 14));
            this.amount = new JLabel();
            this.amount.setFont(new Font(Font.MONOSPACED, Font.BOLD, 12));
            this.add(checkBox, BorderLayout.WEST);
            this.add(amount, BorderLayout.EAST);
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
            amount.setText(" x" + selectable.getAmount());
            checkBox.setOpaque(selectable.isSelected());
            return this;
        }
    }

    private class CheckBoxCellEditor extends JPanel implements TreeCellEditor {

        private final List<CellEditorListener> listeners = new ArrayList<>();
        private final JCheckBox checkBox;
        private final JSpinner spinner = new JSpinner(new SpinnerNumberModel(1, 0, Short.MAX_VALUE, 1));
        private Selectable selectable;

        public CheckBoxCellEditor() {
            super();
            this.setLayout(new BorderLayout());
            this.checkBox = new JCheckBox();
            this.checkBox.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 14));
            checkBox.addActionListener(e -> updateSelectable());
            spinner.addChangeListener(e -> updateSelectable());
            this.add(checkBox, BorderLayout.WEST);
            this.add(spinner, BorderLayout.EAST);
        }

        @Override
        public Component getTreeCellEditorComponent(JTree tree, Object value, boolean isSelected, boolean expanded, boolean leaf, int row) {
            DefaultMutableTreeNode node = (DefaultMutableTreeNode) value;
            selectable = (Selectable) node.getUserObject();
            checkBox.setSelected(selectable.isSelected());
            checkBox.setText(selectable.getName());
            checkBox.setOpaque(selectable.isSelected());
            spinner.setValue(selectable.getAmount());
            return this;
        }

        @Override
        public Object getCellEditorValue() {
            return selectable;
        }

        private void updateSelectable() {
            if (checkBox.isSelected()) {
                selectable.select();
            } else {
                selectable.unselect();
            }
            selectable.setAmount(Short.parseShort(String.valueOf(spinner.getValue())));
            CheckBoxCatalogTree.this.repaint();
            listener.accept(selectable);
        }

        @Override
        public boolean isCellEditable(EventObject anEvent) {
            return true;
        }

        @Override
        public boolean shouldSelectCell(EventObject anEvent) {
            return false;
        }

        @Override
        public boolean stopCellEditing() {
            return true;
        }

        @Override
        public void cancelCellEditing() {
        }

        @Override
        public void addCellEditorListener(CellEditorListener cellEditorListener) {
            listeners.add(cellEditorListener);
        }

        @Override
        public void removeCellEditorListener(CellEditorListener editorListener) {
            listeners.remove(editorListener);
        }
    }
}