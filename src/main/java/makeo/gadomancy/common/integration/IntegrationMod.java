package makeo.gadomancy.common.integration;

import cpw.mods.fml.common.Loader;

/**
 * This class is part of the Gadomancy Mod
 * Gadomancy is Open Source and distributed under the
 * GNU LESSER GENERAL PUBLIC LICENSE
 * for more read the LICENSE file
 *
 * Created by makeo @ 09.07.2015 15:29
 */
public abstract class IntegrationMod {
    public abstract String getModId();

    private boolean isPresent = false;

    public final boolean isPresent() {
        return isPresent;
    }

    public final void init() {
        if(Loader.isModLoaded(getModId())) {
            isPresent = true;
            doInit();
        }
    }

    protected abstract void doInit();
}
