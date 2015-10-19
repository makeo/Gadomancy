package makeo.gadomancy.common.entities.player;

import makeo.gadomancy.common.utils.NBTHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraftforge.common.IExtendedEntityProperties;

/**
 * This class is part of the Gadomancy Mod
 * Gadomancy is Open Source and distributed under the
 * GNU LESSER GENERAL PUBLIC LICENSE
 * for more read the LICENSE file
 *
 * Created by makeo @ 05.07.2015 12:39
 */
public class ExtendedPlayerProperties implements IExtendedEntityProperties {
    private static final int TRANSFORM_INDEX = 26;

    private static final ExtendedPlayerProperties DEFAULTS = new ExtendedPlayerProperties(null);

    private final EntityPlayer player;

    private boolean isTransformed = false;

    public ExtendedPlayerProperties(EntityPlayer player) {
        this.player = player;
    }

    @Override
    public void saveNBTData(NBTTagCompound compound) {
        NBTTagCompound modData = NBTHelper.getPersistentData(compound);

        modData.setBoolean("isTransformed", isTransformed);
    }

    @Override
    public void loadNBTData(NBTTagCompound compound) {
        NBTTagCompound modData = NBTHelper.getPersistentData(compound);

        isTransformed = NBTHelper.getBoolean(modData, "isTransformed", DEFAULTS.isTransformed);
        player.getDataWatcher().updateObject(TRANSFORM_INDEX, isTransformed);
    }

    @Override
    public void init(Entity entity, World world) {
        player.getDataWatcher().addObject(TRANSFORM_INDEX, isTransformed);
    }

    public static boolean isTransformed(EntityPlayer player) {
        return false;

    }

    public void setTransformed(boolean isTransformed) {
        this.isTransformed = isTransformed;
    }
}
