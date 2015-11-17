package makeo.gadomancy.common.entities;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.world.World;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.common.entities.EntitySpecialItem;

/**
 * This class is part of the Gadomancy Mod
 * Gadomancy is Open Source and distributed under the
 * GNU LESSER GENERAL PUBLIC LICENSE
 * for more read the LICENSE file
 *
 * Created by HellFirePvP @ 16.11.2015 14:42
 */
public class EntityAuraCore extends EntitySpecialItem {

    private int ticksExisted = 0;
    private AspectList internalAuraList = new AspectList();

    public EntityAuraCore(World world) {
        super(world);
    }

    @Override
    public void onUpdate() {}

    @Override
    public void readEntityFromNBT(NBTTagCompound compound) {
        super.readEntityFromNBT(compound);

        ticksExisted = compound.getInteger("ticksExisted");
        NBTTagList list = compound.getTagList("auraList", compound.getId());
        for (int i = 0; i < list.tagCount(); i++) {
            NBTTagCompound cmp = list.getCompoundTagAt(i);
            if(cmp.hasKey("tag") && cmp.hasKey("amt")) {
                internalAuraList.add(Aspect.getAspect(cmp.getString("tag")), cmp.getInteger("amt"));
            }
        }
    }

    @Override
    public void writeEntityToNBT(NBTTagCompound compound) {
        super.writeEntityToNBT(compound);

        compound.setInteger("ticksExisted", ticksExisted);
        NBTTagList list = new NBTTagList();
        for(Aspect a : internalAuraList.getAspects()) {
            NBTTagCompound aspectCompound = new NBTTagCompound();
            aspectCompound.setString("tag", a.getTag());
            aspectCompound.setInteger("amt", internalAuraList.getAmount(a));
            list.appendTag(aspectCompound);
        }
        compound.setTag("auraList", list);
    }

}
