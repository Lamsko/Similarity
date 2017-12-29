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
    JPanelImpl picture1Panel, picture2Panel;
    JPanelContainer containerPanel;
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
        containerPanel = new JPanelContainer();
        containerPanel.setLayout(new MigLayout("gap 0, ins 0"));
        containerPanel.setMinimumSize(containerPanelSize);
        containerPanel.setPreferredSize(containerPanelSize);
        containerPanel.setSize(containerPanelSize);
        //containerPanel.setBorder(BorderFactory.createLineBorder(Color.MAGENTA, 2));
        loadPicture1 = new JButton("Wczytaj obrazek 1");
        loadPicture1.addActionListener(this);
        loadPicture2 = new JButton("Wczytaj obrazek 2");
        loadPicture2.addActionListener(this);
        picture1Panel = new JPanelImpl();
        picture1Panel.setMinimumSize(pictureSize);
        picture1Panel.setSize(pictureSize);
        picture1Panel.setPreferredSize(pictureSize);
        picture2Panel = new JPanelImpl();
        picture2Panel.setMinimumSize(pictureSize);
        picture2Panel.setSize(pictureSize);
        picture2Panel.setPreferredSize(pictureSize);
        resolveButton = new JButton("Rozwiąż");
        resolveButton.addActionListener(this);

        add(loadPicture1);
        add(loadPicture2);
        add(containerPanel, "span 2");
        containerPanel.add(picture1Panel);
        containerPanel.add(picture2Panel);
        add(resolveButton, "span 2, growx");
        //resolveButton.setEnabled(false);
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
                    picture1Panel.setImage(picture1, haraffPicture1);
                    picture1Model = Parser.parse(data1);
                    picture1Panel.setModel(picture1Model);
                } else {
                    picture2 = fc.getSelectedFile();
                    haraffPicture2 = new File(picture2.getAbsoluteFile() + ".haraff.sift.png");
                    System.out.println(picture2.getAbsoluteFile());
                    data2 = new File(picture2.getAbsoluteFile() + ".haraff.sift");
                    System.out.println("Also loaded data " + data2.getName());
                    picture2Panel.setImage(picture2, haraffPicture2);
                    picture2Model = Parser.parse(data2);
                    picture2Panel.setModel(picture2Model);
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
        private BufferedImage imageToDraw;
        private BufferedImage originalImage, haraffImage;
        private Model imageModel;

        public JPanelImpl(){
            super();
            addMouseListener(this);
        }

        public void setImage(File original, File haraff){
            try {
                originalImage = ImageIO.read(original);
                haraffImage = ImageIO.read(haraff);
                imageToDraw=originalImage;
            }
            catch (IOException e){
                System.out.println("Failed during reading the image");
                e.printStackTrace();
            }
        }

        public void setModel(Model model){
            this.imageModel=model;
        }

        @Override
        protected void paintComponent(Graphics graphics) {
            super.paintComponent(graphics);
            Graphics2D g = (Graphics2D) graphics;
            if(imageToDraw !=null){
                g.drawImage(imageToDraw, 0,0, null);
            }
            if(imageModel!=null){
                g.setColor(Color.RED);
                for(Point p: imageModel.getPoints()){
                    g.fillOval((int)p.getX(), (int)p.getY(), 4, 4);
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
            imageToDraw=haraffImage;
            repaint();
        }

        @Override
        public void mouseExited(MouseEvent mouseEvent) {
            imageToDraw=originalImage;
            repaint();
        }
    }

    class JPanelContainer extends JPanel{
        @Override
        public void paintComponent(Graphics graphics) {
            super.paintComponent(graphics);
            Graphics2D g = (Graphics2D) graphics;
            g.setColor(Color.RED);
            g.fillOval(100,100, 50, 50);
            System.out.println("Before paint");
            if(picture1Model!=null && picture2Model!=null) {
                System.out.println("In nearest neighbour");
                for (Point p: picture1Model.getPoints()){
                    if(p.hasNearestNeighbour()){
                        g.setColor(Color.GREEN);
                        Point near = p.getNearestNeighbour();
                        g.drawLine((int)p.getX(), (int)p.getY(), (int)(pictureSize.width+near.getX()), (int)near.getY());
                    }
                }
            }
        }
    }

    public void setNearestNeighbourData(Resolver r){
        repaint();
    }

    public static void main (String args[]){
        new GUI("Similarities");
    }
}
