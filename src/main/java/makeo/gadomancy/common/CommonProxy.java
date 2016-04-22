package makeo.gadomancy.common;

import makeo.gadomancy.client.ClientProxy;
import makeo.gadomancy.common.aura.AuraEffects;
import makeo.gadomancy.common.containers.ContainerArcanePackager;
import makeo.gadomancy.common.containers.ContainerInfusionClaw;
import makeo.gadomancy.common.data.SyncDataHolder;
import makeo.gadomancy.common.event.EventHandlerEntity;
import makeo.gadomancy.common.event.EventHandlerGolem;
import makeo.gadomancy.common.event.EventHandlerNetwork;
import makeo.gadomancy.common.event.EventHandlerWorld;
import makeo.gadomancy.common.network.PacketHandler;
import makeo.gadomancy.common.network.packets.PacketStartAnimation;
import makeo.gadomancy.common.registration.RegisteredBlocks;
import makeo.gadomancy.common.registration.RegisteredEnchantments;
import makeo.gadomancy.common.registration.RegisteredEntities;
import makeo.gadomancy.common.registration.RegisteredIntegrations;
import makeo.gadomancy.common.registration.RegisteredItems;
import makeo.gadomancy.common.registration.RegisteredPotions;
import makeo.gadomancy.common.registration.RegisteredRecipes;
import makeo.gadomancy.common.registration.RegisteredResearches;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.util.EnumHelper;
import net.minecraftforge.fml.common.network.IGuiHandler;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.relauncher.Side;

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

    public static final EnumRarity RARITY_SACRED = EnumHelper.addRarity("Sacred", EnumChatFormatting.GOLD, "Sacred");

    public static CreativeTabs creativeTab;

    public void onConstruct() { }

    public void preInitalize() {
        PacketHandler.init();

        creativeTab = new CreativeTabs(Gadomancy.MODID) {
            @Override
            public Item getTabIconItem() {
                return Items.apple;
            }
        };

        RegisteredItems.preInit();

        RegisteredBlocks.init();
        RegisteredItems.init();
        //RegisteredGolemStuff.init(); TODO makeooo?

        //ModSubstitutions.preInit();
    }

    public void initalize() {
        NetworkRegistry.INSTANCE.registerGuiHandler(Gadomancy.instance, this);

        MinecraftForge.EVENT_BUS.register(EVENT_HANDLER_GOLEM);
        //FMLCommonHandler.instance().bus().register(new EventHandlerNetwork());
        //EventHandlerWorld worldEventHandler = new EventHandlerWorld();
        MinecraftForge.EVENT_BUS.register(new EventHandlerNetwork());
        MinecraftForge.EVENT_BUS.register(new EventHandlerWorld());
        //FMLCommonHandler.instance().bus().register(worldEventHandler);
        MinecraftForge.EVENT_BUS.register(new EventHandlerEntity());

        RegisteredEnchantments.init();
        RegisteredRecipes.init();

        SyncDataHolder.initialize();
        //ModSubstitutions.init(); NO node stuff this time.

        RegisteredEntities.init();

        //DimensionManager.registerProviderType(ModConfig.dimOuterId, WorldProviderTCEldrich.class, true);
        //DimensionManager.registerDimension(ModConfig.dimOuterId, ModConfig.dimOuterId);
    }

    public void postInitalize() {
        RegisteredPotions.init();
        AuraEffects.AER.getTickInterval(); //initalize AuraEffects

        RegisteredResearches.init();
        RegisteredIntegrations.init();

        RegisteredResearches.postInit();

        RegisteredItems.postInit();

        //ModSubstitutions.postInit();
    }

    /*public static void unregisterWandHandler(String modid, Block block, int metadata) {
        HashMap<String, HashMap<List, List>> triggers = new Injector(WandTriggerRegistry.class).getField("triggers");
        HashMap<List, List> modTriggers = triggers.get(modid);
        if(modTriggers == null) return;
        List arrKey = Arrays.asList(block, metadata);
        modTriggers.remove(arrKey);
        triggers.put(modid, modTriggers);
    }*/

    public void spawnBubbles(World world, float posX, float posY, float posZ, float rangeAroundItem) {
        PacketStartAnimation pkt = new PacketStartAnimation(PacketStartAnimation.ID_BUBBLES,
                Float.floatToIntBits(posX), Float.floatToIntBits(posY), Float.floatToIntBits(posZ),
                Float.floatToIntBits(rangeAroundItem));
        PacketHandler.INSTANCE.sendToAllAround(pkt, new NetworkRegistry.TargetPoint(world.provider.getDimensionId(),
                posX, posY, posZ, 32));
    }

    @Override
    public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        switch (ID) {
            //case 0:
                //return new ContainerGolem(player.inventory, ((EntityThaumcraftGolem)world.getEntityByID(x)).inventory);
            case 1:
                return new ContainerInfusionClaw(player.inventory, (IInventory) world.getTileEntity(new BlockPos(x, y, z)));
            case 2:
                return new ContainerArcanePackager(player.inventory, (IInventory) world.getTileEntity(new BlockPos(x, y, z)));
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
