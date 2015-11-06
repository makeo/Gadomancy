package makeo.gadomancy.common.items;

import makeo.gadomancy.common.Gadomancy;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.item.ItemBlock;
import net.minecraft.util.IIcon;

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
    public IIcon getIconFromDamage(int damage) {
        return portalIcon;
    }
}
