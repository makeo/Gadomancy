package makeo.gadomancy.common.utils;

import makeo.gadomancy.api.AuraEffect;
import net.minecraft.entity.Entity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.World;
import thaumcraft.api.aspects.Aspect;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * This class is part of the Gadomancy Mod
 * Gadomancy is Open Source and distributed under the
 * GNU LESSER GENERAL PUBLIC LICENSE
 * for more read the LICENSE file
 *
 * Created by HellFirePvP @ 17.11.2015 14:06
 */
public class AuraEffectHandler {

    public static Map<Aspect, AuraEffect> registeredEffects = new HashMap<Aspect, AuraEffect>();

    //Called once every 32 ticks!
    public static void distributeEffects(Aspect aspect, World worldObj, double x, double y, double z, double range) {
        if(!registeredEffects.containsKey(aspect)) return;
        AuraEffect effect = registeredEffects.get(aspect);
        List entitiesInRange = worldObj.getEntitiesWithinAABB(Entity.class, AxisAlignedBB.getBoundingBox(x - 0.5, y - 0.5, z - 0.5, x + 0.5, y + 0.5, z + 0.5).expand(range, range, range));
        Iterator it = entitiesInRange.iterator();
        while(it.hasNext()) {
            Entity e = (Entity) it.next();
            if(e.isDead) it.remove();
            if(!effect.isEntityApplicable(e)) it.remove();
        }
        for(Object e : entitiesInRange) {
            effect.applyEffect((Entity) e);
        }
    }

}
