package gui;

import model.Model;
import model.Point;
import net.miginfocom.swing.MigLayout;
import parser.Parser;
import resolver.Resolver;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class GUI extends JFrame implements ActionListener{
    Dimension size = new Dimension(900,800);
    Dimension pictureSize = new Dimension(450,800);
    Dimension containerPanelSize = new Dimension(900,800);
    JPanelImpl picture1Panel;
    Model picture1Model, picture2Model;
    JButton loadPicture1, loadPicture2, resolveButton;
    File
            picture1 = new File("D:\\Kuba\\Google Drive\\Uczelnia\\Semestr 7\\SI i Inżynieria Wiedzy\\Laboratorium\\Zadanie 4\\Similarity\\src\\main\\resources\\kwiatek1.png"),
            picture2 = new File("D:\\Kuba\\Google Drive\\Uczelnia\\Semestr 7\\SI i Inżynieria Wiedzy\\Laboratorium\\Zadanie 4\\Similarity\\src\\main\\resources\\kwiatek2.png"),
            data1 = new File("D:\\Kuba\\Google Drive\\Uczelnia\\Semestr 7\\SI i Inżynieria Wiedzy\\Laboratorium\\Zadanie 4\\Similarity\\src\\main\\resources\\kwiatek1.png.haraff.sift"),
            data2 = new File("D:\\Kuba\\Google Drive\\Uczelnia\\Semestr 7\\SI i Inżynieria Wiedzy\\Laboratorium\\Zadanie 4\\Similarity\\src\\main\\resources\\kwiatek2.png.haraff.sift"),
            haraffPicture1 = new File("D:\\Kuba\\Google Drive\\Uczelnia\\Semestr 7\\SI i Inżynieria Wiedzy\\Laboratorium\\Zadanie 4\\Similarity\\src\\main\\resources\\kwiatek1.png.haraff.sift.png"),
            haraffPicture2 = new File("D:\\Kuba\\Google Drive\\Uczelnia\\Semestr 7\\SI i Inżynieria Wiedzy\\Laboratorium\\Zadanie 4\\Similarity\\src\\main\\resources\\kwiatek2.png.haraff.sift.png");
    Resolver resolver;

    public GUI(String title){
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
        setTitle(title);
        setLayout(new MigLayout("wrap 2"));
        setSize(size);
        setUpComponents();
        pack();
    }

    private void setUpComponents() {
        loadPicture1 = new JButton("Wczytaj obrazek 1");
        loadPicture1.addActionListener(this);
        loadPicture2 = new JButton("Wczytaj obrazek 2");
        loadPicture2.addActionListener(this);
        picture1Panel = new JPanelImpl();
        picture1Panel.setMinimumSize(containerPanelSize);
        picture1Panel.setSize(containerPanelSize);
        picture1Panel.setPreferredSize(containerPanelSize);
        resolveButton = new JButton("Rozwiąż");
        resolveButton.addActionListener(this);

        add(loadPicture1);
        add(loadPicture2);
        add(picture1Panel, "span 2, growx, growy");
        add(resolveButton, "span 2, growx");
        pack();
    }

    @Override
    public void actionPerformed(ActionEvent actionEvent) {
        if(actionEvent.getSource()==loadPicture1||actionEvent.getSource()==loadPicture2) {
            System.out.println("Button pressed");
            System.out.println(System.getProperty("user.dir") + "\\src\\main\\resources");
            File directory = new File("D:\\Kuba\\Google Drive\\Uczelnia\\Semestr 7\\SI i Inżynieria Wiedzy\\Laboratorium\\Zadanie 4\\Similarity\\src\\main\\resources");
            FileFilter imageFilter = new FileNameExtensionFilter(
                    "Image files", ImageIO.getReaderFileSuffixes());
            JFileChooser fc = new JFileChooser();
            fc.setFileFilter(imageFilter);
            fc.setCurrentDirectory(directory);
            int returnVal = fc.showOpenDialog(this);

            if (returnVal == JFileChooser.APPROVE_OPTION) {
                if (actionEvent.getSource().equals(loadPicture1)) {
                    picture1 = fc.getSelectedFile();
                    haraffPicture1 = new File(picture1.getAbsoluteFile() + ".haraff.sift.png");
                    System.out.println(picture1.getAbsoluteFile());
                    data1 = new File(picture1.getAbsoluteFile() + ".haraff.sift");
                    System.out.println("Also loaded data " + data1.getName());
                    picture1Panel.setImage1(picture1, haraffPicture1);
                    picture1Model = Parser.parse(data1);
                    picture1Panel.setModel1(picture1Model);
                } else {
                    picture2 = fc.getSelectedFile();
                    haraffPicture2 = new File(picture2.getAbsoluteFile() + ".haraff.sift.png");
                    System.out.println(picture2.getAbsoluteFile());
                    data2 = new File(picture2.getAbsoluteFile() + ".haraff.sift");
                    System.out.println("Also loaded data " + data2.getName());
                    picture1Panel.setImage2(picture2, haraffPicture2);
                    picture2Model = Parser.parse(data2);
                    picture1Panel.setModel2(picture2Model);
                }
            }
            repaint();
        }
        else if (actionEvent.getSource()==resolveButton){
            resolver = new Resolver(picture1Model, picture2Model, this);
            resolver.resolve();
        }
    }

    class JPanelImpl extends JPanel implements MouseListener{
        private BufferedImage imageToDraw1, imageToDraw2;
        private BufferedImage originalImage1, haraffImage1, originalImage2, haraffImage2;
        private Model imageModel1, imageModel2;

        public JPanelImpl(){
            super();
            addMouseListener(this);
        }

        public void setImage1(File original, File haraff){
            try {
                originalImage1 = ImageIO.read(original);
                haraffImage1 = ImageIO.read(haraff);
                imageToDraw1 = originalImage1;
            }
            catch (IOException e){
                System.out.println("Failed during reading the image");
                e.printStackTrace();
            }
        }

        public void setImage2(File original, File haraff){
            try {
                originalImage2 = ImageIO.read(original);
                haraffImage2 = ImageIO.read(haraff);
                imageToDraw2 = originalImage2;
            }
            catch (IOException e){
                System.out.println("Failed during reading the image");
                e.printStackTrace();
            }
        }

        public void setModel1(Model model){
            this.imageModel1 =model;
        }

        public void setModel2(Model model){
            this.imageModel2=model;
        }

        @Override
        protected void paintComponent(Graphics graphics) {
            super.paintComponent(graphics);
            Graphics2D g = (Graphics2D) graphics;
            if(imageToDraw1 !=null){
                g.drawImage(imageToDraw1, 0,0, null);
            }
            if(imageToDraw2 !=null){
                g.drawImage(imageToDraw2, 450,0, null);
            }
            if(imageModel1 !=null){
                g.setColor(Color.RED);
                for(Point p: imageModel1.getPoints()){
                    g.fillOval((int)p.getX(), (int)p.getY(), 4, 4);
                }

            }
            if(imageModel2!=null){
                for(Point p: imageModel2.getPoints()){
                    g.fillOval((int)p.getX()+450, (int)p.getY(), 4, 4);
                }
            }

            g.setColor(Color.GREEN);
            if(imageModel1!=null&&imageModel2!=null) {
                for (Point p : imageModel1.getPoints()) {
                    if (p.hasNearestNeighbour()) {
                        Point temp = p.getNearestNeighbour();
                        if(temp.getNearestNeighbour().equals(p)) {
                            g.drawLine((int) p.getX(), (int) p.getY(), (int) (450 + temp.getX()), (int) temp.getY());
                        }
                    }
                }
            }
        }

        @Override
        public void mouseClicked(MouseEvent mouseEvent) {

        }
        @Override
        public void mousePressed(MouseEvent mouseEvent) {

        }
        @Override
        public void mouseReleased(MouseEvent mouseEvent) {

        }

        @Override
        public void mouseEntered(MouseEvent mouseEvent) {
            imageToDraw1 = haraffImage1;
            imageToDraw2 = haraffImage2;
            repaint();
        }

        @Override
        public void mouseExited(MouseEvent mouseEvent) {
            imageToDraw1 = originalImage1;
            imageToDraw2 = originalImage2;
            repaint();
        }
    }

    public void setNearestNeighbourData(Resolver r){
        picture1Model=r.getModel1();
        picture1Panel.setModel1(picture1Model);
        picture2Model=r.getModel2();
        picture1Panel.setModel2(picture2Model);
        repaint();
    }

    public static void main (String args[]){
        new GUI("Similarities");
    }
}
