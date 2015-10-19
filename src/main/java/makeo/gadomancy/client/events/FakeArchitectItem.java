package makeo.gadomancy.client.events;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import thaumcraft.api.BlockCoordinates;
import thaumcraft.api.IArchitect;

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
    private ArrayList<BlockCoordinates> coords;

    public void setCoords(ArrayList<BlockCoordinates> coords) {
        this.coords = coords;
    }

    @Override
    public ArrayList<BlockCoordinates> getArchitectBlocks(ItemStack stack, World world, int x, int y, int z, int side, EntityPlayer player) {
        return coords;
    }

    @Override
    public boolean showAxis(ItemStack paramItemStack, World paramWorld, EntityPlayer paramEntityPlayer, int paramInt, EnumAxis paramEnumAxis) {
        return false;
    }
}
