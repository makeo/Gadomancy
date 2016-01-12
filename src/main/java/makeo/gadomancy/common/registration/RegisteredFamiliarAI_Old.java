package makeo.gadomancy.common.registration;

import cpw.mods.fml.common.network.NetworkRegistry;
import makeo.gadomancy.common.familiar.FamiliarAIController_Old;
import makeo.gadomancy.common.familiar.FamiliarAIProcess_Old;
import makeo.gadomancy.common.items.baubles.ItemFamiliar_Old;
import makeo.gadomancy.common.network.PacketHandler;
import makeo.gadomancy.common.network.packets.PacketFamiliarBolt;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * This class is part of the Gadomancy Mod
 * Gadomancy is Open Source and distributed under the
 * GNU LESSER GENERAL PUBLIC LICENSE
 * for more read the LICENSE file
 *
 * Created by HellFirePvP @ 31.10.2015 14:12
 */
public class RegisteredFamiliarAI_Old {

    public static FamiliarAIProcess_Old familiarAIIdle = new FamiliarAIProcess_Old(1) {
        @Override
        public boolean canRun(World world, double x, double y, double z, EntityPlayer parent, ItemStack stackInSlot) {
            return true;
        }

        @Override
        public void tick(int ticksSoFar, World worldObj, EntityPlayer owningPlayer, ItemStack itemStack) {}

        @Override
        public int getCooldownDuration(ItemStack itemStack) {
            return 0;
        }
    };

    public static FamiliarAIProcess_Old familiarAIZapAttackingMonsters = new FamiliarAIProcess_Old(1) {

        @Override
        public boolean canRun(World world, double x, double y, double z, EntityPlayer parent, ItemStack itemStack) {
            int rangeInc = ((ItemFamiliar_Old) itemStack.getItem()).getAttackRangeIncrease(itemStack);
            return getPotentialTargets(world, parent, rangeInc).size() > 0;
        }

        @Override
        public void tick(int ticksSoFar, World world, EntityPlayer parent, ItemStack itemStack) {
            int rangeInc = ((ItemFamiliar_Old) itemStack.getItem()).getAttackRangeIncrease(itemStack);

            List<EntityLivingBase> lastTargetters = getPotentialTargets(world, parent, rangeInc);
            if(lastTargetters.size() == 0) {
                FamiliarAIController_Old.cleanTargetterList(parent);
                return;
            }
            EntityLivingBase mob = lastTargetters.get(world.rand.nextInt(lastTargetters.size()));
            if(mob.isDead || mob instanceof EntityPlayer) {
                FamiliarAIController_Old.cleanTargetterList(parent);
                return;
            }

            mob.attackEntityFrom(DamageSource.magic, ((ItemFamiliar_Old) itemStack.getItem()).getAttackStrength(itemStack));

            world.playSoundEffect(mob.posX + 0.5, mob.posY + 0.5, mob.posZ + 0.5, "thaumcraft:zap", 0.8F, 1.0F);

            PacketFamiliarBolt bolt = new PacketFamiliarBolt(parent.getCommandSenderName(), (float) mob.posX, (float) mob.posY, (float) mob.posZ, 6, true);
            PacketHandler.INSTANCE.sendToAllAround(bolt, new NetworkRegistry.TargetPoint(mob.worldObj.provider.dimensionId, mob.posX, mob.posY, mob.posZ, 32));
            FamiliarAIController_Old.cleanTargetterList(parent);
        }

        private List<EntityLivingBase> getPotentialTargets(World world, EntityPlayer player, int rangeInc) {
            List<EntityLivingBase> validTargets = getCloseEnoughTargetters(world, player, rangeInc);

            EntityLivingBase attacker = player.getLastAttacker();
            int range = 8 + rangeInc;
            if(attacker != null && !validTargets.contains(attacker) && !attacker.isDead && attacker.getDistanceSq(player.posX, player.posY, player.posZ) <= range * range) {
                validTargets.add(attacker);
            }

            Iterator<EntityLivingBase> it = validTargets.iterator();
            while(it.hasNext()) {
                EntityLivingBase e = it.next();
                if(e.isDead || e instanceof EntityPlayer) it.remove();
            }
            return validTargets;
        }

        private List<EntityLivingBase> getCloseEnoughTargetters(World world, EntityPlayer parent, int rangeInc) {
            LinkedList<EntityLivingBase> lastTargetters = FamiliarAIController_Old.getLastTargetters(parent);
            if(lastTargetters == null || lastTargetters.isEmpty()) return new ArrayList<EntityLivingBase>();
            List<EntityLivingBase> closeEnoughLastTargetters = new ArrayList<EntityLivingBase>();
            int range = 8 + rangeInc;
            List livingAround = world.getEntitiesWithinAABB(EntityLivingBase.class, AxisAlignedBB.getBoundingBox(parent.posX - 0.5, parent.posY - 0.5, parent.posZ - 0.5, parent.posX + 0.5, parent.posY + 0.5, parent.posZ + 0.5).expand(range, range, range));
            for(Object living : livingAround) {
                if(!(living instanceof EntityLivingBase)) continue;
                if(lastTargetters.contains(living)) closeEnoughLastTargetters.add((EntityLivingBase) living);
            }
            return closeEnoughLastTargetters;
        }

        @Override
        public boolean tryLoop() {
            return true;
        }

        @Override
        public int getCooldownDuration(ItemStack itemStack) {
            return 20 - ((ItemFamiliar_Old) itemStack.getItem()).getAttackCooldownReduction(itemStack);
        }
    };

}
