package com.vinz243.tesacore.transforms;

import com.vinz243.tesacore.annotations.Coordinates;
import com.vinz243.tesacore.annotations.InstantiableTransform;
import com.vinz243.tesacore.annotations.Source;
import com.vinz243.tesacore.helpers.Matrix;
import com.vinz243.tesacore.helpers.Vector;

public class YSymTransform extends MultipleAffineTransform {

    private final Vector axis;
    private final Matrix matrix;

    @InstantiableTransform
    public YSymTransform(
            @Coordinates(from = Source.Player) Vector axis
    ) {
        this.axis = axis.multiply(1, 0, 1);
        this.matrix = new Matrix(
                -1, 0, 0,
                0, 1, 0,
                0, 0, -1
        );
    }

    @Override
    Vector getOffset(int i) {
        return this.axis;
    }

    @Override
    Matrix getMatrix(int i) {
        return matrix;
    }

    @Override
    Matrix getChiselMatrix(int i) {
        return matrix;
    }

    @Override
    public String toString() {
        return "YSymTransform{" +
                "axis=" + axis +
                ", matrix=" + matrix +
                '}';
    }

    @Override
    int getIterations() {
        return 1;
    }
}
