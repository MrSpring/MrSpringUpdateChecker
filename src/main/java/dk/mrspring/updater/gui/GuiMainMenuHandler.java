package dk.mrspring.updater.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraft.client.gui.GuiScreen;

import java.lang.reflect.Field;
import java.util.List;

/**
 * Created by MrSpring on 07-07-2015 for MC Music Player.
 */
public class GuiMainMenuHandler
{
    public static final int UPDATE_CHECKER_ID = 642;

    public static void addUpdateButton(List<GuiButton> buttonList)
    {
        buttonList.add(new GuiButton(UPDATE_CHECKER_ID, 5, 5, "Updater"));
    }

    /**
     * A helper method that automatically gets the button list.
     *
     * @param guiMainMenu     The main menu gui object
     * @param buttonListNames The possible names the buttonList field can have. 0 = MCP, 1 = SRG, 2 = OBF
     */
    public static void onInitMainMenuGui(GuiMainMenu guiMainMenu, String[] buttonListNames)
    {
        List<GuiButton> buttonList = getButtonList(guiMainMenu, buttonListNames);
        if (buttonList != null) addUpdateButton(buttonList);
    }

    private static List<GuiButton> getButtonList(GuiMainMenu guiMainMenu, String[] buttonListNames)
    {
        Field buttonList = getButtonListField(buttonListNames);
        if (buttonList == null)
            return null;
        buttonList.setAccessible(true);
        try
        {
            return (List<GuiButton>) buttonList.get(guiMainMenu);
        } catch (IllegalAccessException e)
        {
            e.printStackTrace();
        }
        return null;
    }

    private static Field getButtonListField(String[] buttonListNames)
    {
        Field[] fields = GuiScreen.class.getDeclaredFields();
        for (Field field : fields)
        {
            if (field.getName().equals(buttonListNames[0]))
                return field;
            else if (field.getName().equals(buttonListNames[1]))
                return field;
            else if (field.getName().equals(buttonListNames[2]))
                return field;
        }
        return null;
    }

    public static void onActionPerformed(GuiButton button)
    {
        if (button.enabled && button.id == UPDATE_CHECKER_ID)
            Minecraft.getMinecraft().displayGuiScreen(new GuiScreenUpdater());
    }
}