package dk.mrspring.updater.gui;

import dk.mrspring.updater.core.FileDownloadAction;
import dk.mrspring.updater.core.ModUpdater;
import dk.mrspring.updater.core.UpdatingMod;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.Tessellator;
import org.apache.commons.lang3.ArrayUtils;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;

import java.nio.DoubleBuffer;
import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.opengl.GL11.*;

/**
 * Created by MrSpring on 08-07-2015 for MrSpringUpdateChecker.
 */
public class GuiScreenUpdater extends GuiScreen
{
    List<UpdatingMod> mods;
    List<ListEntry> entries;
    UpdatingMod inFocus;
    DropDown versionSelector;
    GuiButton downloadButton;
    int _listEntryHeight = 50;
    int _entryPadding = 5;

    @Override
    public void initGui()
    {
        super.initGui();
        mods = ModUpdater.getInstance().getRegisteredMods();
        entries = new ArrayList<>();
        for (UpdatingMod mod : mods) entries.add(new ListEntry(mod));
        inFocus = mods.get(0);
        versionSelector = new DropDown(inFocus);
        this.buttonList.add(downloadButton = new GuiButton(0, 0, 0, "Download"));
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks)
    {
        super.drawScreen(mouseX, mouseY, partialTicks);
        this.drawDefaultBackground();
        int xOffset = 0;
        if (mods.size() > 0)
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
        versionSelector.draw(10, width - 10, 30, height, mouseX, mouseY);
        this.drawDownloadWidget(10 + versionSelector.getWidth() + 10, width - 10, height - 20, height, mouseX, mouseY);
    }

    FileDownloadAction downloadAction = null;

    private void drawDownloadWidget(int bLeft, int bRight, int bTop, int bBottom, int mouseX, int mouseY)
    {
        if (downloadAction == null)
        {
//            downloadButton.enabled = true;
//            downloadButton.xPosition = bLeft;
//            downloadButton.yPosition = bTop;
//            downloadButton.width = bRight - bLeft;
//            downloadButton.height = bBottom - bTop;
//            downloadButton.drawButton(mc, mouseX, mouseY);
            drawRect(bLeft, bRight, bTop, bBottom, 0F, 0F, 0F, 0.5F);
        } else if (!downloadAction.hasStarted())
            drawCenteredString(mc.fontRenderer, "Starting download...", bLeft + (bRight - bLeft) / 2, bTop + 3, 0xFFFFFF);
        else if (downloadAction.isDownloading())
            drawCenteredString(mc.fontRenderer, "Downloading...", bLeft + (bRight - bLeft) / 2, bTop + 3, 0xFFFFFF);
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

        drawRect(left, right, top, bottom, 0F, 0F, 0F, alpha); // TODO: Scroll
        String selectedVersion = versionSelector.getSelected();
        String[] changeLog = inFocus.getChangeLogForVersion(selectedVersion);

        enableClippingPane(GL_CLIP_PLANE0, false, -top);
        enableClippingPane(GL_CLIP_PLANE1, true, bottom);

        glPushMatrix();
        boolean dark = false;
        for (String str : changeLog)
        {
            List<String> split = mc.fontRenderer.listFormattedStringToWidth(str, right - left - 8);
            int height = (split.size() * mc.fontRenderer.FONT_HEIGHT) + 6;
            float[] color = dark ? new float[]{0F, 0F, 0F, 0.4F} : new float[]{1F, 1F, 1F, 0.05F};
            drawRect(left, right, top, top + height, color);
            for (int i = 0; i < split.size(); i++)
            {
                String line = split.get(i);
                mc.fontRenderer.drawString(line, left + 4, top + 3 + (i * mc.fontRenderer.FONT_HEIGHT), 0xFFFFFF, true);
            }
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
        List<String> split = mc.fontRenderer.listFormattedStringToWidth(drawing, wrap);
        int offset = 0;
        for (String line : split)
        {
            int textWidth = mc.fontRenderer.getStringWidth(line);
            if (shadow)
            {
                int sColor = (color & 16579836) >> 2 | color & -16777216;
                mc.fontRenderer.drawSplitString(line, x - (textWidth / 2) + 1, y + 1 + offset, wrap, sColor);
            }
            mc.fontRenderer.drawSplitString(line, x - (textWidth / 2), y + offset, wrap, color);
            offset += mc.fontRenderer.FONT_HEIGHT;
        }
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton)
    {
        super.mouseClicked(mouseX, mouseY, mouseButton);
        if (isMouseInBounds(mouseX, mouseY, 10, 120, 0, height))
        {
            System.out.println("Click in list");
        } else versionSelector.mouseDown();
    }

    private boolean isMouseInBounds(int mouseX, int mouseY, int left, int right, int top, int bottom)
    {
        return mouseX < right && mouseX >= left && mouseY < bottom && mouseY >= top;
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

        Tessellator tessellator = Tessellator.instance;
        tessellator.startDrawingQuads();

        tessellator.setColorRGBA_F(color[0], color[1], color[2], color.length > 3 ? color[3] : 1F);
        tessellator.addVertex(left, top, z);
        tessellator.addVertex(right, top, z);
        tessellator.addVertex(right, bottom, z);
        tessellator.addVertex(left, bottom, z);

        tessellator.draw();

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
            drawRect(_entryPadding, 110 - _entryPadding, 0, _listEntryHeight, 0F, 0F, 0F, 0.5F);
            mc.fontRenderer.drawString(mod.getDisplayName(), _entryPadding, 0, 0xFFFFFF, true);
        }
    }

