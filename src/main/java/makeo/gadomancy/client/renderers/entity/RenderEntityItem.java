package makeo.gadomancy.client.renderers.entity;

import makeo.gadomancy.common.entities.EntityAuraCore;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.util.ResourceLocation;

/**
 * This class is part of the Gadomancy Mod
 * Gadomancy is Open Source and distributed under the
 * GNU LESSER GENERAL PUBLIC LICENSE
 * for more read the LICENSE file
 *
 * Created by HellFirePvP @ 29.11.2015 19:13
 */
public class RenderEntityItem extends Render {

    private RenderItem ri;

    public RenderEntityItem() {
        ri = new RenderItem();
        ri.setRenderManager(RenderManager.instance);
    }

    @Override
    public void doRender(Entity entity, double x, double y, double z, float parf1, float parf2) {
        if(!(entity instanceof EntityAuraCore)) return;

        EntityItem ei = new EntityItem(entity.worldObj, x, y, z, ((EntityAuraCore) entity).getEntityItem());
        ei.age = ((EntityAuraCore) entity).age;
        ei.hoverStart = ((EntityAuraCore) entity).hoverStart;
        ri.doRender(ei, x, y, z, parf1, parf2);
    }

    @Override
    protected ResourceLocation getEntityTexture(Entity p_110775_1_) {
        return AbstractClientPlayer.locationStevePng;
    }
}
