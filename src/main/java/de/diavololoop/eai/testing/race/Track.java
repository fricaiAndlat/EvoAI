package de.diavololoop.eai.testing.race;

import com.sun.javafx.geom.Vec2d;
import com.sun.javafx.geom.Vec3d;
import de.diavololoop.eai.individual.Individual;
import de.diavololoop.eai.simulation.ISimulation;

import javax.swing.*;
import java.awt.*;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;

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

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            //System.out.println("length: "+result.size());

            if (result.size() >= minLength) {

                System.out.println("init found: "+init);

                return result;
            }

        }
    }

    private List<Street> createStreet(int width, int height, long init) {

        double tileLength = 40;

        StreetFinder finder = new StreetFinder(init);

        Vec3d nextPoint = new Vec3d(0, 0, 0);
        Vec3d point = new Vec3d(1, 40, 0);

        List<Street> result = new LinkedList<Street>();

        while (true) {



            nextPoint.set(point);
            Vec3d dir = finder.nextDirection();
            dir.mul(tileLength);
            nextPoint.add(dir);



            Street nextStreet = new Street(point, nextPoint);

            if (result.size() > 2 && result.stream().limit(result.size()-3).anyMatch(e -> e.crossInner(nextStreet))) {
                break;
            }

            if (nextPoint.x < 0 || nextPoint.y < 0 || nextPoint.x > width || nextPoint.y > height) {
                break;
            }

            result.add(nextStreet);
            point.set(nextPoint);



        }
        return result;


    }


    private class StreetFinder {

        Random RAND;

        Vec3d direction = new Vec3d(1, 0, 0);
        double angle = 0;
        double a0 = 0;
        double a1 = 0;
        double maxA = 0.001;


        public StreetFinder(long init) {
            RAND = new Random(init);
        }

        public Vec3d nextDirection() {

            a1 = (RAND.nextDouble() * 2 - 1) * maxA;

            //a1 = Math.min(1, Math.max(-1, a1));

            a0 += a1;

            //a0 = (RAND.nextDouble() * 2 - 1) * maxA;

            //a0 = Math.min(1, Math.max(-1, a0));

            angle += a0;


            direction.set(Math.cos(angle), Math.sin(angle), 0);

            return direction;
        }


    }

    private class Street {
        Vec3d roadStart = new Vec3d();
        Vec3d roadEnd   = new Vec3d();

        Vec3d leftStart = new Vec3d();
        Vec3d leftEnd   = new Vec3d();

        Vec3d rightStart = new Vec3d();
        Vec3d rightEnd   = new Vec3d();

        public Street(Vec3d start, Vec3d end) {
            roadStart.set(start);
            roadEnd.set(end);
        }

        public boolean crossInner(Street street) {

            Vec3d n = new Vec3d(this.roadEnd);
            n.sub(this.roadStart);
            n.set(n.y, -n.x, 0);

            Vec3d s = new Vec3d(this.roadStart);
            Vec3d p = new Vec3d(street.roadStart);
            Vec3d u = new Vec3d(street.roadEnd);
            u.sub(street.roadStart);

            s.sub(p);
            double v1 = s.dot(n);
            double v2 = u.dot(n);

            if (v2 == 0) {
                return false;
            }

            double alpha = v1 / v2;

            if (alpha < 0 || alpha > u.length()) {
                return false;
            }

            //+++++

            n.set(street.roadEnd);
            n.sub(street.roadStart);
            n.set(n.y, -n.x, 0);

            s.set(street.roadStart);
            p.set(this.roadStart);
            u.set(this.roadEnd);
            u.sub(this.roadStart);

            s.sub(p);
            v1 = s.dot(n);
            v2 = u.dot(n);

            if (v2 == 0) {
                return false;
            }

            alpha = v1 / v2;

            if (alpha < 0 || alpha > u.length()) {
                return false;
            }

            /*double EPSILON = 0;

            //calc as this as plane
            //n = (y, -x);
            Vec3d n = new Vec3d(this.roadEnd);
            n.sub(this.roadStart);
            n.set(n.y, -n.x, 0);

            //--
            Vec3d temp = new Vec3d(this.roadStart);
            temp.sub(street.roadStart);
            double v1 = temp.dot(n);

            temp.set(street.roadEnd);
            temp.sub(street.roadStart);
            double v2 = temp.dot(n);

            if(v2 == 0) {
                return false;
            }

            double alpha = v1 / v2;

            //System.out.println(alpha);

            if (alpha <= EPSILON || alpha - temp.length() >= -EPSILON) {
                return false;
            }
            System.out.println("------------------------");
            System.out.printf("alpha %f, length %f \r\n", alpha, temp.length());

            //calc other as plane
            n.set(street.roadEnd);
            n.sub(street.roadStart);
            n.set(n.y, -n.x, 0);

            temp.set(street.roadStart);
            temp.sub(this.roadStart);
            v1 = temp.dot(n);
            temp.set(this.roadEnd);
            temp.sub(this.roadStart);
            v2 = temp.dot(n);

            if(v2 == 0) {
                return false;
            }

            alpha = v1 / v2;
            //System.out.println(alpha);

            if (alpha <= EPSILON || alpha - temp.length() >= -EPSILON) {
                return false;
            }
            System.out.printf("alpha %f, length %f \r\n", alpha, temp.length());
            */System.out.printf("(%f; %f), (%f; %f)\r\n", roadStart.x, roadStart.y, roadEnd.x, roadEnd.y);
            System.out.printf("(%f; %f), (%f; %f)\r\n", street.roadStart.x, street.roadStart.y, street.roadEnd.x, street.roadEnd.y);

            System.out.println("not valid");


            return true;

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
            });
        }
    }
}
