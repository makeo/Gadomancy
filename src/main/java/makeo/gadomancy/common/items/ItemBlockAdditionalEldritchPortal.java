package makeo.gadomancy.common.items;

import makeo.gadomancy.common.Gadomancy;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IIcon;
import net.minecraft.util.StatCollector;

import java.util.List;

/**
 * This class is part of the Gadomancy Mod
 * Gadomancy is Open Source and distributed under the
 * GNU LESSER GENERAL PUBLIC LICENSE
 * for more read the LICENSE file
 * <p/>
 * Created by makeo @ 07.11.2015 00:35
 */
public class ItemBlockAdditionalEldritchPortal extends ItemBlock {
    public ItemBlockAdditionalEldritchPortal(Block block) {
        super(block);
    }

    private IIcon portalIcon;

    @Override
    public void registerIcons(IIconRegister ir) {
        super.registerIcons(ir);
        portalIcon = ir.registerIcon(Gadomancy.MODID + ":eldritch_portal");
    }

    @Override
    public void addInformation(ItemStack stack, EntityPlayer player, List list, boolean flag) {
        list.add(EnumChatFormatting.DARK_PURPLE + StatCollector.translateToLocal("gadomancy.eldritch.portalPlacerCreative"));
        super.addInformation(stack, player, list, flag);
    }

    @Override
    public IIcon getIconFromDamage(int damage) {
        return portalIcon;
    }
}
