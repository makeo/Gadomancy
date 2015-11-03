package makeo.gadomancy.client.renderers.item;

import makeo.gadomancy.client.util.FamiliarHandlerClient;
import makeo.gadomancy.common.items.baubles.ItemFamiliar;
import makeo.gadomancy.common.utils.FakeWorld;
import net.minecraft.client.renderer.ActiveRenderInfo;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.IItemRenderer;
import org.lwjgl.opengl.GL11;
import thaumcraft.client.renderers.entity.RenderWisp;
import thaumcraft.common.entities.monster.EntityWisp;

/**
 * This class is part of the Gadomancy Mod
 * Gadomancy is Open Source and distributed under the
 * GNU LESSER GENERAL PUBLIC LICENSE
 * for more read the LICENSE file
 * <p/>
 * Created by HellFirePvP @ 01.11.2015 12:55
 */
public class ItemRenderFamiliar implements IItemRenderer {

    private static final RenderWisp fallbackRenderer;
    private static final EntityWisp ENTITY_WISP;
    private float[] renderInfo = new float[5];

    @Override
    public boolean handleRenderType(ItemStack item, ItemRenderType type) {
        return !(item == null || !(item.getItem() instanceof ItemFamiliar));
    }

    @Override
    public boolean shouldUseRenderHelper(ItemRenderType type, ItemStack item, ItemRendererHelper helper) {
        return helper != ItemRendererHelper.ENTITY_ROTATION;
    }

    static {
        fallbackRenderer = new RenderWisp();
        fallbackRenderer.setRenderManager(RenderManager.instance);
        ENTITY_WISP = new EntityWisp(new FakeWorld());
    }

    @Override
    public void renderItem(ItemRenderType type, ItemStack item, Object... data) {
        if(item == null || !(item.getItem() instanceof ItemFamiliar)) return;
        GL11.glPushMatrix();
        if(type == ItemRenderType.EQUIPPED) {
            GL11.glTranslatef(0.5F, 0.5F, 0.7F);
        } else if(type == ItemRenderType.EQUIPPED_FIRST_PERSON) {
            GL11.glTranslatef(0, 1F, 0.8F);
        } else if(type == ItemRenderType.INVENTORY) {
            GL11.glTranslatef(0, -0.45F, 0);
        }

        try {
            cleanActiveRenderInfo(type);
            GL11.glScalef(1.2F, 1.2F, 1.2F);
            if(((ItemFamiliar) item.getItem()).hasAspect(item)) {
                ENTITY_WISP.ticksExisted = FamiliarHandlerClient.PartialEntityFamiliar.DUMMY_FAMILIAR.ticksExisted;
                ENTITY_WISP.setType(((ItemFamiliar) item.getItem()).getAspect(item).getTag());
                fallbackRenderer.doRender(ENTITY_WISP, 0, 0, 0, 0, 0);
            }
        } finally {
            restoreActiveRenderInfo();
        }
        GL11.glPopMatrix();
    }

    private void cleanActiveRenderInfo(ItemRenderType renderType) {
        renderInfo[0] = ActiveRenderInfo.rotationX;
        renderInfo[1] = ActiveRenderInfo.rotationXZ;
        renderInfo[2] = ActiveRenderInfo.rotationZ;
        renderInfo[3] = ActiveRenderInfo.rotationYZ;
        renderInfo[4] = ActiveRenderInfo.rotationXY;
        switch (renderType) {
            case ENTITY:
                break;
            case EQUIPPED:
                ActiveRenderInfo.rotationX = 0.85535365F;
                ActiveRenderInfo.rotationXZ = 0.9868404F;
                ActiveRenderInfo.rotationZ = -0.51804453F;
                ActiveRenderInfo.rotationYZ = 0.083717324F;
                ActiveRenderInfo.rotationXY = 0.13822734F;
                break;
            case EQUIPPED_FIRST_PERSON:
                ActiveRenderInfo.rotationX = -0.4186075F;
                ActiveRenderInfo.rotationXZ = 0.99932945F;
                ActiveRenderInfo.rotationZ = -0.90816724F;
                ActiveRenderInfo.rotationYZ = 0.033253096F;
                ActiveRenderInfo.rotationXY = -0.015327567F;
                break;
            case INVENTORY:
                ActiveRenderInfo.rotationX = -0.71445745F;
                ActiveRenderInfo.rotationXZ = 0.9573291F;
                ActiveRenderInfo.rotationZ = 0.69967884F;
                ActiveRenderInfo.rotationYZ = -0.20220716F;
                ActiveRenderInfo.rotationXY = -0.20647818F;
                break;
            case FIRST_PERSON_MAP:
                break;
        }
    }

    private void restoreActiveRenderInfo() {
        if(renderInfo[0] != -1) ActiveRenderInfo.rotationX = renderInfo[0];
        if(renderInfo[1] != -1) ActiveRenderInfo.rotationXZ = renderInfo[1];
        if(renderInfo[2] != -1) ActiveRenderInfo.rotationZ = renderInfo[2];
        if(renderInfo[3] != -1) ActiveRenderInfo.rotationYZ = renderInfo[3];
        if(renderInfo[4] != -1) ActiveRenderInfo.rotationXY = renderInfo[4];
        for (int i = 0; i < renderInfo.length; i++) {
            renderInfo[i] = -1;
        }
    }

}
