package model;

import java.util.ArrayList;

public class Point {
    private final int ARRAY_SIZE=128;
    private double x, y;
    private ArrayList<Integer> features;
    private Point nearest;
    private int indexOfNearest;

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
        ArrayList<Integer> distance = new ArrayList<>();
        for(int i=0; i<points.size(); i++){
            distance.add(calculateDistance(points.get(i)));
            //System.out.println("Distance vector: "+distance.get(i));
        }

        int currentMin=Integer.MAX_VALUE;
        for (int i=0; i< distance.size(); i++){
            if(distance.get(i)<currentMin){
                indexOfNearest=i;
                currentMin=distance.get(i);
            }
        }

        target.nearest= points.get(indexOfNearest);

        System.out.println("Derived nearest neighbour of "+this.toString()+" : "+target.nearest.toString()+". Distance is "+distance.get(indexOfNearest));
    }

    private int calculateDistance(Point point) {
        int sum = 0;
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

    public String toString(){
        return "Point x: "+x+", y: "+y;
    }
}
