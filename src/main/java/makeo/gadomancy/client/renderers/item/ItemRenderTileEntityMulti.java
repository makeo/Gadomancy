package makeo.gadomancy.client.renderers.item;

import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.client.IItemRenderer;
import org.lwjgl.opengl.GL11;

import java.util.HashMap;
import java.util.Map;

/**
 * This class is part of the Gadomancy Mod
 * Gadomancy is Open Source and distributed under the
 * GNU LESSER GENERAL PUBLIC LICENSE
 * for more read the LICENSE file
 *
 * Created by HellFirePvP @ 13.11.2015 00:33
 */
public class ItemRenderTileEntityMulti implements IItemRenderer {

    private Map<Integer, RenderSet> renderMap = new HashMap<Integer, RenderSet>();

    public ItemRenderTileEntityMulti(RenderSet... renderSets) {
        for(RenderSet set : renderSets) {
            if(set != null) {
                renderMap.put(set.meta, set);
            }
        }
    }

    @Override
    public boolean handleRenderType(ItemStack item, ItemRenderType type) {
        return true;
    }

    @Override
    public boolean shouldUseRenderHelper(ItemRenderType type, ItemStack item, ItemRendererHelper helper) {
        return true;
    }

    @Override
    public void renderItem(ItemRenderType type, ItemStack item, Object... data) {
        GL11.glPushMatrix();
        if(type == IItemRenderer.ItemRenderType.ENTITY)
            GL11.glTranslatef(-0.5F, -0.5F, -0.5F);

        if(type == ItemRenderType.INVENTORY)
            GL11.glTranslatef(0, -0.1F, 0);

        if(renderMap.containsKey(item.getItemDamage())) {
            RenderSet set = renderMap.get(item.getItemDamage());
            set.renderer.renderTileEntityAt(set.te, 0, 0, 0, 0);
        }

        GL11.glPopMatrix();
    }

    public static class RenderSet {

        private TileEntitySpecialRenderer renderer;
        private TileEntity te;
        private int meta;

        public RenderSet(TileEntitySpecialRenderer renderer, TileEntity te, int meta) {
            this.renderer = renderer;
            this.te = te;
            this.meta = meta;
        }
    }

}
