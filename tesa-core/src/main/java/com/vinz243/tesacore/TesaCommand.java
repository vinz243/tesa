package com.vinz243.tesacore;

import com.sk89q.worldedit.IncompleteRegionException;
import com.sk89q.worldedit.LocalSession;
import com.sk89q.worldedit.WorldEdit;
import com.sk89q.worldedit.regions.Region;
import com.vinz243.tesacore.context.CommandContext;
import com.vinz243.tesacore.exceptions.InvalidSyntaxException;
import com.vinz243.tesacore.helpers.StringComponent;
import com.vinz243.tesacore.helpers.Vector;
import com.vinz243.tesacore.transforms.NoSuchTransformException;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;

import javax.annotation.Nullable;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class TesaCommand extends CommandBase {
    private final TesaCommandManager tesaManager;

    public TesaCommand(TesaCommandManager tesaCommandManager) {
        this.tesaManager = tesaCommandManager;
    }

    @Override
    public String getName() {
        return "tesa";
    }

    @Override
    public String getUsage(ICommandSender sender) {
        return "/tesa <clear|add|visu|mask|pop> <args>";
    }

    @Override
    public List<String> getAliases() {
        return new ArrayList<>();
    }


    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
        if (args.length == 0) {
            showHelp(sender);

            return;
        }
        switch (args[0]) {
            case "disable":
                tesaManager.disable(getContext(sender, args, 1));
                return;
            case "enable":
                tesaManager.enable(getContext(sender, args, 1));
                return;
            case "pop":
                tesaManager.popTransform(getContext(sender, args, 1));
                return;
            case "list":
                tesaManager.getTransforms(getContext(sender, args, 0)).forEach((tr) -> {
                    sender.sendMessage(new StringComponent(tr.toString()));
                });
                return;
            case "clear":
                tesaManager.clearTransforms(getContext(sender, args, 0));
                sender.sendMessage(new StringComponent("Cleared tessellator!"));
                return;
            case "add":
                final String transformName = args[1];
                try {
                    tesaManager.addTransform(transformName,
                            getContext(sender, args, 1));
                } catch (NoSuchTransformException e) {
                    sender.sendMessage(new StringComponent("Unable to find transform %s", transformName));
                } catch (InstantiationException | InvocationTargetException | IllegalAccessException e) {
                    e.printStackTrace();
                    sender.sendMessage(new StringComponent("Unable to instantiate transform"));
                }
                return;
            case "mask":
                final CommandContext ct = getContext(sender, args, 1);
                try {
                    tesaManager.setMask(ct);
                } catch (InvalidSyntaxException e) {
                    e.printStackTrace();
                    sender.sendMessage(new StringComponent("Invalid syntax, use: /tesa mask <transform|group|global> <input|output|both> <set|add|sub|int>"));
                }
                return;
        }
    }

    private void showHelp(ICommandSender sender) {
        sender.sendMessage(new StringComponent("Usage : /tesa [add|pop|clear|list|enable|disable|mask]"));
    }

    private CommandContext getContext(ICommandSender sender, String[] args, int from) {
        Entity entity = Objects.requireNonNull(sender.getCommandSenderEntity());
        WorldEdit instance = WorldEdit.getInstance();

        LocalSession session = instance.getSessionManager().findByName(entity.getName());

        Region selection;
        try {
            selection = session.getSelection(session.getSelectionWorld());
        } catch (IncompleteRegionException | NullPointerException e) {
            return new CommandContext(
                    (EntityPlayer) sender.getCommandSenderEntity(),
                    sender.getEntityWorld(),
                    Arrays.copyOfRange(args, from, args.length),
                    null, null);
        }
        return new CommandContext(
                (EntityPlayer) sender.getCommandSenderEntity(),
                sender.getEntityWorld(),
                Arrays.copyOfRange(args, from, args.length),
                getVector(selection.getMinimumPoint()),
                getVector(selection.getMaximumPoint())
        );
    }

    @Override
    public boolean checkPermission(MinecraftServer server, ICommandSender sender) {
        return true;
    }

    @Override
    public List<String> getTabCompletions(MinecraftServer server, ICommandSender sender, String[] args, @Nullable BlockPos targetPos) {
        return null;
    }

    @Override
    public boolean isUsernameIndex(String[] args, int index) {
        return false;
    }

    @Override
    public int compareTo(ICommand o) {
        return 0;
    }

    Vector getVector(com.sk89q.worldedit.Vector v) {
        return new Vector(v.getX(), v.getY(), v.getZ());
    }
}
