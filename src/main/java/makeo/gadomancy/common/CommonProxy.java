package makeo.gadomancy.common;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.network.IGuiHandler;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import makeo.gadomancy.common.containers.ContainerInfusionClaw;
import makeo.gadomancy.common.events.EventHandlerGolem;
import makeo.gadomancy.common.events.EventHandlerNetwork;
import makeo.gadomancy.common.events.EventHandlerWorld;
import makeo.gadomancy.common.network.PacketHandler;
import makeo.gadomancy.common.registration.*;
import makeo.gadomancy.client.ClientProxy;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import thaumcraft.common.blocks.BlockAiryItem;
import thaumcraft.common.config.ConfigBlocks;
import thaumcraft.common.entities.golems.ContainerGolem;
import thaumcraft.common.entities.golems.EntityGolemBase;

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
    }

    public void initalize() {
        try {
            RegisteredItems.itemBlockAiryCopy = new BlockAiryItem(RegisteredBlocks.blockNode);
            GameRegistry.addSubstitutionAlias("Thaumcraft:blockAiry", GameRegistry.Type.BLOCK, RegisteredBlocks.blockNode);
            GameRegistry.addSubstitutionAlias("Thaumcraft:blockAiry", GameRegistry.Type.ITEM, RegisteredItems.itemBlockAiryCopy);
            ConfigBlocks.blockAiry = RegisteredBlocks.blockNode;
        } catch (Exception e) {
            e.printStackTrace();
        }

        NetworkRegistry.INSTANCE.registerGuiHandler(Gadomancy.instance, this);

        MinecraftForge.EVENT_BUS.register(EVENT_HANDLER_GOLEM);
        FMLCommonHandler.instance().bus().register(new EventHandlerNetwork());
        MinecraftForge.EVENT_BUS.register(new EventHandlerWorld());

        RegisteredRecipes.init();
    }

    public void postInitalize() {
        RegisteredResearches.init();
        RegisteredIntegrations.init();

        RegisteredResearches.postInit();
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
