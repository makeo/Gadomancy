package makeo.gadomancy.common.events;

import cpw.mods.fml.common.eventhandler.EventPriority;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import makeo.gadomancy.api.GadomancyApi;
import makeo.gadomancy.api.golems.AdditionalGolemType;
import makeo.gadomancy.api.golems.cores.AdditionalGolemCore;
import makeo.gadomancy.api.golems.events.GolemDropPlacerEvent;
import makeo.gadomancy.api.golems.events.PlacerCreateGolemEvent;
import makeo.gadomancy.common.Gadomancy;
import makeo.gadomancy.common.data.DataAchromatic;
import makeo.gadomancy.common.data.SyncDataHolder;
import makeo.gadomancy.common.data.config.ModConfig;
import makeo.gadomancy.common.entities.golems.ItemAdditionalGolemPlacer;
import makeo.gadomancy.common.entities.golems.nbt.ExtendedGolemProperties;
import makeo.gadomancy.common.registration.RegisteredGolemStuff;
import makeo.gadomancy.common.registration.RegisteredPotions;
import makeo.gadomancy.common.utils.MiscUtils;
import makeo.gadomancy.common.utils.NBTHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.StatCollector;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.EntityEvent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.PlaySoundAtEntityEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.event.entity.player.EntityInteractEvent;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import thaumcraft.common.config.ConfigItems;
import thaumcraft.common.entities.golems.EntityGolemBase;
import thaumcraft.common.entities.golems.EnumGolemType;
import thaumcraft.common.entities.golems.ItemGolemPlacer;
import thaumcraft.common.items.wands.ItemWandCasting;

import java.util.HashMap;
import java.util.Map;

/**
 * This class is part of the Gadomancy Mod
 * Gadomancy is Open Source and distributed under the
 * GNU LESSER GENERAL PUBLIC LICENSE
 * for more read the LICENSE file
 *
 * Created by makeo @ 13.03.2015 13:56
 */
