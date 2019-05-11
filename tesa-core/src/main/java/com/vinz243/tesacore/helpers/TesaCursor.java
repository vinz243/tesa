package com.vinz243.tesacore.helpers;

import com.vinz243.tesacore.api.Tessellator;
import com.vinz243.tesacore.annotations.CursorTarget;
import com.vinz243.tesacore.transforms.Transform;

public class TesaCursor {
    private Transform currentTransform;
    private Tessellator currentTessellator;

    public TesaCursor(Transform currentTransform, Tessellator currentTessellator) {
        this.currentTransform = currentTransform;
        this.currentTessellator = currentTessellator;
    }

    public TesaCursor() {
    }

    @CursorTarget(name = {"t", "transform", "tf"})
    public Transform getCurrentTransform() {
        return currentTransform;
    }

    public void setCurrentTransform(Transform currentTransform) {
        this.currentTransform = currentTransform;
    }

    @CursorTarget(name = {"g", "tessellator", "tess", "global"})
    public Tessellator getCurrentTessellator() {
        return currentTessellator;
    }

    public void setCurrentTessellator(Tessellator currentTessellator) {
        this.currentTessellator = currentTessellator;
    }

    //private Group currentGroup;
}
