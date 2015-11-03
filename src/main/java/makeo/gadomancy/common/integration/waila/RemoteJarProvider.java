package makeo.gadomancy.common.integration.waila;

import makeo.gadomancy.common.blocks.BlockRemoteJar;
import makeo.gadomancy.common.blocks.tiles.TileRemoteJar;
import makeo.gadomancy.common.items.ItemBlockRemoteJar;
import mcp.mobius.waila.api.IWailaConfigHandler;
import mcp.mobius.waila.api.IWailaDataAccessor;
import mcp.mobius.waila.api.IWailaDataProvider;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;

import java.util.List;

/**
 * This class is part of the Gadomancy Mod
 * Gadomancy is Open Source and distributed under the
 * GNU LESSER GENERAL PUBLIC LICENSE
 * for more read the LICENSE file
 * <p/>
 * Created by makeo @ 20.10.2015 22:17
 */
public class RemoteJarProvider implements IWailaDataProvider {
    @Override
    public ItemStack getWailaStack(IWailaDataAccessor dataAccessor, IWailaConfigHandler iWailaConfigHandler) {
        return null;
    }

    @Override
    public List<String> getWailaHead(ItemStack stack, List<String> strings, IWailaDataAccessor dataAccessor, IWailaConfigHandler iWailaConfigHandler) {
        return null;
    }

    @Override
    public List<String> getWailaBody(ItemStack stack, List<String> strings, IWailaDataAccessor dataAccessor, IWailaConfigHandler iWailaConfigHandler) {
        TileRemoteJar tile = BlockRemoteJar.getJarTile(dataAccessor.getTileEntity());
        if(tile.networkId != null) {
            strings.add(String.format(StatCollector.translateToLocal("gadomancy.lore.remotejar"), ItemBlockRemoteJar.generateName(tile.networkId)));
        }
        return strings;
    }

    @Override
    public List<String> getWailaTail(ItemStack stack, List<String> strings, IWailaDataAccessor dataAccessor, IWailaConfigHandler iWailaConfigHandler) {
        return null;
    }

    @Override
    public NBTTagCompound getNBTData(EntityPlayerMP entityPlayerMP, TileEntity tileEntity, NBTTagCompound compound, World world, int i, int i2, int i3) {
        return null;
    }
}
