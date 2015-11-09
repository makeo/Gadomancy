package makeo.gadomancy.common.events;

import cpw.mods.fml.common.eventhandler.EventPriority;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import makeo.gadomancy.common.Gadomancy;
import makeo.gadomancy.common.blocks.tiles.TileBlockProtector;
import makeo.gadomancy.common.blocks.tiles.TileNodeManipulator;
import makeo.gadomancy.common.blocks.tiles.TileStickyJar;
import makeo.gadomancy.common.data.ModConfig;
import makeo.gadomancy.common.registration.RegisteredBlocks;
import makeo.gadomancy.common.registration.RegisteredItems;
import makeo.gadomancy.common.utils.GolemEnumHelper;
import makeo.gadomancy.common.utils.JarMultiblockHandler;
import makeo.gadomancy.common.utils.world.TCMazeHandler;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.StatCollector;
import net.minecraft.world.Explosion;
import net.minecraft.world.GameRules;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.event.world.ExplosionEvent;
import net.minecraftforge.event.world.WorldEvent;
import thaumcraft.common.items.wands.ItemWandCasting;
import thaumcraft.common.tiles.TileJarFillable;

import java.util.HashMap;
import java.util.Map;

/**
 * This class is part of the Gadomancy Mod
 * Gadomancy is Open Source and distributed under the
 * GNU LESSER GENERAL PUBLIC LICENSE
 * for more read the LICENSE file
 * <p/>
 * Created by makeo @ 05.07.2015 13:20
 */
public class EventHandlerWorld {
    private Entity lastUpdated;

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public void on(LivingEvent.LivingUpdateEvent e) {
        if(!e.entityLiving.worldObj.isRemote) {
            lastUpdated = e.entityLiving;
        }
    }

    @SubscribeEvent(priority = EventPriority.NORMAL)
    public void on(WorldEvent.Load e) {
        if (!e.world.isRemote && e.world.provider.dimensionId == 0) {
            Gadomancy.loadModData();

            GolemEnumHelper.validateSavedMapping();
            GolemEnumHelper.reorderEnum();

            TCMazeHandler.init();
        }

        GameRules rules = e.world.getGameRules();
        rules.theGameRules.put("mobGriefing", new ValueOverride(this, String.valueOf(rules.getGameRuleBooleanValue("mobGriefing"))));
    }

    private static class ValueOverride extends GameRules.Value {
        private final EventHandlerWorld handler;
        public ValueOverride(EventHandlerWorld handler, String value) {
            super(value);
            this.handler = handler;
        }

        @Override
        public boolean getGameRuleBooleanValue() {
            boolean mobGriefing = super.getGameRuleBooleanValue();
            if(mobGriefing) {
                Entity lastUpdated = handler.lastUpdated;
                if(lastUpdated != null) {
                    StackTraceElement[] elements = Thread.currentThread().getStackTrace();
                    for(StackTraceElement element : elements) {
                        if(element.getClassName().equals(EntityLivingBase.class.getName())
                                && (element.getMethodName().equals("func_70071_h") || element.getMethodName().equals("onUpdate"))) {
                            return !TileBlockProtector.isSpotProtected(lastUpdated.worldObj, lastUpdated.posX, lastUpdated.posY, lastUpdated.posZ);
                        }
                    }
                }
            }
            return mobGriefing;
        }
    }

    @SubscribeEvent(priority = EventPriority.HIGH)
    public void on(ExplosionEvent.Start e) {
        Explosion expl = e.explosion;
        if(expl.isSmoking && TileBlockProtector.isSpotProtected(e.world, expl.explosionX, expl.explosionY, expl.explosionZ)) {
            //why?
            //expl.isSmoking = false;
            e.setCanceled(true);
            e.world.newExplosion(expl.exploder, expl.explosionX, expl.explosionY, expl.explosionZ, expl.explosionSize, expl.isFlaming, false);
        }
    }

