package com.vinz243.tesavanilla;

import com.vinz243.tesacore.api.Tessellator;
import com.vinz243.tesacore.api.TessellatorRegistry;
import com.vinz243.tesacore.context.Context;
import com.vinz243.tesacore.helpers.Vector;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class VanillaTesaExecutor {
    private final TessellatorRegistry tessellatorRegistry;

    public VanillaTesaExecutor(TessellatorRegistry tessellatorRegistry) {
        this.tessellatorRegistry = tessellatorRegistry;
    }

    @SubscribeEvent
    public void onBlockPlaced(BlockEvent.PlaceEvent evt) {
        final Context context = new Context(evt.getPlayer(), evt.getWorld());

        final Tessellator tessellator = tessellatorRegistry.getTessellator(context);

        final BlockPos pos = evt.getPos();
        final IBlockState placedBlock = evt.getPlacedBlock();


        if (tessellator.isEnabled() && !tessellator.isEmpty()) {
            tessellator.apply(new Vector(pos), (position) -> {
                evt.getWorld().setBlockState(position.getVector().toBlockPos(), placedBlock);
            });
        }
    }

    @SubscribeEvent
    public void onBlockDestroyed(BlockEvent.BreakEvent evt) {
        final Context context = new Context(evt.getPlayer(), evt.getWorld());

        final Tessellator tessellator = tessellatorRegistry.getTessellator(context);

        final BlockPos pos = evt.getPos();

        if (tessellator.isEnabled() && !tessellator.isEmpty()) {
            tessellator.apply(new Vector(pos), (position) -> {
                evt.getWorld().destroyBlock(position.getVector().toBlockPos(), false);
            });
        }
    }

}
