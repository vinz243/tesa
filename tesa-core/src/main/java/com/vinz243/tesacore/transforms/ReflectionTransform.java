package com.vinz243.tesacore.transforms;

import com.vinz243.tesacore.annotations.Coordinates;
import com.vinz243.tesacore.annotations.Direction;
import com.vinz243.tesacore.annotations.InstantiableTransform;
import com.vinz243.tesacore.helpers.Axis;
import com.vinz243.tesacore.helpers.Matrix;
import com.vinz243.tesacore.helpers.Vector;

public class ReflectionTransform extends SimpleAffineTransform {

    private final Matrix matrix;
    private final Vector offset;

    @InstantiableTransform
    public ReflectionTransform(@Direction Axis direction, @Coordinates Vector pos) {
        if (direction == Axis.X) {
            this.matrix = new Matrix(
                    -1, 0, 0,
                    0, 1, 0,
                    0, 0, 1
            );
        } else {
            this.matrix = new Matrix(
                    1, 0, 0,
                    0, 1, 0,
                    0, 0, -1
            );
        }

        this.offset = pos;
    }

    @Override
    Matrix getTransform() {
        return matrix;
    }

    @Override
    Matrix getChiselTransform() {
        return matrix;
    }

    @Override
    Vector getOffset() {
        return offset;
    }

    @Override
    public String toString() {
        return "ReflectionTransform{" +
                "matrix=" + matrix +
                ", offset=" + offset +
                '}';
    }

}
