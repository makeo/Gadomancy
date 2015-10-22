package makeo.gadomancy.common.items;

import makeo.gadomancy.common.Gadomancy;
import net.minecraft.item.Item;

/**
 * This class is part of the Gadomancy Mod
 * Gadomancy is Open Source and distributed under the
 * GNU LESSER GENERAL PUBLIC LICENSE
 * for more read the LICENSE file
 * <p/>
 * Created by makeo @ 20.10.2015 22:49
 */
public class ItemFakeModIcon extends Item {
    public ItemFakeModIcon() {
        setTextureName(Gadomancy.MODID + ":category_icon");
    }
}
