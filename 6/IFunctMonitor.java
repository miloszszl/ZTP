/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.jrj.fnc;

import javax.ejb.Remote;

@Remote
public interface IFunctMonitor {

    /**
     * Returns value of certain function
     *
     * @param x x parameter for function
     * @param y y parameter for function
     * @return value of certain function
     */
    public double f(double x, double y);
}
