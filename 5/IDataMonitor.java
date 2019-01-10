/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.jrj.data;

import javax.ejb.Remote;

/**
 *
 * @author milosz
 */
@Remote
public interface IDataMonitor {

    public boolean hasNext();

    public double next();
}
