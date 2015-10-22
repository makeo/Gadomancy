package makeo.gadomancy.common.blocks;

import makeo.gadomancy.common.blocks.tiles.TileExtendedNode;
import makeo.gadomancy.common.registration.RegisteredItems;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

/**
 * This class is part of the Gadomancy Mod
 * Gadomancy is Open Source and distributed under the
 * GNU LESSER GENERAL PUBLIC LICENSE
 * for more read the LICENSE file
 *
 * Created by HellFirePvP @ 22.10.2015 19:47
 */
public class BlockNode extends thaumcraft.common.blocks.BlockAiry {
    //We overwrite and use the TC class for visual effects and world interact handling.
    //This way our node behaves like TC ones.

    public BlockNode() {
        super();
        setCreativeTab(RegisteredItems.creativeTab);
    }

    @Override
    public TileEntity createTileEntity(World world, int metadata) {
        return new TileExtendedNode();
    }
}
