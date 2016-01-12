package makeo.gadomancy.client.renderers.entity;

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

    private static RenderItem ri;

    @Override
    public void doRender(Entity entity, double x, double y, double z, float parf1, float parf2) {
        if(!(entity instanceof EntityItem)) return;
        ri.doRender(entity, x, y, z, parf1, parf2);
    }

    @Override
    protected ResourceLocation getEntityTexture(Entity p_110775_1_) {
        return AbstractClientPlayer.locationStevePng;
    }

    static {
        ri = new RenderItem();
        ri.setRenderManager(RenderManager.instance);
    }

}
