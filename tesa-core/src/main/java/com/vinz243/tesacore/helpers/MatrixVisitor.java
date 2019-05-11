package com.vinz243.tesacore.helpers;

@FunctionalInterface
public interface MatrixVisitor {
    void apply(int i, int j, double value);
}
