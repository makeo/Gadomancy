package makeo.gadomancy.client.renderers.entity;

import makeo.gadomancy.common.entities.EntityAuraCore;
import makeo.gadomancy.common.utils.MiscUtils;
import net.minecraft.entity.Entity;

/**
 * This class is part of the Gadomancy Mod
 * Gadomancy is Open Source and distributed under the
 * GNU LESSER GENERAL PUBLIC LICENSE
 * for more read the LICENSE file
 *
 * Created by HellFirePvP @ 01.12.2015 13:55
 */
public class RenderEntityAuraCore extends RenderEntityItem {

    @Override
    public void doRender(Entity entity, double x, double y, double z, float parf1, float parf2) {
        super.doRender(entity, x, y, z, parf1, parf2);

        if(!(entity instanceof EntityAuraCore)) return;

        EntityAuraCore ea = (EntityAuraCore) entity;

        if(ea.auraOrbital != null) {
            ea.auraOrbital.lastRenderCall = System.currentTimeMillis();
            ea.auraOrbital.updateCenter(MiscUtils.getPositionVector(ea));
        }
    }
}
