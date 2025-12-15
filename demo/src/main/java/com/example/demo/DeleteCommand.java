package com.example.demo;

import java.util.ArrayList;

public class DeleteCommand implements Command {

    private StarModel model;
    private ArrayList<Groupable> deleteditems;

    public DeleteCommand(StarModel model, ArrayList<Groupable> targs) {
        this.model = model;
        this.deleteditems = new ArrayList<>(targs);
    }

    public void doIt() {
        for(Groupable item : deleteditems){
            model.removeItem(item);
        }
        model.notifySubs();
    }

    public void undo() {
        for(Groupable item : deleteditems){
            model.addItem(item);
        }
        model.notifySubs();
    }
}
