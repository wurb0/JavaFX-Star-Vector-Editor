package com.example.demo;

import java.util.ArrayList;
import java.util.Stack;

public class InteractionModel {
    public double pressX;
    public double pressY;
    public Star previewStar;

    public double lastX;
    public double lastY;

    public double startMouseAngle;

    public ArrayList<Groupable> selectedStars = new ArrayList<>();

    //undo redo
    public Stack<Command> undoStack = new Stack<>();
    public Stack<Command> redoStack = new Stack<>();
    private StarModel model;
    public void setModel(StarModel model) {
        this.model = model;
    }

    public double startMoveX;
    public double startMoveY;

    // rotating
    public double startMouseX;
    public double initAngle;
    public double originalAngle; // this is for undo redo

    public double originalWidth;
    public double originalHeight;

    // state machine
    public enum Mode{
        IDLE,
        CREATING,
        MOVING,
        RESIZING, ROTATING
    }



    public void executeCommand(Command command){
        command.doIt();
        undoStack.push(command);
        redoStack.clear();
       // model.notifySubs();
    }

    public void undo(){
        if(!undoStack.isEmpty()){
            Command cmd = undoStack.pop();
            cmd.undo();
            redoStack.push(cmd);
            model.notifySubs();
        }
    }

    public void redo(){
        if(!redoStack.isEmpty()){
            Command cmd = redoStack.pop();
            cmd.doIt();
            undoStack.push(cmd);
            model.notifySubs();
        }
    }
    public Mode mode = Mode.IDLE;


    public Star getPreviewStar() {
        return previewStar;
    }


    public boolean hasSelection(){
        return !selectedStars.isEmpty();
    }

    public Groupable getFirstSelectedStar(){
        if(!selectedStars.isEmpty()){
            return selectedStars.get(0);
        }
        return null;
    }

    public void clearSelection(){
        selectedStars.clear();
    }
    public void addSelectedStar(Groupable star) {
        if (!selectedStars.contains(star)) {
            selectedStars.add(star);
        }
    }
    public void removeSelectedStar(Groupable star) {
        selectedStars.remove(star);
    }
    public boolean isSelected(Groupable star){
        return selectedStars.contains(star);
    }
    public void setSelection(Groupable star){
        selectedStars.clear();
        selectedStars.add(star); // a star can be a group,, should prolly rename star to
    }





}
