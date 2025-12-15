package com.example.demo;

import java.util.ArrayList;

public class ResizeCommand implements Command {

    private StarModel model;
    private double oldW, oldH;
    private double newW;
    private double newH;

    private ArrayList<Groupable> targs;

    public ResizeCommand(StarModel model, ArrayList<Groupable> targs, double oldW, double oldH, double newW, double newH) {
        this.model = model;
        this.targs = new ArrayList<>(targs);
        this.oldW = oldW;
        this.oldH = oldH;
        this.newW = newW;
        this.newH = newH;

    }

    @Override
    public void doIt() {
        for (Groupable g : targs) {
            g.setHeight(newH);
            g.setWidth(newW);
        }
        model.notifySubs();
    }

    @Override
    public void undo() {
        for (Groupable g : targs) {
            g.setWidth(oldW);
            g.setHeight(oldH);
        }
        model.notifySubs();
    }
}
