package makeo.gadomancy.coremod;

import net.minecraftforge.fml.relauncher.IFMLCallHook;
import net.minecraftforge.fml.relauncher.IFMLLoadingPlugin;

import java.util.Map;

/**
 * This class is part of the Gadomancy Mod
 * Gadomancy is Open Source and distributed under the
 * GNU LESSER GENERAL PUBLIC LICENSE
 * for more read the LICENSE file
 * <p/>
 * Created by makeo @ 07.12.2015 21:17
 */
@IFMLLoadingPlugin.TransformerExclusions({"makeo.gadomancy.coremod"})
public class GadomancyCore implements IFMLLoadingPlugin, IFMLCallHook {

    public GadomancyCore() {
        System.out.println("[GadomancyCore] Initialized.");
    }

    @Override
    public Void call() throws Exception {
        return null;
    }

    @Override
    public String[] getASMTransformerClass() {
        return null;
    }

    @Override
    public String getModContainerClass() {
        return null;
    }

    @Override
    public String getSetupClass() {
        return null;
    }

    @Override
    public void injectData(Map<String, Object> data) {}

    @Override
    public String getAccessTransformerClass() {
        return "makeo.gadomancy.coremod.GadomancyTransformer";
    }
}
