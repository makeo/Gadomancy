package makeo.gadomancy.common;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.network.IGuiHandler;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.relauncher.Side;
import makeo.gadomancy.client.ClientProxy;
import makeo.gadomancy.common.containers.ContainerInfusionClaw;
import makeo.gadomancy.common.data.ModConfig;
import makeo.gadomancy.common.events.EventHandlerEntity;
import makeo.gadomancy.common.events.EventHandlerGolem;
import makeo.gadomancy.common.events.EventHandlerNetwork;
import makeo.gadomancy.common.events.EventHandlerWorld;
import makeo.gadomancy.common.familiar.FamiliarHandlerServer;
import makeo.gadomancy.common.network.PacketHandler;
import makeo.gadomancy.common.registration.ModSubstitutions;
import makeo.gadomancy.common.registration.RegisteredBlocks;
import makeo.gadomancy.common.registration.RegisteredGolemStuff;
import makeo.gadomancy.common.registration.RegisteredIntegrations;
import makeo.gadomancy.common.registration.RegisteredItems;
import makeo.gadomancy.common.registration.RegisteredPotions;
import makeo.gadomancy.common.registration.RegisteredRecipes;
import makeo.gadomancy.common.registration.RegisteredResearches;
import makeo.gadomancy.common.utils.Injector;
import makeo.gadomancy.common.utils.world.WorldProviderTCEldrich;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.world.World;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.common.MinecraftForge;
import thaumcraft.api.wands.WandTriggerRegistry;
import thaumcraft.common.entities.golems.ContainerGolem;
import thaumcraft.common.entities.golems.EntityGolemBase;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

/**
 * This class is part of the Gadomancy Mod
 * Gadomancy is Open Source and distributed under the
 * GNU LESSER GENERAL PUBLIC LICENSE
 * for more read the LICENSE file
 *
 * Created by makeo @ 29.11.2014 14:18
 */
public class CommonProxy implements IGuiHandler {
    public static final EventHandlerGolem EVENT_HANDLER_GOLEM = new EventHandlerGolem();
    public FamiliarHandlerServer familiarHandler;

    public void onConstruct() { }

    public void preInitalize() {
        PacketHandler.init();

        RegisteredItems.preInit();

        RegisteredBlocks.init();
        RegisteredItems.init();
        RegisteredGolemStuff.init();

        ModSubstitutions.preInit();
    }

    public void initalize() {
        familiarHandler = new FamiliarHandlerServer();

        NetworkRegistry.INSTANCE.registerGuiHandler(Gadomancy.instance, this);

        MinecraftForge.EVENT_BUS.register(EVENT_HANDLER_GOLEM);
        FMLCommonHandler.instance().bus().register(new EventHandlerNetwork());
        MinecraftForge.EVENT_BUS.register(new EventHandlerWorld());
        MinecraftForge.EVENT_BUS.register(new EventHandlerEntity());

        RegisteredRecipes.init();

        ModSubstitutions.init();

        RegisteredPotions.init();

        DimensionManager.registerProviderType(ModConfig.dimOuterId, WorldProviderTCEldrich.class, false);
        DimensionManager.registerDimension(ModConfig.dimOuterId, ModConfig.dimOuterId);
    }

    public void postInitalize() {
        RegisteredResearches.init();
        RegisteredIntegrations.init();

        RegisteredResearches.postInit();

        RegisteredItems.postInit();

        ModSubstitutions.postInit();

        familiarHandler.setupPostInit();
    }

    public static void unregisterWandHandler(String modid, Block block, int metadata) {
        HashMap<String, HashMap<List, List>> triggers = new Injector(WandTriggerRegistry.class).getField("triggers");
        HashMap<List, List> modTriggers = triggers.get(modid);
        if(modTriggers == null) return;
        List arrKey = Arrays.asList(block, metadata);
        modTriggers.remove(arrKey);
        triggers.put(modid, modTriggers);
    }

    @Override
    public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        switch (ID) {
            case 0:
                return new ContainerGolem(player.inventory, ((EntityGolemBase)world.getEntityByID(x)).inventory);
            case 1:
                return new ContainerInfusionClaw(player.inventory, (IInventory) world.getTileEntity(x, y, z));
        }
        return null;
    }

    @Override
    public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        return null;
    }

    public Side getSide() {
        return this instanceof ClientProxy ? Side.CLIENT : Side.SERVER;
    }
}
