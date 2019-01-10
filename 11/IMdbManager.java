/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.jrj.mdb;

import javax.ejb.Remote;

/**
 *
 * @author milosz
 */
@Remote
public interface IMdbManager {

    /**
     * Gives three 3-letter currency code
     *
     * @return currency code
     */
    public String currencyId(); // trzyliterowy kod waluty
}
