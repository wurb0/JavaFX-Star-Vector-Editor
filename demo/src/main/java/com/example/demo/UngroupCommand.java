package com.example.demo;

public class UngroupCommand implements Command {

    public StarModel model;
    private StarGroup group;

    public UngroupCommand(StarModel model, StarGroup group) {
        this.model = model;
        this.group = group;
    }


    @Override
    public void doIt() {
        model.ungroup(group);
        model.notifySubs();
    }

    @Override
    public void undo() {
        model.groupItems(group.getChildren());
        model.notifySubs();

    }
}
