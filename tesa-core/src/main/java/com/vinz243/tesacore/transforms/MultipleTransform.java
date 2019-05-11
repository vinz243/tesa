package com.vinz243.tesacore.transforms;

import com.vinz243.tesacore.helpers.TransformResult;
import com.vinz243.tesacore.helpers.Vector;
import com.vinz243.tesacore.masks.MaskFactory;

import java.util.stream.Collectors;
import java.util.stream.IntStream;

public abstract class MultipleTransform implements Transform {
    private final MaskFactory outputMaskFactory = new MaskFactory();
    private final MaskFactory inputMaskFactory = new MaskFactory();

    @Override
    public Iterable<TransformResult> apply(Vector in) {
        return IntStream.range(0, getIterations()).mapToObj(i -> transform(i, in)).collect(Collectors.toList());
    }

    abstract int getIterations();

    abstract TransformResult transform(int i, Vector in);

    @Override
    public MaskFactory getInputMaskFactory() {
        return inputMaskFactory;
    }

    @Override
    public MaskFactory getOutputMaskFactory() {
        return outputMaskFactory;
    }

}
