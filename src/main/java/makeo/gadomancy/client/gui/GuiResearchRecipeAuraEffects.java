package makeo.gadomancy.client.gui;

import makeo.gadomancy.common.Gadomancy;
import makeo.gadomancy.common.aura.AuraResearchManager;
import makeo.gadomancy.common.aura.ResearchPageAuraAspects;
import makeo.gadomancy.common.utils.Injector;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MathHelper;
import net.minecraft.util.StatCollector;
import org.lwjgl.opengl.GL11;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.api.research.ResearchItem;
import thaumcraft.api.research.ResearchPage;
import thaumcraft.client.gui.GuiResearchRecipe;
import thaumcraft.client.lib.TCFontRenderer;
import thaumcraft.client.lib.UtilsFX;
import thaumcraft.common.Thaumcraft;
import thaumcraft.common.lib.crafting.ThaumcraftCraftingManager;
import thaumcraft.common.lib.utils.InventoryUtils;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * This class is part of the Gadomancy Mod
 * Gadomancy is Open Source and distributed under the
 * GNU LESSER GENERAL PUBLIC LICENSE
 * for more read the LICENSE file
 *
 * Created by HellFirePvP @ 30.11.2015 12:24
 */
public class GuiResearchRecipeAuraEffects extends GuiResearchRecipe {

    private HashMap<String, ArrayList<ItemStack>> itemMap = new HashMap<String, ArrayList<ItemStack>>();

    public GuiResearchRecipeAuraEffects(ResearchItem research, int page, double x, double y) {
        super(research, page, x, y);

        Injector inj = new Injector(this, GuiResearchRecipe.class);

        ResearchPage[] additionalPages = ResearchPageAuraAspects.createAllAuraPagesFor(Minecraft.getMinecraft().thePlayer);
        ResearchPage[] pages = inj.getField("pages");
        ResearchPage[] newPages = new ResearchPage[pages.length + additionalPages.length];
        System.arraycopy(pages, 0, newPages, 0, pages.length);
        System.arraycopy(additionalPages, 0, newPages, pages.length, additionalPages.length);
        inj.setField("pages", newPages);
        inj.setField("maxPages", newPages.length);

        List<String> list = Thaumcraft.proxy.getScannedObjects().get(Minecraft.getMinecraft().thePlayer.getCommandSenderName());
        if ((list != null) && (list.size() > 0)) {
            for (String s : list) {
                try {
                    String s2 = s.substring(1);
                    ItemStack is = getFromCache(Integer.parseInt(s2));
                    if (is != null) {
                        AspectList tags = ThaumcraftCraftingManager.getObjectTags(is);
                        tags = ThaumcraftCraftingManager.getBonusTags(is, tags);
                        if ((tags != null) && (tags.size() > 0)) {
                            for (Aspect a : tags.getAspects()) {
                                ArrayList<ItemStack> items = itemMap.get(a.getTag());
                                if (items == null) {
                                    items = new ArrayList<ItemStack>();
                                }
                                ItemStack is2 = is.copy();
                                is2.stackSize = tags.getAmount(a);
                                items.add(is2);
                                itemMap.put(a.getTag(), items);
                            }
                        }
                    }
                } catch (NumberFormatException e) {}
            }
        }
    }

    public static GuiResearchRecipeAuraEffects create(GuiResearchRecipe oldGui) {
        Injector inj = new Injector(oldGui);
        ResearchItem ri = inj.getField("research");
        double guiX = inj.getField("guiMapX");
        double guiY = inj.getField("guiMapY");
        int page = inj.getField("page");
        return new GuiResearchRecipeAuraEffects(ri, page, guiX, guiY);
    }

    String tex1 = "textures/gui/gui_researchbook.png";

