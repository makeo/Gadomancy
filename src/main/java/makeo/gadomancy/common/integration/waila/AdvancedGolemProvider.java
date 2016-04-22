package makeo.gadomancy.common.integration.waila;

/**
 * This class is part of the Gadomancy Mod
 * Gadomancy is Open Source and distributed under the
 * GNU LESSER GENERAL PUBLIC LICENSE
 * for more read the LICENSE file
 *
 * Created by makeo @ 25.07.2015 14:14
 */
public class AdvancedGolemProvider/* implements IWailaEntityProvider*/ {
    /*
    @Override
    public Entity getWailaOverride(IWailaEntityAccessor data, IWailaConfigHandler config) {
        return null;
    }

    @Override
    public List<String> getWailaHead(Entity paramEntity, List<String> strings, IWailaEntityAccessor data, IWailaConfigHandler config) {
        return null;
    }

    @Override
    public List<String> getWailaBody(Entity paramEntity, List<String> strings, IWailaEntityAccessor data, IWailaConfigHandler config) {
        return null;
    }

    @Override
    public List<String> getWailaTail(Entity paramEntity, List<String> strings, IWailaEntityAccessor data, IWailaConfigHandler config) {
        if(data.getEntity() instanceof EntityGolemBase) {
            AdditionalGolemType type = GadomancyApi.getAdditionalGolemType(((EntityGolemBase) data.getEntity()).getGolemType());
            if(type != null && strings.size() > 0) {
                String oldMod = strings.get(strings.size() - 1);

                ModContainer container = Loader.instance().getIndexedModList().get(type.getModId());
                if(container != null) {
                    String mod = ColorHelper.extractColors(oldMod) + container.getName();

                    strings.remove(strings.size() - 1);
                    strings.add(mod);
                }
            }
        }
        return strings;
    }

    @Override
    public NBTTagCompound getNBTData(EntityPlayerMP paramEntityPlayerMP, Entity paramEntity, NBTTagCompound paramNBTTagCompound, World paramWorld) {
        return null;
    }*/
}
