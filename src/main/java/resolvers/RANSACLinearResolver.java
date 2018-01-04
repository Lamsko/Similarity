package resolvers;

import Jama.Matrix;
import gui.GUI;
import model.Point;

import java.util.ArrayList;
import java.util.Random;

public class RANSACLinearResolver {
    private GUI gui;
    private ArrayList<Point> points;

    public RANSACLinearResolver(GUI gui, ArrayList<Point> input){
        this.gui=gui;
        this.points=input;
    }

    public ArrayList<Point> resolve(){
        ArrayList<Point> sample = generateSample();
        Matrix product = calculateMatrix(sample);
        product.print(10,5);
        double error = checkError(product);
        return sample;
    }

    private ArrayList<Point> generateSample(){
        Random random = new Random();

        Point a1 = points.get(random.nextInt(points.size()));
        Point a2 = a1.getNearestNeighbour();

        double r = Math.pow(800*0.01,2);
        double R = Math.pow(800*0.3,2);

        Point
                b1 = points.get(random.nextInt(points.size())),
                b2 = b1.getNearestNeighbour(),
                c1 = points.get(random.nextInt(points.size())),
                c2 = c1.getNearestNeighbour(),
                d1 = points.get(random.nextInt(points.size())),
                d2 = d1.getNearestNeighbour();

        while(
                r>=Math.pow(a1.getX()-b1.getX(),2)+Math.pow(a1.getY()-b1.getY(),2) &&
                        Math.pow(a1.getX()-b1.getX(),2)+Math.pow(a1.getY()-b1.getY(),2)>R && 
                        r>=Math.pow(a2.getX()-b2.getX(),2)+Math.pow(a2.getY()-b2.getY(),2) &&
                        Math.pow(a2.getX()-b2.getX(),2)+Math.pow(a2.getY()-b2.getY(),2)>R
                )
        {
            System.out.println("Rerolling for b1. "+(a1.getX()-b1.getX()));
            b1 = points.get(random.nextInt(points.size()));
            b2 = b1.getNearestNeighbour();
        }

        while(
                r>=Math.pow(a1.getX()-c1.getX(),2)+Math.pow(a1.getY()-c1.getY(),2) &&
                        Math.pow(a1.getX()-c1.getX(),2)+Math.pow(a1.getY()-c1.getY(),2)>R &&
                        r>=Math.pow(a2.getX()-c2.getX(),2)+Math.pow(a2.getY()-c2.getY(),2) &&
                        Math.pow(a2.getX()-c2.getX(),2)+Math.pow(a2.getY()-c2.getY(),2)>R
                &&
                        r>=Math.pow(b1.getX()-c1.getX(),2)+Math.pow(b1.getY()-c1.getY(),2) &&
                        Math.pow(b1.getX()-c1.getX(),2)+Math.pow(b1.getY()-c1.getY(),2)>R &&
                        r>=Math.pow(b2.getX()-c2.getX(),2)+Math.pow(b2.getY()-c2.getY(),2) &&
                        Math.pow(b2.getX()-c2.getX(),2)+Math.pow(b2.getY()-c2.getY(),2)>R
                )
        {
            System.out.println("Rerolling for c1. "+(a1.getX()-b1.getX()));
            c1=points.get(random.nextInt(points.size()));
            c2=c1.getNearestNeighbour();
        }

        while(
                r>=Math.pow(a1.getX()-d1.getX(),2)+Math.pow(a1.getY()-c1.getY(),2) &&
                        Math.pow(a1.getX()-c1.getX(),2)+Math.pow(a1.getY()-c1.getY(),2)>R &&
                        r>=Math.pow(a2.getX()-d2.getX(),2)+Math.pow(a2.getY()-d2.getY(),2) &&
                        Math.pow(a2.getX()-d2.getX(),2)+Math.pow(a2.getY()-d2.getY(),2)>R
                        &&
                        r>=Math.pow(b1.getX()-d1.getX(),2)+Math.pow(b1.getY()-d1.getY(),2) &&
                        Math.pow(b1.getX()-d1.getX(),2)+Math.pow(b1.getY()-d1.getY(),2)>R &&
                        r>=Math.pow(b2.getX()-d2.getX(),2)+Math.pow(b2.getY()-d2.getY(),2) &&
                        Math.pow(b2.getX()-d2.getX(),2)+Math.pow(b2.getY()-d2.getY(),2)>R
                        &&
                        r>=Math.pow(c1.getX()-d1.getX(),2)+Math.pow(c1.getY()-d1.getY(),2) &&
                        Math.pow(c1.getX()-d1.getX(),2)+Math.pow(c1.getY()-d1.getY(),2)>R &&
                        r>=Math.pow(c2.getX()-d2.getX(),2)+Math.pow(c2.getY()-d2.getY(),2) &&
                        Math.pow(c2.getX()-d2.getX(),2)+Math.pow(c2.getY()-d2.getY(),2)>R
                )
        {
            System.out.println("Rerolling for d1. "+(a1.getX()-b1.getX()));
            c1=points.get(random.nextInt(points.size()));
            c2=c1.getNearestNeighbour();
        }

        ArrayList<Point> product = new ArrayList<>();
        product.add(a1);
        product.add(a2);
        product.add(b1);
        product.add(b2);
        product.add(c1);
        product.add(c2);
        product.add(d1);
        product.add(d2);

        return product;
    }

