package makeo.gadomancy.common.items;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import makeo.gadomancy.common.blocks.tiles.TileRemoteJar;
import makeo.gadomancy.common.blocks.tiles.TileStickyJar;
import makeo.gadomancy.common.registration.RegisteredBlocks;
import makeo.gadomancy.common.utils.NBTHelper;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

import java.util.List;
import java.util.UUID;

/**
 * This class is part of the Gadomancy Mod
 * Gadomancy is Open Source and distributed under the
 * GNU LESSER GENERAL PUBLIC LICENSE
 * for more read the LICENSE file
 *
 * Created by makeo @ 14.10.2015 17:51
 */
public class ItemBlockRemoteJar extends ItemBlock {
    public ItemBlockRemoteJar() {
        super(RegisteredBlocks.blockRemoteJar);
    }

    public ItemBlockRemoteJar(Block block) {
        super(block);
    }

    @Override
    public ItemStack onItemRightClick(ItemStack p_77659_1_, World p_77659_2_, EntityPlayer p_77659_3_) {
        return super.onItemRightClick(p_77659_1_, p_77659_2_, p_77659_3_);
    }

    @Override
    public boolean onItemUse(ItemStack p_77648_1_, EntityPlayer p_77648_2_, World p_77648_3_, int p_77648_4_, int p_77648_5_, int p_77648_6_, int p_77648_7_, float p_77648_8_, float p_77648_9_, float p_77648_10_) {
        return super.onItemUse(p_77648_1_, p_77648_2_, p_77648_3_, p_77648_4_, p_77648_5_, p_77648_6_, p_77648_7_, p_77648_8_, p_77648_9_, p_77648_10_);
    }

    @Override
    public boolean onItemUseFirst(ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ) {
        TileRemoteJar tile = getJarTile(world, x, y, z);
        if (tile != null) {
            if(!world.isRemote) {
                NBTTagCompound compound = NBTHelper.getData(stack);

                UUID networkId = tile.networkId;
                if(networkId == null) {
                    networkId = UUID.randomUUID();
                    tile.networkId = networkId;
                    tile.markForUpdate();
                }

                NBTHelper.setUUID(compound, "networkId", networkId);
                return true;
            } else {
                //TODO: message
                return super.onItemUseFirst(stack, player, world, x, y, z, side, hitX, hitY, hitZ);
            }

        }
        return false;
    }

    @Override
    public boolean placeBlockAt(ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ, int metadata) {
        boolean placed = super.placeBlockAt(stack, player, world, x, y, z, side, hitX, hitY, hitZ, metadata);
        if (!world.isRemote && placed && stack.hasTagCompound()) {
            TileRemoteJar tile = (TileRemoteJar) world.getTileEntity(x, y, z);

            tile.networkId = NBTHelper.getUUID(stack.getTagCompound(), "networkId");
            tile.markForUpdate();
        }
        return placed;
    }

    private TileRemoteJar getJarTile(World world, int x, int y, int z) {
        TileEntity tile = world.getTileEntity(x, y, z);
        if (tile != null) {
            if (tile instanceof TileRemoteJar) {
                return (TileRemoteJar) tile;
            }
            if (tile instanceof TileStickyJar && ((TileStickyJar) tile).getParent() instanceof TileRemoteJar) {
                return (TileRemoteJar) ((TileStickyJar) tile).getParent();
            }
        }
        return null;
    }

    @Override
    public EnumRarity getRarity(ItemStack stack) {
        return EnumRarity.uncommon;
    }

    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack item, EntityPlayer par2EntityPlayer, List list, boolean advancedItemTooltips) {
        if (item.hasTagCompound()) {
            /*int lx = item.stackTagCompound.getInteger("linkX");
            int ly = item.stackTagCompound.getInteger("linkY");
            int lz = item.stackTagCompound.getInteger("linkZ");
            int ldim = item.stackTagCompound.getInteger("linkDim");
            String dimname = item.stackTagCompound.getString("dimname");
            list.add("Linked to " + lx + "," + ly + "," + lz + " in " + dimname);*/
        }
    }
}
