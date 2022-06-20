package com.karatitza.project.catalog;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Consumer;

public abstract class Selectable {

    private final String id = UUID.randomUUID().toString();
    private final String name;
    private boolean selected = true;
    private Consumer<Boolean> selectionListener;

    public Selectable(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public boolean isSelected() {
        return selected;
    }

    public void select() {
        if (!selected) {
            selected = true;
            getSelectionListener().ifPresent(consumer -> consumer.accept(true));
            children().forEach(Selectable::select);
        }
    }

    public void unselect() {
        if (selected) {
            selected = false;
            getSelectionListener().ifPresent(consumer -> consumer.accept(false));
            children().forEach(Selectable::unselect);
        }
    }

    public void enableChildSelectionSensitive() {
        children().forEach(child -> {
            child.subscribe(this::childSelectionChangeEvent);
            child.enableChildSelectionSensitive();
        });
    }

    protected void subscribe(Consumer<Boolean> selectionListener) {
        this.selectionListener = selectionListener;
    }

    protected List<? extends Selectable> children() {
        return Collections.emptyList();
    }

    private void childSelectionChangeEvent(boolean childSelected) {
        if (childSelected) {
            this.select();
        } else {
            boolean isAllChildUnselected = children().stream().noneMatch(Selectable::isSelected);
            if (isAllChildUnselected) {
                this.unselect();
            }
        }
    }

    private Optional<Consumer<Boolean>> getSelectionListener() {
        return Optional.ofNullable(selectionListener);
    }
}
