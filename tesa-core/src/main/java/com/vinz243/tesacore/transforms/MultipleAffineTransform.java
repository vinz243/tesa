package com.vinz243.tesacore.transforms;


import com.vinz243.tesacore.helpers.Matrix;
import com.vinz243.tesacore.helpers.TransformResult;
import com.vinz243.tesacore.helpers.Vector;

public abstract class MultipleAffineTransform extends MultipleTransform {

    abstract Vector getOffset(int i);

    abstract Matrix getMatrix(int i);

    Matrix getChiselMatrix(int i) {
        return Matrix.IDENTITY_3;
    }

    @Override
    TransformResult transform(int i, Vector in) {
        Vector offset = getOffset(i);
        return new TransformResult(
                getMatrix(i).multiply(in.subtract(offset)).add(offset),
                in, getChiselMatrix(i)
        );
    }
}
