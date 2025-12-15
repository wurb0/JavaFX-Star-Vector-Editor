package com.example.demo;

import java.util.ArrayList;

public class RotateCommand implements Command {

    private StarModel model;
    private ArrayList<Groupable> targs;
    private double oldAngle, newangle;

    public RotateCommand(StarModel model, ArrayList<Groupable> targs, double oldAngle, double newangle) {
        this.model = model;
        this.targs = new ArrayList<>(targs);
        this.oldAngle = oldAngle;
        this.newangle = newangle;

    }

    @Override
    public void doIt() {
        for (Groupable group : targs) {
            group.setAngle(newangle);
        }
        model.notifySubs();
    }

    @Override
    public void undo() {
        for (Groupable group : targs) {
            group.setAngle(oldAngle);
        }
        model.notifySubs();
    }



}
