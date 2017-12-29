package model;

import java.util.ArrayList;

public class Model {
    private ArrayList<Point> points;
    private int currentSize;

    public Model(String size){
        points = new ArrayList<Point>();
        currentSize=0;
    }

    public void addPoint(Point point){
        points.add(point);
    }

    public Point getPoint(int i){
        return points.get(i);
    }

    public ArrayList<Point> getPoints() {
        return points;
    }
}
