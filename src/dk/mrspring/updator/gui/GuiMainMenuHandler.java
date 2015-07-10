package dk.mrspring.updator.gui;

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

    public static String buttonListSRG = "field_146292_n";
    public static String buttonListOBF = "n";
    public static String buttonListMCP = "buttonList";

    public static void onInitMainMenuGui(GuiMainMenu guiMainMenu)
    {
        List<GuiButton> buttonList = getButtonList(guiMainMenu);
        if (buttonList == null)
        {
            return;
        }
        System.out.println("Adding");
        buttonList.add(new GuiButton(UPDATE_CHECKER_ID, 5, 5, "Updater"));
    }

    private static List<GuiButton> getButtonList(GuiMainMenu guiMainMenu)
    {
        Field buttonList = getButtonListField();
        if (buttonList == null)
            return null;
        System.out.println("Not null");
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

    private static Field getButtonListField()
    {
        Field[] fields = GuiScreen.class.getDeclaredFields();
        for (Field field : fields)
        {
            System.out.println("Trying field: " + field.getName());
            if (field.getName().equals(buttonListMCP))
                return field;
            else if (field.getName().equals(buttonListSRG))
                return field;
            else if (field.getName().equals(buttonListOBF))
                return field;
        }
        return null;
    }

    public static void onActionPerformed(GuiButton button)
    {
        if (button.id == UPDATE_CHECKER_ID)
        {
            System.out.println("Updater clicked!");
            Minecraft.getMinecraft().displayGuiScreen(new GuiScreenUpdater());
        }
        System.out.println("action performed");
    }
}
