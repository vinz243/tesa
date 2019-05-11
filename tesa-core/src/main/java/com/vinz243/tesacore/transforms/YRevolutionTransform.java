package com.vinz243.tesacore.transforms;

import com.vinz243.tesacore.annotations.Coordinates;
import com.vinz243.tesacore.annotations.InstantiableTransform;
import com.vinz243.tesacore.annotations.IntParam;
import com.vinz243.tesacore.annotations.Source;
import com.vinz243.tesacore.helpers.Matrix;
import com.vinz243.tesacore.helpers.Vector;
import com.vinz243.tesacore.helpers.YRotationMatrix;

public class YRevolutionTransform extends MultipleAffineTransform {

    private Vector axis;
    private int count;

    @InstantiableTransform
    public YRevolutionTransform(
            @Coordinates(from = Source.Player) Vector axis,
            @IntParam(required = false, defaultValue = 3, min = 1, max = 3) int count
    ) {
        this.axis = axis.multiply(1, 0, 1);
        this.count = count;
    }

    @Override
    Vector getOffset(int i) {
        return this.axis;
    }

    @Override
    Matrix getMatrix(int i) {
        return new YRotationMatrix((i + 1) * Math.PI / 2);
    }

    @Override
    Matrix getChiselMatrix(int i) {
        return new YRotationMatrix((i + 1) * Math.PI / 2);
    }

    @Override
    int getIterations() {
        return count;
    }

    @Override
    public String toString() {
        return "YRevolutionTransform{" +
                "axis=" + axis +
                '}';
    }

}
