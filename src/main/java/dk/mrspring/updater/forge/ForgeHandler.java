package dk.mrspring.updater.forge;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import dk.mrspring.updater.gui.GuiMainMenuHandler;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.common.MinecraftForge;

/**
 * Created on 24-09-2015 for MrSpringUpdateChecker.
 */
public class ForgeHandler
{
    private static boolean shouldInit = true;

    public static void onPreInit()
    {
        if (shouldInit)
        {
            MinecraftForge.EVENT_BUS.register(new ForgeHandler());
            shouldInit = false;
        }
    }

    @SubscribeEvent
    public void onGuiInit(GuiScreenEvent.InitGuiEvent event)
    {
        if (event.gui.getClass() == GuiMainMenu.class)
            GuiMainMenuHandler.addUpdateButton(event.buttonList);
    }

    @SubscribeEvent
    public void onActionPerformed(GuiScreenEvent.ActionPerformedEvent event)
    {
        GuiMainMenuHandler.onActionPerformed(event.button);
    }
}
