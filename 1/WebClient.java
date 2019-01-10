/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 *
 * @author Milosz
 */
public class WebClient {

    /**
     * Adds character "!" at the end of a string n times
     *
     * @param token string for extension
     * @param n number of times character "!" will be added
     * @return string expanded with "!" charcters at the end
     */
    public static String fillTokenEnd(String token, int n) {
        String character = "!";
        StringBuilder sb = new StringBuilder(token);

        for (int i = 0; i < n; i++) {
            sb.append(character);
        }

        return sb.toString();
    }

    /**
     * Adds character "!" at the beginning of a string n times
     *
     * @param token string for extension
     * @param n number of times character "!" will be added
     * @return string expanded with "!" charcters at the beginning
     */
    public static String fillTokenBeginning(String token, int n) {
        String character = "!";
        StringBuilder sb = new StringBuilder(token);

        for (int i = 0; i < n; i++) {
            sb.insert(0, character);
        }

        return sb.toString();
    }

    /**
     * Compares two strings by checking Hamming distance between them. Works
     * even for string that have different lengths.
     *
     * @param s1 first string for comparison
     * @param s2 second string for comparison
     * @return minimal distance between two strings
     */
    public static int compareTwoTokens(String s1, String s2) {
        int diff = s1.length() - s2.length();
        int minDist = Integer.MAX_VALUE;
        int res;

        if (diff < 0) { //s2.length()>s1.length()
            diff = -diff;
            String newToken = fillTokenEnd(s1, diff);

            res = Hamming.calculateDistance(newToken, s2);
            if ((res < minDist) && (res != -1)) {
                minDist = res;
            }

            newToken = fillTokenBeginning(s1, diff);

            res = Hamming.calculateDistance(newToken, s2);
            if ((res < minDist) && (res != -1)) {
                minDist = res;
            }
        } else if (diff > 0) { //s2.length()<s1.length()
            String newToken = fillTokenEnd(s2, diff);

            res = Hamming.calculateDistance(s1, newToken);
            if ((res < minDist) && (res != -1)) {
                minDist = res;
            }

            newToken = fillTokenBeginning(s2, diff);

            res = Hamming.calculateDistance(s1, newToken);
            if ((res < minDist) && (res != -1)) {
                minDist = res;
            }
        } else {    //s2.length()==s1.length()
            res = Hamming.calculateDistance(s1, s2);
            if ((res < minDist) && (res != -1)) {
                minDist = res;
            }
        }
        return minDist;
    }

    /**
     * Compares lists of tokens in different configurations. Lists must have
     * same size.
     *
     * @param list1 first list of strings for comparison
     * @param list2 second list of strings for comparison
     * @return minimal distance between two lists of strings
     */
    public static int compareSameLengthLists(List<String> list1,
            List<String> list2) {   //invoked only when lists have same size
        int minDist = Integer.MAX_VALUE;
        int res = 0;    //temporary result
        int listSize = list1.size();

        for (int i = 0; i < listSize; i++) {
            res += compareTwoTokens(list1.get(i), list2.get(i));
        }

        if (res < minDist) {
            minDist = res;
        }

        res = 0;

        if (listSize == 2) {
            res += compareTwoTokens(list1.get(0), list2.get(1));
            res += compareTwoTokens(list1.get(1), list2.get(0));

            if (res < minDist) {
                minDist = res;
            }

        } else if (listSize == 3) {
            res += compareTwoTokens(list1.get(0), list2.get(2));
            res += compareTwoTokens(list1.get(1), list2.get(0));
            res += compareTwoTokens(list1.get(2), list2.get(1));

            if (res < minDist) {
                minDist = res;
            }

            res = 0;

            res += compareTwoTokens(list1.get(2), list2.get(0));
            res += compareTwoTokens(list1.get(0), list2.get(1));
            res += compareTwoTokens(list1.get(1), list2.get(2));

            if (res < minDist) {
                minDist = res;
            }
        }

        return minDist;
    }

    /**
     * Compares lists of tokens in different configurations. Lists must have
     * different size.
     *
     * @param list1 first list of strings for comparison. Its size must equal 2
     * @param list2 second list of strings for comparison. Its size must equal 3
     * @return minimal distance between two lists of strings
     */
    public static int compareDifferentLengthLists(List<String> list1,
            List<String> list2) {
        //list1.size()==2 list2.size==3 -> only possibility
        int minDist = Integer.MAX_VALUE;
        int res = 0;  //temporary result

        res += compareTwoTokens(list1.get(0), list2.get(0));
        res += compareTwoTokens(list1.get(1), list2.get(1));
        res += list2.get(2).length();

        if (res < minDist) {
            minDist = res;
        }

        res = 0;
        res += compareTwoTokens(list1.get(0), list2.get(1));
        res += compareTwoTokens(list1.get(1), list2.get(0));
        res += list2.get(2).length();

        if (res < minDist) {
            minDist = res;
        }

        res = 0;
        res += compareTwoTokens(list1.get(0), list2.get(2));
        res += compareTwoTokens(list1.get(1), list2.get(0));
        res += list2.get(1).length();

        if (res < minDist) {
            minDist = res;
        }

        res = 0;
        res += compareTwoTokens(list1.get(1), list2.get(2));
        res += compareTwoTokens(list1.get(0), list2.get(0));
        res += list2.get(1).length();

        if (res < minDist) {
            minDist = res;
        }

        return minDist;
    }

    /**
     * Compares lists of tokens either of same size and different sizes.
     *
     * @param list1 first list for comparison
     * @param list2 second list for comparison
     * @return minimal distance between two lists of strings
     */
    public static int findMinDistance(List<String> list1, List<String> list2) {
        int minDist = Integer.MAX_VALUE;
        
        if ((list1.size()) == 2 && (list2.size() == 3)) {
            minDist = compareDifferentLengthLists(list1, list2);
        } else if ((list1.size()) == 3 && (list2.size() == 2)) {
            minDist = compareDifferentLengthLists(list2, list1);
        } else if (list1.size() == list2.size()) {
            minDist = compareSameLengthLists(list1, list2);
        }
        return minDist;
    }

    /**
     * Finds line number that contains most similar pharse to given data.
     *
     * @param filePath path to file that will be searched
     * @param pattern string that will be compared to every line in file
     * @return line that fits the best given pattern. -1 when file can not be
     * found
     */
    public static long findBestFitLineNumber(String filePath, String pattern) {
        long bestLineNumber = 1;
        long currentLineNumber = 1;
        int minDist = Integer.MAX_VALUE;
        int res;
        List<String> userTokens = tokenizeData(pattern);

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            
            List<String> lineTokens;
            String line;

            while ((line = br.readLine()) != null) {
                lineTokens = tokenizeData(line);
                res = findMinDistance(userTokens, lineTokens);

                if (res < minDist) {
                    minDist = res;
                    bestLineNumber = currentLineNumber;
                }

                ++currentLineNumber;
            }

        } catch (IOException e) {
            System.out.println("Podany plik nie isnieje");
            return -1;
        }

        return bestLineNumber;
    }

    /**
     * Splits data into tokens.
     *
     * @param data string that will be used for tokens creation
     * @return list of tokens
     */
    public static List<String> tokenizeData(String data) {
        return new ArrayList<>(Arrays.asList(data.trim().toLowerCase()
                .split("\\s+")));
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {

        long line = findBestFitLineNumber(args[0], args[1]);

        if (line != -1) {
            System.out.println("Linia : " + line);
        }
    }
}
