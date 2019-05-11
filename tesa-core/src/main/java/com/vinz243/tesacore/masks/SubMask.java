package com.vinz243.tesacore.masks;

import com.vinz243.tesacore.helpers.Vector;

public class SubMask extends ComposedMask {
    public SubMask(Mask a, Mask b) {
        super(a, b);
    }

    @Override
    public boolean test(Vector pos) {
        return a.test(pos) && !b.test(pos);
    }
}

