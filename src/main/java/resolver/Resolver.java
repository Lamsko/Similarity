package resolver;

import gui.GUI;
import model.Model;
import model.Point;

import java.util.ArrayList;

public class Resolver {
    private Model model1, model2;
    private GUI listener;

    public Resolver(Model model1, Model model2, GUI listener){
        this.model1 = model1;
        this.model2 = model2;
        this.listener=listener;
    }

    public void resolve(){
        ArrayList<Point> aPoints = model1.getPoints(), bPoints=model2.getPoints();

        //find nearest neighbours for all of the aPoints
        for (int i=0; i<aPoints.size(); i++){
            aPoints.get(i).deriveDistanceFunction(bPoints);
        }
        for (int i=0; i<bPoints.size(); i++){
            bPoints.get(i).deriveDistanceFunction(aPoints);
        }

        listener.setNearestNeighbourData(this);
    }
}
