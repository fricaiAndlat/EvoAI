package de.diavololoop.eai.testing.race;

import de.diavololoop.eai.individual.Individual;
import de.diavololoop.eai.simulation.ISimulation;
import org.joml.Intersectiond;
import org.joml.Vector2d;

import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.util.List;

public class Track extends JFrame implements ISimulation<Car> {

    private List<Street> streets;


    public Track() {

        setTitle("Track Simulation");
        setSize(1600, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        add(new Panel(), BorderLayout.CENTER);

        new Thread(() -> reset()).start();

        setVisible(true);

    }


    @Override
    public void reset() {

        streets = createStreetMin(1600, 800, 50);
        repaint();


        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


    }

    @Override
    public void addMember(Individual<Car> member) {

    }

    @Override
    public boolean isFinished() {
        return false;
    }

    @Override
    public void step() {

    }

    @Override
    public Map<Individual<Car>, Double> getResult() {
        return null;
    }

    private List<Street> createStreetMin(int width, int height, int minLength) {
        long init = 2;

        while(true) {

            List<Street> result = createStreet(width, height, init);
            ++init;

            streets = result;
            repaint();
            /*
            try {
                Thread.sleep( 1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }*/

            //System.out.println("length: "+result.size());

            if (result.size() >= minLength) {

                System.out.println("init found: "+init);
                repaint();
                return result;
            }

        }
    }

    private List<Street> createStreet(int width, int height, long init) {

        double tileLength = 40;
        double borderWidth = 40;

        StreetFinder finder = new StreetFinder(init);

        Vector2d nextPoint = new Vector2d(0, 0);
        Vector2d point = new Vector2d(1, 40);

        LinkedList<Street> result = new LinkedList<>();

        while (true) {



            nextPoint.set(point);
            Vector2d dir = finder.nextDirection();
            dir.mul(tileLength);
            nextPoint.add(dir);



            Street nextStreet = new Street(point, nextPoint);

            if (result.stream().anyMatch(e -> e.crossInner(nextStreet))) {
                break;
            }

            if (nextPoint.x < 0 || nextPoint.y < 0 || nextPoint.x > width || nextPoint.y > height) {
                break;
            }

            nextStreet.createBorder(borderWidth);

            if (result.size() > 0) {
                nextStreet.linkBack(result.getLast());
            }

            if (result.stream().anyMatch(e -> e.crossOuter(nextStreet))) {
                break;
            }

            result.addLast(nextStreet);
            point.set(nextPoint);
        }


        return result;


    }


    private class StreetFinder {

        Random RAND;

        Vector2d direction = new Vector2d(1, 0);
        double angle = 0;
        double a0 = 0;
        double a1 = 0;
        double maxA = 0.9;


        public StreetFinder(long init) {
            RAND = new Random(init);
        }

        public Vector2d nextDirection() {

            a1 = (RAND.nextDouble() * 2 - 1) * maxA;

            //a1 = Math.min(1, Math.max(-1, a1));

            a0 += a1;

            //a0 = (RAND.nextDouble() * 2 - 1) * maxA;

            a0 = Math.min(2, Math.max(-2, a0));

            angle += a0;


            direction.set(Math.cos(angle), Math.sin(angle));

            return direction;
        }


    }

    private class Street {
        Vector2d roadStart = new Vector2d();
        Vector2d roadEnd   = new Vector2d();

        Vector2d leftStart = new Vector2d();
        Vector2d leftEnd   = new Vector2d();

        Vector2d rightStart = new Vector2d();
        Vector2d rightEnd   = new Vector2d();

        Polygon polygon;

        public Street(Vector2d start, Vector2d end) {
            roadStart.set(start);
            roadEnd.set(end);
        }

        public boolean crossInner(Street street) {
            return cross(roadStart, roadEnd, street.roadStart, street.roadEnd);
        }

        public boolean crossOuter(Street street) {

            return cross(leftStart, leftEnd, street.leftStart, street.leftEnd)
                    || cross(leftStart, leftEnd, street.rightStart, street.rightEnd)
                    || cross(rightStart, rightEnd, street.leftStart, street.leftEnd)
                    || cross(rightStart, rightEnd, street.rightStart, street.rightEnd);

        }

        public Polygon getPolygon() {

            if (polygon == null) {
                polygon = new Polygon();
                polygon.addPoint((int)leftStart.x, (int)leftStart.y);
                polygon.addPoint((int)leftEnd.x, (int)leftEnd.y);
                polygon.addPoint((int)rightEnd.x, (int)rightEnd.y);
                polygon.addPoint((int)rightStart.x, (int)rightStart.y);
            }

            return polygon;
        }

        private boolean cross(Vector2d s1, Vector2d s2, Vector2d u1, Vector2d u2) {
            Vector2d dir = new Vector2d(s2);
            dir.sub(s1);
            double alpha = Intersectiond.intersectRayLineSegment(s1, dir, u1, u2);


            return alpha > 0.01 && alpha < 0.99;
        }

        public void linkBack(Street street) {


            Vector2d result = new Vector2d();

            Intersectiond.intersectLineLine(
                    leftStart.x, leftStart.y, 
                    leftEnd.x, leftEnd.y,
                    street.leftStart.x, street.leftStart.y, 
                    street.leftEnd.x, street.leftEnd.y,
                    result);
            
            leftStart.set(result);
            street.leftEnd.set(result);

            Intersectiond.intersectLineLine(
                    rightStart.x, rightStart.y,
                    rightEnd.x, rightEnd.y,
                    street.rightStart.x, street.rightStart.y,
                    street.rightEnd.x, street.rightEnd.y,
                    result);
            rightStart.set(result);
            street.rightEnd.set(result);

        }

        public void createBorder(double width) {

            Vector2d dir = new Vector2d(roadEnd);
            dir.sub(roadStart);

            Vector2d n = new Vector2d(dir);
            n.normalize(width);
            n.set(-n.y, n.x);

            rightStart = new Vector2d(roadStart);
            rightStart.add(n);

            rightEnd = new Vector2d(rightStart);
            rightEnd.add(dir);

            n.mul(-1);

            leftStart = new Vector2d(roadStart);
            leftStart.add(n);

            leftEnd = new Vector2d(leftStart);
            leftEnd.add(dir);

        }
    }

    public class Panel extends JPanel {

        @Override
        public void paintComponent(Graphics g) {

            if(streets == null) {
                return;
            }

            g.setColor(Color.YELLOW);

            g.fillRect(0, 0, getWidth(), getHeight());

            g.setColor(Color.BLACK);

            streets.forEach(s -> {
                g.drawLine((int)s.roadStart.x, (int)s.roadStart.y, (int)s.roadEnd.x, (int)s.roadEnd.y);

                if (s.leftStart == null) {
                    return;
                }
                g.setColor(Color.WHITE);
                g.fillPolygon(s.getPolygon());

                g.setColor(Color.BLACK);
                g.drawLine((int)s.leftStart.x, (int)s.leftStart.y, (int)s.leftEnd.x, (int)s.leftEnd.y);
                g.drawLine((int)s.rightStart.x, (int)s.rightStart.y, (int)s.rightEnd.x, (int)s.rightEnd.y);
            });
        }
    }
}
