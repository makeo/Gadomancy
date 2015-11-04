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
import java.util.Map;

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
    public ItemStack getWailaStack(final IWailaDataAccessor data, final IWailaConfigHandler config) {
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
    public List<String> getWailaHead(final ItemStack stack, final List<String> strings, final IWailaDataAccessor data, final IWailaConfigHandler config) {
        TileStickyJar tile = (TileStickyJar) data.getTileEntity();
        if(tile != null && tile.isValid()) {
            callParentProviders(ModuleRegistrar.instance().getBodyProviders(tile.getParentBlock()), new Callable<IWailaDataProvider>() {
                @Override
                public void call(IWailaDataProvider provider) {
                    provider.getWailaHead(stack, strings, data, config);
                }
            });
        }
        return strings;
    }

    @Override
    public List<String> getWailaBody(final ItemStack stack, final List<String> strings, final IWailaDataAccessor data, final IWailaConfigHandler config) {
        TileStickyJar tile = (TileStickyJar) data.getTileEntity();
        if(tile != null && tile.isValid()) {
            callParentProviders(ModuleRegistrar.instance().getBodyProviders(tile.getParentBlock()), new Callable<IWailaDataProvider>() {
                @Override
                public void call(IWailaDataProvider provider) {
                   provider.getWailaBody(stack, strings, data, config);
                }
            });
        }
        return strings;
    }

    @Override
    public List<String> getWailaTail(final ItemStack stack, final List<String> strings, final IWailaDataAccessor data, final IWailaConfigHandler config) {
        TileStickyJar tile = (TileStickyJar) data.getTileEntity();
        if(tile != null && tile.isValid()) {
            callParentProviders(ModuleRegistrar.instance().getBodyProviders(tile.getParentBlock()), new Callable<IWailaDataProvider>() {
                @Override
                public void call(IWailaDataProvider provider) {
                    provider.getWailaTail(stack, strings, data, config);
                }
            });
        }

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

    private void callParentProviders(Map<Integer, List<IWailaDataProvider>> providers, Callable<IWailaDataProvider> callable) {
        for(Map.Entry<Integer, List<IWailaDataProvider>> entry : providers.entrySet()) {
            for(IWailaDataProvider provider : entry.getValue()) {
                callable.call(provider);
            }
        }
    }

    private abstract static class Callable<T> {
        public abstract void call(IWailaDataProvider provider);
    }
}
