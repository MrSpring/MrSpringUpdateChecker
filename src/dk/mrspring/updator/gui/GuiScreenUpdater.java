package dk.mrspring.updator.gui;

import dk.mrspring.updater.core.UpdateChecker;
import dk.mrspring.updater.core.UpdatingMod;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.Tessellator;
import org.lwjgl.BufferUtils;

import java.io.IOException;
import java.nio.DoubleBuffer;
import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.opengl.GL11.*;

/**
 * Created by MrSpring on 08-07-2015 for MC Music Player.
 */
public class GuiScreenUpdater extends GuiScreen
{
    List<UpdatingMod> mods;
    List<ListEntry> entries;
    UpdatingMod inFocus;
    DropDown versionSelector;
    int _listEntryHeight = 50;
    int _entryPadding = 5;

    @Override
    public void initGui()
    {
        super.initGui();
        mods = UpdateChecker.getInstance().getRegisteredMods();
        entries = new ArrayList<ListEntry>();
        for (UpdatingMod mod : mods)
            entries.add(new ListEntry(mod));
        inFocus = mods.get(0);
        versionSelector = new DropDown(inFocus);
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks)
    {
        super.drawScreen(mouseX, mouseY, partialTicks);
        this.drawDefaultBackground();
        int xOffset = 0;
        if (mods.size() > 1)
            xOffset += 120;
        drawRect(10, xOffset, 0, height, 0F, 0F, 0F, 0.5F);
        glPushMatrix();
        glTranslatef(10, -getListHeight() / 2 + height / 2, 0);
        for (ListEntry entry : entries)
        {
            glPushMatrix();
            entry.draw();
            glPopMatrix();
            glTranslatef(0, _listEntryHeight + _entryPadding, 0);
        }
        glPopMatrix();

        glPushMatrix();
        int width = this.width - 20 - xOffset;
        int height = this.height - 20;
        glTranslatef(xOffset + 10, 10, 0);

        glPushMatrix();
        glScalef(2F, 2F, 2F);
        drawCenteredSplitString(inFocus.getDisplayName(), width / 4, 10, width, 0xFFFFFF, true);
        glPopMatrix();
        boolean latest = inFocus.hasLatest();
        drawCenteredSplitString(latest ?
                "Has latest version!" :
                "Newer version available!", width / 2, 40, width, latest ?
                0x00FF00 : 0xFF0000, true);

//        String[] chages = inFocus.getLatestChangelog();
//        for (int i = 0; i < chages.length; i++)
//        {
//            String str = chages[i];
//            mc.fontRendererObj.drawString(str, 10, 60 + (i * 12), 0xFFFFFF, true);
//        }

        int offset = 66;
//        drawRect(0, width, offset, height, 1F, 1F, 1F, 0.1F);

        glTranslatef(0, offset, 0);

        if (height - offset > width)
            drawTall(width, height - offset, mouseX - xOffset - 10, mouseY - offset - 10);
        else drawWide(width, height - offset, mouseX - xOffset - 10, mouseY - offset - 10);

        glPopMatrix();
    }

    private void drawTall(int width, int height, int mouseX, int mouseY)
    {
        int third = width / 3;

        drawCenteredSplitString("Current Version:\n" + inFocus.getCurrent(), third, 0, third, 0xFFFFFF, true);
        drawCenteredSplitString("Latest Version:\n" + inFocus.getLatest(), third * 2, 0, third, 0xFFFFFF, true);

        drawChangeLog(10, width - 10, 30, height - 60);
        versionSelector.draw(10, width - 10, 30, height, 0, height - 40, false, mouseX, mouseY);
    }

