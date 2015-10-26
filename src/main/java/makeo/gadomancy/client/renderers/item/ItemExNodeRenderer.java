package makeo.gadomancy.client.renderers.item;

import makeo.gadomancy.common.registration.RegisteredBlocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import thaumcraft.client.renderers.tile.ItemNodeRenderer;

/**
 * This class is part of the Gadomancy Mod
 * Gadomancy is Open Source and distributed under the
 * GNU LESSER GENERAL PUBLIC LICENSE
 * for more read the LICENSE file
 *
 * Created by HellFirePvP @ 22.10.2015 20:11
 */
public class ItemExNodeRenderer extends ItemNodeRenderer {

    @Override
    public boolean handleRenderType(ItemStack item, ItemRenderType type) {
        return (item != null) && (item.getItem() == Item.getItemFromBlock(RegisteredBlocks.blockNode)) && ((item.getItemDamage() == 0) || item.getItemDamage() == 5);
    }

    @Override
    public boolean shouldUseRenderHelper(ItemRenderType type, ItemStack item, ItemRendererHelper helper) {
        return helper != ItemRendererHelper.EQUIPPED_BLOCK;
    }

    @Override
    public void renderItem(ItemRenderType type, ItemStack item, Object... data) {
        makeo.gadomancy.client.renderers.item.ItemNodeRenderer.renderNodeItem(type, item, data);
    }
}
