package com.vinz243.tesacore.transforms;

import com.vinz243.tesacore.helpers.Matrix;
import com.vinz243.tesacore.helpers.TransformResult;
import com.vinz243.tesacore.helpers.Vector;
import com.vinz243.tesacore.masks.MaskFactory;

import java.util.Collections;

public abstract class SimpleAffineTransform implements Transform {

    private final MaskFactory inputMaskFactory = new MaskFactory();
    private final MaskFactory outputMaskFactory = new MaskFactory();

    @Override
    public Iterable<TransformResult> apply(Vector in) {
        return Collections.singleton(new TransformResult(getTransform().multiply(in.subtract(getOffset())).add(getOffset()), in, getChiselTransform()));
    }

    abstract Matrix getTransform();

    abstract Vector getOffset();

    Matrix getChiselTransform() {
        return Matrix.IDENTITY_3;
    }

    @Override
    public MaskFactory getInputMaskFactory() {
        return inputMaskFactory;
    }

    @Override
    public MaskFactory getOutputMaskFactory() {
        return outputMaskFactory;
    }
}
