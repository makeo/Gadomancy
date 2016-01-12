package makeo.gadomancy.common.items;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import makeo.gadomancy.common.Gadomancy;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;

import java.util.List;

/**
 * This class is part of the Gadomancy Mod
 * Gadomancy is Open Source and distributed under the
 * GNU LESSER GENERAL PUBLIC LICENSE
 * for more read the LICENSE file
 *
 * Created by makeo @ 25.06.2015 22:25
 */
public class ItemFakeGolemShield extends Item {
    private final Item golemItem = new ItemFakeGolemPlacer();
    private IIcon shieldIcon;

    public ItemFakeGolemShield() {
        setCreativeTab(null);
    }

    @SideOnly(Side.CLIENT)
    public int getRenderPasses(int metadata) {
        return golemItem.getRenderPasses(metadata) + 1;
    }

    @Override
    public boolean getHasSubtypes() {
        return golemItem.getHasSubtypes();
    }

    @Override
    public void getSubItems(Item item, CreativeTabs tab, List list) {
        golemItem.getSubItems(item, tab, list);
    }

    @SideOnly(Side.CLIENT)
    public boolean requiresMultipleRenderPasses() {
        return true;
    }

    @Override
    public void registerIcons(IIconRegister ir) {
        shieldIcon = ir.registerIcon(Gadomancy.MODID + ":r_runicupg");
        golemItem.registerIcons(ir);
    }

    @Override
    public IIcon getIcon(ItemStack stack, int pass) {
        if(pass == getRenderPasses(stack.getItemDamage()) - 1) {
            return shieldIcon;
        } else {
            return golemItem.getIcon(stack, pass);
        }
    }
}
