package makeo.gadomancy.client.events;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import thaumcraft.api.items.IArchitect;

import java.util.ArrayList;

/**
 * This class is part of the Gadomancy Mod
 * Gadomancy is Open Source and distributed under the
 * GNU LESSER GENERAL PUBLIC LICENSE
 * for more read the LICENSE file
 *
 * Created by makeo @ 13.10.2015 20:34
 */
public class FakeArchitectItem extends Item implements IArchitect {

    private ArrayList<BlockPos> coords;

    public void setCoords(ArrayList<BlockPos> coords) {
        this.coords = coords;
    }

    @Override
    public ArrayList<BlockPos> getArchitectBlocks(ItemStack stack, World world, BlockPos pos, EnumFacing enumFacing, EntityPlayer player) {
        return coords;
    }

    @Override
    public boolean showAxis(ItemStack stack, World world, EntityPlayer player, EnumFacing enumFacing, EnumAxis enumAxis) {
        return false;
    }
}
