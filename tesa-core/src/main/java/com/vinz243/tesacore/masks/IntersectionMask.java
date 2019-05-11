package com.vinz243.tesacore.masks;


import com.vinz243.tesacore.helpers.Vector;

public class IntersectionMask extends ComposedMask {
    public IntersectionMask(Mask a, Mask b) {
        super(a, b);
    }

    @Override
    public boolean test(Vector pos) {
        return a.test(pos) && b.test(pos);
    }
}
