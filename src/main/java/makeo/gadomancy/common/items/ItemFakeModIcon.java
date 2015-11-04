package makeo.gadomancy.common.items;

import makeo.gadomancy.common.Gadomancy;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.item.Item;
import net.minecraft.util.IIcon;

/**
 * This class is part of the Gadomancy Mod
 * Gadomancy is Open Source and distributed under the
 * GNU LESSER GENERAL PUBLIC LICENSE
 * for more read the LICENSE file
 *
 * Created by makeo @ 20.10.2015 22:49
 */
public class ItemFakeModIcon extends Item {

    public ItemFakeModIcon() {
        setTextureName(Gadomancy.MODID + ":category_icon");
    }

    public IIcon[] icons = new IIcon[6];

    @Override
    public void registerIcons(IIconRegister ir) {
        icons[0] = ir.registerIcon(Gadomancy.MODID + ":category_icon");
        icons[1] = ir.registerIcon(Gadomancy.MODID + ":Attack1");
        icons[2] = ir.registerIcon(Gadomancy.MODID + ":Attack2");
        icons[3] = ir.registerIcon(Gadomancy.MODID + ":Attack3");
        icons[4] = ir.registerIcon(Gadomancy.MODID + ":Range1");
        icons[5] = ir.registerIcon(Gadomancy.MODID + ":Cooldown1");
    }

    @Override
    public IIcon getIconFromDamage(int damage) {
        if(damage < 0 || damage >= icons.length) return null;
        return icons[damage];
    }

}
