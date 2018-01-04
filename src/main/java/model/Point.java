package model;

import java.util.ArrayList;

public class Point implements Cloneable{
    private final int ARRAY_SIZE=128;
    private double x, y;
    private ArrayList<Integer> features;
    private Point nearest;
    private int indexOfNearest;

    private Point(){
        features = new ArrayList<>();
    }

    public Point(String[] input){
        features = new ArrayList<Integer>();
        x = Double.parseDouble(input[0]);
        y = Double.parseDouble(input[1]);

        for (int i=5; i<128; i++){
            features.add(Integer.parseInt(input[i]));
        }
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public ArrayList<Integer> getFeatures() {
        return features;
    }

    public void deriveDistanceFunction(Point target, ArrayList<Point> points){
        ArrayList<Double> distance = new ArrayList<>();
        for(int i=0; i<points.size(); i++){
            distance.add(calculateDistance(points.get(i)));
        }

        double currentMin=Double.MAX_VALUE;
        for (int i=0; i< distance.size(); i++){
            if(distance.get(i)<currentMin){
                indexOfNearest=i;
                currentMin=distance.get(i);
            }
        }

        target.nearest= points.get(indexOfNearest);

        System.out.println("Derived nearest neighbour of "+this.toString()+" : "+target.nearest.toString()+". Distance is "+distance.get(indexOfNearest));
    }

    private double calculateDistance(Point point) {
        double sum = 0;
        for (int i=0; i<features.size(); i++){
            sum+=Math.pow((features.get(i)-point.features.get(i)),2);
        }
        return sum;
    }

    public Point getNearestNeighbour() {
        return nearest;
    }

    public boolean hasNearestNeighbour(){
        return nearest!=null;
    }

    @Override
    public String toString(){
        return "Point x: "+x+", y: "+y;
    }

    @Override
    public Point clone(){
        Point clone = new Point();
        clone.x=x;
        clone.y=y;
        clone.nearest=nearest.clone();
        clone.indexOfNearest=indexOfNearest;
        clone.features.addAll(features);
        return clone;
    }

    @Override
    public boolean equals(Object o) {
        Point p = (Point) o;
        return x==p.x&&y==p.y&&indexOfNearest==p.indexOfNearest;
    }
}
