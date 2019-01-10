/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;

/**
 *
 * @author milosz
 */
public class AppMain {

    private static final String PU_NAME = "myPersistence";
    private static EntityManagerFactory factory = null;
    private static String firstName = "";
    private static String lastName = "";
    private static String courseName = "";

    /**
     * Read file and find first, last student name and course name
     *
     * @param filePath String path to file
     * @return List of strings that contains first, last student name and course
     * name
     */
    private static void getDataFromFile(String filePath) {
        String line;

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            line = br.readLine();
            if (line != null) {
                courseName = line.trim();
            }

            line = br.readLine();
            if (line != null) {
                String[] arr = line.trim().split("\\s+");
                if (arr.length > 0) {
                    firstName = arr[0];
                }
                if (arr.length > 1) {
                    lastName = arr[1];
                }
            }
        } catch (IOException e) {
        }
    }

    /**
     * Get student mark in certain course from database
     *
     * @param courseName String course name
     * @param firstName String student first name
     * @param lastName String student last name
     * @return Integer mark
     */
    @SuppressWarnings("unchecked")
    private static Integer getMark(String courseName,
            String firstName, String lastName) {

        EntityManager em = factory.createEntityManager();
        try {
            em.getTransaction().begin();
            Query q = em.createQuery("select sc.mark "
                    + "from TblStudentcourse sc, TblStudents s, TblCourses c "
                    + "where s.id=sc.tblStudentcoursePK.studentid and "
                    + "c.id=sc.tblStudentcoursePK.courseid and "
                    + "s.firstname = :firstName and "
                    + "s.lastname = :lastName and "
                    + "c.coursename = :courseName");
            q.setParameter("firstName", firstName);
            q.setParameter("lastName", lastName);
            q.setParameter("courseName", courseName);
            List<Integer> results = (List<Integer>) q.getResultList();
            em.getTransaction().commit();

            if (results.size() > 0) {
                return results.get(0);
            } else {
                return null;
            }
            //finally will be always called, even after return statement
        } finally {
            em.close();
        }
    }

    /**
     * Get all marks of certain course
     *
     * @param courseName String course name
     * @return list of marks
     */
    @SuppressWarnings("unchecked")
    private static List<Integer> getAllMarks(String courseName) {
        EntityManager em = factory.createEntityManager();
        try {
            em.getTransaction().begin();
            Query q = em.createQuery("select sc.mark "
                    + "from TblStudentcourse sc, TblCourses c "
                    + "where c.id=sc.tblStudentcoursePK.courseid and "
                    + "c.coursename = :courseName");
            q.setParameter("courseName", courseName);
            List<Integer> results = (List<Integer>) q.getResultList();
            em.getTransaction().commit();
            return results;
            //finally will be always called, even after return statement
        } finally {
            em.close();
        }
    }

    /**
     * Calculates median of marks for certain list of marks
     *
     * @param marks list of integers that represents marks
     * @return median for list of marks
     */
    private static double findMedian(List<Integer> marks) {
        if (marks.size() > 0) {
            Collections.sort(marks);
            int middle = marks.size() / 2;
            if ((marks.size() % 2 == 0) && (marks.size() > 1)) {
                return (marks.get(middle) + marks.get(middle - 1)) / 2.0;
            } else {
                return marks.get(middle);
            }
        } else {
            return 0.0;
        }
    }

    /**
     * Calculate how much greater/lower than median is mark.
     *
     * @param median double median of marks
     * @param mark double certain mark
     * @return percentage that describes how much greater/lower than median is
     * mark. If mark and median are equal then 0 is returned.
     */
    private static double calculateResult(double median, double mark) {
        if (Math.abs(mark - median) < 0.00000001) {
            return 0.0;
        } else {
            return (mark - median) / median * 100.0;
        }
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        getDataFromFile(args[0]);
        factory = Persistence.createEntityManagerFactory(PU_NAME);
        Integer mark = getMark(courseName, firstName, lastName);
        if (mark == null) {
            System.out.println("Wynik : 0%");
            return;
        }
        List<Integer> allMarks = getAllMarks(courseName);
        double median = findMedian(allMarks);
        double output = calculateResult(median, (double) mark);
        System.out.println("Wynik : " + output + "%");
    }
}
