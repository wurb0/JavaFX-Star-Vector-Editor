package com.example.demo;

public class CreateCommand implements Command {
    private StarModel model;
    private Groupable item;
    private double x,y,w,h;

    public CreateCommand(StarModel model, double x, double y, double w, double h) {
        this.model = model;
        this.x = x;
        this.y = y;
        this.w = w;
        this.h = h;
    }
    public void doIt(){
        if(item == null){
            item = new Star(x,y,w,h,0);
        }
        model.addItem(item);
        model.notifySubs();
    }

    public void undo(){
        model.removeItem(item);
        model.notifySubs();

    }
}
