package parser;

import model.Model;
import model.Point;

import java.io.*;

public final class Parser {
    private Parser(){

    }

    public static Model parse(File input){
        Model model = null;

        try {
            BufferedReader reader = new BufferedReader(new FileReader(input));
            //read feature number
            String line = reader.readLine();
            //read points number
            line = reader.readLine();
            model = new Model(line);
            while((line=reader.readLine())!=null){
                //line = reader.readLine();
                model.addPoint(new Point(line.split(" ")));
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        catch (NullPointerException e){
            System.out.println("Exception caught!");
        }
        return model;
    }
}
