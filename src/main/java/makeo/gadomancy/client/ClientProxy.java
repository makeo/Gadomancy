package makeo.gadomancy.client;

import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;
import cpw.mods.fml.client.registry.RenderingRegistry;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.FMLLog;
import makeo.gadomancy.client.effect.EffectHandler;
import makeo.gadomancy.client.events.ClientHandler;
import makeo.gadomancy.client.events.RenderEventHandler;
import makeo.gadomancy.client.events.ResourceReloadListener;
import makeo.gadomancy.client.gui.ArcanePackagerGui;
import makeo.gadomancy.client.gui.InfusionClawGui;
import makeo.gadomancy.client.renderers.block.BlockExtendedNodeJarRenderer;
import makeo.gadomancy.client.renderers.block.RenderBlockStoneMachine;
import makeo.gadomancy.client.renderers.block.RenderBlockTransparent;
import makeo.gadomancy.client.renderers.entity.RenderAdditionalGolemBase;
import makeo.gadomancy.client.renderers.entity.RenderEntityAuraCore;
import makeo.gadomancy.client.renderers.item.ItemCreativeNodeRenderer;
import makeo.gadomancy.client.renderers.item.ItemExNodeRenderer;
import makeo.gadomancy.client.renderers.item.ItemJarExtendedNodeRenderer;
import makeo.gadomancy.client.renderers.item.ItemRenderFamiliar;
import makeo.gadomancy.client.renderers.item.ItemRenderRemoteJar;
import makeo.gadomancy.client.renderers.item.ItemRenderStoneMachine;
import makeo.gadomancy.client.renderers.item.ItemRenderTEKnowledgeBook;
import makeo.gadomancy.client.renderers.item.ItemRenderTileEntity;
import makeo.gadomancy.client.renderers.item.ItemRenderTileEntityMulti;
import makeo.gadomancy.client.renderers.tile.*;
import makeo.gadomancy.client.util.MultiTickEffectDispatcher;
import makeo.gadomancy.common.CommonProxy;
import makeo.gadomancy.common.blocks.tiles.*;
import makeo.gadomancy.common.entities.EntityAuraCore;
import makeo.gadomancy.common.entities.EntityItemElement;
import makeo.gadomancy.common.entities.EntityPermNoClipItem;
import makeo.gadomancy.common.registration.RegisteredBlocks;
import makeo.gadomancy.common.registration.RegisteredItems;
import makeo.gadomancy.common.utils.Injector;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.client.resources.SimpleReloadableResourceManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.world.World;
import net.minecraftforge.client.MinecraftForgeClient;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.util.ForgeDirection;
import thaumcraft.client.gui.GuiGolem;
import thaumcraft.client.renderers.entity.RenderGolemBase;
import thaumcraft.client.renderers.entity.RenderSpecialItem;
import thaumcraft.client.renderers.tile.TileEldritchPortalRenderer;
import thaumcraft.common.config.ConfigBlocks;
import thaumcraft.common.entities.golems.EntityGolemBase;
import thaumcraft.common.tiles.TileEldritchAltar;
import thaumcraft.common.tiles.TileEldritchCap;
import thaumcraft.common.tiles.TileEldritchObelisk;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

/**
 * This class is part of the Gadomancy Mod
 * Gadomancy is Open Source and distributed under the
 * GNU LESSER GENERAL PUBLIC LICENSE
 * for more read the LICENSE file
 *
 * Created by makeo @ 29.11.2014 14:15
 */
public class ClientProxy extends CommonProxy {
    @Override
    public void preInitalize() {
        super.preInitalize();
    }

