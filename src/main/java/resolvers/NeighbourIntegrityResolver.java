package resolvers;

import gui.GUI;
import javafx.util.Pair;
import model.Point;

import java.util.ArrayList;
import java.util.List;

public class NeighbourIntegrityResolver {
    private List<Point> neighbouringPoints;
    private List<List<Pair<Integer,Double>>> distancesOfNeighbours;
    private List<Point> product;
    private GUI gui;

    public NeighbourIntegrityResolver(GUI gui, ArrayList<Point> points){
        this.neighbouringPoints=points;
        distancesOfNeighbours = new ArrayList<>();
        product = new ArrayList<>();
        this.gui=gui;
    }

    public void resolve() {
        //dla każdego punktu
        for (Point p: neighbouringPoints){
            List<Pair<Integer, Double>> distances = new ArrayList<>();
            //policz odległość od każdego z pozostałych punktów
            for (int i=0; i<neighbouringPoints.size(); i++){
                distances.add(new Pair<Integer, Double>(i,distanceBetween(p,neighbouringPoints.get(i))));
            }
            //posortuj według odległości
            distances.sort((t1, t2) -> {
                if(t1.getValue()<t2.getValue()) return -1;
                else if (t1.getValue().equals(t2.getValue())) return 0;
                else return 1;
            });

            //usun autoreferencję i wybierz 5 najbliższych sąsiadów.
            distances = distances.subList(1,6);

            /*//System.out.println("Distances from point "+p);
            for (Pair<Integer,Double> d: distances){
                System.out.println("Point no "+d.getKey()+" dist: "+d.getValue());
            }*/
            distancesOfNeighbours.add(distances);
        }

        //wyznacz indeks najbliższego sąsiada
        for (int i=0; i<neighbouringPoints.size(); i++){
            int currNeigh = -1;
            for (int j=0; j<neighbouringPoints.size(); j++){
                if(neighbouringPoints.get(i).getNearestNeighbour().equals(neighbouringPoints.get(j))) {
                    currNeigh = j;
                    break;
                }
            }
            //System.out.println(neighbouringPoints.get(i)+" 's neighbour is "+neighbouringPoints.get(currNeigh));
            //po wyznaczeniu, sprawdź ile procent sąsiedztwa jest ze sobą zgodne
            Point current = neighbouringPoints.get(i);
            Point neighbor = neighbouringPoints.get(currNeigh);

            int contains=0;
            ArrayList<Integer>
                    currentList = pairToInteger(distancesOfNeighbours.get(i)),
                    neighbourList = pairToInteger(distancesOfNeighbours.get(currNeigh));

            for (Integer curr:currentList) {
                if(neighbourList.contains(curr)){
                    System.out.println("Contains!");
                    contains++;
                }
            }

            if(contains>=1){
                System.out.println("Match found!");
                product.add(neighbouringPoints.get(i));
            }
        }

        gui.refresh();
    }

    private ArrayList<Integer> pairToInteger(List<Pair<Integer, Double>> input){
        ArrayList<Integer> product = new ArrayList<>();
        input.stream().forEach(pair -> product.add(pair.getKey()));
        return product;
    }

    private double distanceBetween(Point a, Point b){
        return Math.pow(b.getX()-a.getX(),2)+Math.pow(b.getY()-a.getY(),2);
    }

    public int getNeighboringPointCount() {
        return product.size();
    }

    public List<Point> getProduct(){
        return product;
    }
}