    private void drawChangeLog(int left, int right, int top, int bottom)
    {
        float alpha = 0.5F;
//        drawRect(11, width - 11, 31, height - 11, 0, 0, 0, 0, alpha);
//        drawRect(13, width - 13, 30, 31, 0, 0, 0, 0, alpha);
//        drawRect(10, 11, 33, height - 13, 0, 0, 0, 0, alpha);
//        drawRect(width - 11, width - 10, 33, height - 13, 0, 0, 0, 0, alpha);
//        drawRect(13, width - 13, height - 11, height - 10, 0, 0, 0, 0, alpha);

//        drawRect(left + 1, right - 1, top + 1, bottom - 1, 0F, 0F, 0F, alpha);
//        drawRect(left + 3, right - 3, top, top + 1, 0F, 0F, 0F, alpha);
//        drawRect(left + 3, right - 3, bottom - 1, bottom, 0F, 0F, 0F, alpha);
//        drawRect(left, left + 1, top + 3, bottom - 3, 0F, 0F, 0F, alpha);
//        drawRect(right - 1, right, top + 3, bottom - 3, 0F, 0F, 0F, alpha);

        int width = right - left;
        drawCenteredSplitString("Changelog:", left + (width / 2), top + 3, width, 0xFFFFFF, true);

        top += 19;

        drawRect(left, right, top, bottom, 0F, 0F, 0F, alpha);
        String[] changeLog = inFocus.getLatestChangelog();

        enableClippingPane(GL_CLIP_PLANE0, false, -top);
        enableClippingPane(GL_CLIP_PLANE1, true, bottom);

        glPushMatrix();
        boolean dark = true;
        for (String str : changeLog)
        {
            List<String> split = mc.fontRendererObj.listFormattedStringToWidth(str, right - left);
            int height = (split.size() * mc.fontRendererObj.FONT_HEIGHT) + 6;
//            float[] color = dark ? new float[]{0F, 0F, 0F, 0.2F} : new float[]{1F, 1F, 1F, 0.05F};
//            drawRect(left, right, top, top + height, color);
            for (String line : split)
                mc.fontRendererObj.drawString(line, left + 4, top + 3, 0xFFFFFF, true);
            dark = !dark;
            glTranslatef(0, height, 0);
        }
        glPopMatrix();

        glDisable(GL_CLIP_PLANE0);
        glDisable(GL_CLIP_PLANE1);
    }

    private void enableClippingPane(int plane, boolean flip, int yValue)
    {
        DoubleBuffer buffer = BufferUtils.createDoubleBuffer(8).put(new double[]{0, flip ? -1 : 1, 0, yValue});
        buffer.flip();
        glClipPlane(plane, buffer);
        glEnable(plane);
    }

    private void drawWide(int width, int height, int mouseX, int mouseY)
    {

    }

