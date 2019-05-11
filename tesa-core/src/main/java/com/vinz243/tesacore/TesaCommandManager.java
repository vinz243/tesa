package com.vinz243.tesacore;

import com.vinz243.tesacore.annotations.CursorTarget;
import com.vinz243.tesacore.api.Tessellator;
import com.vinz243.tesacore.api.TessellatorRegistry;
import com.vinz243.tesacore.context.CommandContext;
import com.vinz243.tesacore.exceptions.InvalidSyntaxException;
import com.vinz243.tesacore.helpers.TesaCursor;
import com.vinz243.tesacore.masks.ComposedMaskFactory;
import com.vinz243.tesacore.masks.IMaskable;
import com.vinz243.tesacore.masks.MaskFactory;
import com.vinz243.tesacore.transforms.NoSuchTransformException;
import com.vinz243.tesacore.transforms.Transform;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Optional;

public class TesaCommandManager {

    private final TessellatorRegistry registry;

    public TesaCommandManager(TessellatorRegistry registry) {
        this.registry = registry;
    }

    public void addTransform(String key, CommandContext context) throws NoSuchTransformException, InstantiationException, IllegalAccessException, InvocationTargetException {
        Tessellator tessellator = getTessellator(context);
        tessellator.addTransform(key, context);
    }

    private Tessellator getTessellator(CommandContext context) {
        return registry.getTessellator(context);
    }

    public void clearTransforms(CommandContext context) {
        getTessellator(context).clear();
    }

    public void setMask(CommandContext ct) throws InvalidSyntaxException {
        final Tessellator tessellator = getTessellator(ct);
        final String maskTarget = ct.getRemainingArgs()[0];

        final TesaCursor cursor = tessellator.getCursor();

        final Method[] declaredMethods = cursor.getClass().getDeclaredMethods();

        final Optional<Method> targetMethod = Arrays.stream(declaredMethods).filter((method) -> {
            try {
                final CursorTarget cursorTarget = method.getAnnotation(CursorTarget.class);

                return Arrays.asList(cursorTarget.name()).contains(maskTarget);
            } catch (NullPointerException ignored) {
            }
            return false;
        }).findFirst();

        targetMethod.orElseThrow(InvalidSyntaxException::new);

        final Method method = targetMethod.get();
        try {
            final IMaskable maskable = (IMaskable) method.invoke(cursor);

            final MaskFactory maskFactory;

            switch (ct.getRemainingArgs()[1]) {
                case "i":
                case "in":
                case "input":
                    maskFactory = maskable.getInputMaskFactory();
                    break;
                case "o":
                case "out":
                case "output":
                    maskFactory = maskable.getOutputMaskFactory();
                    break;
                case "b":
                case "both":
                    maskFactory = new ComposedMaskFactory(maskable.getInputMaskFactory(), maskable.getOutputMaskFactory());
                    break;
                default:
                    throw new InvalidSyntaxException("Use either input, output or both");
            }

            switch (ct.getRemainingArgs()[2]) {
                case "add":
                    maskFactory.add(ct.getPos1(), ct.getPos2());
                    break;
                case "sub":
                    maskFactory.sub(ct.getPos1(), ct.getPos2());
                    break;
                case "set":
                    maskFactory.set(ct.getPos1(), ct.getPos2());
                    break;
                case "int":
                    maskFactory.intersection(ct.getPos1(), ct.getPos2());
                    break;
            }

        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }

    }

    public void popTransform(CommandContext context) {
        getTessellator(context).pop();
    }

    public void disable(CommandContext context) {
        getTessellator(context).setEnabled(false);
    }

    public void enable(CommandContext context) {
        getTessellator(context).setEnabled(true);
    }

    public Iterable<Transform> getTransforms(CommandContext context) {
        return registry.getTessellator(context).getTransforms();
    }
}
