package com.example.demo;

public interface Groupable {
    double getX();
    double getY();
    double getWidth();
    double getHeight();
    double getAngle();

    void setX(double x);
    void setY(double y);
    void setWidth(double width);
    void setHeight(double height);
    void setAngle(double angle);
    void move(double dx, double dy);


    double getLeft();
    double getTop();
    double getRight();
    double getBottom();
    int getID();
    void setID(int id);

}
