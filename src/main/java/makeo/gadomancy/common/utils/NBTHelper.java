package makeo.gadomancy.common.utils;

import makeo.gadomancy.common.Gadomancy;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectList;

import java.util.UUID;

/**
 * This class is part of the Gadomancy Mod
 * Gadomancy is Open Source and distributed under the
 * GNU LESSER GENERAL PUBLIC LICENSE
 * for more read the LICENSE file
 *
 * Created by makeo @ 05.07.2015 12:45
 */
public class NBTHelper {
    private NBTHelper() {}

    public static NBTTagCompound getPersistentData(Entity entity) {
        return getPersistentData(entity.getEntityData());
    }

    public static NBTTagCompound getPersistentData(ItemStack item) {
        return getPersistentData(getData(item));
    }

    public static NBTTagCompound getPersistentData(NBTTagCompound base) {
        NBTTagCompound compound;
        if(hasPersistentData(base)) {
            compound = base.getCompoundTag(Gadomancy.MODID);
        } else {
            compound = new NBTTagCompound();
            base.setTag(Gadomancy.MODID, compound);
        }
        return compound;
    }

    public static boolean hasPersistentData(Entity entity) {
        return hasPersistentData(entity.getEntityData());
    }

    public static boolean hasPersistentData(ItemStack item) {
        return item.getTagCompound() != null && hasPersistentData(item.getTagCompound());
    }

    public static boolean hasPersistentData(NBTTagCompound base) {
        NBTBase modData = base.getTag(Gadomancy.MODID);
        return modData != null && modData instanceof NBTTagCompound;
    }


    public static void removePersistentData(Entity entity) {
        removePersistentData(entity.getEntityData());
    }
    public static void removePersistentData(ItemStack item) {
        if(item.hasTagCompound()) removePersistentData(item.getTagCompound());
    }

    public static void removePersistentData(NBTTagCompound base) {
        base.removeTag(Gadomancy.MODID);
    }


    public static NBTTagCompound getData(ItemStack stack) {
        NBTTagCompound compound = stack.getTagCompound();
        if(compound == null) {
            compound = new NBTTagCompound();
            stack.setTagCompound(compound);
        }
        return compound;
    }


    public static void setStack(NBTTagCompound compound, String tag, ItemStack stack) {
        NBTTagCompound stackCompound = new NBTTagCompound();
        stack.writeToNBT(stackCompound);
        compound.setTag(tag, stackCompound);
    }

    public static void setAspectList(NBTTagCompound compound, String tag, AspectList list) {
        NBTTagCompound listTag = new NBTTagCompound();
        for (Aspect a : list.aspects.keySet()) {
            int amt = list.aspects.get(a);
            listTag.setInteger(a.getTag(), amt);
        }
        compound.setTag(tag, listTag);
    }

    public static AspectList getAspectList(NBTTagCompound compound, String tag) {
        return getAspectList(compound, tag, null);
    }

    public static AspectList getAspectList(NBTTagCompound compound, String tag, AspectList defaultValue) {
        if(!compound.hasKey(tag)) return defaultValue;
        NBTTagCompound cmp = compound.getCompoundTag(tag);
        AspectList out = new AspectList();
        for (Object key : cmp.func_150296_c()) {
            String strKey = (String) key;
            Aspect a = Aspect.getAspect(strKey);
            if(a != null) {
                out.add(a, cmp.getInteger(strKey));
            }
        }
        return out;
    }

    public static void setUUID(NBTTagCompound compound, String tag, UUID uuid) {
        NBTTagCompound uuidComp = new NBTTagCompound();
        uuidComp.setLong("mostSigBits", uuid.getMostSignificantBits());
        uuidComp.setLong("leastSigBits", uuid.getLeastSignificantBits());
        compound.setTag(tag, uuidComp);
    }

    public static ItemStack getStack(NBTTagCompound compound, String tag) {
        return getStack(compound, tag, null);
    }

    public static UUID getUUID(NBTTagCompound compound, String tag) {
        return getUUID(compound, tag, null);
    }

    //Get tags with default value
    public static ItemStack getStack(NBTTagCompound compound, String tag, ItemStack defaultValue) {
        if(compound.hasKey(tag)) {
            return ItemStack.loadItemStackFromNBT(compound.getCompoundTag(tag));
        }
        return defaultValue;
    }

    public static UUID getUUID(NBTTagCompound compound, String tag, UUID defaultValue) {
        if(compound.hasKey(tag)) {
            NBTTagCompound uuidComp = compound.getCompoundTag(tag);
            return new UUID(uuidComp.getLong("mostSigBits"), uuidComp.getLong("leastSigBits"));
        }
        return defaultValue;
    }

    public static boolean getBoolean(NBTTagCompound compound, String tag, boolean defaultValue) {
        return compound.hasKey(tag) ? compound.getBoolean(tag) : defaultValue;
    }

    public static String getString(NBTTagCompound compound, String tag, String defaultValue) {
        return compound.hasKey(tag) ? compound.getString(tag) : defaultValue;
    }

    public static int getInteger(NBTTagCompound compound, String tag, int defaultValue) {
        return compound.hasKey(tag) ? compound.getInteger(tag) : defaultValue;
    }

    public static double getDouble(NBTTagCompound compound, String tag, double defaultValue) {
        return compound.hasKey(tag) ? compound.getDouble(tag) : defaultValue;
    }

    public static float getFloat(NBTTagCompound compound, String tag, float defaultValue) {
        return compound.hasKey(tag) ? compound.getFloat(tag) : defaultValue;
    }

    public static byte getByte(NBTTagCompound compound, String tag, byte defaultValue) {
        return compound.hasKey(tag) ? compound.getByte(tag) : defaultValue;
    }

    public static short getShort(NBTTagCompound compound, String tag, short defaultValue) {
        return compound.hasKey(tag) ? compound.getShort(tag) : defaultValue;
    }

    public static long getLong(NBTTagCompound compound, String tag, long defaultValue) {
        return compound.hasKey(tag) ? compound.getLong(tag) : defaultValue;
    }
}
