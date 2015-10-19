package makeo.gadomancy.api.golems.cores;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import thaumcraft.common.entities.golems.EntityGolemBase;

/**
 * This class is part of the Gadomancy Mod
 * Gadomancy is Open Source and distributed under the
 * GNU LESSER GENERAL PUBLIC LICENSE
 * for more read the LICENSE file
 *
 * Created by makeo @ 11.08.2015 14:10
 */
public abstract class AdditionalGolemCore {
    private String name;

    public abstract void setupGolem(EntityGolemBase golem);

    public void setupGolemInventory(EntityGolemBase golem) { }

    public abstract boolean hasGui();

    public byte getBaseCore() {
        return 1;
    }

    public boolean openGui(EntityPlayer player, EntityGolemBase golem) {
        return false;
    }

    public ItemStack getToolItem(EntityGolemBase golem) {
        return null;
    }

    public String getUnlocalizedGuiText() {
        return "gadomancy.golem.core.text.default";
    }

    public abstract String getUnlocalizedName();

    public abstract ItemStack getItem();

    public String getName() {
        return name;
    }

    @Deprecated
    public void setName(String name) {
        this.name = name;
    }
}
