package makeo.gadomancy.common.blocks;

import makeo.gadomancy.common.Gadomancy;
import makeo.gadomancy.common.blocks.tiles.*;
import makeo.gadomancy.common.registration.RegisteredBlocks;
import makeo.gadomancy.common.registration.RegisteredItems;
import makeo.gadomancy.common.utils.ItemUtils;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.IIcon;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import thaumcraft.common.config.ConfigBlocks;
import thaumcraft.common.lib.utils.InventoryUtils;
import thaumcraft.common.tiles.TileJarFillable;
import thaumcraft.common.tiles.TilePedestal;

import java.util.List;
import java.util.Random;

/**
 * This class is part of the Gadomancy Mod
 * Gadomancy is Open Source and distributed under the
 * GNU LESSER GENERAL PUBLIC LICENSE
 * for more read the LICENSE file
 * <p/>
 * Created by makeo @ 27.10.2015 13:16
 */
public class BlockStoneMachine extends Block {
    public BlockStoneMachine() {
        super(Material.rock);
        setHardness(3.0F);
        setResistance(25.0F);
        setStepSound(Block.soundTypeStone);
        setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
        setCreativeTab(RegisteredItems.creativeTab);
    }

    private IIcon pillarIcon;
    private IIcon focusIcon;
    public IIcon pedestalSideIcon;
    public IIcon pedestalTopIcon;
    public IIcon protectorIcon;
    public IIcon packagerIcon;

    @Override
    public void registerBlockIcons(IIconRegister ir) {
        pillarIcon = ir.registerIcon("thaumcraft:es_1");
        focusIcon = ir.registerIcon("thaumcraft:thaumiumblock");
        pedestalTopIcon = ir.registerIcon(Gadomancy.MODID + ":manipulator_bot");
        pedestalSideIcon = ir.registerIcon(Gadomancy.MODID + ":ancient_pedestal_side");
        protectorIcon = ir.registerIcon("thaumcraft:pipe_1");
        packagerIcon = ir.registerIcon("thaumcraft:planks_greatwood");
    }

    @Override
    public IIcon getIcon(int side, int metadata) {
        if (metadata == 11 || metadata == 15) {
            return pillarIcon;
        } else if (metadata == 0 || metadata == 3) {
            return focusIcon;
        } else if (metadata == 1) {
            if (side > 1) {
                return pedestalSideIcon;
            } else {
                return pedestalTopIcon;
            }
        } else if (metadata == 2) {
            return protectorIcon;
        } else if(metadata == 4) {
            return packagerIcon;
        }

        return super.getIcon(side, metadata);
    }

    @Override
    public boolean hasTileEntity(int metadata) {
        return metadata == 15 || metadata == 0 || metadata == 1 || metadata == 2 || metadata == 3 || metadata == 4 || metadata == 5;
    }

    @Override
    public void getSubBlocks(Item item, CreativeTabs tab, List list) {
        for (int i = 0; i < 5; i++) {
            list.add(new ItemStack(item, 1, i));
        }
    }

    @Override
    public ItemStack getPickBlock(MovingObjectPosition target, World world, int x, int y, int z, EntityPlayer player) {
        int metadata = world.getBlockMetadata(x, y, z);
        if (metadata == 11 || metadata == 15) {
            return new ItemStack(getItemDropped(metadata, null, 0), 1, damageDropped(metadata));
        }

        return super.getPickBlock(target, world, x, y, z, player);
    }

    @Override
    public TileEntity createTileEntity(World world, int metadata) {
        if (metadata == 15) {
            return new TileManipulatorPillar();
        } else if (metadata == 0) {
            return new TileManipulationFocus();
        } else if (metadata == 1) {
            return new TilePedestal();
        } else if (metadata == 2) {
            return new TileBlockProtector();
        } else if (metadata == 3) {
            return new TileManipulationFocus();
        } else if(metadata == 4) {
            return new TileArcanePackager();
        }/* else if(metadata == 5) {
            return new TileAIShutdown();
        }*/
        return null;
    }

