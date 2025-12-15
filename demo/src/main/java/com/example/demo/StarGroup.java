package com.example.demo;

import java.util.ArrayList;

public class StarGroup  implements Groupable {
    private double x;
    private double y; // centre of group
    private double angle;
    public int ID; // group id


    private ArrayList<Groupable> children; // a group can have more groups wihitn

    public StarGroup(){
        children = new ArrayList<>();
        x =0.0;
        y =0.0;
        angle = 0;
    }

    public boolean contains(Groupable g){
        for (Groupable child : children){
            if(child.equals(g)){
                return true;
            }
            if(child instanceof StarGroup){
                if(((StarGroup)child).contains(g)){
                    return true;
                }
            }
        }
        return false;
    }


    public void addChild(Groupable child){
        children.add(child);
        calculateCentre();
    }
    private void calculateCentre(){
        if(children.size()==0){
            return;
        }
        double sumx = 0;
        double sumy = 0;
        for(Groupable child:children){
            sumx += child.getX();
            sumy += child.getY();
        }
        x = sumx/children.size();
        y = sumy/children.size();
    }


    public ArrayList<Groupable> getChildren(){
        return children;
    }

    public double getX(){
        return x;
    }
    public double getY(){
        return y;
    }
    public double getWidth(){
        return getRight() - getLeft();
    }
    public double getHeight(){
        return getBottom() - getTop();
    }
    public double getAngle(){
        return angle;
    }
    public void setAngle(double angle){
        //this.angle = angle;
        double newAngle = angle - this.angle;
        this.angle = newAngle;

        for(Groupable child:children){
            double dx = child.getX() - getX();
            double dy = child.getY() - getY();
            double rad = Math.toRadians(newAngle);
            double newDX = dx * Math.cos(rad) - dy * Math.sin(rad);
            double newDY = dx * Math.sin(rad) + dy * Math.cos(rad);
            child.setX(x+newDX);
            child.setY(y+newDY);
            child.setAngle(child.getAngle() + newAngle);
        }
    }

    public void setX(double x){
        this.x = x;
    }
    public void setY(double y){
        this.y = y;
    }
    public void setWidth(double width){
        double currWidth = getRight() - getLeft();
        if(currWidth == 0){
            return;
        }
        // change childrens width too
        double scale = width / currWidth;
        for(Groupable child:children){
            double dx= child.getX() -x;
            child.setX(x+dx*scale);
            child.setWidth(child.getWidth()*scale);
        }
    }


    public void setHeight(double height){
        double currHeight = getBottom() - getTop();
        if(currHeight == 0){
            return;
        }
        double scale = height / currHeight;
        for(Groupable child:children){
            double dy= child.getY() -y;
            child.setY(y+dy*scale);
            child.setHeight(child.getHeight()*scale);

        }


    }

    public void move(double dx, double dy){
        x+= dx;
        y+=dy;
        for (Groupable child:children){
            child.move(dx, dy);
        }
    }

    public double getLeft(){
        if(children.size()==0){return x;}
        double min = Double.MAX_VALUE;
        for(Groupable child:children){
            min = Math.min(min,child.getLeft());
        }
        return min;
    }

    public double getRight(){
        if(children.size()==0){return x;}
        double max = Double.MIN_VALUE;
        for(Groupable child:children){
            max = Math.max(max,child.getRight());
        }
        return max;
    }
    public double getTop(){
        if(children.size()==0){return y;}

        double min = Double.MAX_VALUE;
        for(Groupable child:children){
            min = Math.min(min,child.getTop());
        }
        return min;
    }
    public double getBottom(){
        if(children.size()==0){return y;}
        double max = Double.MIN_VALUE;
        for(Groupable child:children){
            max = Math.max(max,child.getBottom());
        }
        return max;
    }

    public int getID(){
        return ID;
    }
    public void setID(int ID){
        this.ID = ID;
    }




}
