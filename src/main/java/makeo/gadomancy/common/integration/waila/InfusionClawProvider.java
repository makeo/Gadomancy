package makeo.gadomancy.common.integration.waila;

import makeo.gadomancy.common.blocks.tiles.TileInfusionClaw;
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
 *
 * Created by makeo @ 07.10.2015 18:02
 */
public class InfusionClawProvider implements IWailaDataProvider {
    @Override
    public ItemStack getWailaStack(IWailaDataAccessor paramIWailaDataAccessor, IWailaConfigHandler paramIWailaConfigHandler) {
        return null;
    }

    @Override
    public List<String> getWailaHead(ItemStack paramItemStack, List<String> paramList, IWailaDataAccessor paramIWailaDataAccessor, IWailaConfigHandler paramIWailaConfigHandler) {
        return null;
    }

    @Override
    public List<String> getWailaBody(ItemStack paramItemStack, List<String> strings, IWailaDataAccessor dataAccessor, IWailaConfigHandler paramIWailaConfigHandler) {
        TileInfusionClaw tile = (TileInfusionClaw) dataAccessor.getTileEntity();
        if(tile.hasOwner()) {
            strings.add(StatCollector.translateToLocal("gadomancy.info.InfusionClaw.waila") + " " + tile.getOwner());
        }
        return strings;
    }

    @Override
    public List<String> getWailaTail(ItemStack paramItemStack, List<String> paramList, IWailaDataAccessor dataAccessor, IWailaConfigHandler paramIWailaConfigHandler) {
        return null;
    }

    @Override
    public NBTTagCompound getNBTData(EntityPlayerMP paramEntityPlayerMP, TileEntity paramTileEntity, NBTTagCompound paramNBTTagCompound, World paramWorld, int paramInt1, int paramInt2, int paramInt3) {
        return null;
    }
}
