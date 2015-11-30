package makeo.gadomancy.common.events;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import makeo.gadomancy.common.aura.AuraResearchManager;
import makeo.gadomancy.common.blocks.tiles.TileAuraPylon;
import makeo.gadomancy.common.blocks.tiles.TileBlockProtector;
import makeo.gadomancy.common.data.ModConfig;
import makeo.gadomancy.common.entities.EntityAuraCore;
import makeo.gadomancy.common.entities.EntityPermNoClipItem;
import makeo.gadomancy.common.familiar.FamiliarAIController;
import makeo.gadomancy.common.items.ItemAuraCore;
import makeo.gadomancy.common.utils.world.TCMazeHandler;
import net.minecraft.entity.boss.IBossDisplayData;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.living.EnderTeleportEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.living.LivingSetAttackTargetEvent;
import net.minecraftforge.event.entity.player.EntityItemPickupEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import thaumcraft.common.items.armor.Hover;

import java.io.File;

/**
 * This class is part of the Gadomancy Mod
 * Gadomancy is Open Source and distributed under the
 * GNU LESSER GENERAL PUBLIC LICENSE
 * for more read the LICENSE file
 * <p/>
 * Created by HellFirePvP @ 31.10.2015 15:28
 */
public class EventHandlerEntity {

    /*@SubscribeEvent(priority = EventPriority.LOWEST)
    public void on(EntityEvent.EntityConstructing e) {
        if (e.entity instanceof EntityPlayer) {
            //e.entity.registerExtendedProperties(Gadomancy.MODID, new ExtendedPlayerProperties((EntityPlayer) e.entity));
        }
    }*/

    @SubscribeEvent
    public void on(EnderTeleportEvent e) {
        if(!(e.entityLiving instanceof EntityPlayer) && !(e.entityLiving instanceof IBossDisplayData)) {
            if(TileBlockProtector.isSpotProtected(e.entityLiving.worldObj, e.entityLiving)) {
                e.setCanceled(true);
            }
        }
    }

    @SubscribeEvent
    public void on(LivingSetAttackTargetEvent targetEvent) {
        if(targetEvent.target instanceof EntityPlayer) {
            FamiliarAIController.notifyTargetEvent(targetEvent.entityLiving, (EntityPlayer) targetEvent.target);
        }
    }

    @SubscribeEvent
    public void on(LivingDeathEvent event) {
        if(!event.entity.worldObj.isRemote && event.entityLiving instanceof EntityPlayer) {
            TCMazeHandler.closeSession((EntityPlayer) event.entityLiving, false);
        }
    }

    @SubscribeEvent
    public void onJoin(EntityJoinWorldEvent event) {
        if(event.entity != null && event.entity instanceof EntityItem) {
            ItemStack stack = ((EntityItem) event.entity).getEntityItem();
            if(stack == null) return;
            if(event.entity instanceof EntityAuraCore) return;
            Item i = stack.getItem();
            if(i instanceof ItemAuraCore) {
                event.setCanceled(true);
                if(event.world.isRemote) return;
                EntityAuraCore e = new EntityAuraCore(event.world);
                e.setEntityItemStack(((EntityItem) event.entity).getEntityItem());
                e.setPosition(event.entity.posX, event.entity.posY, event.entity.posZ);
                e.age = ((EntityItem) event.entity).age;
                e.hoverStart = ((EntityItem) event.entity).hoverStart;
                e.delayBeforeCanPickup = ((EntityItem) event.entity).delayBeforeCanPickup;
                e.motionX = event.entity.motionX;
                e.motionY = event.entity.motionY;
                e.motionZ = event.entity.motionZ;
                event.world.spawnEntityInWorld(e);
            }
        }
    }

    @SubscribeEvent
    public void on(EntityItemPickupEvent event) {
        if(!event.entityPlayer.worldObj.isRemote) {
            if(event.item != null && event.item instanceof EntityPermNoClipItem) {
                EntityPermNoClipItem item = (EntityPermNoClipItem) event.item;
                ChunkCoordinates master = (ChunkCoordinates) item.getDataWatcher().getWatchedObject(ModConfig.entityNoClipItemDatawatcherMasterId).getObject();
                TileEntity te = event.entityPlayer.worldObj.getTileEntity(master.posX, master.posY, master.posZ);
                if(te == null || !(te instanceof TileAuraPylon)) return;
                ((TileAuraPylon) te).informItemPickup();
            }
        }
    }

    @SubscribeEvent
    public void on(LivingEvent.LivingUpdateEvent event) {
        if(event.entityLiving == null || !(event.entityLiving instanceof EntityPlayer)) return;
        EntityPlayer player = (EntityPlayer)event.entity;
        if ((event.entity.worldObj.provider.dimensionId == ModConfig.dimOuterId) && ((player.ticksExisted & 7) == 0) && ((player.capabilities.isFlying) || (Hover.getHover(player.getEntityId())))){
            player.capabilities.isFlying = false;
            Hover.setHover(player.getEntityId(), false);
            if(!((EntityPlayer) event.entityLiving).worldObj.isRemote) {
                String msg = StatCollector.translateToLocal("tc.break.fly");
                if(player.capabilities.isCreativeMode) {
                    msg += " " + StatCollector.translateToLocal("gadomancy.eldritch.noflyCreative");
                }
                player.addChatMessage(new ChatComponentText(EnumChatFormatting.ITALIC + "" + EnumChatFormatting.GRAY + msg));
            }
        }
    }

    @SubscribeEvent
    public void onLoad(PlayerEvent.LoadFromFile loadEvent) {
        File auraData = loadEvent.getPlayerFile("auraDat");
        AuraResearchManager.loadDataFromFile(loadEvent.entityPlayer.getCommandSenderName(), loadEvent.entityPlayer.getUniqueID(), auraData);
    }

    @SubscribeEvent
    public void onSave(PlayerEvent.SaveToFile saveEvent) {
        File auraData = saveEvent.getPlayerFile("auraDat");
        AuraResearchManager.saveDataToFile(saveEvent.entityPlayer.getCommandSenderName(), saveEvent.entityPlayer.getUniqueID(), auraData);
    }

}
