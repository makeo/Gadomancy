package makeo.gadomancy.client.renderers.item;

import makeo.gadomancy.client.util.FamiliarHandlerClient;
import makeo.gadomancy.common.items.baubles.ItemFamiliar;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.IItemRenderer;
import org.lwjgl.opengl.GL11;
import thaumcraft.client.renderers.entity.RenderPrimalOrb;

/**
 * This class is part of the Gadomancy Mod
 * Gadomancy is Open Source and distributed under the
 * GNU LESSER GENERAL PUBLIC LICENSE
 * for more read the LICENSE file
 * <p/>
 * Created by HellFirePvP @ 01.11.2015 12:55
 */
public class ItemRenderFamiliar implements IItemRenderer {

    private static final RenderPrimalOrb fallbackRenderer;

    @Override
    public boolean handleRenderType(ItemStack item, ItemRenderType type) {
        return !(item == null || !(item.getItem() instanceof ItemFamiliar));
    }

    @Override
    public boolean shouldUseRenderHelper(ItemRenderType type, ItemStack item, ItemRendererHelper helper) {
        return helper != ItemRendererHelper.ENTITY_ROTATION;
    }

    static {
        fallbackRenderer = new RenderPrimalOrb();
        fallbackRenderer.setRenderManager(RenderManager.instance);
    }

    @Override
    public void renderItem(ItemRenderType type, ItemStack item, Object... data) {
        if(item == null || !(item.getItem() instanceof ItemFamiliar)) return;
        GL11.glPushMatrix();
        if(type == ItemRenderType.EQUIPPED) {
            GL11.glTranslatef(0.5F, 0.5F, 0.7F);
        } else if(type == ItemRenderType.EQUIPPED_FIRST_PERSON) {
            GL11.glTranslatef(0, 1F, 0.8F);
        }
        GL11.glScalef(1.2F, 1.2F, 1.2F);
        fallbackRenderer.renderEntityAt(FamiliarHandlerClient.PartialEntityFamiliar.DUMMY_FAMILIAR.dummyEntity, 0, 0, 0, 0, 0);
        GL11.glPopMatrix();
    }

}
