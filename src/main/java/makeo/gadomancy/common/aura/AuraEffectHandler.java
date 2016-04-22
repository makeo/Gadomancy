package makeo.gadomancy.common.aura;

import makeo.gadomancy.api.AuraEffect;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import thaumcraft.api.aspects.Aspect;

import java.util.ArrayList;
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

    public static void distributeEffects(Aspect aspect, World worldObj, double x, double y, double z, int tick) {
        if(!registeredEffects.containsKey(aspect) || worldObj.isRemote || AuraResearchManager.isBlacklisted(aspect)) return;
        AuraEffect effect = registeredEffects.get(aspect);
        if((tick % effect.getTickInterval()) != 0) return;

        AuraEffect.EffectType type = effect.getEffectType();
        if(type != null) {
            switch (type) {
                case ENTITY_EFFECT:
                    doEntityEffects(effect, worldObj, x, y, z);
                    break;
                case BLOCK_EFFECT:
                    doBlockEffects(effect, worldObj, x, y, z);
                    break;
            }
        } else {
            doEntityEffects(effect, worldObj, x, y, z);
            doBlockEffects(effect, worldObj, x, y, z);
        }

        //Distribute research
        double range = effect.getRange();
        List players = worldObj.getEntitiesWithinAABB(EntityPlayer.class, AxisAlignedBB.fromBounds(x - 0.5, y - 0.5, z - 0.5, x + 0.5, y + 0.5, z + 0.5).expand(range, range, range));
        for(Object p : players) {
            if(p == null || !(p instanceof EntityPlayer)) continue;
            EntityPlayer player = (EntityPlayer) p;
            AuraResearchManager.tryUnlockAuraEffect(player, aspect);
        }
    }

    private static void doBlockEffects(AuraEffect effect, World worldObj, double x, double y, double z) {
        int count = effect.getBlockCount(worldObj.rand);

        BlockPos origin = new BlockPos((int) x, (int) y, (int) z);
        List<BlockPos> foundBlocks = new ArrayList<BlockPos>();
        int intRange = (int) effect.getRange();
        for (int i = 0; i < count; i++) {
            int xx = worldObj.rand.nextInt(intRange) - worldObj.rand.nextInt(intRange);
            int yy = worldObj.rand.nextInt(intRange) - worldObj.rand.nextInt(intRange);
            int zz = worldObj.rand.nextInt(intRange) - worldObj.rand.nextInt(intRange);

            BlockPos blockCC = new BlockPos((int) x + xx, (int) y + yy, (int) z + zz);
            if(foundBlocks.contains(blockCC)) {
                count++;
            } else {
                foundBlocks.add(blockCC);
                effect.doBlockEffect(origin, blockCC, worldObj);
            }
        }
    }

    private static void doEntityEffects(AuraEffect effect, World worldObj, double x, double y, double z) {
        double range = effect.getRange();
        List entitiesInRange = worldObj.getEntitiesWithinAABB(Entity.class, AxisAlignedBB.fromBounds(x - 0.5, y - 0.5, z - 0.5, x + 0.5, y + 0.5, z + 0.5).expand(range, range, range));
        Iterator it = entitiesInRange.iterator();
        while(it.hasNext()) {
            Entity e = (Entity) it.next();
            if(e == null || e.isDead) {
                it.remove();
                continue;
            }
            if(!effect.isEntityApplicable(e)) it.remove();
        }
        BlockPos origin = new BlockPos((int) x, (int) y, (int) z);
        for(Object e : entitiesInRange) {
            effect.doEntityEffect(origin, (Entity) e);
        }
    }

}
