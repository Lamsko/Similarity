package model;

import java.util.ArrayList;

public class Point {
    private final int ARRAY_SIZE=128;
    private double x, y;
    private ArrayList<Integer> features;
    private Point nearest;
    public boolean hasNearestNeighbour;

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

    public void deriveDistanceFunction(ArrayList<Point> points){
        ArrayList<Integer> distance = new ArrayList<>();
        for(int i=0; i<points.size(); i++){
            distance.add(calculateDistance(points.get(i)));
            //System.out.println("Distance vector: "+distance.get(i));
        }

        int minIndex=-1, currentMin=Integer.MAX_VALUE;
        for (int i=0; i< distance.size(); i++){
            if(distance.get(i)<currentMin){
                minIndex=i;
                currentMin=distance.get(i);
            }
        }

        nearest= points.get(minIndex);

        System.out.println("Derived nearest neighbour of "+this.toString()+" : "+nearest.toString()+". Distance is "+distance.get(minIndex));
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
