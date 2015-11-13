package makeo.gadomancy.common.blocks;

import makeo.gadomancy.api.ClickBehavior;
import makeo.gadomancy.common.Gadomancy;
import makeo.gadomancy.common.blocks.tiles.TileInfusionClaw;
import makeo.gadomancy.common.registration.RegisteredBlocks;
import makeo.gadomancy.common.registration.RegisteredItems;
import makeo.gadomancy.common.utils.ItemUtils;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.world.World;

/**
 * This class is part of the Gadomancy Mod
 * Gadomancy is Open Source and distributed under the
 * GNU LESSER GENERAL PUBLIC LICENSE
 * for more read the LICENSE file
 *
 * Created by makeo @ 05.10.2015 21:38
 */
public class BlockInfusionClaw extends BlockTransparent {
    public BlockInfusionClaw() {
        super(Material.rock);
        setHardness(3.5f);

        setCreativeTab(RegisteredItems.creativeTab);
    }

    @Override
    protected String getTextureName() {
        return "thaumcraft:pedestal_top";
    }

    @Override
    public void onNeighborBlockChange(World world, int x, int y, int z, Block block) {
        if(!world.isRemote) {
            TileInfusionClaw tile = (TileInfusionClaw) world.getTileEntity(x, y, z);
            tile.updateRedstone(world.isBlockIndirectlyGettingPowered(x, y, z));
        }
    }

    @Override
    public void onBlockPlacedBy(World world, int x, int y, int z, EntityLivingBase entity, ItemStack stack) {
        if(entity instanceof EntityPlayer) {
            TileInfusionClaw tile = (TileInfusionClaw) world.getTileEntity(x, y, z);
            tile.setOwner((EntityPlayer) entity);
        }
    }

    @Override
    public TileEntity createTileEntity(World world, int metadata) {
        return new TileInfusionClaw();
    }

    @Override
    public boolean isOpaqueCube() {
        return false;
    }

    @Override
    public boolean hasTileEntity(int metadata) {
        return true;
    }

    @Override
    public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int p_149727_6_, float p_149727_7_, float p_149727_8_, float p_149727_9_) {
        if(!world.isRemote) {
            TileInfusionClaw tile = (TileInfusionClaw) world.getTileEntity(x, y, z);
            if(tile.hasOwner()) {
                player.openGui(Gadomancy.instance, 1, world, x, y, z);
            } else if(tile.setOwner(player)) {
                player.addChatComponentMessage(new ChatComponentTranslation("gadomancy.info.InfusionClaw.owner"));
            }
        }
        return true;
    }

    @Override
    public void breakBlock(World world, int x, int y, int z, Block block, int metadata) {
        TileInfusionClaw tile = (TileInfusionClaw) world.getTileEntity(x, y, z);
        ItemStack wandStack = tile.getStackInSlot(0);
        if(wandStack != null && wandStack.stackSize > 0) {
            float f = world.rand.nextFloat() * 0.8F + 0.1F;
            float f1 = world.rand.nextFloat() * 0.8F + 0.1F;
            float f2 = world.rand.nextFloat() * 0.8F + 0.1F;

            EntityItem entityItem = new EntityItem(world, x + f, y + f1, z + f2, wandStack.copy());
            ItemUtils.applyRandomDropOffset(entityItem, world.rand);

            world.spawnEntityInWorld(entityItem);
        }
    }

    @Override
    public boolean hasComparatorInputOverride() {
        return true;
    }

    @Override
    public int getComparatorInputOverride(World world, int x, int y, int z, int side) {
        if(y > 0) {
            ClickBehavior behavior = RegisteredBlocks.getClawClickBehavior(world, x, y - 1, z);
            if(behavior != null) {
                return behavior.getComparatorOutput();
            }
        }
        return 0;
    }
}
