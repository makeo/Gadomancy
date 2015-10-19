package makeo.gadomancy.common.integration.waila;

import makeo.gadomancy.common.Gadomancy;
import makeo.gadomancy.common.blocks.tiles.TileStickyJar;
import makeo.gadomancy.common.utils.ColorHelper;
import mcp.mobius.waila.api.IWailaConfigHandler;
import mcp.mobius.waila.api.IWailaDataAccessor;
import mcp.mobius.waila.api.IWailaDataProvider;
import mcp.mobius.waila.api.impl.ModuleRegistrar;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * This class is part of the Gadomancy Mod
 * Gadomancy is Open Source and distributed under the
 * GNU LESSER GENERAL PUBLIC LICENSE
 * for more read the LICENSE file
 *
 * Created by makeo @ 24.07.2015 16:43
 */
public class StickyJarProvider implements IWailaDataProvider {
    @Override
    public ItemStack getWailaStack(IWailaDataAccessor data, IWailaConfigHandler config) {
        if(data.getTileEntity() instanceof TileStickyJar) {
            TileStickyJar stickyJar = (TileStickyJar) data.getTileEntity();

            if(stickyJar.isValid()) {
                ItemStack stack = new ItemStack(stickyJar.getParentBlock(), 1, stickyJar.getBlockMetadata());
                for(IWailaDataProvider provider : getParentProviders(ModuleRegistrar.instance().stackBlockProviders, stickyJar.getParentBlock().getClass(), stickyJar.getParent().getClass())) {
                    stack = provider.getWailaStack(data, config);
                }
                return stack;
            }
        }
        return null;
    }

    private List<IWailaDataProvider> getParentProviders(LinkedHashMap<Class, ArrayList<IWailaDataProvider>> providerMap, Class... classes) {
        List<IWailaDataProvider> providers = new ArrayList<IWailaDataProvider>();
        for(Class parent : classes) {
            ArrayList<IWailaDataProvider> providerList = providerMap.get(parent);
            if(providerList != null)
                providers.addAll(providerList);
        }
        return providers;
    }

    @Override
    public List<String> getWailaHead(ItemStack stack, List<String> strings, IWailaDataAccessor data, IWailaConfigHandler config) {
        return null;
    }

    @Override
    public List<String> getWailaBody(ItemStack stack, List<String> strings, IWailaDataAccessor data, IWailaConfigHandler config) {
        return null;
    }

    @Override
    public List<String> getWailaTail(ItemStack stack, List<String> strings, IWailaDataAccessor data, IWailaConfigHandler config) {
        if(data.getTileEntity() instanceof TileStickyJar) {
            if(strings.size() > 0) {
                String oldMod = strings.get(strings.size() - 1);
                String mod = ColorHelper.extractColors(oldMod) + Gadomancy.NAME;
                strings.remove(strings.size() - 1);
                strings.add(mod);
            }
        }
        return strings;
    }

    @Override
    public NBTTagCompound getNBTData(EntityPlayerMP paramEntityPlayerMP, TileEntity paramTileEntity, NBTTagCompound paramNBTTagCompound, World paramWorld, int paramInt1, int paramInt2, int paramInt3) {
        return null;
    }
}
