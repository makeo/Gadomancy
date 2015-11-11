package makeo.gadomancy.client.renderers.item;

import makeo.gadomancy.common.blocks.tiles.TileManipulationFocus;
import makeo.gadomancy.common.utils.world.fake.FakeWorld;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.client.IItemRenderer;
import org.lwjgl.opengl.GL11;

/**
 * This class is part of the Gadomancy Mod
 * Gadomancy is Open Source and distributed under the
 * GNU LESSER GENERAL PUBLIC LICENSE
 * for more read the LICENSE file
 *
 * Created by makeo @ 29.09.2015 18:29
 */
public class ItemRenderTileEntity<T extends TileEntity> implements IItemRenderer {
    protected static final World FAKE_WORLD = new FakeWorld();

    protected TileEntitySpecialRenderer renderer;
    protected T tile;

    public ItemRenderTileEntity(TileEntitySpecialRenderer renderer, T tile) {
        this.renderer = renderer;
        this.tile = tile;

        if(tile.getWorldObj() == null) {
            tile.setWorldObj(FAKE_WORLD);
        }
    }

    protected ItemRenderTileEntity() { }

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
        if(type == IItemRenderer.ItemRenderType.ENTITY)
            GL11.glTranslatef(-0.5F, -0.5F, -0.5F);

        if(type == ItemRenderType.INVENTORY)
            GL11.glTranslatef(0, -0.1F, 0);

        if(type == ItemRenderType.EQUIPPED_FIRST_PERSON && tile instanceof TileManipulationFocus)
            GL11.glTranslatef(0, 0.6F, 0);

        this.renderer.renderTileEntityAt(tile, 0.0D, 0.0D, 0.0D, 0.0F);
    }
}
