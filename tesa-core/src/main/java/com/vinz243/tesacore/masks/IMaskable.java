package com.vinz243.tesacore.masks;

public interface IMaskable {
    MaskFactory getInputMaskFactory();

    MaskFactory getOutputMaskFactory();
}