    @Override
    public int damageDropped(int metadata) {
        return metadata;
    }

    @Override
    public Item getItemDropped(int metadata, Random random, int fortune) {
        if (metadata == 11 || metadata == 15) {
            return Item.getItemFromBlock(ConfigBlocks.blockCosmeticSolid);
        }

        return super.getItemDropped(metadata, random, fortune);
    }

    public void breakBlock(World world, int x, int y, int z, Block block, int metadata) {
        if (metadata == 1) {
            InventoryUtils.dropItems(world, x, y, z);
        } else if(metadata == 4) {
            if(!world.isRemote) {
                TileArcanePackager tile = (TileArcanePackager) world.getTileEntity(x, y, z);

                for(int i = 0; i < tile.getSizeInventory(); i++) {
                    ItemStack stack = tile.getStackInSlot(i);
                    if(stack != null && stack.stackSize > 0) {
                        float f = world.rand.nextFloat() * 0.8F + 0.1F;
                        float f1 = world.rand.nextFloat() * 0.8F + 0.1F;
                        float f2 = world.rand.nextFloat() * 0.8F + 0.1F;

                        EntityItem entityItem = new EntityItem(world, x + f, y + f1, z + f2, stack.copy());
                        ItemUtils.applyRandomDropOffset(entityItem, world.rand);

                        world.spawnEntityInWorld(entityItem);
                        tile.setInventorySlotContents(i, null);
                    }
                }
            }
        }


        super.breakBlock(world, x, y, z, block, metadata);
    }

    @Override
    public boolean canReplace(World world, int x, int y, int z, int side, ItemStack stack) {
        int damage = stack.getItemDamage();
        if ((damage == 0 || damage == 3) && (y < 1 || world.getBlock(x, y - 1, z) != RegisteredBlocks.blockNodeManipulator)) {
            return false;
        }
        return super.canReplace(world, x, y, z, side, stack);
    }

    @Override
    public void onBlockPlacedBy(World world, int x, int y, int z, EntityLivingBase ent, ItemStack stack) {
        int metadata = world.getBlockMetadata(x, y, z);
        if (metadata == 2 || metadata == 4) {
            ((TileJarFillable) world.getTileEntity(x, y, z)).facing = MathHelper.floor_double(-ent.rotationYaw * 4.0F / 360.0F + 0.5D) & 0x3;
        }
    }

    @Override
    public void onNeighborBlockChange(World world, int x, int y, int z, Block block) {
        int metadata = world.getBlockMetadata(x, y, z);
        if (metadata == 15) {
            if (world.getBlock(x, y + 1, z) != this || world.getBlockMetadata(x, y + 1, z) != 11) {
                dropBlockAsItem(world, x, y, z, metadata, 0);
                world.setBlockToAir(x, y, z);
            }
        } else if (metadata == 11) {
            if (world.getBlock(x, y - 1, z) != this || world.getBlockMetadata(x, y - 1, z) != 15) {
                dropBlockAsItem(world, x, y, z, metadata, 0);
                world.setBlockToAir(x, y, z);
            }
        } else if (metadata == 0 || metadata == 3) {
            if (world.getBlock(x, y - 1, z) != RegisteredBlocks.blockNodeManipulator || world.getBlockMetadata(x, y - 1, z) != 5) {
                dropBlockAsItem(world, x, y, z, metadata, 0);
                world.setBlockToAir(x, y, z);
            }
        } else if(metadata == 1) {
            if (!world.isAirBlock(x, y + 1, z)) {
                InventoryUtils.dropItems(world, x, y, z);
            }
        } else if(metadata == 4) {
            if(!world.isRemote) {
                TileArcanePackager tile = (TileArcanePackager) world.getTileEntity(x, y, z);
                tile.updateRedstone(world.isBlockIndirectlyGettingPowered(x, y, z));
            }
        }
    }

