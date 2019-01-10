/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.util.LinkedList;
import java.util.List;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import pl.jrj.data.IDataMonitor;

/**
 *
 * @author milosz
 */
public class EbClient {

    /*
    line
    3ax+2by=0
    3by+2cz=0
    
    P0=(0,0,0)
    n1=(3a,2b,0)
    n2=(0,3b,2c)
    
    direction vector
    n1xn2 = (4bc,-6ac,9ab)
    
    parametric line equation:
    x=4bct
    y=-6act
    z=9abt
     */
    private static IDataMonitor dataMonitor;
    private static double a;
    private static double b;
    private static double c;

    /**
     * Retrieves bean using JNDI lookup and initial context
     *
     * @throws NamingException
     */
    static void loadDataMonitor() throws NamingException {
        Context ctx = new InitialContext();
        dataMonitor = (IDataMonitor) ctx.lookup(
                "java:global/ejb-project/DataMonitor");
    }

    /**
     * Class that represents point in 3D with mass
     */
    private static class Point3D {

        private double x;
        private double y;
        private double z;
        private double mass;

        /**
         * Constructor
         */
        public Point3D() {
        }

        /**
         * Constructor with parameters
         *
         * @param x X coordinate of point
         * @param y Y coordinate of point
         * @param z Z coordinate of point
         * @param mass mass of point
         */
        public Point3D(double x, double y, double z, double mass) {
            this.x = x;
            this.y = y;
            this.z = z;
            this.mass = mass;
        }

        /**
         * Getter
         *
         * @return X coordinate of point
         */
        public double getX() {
            return x;
        }

        /**
         * Setter
         *
         * @param x X coordinate of Point
         */
        public void setX(double x) {
            this.x = x;
        }

        /**
         * Getter
         *
         * @return Y coordinate of point
         */
        public double getY() {
            return y;
        }

        /**
         * Setter
         *
         * @param y Y coordinate of Point
         */
        public void setY(double y) {
            this.y = y;
        }

        /**
         * Getter
         *
         * @return Z coordinate of point
         */
        public double getZ() {
            return z;
        }

        /**
         * Setter
         *
         * @param z Z coordinate of point
         */
        public void setZ(double z) {
            this.z = z;
        }

        /**
         * Getter
         *
         * @return mass of point
         */
        public double getMass() {
            return mass;
        }

        /**
         * Setter
         *
         * @param mass mass of point
         */
        public void setMass(double mass) {
            this.mass = mass;
        }
    }

    /**
     * Retrieves numbers from dataMonitor(EJB) and creates Point3D from it
     *
     * @return Point3D with coordinates and mass retrieved from
     * dataMonitor(EJB). If one of parameters retrieved from dataMonitor is
     * null, then method returns null;
     */
    private static Point3D retrieveOnePoint() {
        Double x, y, z, mass;
        x = y = z = mass = null;

        if (dataMonitor.hasNext()) {
            x = dataMonitor.next();
        }
        if (dataMonitor.hasNext()) {
            y = dataMonitor.next();
        }
        if (dataMonitor.hasNext()) {
            z = dataMonitor.next();
        }
        if (dataMonitor.hasNext()) {
            mass = dataMonitor.next();
        }

        if (x != null && y != null && z != null && mass != null) {
            return new Point3D(x, y, z, mass);
        }
        return null;
    }

    /**
     * Creates list of Point3D and returns it. Points are created from numbers
     * from dataMonitor(EJB)
     *
     * @return List of Point3D objects. There is no null value inside this list
     */
    private static List<Point3D> retrieveAllPoints() {
        List<Point3D> pointList = new LinkedList<>();
        Point3D point;
        while (true) {
            point = retrieveOnePoint();
            if (point != null) {
                pointList.add(point);
            } else {
                break;
            }
        }

        return pointList;
    }

    /**
     * Assigns first three numbers from dataMonitor(EJB) to a,b,c coefficients
     */
    private static void loadABCCoeffiecients() {
        if (dataMonitor.hasNext()) {
            a = dataMonitor.next();
        }
        if (dataMonitor.hasNext()) {
            b = dataMonitor.next();
        }
        if (dataMonitor.hasNext()) {
            c = dataMonitor.next();
        }
    }

    /**
     * Class that represents vector in 3D
     */
    private static class Vector3D {

        private double x;
        private double y;
        private double z;

        /**
         * Constructor
         */
        public Vector3D() {
        }

