package com.example.demo;

import java.util.ArrayList;

public class GroupCommand implements Command {

    private StarModel model;
    private ArrayList<Groupable> groups;
    private StarGroup newGroup;
    public GroupCommand(StarModel model, ArrayList<Groupable> groupthese) {
        this.model = model;
        this.groups = new ArrayList<>(groupthese);
    }

    public void doIt(){
        newGroup = model.groupItems(groups);
        model.notifySubs();
    }

    public void undo(){
        model.ungroup(newGroup);
        model.notifySubs();
    }


}
