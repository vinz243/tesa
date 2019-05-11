package com.vinz243.tesacore.transforms;

import com.vinz243.tesacore.helpers.TransformResult;
import com.vinz243.tesacore.helpers.Vector;
import com.vinz243.tesacore.masks.IMaskable;

public interface Transform extends IMaskable {
    Iterable<TransformResult> apply(Vector in);

}
