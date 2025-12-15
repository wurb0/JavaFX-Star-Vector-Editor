package com.example.demo;

import javafx.scene.Group;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import javafx.scene.paint.Color;

import java.util.ArrayList;

public class StarController{

    private StarModel model;
    private InteractionModel iModel;
    private StarView view;


    // total movement so it cna undo to the right pos
    private double totalDx=0;
    private double totalDy=0;

    public StarController(StarModel model, InteractionModel iModel, StarView view) {
        this.model = model;
        this.iModel = iModel;
        this.view = view;

        setupEvents();
    }
    private void setupEvents() {
        handleMousePressed();
        handleMouseReleased();
        handleMouseDragged();
        handleKeyPressed();
    }


    private void handleMouseDragged(){
        view.getCanvas().setOnMouseDragged(event -> {
            if(iModel.mode == InteractionModel.Mode.CREATING){
                double w= event.getX()- iModel.pressX;
                double h= event.getY()- iModel.pressY;
                if (w > 0 && h > 0) {
                    double cx = iModel.pressX + w/2;
                    double cy = iModel.pressY + h/2;
                    iModel.previewStar = new Star(cx,cy, Math.abs(h),Math.abs(w),0);
                }else{
                    iModel.previewStar = null;
                }
                model.notifySubs();
            } else if (iModel.mode == InteractionModel.Mode.MOVING && iModel.hasSelection()) {
                double dx =  event.getX() - iModel.pressX;
                double dy = event.getY() - iModel.pressY;

                totalDx+=dx;
                totalDy +=dy;

                for(Groupable g: iModel.selectedStars){
                    g.move(dx,dy);
                }

                iModel.pressX = event.getX();
                iModel.pressY = event.getY();
                model.notifySubs();
            } else if (iModel.mode == InteractionModel.Mode.RESIZING && iModel.hasSelection()) {
                Groupable prim = iModel.getFirstSelectedStar();
                if (prim == null) {
                    return;
                }
                double dx = event.getX() - prim.getX();
                double dy = event.getY() - prim.getY();
                double newWidth = Math.max(30, 2 * Math.abs(dx));
                double newHeight = Math.max(30, 2 * Math.abs(dy));

                //fo
                for (Groupable g : iModel.selectedStars) {
                    g.setWidth(newWidth);
                    g.setHeight(newHeight);
                    //g.setAngle();
                }
                model.notifySubs();

            }else if (iModel.mode == InteractionModel.Mode.ROTATING && iModel.hasSelection()){
                Groupable prim = iModel.getFirstSelectedStar();
                if (prim==null) {return;}

                double cx = prim.getX();
                double cy = prim.getY();
                double mx = event.getX();
                double my = event.getY();

                double angleNow = Math.toDegrees(Math.atan2(my - cy,mx -cx));
                double delta = angleNow - iModel.startMouseAngle;

                for (Groupable g :iModel.selectedStars) {
                    g.setAngle(iModel.originalAngle + delta);
                }

                model.notifySubs();
            }

        });
    }

    private void handleKeyPressed(){
        view.getCanvas().setOnKeyPressed(e ->{
            switch (e.getCode()){
                case DELETE, BACK_SPACE:
                    if(!iModel.hasSelection()){
                        return;
                    }
                    DeleteCommand cmd =  new DeleteCommand(model, new ArrayList<>(iModel.selectedStars));
                    iModel.executeCommand(cmd);
                    iModel.clearSelection();
                    break;
                case G:
                    if(iModel.selectedStars.size() < 2){return;}
                    GroupCommand gcmd = new GroupCommand(model, new ArrayList<>(iModel.selectedStars));
                    iModel.executeCommand(gcmd);
                    iModel.setSelection(model.getItems().getLast());

                    break;
                case U:
                    Groupable first = iModel.getFirstSelectedStar();
                    if(first instanceof StarGroup sg){
                        UngroupCommand ucmd = new UngroupCommand(model,sg);
                        iModel.executeCommand(ucmd);
                        iModel.clearSelection();
                    }
                    break;
                case Z:
                    System.out.println("undoing");
                    iModel.undo();
                    break;
                case R:
                    System.out.println("redoing");
                    iModel.redo();
                    break;
            }

        });
    }

    private void handleMouseReleased(){
        view.getCanvas().setOnMouseReleased(e->{
            if(iModel.mode == InteractionModel.Mode.CREATING&& iModel.previewStar != null){
                CreateCommand cmd = new CreateCommand(model, iModel.previewStar.getX(),iModel.previewStar.getY(),iModel.previewStar.getWidth(), iModel.previewStar.getHeight());
                iModel.previewStar = null;
                iModel.executeCommand(cmd);

            }

            if(iModel.mode == InteractionModel.Mode.MOVING&&iModel.hasSelection()){
                if(totalDx != 0 ||  totalDy != 0){
                    for(Groupable g: iModel.selectedStars){
                        g.move(-totalDx,-totalDy);
                    }
                    MoveCommand cmd = new MoveCommand(model, new ArrayList<>(iModel.selectedStars), totalDx, totalDy);
                    iModel.executeCommand(cmd);
                    //reset accums
                    totalDx=0; totalDy=0;
                }

            }

            if(iModel.mode == InteractionModel.Mode.RESIZING&&iModel.hasSelection()){
                Groupable prim = iModel.getFirstSelectedStar();

                double finalw = prim.getWidth();
                double finalh = prim.getHeight();
                if(iModel.originalWidth != finalw || iModel.originalHeight != finalh){
                    ResizeCommand cmd = new ResizeCommand(model, new ArrayList<>(iModel.selectedStars),
                            iModel.originalWidth, iModel.originalHeight, finalw, finalh);
                    iModel.executeCommand(cmd);
                }
            }

            if(iModel.mode == InteractionModel.Mode.ROTATING&&iModel.hasSelection()){
                Groupable prim =  iModel.getFirstSelectedStar();
                double finalAngle = prim.getAngle();

                if(iModel.originalAngle != finalAngle){
                    RotateCommand cmd = new RotateCommand(
                            model, new ArrayList<>(iModel.selectedStars), iModel.originalAngle, finalAngle
                    );
                    iModel.executeCommand(cmd);
                }
            }

            iModel.mode = InteractionModel.Mode.IDLE;
        });
    }


