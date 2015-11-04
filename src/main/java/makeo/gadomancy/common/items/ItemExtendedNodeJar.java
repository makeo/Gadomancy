package makeo.gadomancy.common.items;

import makeo.gadomancy.common.blocks.tiles.TileExtendedNodeJar;
import makeo.gadomancy.common.node.ExtendedNodeType;
import makeo.gadomancy.common.registration.RegisteredBlocks;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagInt;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.api.aspects.IEssentiaContainerItem;
import thaumcraft.api.nodes.NodeModifier;
import thaumcraft.api.nodes.NodeType;
import thaumcraft.common.Thaumcraft;
import thaumcraft.common.blocks.ItemJarNode;
import thaumcraft.common.config.ConfigItems;

import java.util.List;

/**
 * This class is part of the Gadomancy Mod
 * Gadomancy is Open Source and distributed under the
 * GNU LESSER GENERAL PUBLIC LICENSE
 * for more read the LICENSE file
 * <p/>
 * Original created by Azanor@Thaumcraft: thaumcraft.common.blocks.ItemJarNode
 * Modified to create compatibility with ExtendedNodes
 * Created by HellFirePvP @ 25.10.2015 22:33
 */
public class ItemExtendedNodeJar extends Item implements IEssentiaContainerItem {

    public ItemExtendedNodeJar() {
        setMaxDamage(0);
        setMaxStackSize(1);
        setUnlocalizedName("BlockJarNodeItem");
    }

    @Override
    public void registerIcons(IIconRegister p_94581_1_) {}

    @Override
    public IIcon getIcon(ItemStack stack, int pass) {
        return ((ItemJarNode) ConfigItems.itemJarNode).icon;
    }

    @Override
    public IIcon getIconFromDamage(int dmg) {
        return ((ItemJarNode) ConfigItems.itemJarNode).icon;
    }

    @Override
    public int getMetadata(int meta) {
        return meta;
    }

    @Override
    public void addInformation(ItemStack stack, EntityPlayer player, List list, boolean par4) {
        String desc = "§9" + StatCollector.translateToLocal("nodetype." + getNodeType(stack) + ".name");
        if(getExtendedNodeType(stack) != null) {
            desc = desc + ", " + StatCollector.translateToLocal("gadomancy.nodes." + getExtendedNodeType(stack));
        }
        if (getNodeModifier(stack) != null) {
            desc = desc + ", " + StatCollector.translateToLocal("nodemod." + getNodeModifier(stack) + ".name");
        }
        list.add(desc);
        AspectList aspects = getAspects(stack);
        if ((aspects != null) && (aspects.size() > 0)) {
            for (Aspect tag : aspects.getAspectsSorted()) {
                if (Thaumcraft.proxy.playerKnowledge.hasDiscoveredAspect(player.getCommandSenderName(), tag)) {
                    list.add(tag.getName() + " x " + aspects.getAmount(tag));
                } else {
                    list.add(StatCollector.translateToLocal("tc.aspect.unknown"));
                }
            }
        }
        super.addInformation(stack, player, list, par4);
    }

    public boolean onItemUse(ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int side, float par8, float par9, float par10) {
        Block var11 = world.getBlock(x, y, z);
        if (var11 == Blocks.snow_layer) {
            side = 1;
        } else if ((var11 != Blocks.vine) && (var11 != Blocks.tallgrass) && (var11 != Blocks.deadbush) && ((var11.isAir(world, x, y, z)) || (!var11.isReplaceable(world, x, y, z)))) {
            if (side == 0) {
                y--;
            }
            if (side == 1) {
                y++;
            }
            if (side == 2) {
                z--;
            }
            if (side == 3) {
                z++;
            }
            if (side == 4) {
                x--;
            }
            if (side == 5) {
                x++;
            }
        }
        if (stack.stackSize == 0) {
            return false;
        }
        if (!player.canPlayerEdit(x, y, z, side, stack)) {
            return false;
        }
        if ((y == 255) && (RegisteredBlocks.blockExtendedNodeJar.getMaterial().isSolid())) {
            return false;
        }
        if (world.canPlaceEntityOnSide(RegisteredBlocks.blockExtendedNodeJar, x, y, z, false, side, player, stack)) {
            Block var12 = RegisteredBlocks.blockExtendedNodeJar;
            int var13 = 2;
            int var14 = RegisteredBlocks.blockExtendedNodeJar.onBlockPlaced(world, x, y, z, side, par8, par9, par10, var13);
            if (placeBlockAt(stack, player, world, x, y, z, side, par8, par9, par10, var14)) {
                TileEntity te = world.getTileEntity(x, y, z);

                if ((te != null) && ((te instanceof TileExtendedNodeJar))) {
                    if (stack.hasTagCompound()) {
                        AspectList aspects = getAspects(stack);
                        if (aspects != null) {
                            ((TileExtendedNodeJar) te).setAspects(aspects);
                            ((TileExtendedNodeJar) te).setNodeType(getNodeType(stack));
                            ((TileExtendedNodeJar) te).setNodeModifier(getNodeModifier(stack));
                            ((TileExtendedNodeJar) te).setExtendedNodeType(getExtendedNodeType(stack));
                            ((TileExtendedNodeJar) te).setId(getNodeId(stack));
                            ((TileExtendedNodeJar) te).setBehaviorSnapshot(getBehaviorSnapshot(stack));
                        }
                    }
                }


                world.playSoundEffect(x + 0.5F, y + 0.5F, z + 0.5F, var12.stepSound.getStepResourcePath(), (var12.stepSound.getVolume() + 1.0F) / 2.0F, var12.stepSound.getPitch() * 0.8F);
                stack.stackSize -= 1;
            }
            return true;
        }
        return false;
    }

