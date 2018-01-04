package resolvers;

import Jama.Matrix;
import gui.GUI;
import model.Point;

import java.util.ArrayList;

public class RANSACPerspectiveResolver extends RANSACLinearResolver{
    public RANSACPerspectiveResolver(GUI gui, ArrayList<Point> input) {
        super(gui, input);
        MAX_ERROR=0.5;
    }

    @Override
    protected ArrayList<Point> findPointsThatMeetRequirements(Matrix value) {
        double[][] array = value.getArray();
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

            double xPrim = (a*p.getX()+b*p.getY()+c)/(g*p.getX()+h*p.getY()+1);
            double yPrim = (d*p.getX()+e*p.getY()+f)/(g*p.getX()+h*p.getY()+1);

            double total = Math.abs(xPrim-neighbour.getX())+Math.abs(yPrim-neighbour.getY());
            total/=1000;
            if(total<=MAX_ERROR)
                product.add(p);
        }

        gui.refresh();

        return product;
    }

    @Override
    protected Matrix calculateMatrix(ArrayList<Point> sample) {
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
                {x1.getX(), x1.getY(), 1, 0, 0, 0, -u1.getX()*x1.getX(), -u1.getX()*x1.getY()},
                {x2.getX(), x2.getY(), 1, 0, 0, 0, -u2.getX()*x2.getX(), -u2.getX()*x2.getY()},
                {x3.getX(), x3.getY(), 1, 0, 0, 0, -u3.getX()*x3.getX(), -u3.getX()*x3.getY()},
                {x4.getX(), x4.getY(), 1, 0, 0, 0, -u4.getX()*x4.getX(), -u4.getX()*x4.getY()},
                {0, 0, 0, x1.getX(), x1.getY(), 1, -u1.getY()*x1.getX(), -u1.getY()*x1.getY()},
                {0, 0, 0, x2.getX(), x2.getY(), 1, -u2.getY()*x2.getX(), -u2.getY()*x2.getY()},
                {0, 0, 0, x3.getX(), x3.getY(), 1, -u3.getY()*x3.getX(), -u3.getY()*x3.getY()},
                {0, 0, 0, x4.getX(), x4.getY(), 1, -u4.getY()*x4.getX(), -u4.getY()*x4.getY()},
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

    @Override
    protected int checkError(Matrix product) {
        int error=0;

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

            double xPrim = (a*p.getX()+b*p.getY()+c)/(g*p.getX()+h*p.getY()+1);
            double yPrim = (d*p.getX()+e*p.getY()+f)/(g*p.getX()+h*p.getY()+1);

            double total = Math.abs(xPrim-neighbour.getX())+Math.abs(yPrim-neighbour.getY());
            total/=1000;
            System.out.println("Error for point "+p+" and neighbour "+neighbour+" is "+total);
            if(total>MAX_ERROR)
                error++;
        }
        return error;
    }
}
