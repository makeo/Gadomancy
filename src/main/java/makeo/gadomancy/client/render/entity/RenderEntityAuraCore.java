package makeo.gadomancy.client.render.entity;

import makeo.gadomancy.common.entities.EntityAuraCore;
import makeo.gadomancy.common.utils.MiscUtils;
import net.minecraft.client.renderer.entity.RenderEntityItem;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;

/**
 * This class is part of the Gadomancy Mod
 * Gadomancy is Open Source and distributed under the
 * GNU LESSER GENERAL PUBLIC LICENSE
 * for more read the LICENSE file
 *
 * Created by HellFirePvP @ 01.12.2015 13:55
 */
public class RenderEntityAuraCore extends RenderEntityItem {

    public RenderEntityAuraCore(RenderManager renderManagerIn, RenderItem p_i46167_2_) {
        super(renderManagerIn, p_i46167_2_);
    }

    @Override
    public void doRender(EntityItem entity, double x, double y, double z, float entityYaw, float partialTicks) {
        super.doRender(entity, x, y, z, entityYaw, partialTicks);

        if(!(entity instanceof EntityAuraCore)) return;

        EntityAuraCore ea = (EntityAuraCore) entity;

        if(ea.auraOrbital != null) {
            ea.auraOrbital.lastRenderCall = System.currentTimeMillis();
            ea.auraOrbital.updateCenter(MiscUtils.getPositionVector(ea));
        }
    }
}
