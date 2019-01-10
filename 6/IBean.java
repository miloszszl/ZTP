/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import javax.ejb.Stateless;
import javax.naming.Context;
import pl.jrj.fnc.IFunctMonitor;
import javax.naming.InitialContext;
import javax.naming.NamingException;

/**
 *
 * @author milosz
 */
@Stateless
public class IBean implements IBeanRemote {

    private IFunctMonitor functMonitor;

    /**
     * Finds and retrieves FunctMonitor EJB
     * 
     * @return IFunctMonitor EJB
     */
    private IFunctMonitor retrieveFunctionMonitor() {
        try {
            Context ctx = new InitialContext();
            String name = "java:global/ejb-project/FunctMonitor"
                    + "!pl.jrj.fnc.IFunctMonitor";
            return (IFunctMonitor) ctx.lookup(name);
        } catch (NamingException ex) {
            return null;
        }
    }

    /**
     * Calculates double integral and rounds result to "n" decimal places
     *
     * @param a upper bound of interval for second integral
     * @param b upper bound of interval for first integral
     * @param n precision (10^(-n))
     * @return double integral value rounded up to "n" decimal places
     */
    @Override
    public double solve(double a, double b, int n) {
        functMonitor = retrieveFunctionMonitor();
        if (functMonitor == null) {
            return Double.NaN;
        }
        double result = calculateIntegralUsingTrapezoidRule(a, b);
        return round(result, n);
    }

    /**
     * Calculates double integral using trapezoid rule
     *
     * @param a upper bound of interval for second integral
     * @param b upper bound of interval for first integral
     * @return double integral value (volume)
     */
    private double calculateIntegralUsingTrapezoidRule(double a, double b) {
        int sections = 220;
        double totalVolume = 0.0;
        double sectionVolume;
        double deltaX = b / sections;
        double deltaY = a / sections;

        for (int i = 0; i < sections; i++) {
            for (int j = 0; j < sections; j++) {
                sectionVolume = ((functMonitor.f(i * deltaX, j * deltaY)
                        + functMonitor.f((i + 1) * deltaX, j * deltaY)
                        + functMonitor.f(i * deltaX, (j + 1) * deltaY)
                        + functMonitor.f((i + 1) * deltaX, (j + 1) * deltaY))
                        / 4.0) * deltaX * deltaY;
                totalVolume += sectionVolume;
            }
        }

        return totalVolume;
    }

    /**
     * Rounds value to "precision" number of decimal places
     *
     * @param value value to be rounded
     * @param precision number of decimal places
     * @return value rounded up to "precision" decimal places
     */
    private double round(double value, int precision) {
        double scale = Math.pow(10.0, precision);
        double roudedResult = Math.round(value * scale) / scale;
        return roudedResult;
    }
}
