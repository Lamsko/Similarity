package resolvers;

import gui.GUI;
import model.Model;
import model.Point;

import java.util.ArrayList;

public class NeighbouringPointsResolver {
    private Model model1, model2;
    private GUI listener;
    private ArrayList<Point> pointsWithNeighbours;

    public NeighbouringPointsResolver(Model model1, Model model2, GUI listener){
        this.model1 = model1;
        this.model2 = model2;
        this.listener=listener;
        pointsWithNeighbours = new ArrayList<>();
    }

    public void resolve(){
        ArrayList<Point> aPoints = model1.getPoints(), bPoints=model2.getPoints();

        //find nearest neighbours for all of the aPoints
        for (int i=0; i<aPoints.size(); i++){
            aPoints.get(i).deriveDistanceFunction(aPoints.get(i), bPoints);
        }
        for (int i=0; i<bPoints.size(); i++){
            bPoints.get(i).deriveDistanceFunction(bPoints.get(i), aPoints);
        }

        model1.setPoints(aPoints);
        model2.setPoints(bPoints);

        for (int i=0; i<aPoints.size(); i++){
            if(aPoints.get(i).hasNearestNeighbour()){
                pointsWithNeighbours.add(aPoints.get(i));
            }
        }

        System.out.println("Points with nearest neighbours count: "+pointsWithNeighbours.size());

        listener.setNearestNeighbourData(this);
    }

    public int getNeighboringPointCount(){
        return pointsWithNeighbours.size()>0?pointsWithNeighbours.size():-1;
    }

    public Model getModel1() {
        return model1;
    }

    public Model getModel2() {
        return model2;
    }
}
