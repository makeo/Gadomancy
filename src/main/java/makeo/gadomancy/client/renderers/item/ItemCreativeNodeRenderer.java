package makeo.gadomancy.client.renderers.item;

import makeo.gadomancy.common.registration.RegisteredItems;
import net.minecraft.item.ItemStack;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.api.nodes.NodeType;

/**
 * This class is part of the Gadomancy Mod
 * Gadomancy is Open Source and distributed under the
 * GNU LESSER GENERAL PUBLIC LICENSE
 * for more read the LICENSE file
 * <p/>
 * Created by makeo @ 04.11.2015 20:41
 */
public class ItemCreativeNodeRenderer extends ItemExNodeRenderer {
    private static final AspectList ASPECTS = new AspectList().add(Aspect.AIR, 50).add(Aspect.FIRE, 50).add(Aspect.EARTH, 50).add(Aspect.WATER, 50);

    @Override
    public boolean handleRenderType(ItemStack item, ItemRenderType type) {
        return (item != null) && (item.getItem() == RegisteredItems.itemCreativeNode);
    }

    @Override
    public void renderItem(ItemRenderType type, ItemStack item, Object... data) {
        makeo.gadomancy.client.renderers.item.ItemNodeRenderer.renderNodeItem(type, item, ASPECTS, NodeType.values()[item.getItemDamage()], null, data);
    }
}
