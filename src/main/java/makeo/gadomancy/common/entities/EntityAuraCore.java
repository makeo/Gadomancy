package makeo.gadomancy.common.entities;

import makeo.gadomancy.common.data.ModConfig;
import net.minecraft.entity.item.EntityItem;
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
public class EntityAuraCore extends EntityItem {

    private int ticksExisted = 0;
    private AspectList internalAuraList = new AspectList();
    private Aspect dominatingAspect = null;

    public EntityAuraCore(World world) {
        super(world);
    }

    @Override
    protected void entityInit() {
        super.entityInit();

        getDataWatcher().addObjectByDataType(ModConfig.entityAuraCoreDatawatcherAspectId, 4);

        getDataWatcher().updateObject(ModConfig.entityAuraCoreDatawatcherAspectId, "");
    }

    @Override
    public void onUpdate() {
        super.onUpdate();
        if (this.age + 5 >= this.lifespan) {
            this.age = 0;
        }
    }

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
        String tag = compound.getString("domAspect");
        dominatingAspect = Aspect.getAspect(tag);
    }

    @Override
    public void writeEntityToNBT(NBTTagCompound compound) {
        super.writeEntityToNBT(compound);

        compound.setInteger("ticksExisted", ticksExisted);
        NBTTagList list = new NBTTagList();
        for(Aspect a : internalAuraList.getAspects()) {
            if(a == null) continue;
            NBTTagCompound aspectCompound = new NBTTagCompound();
            aspectCompound.setString("tag", a.getTag());
            aspectCompound.setInteger("amt", internalAuraList.getAmount(a));
            list.appendTag(aspectCompound);
        }
        compound.setTag("auraList", list);
        compound.setString("domAspect", dominatingAspect == null ? "" : dominatingAspect.getTag());
    }

}