    @Override
    public void initalize() {
        super.initalize();

        injectGolemTextures();

        //Tiles
        ClientRegistry.bindTileEntitySpecialRenderer(TileStickyJar.class, new RenderTileStickyJar());

        RenderTileRemoteJar renderTileRemoteJar = new RenderTileRemoteJar();
        ClientRegistry.bindTileEntitySpecialRenderer(TileRemoteJar.class, renderTileRemoteJar);

        RenderTileArcaneDropper renderTileArcaneDropper = new RenderTileArcaneDropper();
        ClientRegistry.bindTileEntitySpecialRenderer(TileArcaneDropper.class, renderTileArcaneDropper);

        RenderTileInfusionClaw renderTileInfusionClaw = new RenderTileInfusionClaw();
        ClientRegistry.bindTileEntitySpecialRenderer(TileInfusionClaw.class, renderTileInfusionClaw);

        RenderTileExtendedNode nodeRenderer = new RenderTileExtendedNode();
        ClientRegistry.bindTileEntitySpecialRenderer(TileExtendedNode.class, nodeRenderer);

        RenderTileExtendedNodeJar nodeJarRenderer = new RenderTileExtendedNodeJar();
        ClientRegistry.bindTileEntitySpecialRenderer(TileExtendedNodeJar.class, nodeJarRenderer);

        ClientRegistry.bindTileEntitySpecialRenderer(TileNodeManipulator.class, new RenderTileNodeManipulator());

        ClientRegistry.bindTileEntitySpecialRenderer(TileManipulatorPillar.class, new RenderTileManipulatorPillar());
        RenderTileManipulationFocus renderTileManipulationFocus = new RenderTileManipulationFocus();
        ClientRegistry.bindTileEntitySpecialRenderer(TileManipulationFocus.class, renderTileManipulationFocus);

        ClientRegistry.bindTileEntitySpecialRenderer(TileAdditionalEldritchPortal.class, new TileEldritchPortalRenderer());

        ClientRegistry.bindTileEntitySpecialRenderer(TileEldritchObelisk.class, new RenderTileObelisk());
        ClientRegistry.bindTileEntitySpecialRenderer(TileEldritchAltar.class, new RenderTileCapEldritch("textures/models/obelisk_cap_altar.png"));
        ClientRegistry.bindTileEntitySpecialRenderer(TileEldritchCap.class, new RenderTileCapEldritch("textures/models/obelisk_cap.png"));
        RenderTileEssentiaCompressor renderTileEssentiaCompressor = new RenderTileEssentiaCompressor();
        ClientRegistry.bindTileEntitySpecialRenderer(TileEssentiaCompressor.class, renderTileEssentiaCompressor);

        //ClientRegistry.bindTileEntitySpecialRenderer(TileAIShutdown.class, new RenderTileAIShutdown());

        RenderTileAuraPylon renderTileAuraPylon = new RenderTileAuraPylon();
        ClientRegistry.bindTileEntitySpecialRenderer(TileAuraPylon.class, renderTileAuraPylon);
        ClientRegistry.bindTileEntitySpecialRenderer(TileAuraPylonTop.class, renderTileAuraPylon);

        RenderTileBlockProtector renderTileBlockProtector = new RenderTileBlockProtector();
        ClientRegistry.bindTileEntitySpecialRenderer(TileBlockProtector.class, renderTileBlockProtector);

        RenderTileArcanePackager renderTileArcanePackager = new RenderTileArcanePackager();
        ClientRegistry.bindTileEntitySpecialRenderer(TileArcanePackager.class, renderTileArcanePackager);

        RenderTileKnowledgeBook bookRender = new RenderTileKnowledgeBook();
        ClientRegistry.bindTileEntitySpecialRenderer(TileKnowledgeBook.class, bookRender);

        //Items
        TileArcaneDropper fakeTile = new TileArcaneDropper();
        fakeTile.blockMetadata = 8 | ForgeDirection.SOUTH.ordinal();
        MinecraftForgeClient.registerItemRenderer(Item.getItemFromBlock(RegisteredBlocks.blockArcaneDropper), new ItemRenderTileEntity<TileArcaneDropper>(renderTileArcaneDropper, fakeTile));

        MinecraftForgeClient.registerItemRenderer(Item.getItemFromBlock(RegisteredBlocks.blockInfusionClaw), new ItemRenderTileEntity<TileInfusionClaw>(renderTileInfusionClaw, new TileInfusionClaw()));

        MinecraftForgeClient.registerItemRenderer(Item.getItemFromBlock(RegisteredBlocks.blockEssentiaCompressor), new ItemRenderTileEntity<TileEssentiaCompressor>(renderTileEssentiaCompressor, new TileEssentiaCompressor()));

        MinecraftForgeClient.registerItemRenderer(Item.getItemFromBlock(ConfigBlocks.blockAiry), new ItemExNodeRenderer());

        MinecraftForgeClient.registerItemRenderer(Item.getItemFromBlock(RegisteredBlocks.blockKnowledgeBook), new ItemRenderTEKnowledgeBook(bookRender));

        ItemRenderTileEntityMulti multi = new ItemRenderTileEntityMulti(new ItemRenderTileEntityMulti.RenderSet(renderTileAuraPylon, new TileAuraPylon(), 0),
               new ItemRenderTileEntityMulti.RenderSet(renderTileAuraPylon, new TileAuraPylonTop(), 1));
        MinecraftForgeClient.registerItemRenderer(Item.getItemFromBlock(RegisteredBlocks.blockAuraPylon), multi);

        MinecraftForgeClient.registerItemRenderer(RegisteredItems.itemExtendedNodeJar, new ItemJarExtendedNodeRenderer());
        MinecraftForgeClient.registerItemRenderer(Item.getItemFromBlock(RegisteredBlocks.blockRemoteJar), new ItemRenderRemoteJar(renderTileRemoteJar));

        ItemRenderStoneMachine itemRenderStoneMachine = new ItemRenderStoneMachine();
        MinecraftForgeClient.registerItemRenderer(Item.getItemFromBlock(RegisteredBlocks.blockStoneMachine), itemRenderStoneMachine);
        itemRenderStoneMachine.registerRenderer(0, new TileManipulationFocus(), renderTileManipulationFocus);
        TileManipulationFocus tileManipulationFocus = new TileManipulationFocus();
        tileManipulationFocus.blockMetadata = 3;
        itemRenderStoneMachine.registerRenderer(3, tileManipulationFocus, renderTileManipulationFocus);
        TileBlockProtector tileBlockProtector = new TileBlockProtector();
        tileBlockProtector.facing = 3;
        itemRenderStoneMachine.registerRenderer(2, tileBlockProtector, renderTileBlockProtector);
        itemRenderStoneMachine.registerRenderer(4, new TileArcanePackager(), renderTileArcanePackager);

        MinecraftForgeClient.registerItemRenderer(RegisteredItems.itemEtherealFamiliar, new ItemRenderFamiliar());
        MinecraftForgeClient.registerItemRenderer(RegisteredItems.itemCreativeNode, new ItemCreativeNodeRenderer());

        //Entities
        RenderingRegistry.registerEntityRenderingHandler(EntityPermNoClipItem.class, new RenderSpecialItem());
        RenderingRegistry.registerEntityRenderingHandler(EntityAuraCore.class, new RenderEntityAuraCore());

        //Blocks
        RegisteredBlocks.rendererTransparentBlock = registerBlockRenderer(new RenderBlockTransparent());
        RegisteredBlocks.rendererExtendedNodeJarBlock = registerBlockRenderer(new BlockExtendedNodeJarRenderer());
        RegisteredBlocks.rendererBlockStoneMachine = registerBlockRenderer(new RenderBlockStoneMachine());
    }

