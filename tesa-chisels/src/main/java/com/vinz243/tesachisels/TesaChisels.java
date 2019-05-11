package com.vinz243.tesachisels;

import com.vinz243.tesacore.TesaCommand;
import com.vinz243.tesacore.TesaCommandManager;
import com.vinz243.tesacore.api.TessellatorRegistry;
import com.vinz243.tesacore.transforms.ReflectionTransform;
import com.vinz243.tesacore.transforms.TransformRepository;
import com.vinz243.tesacore.transforms.YRevolutionTransform;
import com.vinz243.tesacore.transforms.YSymTransform;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.common.Mod;

@Mod(
        modid = TesaChisels.MOD_ID,
        name = TesaChisels.MOD_NAME,
        version = TesaChisels.VERSION
)
public class TesaChisels {
    public static final String MOD_ID = "tesa-chisels";
    public static final String MOD_NAME = "Tesa Chisels";
    public static final String VERSION = "1.0-SNAPSHOT";

    private final TransformRepository transformRepository = new TransformRepository();

    /**
     * This is the instance of your mod as created by Forge. It will never be null.
     */
    @Instance(MOD_ID)
    public static TesaChisels INSTANCE;

    /**
     * This is the first initialization event. Register tile entities here.
     * The registry events below will have fired prior to entry to this method.
     */
    @Mod.EventHandler
    public void preinit(FMLPreInitializationEvent event) {
    }

    /**
     * This is the second initialization event. Register custom recipes
     */
    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        transformRepository
                .register("yrevolution", YRevolutionTransform.class)
                .register("yrev", YRevolutionTransform.class)
                .register("refl", ReflectionTransform.class)
                .register("reflection", ReflectionTransform.class)
                .register("ysim", YSymTransform.class);
    }


    @Mod.EventHandler
    public void serverStarting(FMLServerStartingEvent event){
        final TessellatorRegistry tessellatorRegistry = new TessellatorRegistry(transformRepository);
        final TesaCommandManager commandManager = new TesaCommandManager(tessellatorRegistry);
        event.registerServerCommand(new TesaCommand(commandManager));

        MinecraftForge.EVENT_BUS.register(new ChiselsTesaExecutor(tessellatorRegistry));
    }
}
