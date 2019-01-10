/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.jrj.fnc;

import javax.ejb.Stateless;

/**
 *
 * @author milosz
 */
@Stateless
public class FunctMonitor implements IFunctMonitor {

    @Override
    public double f(double x, double y) {
        return 5*x*x*x+2*x*x-9*x-12+6*y*y*y+y*y-2*y-1.0/2;
        //return x * x * x + y * y + 23.5 + Math.sin(y);
    }
}
