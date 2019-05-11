package com.vinz243.tesachisels;

import com.vinz243.tesacore.VanillaTesaExecutor;
import com.vinz243.tesacore.api.Tessellator;
import com.vinz243.tesacore.api.TessellatorRegistry;
import com.vinz243.tesacore.context.Context;
import com.vinz243.tesacore.helpers.Vector;
import mod.chiselsandbits.api.*;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class ChiselsTesaExecutor {
    private final TessellatorRegistry tessellatorRegistry;
    private final VanillaTesaExecutor vanillaTesaExecutor;
    private final Set<UUID> lockedChisels = new HashSet<>();

    public ChiselsTesaExecutor(TessellatorRegistry tessellatorRegistry) {
        this.tessellatorRegistry = tessellatorRegistry;
        this.vanillaTesaExecutor = new VanillaTesaExecutor(tessellatorRegistry);
    }

    @SubscribeEvent
    public void onBlockPlaced(BlockEvent.PlaceEvent evt) {
        vanillaTesaExecutor.onBlockPlaced(evt);
    }

    @SubscribeEvent
    public void onBlockDestroyed(BlockEvent.BreakEvent evt) {
        vanillaTesaExecutor.onBlockDestroyed(evt);
    }

    @SubscribeEvent
    void onBitModification(EventBlockBitModification event) {
        lockedChisels.remove(event.getPlayer().getUniqueID());
    }

    @SubscribeEvent
    void onChiselEdited(EventBlockBitPostModification event) {
        final BlockPos pos = event.getPos();
        final EntityPlayer closestPlayer = event.getWorld().getClosestPlayer(pos.getX(), pos.getY(), pos.getZ(), 20, false);

        if (closestPlayer == null) return;

        if (lockedChisels.contains(closestPlayer.getUniqueID())) {
            return;
        }

        final Context context = new Context(closestPlayer, event.getWorld());

        if (!tessellatorRegistry.hasTessellator(context)) {
            return;
        }

        final Tessellator tessellator = tessellatorRegistry.getTessellator(context);

        if (!tessellator.isEnabled()) {
            return;
        }

        lockedChisels.add(closestPlayer.getUniqueID());

        vanillaTesaExecutor.updateTracker(tessellator, context);

        tessellator.apply(new Vector(pos), (result) -> {
            IChiselAndBitsAPI bitsAPI = ChiselAPIAccess.apiInstance;
            try {
                IBitAccess outAccess = bitsAPI.getBitAccess(event.getWorld(), result.getVector().toBlockPos());

                IBitAccess inAccess = bitsAPI.getBitAccess(event.getWorld(), pos);

                inAccess.visitBits((x, y, z, brush) -> {
                    Vector outPos = result.getChiselTransform().multiply(new Vector(x, y, z).add(-7.5)).add(7.5);
                    try {
                        outAccess.setBitAt((int) Math.round(outPos.getX()), (int) Math.round(outPos.getY()), (int) Math.round(outPos.getZ()), brush);
                    } catch (APIExceptions.SpaceOccupied spaceOccupied) {
                        spaceOccupied.printStackTrace();
                    }
                    return brush;
                });

                outAccess.commitChanges(true);
            } catch (APIExceptions.CannotBeChiseled cannotBeChiseled) {
                cannotBeChiseled.printStackTrace();
            }
        });

    }

}