    @Override
    public void postInitalize() {
        IResourceManager manager = Minecraft.getMinecraft().getResourceManager();
        if(manager instanceof SimpleReloadableResourceManager) {
            SimpleReloadableResourceManager rm = (SimpleReloadableResourceManager) manager;
            rm.registerReloadListener(ResourceReloadListener.getInstance());
        }

        MinecraftForge.EVENT_BUS.register(EffectHandler.getInstance());
        MinecraftForge.EVENT_BUS.register(new RenderEventHandler());

        FMLCommonHandler.instance().bus().register(new ClientHandler());

        super.postInitalize();
    }

    public int registerBlockRenderer(ISimpleBlockRenderingHandler renderer) {
        int nextId = RenderingRegistry.getNextAvailableRenderId();
        RenderingRegistry.registerBlockHandler(nextId, renderer);
        return nextId;
    }

    @Override
    public void spawnBubbles(World world, float posX, float posY, float posZ, float rangeAroundItem) {
        MultiTickEffectDispatcher.BubbleFXInfo bubbles =
                new MultiTickEffectDispatcher.BubbleFXInfo(Minecraft.getMinecraft().theWorld.provider.dimensionId,
                        posX, posY, posZ, 10, rangeAroundItem);
        MultiTickEffectDispatcher.registerBubbles(bubbles);
    }

    @Override
    public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        switch (ID) {
            case 0:
                return new GuiGolem(player, (EntityGolemBase) world.getEntityByID(x));
                //return new AdditionalGolemGui(player, (EntityGolemBase)world.getEntityByID(x));
            case 1:
                return new InfusionClawGui(player.inventory, (IInventory) world.getTileEntity(x, y, z));
            case 2:
                return new ArcanePackagerGui(player.inventory, (IInventory) world.getTileEntity(x, y, z));
        }
        return null;
    }

    public static void injectGolemTextures() {
        RenderGolemBase render = unregisterRenderer(EntityGolemBase.class, RenderGolemBase.class);
        if(render != null) {
            RenderingRegistry.registerEntityRenderingHandler(EntityGolemBase.class, new RenderAdditionalGolemBase());
        }
    }

    public static <T extends Render> T unregisterRenderer(Class<? extends Entity> entityClass, Class<T> renderClass) {
        Injector registry = new Injector(RenderingRegistry.instance());
        List entityRenderers = registry.getField("entityRenderers");

        if(entityRenderers == null) {
            FMLLog.severe("Failed to get entityRenderers field in RenderingRegistry!");
            return null;
        }

        for(int i = 0; i < entityRenderers.size(); i++) {
            Injector pair = new Injector(entityRenderers.get(i));

            Class<? extends Entity> target = pair.getField("target");

            if(entityClass.equals(target)) {
                Render render = pair.getField("renderer");
                if(renderClass.isInstance(render)) {
                    entityRenderers.remove(i);
                    return (T)render;
                }
            }
        }
        return null;
    }

    public final Queue<Runnable> clientActions = new ArrayDeque<Runnable>();

    @Override
    public void runDelayedClientSide(Runnable run) {
        clientActions.add(run);
    }
}
