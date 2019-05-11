package com.vinz243.tesacore;

import com.vinz243.tesacore.api.Tessellator;
import com.vinz243.tesacore.api.TessellatorRegistry;
import com.vinz243.tesacore.context.Context;
import com.vinz243.tesacore.helpers.Vector;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class VanillaTesaExecutor {
    private final TessellatorRegistry tessellatorRegistry;

    public VanillaTesaExecutor(TessellatorRegistry tessellatorRegistry) {
        this.tessellatorRegistry = tessellatorRegistry;
    }

    public void updateTracker (Tessellator tessellator, Context context) {
        final UsageTracker usageTracker = tessellator.getUsageTracker();

        if (usageTracker.getTimeDelta() > 2 * 1000) {
            context.getPlayer().sendMessage(new TextComponentString("Tesa is still enabled !"));
        }

        usageTracker.refresh();
    }

    @SubscribeEvent
    public void onBlockPlaced(BlockEvent.PlaceEvent evt) {
        final Context context = new Context(evt.getPlayer(), evt.getWorld());

        if (!tessellatorRegistry.hasTessellator(context)) {
            return;
        }

        final Tessellator tessellator = tessellatorRegistry.getTessellator(context);

        final BlockPos pos = evt.getPos();
        final IBlockState placedBlock = evt.getPlacedBlock();


        if (tessellator.isEnabled() && !tessellator.isEmpty()) {
            updateTracker(tessellator, context);

            tessellator.apply(new Vector(pos), (position) -> {
                evt.getWorld().setBlockState(position.getVector().toBlockPos(), placedBlock);
            });
        }
    }

    @SubscribeEvent
    public void onBlockDestroyed(BlockEvent.BreakEvent evt) {
        final Context context = new Context(evt.getPlayer(), evt.getWorld());

        if (!tessellatorRegistry.hasTessellator(context)) {
            return;
        }

        final Tessellator tessellator = tessellatorRegistry.getTessellator(context);

        final BlockPos pos = evt.getPos();

        if (tessellator.isEnabled() && !tessellator.isEmpty()) {
            updateTracker(tessellator, context);
            tessellator.apply(new Vector(pos), (position) -> {
                evt.getWorld().destroyBlock(position.getVector().toBlockPos(), false);
            });
        }
    }

}
