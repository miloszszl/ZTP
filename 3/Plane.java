/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Stack;
import java.util.TreeSet;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author milosz szlachetka
 */
@WebServlet(urlPatterns = {"/Plane"})
public class Plane extends HttpServlet {

    private static final long serialVersionUID = 12345L;

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");

        response.setCharacterEncoding("UTF-8");
        request.setCharacterEncoding("UTF-8");

        try (PrintWriter out = response.getWriter()) {

            if (request.getParameter("db") == null) {
                out.println("no database url");
                return;
            }

            String db = new String(request.getParameter("db")
                    .getBytes("ISO-8859-1"), "UTF-8");

            try {
                ResultSet rs = getResultsFromDB(db);
                Map<Float, List<Point>> pointsMap = createPoints(rs);
                float maxArea = findMaxArea(pointsMap);

                out.println(String.format("Maksimum : %.5f", maxArea));
            } catch (SQLException | ClassNotFoundException ex) {
                out.println("Database problem");
            }
        }
    }

    /**
     * Reads data from database
     *
     * @param db URL that describes connection
     * @return ResultSet that contains query results from database
     * @throws SQLException
     * @throws ClassNotFoundException
     */
    private ResultSet getResultsFromDB(String db) throws SQLException,
            ClassNotFoundException {
        String database = db;
        Connection conn = DriverManager.getConnection(database);
        Statement stmt = conn.createStatement();
        return stmt.executeQuery("SELECT x, y, z FROM Otable");
    }

    /**
     * Creates points from ResultSet
     *
     * @param rs ResultSet that contains coordinates of points
     * @return Map<Float, List<Point>> points that are grouped by "z" coordinate
     * @throws SQLException
     */
    private Map<Float, List<Point>> createPoints(ResultSet rs)
            throws SQLException {

        Map<Float, List<Point>> groupedPoints = new HashMap<>();

        while (rs.next()) {

            float z = rs.getFloat("z");
            Point p = new Point(rs.getFloat("x"),
                    rs.getFloat("y"));

            if (groupedPoints.containsKey(z)) {
                groupedPoints.get(z).add(p);
            } else {
                groupedPoints.put(z, new ArrayList<>(Arrays.asList(p)));
            }
        }
        return groupedPoints;
    }

    /**
     * Finds max area from all groups of points
     *
     * @param pointsMap Map<Float, List<Point>> points that are grouped by "z"
     * coordinate
     * @return maximum from all groups of points (float)
     */
    private float findMaxArea(Map<Float, List<Point>> pointsMap) {
        Iterator<Map.Entry<Float, List<Point>>> it = pointsMap.entrySet()
                .iterator();
        float maxArea = -1.0f;
        float currentArea;
        GrahamScan gs = new GrahamScan();
        List<Point> convexHull;
        while (it.hasNext()) {
            Map.Entry<Float, List<Point>> entry = it.next();
            convexHull = gs.getConvexHull(entry.getValue());

            if (convexHull == null) {
                continue;
            }
            currentArea = calculatePolygonArea(convexHull);

            if (currentArea > maxArea) {
                maxArea = currentArea;
            }
        }
        return maxArea;
    }

    /**
     * Class that represents 2D point
     *
     */
    class Point {

        private float x;
        private float y;

        /**
         * Constructor
         */
        public Point() {
        }

        /**
         * Constructor with parameters
         *
         * @param x Point X coordinate
         * @param y Point Y coordinate
         */
        public Point(float x, float y) {
            this.x = x;
            this.y = y;
        }

        /**
         * Getter
         *
         * @return Point X coordinate
         */
        public float getX() {
            return x;
        }

        /**
         * Setter
         *
         * @param x Point X coordinate to be set
         */
        public void setX(float x) {
            this.x = x;
        }

        /**
         * Getter
         *
         * @return Point Y coordinate
         */
        public float getY() {
            return y;
        }

        /**
         * Setter
         *
         * @param y Point Y coordinate to be set
         */
        public void setY(float y) {
            this.y = y;
        }

        /**
         * Calculates theta (angle) from polar coordinates (r,theta) for two
         * points(this and other point given by "p" parameter)
         *
         * @param p Point needed for calculating theta
         * @return angle value (theta) from polar coordinates (r,theta) that
         * corresponds Cartesian coordinates (x,y) for two points
         */
        public float getTheta(Point p) {
            return (float) Math.atan2(this.getY() - p.getY(),
                    this.getX() - p.getX());
        }

        /**
         * Calculates distance between two points
         *
         * @param p Point that distance will be calculated to
         * @return distance between "this" Point and "p" Point
         */
        public float getDistance(Point p) {
            float deltaX = (float) Math.pow(p.getX() - this.getX(), 2);
            float deltaY = (float) Math.pow(p.getY() - this.getY(), 2);
            return (float) Math.sqrt(deltaX + deltaY);
        }

        /**
         * Indicates if other object is equal to this object
         *
         * @param obj object for comparison
         * @return
         */
        @Override
        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj == null) {
                return false;
            }
            if (getClass() != obj.getClass()) {
                return false;
            }
            final Point other = (Point) obj;
            if (Float.floatToIntBits(this.x)
                    != Float.floatToIntBits(other.x)) {
                return false;
            }

            return Float.floatToIntBits(this.y)
                    == Float.floatToIntBits(other.y);
        }

        /**
         * Calculates hashCode for object
         *
         * @return hashCode based on X,Y coordinates
         */
        @Override
        public int hashCode() {
            int hash = 7;
            hash = 47 * hash + Float.floatToIntBits(this.x);
            hash = 47 * hash + Float.floatToIntBits(this.y);
            return hash;
        }
    }

    /**
     * Calculates polygon area from coordinates
     *
     * @param points List of points in clockwise or counter-clockwise order
     * @return area of polygon
     */
    private float calculatePolygonArea(List<Point> points) {
        float sum = 0.0f;

        Point current, next;
        for (int i = 0; i < points.size(); i++) {

            current = points.get(i);
            if (i == points.size() - 1) {
                next = points.get(0);
            } else {
                next = points.get(i + 1);
            }

            sum += current.x * next.y - current.y * next.x;
        }
        return (float) Math.abs(sum / 2.0);
    }

    /**
     * Class for Points comparison
     */
    class PointsComparator implements Comparator<Point> {

        Point lowestPoint;

        /**
         * Constructor
         *
         * @param lowestPoint Point that has minimal Y coordinate value or
         * minimal Y and X values
         */
        public PointsComparator(Point lowestPoint) {
            this.lowestPoint = lowestPoint;
        }

        /**
         * Compares two Points
         *
         * @param p1 First Point for comparison
         * @param p2 Second Point for comparison
         * @return 0 if points are equal, -1 if p1 > p2 else 1
         */
        @Override
        public int compare(Point p1, Point p2) {

            if (p1.equals(p2)) {
                return 0;
            }

            float theta1 = p1.getTheta(lowestPoint);
            float theta2 = p2.getTheta(lowestPoint);

            if (theta1 > theta2) {
                return 1;
            } else if (theta1 < theta2) {
                return -1;
            } else {
                float distP1 = p1.getDistance(lowestPoint);
                float distP2 = p2.getDistance(lowestPoint);

                if (distP1 < distP2) {
                    return -1;
                } else {
                    return 1;
                }
            }
        }
    }

    /**
     * Class for finding ConvexHull with Graham algorithm
     */
    class GrahamScan {

        /**
         * Finds lowest point based on Y and X coordinates
         *
         * @param points List of points that will be searched
         * @return Point that has minimal Y coordinate value or minimal Y and X
         * values
         */
        public Point getLowestPoint(List<Point> points) {

            if (points.size() <= 0) {
                return null;
            } else {
                Point lowestPoint = points.get(0);

                for (Point point : points) {
                    if ((point.y < lowestPoint.y)
                            || (point.y == lowestPoint.y
                            && point.x < lowestPoint.x)) {
                        lowestPoint = point;
                    }
                }

                return lowestPoint;
            }
        }

        /**
         * Sorts points based on angle form OX axis and distance from lowest
         * Point
         *
         * @param points List of points that will be sorted
         * @return
         */
        private Set<Point> getSortedPoints(List<Point> points) {

            PointsComparator pointsComparator
                    = new PointsComparator(getLowestPoint(points));
            TreeSet<Point> pointsSet = new TreeSet<>(pointsComparator);
            pointsSet.addAll(points);

            return pointsSet;
        }

        /**
         * Checks if points are on same line
         *
         * @param points Points to be checked
         * @return true if points lay on one line, otherwise false
         */
        private boolean areCollinear(List<Point> points) {

            if (points.size() > 1) {
                Point p1 = points.get(0);
                Point p2 = points.get(1);

                for (int i = 2; i < points.size(); i++) {

                    if (turn(p1, p2, points.get(i)) != 0) {
                        return false;
                    }
                }
            }
            return true;
        }

        /**
         * Finds convex hull for list of points
         *
         * @param points List of points from which convex hull will be created
         * @return List of points that create convex hull
         */
        public List<Point> getConvexHull(List<Point> points) {

            List<Point> sortedPoints
                    = new ArrayList<>(getSortedPoints(points));

            if (sortedPoints.size() < 3 || areCollinear(sortedPoints)) {
                return null;
            }

            Stack<Point> pointsStack = new Stack<>();

            pointsStack.push(sortedPoints.get(0));
            pointsStack.push(sortedPoints.get(1));

            Point first, middle, last;
            for (int i = 2; i < sortedPoints.size(); i++) {

                first = sortedPoints.get(i);
                middle = pointsStack.pop();
                last = pointsStack.peek();

                switch (turn(last, middle, first)) {
                    case 1:
                        pointsStack.push(middle);
                        pointsStack.push(first);
                        break;
                    case 0:
                        pointsStack.push(first);
                        break;
                    case -1:
                        i--;
                        break;
                }
            }
            pointsStack.push(sortedPoints.get(0));
            return new ArrayList<>(pointsStack);
        }

        /**
         * Gives direction of checking ordered points by calculating vector
         * product between next 3 points and basing on result gives suitable
         * direction
         *
         * @param p1 First Point needed for calculating direction
         * @param p2 Second Point needed for calculating direction
         * @param p3 Third Point needed for calculating direction
         * @return int value that designates direction: -1 -> clockwise, 0 ->
         * collinear, 1 -> counter-clockwise
         */
        private int turn(Point p1, Point p2, Point p3) {

            float vectorProduct
                    = (p2.getX() - p1.getX()) * (p3.getY() - p1.getY())
                    - (p2.getY() - p1.getY()) * (p3.getX() - p1.getX());

            if (vectorProduct < 0) {
                return -1;  //clockwise
            } else if (vectorProduct > 0) {
                return 1;   //counter clockwise
            } else {
                return 0;   //collinear
            }
        }
    }

// <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request request for servlet
     * @param response response from servlet
     * @throws ServletException if a such error
     * @throws IOException if such error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request request for servlet
     * @param response response from servlet
     * @throws ServletException if such error occurs
     * @throws IOException if such error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Description for Servlet
     *
     * @return Servlet description
     */
    @Override
    public String getServletInfo() {
        return "ZTP3 Servlet";
    }// </editor-fold>

}
