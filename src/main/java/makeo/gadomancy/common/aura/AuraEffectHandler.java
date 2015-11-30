package makeo.gadomancy.common.aura;

import makeo.gadomancy.api.AuraEffect;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
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

    //Called once every 8 ticks!
    public static void distributeEffects(Aspect aspect, World worldObj, double x, double y, double z, double range, int tick) {
        if(!registeredEffects.containsKey(aspect) || worldObj.isRemote) return;
        AuraEffect effect = registeredEffects.get(aspect);
        if(tick % effect.getTickInterval() != 0) return;

        List entitiesInRange = worldObj.getEntitiesWithinAABB(Entity.class, AxisAlignedBB.getBoundingBox(x - 0.5, y - 0.5, z - 0.5, x + 0.5, y + 0.5, z + 0.5).expand(range, range, range));
        Iterator it = entitiesInRange.iterator();
        while(it.hasNext()) {
            Entity e = (Entity) it.next();
            if(e.isDead) it.remove();
            if(!effect.isEntityApplicable(e)) it.remove();
        }
        for(Object e : entitiesInRange) {
            effect.applyEffect((Entity) e);
            if(e instanceof EntityPlayer) {
                AuraResearchManager.tryUnlockAuraEffect((EntityPlayer) e, aspect);
            }
        }
    }

    static {

        registeredEffects.put(Aspect.HEAL, AuraEffects.DUMMY_SANO);

    }

}
