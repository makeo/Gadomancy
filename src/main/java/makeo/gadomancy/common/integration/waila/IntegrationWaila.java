package makeo.gadomancy.common.integration.waila;

import makeo.gadomancy.common.blocks.BlockInfusionClaw;
import makeo.gadomancy.common.blocks.BlockRemoteJar;
import makeo.gadomancy.common.blocks.BlockStickyJar;
import makeo.gadomancy.common.integration.IntegrationMod;
import mcp.mobius.waila.api.impl.ModuleRegistrar;
import thaumcraft.common.entities.golems.EntityGolemBase;

/**
 * This class is part of the Gadomancy Mod
 * Gadomancy is Open Source and distributed under the
 * GNU LESSER GENERAL PUBLIC LICENSE
 * for more read the LICENSE file
 *
 * Created by makeo @ 24.07.2015 14:48
 */
public class IntegrationWaila extends IntegrationMod {
    @Override
    public String getModId() {
        return "Waila";
    }

    @Override
    protected void doInit() {
        registerProviders(ModuleRegistrar.instance());
    }

    public static void registerProviders(ModuleRegistrar registrar) {
        StickyJarProvider stickyJarProvider = new StickyJarProvider();
        registrar.registerTailProvider(stickyJarProvider, BlockStickyJar.class);
        registrar.registerStackProvider(stickyJarProvider, BlockStickyJar.class);
        registrar.registerHeadProvider(stickyJarProvider, BlockStickyJar.class);
        registrar.registerBodyProvider(stickyJarProvider, BlockStickyJar.class);

        registrar.registerTailProvider(new AdvancedGolemProvider(), EntityGolemBase.class);

        registrar.registerBodyProvider(new InfusionClawProvider(), BlockInfusionClaw.class);
        registrar.registerBodyProvider(new RemoteJarProvider(), BlockRemoteJar.class);
    }
}
