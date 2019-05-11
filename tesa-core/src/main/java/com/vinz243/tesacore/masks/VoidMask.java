package com.vinz243.tesacore.masks;

import com.vinz243.tesacore.helpers.Vector;

public class VoidMask implements Mask {
    @Override
    public boolean test(Vector pos) {
        return true;
    }
}
