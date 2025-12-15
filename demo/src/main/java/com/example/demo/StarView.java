package com.example.demo;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import java.io.Serializable;

public class StarView implements iModelListener {
    private static int WIDTH = 1500;
    private static int HEIGHT = 900;


    private Canvas canvas;
    private GraphicsContext gc;
    private StarModel model;
    private InteractionModel iModel;

    // offscreen nitmap .. hidden canvas
    private Canvas offscreen;
    private GraphicsContext ogc;

    public void setModel(StarModel model) {
        this.model = model;
    }
    public void setInteractionModel(InteractionModel interactionModel) {
        this.iModel = interactionModel;

    }

    public StarView() {
        canvas = new Canvas(WIDTH, HEIGHT);
        gc= canvas.getGraphicsContext2D();
        offscreen = new Canvas(WIDTH, HEIGHT);
        ogc= offscreen.getGraphicsContext2D();
        canvas.setFocusTraversable(true); // for key evevnts
        clear();
    }

    public Canvas getCanvas() {
        return canvas;
    }
    public void clear() {
        gc.setFill(Color.LIGHTGRAY);
        gc.fillRect(0, 0, WIDTH, HEIGHT);

    }

    public javafx.scene.image.PixelReader getOffscreenReader() {
        return offscreen.snapshot(null, null).getPixelReader();
    }

    public void modelChanged(){
        //System.out.println("MODEL CHANGED !!!,, REDRAWING");
        redraw();
    }

    private void redraw() {
        clear();
        int nextID=1;

        // main view
        for (Groupable s : model.getItems()){
            int itemID = nextID;

            s.setID(itemID);
            nextID++;

            drawGroupable(s, iModel.isSelected(s), false, null);

            if(s instanceof StarGroup){
                nextID += countchildren((StarGroup) s);
            }
           // drawStar(s);
        }

        // preview star Dragging;;
        if(model!= null && iModel.getPreviewStar() !=null){
            drawStar(iModel.getPreviewStar(), false,false,null);
        }

        // offscreen view
        ogc.clearRect(0, 0, WIDTH, HEIGHT);
        nextID=1;
        for (Groupable s : model.getItems()){
            int itemID = nextID;
            nextID++;
            Color idColor = Color.rgb(itemID,0,0);
            drawGroupable(s,false,true,idColor);

            if(s instanceof StarGroup){
                nextID += countchildren((StarGroup) s);
            }
        }
        //clear();
    }

    private void drawGroupable(Groupable item, boolean selected, boolean pickMode, Color idColor) {
        if(item instanceof StarGroup){
            drawGroup((StarGroup) item, selected, pickMode, idColor);
        }else if(item instanceof  Star){
            drawStar((Star) item, selected, pickMode, idColor);
        }
    }

    private void drawStar(Star s, boolean selected, boolean pickMode, Color idColor) {
        GraphicsContext g = pickMode ? ogc : gc;

        g.save();


        g.translate(s.x, s.y);
        g.rotate(s.angle);
        //oval

        if (pickMode) {
            g.setFill(idColor);
            g.fillOval(-s.width/2 , -s.height/2 , s.width, s.height);
            g.restore();
            return;
        }

        g.setStroke(selected? Color.RED : Color.GRAY);

        //gc.setStroke(Color.BLACK);
        g.setLineDashes(10);
        g.strokeOval(-s.width /2, -s.height /2, s.width, s.height);

        //draw star
        g.setFill(Color.LIGHTGREEN);
        g.setStroke(Color.BLACK);
        g.setLineDashes(null);

        double[] xs = new double[8]; // why 8
        double[] ys = new double[8];

        double outerR= s.width /2;
        double innerR = s.width /4;

        for(int i =0; i<8; i++){
            double angle= Math.toRadians(i *45);
            double r= (i%2==0) ? outerR : innerR;
            xs[i] = r * Math.cos(angle);
            ys[i] = r * Math.sin(angle);
        }

        // if selecetd , resize part
        if(selected){
           // g.setFill(idColor);
            g.setFill(Color.RED);
            g.setStroke(Color.RED);
            g.setLineDashes(null);

            double hx = s.width/2;
            double hy = s.height/2;

            g.fillRect(hx- 5, hy - 5 ,10, 10);
            double rotY = -s.height /2 -25;
            g.fillOval(-5, rotY - 5, 10, 10);
        }

        g.fillPolygon(xs, ys, 8);
        g.strokePolygon(xs, ys, 8);
        g.restore();
    }

//    private int countchildren(Groupable group){
//        if(group instanceof Star){ return 1;}
//        int count =1;
//        for(Groupable child: ((StarGroup)group).getChildren()){
//            count += countchildren(child);
//        }
//        return count;
//    }
    private int countchildren(StarGroup group){
        int count = 0;
        for(Groupable child:group.getChildren()){
            count++;
            if(child instanceof StarGroup){
                count += countchildren((StarGroup) child);
            }
        }
        return count;
    }

    private void drawGroup(StarGroup group, boolean selected, boolean pickMode, Color idColor) {
        GraphicsContext g = pickMode ? ogc : gc;

        if(pickMode){
            for (Groupable child : group.getChildren()) {
                drawGroupable(child, false, true, idColor);
            }return;
        }

        //normal part
        double left = group.getLeft();
        double right = group.getRight();
        double top = group.getTop();
        double bottom = group.getBottom();
        double h = bottom - top;
        double w =  right - left;
       // g.save();

        g.setLineDashes(10);
        g.setStroke(selected? Color.RED : Color.BLACK);
        g.strokeRect(left, top, w,h);

        for(Groupable child : group.getChildren()){
            drawGroupable(child, iModel.isSelected(child), false, null);
        }

        //if(selected && iModel.selectedStars.size() == 1){
        if(selected && group== iModel.getFirstSelectedStar()){
            g.setFill(Color.RED);
            g.setLineDashes(null);
            g.fillRect(right -5, bottom -5, 10, 10);

            double cx = (right + left) /2;
            double ry = top-25;
            g.fillOval(cx-5,ry-5,10,10);
        }
    }


}
