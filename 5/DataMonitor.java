/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.jrj.data;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import javax.ejb.Stateful;

/**
 *
 * @author milosz
 */
@Stateful
public class DataMonitor implements IDataMonitor {

    int currentIndex = 0;
    List<Double> numbers;

    public DataMonitor() {
        numbers = new LinkedList();

        numbers.add(3.5);
        numbers.add(2.1);
        numbers.add(-1.4);

        Random generator = new Random();
        for (int i = 0; i < 500; i++) {
            numbers.add(generator.nextDouble() * 100 + 12);
            numbers.add(generator.nextDouble() * 100 + 12);
            numbers.add(generator.nextDouble() * 100 + 12);
            numbers.add(generator.nextDouble() * 35 + 4);
        }
    }

    @Override
    public boolean hasNext() {
        if (numbers.size() > currentIndex) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public double next() {
        return numbers.get(currentIndex++);
    }
}
