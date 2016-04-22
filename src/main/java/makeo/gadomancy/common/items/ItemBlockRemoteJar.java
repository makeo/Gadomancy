package makeo.gadomancy.common.items;

import makeo.gadomancy.client.events.ResourceReloadListener;
import makeo.gadomancy.common.blocks.BlockRemoteJar;
import makeo.gadomancy.common.blocks.tiles.TileRemoteJar;
import makeo.gadomancy.common.registration.RegisteredBlocks;
import makeo.gadomancy.common.utils.NBTHelper;
import makeo.gadomancy.common.utils.StringHelper;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.api.blocks.BlocksTC;
import thaumcraft.common.blocks.devices.BlockJarItem;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
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
    public ItemStack onItemRightClick(ItemStack stack, World world, EntityPlayer player) {
        if(!world.isRemote) {
            if(player.isSneaking()) {
                NBTTagCompound compound = NBTHelper.getData(stack);
                if(compound.hasKey("networkId")) {
                    compound.removeTag("networkId");

                    if(compound.hasNoTags()) {
                        stack.setTagCompound(null);
                    }

                    player.addChatComponentMessage(new ChatComponentTranslation("gadomancy.info.RemoteJar.clear"));
                }
            }
        }

        return super.onItemRightClick(stack, world, player);
    }

    @Override
    public boolean onItemUse(ItemStack stack, EntityPlayer playerIn, World worldIn, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ) {
        return super.onItemUse(stack, playerIn, worldIn, pos, side, hitX, hitY, hitZ);
    }

    @Override
    public boolean onItemUseFirst(ItemStack stack, EntityPlayer player, World world, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ) {
        TileRemoteJar tile = BlockRemoteJar.getJarTile(world, pos);
        if (tile != null) {
            if(!world.isRemote) {
                NBTTagCompound compound = NBTHelper.getData(stack);
                if(!player.isSneaking()) {
                    UUID networkId = null;
                    if(tile.networkId == null) {
                        player.addChatComponentMessage(new ChatComponentTranslation("gadomancy.info.RemoteJar.new"));
                        networkId = UUID.randomUUID();
                        tile.networkId = networkId;
                        tile.markForUpdate();
                    } else {
                        UUID current = NBTHelper.getUUID(compound, "networkId");
                        if(current == null || !current.equals(tile.networkId)) {
                            player.addChatComponentMessage(new ChatComponentTranslation("gadomancy.info.RemoteJar.connected"));
                            networkId = tile.networkId;
                        }
                    }

                    if(networkId != null) {
                        NBTHelper.setUUID(compound, "networkId", networkId);
                    }
                }
                return true;
            } else {
                if(player.isSneaking()) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public boolean placeBlockAt(ItemStack stack, EntityPlayer player, World world, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ, IBlockState newState) {
        boolean placed = super.placeBlockAt(stack, player, world, pos, side, hitX, hitY, hitZ, newState);
        if (placed && stack.hasTagCompound()) {
            TileRemoteJar tile = (TileRemoteJar) world.getTileEntity(pos);

            AspectList aspects = ((BlockJarItem) Item.getItemFromBlock(BlocksTC.jar)).getAspects(stack);
            if(aspects != null) {
                tile.aspect = aspects.getAspects()[0];
                tile.amount = aspects.getAmount(tile.aspect);
            }

            if(!world.isRemote) {
                tile.networkId = NBTHelper.getUUID(stack.getTagCompound(), "networkId");
                tile.markForUpdate();
            }
        }
        return placed;
    }

    @Override
    public EnumRarity getRarity(ItemStack stack) {
        return EnumRarity.UNCOMMON;
    }

    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, EntityPlayer player, List list, boolean advancedItemTooltips) {
        if (stack.hasTagCompound()) {
            UUID networkId = NBTHelper.getUUID(stack.getTagCompound(), "networkId");
            if(networkId != null) {
                list.add(String.format(StatCollector.translateToLocal("gadomancy.lore.remotejar"), generateName(networkId)));
            }
        }
        ((BlockJarItem) Item.getItemFromBlock(BlocksTC.jar)).addInformation(stack, player, list, advancedItemTooltips);
    }

    public static String generateName(UUID uuid) {
        List<String> values = new ArrayList<String>(ResourceReloadListener.languageList.values());

        Random random = new Random(uuid.getLeastSignificantBits());

        StringBuilder result = new StringBuilder();

        for(int i = 0; i < 2; i++) {
            int entryIndex = random.nextInt(values.size());
            String[] split = values.get(entryIndex).split(" ");
            String part = split[random.nextInt(split.length)];

            if(!isValidName(part)) {
                i--;
                continue;
            }
            result.append(StringHelper.firstToUpper(part.toLowerCase()));
        }
        return result.toString().trim();
    }

    private static boolean isValidName(String name) {
        if(name.length() <= 8 && name.length() >= 4) {
            for(char c : name.toCharArray()) {
                if(!Character.isAlphabetic(c)) {
                    return false;
                }
            }
            return true;
        }
        return false;
    }
}