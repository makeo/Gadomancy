package makeo.gadomancy.common.registration;

import cpw.mods.fml.common.network.NetworkRegistry;
import makeo.gadomancy.common.familiar.FamiliarAIController;
import makeo.gadomancy.common.familiar.ai.FamiliarAIProcess;
import makeo.gadomancy.common.network.PacketHandler;
import makeo.gadomancy.common.network.packets.PacketFamiliar;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;

import java.util.ArrayList;
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
public class RegisteredFamiliarAI {

    public static FamiliarAIProcess familiarAIIdle = new FamiliarAIProcess(1) {
        @Override
        public boolean canRun(World world, double x, double y, double z, EntityPlayer parent) {
            return true;
        }

        @Override
        public void tick(int ticksSoFar, World worldObj, EntityPlayer owningPlayer) {}

        @Override
        public int getCooldownDuration() {
            return 0;
        }
    };

    public static FamiliarAIProcess familiarAIZapAttackingMonsters = new FamiliarAIProcess(1) {

        @Override
        public boolean canRun(World world, double x, double y, double z, EntityPlayer parent) {
            return getPotentialTargets(world, parent).size() > 0;
        }

        @Override
        public void tick(int ticksSoFar, World world, EntityPlayer parent) {
            List<EntityLivingBase> lastTargetters = getPotentialTargets(world, parent);
            if(lastTargetters.size() < 1) {
                FamiliarAIController.cleanTargetterList(parent);
                return;
            }
            EntityLivingBase mob = lastTargetters.get(world.rand.nextInt(lastTargetters.size()));
            if(mob.isDead) {
                FamiliarAIController.cleanTargetterList(parent);
                return;
            }

            mob.attackEntityFrom(DamageSource.magic, 6);

            world.playSoundEffect(mob.posX + 0.5, mob.posY + 0.5, mob.posZ + 0.5, "thaumcraft:zap", 0.8F, 1.0F);

            PacketFamiliar.PacketFamiliarBolt bolt = new PacketFamiliar.PacketFamiliarBolt(parent.getCommandSenderName(), (float) mob.posX, (float) mob.posY, (float) mob.posZ);
            PacketHandler.INSTANCE.sendToAllAround(bolt, new NetworkRegistry.TargetPoint(mob.worldObj.provider.dimensionId, mob.posX, mob.posY, mob.posZ, 32));
            FamiliarAIController.cleanTargetterList(parent);
        }

        private List<EntityLivingBase> getPotentialTargets(World world, EntityPlayer player) {
            List<EntityLivingBase> validTargets = getCloseEnoughTargetters(world, player);
            List mobs = world.getEntitiesWithinAABB(IMob.class, AxisAlignedBB.getBoundingBox(player.posX - 0.5, player.posY - 0.5, player.posZ - 0.5, player.posX + 0.5, player.posY + 0.5, player.posZ + 0.5).expand(5, 5, 5));
            for(Object mobObj : mobs) {
                if(!(mobObj instanceof EntityLivingBase)) continue;
                EntityLivingBase mob = (EntityLivingBase) mobObj;
                if(mob.isDead) continue;
                if(!validTargets.contains(mob)) validTargets.add(mob);
            }
            return validTargets;
        }

        private List<EntityLivingBase> getCloseEnoughTargetters(World world, EntityPlayer parent) {
            LinkedList<EntityLivingBase> lastTargetters = FamiliarAIController.getLastTargetters(parent);
            if(lastTargetters == null || lastTargetters.isEmpty()) return new ArrayList<EntityLivingBase>();
            List<EntityLivingBase> closeEnoughLastTargetters = new ArrayList<EntityLivingBase>();
            List livingAround = world.getEntitiesWithinAABB(EntityLivingBase.class, AxisAlignedBB.getBoundingBox(parent.posX - 0.5, parent.posY - 0.5, parent.posZ - 0.5, parent.posX + 0.5, parent.posY + 0.5, parent.posZ + 0.5).expand(8, 8, 8));
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
        public int getCooldownDuration() {
            return 20;
        }
    };

}
