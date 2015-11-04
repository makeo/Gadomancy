package makeo.gadomancy.common.entities.golems.cores;

import makeo.gadomancy.api.golems.cores.AdditionalGolemCore;
import makeo.gadomancy.common.Gadomancy;
import makeo.gadomancy.common.entities.ai.AIFollowOwner;
import makeo.gadomancy.common.entities.ai.AISit;
import makeo.gadomancy.common.entities.ai.AIUncheckedAttackOnCollide;
import makeo.gadomancy.common.entities.ai.AIUncheckedDartAttack;
import makeo.gadomancy.common.entities.golems.nbt.ExtendedGolemProperties;
import makeo.gadomancy.common.registration.RegisteredItems;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAIOwnerHurtByTarget;
import net.minecraft.entity.ai.EntityAIOwnerHurtTarget;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.ai.EntityLookHelper;
import net.minecraft.entity.ai.EntitySenses;
import net.minecraft.entity.ai.attributes.IAttribute;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.entity.passive.EntityTameable;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.pathfinding.PathNavigate;
import thaumcraft.common.entities.ai.misc.AIOpenDoor;
import thaumcraft.common.entities.golems.EntityGolemBase;
import thaumcraft.common.entities.golems.Marker;

import java.util.ArrayList;
import java.util.Random;

/**
 * This class is part of the Gadomancy Mod
 * Gadomancy is Open Source and distributed under the
 * GNU LESSER GENERAL PUBLIC LICENSE
 * for more read the LICENSE file
 * <p/>
 * Created by makeo @ 31.10.2015 15:35
 */
public class BodyguardGolemCore extends AdditionalGolemCore {
    @Override
    public void setupGolem(EntityGolemBase golem) {
        WrappedGolem wrapped = new WrappedGolem(golem);

        golem.setHomeArea(0, 300000000, 0, 300000000);
        golem.setMarkers(new ArrayList<Marker>());

        golem.targetTasks.addTask(1, new EntityAIOwnerHurtByTarget(wrapped));
        golem.targetTasks.addTask(2, new EntityAIOwnerHurtTarget(wrapped));
        golem.targetTasks.addTask(3, new EntityAIHurtByTarget(golem, true));

        golem.tasks.addTask(1, new AISit(golem));

        if (golem.decoration.contains("R")) {
            golem.tasks.addTask(2, new AIUncheckedDartAttack(golem));
        }
        golem.tasks.addTask(3, new AIUncheckedAttackOnCollide(golem));
        golem.tasks.addTask(4, new AIFollowOwner(wrapped, 5, 2));

        golem.tasks.addTask(5, new AIOpenDoor(golem, true));
        golem.tasks.addTask(7, new EntityAIWatchClosest(golem, EntityPlayer.class, 6.0F));
        golem.tasks.addTask(8, new EntityAILookIdle(golem));
    }

    @Override
    public boolean hasMarkers() {
        return false;
    }

    private static class WrappedGolem extends EntityTameable {
        private EntityGolemBase golem;

        public WrappedGolem(EntityGolemBase golem) {
            super(golem.worldObj);

            this.golem = golem;
        }

        @Override
        public boolean isWithinHomeDistance(int p_110176_1_, int p_110176_2_, int p_110176_3_) {
            return true;
        }

        @Override
        public float getAIMoveSpeed() {
            return golem.getAIMoveSpeed();
        }

        @Override
        public Random getRNG() {
            return golem.getRNG();
        }

        @Override
        public EntityLivingBase getAttackTarget() {
            return golem.getAttackTarget();
        }

        @Override
        public boolean canAttackClass(Class p_70686_1_) {
            return golem.canAttackClass(p_70686_1_);
        }

        @Override
        public EntitySenses getEntitySenses() {
            return golem.getEntitySenses();
        }

        @Override
        public IAttributeInstance getEntityAttribute(IAttribute p_110148_1_) {
            if(golem != null) {
                return golem.getEntityAttribute(p_110148_1_);
            }
            return super.getEntityAttribute(p_110148_1_);
        }

        @Override
        public void setAttackTarget(EntityLivingBase p_70624_1_) {
            golem.setAttackTarget(p_70624_1_);
        }

        @Override
        public boolean isTamed() {
            return true;
        }

        @Override
        public PathNavigate getNavigator() {
            return golem.getNavigator();
        }

        @Override
        public EntityAgeable createChild(EntityAgeable entity) {
            return null;
        }

        @Override
        public EntityLivingBase getOwner() {
            return golem.getOwner();
        }

        @Override
        public EntityLookHelper getLookHelper() {
            return golem.getLookHelper();
        }

        @Override
        public double getDistanceSqToEntity(Entity entity) {
            return golem.getDistanceSqToEntity(entity);
        }

        @Override
        public boolean getLeashed() {
            return golem.getLeashed();
        }

        @Override
        public int getVerticalFaceSpeed() {
            return golem.getVerticalFaceSpeed();
        }

        @Override
        public void setLocationAndAngles(double x, double y, double z, float rotationYaw, float rotationPitch) {
            rotationYaw = golem.rotationYaw;
            rotationPitch = golem.rotationPitch;
            golem.setLocationAndAngles(x, y, z, rotationYaw, rotationPitch);
            this.worldObj.playSoundEffect(x, y, z, "mob.endermen.portal", 0.5F, 1.0F);
        }
    }

    @Override
    public boolean hasGui() {
        return true;
    }

    @Override
    public boolean openGui(EntityPlayer player, EntityGolemBase golem) {
        ExtendedGolemProperties props = (ExtendedGolemProperties) golem.getExtendedProperties(Gadomancy.MODID);
        props.setSitting(!props.isSitting());

        return true;
    }

    @Override
    public byte getBaseCore() {
        return 4;
    }

    @Override
    public String getUnlocalizedName() {
        return "gadomancy.golem.bodyguardcore";
    }

    @Override
    public ItemStack getItem() {
        return new ItemStack(RegisteredItems.itemGolemCoreBreak, 1, 1);
    }
}