    private void handleMousePressed(){
        view.getCanvas().setOnMousePressed(mouseEvent -> {
            totalDx=0;
            totalDy=0;
            iModel.pressX = mouseEvent.getX();
            iModel.pressY = mouseEvent.getY();

            view.getCanvas().requestFocus();

            //clear
            if(mouseEvent.getButton() == MouseButton.SECONDARY){
                iModel.clearSelection();
                iModel.mode = InteractionModel.Mode.IDLE;
                model.notifySubs();
                return;
            }

            Groupable hit = top(mouseEvent.getX(),mouseEvent.getY());
            boolean ctrl = mouseEvent.isControlDown();

            if(iModel.hasSelection()){
                Groupable primary = iModel.getFirstSelectedStar();

                if(primary != null){
                    if(isOnResizeHandle(mouseEvent.getX(),mouseEvent.getY(),primary)){
                        iModel.mode = InteractionModel.Mode.RESIZING;
                        iModel.originalWidth = primary.getWidth();
                        iModel.originalHeight = primary.getHeight();
                        model.notifySubs();
                        return;

                    }
                    if(isOnRotateHandle(mouseEvent.getX(),mouseEvent.getY(),primary)){
                        Groupable primarya = iModel.getFirstSelectedStar();
                        double cx =primarya.getX();
                        double cy= primarya.getY();

                        iModel.originalAngle = primarya.getAngle();
                        iModel.startMouseAngle =Math.toDegrees(Math.atan2(mouseEvent.getY() - cy, mouseEvent.getX() - cx)
                        );
                        iModel.mode = InteractionModel.Mode.ROTATING;

                        model.notifySubs();

                        return;
                    }
                }
            }

            if(hit!=null){
                //boolean isprim =
                if(ctrl){
                    if(iModel.isSelected(hit)){
                        iModel.removeSelectedStar(hit);
                    }else{
                        iModel.addSelectedStar(hit);
                    }
                }else{
                    iModel.setSelection(hit);
                }

                iModel.startMoveX = iModel.pressX;
                iModel.startMoveY = iModel.pressY;

                iModel.mode = InteractionModel.Mode.MOVING;
                iModel.previewStar = null;
            }else{
                if(!ctrl){
                    iModel.clearSelection();
                }
                iModel.mode = InteractionModel.Mode.CREATING;
            }
            model.notifySubs();
        });
    }

    private Groupable top(double x, double y){
        Color c = view.getOffscreenReader().getColor((int)x, (int)y);
        int id = (int) (c.getRed() *255);
        if(id == 0){
            return null;
        }
        return model.getIDitem(id);
    }

    private boolean isOnResizeHandle(double mouseX,double mouseY,Groupable g){
        if(g instanceof  StarGroup){
            double right = g.getRight();
            double left = g.getLeft();
            double bottom = g.getBottom();
            return (Math.abs(mouseX-right)<=7 && Math.abs(mouseY-bottom)<=7);
        }
        double hxLocal = g.getWidth()/2.0;
        double hyLocal = g.getHeight()/2;
        double theta= Math.toRadians(g.getAngle());
        double hxWorld = g.getX() + Math.cos(theta)*hxLocal - Math.sin(theta)*hyLocal;
        double hyWorld = g.getY() + Math.sin(theta)*hxLocal + Math.cos(theta)*hyLocal;

        double dx= mouseX- hxWorld;
        double dy = mouseY - hyWorld;
        return dx*dx +dy *dy <= 49;

    }
    private boolean isOnRotateHandle(double mouseX,double mouseY,Groupable g){
        if(g instanceof  StarGroup){
            double cx = (g.getLeft()+g.getRight())/2;
            double cy= g.getTop() -25;
            return (Math.abs(mouseX-cx)<=7 && Math.abs(mouseY-cy)<=7);
        }


        double hxLocal = 0;
        double hyLocal = -g.getHeight()/2.0 -20;

        double theta= Math.toRadians(g.getAngle());
        double hxWorld = g.getX() + Math.cos(theta)*hxLocal - Math.sin(theta)*hyLocal;
        double hyWorld = g.getY() + Math.sin(theta)*hxLocal + Math.cos(theta)*hyLocal;
        double dx= mouseX- hxWorld;
        double dy = mouseY - hyWorld;
        return dx*dx +dy *dy <= 100;
    }

    }


