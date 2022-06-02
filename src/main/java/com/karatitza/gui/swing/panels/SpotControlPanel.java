package com.karatitza.gui.swing.panels;

import com.karatitza.project.layout.CommonPageFormat;
import com.karatitza.project.layout.PageFormat;
import com.karatitza.project.layout.spots.SpotSize;
import com.karatitza.project.layout.spots.SpotsLayout;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import static com.karatitza.project.MeasureUtils.pointsToMillimetersRound;
import static javax.swing.BorderFactory.*;

public class SpotControlPanel extends JPanel implements ChangeListener, ItemListener {

    private SpotsLayoutPreview spotsLayoutPreview = SpotsLayoutPreview.defaultLogger();
    private final JSpinner spotHeight;
    private final JSpinner spotWidth;
    private final JSpinner spotSpace;
    private final JComboBox<PageFormat> pageSizeJComboBox;

    public SpotControlPanel() {
        spotHeight = new JSpinner(new SpinnerNumberModel(50, 10, Integer.MAX_VALUE, 1));
        spotHeight.setToolTipText("test");
        spotWidth = new JSpinner(new SpinnerNumberModel(50, 10, Integer.MAX_VALUE, 1));
        spotSpace = new JSpinner(new SpinnerNumberModel(0, 0, Integer.MAX_VALUE, 1));
        ComboBoxModel<PageFormat> comboBoxModel = new DefaultComboBoxModel<>(CommonPageFormat.values());
        comboBoxModel.setSelectedItem(CommonPageFormat.A4);
        pageSizeJComboBox = new JComboBox<>(comboBoxModel);
        setLayout(new GridLayout(0, 2, 10, 10));
        setBorder(createCompoundBorder(
                createEmptyBorder(10, 10, 10, 10), createTitledBorder("Spots layout control"))
        );
        spotHeight.addChangeListener(this);
        spotWidth.addChangeListener(this);
        spotSpace.addChangeListener(this);
        pageSizeJComboBox.addItemListener(this);
        add(new JLabel("Spot Height:"));
        add(spotHeight);
        add(new JLabel("Spot Width:"));
        add(spotWidth);
        add(new JLabel("Spot Space:"));
        add(spotSpace);
        add(new Label("Page Size:"));
        add(pageSizeJComboBox);
        refreshPreview();
    }

    public SpotControlPanel setInitialSpots(SpotsLayout spots) {
        SpotSize spotSize = spots.getSpotSize();
        spotHeight.setValue(pointsToMillimetersRound(spotSize.getHeight()));
        spotWidth.setValue(pointsToMillimetersRound(spotSize.getWidth()));
        spotSpace.setValue(pointsToMillimetersRound(spotSize.getSpace()));
        pageSizeJComboBox.getModel().setSelectedItem(spots.getPageFormat());
        return this;
    }

    public SpotControlPanel setSpotsLayoutPreview(SpotsLayoutPreview spotsLayoutPreview) {
        this.spotsLayoutPreview = spotsLayoutPreview;
        refreshPreview();
        return this;
    }

    @Override
    public void stateChanged(ChangeEvent e) {
        refreshPreview();
    }

    @Override
    public void itemStateChanged(ItemEvent e) {
        refreshPreview();
    }

    private void refreshPreview() {
        spotsLayoutPreview.refresh();
    }
}
