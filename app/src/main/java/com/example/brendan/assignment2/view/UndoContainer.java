package com.example.brendan.assignment2.view;

import com.example.brendan.assignment2.Exceptions.NoMoreUndosException;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by Brendan on 2/7/2016.
 */
public class UndoContainer<T> {

    private List<T> undos = new LinkedList<>();
    private int maxSize;

    public UndoContainer(){}

    public UndoContainer(int maxSize) {
        this.maxSize = maxSize;
    }

    public void clear() {
        undos.clear();
    }

    public void setMaxSize(int newMaxSize) {
        maxSize = newMaxSize;
        fitToMaxSize();
    }

    private void fitToMaxSize() {
        if (undos.size() > maxSize)
            undos = undos.subList(undos.size()-maxSize, undos.size());
    }

    public void append(T object) {
        undos.add(object);
        fitToMaxSize();
    }

    public T pop() {
        try {
            return undos.remove(undos.size()-1);
        } catch (IndexOutOfBoundsException e) {
            throw new NoMoreUndosException("No more undos in memory");
        }
    }

}
