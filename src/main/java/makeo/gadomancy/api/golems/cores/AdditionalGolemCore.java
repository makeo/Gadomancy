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

    /**
     * Init your AI stuff here
     * @param golem the golem entity
     */
    public abstract void setupGolem(EntityGolemBase golem);

    /**
     * @return If the  golem has a gui
     */
    public abstract boolean hasGui();

    /**
     * @return the core that is used to define some basic behaviors of the golem e.g. guard core: mob selection
     */
    public byte getBaseCore() {
        return 1;
    }

    /**
     * Open custom a gui here
     * Only called if hasGui is true
     * @param player player entity
     * @param golem golem entity
     * @return false -> open default gui true -> no gui
     */
    public boolean openGui(EntityPlayer player, EntityGolemBase golem) {
        return false;
    }

    /**
     * @param golem
     * @return the held tool item of a golem e.g. fishing rod
     */
    public ItemStack getToolItem(EntityGolemBase golem) {
        return null;
    }

    public String getUnlocalizedGuiText() {
        return "gadomancy.golem.core.text.default";
    }

    public abstract String getUnlocalizedName();

    /**
     * @return If you're able to set markers using the golemancers bell
     */
    public boolean hasMarkers() {
        return true;
    }

    /**
     * @return the item that is used as a core
     */
    public abstract ItemStack getItem();

    public String getName() {
        return name;
    }

    @Deprecated
    public void setName(String name) {
        this.name = name;
    }
}
