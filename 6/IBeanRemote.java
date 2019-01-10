/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import javax.ejb.Remote;

/**
 *
 * @author milosz
 */
@Remote
public interface IBeanRemote {

    /**
     * Calculates double integral and rounds result to "n" decimal places
     *
     * @param a upper bound of interval for second integral
     * @param b upper bound of interval for first integral
     * @param n precision (10^(-n))
     * @return double integral value rounded up to "n" decimal places
     */
    double solve(double a, double b, int n);
}