    private class DropDown
    {
        Entry[] entries;
        int selected, hovering = -1;
        boolean expanded = false;
        int width, height;
        int scrollOffset = 0;

        public DropDown(UpdatingMod mod)
        {
            String[] versionIds = mod.getVersionIds();
            String[] display = mod.getVersionsForDisplay();
            entries = new Entry[versionIds.length];
            for (int i = 0; i < versionIds.length; i++)
                entries[i] = new Entry(versionIds[i], display[i]);
        }

        public void draw(int left, int right, int top, int bottom, int mouseX, int mouseY)
        {
            if (width == 0) this.calcWidth();

            GL11.glPushMatrix();
            int entryHeight = 20;
            int entriesToDraw = expanded ? (bottom - top) / entryHeight : 1;
            height = entriesToDraw * entryHeight;
            int y = bottom - height;
            GL11.glTranslatef(left, y, 1);
            mouseY -= y;
            mouseX -= left;
            Entry[] drawing = expanded ?
                    ArrayUtils.subarray(entries, scrollOffset, Math.min(entries.length, scrollOffset + entriesToDraw)) :
                    new Entry[]{entries[selected]};
            int listIndex = scrollOffset;
            hovering = -1;
            for (int i = 0; i < drawing.length; i++)
            {
                Entry entry = drawing[i];
                GL11.glPushMatrix();
                GL11.glTranslatef(0, i * entryHeight, 0);
                String text = entry.name;
                boolean more = expanded && ((i == 0 && listIndex > 0) ||
                        (i == drawing.length - 1 && listIndex != entries.length - 1));
                if (more) text = "More";
                boolean hover = isMouseInBounds(mouseX, mouseY, 0, width, 0, entryHeight);
                if (hover) hovering = listIndex;
                drawEntry(text, hover, more);
                mouseY -= entryHeight;
                GL11.glPopMatrix();
                listIndex++;
            }
            GL11.glPopMatrix();
        }

        private void calcWidth()
        {
            for (Entry entry : entries) width = Math.max(width, mc.fontRenderer.getStringWidth(entry.name) + 8);
        }

        private void drawEntry(String text, boolean hover, boolean centerText)
        {
            float[] color = hover ?
                    new float[]{0F, 0F, 1F, expanded ? 0.75F : 0.5F} :
                    new float[]{0F, 0F, 0F, expanded ? 0.75F : 0.5F};
            drawRect(0, width, 0, 20, color);
            int tY = 20 / 2 - (mc.fontRenderer.FONT_HEIGHT / 2);
            if (centerText)
                drawCenteredSplitString(text, (width / 2), tY, width, 0xFFFFFF, true);
            else mc.fontRenderer.drawString(text, 4, tY, 0xFFFFFF, true);
        }

        public int getWidth()
        {
            return width;
        }

        public String getSelected()
        {
            return entries[selected].id;
        }

        public void mouseDown()
        {
            int entryHeight = 20;
            if (expanded)
            {
                int onScreen = height / entryHeight;
                if (hovering == -1) expanded = false;
                else if (scrollOffset > 0 && hovering == scrollOffset) this.scrollUp();
                else if (scrollOffset < entries.length - onScreen && hovering == onScreen - 1 + scrollOffset)
                    this.scrollDown();
                else
                {
                    selected = hovering;
                    expanded = false;
                }
            } else if (hovering != -1) expanded = true;
        }

        private void scrollDown()
        {
            this.scrollOffset = Math.min(scrollOffset + 2, entries.length - (height / 20));
        }

        private void scrollUp()
        {
            this.scrollOffset = Math.max(scrollOffset - 2, 0);
        }

