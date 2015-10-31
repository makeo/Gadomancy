package makeo.gadomancy.common.registration;

import cpw.mods.fml.common.network.NetworkRegistry;
import makeo.gadomancy.common.familiar.ai.FamiliarAIProcess;
import makeo.gadomancy.common.network.PacketHandler;
import makeo.gadomancy.common.network.packets.PacketFamiliar;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;

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

    public static FamiliarAIProcess familiarAIZapMonsters = new FamiliarAIProcess(1) {
        @Override
        public boolean canRun(World world, double x, double y, double z, EntityPlayer parent) {
            return !world.getEntitiesWithinAABB(EntityMob.class, AxisAlignedBB.getBoundingBox(parent.posX - 0.5, parent.posY - 0.5, parent.posZ - 0.5, parent.posX + 0.5, parent.posY + 0.5, parent.posZ + 0.5).expand(5, 5, 5)).isEmpty();
        }

        @Override
        public void tick(int ticksSoFar, World world, EntityPlayer parent) {
            List mobs = world.getEntitiesWithinAABB(EntityMob.class, AxisAlignedBB.getBoundingBox(parent.posX - 0.5, parent.posY - 0.5, parent.posZ - 0.5, parent.posX + 0.5, parent.posY + 0.5, parent.posZ + 0.5).expand(5, 5, 5));
            Object random = mobs.get(world.rand.nextInt(mobs.size()));
            EntityMob mob = (EntityMob) random;

            mob.attackEntityFrom(DamageSource.magic, 6);

            PacketFamiliar.PacketFamiliarBolt bolt = new PacketFamiliar.PacketFamiliarBolt((float) mob.posX, (float) mob.posY, (float) mob.posZ);
            PacketHandler.INSTANCE.sendToAllAround(bolt, new NetworkRegistry.TargetPoint(mob.worldObj.provider.dimensionId, mob.posX, mob.posY, mob.posZ, 32));
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
