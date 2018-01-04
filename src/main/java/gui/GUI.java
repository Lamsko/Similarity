package gui;

import model.Model;
import model.Point;
import net.miginfocom.swing.MigLayout;
import parser.Parser;
import resolvers.NeighbourIntegrityResolver;
import resolvers.NeighbouringPointsResolver;
import resolvers.RANSACLinearResolver;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class GUI extends JFrame implements ActionListener, ItemListener{
    Dimension size = new Dimension(900,800);
    Dimension pictureSize = new Dimension(450,800);
    Dimension containerPanelSize = new Dimension(900,800);
    JPanelImpl picture1Panel;
    Model picture1Model, picture2Model;
    JButton
            loadPicture1,
            loadPicture2,
            resolveButton,
            resolveForIntegrityButton,
            resolveWithRANSACLinearButton,
            resolveWithRANSACPerspectiveButton;
    JCheckBox
            showPointsCheckBox,
            showNeighboursCheckBox,
            showCharactersisticPointsCheckBox;
    File
            picture1 = new File("D:\\Kuba\\Google Drive\\Uczelnia\\Semestr 7\\SI i Inżynieria Wiedzy\\Laboratorium\\Zadanie 4\\Similarity\\src\\main\\resources\\kwiatek1.png"),
            picture2 = new File("D:\\Kuba\\Google Drive\\Uczelnia\\Semestr 7\\SI i Inżynieria Wiedzy\\Laboratorium\\Zadanie 4\\Similarity\\src\\main\\resources\\kwiatek2.png"),
            data1 = new File("D:\\Kuba\\Google Drive\\Uczelnia\\Semestr 7\\SI i Inżynieria Wiedzy\\Laboratorium\\Zadanie 4\\Similarity\\src\\main\\resources\\kwiatek1.png.haraff.sift"),
            data2 = new File("D:\\Kuba\\Google Drive\\Uczelnia\\Semestr 7\\SI i Inżynieria Wiedzy\\Laboratorium\\Zadanie 4\\Similarity\\src\\main\\resources\\kwiatek2.png.haraff.sift"),
            haraffPicture1 = null,
            haraffPicture2 = null;
    NeighbouringPointsResolver neighbouringPointsResolver;
    NeighbourIntegrityResolver neighbourIntegrityResolver;
    RANSACLinearResolver ransacLinearResolver;

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
        resolveButton = new JButton("Znajdź wzajemne punkty kluczowe");
        resolveButton.addActionListener(this);
        resolveButton.setEnabled(false);
        resolveForIntegrityButton = new JButton("Analiza spójności sąsiedztwa");
        resolveForIntegrityButton.addActionListener(this);
        resolveForIntegrityButton.setEnabled(false);
        resolveWithRANSACLinearButton = new JButton("RANSAC dwuliniowy");
        resolveWithRANSACLinearButton.addActionListener(this);
        resolveWithRANSACLinearButton.setEnabled(false);
        resolveWithRANSACPerspectiveButton = new JButton("RANSAC perspektywiczny");
        resolveWithRANSACPerspectiveButton.addActionListener(this);
        resolveWithRANSACPerspectiveButton.setEnabled(false);
        showNeighboursCheckBox = new JCheckBox("Wyświetl wz. sąsiadów");
        showNeighboursCheckBox.addItemListener(this);
        showNeighboursCheckBox.setSelected(false);
        showPointsCheckBox = new JCheckBox("Rysuj punkty kluczowe");
        showPointsCheckBox.addItemListener(this);
        showPointsCheckBox.setSelected(false);
        showCharactersisticPointsCheckBox = new JCheckBox("Rysuj elispy punktów kluczowych");
        showCharactersisticPointsCheckBox.addItemListener(this);
        showNeighboursCheckBox.setSelected(false);

        add(loadPicture1);
        add(loadPicture2);
        add(picture1Panel, "span 2, growx, growy");
        add(resolveButton, "span 2, growx");
        add(resolveForIntegrityButton, "span 2, growx");
        add(resolveWithRANSACLinearButton, "span 2, growx");
        add(resolveWithRANSACPerspectiveButton, "span 2, growx");
        add(showCharactersisticPointsCheckBox);
        add(showPointsCheckBox, "split 2");
        add(showNeighboursCheckBox);
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
                    if(haraffPicture2!=null){
                        resolveButton.setEnabled(true);
                    }
                } else {
                    picture2 = fc.getSelectedFile();
                    haraffPicture2 = new File(picture2.getAbsoluteFile() + ".haraff.sift.png");
                    System.out.println(picture2.getAbsoluteFile());
                    data2 = new File(picture2.getAbsoluteFile() + ".haraff.sift");
                    System.out.println("Also loaded data " + data2.getName());
                    picture1Panel.setImage2(picture2, haraffPicture2);
                    picture2Model = Parser.parse(data2);
                    picture1Panel.setModel2(picture2Model);
                    if(haraffPicture1!=null){
                        resolveButton.setEnabled(true);
                    }
                }
            }
            repaint();
        }
        else if (actionEvent.getSource()==resolveButton){
            neighbouringPointsResolver = new NeighbouringPointsResolver(picture1Model, picture2Model, this);
            neighbouringPointsResolver.resolve();
        }
        else if (actionEvent.getSource()==resolveForIntegrityButton){
            neighbourIntegrityResolver = new NeighbourIntegrityResolver(this, neighbouringPointsResolver.getPointsWithNeighbors());
            neighbourIntegrityResolver.resolve();
        }
        else if (actionEvent.getSource()==resolveWithRANSACLinearButton){
            System.out.println("Resolving with linear RANSAC");
            ransacLinearResolver = new RANSACLinearResolver(this,neighbouringPointsResolver.getPointsWithNeighbors());
            ArrayList<Point> RANSAC = ransacLinearResolver.resolve();
            for (Point p: RANSAC){
                System.out.println(p);
            }
        }
    }

    @Override
    public void itemStateChanged(ItemEvent itemEvent) {
        repaint();
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
            g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            if(imageToDraw1 !=null){
                g.drawImage(imageToDraw1, 0,0, null);
            }
            if(imageToDraw2 !=null){
                g.drawImage(imageToDraw2, 450,0, null);
            }
            if(imageModel1 !=null&&showPointsCheckBox.isSelected()){
                g.setColor(Color.RED);
                for(Point p: imageModel1.getPoints()){
                    g.fillOval((int)p.getX(), (int)p.getY(), 4, 4);
                }

            }
            if(imageModel2!=null&&showPointsCheckBox.isSelected()){
                for(Point p: imageModel2.getPoints()){
                    g.fillOval((int)p.getX()+450, (int)p.getY(), 4, 4);
                }
            }

            g.setColor(Color.GREEN);
            if(imageModel1!=null&&imageModel2!=null&&showNeighboursCheckBox.isSelected()) {
                for (Point p : imageModel1.getPoints()) {
                    if (p.hasNearestNeighbour()) {
                        Point temp = p.getNearestNeighbour();
                        if(temp.getNearestNeighbour().equals(p)) {
                            g.drawLine((int) p.getX(), (int) p.getY(), (int) (450 + temp.getX()), (int) temp.getY());
                        }
                    }
                }
            }

            if(neighbouringPointsResolver !=null && neighbouringPointsResolver.getNeighboringPointCount()!=-1){
                Font f = new Font("serif", Font.BOLD, 20);
                g.setColor(Color.RED);
                g.setFont(f);
                g.drawString(("Ilość wzajemnych punktów kluczowych: "+ neighbouringPointsResolver.getNeighboringPointCount()), 10, 30);
            }

            if(neighbourIntegrityResolver != null && neighbourIntegrityResolver.getNeighboringPointCount()!=0){
                g.setColor(Color.CYAN);
                System.out.println("Painiting points!");
                g.drawString("Ilość znalezionych punktów spójnych: "+neighbourIntegrityResolver.getNeighboringPointCount(),10,50);
                for (Point p: neighbourIntegrityResolver.getProduct()){
                    Point temp = p.getNearestNeighbour();
                    g.drawLine((int) p.getX(), (int) p.getY(), (int) (450 + temp.getX()), (int) temp.getY());
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
            if(showCharactersisticPointsCheckBox.isSelected()) {
                imageToDraw1 = haraffImage1;
                imageToDraw2 = haraffImage2;
                repaint();
            }
        }

        @Override
        public void mouseExited(MouseEvent mouseEvent) {
            if(showCharactersisticPointsCheckBox.isSelected()) {
                imageToDraw1 = originalImage1;
                imageToDraw2 = originalImage2;
                repaint();
            }
        }
    }

    public void setNearestNeighbourData(NeighbouringPointsResolver r){
        picture1Model=r.getModel1();
        picture1Panel.setModel1(picture1Model);
        picture2Model=r.getModel2();
        picture1Panel.setModel2(picture2Model);
        resolveForIntegrityButton.setEnabled(true);
        resolveWithRANSACLinearButton.setEnabled(true);
        resolveWithRANSACPerspectiveButton.setEnabled(true);
        repaint();
    }

    public void refresh(){
        this.repaint();
    }

    public static void main (String args[]){
        new GUI("Similarities");
    }
}
