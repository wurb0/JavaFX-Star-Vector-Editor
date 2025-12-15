package com.example.demo;

import java.util.ArrayList;
import java.util.List;

public class MoveCommand implements Command {
    private StarModel model;
    private ArrayList<Groupable> targs;
    private double dx;
    private double dy;


    public MoveCommand(StarModel model,ArrayList<Groupable> targs, double dx, double dy) {
        this.model = model;
//        this.targs = new ArrayList<>();
//        this.targs.add(targs);
        this.targs = new ArrayList<>(targs);
        this.dx = dx;
        this.dy = dy;
    }

    @Override
    public void doIt() {
        for(Groupable g : targs){
            g.move(dx, dy);
        }
        model.notifySubs();
    }

    @Override
    public void undo() {
        for(Groupable g : targs){
            g.move(-dx, -dy);
        }
        model.notifySubs();

    }

}