        /*boolean needsScroll = false;
        int scrollOffset = 0;
        int scrollHides = 0;
        boolean bottomHover = false;
        boolean topHover = false;
        int hovering = -1;
        int width = 0;

        public DropDown(Entry... entries)
        {
//            this.entries = entries;
            this.entries = entries;
            ArrayUtils.reverse(this.entries);
            selected = 0;
        }

        public DropDown(UpdatingMod mod)
        {
            String[] versionIds = mod.getVersionIds();
            String[] display = mod.getVersionsForDisplay();
            entries = new Entry[versionIds.length];
            for (int i = 0; i < versionIds.length; i++)
                entries[i] = new Entry(versionIds[i], display[i]);
            ArrayUtils.reverse(this.entries);
            selected = entries.length - 1;
            scrollHides = selected;
        }

        public int getWidth()
        {
            return width;
        }

        public void mouseDown(int mouseX, int mouseY, int mouseButton)
        {
            if (scrollOffset > 0 && topHover)
            {
                scrollOffset -= 2;
                if (scrollOffset < 0) scrollOffset = 0;
            } else if (scrollOffset < scrollHides && bottomHover)
            {
                scrollOffset += 2;
                if (scrollOffset > scrollHides) scrollOffset = scrollHides;
            } else if (hovering != -1)
            {
                if (expanded)
                {
                    selected = hovering;
                    expanded = false;
                } else expanded = true;
            } else expanded = false;
        }

        public void draw(int bLeft, int bRight, int bTop, int bBottom, int baseHeight, int y, boolean center, int mouseX, int mouseY)
        {
            System.out.println(scrollOffset+", "+scrollHides);

//            mc.fontRendererObj.drawString(selected.name, 0, 0, 0xFFFFFF, true);
            glPushMatrix();
            glTranslatef(0, 0, 1);
            mouseX -= bLeft;
//            int bX = x;
//            int textWidth = mc.fontRendererObj.getStringWidth(selected.name);
//            if (center) bX -= textWidth / 2;
//            int tH = mc.fontRendererObj.FONT_HEIGHT;
//            drawRect(bX, bX + textWidth + tH, y, y + mc.fontRendererObj.FONT_HEIGHT * 2, 0F, 0F, 0F, 0.5F);
//            if (center)
//                drawCenteredSplitString(selected.name, x + (tH / 2), y + (tH / 2), bRight - bLeft, 0xFFFFFF, true);
//            else mc.fontRendererObj.drawStringWithShadow(selected.name, x + (tH / 2), y + (tH / 2), 0xFFFFFF);

            FontRenderer fR = mc.fontRenderer;

            if (width == 0)
                for (Entry entry : entries) width = Math.max(width, fR.getStringWidth(entry.name) + fR.FONT_HEIGHT);

//            if (expanded)
//            {
//            int width = fR.getStringWidth(entries[0].name) + fR.FONT_HEIGHT;

//            if (expanded)
//                for (Entry entry : entries) width = Math.max(width, fR.getStringWidth(entry.name) + fR.FONT_HEIGHT);
            int entryHeight = baseHeight;
            int height = entryHeight;//2 * fR.FONT_HEIGHT;
//            if (expanded) height *= entries.length;

            int yOffset = bBottom - height + (fR.FONT_HEIGHT / 2);
            if (yOffset < bTop)
            {
                yOffset = bBottom - height;
            }
            hovering = -1;
            int listIndex = scrollOffset, renderIndex = 0;
            topHover = bottomHover = false;
            if (expanded)
                while (listIndex < entries.length)
                {
                    Entry entry = entries[listIndex];
                    int tY = yOffset;
                    boolean nextExceedsHeight = yOffset - entryHeight < bTop(yOffset + (2 * (1.5 * fR.FONT_HEIGHT))) > bBottom;
                    boolean hover = isMouseInBounds(mouseX, mouseY, bLeft, bLeft + width, tY, tY + entryHeight);
                    if (hover) hovering = listIndex;
                    boolean centerText = false;
                    String text = entry.name;
                    if ((renderIndex == 0 && listIndex > 0) || (nextExceedsHeight && listIndex != entries.length - 1))
                    {
                        if (hover) bottomHover = !(topHover = renderIndex == 0);
                        centerText = true;
                        text = "More...";
                    }
                    drawEntry(bLeft, tY, width, hover, centerText, text);
                    yOffset -= entryHeight;

                    if (!expanded) break;
                    if (nextExceedsHeight)
                    {
                        scrollHides = entries.length - 1 - renderIndex;
                        break;
                    }

                    listIndex++;
                    renderIndex++;
                }
            else
            {
                Entry drawing = entries[selected];
                boolean hover = isMouseInBounds(mouseX, mouseY, bLeft, bLeft + width, yOffset, yOffset + height);
                if (hover) hovering = selected;
                drawEntry(bLeft, yOffset, width, hover, false, drawing.name);
            }

            for (int i = scrollOffset, j = 0; i < entries.length; i++)
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
            }
            glPopMatrix();
        }

        private void drawEntry(int x, int y, int width, boolean hover, boolean centerText, String text)
        {
            float[] color = hover ?
                    new float[]{0F, 0F, 1F, expanded ? 0.75F : 0.5F} :
                    new float[]{0F, 0F, 0F, expanded ? 0.75F : 0.5F};
            drawRect(x, x + width, y, y + 20(2 * mc.fontRenderer.FONT_HEIGHT), color);
            int tY = 20 / 2 - (mc.fontRenderer.FONT_HEIGHT / 2);
            if (centerText)
                drawCenteredSplitString(text, x + (width / 2), y + tY, width, 0xFFFFFF, true);
            else mc.fontRenderer.drawString(text, x + 4, y + tY, 0xFFFFFF, true);
        }

        public String getSelected()
        {
            return entries[selected].id;
        }
    */
    }

    private class Entry
    {
        public String id;
        public String name;

        public Entry(String id, String display)
        {
            this.id = id;
            this.name = display;
        }
    }
}
