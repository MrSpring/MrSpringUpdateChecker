package dk.mrspring.testmod;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import dk.mrspring.updater.core.ModUpdater;
import dk.mrspring.updater.core.UpdatingMod;
import dk.mrspring.updater.forge.ForgeHandler;

/**
 * Created on 24-09-2015 for MrSpringUpdateChecker.
 */
@Mod(modid = "test", name = "Test Mod", version = "1.0.0")
public class TestMod
{
    @Mod.EventHandler
    public static void preInit(FMLPreInitializationEvent event)
    {
        ModUpdater.getInstance().registerMod(new UpdatingMod("The Kitchen Mod", "tkm", "1.3.14", event.getSourceFile()));
        ForgeHandler.onPreInit();
    }
}
