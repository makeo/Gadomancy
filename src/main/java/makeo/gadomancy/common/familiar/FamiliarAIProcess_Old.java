package makeo.gadomancy.common.familiar;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

/**
 * This class is part of the Gadomancy Mod
 * Gadomancy is Open Source and distributed under the
 * GNU LESSER GENERAL PUBLIC LICENSE
 * for more read the LICENSE file
 *
 * Created by HellFirePvP @ 31.10.2015 14:05
 */
public abstract class FamiliarAIProcess_Old {

    private static int sessionProcessIdCounter = 0;
    private int id;
    private int duration;

    public FamiliarAIProcess_Old(int durationTicks) {
        this.duration = durationTicks;
        this.id = sessionProcessIdCounter++;
    }

    public abstract boolean canRun(World world, double x, double y, double z, EntityPlayer parent, ItemStack itemStack);

    public abstract void tick(int ticksSoFar, World worldObj, EntityPlayer owningPlayer, ItemStack itemStack);

    public abstract int getCooldownDuration(ItemStack itemStack);

    public boolean tryLoop() {
        return false;
    }

    public final int getDuration() {
        return duration;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FamiliarAIProcess_Old that = (FamiliarAIProcess_Old) o;
        return id == that.id;

    }

    @Override
    public int hashCode() {
        return id;
    }
}