    public boolean placeBlockAt(ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ, int metadata) {
        if (!world.setBlock(x, y, z, RegisteredBlocks.blockExtendedNodeJar, metadata, 3)) {
            return false;
        }
        if (world.getBlock(x, y, z) == RegisteredBlocks.blockExtendedNodeJar) {
            RegisteredBlocks.blockExtendedNodeJar.onBlockPlacedBy(world, x, y, z, player, stack);
            RegisteredBlocks.blockExtendedNodeJar.onPostBlockPlaced(world, x, y, z, metadata);
        }
        return true;
    }

    @Override
    public AspectList getAspects(ItemStack itemstack) {
        if (itemstack.hasTagCompound()) {
            AspectList aspects = new AspectList();
            aspects.readFromNBT(itemstack.getTagCompound());
            return aspects.size() > 0 ? aspects : null;
        }
        return null;
    }

    @Override
    public void setAspects(ItemStack itemstack, AspectList aspects) {
        if (!itemstack.hasTagCompound()) {
            itemstack.setTagCompound(new NBTTagCompound());
        }
        aspects.writeToNBT(itemstack.getTagCompound());
    }

    public void setNodeAttributes(ItemStack itemstack, NodeType type, NodeModifier mod, ExtendedNodeType extendedNodeType, String id) {
        if (!itemstack.hasTagCompound()) {
            itemstack.setTagCompound(new NBTTagCompound());
        }
        itemstack.setTagInfo("nodetype", new NBTTagInt(type.ordinal()));
        if (mod != null) {
            itemstack.setTagInfo("nodemod", new NBTTagInt(mod.ordinal()));
        }
        if(extendedNodeType != null) {
            itemstack.setTagInfo("nodeExMod", new NBTTagInt(extendedNodeType.ordinal()));
        }
        itemstack.setTagInfo("nodeid", new NBTTagString(id));
    }

    public void setBehaviorSnapshot(ItemStack itemstack, NBTTagCompound tagCompound) {
        if(tagCompound == null) return;
        if (!itemstack.hasTagCompound()) {
            itemstack.setTagCompound(new NBTTagCompound());
        }
        itemstack.setTagInfo("Behavior", tagCompound);
    }

    public NBTTagCompound getBehaviorSnapshot(ItemStack itemstack) {
        if (!itemstack.hasTagCompound() || (!itemstack.getTagCompound().hasKey("Behavior"))) {
            return null;
        }
        return itemstack.getTagCompound().getCompoundTag("Behavior");
    }

    public ExtendedNodeType getExtendedNodeType(ItemStack itemStack) {
        if (!itemStack.hasTagCompound() || (!itemStack.getTagCompound().hasKey("nodeExMod"))) {
            return null;
        }
        return ExtendedNodeType.values()[itemStack.getTagCompound().getInteger("nodeExMod")];
    }

    public NodeType getNodeType(ItemStack itemstack) {
        if (!itemstack.hasTagCompound()) {
            return null;
        }
        return NodeType.values()[itemstack.getTagCompound().getInteger("nodetype")];
    }

    public NodeModifier getNodeModifier(ItemStack itemstack) {
        if ((!itemstack.hasTagCompound()) || (!itemstack.getTagCompound().hasKey("nodemod"))) {
            return null;
        }
        return NodeModifier.values()[itemstack.getTagCompound().getInteger("nodemod")];
    }

    public String getNodeId(ItemStack itemstack) {
        if (!itemstack.hasTagCompound()) {
            return "0";
        }
        return itemstack.getTagCompound().getString("nodeid");
    }

}
