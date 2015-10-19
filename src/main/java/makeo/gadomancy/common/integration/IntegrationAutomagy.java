package makeo.gadomancy.common.integration;

import makeo.gadomancy.common.registration.RegisteredBlocks;
import makeo.gadomancy.common.registration.RegisteredItems;
import net.minecraft.block.Block;
import net.minecraft.item.Item;

/**
 * This class is part of the Gadomancy Mod
 * Gadomancy is Open Source and distributed under the
 * GNU LESSER GENERAL PUBLIC LICENSE
 * for more read the LICENSE file
 *
 * Created by makeo @ 04.10.2015 02:30
 */
public class IntegrationAutomagy extends IntegrationMod {
    @Override
    public String getModId() {
        return "Automagy";
    }

    @Override
    protected void doInit() {
        Block infinityJar = Block.getBlockFromName("Automagy:blockCreativeJar");
        if(infinityJar != null) {
            RegisteredBlocks.registerStickyJar(infinityJar, 3, false, true);
            RegisteredItems.registerStickyJar(Item.getItemFromBlock(infinityJar), 3);
        }
    }
}