public class EventHandlerGolem {
    @SubscribeEvent(priority = EventPriority.LOWEST)
    public void on(EntityEvent.EntityConstructing e) {
        if(e.entity instanceof EntityGolemBase) {
            EntityGolemBase golem = (EntityGolemBase) e.entity;

            golem.registerExtendedProperties(Gadomancy.MODID, new ExtendedGolemProperties(golem));

            golem.getDataWatcher().addObject(ModConfig.golemDatawatcherId, "");
        }
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public void on(EntityEvent.EnteringChunk event) {
        if(event.entity instanceof EntityGolemBase) {
            EntityGolemBase golem = (EntityGolemBase) event.entity;
            if(GadomancyApi.isAdditionalGolemType(golem.getGolemType())) {
                ExtendedGolemProperties props = (ExtendedGolemProperties)event.entity.getExtendedProperties(Gadomancy.MODID);
                if(props.shouldUpdateHealth()) {
                    props.resetUpdateHealth();
                    golem.setHealth(props.getHealth());
                }
            }
        }
    }

    @SubscribeEvent(priority = EventPriority.LOWEST, receiveCanceled = true)
    public void on(EntityJoinWorldEvent event) {
        if(!event.entity.worldObj.isRemote && event.entity instanceof EntityGolemBase) {
            EntityGolemBase golem = (EntityGolemBase) event.entity;
            ExtendedGolemProperties props = (ExtendedGolemProperties) golem.getExtendedProperties(Gadomancy.MODID);
            if(props != null) {
                props.setWrapperIfNeeded();
            }
        }

        if (event.entity instanceof EntityItem) {
            EntityItem item = (EntityItem) event.entity;
            ItemStack stack = item.getEntityItem();

            if (stack.getItem() == ConfigItems.itemGolemPlacer) {
                AdditionalGolemType type = GadomancyApi.getAdditionalGolemType(EnumGolemType.getType(stack.getItemDamage()));
                if (type != null) {
                    ItemStack fakePlacer = new ItemStack(type.getPlacerItem());
                    fakePlacer.setTagCompound(stack.getTagCompound());
                    fakePlacer.setItemDamage(stack.getItemDamage());

                    item.setEntityItemStack(fakePlacer);
                }
            }
        }

        if(!event.world.isRemote && event.entity instanceof EntityLivingBase) {
            if(((EntityLivingBase) event.entity).isPotionActive(RegisteredPotions.ACHROMATIC)) {
                ((DataAchromatic) SyncDataHolder.getDataServer("AchromaticData")).handleApplication((EntityLivingBase) event.entity);
            }
        }
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public void on(PlayerInteractEvent e) {
        if(!e.world.isRemote && e.action == PlayerInteractEvent.Action.RIGHT_CLICK_BLOCK) {
            ItemStack itemInHand = e.entityPlayer.getHeldItem();
            if(itemInHand != null && (itemInHand.getItem() instanceof ItemGolemPlacer
                    || itemInHand.getItem() instanceof ItemAdditionalGolemPlacer)) {
                int entityId = Entity.nextEntityID;
                if(itemInHand.getItem().onItemUseFirst(itemInHand, e.entityPlayer, e.world, e.x, e.y, e.z, e.face, 0, 0, 0)) {
                    e.setCanceled(true);
                    Entity entity = e.world.getEntityByID(entityId);
                    if(entity != null && entity instanceof EntityGolemBase) {
                        EntityGolemBase golem = (EntityGolemBase) entity;

                        //move persistent data to entity
                        golem.getEntityData().setTag(Gadomancy.MODID, NBTHelper.getPersistentData(itemInHand).copy());

                        MinecraftForge.EVENT_BUS.post(new PlacerCreateGolemEvent(e.entityPlayer, golem, itemInHand));

                        ExtendedGolemProperties props = (ExtendedGolemProperties) golem.getExtendedProperties(Gadomancy.MODID);
                        if(props != null) {
                            props.updateGolemCore();
                            props.updateGolem();
                        }

                        //update runic shielding
                        if(RegisteredGolemStuff.upgradeRunicShield.hasUpgrade(golem)) {
                            RegisteredGolemStuff.upgradeRunicShield.getCharge(golem);
                        }
                    }
                }
            }
        }
    }

    private final Map<EntityGolemBase, EntityPlayer> markedGolems = new HashMap<EntityGolemBase, EntityPlayer>();

    @SubscribeEvent(priority = EventPriority.HIGHEST, receiveCanceled = true)
    public void on(PlaySoundAtEntityEvent event) {
        if(!event.entity.worldObj.isRemote && event.entity instanceof EntityGolemBase
                && event.name.equals("thaumcraft:zap") && event.volume == 0.5F && event.pitch == 1.0F) {
            EntityGolemBase golem = (EntityGolemBase) event.entity;
            if(markedGolems.containsKey(golem)) {
                EntityPlayer player = markedGolems.get(golem);
                markedGolems.remove(golem);

                AdditionalGolemCore core = GadomancyApi.getAdditionalGolemCore(golem);

                boolean movedPlacer = false;
                boolean movedCore = core == null || !player.isSneaking();

                for(EntityItem entityItem : golem.capturedDrops) {
                    ItemStack item = entityItem.getEntityItem();

                    if(!movedCore && item.getItem() == ConfigItems.itemGolemCore) {
                        entityItem.setEntityItemStack(core.getItem());
                    }

                    if(!movedPlacer && item.getItem() instanceof ItemGolemPlacer
                            || item.getItem() instanceof ItemAdditionalGolemPlacer) {
                        //move persistent data to item
                        NBTTagCompound persistent = (NBTTagCompound) NBTHelper.getPersistentData(golem).copy();
                        if(player.isSneaking()) {
                            persistent.removeTag("Core");
                        }
                        NBTHelper.getData(item).setTag(Gadomancy.MODID, persistent);
                        event.entity.setDead();
                        entityItem.setEntityItemStack(item);

                        MinecraftForge.EVENT_BUS.post(new GolemDropPlacerEvent(player, entityItem, golem));

                        movedPlacer = true;
                    }
                    event.entity.worldObj.spawnEntityInWorld(entityItem);
                }
                golem.capturedDrops.clear();
                golem.captureDrops = false;
            }
        }
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public void on(AttackEntityEvent event) {
        ItemStack heldItem = event.entityPlayer.getHeldItem();
        if(heldItem != null && heldItem.getItem() == ConfigItems.itemGolemBell && event.target instanceof EntityGolemBase
                && !event.target.worldObj.isRemote && !event.target.isDead) {
            event.target.captureDrops = true;
            markedGolems.put((EntityGolemBase) event.target, event.entityPlayer);
        }
    }

    @SubscribeEvent(priority = EventPriority.NORMAL)
    public void on(LivingHurtEvent event) {
        if (!event.entity.worldObj.isRemote) {
            if(event.entity instanceof EntityGolemBase) {
                EntityGolemBase golem = (EntityGolemBase) event.entity;
                if(event.ammount > 0 && RegisteredGolemStuff.upgradeRunicShield.hasUpgrade(golem)) {
                    event.ammount = RegisteredGolemStuff.upgradeRunicShield.absorb(golem, event.ammount, event.source);
                }
            }

            /*if(event.source.getEntity() != null && event.source.getEntity() instanceof EntityGolemBase
                    && ((EntityGolemBase) event.source.getEntity()).getGolemType()
                        == RegisteredGolemStuff.typeObsidian.getEnumEntry()) {
                event.entityLiving.addPotionEffect(new PotionEffect(Potion.wither.getId(), 3*20, 1));
            }*/
        }
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public void on(EntityInteractEvent event) {
        ItemStack heldItem = event.entityPlayer.getHeldItem();
        if(event.target instanceof EntityGolemBase) {
            EntityGolemBase golem = (EntityGolemBase) event.target;

            if(golem.getCore() < 0) {
                if(heldItem != null) {
                    AdditionalGolemCore core = null;
                    for(AdditionalGolemCore entry : GadomancyApi.getAdditionalGolemCores()) {
                        if(entry.getItem().isItemEqual(heldItem)) {
                            core = entry;
                            break;
                        }
                    }

                    if(core != null && heldItem.stackSize > 0) {
                        GadomancyApi.setAdditionalGolemCore(golem, core);
                        event.setCanceled(true);
                        if(!event.entityPlayer.capabilities.isCreativeMode) {
                            heldItem.stackSize--;
                        }
                    }
                }
            } else {
                if(!event.target.worldObj.isRemote) {
                    if(heldItem == null
                            || (heldItem.getItem() != ConfigItems.itemGolemBell
                            && heldItem.getItem() != ConfigItems.itemGolemUpgrade
                            && heldItem.getItem() != ConfigItems.itemGolemDecoration
                            && !(heldItem.getItem() instanceof ItemWandCasting))) {
                        AdditionalGolemCore core = GadomancyApi.getAdditionalGolemCore(golem);
                        if(core != null) {
                            if(core.hasGui() && !core.openGui(event.entityPlayer, golem)) {
                                event.entityPlayer.openGui(Gadomancy.instance, 0, golem.worldObj, golem.getEntityId(), 0, 0);
                            }
                            event.setCanceled(true);
                        }
                    }
                }
                if(heldItem != null && heldItem.getItem() == ConfigItems.itemGolemBell) {
                    AdditionalGolemCore core = GadomancyApi.getAdditionalGolemCore(golem);
                    if(core != null && !core.hasMarkers()) {
                        event.setCanceled(true);
                    }
                }
            }
        }
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public void on(ItemTooltipEvent event) {
        if(event.itemStack != null) {
            if(event.itemStack.getItem() instanceof ItemGolemPlacer
                    || event.itemStack.getItem() instanceof ItemAdditionalGolemPlacer) {
                if(RegisteredGolemStuff.upgradeRunicShield.hasUpgrade(event.itemStack)) {
                    event.toolTip.add("\u00a76" + StatCollector.translateToLocal("item.runic.charge") + " +" + RegisteredGolemStuff.upgradeRunicShield.getChargeLimit(event.itemStack));
                }

                AdditionalGolemCore core = GadomancyApi.getAdditionalGolemCore(event.itemStack);
                if(core != null) {
                    String searchStr = StatCollector.translateToLocal("item.ItemGolemCore.name");
                    for(int i = 0; i < event.toolTip.size(); i++) {
                        String line = event.toolTip.get(i);
                        if(line.contains(searchStr)) {
                            int index = line.indexOf('\u00a7', searchStr.length()) + 2;
                            event.toolTip.remove(i);
                            event.toolTip.add(i, line.substring(0, index) + StatCollector.translateToLocal(core.getUnlocalizedName()));
                            break;
                        }
                    }
                }
            }
        }
    }
}
