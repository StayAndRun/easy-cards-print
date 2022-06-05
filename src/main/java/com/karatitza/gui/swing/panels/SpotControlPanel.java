package com.karatitza.gui.swing.panels;

import com.karatitza.project.CardProject;
import com.karatitza.project.layout.CommonPageFormat;
import com.karatitza.project.layout.PageFormat;
import com.karatitza.project.layout.spots.SpotSize;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import static com.karatitza.project.MeasureUtils.pointsToMillimetersRound;
import static javax.swing.BorderFactory.*;

public class SpotControlPanel extends JPanel implements ChangeListener, ItemListener {
    public static final String SPOT_LAYOUTS_PROPERTY = "spot_layouts_property";

    private final CardProject cardProject;
    private final JSpinner spotHeight;
    private final JSpinner spotWidth;
    private final JSpinner spotSpace;
    private final JComboBox<PageFormat> pageSizeJComboBox;

    public SpotControlPanel(CardProject cardProject) {
        this.cardProject = cardProject;
        setLayout(new GridLayout(0, 2, 10, 10));
        setBorder(createCompoundBorder(
                createEmptyBorder(10, 10, 10, 10), createTitledBorder("Spots layout control"))
        );
        spotHeight = addSpotHeightSpinner();
        spotWidth = addSpotWidthSpinner();
        spotSpace = addSpotSpaceSpinner();
        pageSizeJComboBox = addPageFormatComboBox();
    }

    private JSpinner addSpotHeightSpinner() {
        add(new JLabel("Spot Height:"));
        int initialValue = pointsToMillimetersRound(cardProject.getSpotsLayout().getSpotSize().getHeight());
        JSpinner spinner = new JSpinner(new SpinnerNumberModel(initialValue, 10, Integer.MAX_VALUE, 1));
        spinner.addChangeListener(this);
        add(spinner);
        return spinner;
    }

    private JSpinner addSpotWidthSpinner() {
        add(new JLabel("Spot Width:"));
        int initialValue = pointsToMillimetersRound(cardProject.getSpotsLayout().getSpotSize().getWidth());
        JSpinner spinner = new JSpinner(new SpinnerNumberModel(initialValue, 10, Integer.MAX_VALUE, 1));
        spinner.addChangeListener(this);
        add(spinner);
        return spinner;
    }

    private JSpinner addSpotSpaceSpinner() {
        add(new JLabel("Spot Space:"));
        int initialValue = pointsToMillimetersRound(cardProject.getSpotsLayout().getSpotSize().getSpace());
        JSpinner spinner = new JSpinner(new SpinnerNumberModel(initialValue, 0, Integer.MAX_VALUE, 1));
        spinner.addChangeListener(this);
        add(spinner);
        return spinner;
    }

    private JComboBox<PageFormat> addPageFormatComboBox() {
        add(new Label("Page Size:"));
        PageFormat initialFormat = cardProject.getSpotsLayout().getPageFormat();
        ComboBoxModel<PageFormat> comboBoxModel = new DefaultComboBoxModel<>(CommonPageFormat.values());
        comboBoxModel.setSelectedItem(initialFormat);
        JComboBox<PageFormat> pageSizeJComboBox = new JComboBox<>(comboBoxModel);
        add(pageSizeJComboBox);
        pageSizeJComboBox.addItemListener(this);
        return pageSizeJComboBox;
    }

    @Override
    public void stateChanged(ChangeEvent e) {
        updateProjectLayout();
    }

    @Override
    public void itemStateChanged(ItemEvent e) {
        updateProjectLayout();
    }

    public void addLayoutsChangeListener(SpotsLayoutListener listener) {
        spotHeight.addChangeListener(listener);
        spotWidth.addChangeListener(listener);
        spotSpace.addChangeListener(listener);
        pageSizeJComboBox.addItemListener(listener);
    }

    private void updateProjectLayout() {
        int height = Integer.parseInt(String.valueOf(spotHeight.getValue()));
        int width = Integer.parseInt(String.valueOf(spotWidth.getValue()));
        int space = Integer.parseInt(String.valueOf(spotSpace.getValue()));
        PageFormat pageFormat = (PageFormat) pageSizeJComboBox.getSelectedItem();
        cardProject.selectSpots(pageFormat, SpotSize.millimeters(height, width, space));
    }
}
