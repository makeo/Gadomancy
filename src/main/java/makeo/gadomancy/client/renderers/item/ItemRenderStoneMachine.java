package makeo.gadomancy.client.renderers.item;

import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import org.lwjgl.opengl.GL11;

import java.util.HashMap;
import java.util.Map;

/**
 * This class is part of the Gadomancy Mod
 * Gadomancy is Open Source and distributed under the
 * GNU LESSER GENERAL PUBLIC LICENSE
 * for more read the LICENSE file
 * <p/>
 * Created by makeo @ 28.10.2015 23:27
 */
public class ItemRenderStoneMachine extends ItemRenderTileEntity<TileEntity> {
    private final Map<Integer, RenderInfo> renderers = new HashMap<Integer, RenderInfo>();

    public ItemRenderStoneMachine() { }

    private static class RenderInfo {
        private TileEntity tile;
        private TileEntitySpecialRenderer renderer;
    }

    public void registerRenderer(int metadata, TileEntity tile, TileEntitySpecialRenderer renderer) {
        RenderInfo info = new RenderInfo();
        info.renderer = renderer;
        info.tile = tile;
        renderers.put(metadata, info);

        if(tile.getWorldObj() == null) {
            tile.setWorldObj(FAKE_WORLD);
        }
    }

    @Override
    public void renderItem(ItemRenderType type, ItemStack stack, Object... data) {
        RenderInfo info = renderers.get(stack.getItemDamage());

        tile = info.tile;
        renderer = info.renderer;

        if(stack.getItemDamage() == 0) {
            GL11.glTranslatef(0, 2/16f, 0);
        }

        super.renderItem(type, stack, data);
    }

    @Override
    public boolean handleRenderType(ItemStack stack, ItemRenderType type) {
        return renderers.containsKey(stack.getItemDamage());
    }
}
