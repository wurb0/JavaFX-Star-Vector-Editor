package com.example.demo;

public class Star implements Groupable{
    public double x;
    public double y;
    public double height;
    public double width;
    public double angle;
    public int ID;

    public Star(double x, double y, double height, double width, double angle) {
        this.x = x;
        this.y = y;
        this.height = height;
        this.width = width;
        this.angle = angle;

    }

    public double  getX() {
        return x;
    }
    public void setX(double x) {
        this.x = x;
    }
    public double  getY() {
        return y;
    }
    public void setY(double y) {
        this.y = y;
    }
    public double getHeight() {
        return height;
    }
    public void setHeight(double height) {
        this.height = height;
    }
    public double getWidth() {
        return width;

    }
    public void setWidth(double width) {
        this.width = width;
    }
    public double getAngle() {
        return angle;
    }
    public void setAngle(double angle) {
        this.angle = angle;
    }
    public void move(double x, double y) {
        this.x += x;
        this.y += y;
    }

    public double getLeft() {
        return x-width / 2;
    }
    public double getTop() {
        return y - height /2;
    }
    public double getRight() {
        return x+width / 2;
    }
    public double getBottom() {
        return y+height/2;
    }

    public void setID(int ID) {
        this.ID = ID;
    }
    public int getID() {
        return ID;
    }

}