    @Override
    protected void genResearchBackground(int par1, int par2, float par3) {
        int sw = (this.width - this.paneWidth) / 2;
        int sh = (this.height - this.paneHeight) / 2;

        float var10 = (this.width - this.paneWidth * 1.3F) / 2.0F;
        float var11 = (this.height - this.paneHeight * 1.3F) / 2.0F;

        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        UtilsFX.bindTexture(this.tex1);

        GL11.glPushMatrix();
        GL11.glTranslatef(var10, var11, 0.0F);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glScalef(1.3F, 1.3F, 1.0F);
        drawTexturedModalRect(0, 0, 0, 0, this.paneWidth, this.paneHeight);
        GL11.glPopMatrix();

        Injector inj = new Injector(this, GuiResearchRecipe.class);

        ArrayList<List> reference = inj.getField("reference");
        inj.setField("tooltip", null);
        ResearchPage[] pages = inj.getField("pages");
        int page = inj.getField("page");
        int maxPages = inj.getField("maxPages");

        reference.clear();
        int current = 0;
        for (int a = 0; a < pages.length; a++) {
            if (((current == page) || (current == page + 1)) && (current < maxPages)) {
                ResearchPage rPage = pages[a];
                if (rPage instanceof ResearchPageAuraAspects) {
                    drawAuraAspectPagePre(rPage, current % 2, sw, sh, par1, par2, page);
                } else if(rPage != null) {
                    try {
                        Method m = Injector.getMethod("drawPage", GuiResearchRecipe.class, ResearchPage.class, int.class, int.class, int.class, int.class, int.class);
                        m.setAccessible(true);
                        m.invoke(this, pages[a], current % 2, sw, sh, par1, par2);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
            current++;
            if (current > page + 1) {
                break;
            }
        }
        UtilsFX.bindTexture(this.tex1);
        float bob = MathHelper.sin(this.mc.thePlayer.ticksExisted / 3.0F) * 0.2F + 0.1F;
        if (!history.isEmpty()) {
            GL11.glEnable(GL11.GL_BLEND);
            drawTexturedModalRectScaled(sw + 118, sh + 189, 38, 202, 20, 12, bob);
        }
        if (page > 0) {
            GL11.glEnable(GL11.GL_BLEND);
            drawTexturedModalRectScaled(sw - 16, sh + 190, 0, 184, 12, 8, bob);
        }
        if (page < maxPages - 2) {
            GL11.glEnable(GL11.GL_BLEND);
            drawTexturedModalRectScaled(sw + 262, sh + 190, 12, 184, 12, 8, bob);
        }

        inj.setField("reference", reference);
    }

    private void drawAuraAspectPagePre(ResearchPage page, int side, int x, int y, int mx, int my, int thisPage) {
        GL11.glPushAttrib(GL11.GL_ALL_ATTRIB_BITS);
        String researchName = Gadomancy.MODID.toUpperCase() + ".AURA_EFFECTS"; //We know the name of that research.
        researchName = StatCollector.translateToLocal(researchName);
        if ((thisPage == 0) && (side == 0)) {
            drawTexturedModalRect(x + 4, y - 13, 24, 184, 96, 4);
            drawTexturedModalRect(x + 4, y + 4, 24, 184, 96, 4);
            int offset = this.fontRendererObj.getStringWidth(researchName);
            if (offset <= 130) {
                this.fontRendererObj.drawString(researchName, x + 52 - offset / 2, y - 6, 3158064);
            } else {
                float vv = 130.0F / offset;
                GL11.glPushMatrix();
                GL11.glTranslatef(x + 52 - offset / 2 * vv, y - 6.0F * vv, 0.0F);
                GL11.glScalef(vv, vv, vv);
                this.fontRendererObj.drawString(researchName, 0, 0, 3158064);
                GL11.glPopMatrix();
            }
            y += 25;
        }
        GL11.glAlphaFunc(GL11.GL_GREATER, 0.003921569F);
        drawAuraPage(side, x - 8, y - 8, mx, my, page.aspects);
        GL11.glAlphaFunc(GL11.GL_GREATER, 0.1F);
        GL11.glPopAttrib();
    }

    private void drawAuraPage(int side, int x, int y, int mx, int my, AspectList aspects) {
        if ((aspects != null) && (aspects.size() > 0)) {
            TCFontRenderer fr = new Injector(this, GuiResearchRecipe.class).getField("fr");
            GL11.glPushMatrix();
            int start = side * 152;
            int count = 0;
            for (Aspect aspect : aspects.getAspectsSorted()) {
                if (aspect.getImage() != null) {
                    GL11.glPushMatrix();
                    int tx = x + start;
                    int ty = y + count * 50;
                    if ((mx >= tx) && (my >= ty) && (mx < tx + 40) && (my < ty + 40)) {
                        UtilsFX.bindTexture("textures/aspects/_back.png");
                        GL11.glPushMatrix();
                        GL11.glEnable(GL11.GL_BLEND);
                        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
                        GL11.glTranslated(x + start - 5, y + count * 50 - 5, 0.0D);
                        GL11.glScaled(2.5D, 2.5D, 0.0D);
                        UtilsFX.drawTexturedQuadFull(0, 0, this.zLevel);
                        GL11.glDisable(GL11.GL_BLEND);
                        GL11.glPopMatrix();
                    }
                    GL11.glScalef(2.0F, 2.0F, 2.0F);
                    UtilsFX.drawTag((x + start) / 2, (y + count * 50) / 2, aspect, aspects.getAmount(aspect), 0, this.zLevel);
                    GL11.glPopMatrix();
                    String text = aspect.getName();
                    int offset = fr.getStringWidth(text) / 2;
                    fr.drawString(text, x + start + 16 - offset, y + 33 + count * 50, 5263440);

                    List<String> lines = AuraResearchManager.getLines(aspect.getTag());

                    int drawX = x + start + 48 + 36;
                    int drawY = y + 12 + count * 50;
                    int lineSpace = 8;
                    int yOffset = drawY - (((lines.size() - 1) * lineSpace) / 2);
                    for (int i = 0; i < lines.size(); i++) {
                        String txt = lines.get(i);
                        int width = fr.getStringWidth(txt);
                        int actDrawX = drawX - (width / 2);
                        int actDrawY = yOffset + (i * lineSpace);
                        fr.drawString(txt, actDrawX, actDrawY, 0x444444);
                    }
                }
                count++;
            }
            count = 0;
            for (Aspect aspect : aspects.getAspectsSorted()) {
                int tx = x + start;
                int ty = y + count * 50;
                if ((mx >= tx) && (my >= ty) && (mx < tx + 40) && (my < ty + 40)) {
                    ArrayList<ItemStack> items = itemMap.get(aspect.getTag());
                    if ((items != null) && (items.size() > 0)) {
                        int xcount = 0;
                        int ycount = 0;
                        for (ItemStack item : items) {
                            GL11.glPushMatrix();
                            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
                            RenderHelper.enableGUIStandardItemLighting();
                            GL11.glEnable(GL11.GL_LIGHTING);
                            itemRenderer.renderItemAndEffectIntoGUI(this.mc.fontRenderer, this.mc.renderEngine, InventoryUtils.cycleItemStack(item), mx + 8 + xcount * 17, 17 * ycount + (my - (4 + items.size() / 8 * 8)));
                            itemRenderer.renderItemOverlayIntoGUI(this.mc.fontRenderer, this.mc.renderEngine, InventoryUtils.cycleItemStack(item), mx + 8 + xcount * 17, 17 * ycount + (my - (4 + items.size() / 8 * 8)));
                            RenderHelper.disableStandardItemLighting();
                            GL11.glPopMatrix();
                            xcount++;
                            if (xcount >= 8) {
                                xcount = 0;
                                ycount++;
                            }
                        }
                        GL11.glEnable(GL11.GL_LIGHTING);
                    }
                }
                count++;
            }
            GL11.glPopMatrix();
        }
    }
}
