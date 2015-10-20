package makeo.gadomancy.api.golems;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.util.ResourceLocation;
import thaumcraft.common.entities.golems.EnumGolemType;

/**
 * This class is part of the Gadomancy Mod
 * Gadomancy is Open Source and distributed under the
 * GNU LESSER GENERAL PUBLIC LICENSE
 * for more read the LICENSE file
 *
 * Created by makeo @ 26.07.2015 20:55
 */
public abstract class AdditionalGolemType {
    private EnumGolemType enumEntry;
    private Item placerItem;
    private String modId;

    public final int maxHealth;
    public final int armor;
    public final float movementSpeed;
    public final boolean fireResist;
    public final int upgradeAmount;
    public final int carryLimit;
    public final int regenDelay;
    public final int strength;

    public AdditionalGolemType(int maxHealth, int armor, float movementSpeed, boolean fireResist, int upgradeAmount, int carryLimit, int regenDelay, int strength) {
        this.maxHealth = maxHealth;
        this.armor = armor;
        this.movementSpeed = movementSpeed;
        this.fireResist = fireResist;
        this.upgradeAmount = upgradeAmount;
        this.carryLimit = carryLimit;
        this.regenDelay = regenDelay;
        this.strength = strength;
    }

    public abstract String getUnlocalizedName();

    public abstract void registerIcons(IIconRegister ir);
    public abstract IIcon getIcon(ItemStack stack, int pass);

    public int getRenderPasses() { return 1; }

    public abstract ResourceLocation getEntityTexture();

    /**
     * @return resource location for the golem inventory slot texture
     */
    public abstract ResourceLocation getInvSlotTexture();

    public EnumGolemType getEnumEntry() {
        return enumEntry;
    }

    public String getName() {
        return enumEntry.name();
    }

    public Item getPlacerItem() {
        return placerItem;
    }

    public String getModId() {
        return modId;
    }

    @Deprecated
    public final void setEnumEntry(EnumGolemType enumEntry) {
        this.enumEntry = enumEntry;
    }

    @Deprecated
    public void setPlacerItem(Item placerItem) {
        this.placerItem = placerItem;
    }

    @Deprecated
    public void setModId(String modId) {
        this.modId = modId;
    }
}