    protected Matrix calculateMatrix(ArrayList<Point> sample){
        Point
                x1 = sample.get(0),
                u1 = sample.get(1),
                x2 = sample.get(2),
                u2 = sample.get(3),
                x3 = sample.get(4),
                u3 = sample.get(5),
                x4 = sample.get(6),
                u4 = sample.get(7);

        double matrixA[][] = {
                {x1.getX(), x1.getY(), x1.getX()*x1.getY(), 1, 0, 0, 0, 0},
                {x2.getX(), x2.getY(), x2.getX()*x2.getY(), 1, 0, 0, 0, 0},
                {x3.getX(), x3.getY(), x3.getX()*x3.getY(), 1, 0, 0, 0, 0},
                {x4.getX(), x4.getY(), x4.getX()*x4.getY(), 1, 0, 0, 0, 0},
                {0, 0, 0, 0, x1.getX(), x1.getY(), x1.getX()*x1.getY(), 1},
                {0, 0, 0, 0, x2.getX(), x2.getY(), x2.getX()*x2.getY(), 1},
                {0, 0, 0, 0, x3.getX(), x3.getY(), x3.getX()*x3.getY(), 1},
                {0, 0, 0, 0, x4.getX(), x4.getY(), x4.getX()*x4.getY(), 1}
        };

        double matrixB[][] = {
                {u1.getX()},
                {u2.getX()},
                {u3.getX()},
                {u4.getX()},
                {u1.getY()},
                {u2.getY()},
                {u3.getY()},
                {u4.getY()}
        };

        Matrix matA = new Matrix(matrixA);
        matA = matA.transpose();

        Matrix matB = new Matrix(matrixB);

        return matA.solve(matB);
    }

    protected double checkError(Matrix product){
        double error=0;

        double[][] array = product.getArray();

        System.out.println("Got array");
        double
                a = array[0][0],
                b = array[1][0],
                c = array[2][0],
                d = array[3][0],
                e = array[4][0],
                f = array[5][0],
                g = array[6][0],
                h = array[7][0];

        for (Point p: points){
            Point neighbour = p.getNearestNeighbour();

            double xPrim = a*p.getX()+b*p.getY()+c*p.getX()*p.getY()+d;
            double yPrim = e*p.getX()+f*p.getY()+g*p.getX()*p.getY()+h;

            double total = Math.abs(xPrim-neighbour.getX())+Math.abs(yPrim-neighbour.getY());

            System.out.println("Error for point "+p+" and neighbour "+neighbour+" is "+total);
            error+=total;
        }
        return error;
    }
}
