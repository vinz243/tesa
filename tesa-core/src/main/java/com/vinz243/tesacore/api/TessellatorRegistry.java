package com.vinz243.tesacore.api;

import com.vinz243.tesacore.context.Context;
import com.vinz243.tesacore.transforms.TransformRepository;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class TessellatorRegistry {
    private final Map<UUID, Tessellator> tessellators = new HashMap<>();
    private final TransformRepository transformRepository;

    public TessellatorRegistry(TransformRepository transformRepository) {
        this.transformRepository = transformRepository;
    }

    public Tessellator getTessellator (Context context) {
        return tessellators.computeIfAbsent(context.getPlayer().getUniqueID(), x -> new Tessellator(transformRepository));
    }

    public boolean hasTessellator (Context context) {
        return tessellators.containsKey(context.getPlayer().getUniqueID());
    }
}
