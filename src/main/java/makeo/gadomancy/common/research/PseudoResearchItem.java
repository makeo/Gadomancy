package makeo.gadomancy.common.research;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import thaumcraft.api.ThaumcraftApiHelper;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.api.research.ResearchCategories;
import thaumcraft.api.research.ResearchItem;
import thaumcraft.api.research.ResearchPage;

import java.util.Arrays;

/**
 * This class is part of the Gadomancy Mod
 * Gadomancy is Open Source and distributed under the
 * GNU LESSER GENERAL PUBLIC LICENSE
 * for more read the LICENSE file
 *
 * Created by makeo @ 14.06.2015 14:57
 */
public class PseudoResearchItem extends SimpleResearchItem {
    private static final String PSEUDO_PREFIX = "PSEUDO.";

    private final ResearchItem original;

    private PseudoResearchItem(ResearchItem original, String key, int col, int row, ResourceLocation icon, boolean doubleThisPage) {
        super(PSEUDO_PREFIX + key + (doubleThisPage ? ".2" : ""), col, row, original.getComplexity(), icon, new AspectList());
        this.original = original;
        setStub().setHidden();
    }

    private PseudoResearchItem(ResearchItem original, String key, int col, int row, ItemStack icon, boolean doubleThisPage) {
        super(PSEUDO_PREFIX + key + (doubleThisPage ? ".2" : ""), col, row, original.getComplexity(), icon, new AspectList());
        this.original = original;
        setStub().setHidden();
    }

    @Override
    public ResearchPage[] getPages() {
        return original.getPages();
    }

    @Override
    public String getName() {
        return original.getName();
    }

    @Override
    public String getText() {
        return original.getText();
    }

    @Override
    @SideOnly(Side.CLIENT)
    public boolean isHidden() {
        EntityPlayer p = Minecraft.getMinecraft().thePlayer;
        if(p != null) {
            if(!ThaumcraftApiHelper.isResearchComplete(p.getCommandSenderName(), this.key)) {
                return false;
            }
        }
        return super.isHidden();
    }

    public static ResearchItem create(String original, int col, int row) {
        return create(original, col, row, false);
    }

    public static ResearchItem create(String original, int col, int row, boolean doubleInThisPage) {
        ResearchItem item = ResearchCategories.getResearch(original);

        if(item != null) {
            ResearchItem pseudo;

            if(item.icon_resource == null) {
                pseudo = new PseudoResearchItem(item, item.key, col, row, item.icon_item, doubleInThisPage);
            } else {
                pseudo = new PseudoResearchItem(item, item.key, col, row, item.icon_resource, doubleInThisPage);
            }

            String[] siblings = item.siblings;
            if(siblings == null) {
                siblings = new String[]{ pseudo.key };
            } else {
                siblings = Arrays.copyOf(siblings, siblings.length + 1);
                siblings[siblings.length - 1] = pseudo.key;
            }

            item.setSiblings(siblings);

            if(item.isSecondary()) {
                pseudo.setSecondary();
            }

            return pseudo.setParentsHidden(original);
        }
        return null;
    }
}
