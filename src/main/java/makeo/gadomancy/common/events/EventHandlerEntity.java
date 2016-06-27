package makeo.gadomancy.common.events;

import cpw.mods.fml.common.eventhandler.Event;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import makeo.gadomancy.common.aura.AuraEffects;
import makeo.gadomancy.common.blocks.tiles.TileAIShutdown;
import makeo.gadomancy.common.blocks.tiles.TileAuraPylon;
import makeo.gadomancy.common.blocks.tiles.TileBlockProtector;
import makeo.gadomancy.common.data.config.ModConfig;
import makeo.gadomancy.common.entities.EntityPermNoClipItem;
import makeo.gadomancy.common.familiar.FamiliarAIController_Old;
import makeo.gadomancy.common.utils.MiscUtils;
import makeo.gadomancy.common.utils.Vector3;
import makeo.gadomancy.common.utils.world.TCMazeHandler;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.entity.boss.IBossDisplayData;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;
import net.minecraftforge.event.entity.living.EnderTeleportEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.living.LivingSetAttackTargetEvent;
import net.minecraftforge.event.entity.living.LivingSpawnEvent;
import net.minecraftforge.event.entity.player.EntityItemPickupEvent;
import thaumcraft.common.items.armor.Hover;

import java.util.ArrayList;
import java.util.List;

/**
 * This class is part of the Gadomancy Mod
 * Gadomancy is Open Source and distributed under the
 * GNU LESSER GENERAL PUBLIC LICENSE
 * for more read the LICENSE file
 * <p/>
 * Created by HellFirePvP @ 31.10.2015 15:28
 */
public class EventHandlerEntity {

    public static List<ChunkCoordinates> registeredLuxPylons = new ArrayList<ChunkCoordinates>();

    /*@SubscribeEvent(priority = EventPriority.LOWEST)
    public void on(EntityEvent.EntityConstructing e) {
        if (e.entity instanceof EntityPlayer) {
            //e.entity.registerExtendedProperties(Gadomancy.MODID, new ExtendedPlayerProperties((EntityPlayer) e.entity));
        }
    }*/

    @SubscribeEvent
    public void on(LivingSpawnEvent.CheckSpawn event) {
        if(event.entityLiving.isCreatureType(EnumCreatureType.monster, false)) {
            double rangeSq = AuraEffects.LUX.getRange() * AuraEffects.LUX.getRange();
            Vector3 entityPos = MiscUtils.getPositionVector(event.entity);
            for(ChunkCoordinates luxPylons : registeredLuxPylons) {
                Vector3 pylon = Vector3.fromCC(luxPylons);
                if(entityPos.distanceSquared(pylon) <= rangeSq) {
                    event.setResult(Event.Result.DENY);
                    return;
                }
            }
        }
    }

    @SubscribeEvent
    public void on(EnderTeleportEvent e) {
        if (!(e.entityLiving instanceof EntityPlayer) && !(e.entityLiving instanceof IBossDisplayData)) {
            if (TileBlockProtector.isSpotProtected(e.entityLiving.worldObj, e.entityLiving)) {
                e.setCanceled(true);
            }
        }
    }

    /*@SubscribeEvent
    public void on(LivingSetAttackTargetEvent targetEvent) {
        if (targetEvent.target instanceof EntityPlayer) {
            FamiliarAIController_Old.notifyTargetEvent(targetEvent.entityLiving, (EntityPlayer) targetEvent.target);
        }
    }*/

    @SubscribeEvent
    public void on(LivingDeathEvent event) {
        if (!event.entity.worldObj.isRemote) {
            if(event.entityLiving instanceof EntityPlayer) {
                TCMazeHandler.closeSession((EntityPlayer) event.entityLiving, false);
            }
            if(event.entityLiving instanceof EntityLiving) {
                TileAIShutdown.removeTrackedEntity((EntityLiving) event.entityLiving);
            }
        }
    }

    @SubscribeEvent
    public void on(EntityItemPickupEvent event) {
        if (!event.entityPlayer.worldObj.isRemote) {
            if (event.item != null && event.item instanceof EntityPermNoClipItem) {
                EntityPermNoClipItem item = (EntityPermNoClipItem) event.item;
                ChunkCoordinates master = (ChunkCoordinates) item.getDataWatcher().getWatchedObject(ModConfig.entityNoClipItemDatawatcherMasterId).getObject();
                TileEntity te = event.entityPlayer.worldObj.getTileEntity(master.posX, master.posY, master.posZ);
                if (te == null || !(te instanceof EntityPermNoClipItem.IItemMasterTile)) return;
                ((EntityPermNoClipItem.IItemMasterTile) te).informItemRemoval();
            }
        }
    }

    @SubscribeEvent
    public void on(LivingEvent.LivingUpdateEvent event) {
        if (event.entityLiving == null || !(event.entityLiving instanceof EntityPlayer)) return;
        EntityPlayer player = (EntityPlayer) event.entity;
        if ((event.entity.worldObj.provider.dimensionId == ModConfig.dimOuterId) && ((player.ticksExisted & 7) == 0) && ((player.capabilities.isFlying) || (Hover.getHover(player.getEntityId())))) {
            if(player.capabilities.isCreativeMode && MiscUtils.isANotApprovedOrMisunderstoodPersonFromMoreDoor(player)) return;
            player.capabilities.isFlying = false;
            Hover.setHover(player.getEntityId(), false);
            if (!((EntityPlayer) event.entityLiving).worldObj.isRemote) {
                String msg = StatCollector.translateToLocal("tc.break.fly");
                if (player.capabilities.isCreativeMode) {
                    msg += " " + StatCollector.translateToLocal("gadomancy.eldritch.noflyCreative");
                }
                player.addChatMessage(new ChatComponentText(EnumChatFormatting.ITALIC + "" + EnumChatFormatting.GRAY + msg));
            }
        }
    }

}
