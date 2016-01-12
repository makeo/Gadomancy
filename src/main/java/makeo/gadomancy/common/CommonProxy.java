package makeo.gadomancy.common;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.network.IGuiHandler;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.relauncher.Side;
import makeo.gadomancy.client.ClientProxy;
import makeo.gadomancy.common.aura.AuraEffects;
import makeo.gadomancy.common.containers.ContainerArcanePackager;
import makeo.gadomancy.common.containers.ContainerInfusionClaw;
import makeo.gadomancy.common.data.SyncDataHolder;
import makeo.gadomancy.common.data.config.ModConfig;
import makeo.gadomancy.common.events.EventHandlerEntity;
import makeo.gadomancy.common.events.EventHandlerGolem;
import makeo.gadomancy.common.events.EventHandlerNetwork;
import makeo.gadomancy.common.events.EventHandlerWorld;
import makeo.gadomancy.common.network.PacketHandler;
import makeo.gadomancy.common.network.packets.PacketStartAnimation;
import makeo.gadomancy.common.registration.*;
import makeo.gadomancy.common.utils.Injector;
import makeo.gadomancy.common.utils.world.WorldProviderTCEldrich;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
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
        NetworkRegistry.INSTANCE.registerGuiHandler(Gadomancy.instance, this);

        MinecraftForge.EVENT_BUS.register(EVENT_HANDLER_GOLEM);
        FMLCommonHandler.instance().bus().register(new EventHandlerNetwork());
        EventHandlerWorld worldEventHandler = new EventHandlerWorld();
        MinecraftForge.EVENT_BUS.register(worldEventHandler);
        FMLCommonHandler.instance().bus().register(worldEventHandler);
        MinecraftForge.EVENT_BUS.register(new EventHandlerEntity());

        RegisteredEnchantments.init();
        RegisteredRecipes.init();

        SyncDataHolder.initialize();
        ModSubstitutions.init();

        RegisteredEntities.init();

        DimensionManager.registerProviderType(ModConfig.dimOuterId, WorldProviderTCEldrich.class, true);
        DimensionManager.registerDimension(ModConfig.dimOuterId, ModConfig.dimOuterId);
    }

    public void postInitalize() {
        RegisteredPotions.init();
        AuraEffects.AER.getTickInterval(); //initalize AuraEffects

        RegisteredResearches.init();
        RegisteredIntegrations.init();

        RegisteredResearches.postInit();

        RegisteredItems.postInit();

        ModSubstitutions.postInit();
    }

    public static void unregisterWandHandler(String modid, Block block, int metadata) {
        HashMap<String, HashMap<List, List>> triggers = new Injector(WandTriggerRegistry.class).getField("triggers");
        HashMap<List, List> modTriggers = triggers.get(modid);
        if(modTriggers == null) return;
        List arrKey = Arrays.asList(block, metadata);
        modTriggers.remove(arrKey);
        triggers.put(modid, modTriggers);
    }

    public void spawnBubbles(World world, float posX, float posY, float posZ, float rangeAroundItem) {
        PacketStartAnimation pkt = new PacketStartAnimation(PacketStartAnimation.ID_BUBBLES,
                Float.floatToIntBits(posX), Float.floatToIntBits(posY), Float.floatToIntBits(posZ),
                Float.floatToIntBits(rangeAroundItem));
        PacketHandler.INSTANCE.sendToAllAround(pkt, new NetworkRegistry.TargetPoint(world.provider.dimensionId,
                posX, posY, posZ, 32));
    }

    @Override
    public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        switch (ID) {
            case 0:
                return new ContainerGolem(player.inventory, ((EntityGolemBase)world.getEntityByID(x)).inventory);
            case 1:
                return new ContainerInfusionClaw(player.inventory, (IInventory) world.getTileEntity(x, y, z));
            case 2:
                return new ContainerArcanePackager(player.inventory, (IInventory) world.getTileEntity(x, y, z));
        }
        return null;
    }

    @Override
    public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        return null;
    }

    public void runDelayedClientSide(Runnable run) { }

    public Side getSide() {
        return this instanceof ClientProxy ? Side.CLIENT : Side.SERVER;
    }
}