    private void drawCenteredSplitString(String drawing, int x, int y, int wrap, int color, boolean shadow)
    {
//        int textWidth = mc.fontRendererObj.getStringWidth(drawing);
//        if (shadow)
//        {
//            int sColor = (color & 16579836) >> 2 | color & -16777216;
//            mc.fontRendererObj.drawSplitString(drawing, x - (textWidth / 2) + 1, y + 1, wrap, sColor);
//        }
//        mc.fontRendererObj.drawSplitString(drawing, x - (textWidth / 2), y, wrap, color);
        List<String> split = mc.fontRendererObj.listFormattedStringToWidth(drawing, wrap);
        int offset = 0;
        for (String line : split)
        {
            int textWidth = mc.fontRendererObj.getStringWidth(line);
            if (shadow)
            {
                int sColor = (color & 16579836) >> 2 | color & -16777216;
                mc.fontRendererObj.drawSplitString(line, x - (textWidth / 2) + 1, y + 1 + offset, wrap, sColor);
            }
            mc.fontRendererObj.drawSplitString(line, x - (textWidth / 2), y + offset, wrap, color);
            offset += mc.fontRendererObj.FONT_HEIGHT;
        }
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException
    {
        super.mouseClicked(mouseX, mouseY, mouseButton);
        if (isMouseInBounds(mouseX, mouseY, 10, 120, 0, height))
        {
            System.out.println("Click in list");
        } else versionSelector.mouseDown(mouseX, mouseY, mouseButton);
    }

    private boolean isMouseInBounds(int mouseX, int mouseY, int left, int right, int top, int bottom)
    {
        return mouseX < right && mouseX > left && mouseY < bottom && mouseY > top;
    }

    private int getListHeight()
    {
        return (entries.size() * (_listEntryHeight + _entryPadding)) - _entryPadding;
    }

    private void drawRect(int left, int right, int top, int bottom, float... color)
    {
        drawRect(left, right, top, bottom, 0, color);
    }

    private void drawRect(int left, int right, int top, int bottom, int z, float... color)
    {
        glEnable(GL_BLEND);
        glDisable(GL_TEXTURE_2D);
        glDisable(GL_CULL_FACE);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
        glShadeModel(GL_SMOOTH);

        Tessellator tessellator = Tessellator.getInstance();
        tessellator.getWorldRenderer().startDrawingQuads();

        tessellator.getWorldRenderer().setColorRGBA_F(color[0], color[1], color[2], color.length > 3 ? color[3] : 1F);
        tessellator.getWorldRenderer().addVertex(left, top, z);
        tessellator.getWorldRenderer().addVertex(right, top, z);
        tessellator.getWorldRenderer().addVertex(right, bottom, z);
        tessellator.getWorldRenderer().addVertex(left, bottom, z);

        tessellator.draw();

//        glColor4f(1, 1, 1, 1);
        glShadeModel(GL_FLAT);
        glEnable(GL_TEXTURE_2D);
        glEnable(GL_CULL_FACE);
        glDisable(GL_BLEND);
    }

    private class ListEntry
    {
        UpdatingMod mod;

        public ListEntry(UpdatingMod mod)
        {
            this.mod = mod;
        }

        public void draw()
        {
            drawRect(_entryPadding, 110 - _entryPadding, 0, _listEntryHeight, 0F, 0F, 0F);
            mc.fontRendererObj.drawString(mod.getDisplayName(), _entryPadding, 0, 0xFFFFFF, true);
        }
    }

    private class DropDown
    {
        Entry[] entries;
        Entry selected;
        boolean expanded = false;

        boolean needsScroll = false;
        int scrollOffset = 0;
        int scrollHides = 0;
        boolean bottomHover = false;
        boolean topHover = false;
        int hovering = -1;

        public DropDown(Entry... entries)
        {
            this.entries = entries;
            selected = entries[0];
        }

        public DropDown(UpdatingMod mod)
        {
            String[] versionIds = mod.getAvailableVersions(false);
            String[] display = mod.getAvailableVersions(true);
            entries = new Entry[versionIds.length];
            for (int i = 0; i < versionIds.length; i++)
                entries[i] = new Entry(versionIds[i], display[i]);
            selected = entries[0];
        }

        public void mouseDown(int mouseX, int mouseY, int mouseButton)
        {
            if (scrollOffset > 0 && topHover)
            {
                System.out.println("top click");
//                scrollOffset = Math.max(0, scrollOffset - 1);
                scrollOffset -= 2;
                if (scrollOffset < 0) scrollOffset = 0;
            } else if (scrollOffset < scrollHides && bottomHover)
            {
                System.out.println("bottom click");
//                scrollOffset = Math.min(scrollHides, scrollOffset + 1);
                scrollOffset += 2;
                if (scrollOffset > scrollHides) scrollOffset = scrollHides;
            } else if (hovering != -1)
            {
                System.out.println("Clicked");
                if (expanded)
                {
                    selected = entries[hovering];
                    System.out.println(hovering);
                    expanded = false;
                } else expanded = true;
            }
        }

        public void draw(int bLeft, int bRight, int bTop, int bBottom, int x, int y, boolean center, int mouseX, int mouseY)
        {
//            mc.fontRendererObj.drawString(selected.name, 0, 0, 0xFFFFFF, true);
            glPushMatrix();
            glTranslatef(0, 0, 1);
//            int bX = x;
//            int textWidth = mc.fontRendererObj.getStringWidth(selected.name);
//            if (center) bX -= textWidth / 2;
//            int tH = mc.fontRendererObj.FONT_HEIGHT;
//            drawRect(bX, bX + textWidth + tH, y, y + mc.fontRendererObj.FONT_HEIGHT * 2, 0F, 0F, 0F, 0.5F);
//            if (center)
//                drawCenteredSplitString(selected.name, x + (tH / 2), y + (tH / 2), bRight - bLeft, 0xFFFFFF, true);
//            else mc.fontRendererObj.drawStringWithShadow(selected.name, x + (tH / 2), y + (tH / 2), 0xFFFFFF);

            FontRenderer fR = mc.fontRendererObj;

//            if (expanded)
//            {
            int width = fR.getStringWidth(entries[0].name) + fR.FONT_HEIGHT;

            if (expanded)
                for (Entry entry : entries) width = Math.max(width, fR.getStringWidth(entry.name) + fR.FONT_HEIGHT);

            int height = 2 * fR.FONT_HEIGHT;
            if (expanded) height *= entries.length;

            int bottom = Math.min(y + height, bBottom);
            int yOffset = bottom - height + (fR.FONT_HEIGHT / 2);
            if (yOffset < bTop)
            {
                yOffset = bTop;
            }
            hovering = -1;
            int listIndex = scrollOffset, renderIndex = 0;
            topHover = bottomHover = false;
            while (listIndex < entries.length)
            {
                Entry entry = entries[listIndex];
                int tY = yOffset - (fR.FONT_HEIGHT / 2);
                boolean nextExceedsHeight = (yOffset + (2 * (1.5 * fR.FONT_HEIGHT))) > bBottom;
                boolean hover = isMouseInBounds(mouseX, mouseY, x, x + width, tY, tY + (2 * fR.FONT_HEIGHT));
//                drawRect(x, x + width, tY, tY + (2 * fR.FONT_HEIGHT), color);
                if (hover) hovering = listIndex;
                boolean centerText = false;
                String text = entry.name;
                if ((renderIndex == 0 && listIndex > 0) || (nextExceedsHeight && listIndex != entries.length - 1))
                {
                    centerText = false;
                    text="More...";
                }
                drawEntry(x,tY,width,hover,centerText,text);
//                if (renderIndex == 0 && listIndex > 0)
//                {
//                    drawCenteredSplitString("More...", x + (width / 2), yOffset, width, 0xFFFFFF, true);
//                    if (hover) this.topHover = true;
//                } else if (nextExceedsHeight && listIndex != entries.length - 1)
//                {
//                    drawCenteredSplitString("More...", x + (width / 2), yOffset, width, 0xFFFFFF, true);
//                    if (hover) this.bottomHover = true;
//                } else fR.drawString(entry.name, x + (fR.FONT_HEIGHT / 2), yOffset, 0xFFFFFF, true);

                    yOffset += (2 * fR.FONT_HEIGHT);

                if (!expanded) break;
                if (nextExceedsHeight)
                {
                    scrollHides = entries.length - 1 - renderIndex;
                    break;
                }

                listIndex++;
                renderIndex++;
            }

            /*for (int i = scrollOffset, j = 0; i < entries.length; i++)
            {
                Entry entry = entries[i];
                int tY = yOffset - (fR.FONT_HEIGHT / 2);
                float[] color = isMouseInBounds(mouseX, mouseY, x, x + width, tY, tY + (2 * fR.FONT_HEIGHT)) ?
                        new float[]{0F, 0F, 1F, expanded ? 0.75F : 0.5F} :
                        new float[]{0F, 0F, 0F, expanded ? 0.75F : 0.5F};
                drawRect(x, x + width, tY, tY + (2 * fR.FONT_HEIGHT), color);

                if (j == 0 && i > 0)
                {
                    drawCenteredSplitString("More...", x + (width / 2), yOffset, width, 0xFFFFFF, true);
                } else if (i == entries.length - 1 && scrollOffset != scrollHides)
                    fR.drawString(entry.name, x + (fR.FONT_HEIGHT / 2), yOffset, 0xFFFFFF, true);

                yOffset += (2 * fR.FONT_HEIGHT);
                boolean exceedsHeight = yOffset + (fR.FONT_HEIGHT * 2) > bBottom;
                if (!expanded) break;
                else if (exceedsHeight)
                {
                    scrollHides = i - scrollOffset;
                    break;
                }
                j++;
            }*/
            glPopMatrix();
        }

        private void drawEntry(int x, int y, int width, boolean hover, boolean centerText, String text)
        {
            float[] color = hover ?
                    new float[]{0F, 0F, 1F, expanded ? 0.75F : 0.5F} :
                    new float[]{0F, 0F, 0F, expanded ? 0.75F : 0.5F};
            drawRect(x, x + width, y, y + (2 * mc.fontRendererObj.FONT_HEIGHT), color);
            int tY = (mc.fontRendererObj.FONT_HEIGHT / 2);
            if (centerText)
                drawCenteredSplitString(text, x + (width / 2), y + tY, width, 0xFFFFFF, true);
            else mc.fontRendererObj.drawString(text, x + tY, y+tY, 0xFFFFFF, true);
        }
    }

    private class Entry
    {
        public Object id;
        public String name;

        public Entry(Object id, String display)
        {
            this.id = id;
            this.name = display;
        }
    }
}