    @Override
    public int getLightValue(IBlockAccess world, int x, int y, int z) {
        int metadata = world.getBlockMetadata(x, y, z);
        if(metadata == 2) {
            TileBlockProtector protector = (TileBlockProtector) world.getTileEntity(x, y, z);
            return protector.getCurrentRange();
        }
        return super.getLightValue(world, x, y, z);
    }

    @Override
    public boolean hasComparatorInputOverride() {
        return true;
    }

    @Override
    public int getComparatorInputOverride(World world, int x, int y, int z, int side) {
        int metadata = world.getBlockMetadata(x, y, z);
        if(metadata == 2) {
            TileBlockProtector protector = (TileBlockProtector) world.getTileEntity(x, y, z);
            return protector.getCurrentRange();
        }
        return 0;
    }

    @Override
    public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int side, float hitX, float hitY, float hitZ) {
        int metadata = world.getBlockMetadata(x, y, z);

        if (metadata == 0 || metadata == 3) {
            if (world.getBlock(x, y - 1, z).equals(RegisteredBlocks.blockNodeManipulator) && world.getBlockMetadata(x, y - 1, z) == 5) {
                return world.getBlock(x, y - 1, z).onBlockActivated(world, x, y - 1, z, player, side, hitX, hitY, hitZ);
            }
        } else if (metadata == 1) {
            if(world.isAirBlock(x, y + 1, z)) {
                return ConfigBlocks.blockStoneDevice.onBlockActivated(world, x, y, z, player, side, hitX, hitY, hitZ);
            }
            return true;
        } else if(metadata == 4) {
            player.openGui(Gadomancy.instance, 2, world, x, y, z);
            return true;
        }

        return super.onBlockActivated(world, x, y, z, player, side, hitX, hitY, hitZ);
    }

    @Override
    public void setBlockBoundsBasedOnState(IBlockAccess world, int x, int y, int z) {
        int metadata = world.getBlockMetadata(x, y, z);
        if (metadata == 15) {
            setBlockBounds(0, 0, 0, 1, 0.5f, 1);
        } else if (metadata == 11) {
            setBlockBounds(0.0F, -1.0F, 0.0F, 1.0F, -0.5F, 1.0F);
        } else if (metadata == 0 || metadata == 3) {
            setBlockBounds(3 / 16f, 0, 3 / 16f, 1 - (3 / 16f), 6 / 16f, 1 - (3 / 16f));
        } else if (metadata == 1) {
            setBlockBounds(0.25f, 0, 0.25f, 0.75f, 0.99f, 0.75f);
        } else if(metadata == 2) {
            setBlockBounds(0, 0, 0, 1, 3/16f, 1);
        } else if(metadata == 4) {
             setBlockBounds(0, 0, 0, 1, 12/16f, 1);
        }/* else if(metadata == 5) {
            setBlockBounds(0, 0, 0, 1, 1, 1);
        }*/
        super.setBlockBoundsBasedOnState(world, x, y, z);
    }

    @Override
    public void addCollisionBoxesToList(World world, int x, int y, int z, AxisAlignedBB axisalignedbb, List list, Entity entity) {
        int metadata = world.getBlockMetadata(x, y, z);
        if (metadata == 11 || metadata == 15 || metadata == 1) {
            setBlockBounds(0, 0, 0, 1, 1, 1);
        } else if(metadata == 2) {
            setBlockBounds(3 / 16f, 0, 3 / 16f, 1 - (3 / 16f), 9.5f/16f, 1 - (3 / 16f));
            super.addCollisionBoxesToList(world, x, y, z, axisalignedbb, list, entity);
            setBlockBoundsBasedOnState(world, x, y, z);
        } else {
            setBlockBoundsBasedOnState(world, x, y, z);
        }
        super.addCollisionBoxesToList(world, x, y, z, axisalignedbb, list, entity);
    }

    public boolean isOpaqueCube() {
        return false;
    }

    public boolean renderAsNormalBlock() {
        return false;
    }

    @Override
    public int getRenderType() {
        return RegisteredBlocks.rendererBlockStoneMachine;
    }
}