        /**
         * Constructor with parameters
         *
         * @param x X coordinate of vector
         * @param y Y coordinate of vector
         * @param z Z coordinate of vector
         */
        public Vector3D(double x, double y, double z) {
            this.x = x;
            this.y = y;
            this.z = z;
        }

        /**
         * Calculates length of vector
         *
         * @return vector length
         */
        public double calculateLength() {
            return Math.sqrt(this.x * this.x
                    + this.y * this.y
                    + this.z * this.z);
        }

        /**
         * Getter
         *
         * @return X coordinate of vector
         */
        public double getX() {
            return x;
        }

        /**
         * Setter
         *
         * @param x X coordinate of vector
         */
        public void setX(double x) {
            this.x = x;
        }

        /**
         * Getter
         *
         * @return Y coordinate of vector
         */
        public double getY() {
            return y;
        }

        /**
         * Setter
         *
         * @param x Y coordinate of vector
         */
        public void setY(double y) {
            this.y = y;
        }

        /**
         * Getter
         *
         * @return Z coordinate of vector
         */
        public double getZ() {
            return z;
        }

        /**
         * Setter
         *
         * @param x Z coordinate of vector
         */
        public void setZ(double z) {
            this.z = z;
        }

        /**
         * Gives direction vector for line: 3ax + 2by = 0 , 3by + 2cz = 0
         *
         * @return direction vector for line: 3ax + 2by = 0 , 3by + 2cz = 0
         */
        public static Vector3D getDirectionVector() {
            return new Vector3D(4.0 * b * c, -6.0 * a * c, 9.0 * a * b);
        }

        /**
         * Gives vector P1P0 created from two points: P1 and P0. P0=(0,0,0) and
         * P1 is given as parameter
         *
         * @param P1 point, that will be used for vector creation
         * @return vector created from two points
         */
        public static Vector3D getVectorP1P0(Point3D P1) {
            return new Vector3D(P1.getX(), P1.getY(), P1.getZ());
        }

        /**
         * Gives vector product of two vectors
         *
         * @param v1 first vector
         * @param v2 second vector
         * @return vector product of two vectors given in method parameters
         */
        public static Vector3D calculateVectorProduct(Vector3D v1,
                Vector3D v2) {
            return new Vector3D(v1.getY() * v2.getZ() - v1.getZ() * v2.getY(),
                    -(v1.getX() * v2.getZ() - v1.getZ() * v2.getX()),
                    v1.getX() * v2.getY() - v1.getY() * v2.getX());
        }
    }

    /**
     * Calculates distance Between point and line in 3D
     *
     * @param point3D distance will be measured from this point
     * @return distance between given point and line in 3D
     */
    private static double calculateDistanceFromPointToLine(Point3D point3D) {
        Vector3D directionVector = Vector3D.getDirectionVector();
        Vector3D vectorP1P0 = Vector3D.getVectorP1P0(point3D);
        Vector3D vectorProduct
                = Vector3D.calculateVectorProduct(vectorP1P0, directionVector);
        double distance
                = vectorProduct.calculateLength()
                / directionVector.calculateLength();
        return distance;
    }

    /**
     * Calculates moment of inertia
     *
     * @param distance distance from point to line
     * @param mass mass of point
     * @return moment of inertia for given distance and mass
     */
    private static double calculateMomentOfInertia(double distance,
            double mass) {
        return mass * distance * distance;
    }

    /**
     * Calculates radius of inertia
     *
     * @param point3DList list of points in 3D. Radius of inertia will be
     * calculated for this list of points
     * @return radius of inertia for given list of points
     */
    private static double calculateRadiusOfInertia(List<Point3D> point3DList) {
        double momentOfInertiaSum = 0;
        double massSum = 0;
        double distance;
        for (Point3D point3D : point3DList) {
            distance = calculateDistanceFromPointToLine(point3D);
            momentOfInertiaSum += calculateMomentOfInertia(distance,
                    point3D.getMass());
            massSum += point3D.getMass();
        }
        return Math.sqrt(momentOfInertiaSum / massSum);
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {

        try {
            loadDataMonitor();
        } catch (NamingException ex) {
            return;
        }

        loadABCCoeffiecients();
        List<Point3D> point3DList = retrieveAllPoints();

        double radiusOfInertia = calculateRadiusOfInertia(point3DList);
        System.out.printf("%.5f", radiusOfInertia);
    }
}
