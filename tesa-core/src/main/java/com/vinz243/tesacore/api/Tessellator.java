package com.vinz243.tesacore.api;

import com.vinz243.tesacore.context.CommandContext;
import com.vinz243.tesacore.helpers.Matrix;
import com.vinz243.tesacore.helpers.TesaCursor;
import com.vinz243.tesacore.helpers.TransformResult;
import com.vinz243.tesacore.helpers.Vector;
import com.vinz243.tesacore.masks.*;
import com.vinz243.tesacore.transforms.NoSuchTransformException;
import com.vinz243.tesacore.transforms.Transform;
import com.vinz243.tesacore.transforms.TransformRepository;

import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.function.Consumer;

public class Tessellator implements IMaskable {
    private List<Transform> transforms = new ArrayList<>();
    private boolean chiselLocked = false;
    private boolean enabled = true;
    private Set<Integer> debouncedBlocks = new HashSet<>();

    private final MaskFactory inputMaskFactory = new MaskFactory();
    private final MaskFactory outputMaskFactory = new MaskFactory();

    private final TesaCursor cursor = new TesaCursor();
    private final TransformRepository transformRepository;

    public Tessellator(TransformRepository transformRepository) {
        this.transformRepository = transformRepository;
        cursor.setCurrentTessellator(this);
    }

    public boolean isEmpty () {
        return transforms.isEmpty();
    }

    public TesaCursor getCursor() {
        return cursor;
    }

    void debouncedApply(Vector pos, Consumer<TransformResult> applyFunction) {

        if (debouncedBlocks.remove(pos.blockPosHashCode())) {
            return;
        }
        apply(pos, (res) -> {
            debouncedBlocks.add(res.getVector().blockPosHashCode());
            applyFunction.accept(res);
        });
    }

    public void apply(Vector pos, Consumer<TransformResult> applyFunction) {
        final Set<Integer> placedBlocks = new HashSet<>();
        placedBlocks.add(pos.blockPosHashCode());
        apply(pos, applyFunction, transforms, placedBlocks);
    }

    private void apply(Vector pos, Consumer<TransformResult> applyFunction, List<Transform> transforms, Set<Integer> placedBlocks) {
        Map<Vector, TransformResult> results = new HashMap<>();
        results.put(pos, new TransformResult(pos, pos, Matrix.IDENTITY_3));

        for (Transform transform : transforms) {

            final Mask inputMask = new ComposedMaskFactory(getInputMaskFactory(), transform.getInputMaskFactory()).get();
            final Mask outputMask = new ComposedMaskFactory(getOutputMaskFactory(), transform.getOutputMaskFactory()).get();

            new HashMap<>(results).forEach((key, value) -> {

                if (!inputMask.test(key)) return;

                transform.apply(value.getVector()).forEach((result) -> {
                    final Matrix transform1 = result.getChiselTransform().multiply(value.getChiselTransform());
                    final TransformResult transformResult = new TransformResult(result.getVector(), result.getInput(), transform1);
                    transformResult.setMasked(!outputMask.test(result.getVector()));
                    results.merge(result.getVector(), transformResult, TransformResult::new);
                });
            });
        }

        results.values().forEach(r -> {
            if (!r.isMasked()) applyFunction.accept(r);
        });
    }


    public boolean isChiselLocked() {
        return chiselLocked;
    }

    public void setChiselLocked(boolean chiselLocked) {
        this.chiselLocked = chiselLocked;
    }


    public void clear() {
        transforms.clear();
        cursor.setCurrentTransform(null);
        inputMaskFactory.set(new VoidMask());
        outputMaskFactory.set(new VoidMask());
    }

    public void addTransform(Transform transform) {
        transforms.add(transform);
        cursor.setCurrentTransform(transform);
    }

    public Transform addTransform(String key, CommandContext context) throws InvocationTargetException, NoSuchTransformException, InstantiationException, IllegalAccessException {
        final Transform instantiate = transformRepository.instantiate(key, context);
        addTransform(instantiate);
        return instantiate;
    }

    public List<Transform> getTransforms() {
        return Collections.unmodifiableList(transforms);
    }

    @Override
    public MaskFactory getInputMaskFactory() {
        return inputMaskFactory;
    }

    @Override
    public MaskFactory getOutputMaskFactory() {
        return outputMaskFactory;
    }


    public void pop() {
        transforms.remove(transforms.size() - 1);
        cursor.setCurrentTransform(transforms.get(transforms.size() - 1));
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }
}
