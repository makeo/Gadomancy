package makeo.gadomancy.client.renderers.item;

import cpw.mods.fml.client.registry.RenderingRegistry;
import makeo.gadomancy.client.renderers.tile.RenderTileRemoteJar;
import makeo.gadomancy.common.blocks.tiles.TileRemoteJar;
import makeo.gadomancy.common.registration.RegisteredBlocks;
import makeo.gadomancy.common.utils.NBTHelper;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.IItemRenderer;
import org.lwjgl.opengl.GL11;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.common.blocks.ItemJarFilled;
import thaumcraft.common.config.ConfigItems;

/**
 * This class is part of the Gadomancy Mod
 * Gadomancy is Open Source and distributed under the
 * GNU LESSER GENERAL PUBLIC LICENSE
 * for more read the LICENSE file
 *
 * Created by makeo @ 17.10.2015 01:44
 */
public class ItemRenderRemoteJar extends ItemRenderTileEntity<TileRemoteJar> {
    public ItemRenderRemoteJar(RenderTileRemoteJar render) {
        super(render, new TileRemoteJar());
    }

    @Override
    public void renderItem(IItemRenderer.ItemRenderType type, ItemStack stack, Object... data) {
        GL11.glEnable(GL11.GL_ALPHA_TEST);

        AspectList aspects = ((ItemJarFilled)ConfigItems.itemJarFilled).getAspects(stack);
        if(aspects != null) {
            tile.aspect = aspects.getAspects()[0];
            tile.amount = aspects.getAmount(tile.aspect);
        } else {
            tile.aspect = null;
            tile.amount = 0;
        }

        if(stack.hasTagCompound()) {
            tile.networkId = NBTHelper.getUUID(stack.getTagCompound(), "networkId");
        }

        super.renderItem(type, stack, data);

        GL11.glPushMatrix();
        GL11.glTranslatef(0.5f, 0.5f, 0.5f);

        RenderingRegistry.instance().renderInventoryBlock(RenderBlocks.getInstance(), RegisteredBlocks.blockRemoteJar, 0, RegisteredBlocks.blockRemoteJar.getRenderType());
        GL11.glPopMatrix();
    }
}
