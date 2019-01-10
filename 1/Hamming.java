
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author Milosz
 */
public class Hamming {

    /**
     * Calculates Hamming distance between two strings.
     *
     * @param s1 any string for comparison
     * @param s2 any string for comparison
     * @return distance between two strings if their length is equal. 
     *         If strings length is different or at least one string is null 
     *         then method returns -1
     */
    public static int calculateDistance(String s1, String s2) {

        if ((s1 == null || s2 == null) || (s1.length() != s2.length())) {
            return -1;
        }

        int dist = 0;
        int length = s1.length();
        for (int i = 0; i < length; i++) {
            if (s1.charAt(i) != s2.charAt(i)) {
                dist++;
            }
        }

        return dist;
    }
}
