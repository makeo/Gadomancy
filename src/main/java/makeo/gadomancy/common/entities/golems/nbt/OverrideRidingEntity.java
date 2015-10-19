package makeo.gadomancy.common.entities.golems.nbt;

import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTTagCompound;
import thaumcraft.common.entities.golems.EntityGolemBase;
import thaumcraft.common.entities.golems.EnumGolemType;

/**
 * This class is part of the Gadomancy Mod
 * Gadomancy is Open Source and distributed under the
 * GNU LESSER GENERAL PUBLIC LICENSE
 * for more read the LICENSE file
 *
 * Created by makeo @ 28.07.2015 18:11
 */
public class OverrideRidingEntity extends Entity {
    private static final EnumGolemType DEFAULT_TYPE = EnumGolemType.WOOD;

    private EntityGolemBase golem;
    private NBTTagCompound compound;
    private Entity ridingEntity;

    public OverrideRidingEntity(EntityGolemBase golem, NBTTagCompound compound, Entity ridingEntity) {
        super(golem.worldObj);
        Entity.nextEntityID--;

        this.golem = golem;
        this.compound = compound;
        this.ridingEntity = ridingEntity;
    }

    @Override
    public boolean writeMountToNBT(NBTTagCompound compound) {
        this.compound.setFloat("HealF", golem.getHealth() * -1);
        this.compound.setByte("GolemType", (byte) DEFAULT_TYPE.ordinal());

        boolean result = false;
        if(ridingEntity != null) {
            result = ridingEntity.writeMountToNBT(compound);
        }

        golem.ridingEntity = ridingEntity;
        return result;
    }

    @Override
    protected void entityInit() {

    }

    @Override
    protected void readEntityFromNBT(NBTTagCompound p_70037_1_) {

    }

    @Override
    protected void writeEntityToNBT(NBTTagCompound p_70014_1_) {

    }
}