    @SubscribeEvent(priority = EventPriority.NORMAL)
    public void on(WorldEvent.Unload e) {
        if (!e.world.isRemote && e.world.provider.dimensionId == 0) {
            Gadomancy.unloadModData();

            TCMazeHandler.closeAllSessionsAndCleanup();
        }
    }

    @SubscribeEvent
    public void on(TickEvent.ServerTickEvent event) {
        if(event.phase != TickEvent.Phase.END) return;
        WorldServer worldServer = DimensionManager.getWorld(ModConfig.dimOuterId);
        if(worldServer == null) return;
        if (!worldServer.isRemote && (worldServer.provider.dimensionId == 0 || worldServer.provider.dimensionId == ModConfig.dimOuterId)) {
            TCMazeHandler.tick();
        }

        TCMazeHandler.scheduleTick();
    }

    private Map<EntityPlayer, Integer> interacts = null;

    @SubscribeEvent(priority = EventPriority.LOWEST, receiveCanceled = true)
    public void on(BlockEvent.PlaceEvent e) {
        if (e.isCanceled()) {
            if (interacts != null)
                interacts.remove(e.player);
        } else {
            if (!e.world.isRemote && isStickyJar(e.itemInHand)) {
                TileEntity parent = e.world.getTileEntity(e.x, e.y, e.z);
                if (parent instanceof TileJarFillable) {
                    int metadata = e.world.getBlockMetadata(e.x, e.y, e.z);
                    e.world.setBlock(e.x, e.y, e.z, RegisteredBlocks.blockStickyJar, metadata, 2);

                    TileEntity tile = e.world.getTileEntity(e.x, e.y, e.z);
                    if (tile instanceof TileStickyJar) {
                        Integer sideHit = interacts.get(e.player);
                        ((TileStickyJar) tile).init((TileJarFillable) parent, e.placedBlock, metadata,
                                ForgeDirection.getOrientation(sideHit == null ? 1 : sideHit).getOpposite());
                        RegisteredBlocks.blockStickyJar.onBlockPlacedBy(e.world, e.x, e.y, e.z, e.player, e.itemInHand);
                    }
                }
            }
        }
    }

    @SubscribeEvent
    public void onBreak(BlockEvent.BreakEvent event) {
        if (!event.world.isRemote) {
            if (event.block == RegisteredBlocks.blockNodeManipulator) {
                TileEntity te = event.world.getTileEntity(event.x, event.y, event.z);
                if (te != null && te instanceof TileNodeManipulator) {
                    if (((TileNodeManipulator) te).isInMultiblock())
                        ((TileNodeManipulator) te).breakMultiblock();
                }
            }
        }
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public void on(PlayerInteractEvent e) {
        if (!e.world.isRemote && e.action == PlayerInteractEvent.Action.RIGHT_CLICK_BLOCK
                && isStickyJar(e.entityPlayer.getHeldItem())) {
            if (interacts == null) {
                interacts = new HashMap<EntityPlayer, Integer>();
            }
            interacts.put(e.entityPlayer, e.face);
        }

        if (e.action == PlayerInteractEvent.Action.RIGHT_CLICK_BLOCK) {
            ItemStack i = e.entityPlayer.getHeldItem();
            if (i != null && (i.getItem() instanceof ItemWandCasting)) {
                JarMultiblockHandler.handleWandInteract(e.world, e.x, e.y, e.z, e.entityPlayer, i);
            }
        }
    }

    private boolean isStickyJar(ItemStack stack) {
        return stack != null && RegisteredItems.isStickyableJar(stack)
                && stack.hasTagCompound() && stack.stackTagCompound.getBoolean("isStickyJar");
    }

    @SubscribeEvent(priority = EventPriority.NORMAL)
    public void on(ItemTooltipEvent e) {
        if (e.toolTip.size() > 0 && e.itemStack.hasTagCompound()) {
            if (e.itemStack.stackTagCompound.getBoolean("isStickyJar")) {
                e.toolTip.add(1, "\u00a7a" + StatCollector.translateToLocal("gadomancy.lore.stickyjar"));
            }
        }
    }
}
