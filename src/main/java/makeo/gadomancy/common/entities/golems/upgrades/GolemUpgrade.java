package makeo.gadomancy.common.entities.golems.upgrades;

import makeo.gadomancy.common.Gadomancy;
import makeo.gadomancy.common.utils.NBTHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagByte;
import net.minecraft.nbt.NBTTagCompound;
import thaumcraft.common.entities.golems.EntityGolemBase;

/**
 * This class is part of the Gadomancy Mod
 * Gadomancy is Open Source and distributed under the
 * GNU LESSER GENERAL PUBLIC LICENSE
 * for more read the LICENSE file
 *
 * Created by makeo @ 16.06.2015 14:38
 */
public abstract class GolemUpgrade<T extends NBTBase> {
    private static final String UPGRADE_COMPOUND = "upgrades";

    public abstract String getName();

    public String getTagName() {
        return getName();
    }

    public String getText() {
        return Gadomancy.MODID.toLowerCase() + ".upgrades." + getName().toLowerCase();
    }

    public ItemStack getUpgradeItem() {
        return null;
    }

    protected T createUpgradeData() {
        return null;
    }

    protected T getUpgradeData(ItemStack stack) {
        return getUpgradeData(NBTHelper.getPersistentData(stack));
    }

    protected T getUpgradeData(EntityGolemBase golem) {
        return getUpgradeData(NBTHelper.getPersistentData(golem));
    }

    protected T getUpgradeData(NBTTagCompound compound) {
        if(compound.hasKey(UPGRADE_COMPOUND)) {
            NBTTagCompound upgrades = compound.getCompoundTag(UPGRADE_COMPOUND);
            try {
                return (T) upgrades.getTag(getTagName());
            } catch (ClassCastException ignored) {}
        }
        return null;
    }

    public boolean hasUpgrade(ItemStack stack) {
        return hasUpgrade(NBTHelper.getPersistentData(stack));
    }

    public boolean hasUpgrade(EntityGolemBase golem) {
        return hasUpgrade(NBTHelper.getPersistentData(golem));
    }

    protected boolean hasUpgrade(NBTTagCompound compound) {
        return getUpgradeData(compound) != null;
    }

    public void addUpgrade(ItemStack stack) {
        addUpgrade(NBTHelper.getPersistentData(stack));
    }

    public void addUpgrade(EntityGolemBase golem) {
        addUpgrade(NBTHelper.getPersistentData(golem));
    }

    protected void addUpgrade(NBTTagCompound compound) {
        NBTTagCompound upgrades = compound.getCompoundTag(UPGRADE_COMPOUND);
        if(!compound.hasKey(UPGRADE_COMPOUND)) {
            compound.setTag(UPGRADE_COMPOUND, upgrades);
        }

        NBTBase data = createUpgradeData();
        upgrades.setTag(getTagName(), data == null ? new NBTTagByte((byte)1) : data);
    }

    public void removeUpgrade(ItemStack stack) {
        removeUpgrade(NBTHelper.getPersistentData(stack));
    }

    public void removeUpgrade(EntityGolemBase golem) {
        removeUpgrade(NBTHelper.getPersistentData(golem));
    }

    protected void removeUpgrade(NBTTagCompound compound) {
        if(compound.hasKey(UPGRADE_COMPOUND)) {
            compound.removeTag(getTagName());
        }
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof GolemUpgrade && ((GolemUpgrade) obj).getName().equals(getName());
    }
}
