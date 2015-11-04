package makeo.gadomancy.common.items;

import makeo.gadomancy.common.registration.RegisteredItems;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.api.nodes.NodeModifier;
import thaumcraft.api.nodes.NodeType;
import thaumcraft.common.config.ConfigBlocks;
import thaumcraft.common.tiles.TileNode;

import java.util.List;

/**
 * This class is part of the Gadomancy Mod
 * Gadomancy is Open Source and distributed under the
 * GNU LESSER GENERAL PUBLIC LICENSE
 * for more read the LICENSE file
 * <p/>
 * Created by makeo @ 04.11.2015 09:57
 */
public class ItemCreativeNode extends Item {
    public ItemCreativeNode() {
        setCreativeTab(RegisteredItems.creativeTab);
    }

    @Override
    public String getUnlocalizedName(ItemStack stack) {
        return super.getUnlocalizedName(stack) + "." + stack.getItemDamage();
    }

    @Override
    public boolean onItemUse(ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ) {
        ForgeDirection dir = ForgeDirection.getOrientation(side);
        x += dir.offsetX;
        y += dir.offsetY;
        z += dir.offsetZ;

        if(placeRandomNode(world, x, y, z)) {
            int metadata = stack.getItemDamage();
            TileNode node = (TileNode) world.getTileEntity(x, y, z);

            if(metadata == 0) {
                node.setNodeType(NodeType.NORMAL);
                node.setNodeModifier(NodeModifier.BRIGHT);

                AspectList aspects = new AspectList();
                for(Aspect primal : Aspect.getPrimalAspects()) {
                    aspects.add(primal, 10000);
                }
                node.setAspects(aspects);
                node.markDirty();
                world.markBlockForUpdate(x, y, z);
            }
            return true;
        }
        return false;
    }

    private boolean placeRandomNode(World world, int x, int y, int z) {
        return world.getBlock(x, y, z) == Blocks.air && world.setBlock(x, y, z, ConfigBlocks.blockAiry);
    }

    @Override
    public void addInformation(ItemStack stack, EntityPlayer player, List list, boolean advancedItemTooltips) {
        int metadata = stack.getItemDamage();

        if(metadata == 0) {
            list.add("\u00a75Place a huge and bright aura node");
        }

        list.add("\u00a7oCreative Mode Only");
    }
}
