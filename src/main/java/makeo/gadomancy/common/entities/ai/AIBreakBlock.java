package makeo.gadomancy.common.entities.ai;

import makeo.gadomancy.common.entities.fake.AdvancedFakePlayer;
import makeo.gadomancy.common.entities.fake.GolemFakePlayer;
import net.minecraft.block.Block;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.server.management.ItemInWorldManager;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.util.ForgeDirection;
import thaumcraft.common.config.Config;
import thaumcraft.common.entities.golems.EntityGolemBase;
import thaumcraft.common.entities.golems.Marker;
import thaumcraft.common.lib.utils.InventoryUtils;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This class is part of the Gadomancy Mod
 * Gadomancy is Open Source and distributed under the
 * GNU LESSER GENERAL PUBLIC LICENSE
 * for more read the LICENSE file
 *
 * Created by makeo @ 17.09.2015 20:22
 */
public class AIBreakBlock extends EntityAIBase {
    private static final int BLACKLIST_TICKS = 20*40;

    private EntityGolemBase golem;
    private AdvancedFakePlayer player;

    private Marker currentMarker;
    private boolean hasValidTool = false;

    private Map<Marker, Integer> blacklist = new HashMap<Marker, Integer>();
    private int blacklistCount = 0;

    public AIBreakBlock(EntityGolemBase golem) {
        this.golem = golem;

        if (golem.worldObj instanceof WorldServer) {
            this.player = new GolemFakePlayer((WorldServer) golem.worldObj, golem);
        }

        setMutexBits(3);
    }

    @Override
    public boolean shouldExecute() {
        if (golem.ticksExisted % Config.golemDelay > 0) {
            return false;
        }

        currentMarker = getNextMarker();

        if(!hasValidTool()) {
            if(isInHomeRange()) {
                return true;
            }
            return false;
        }

        return currentMarker != null;
    }

    private int count = 0;
    private int clickCount = 0;

    @Override
    public boolean continueExecuting() {
        if(hasValidTool) {
            if(!hasBlock(currentMarker)) {
                return false;
            }

            if(distanceSquaredToGolem(currentMarker) < 1) {
                if(golem.getCarried() == null) {
                    golem.startActionTimer();
                } else {
                    golem.startRightArmTimer();
                }

                if(clickCount % (7 - Math.min(6, golem.getGolemStrength())) == 0) {
                    doLeftClick();
                }
                clickCount++;


                golem.getLookHelper().setLookPosition(currentMarker.x + 0.5D, currentMarker.y + 0.5D, currentMarker.z + 0.5D, 30.0F, 30.0F);

                count = 0;
            } else {
                if(count == 20) {
                    count = 0;

                    ForgeDirection dir = ForgeDirection.getOrientation(currentMarker.side);
                    boolean path = golem.getNavigator().tryMoveToXYZ(currentMarker.x + 0.5D + dir.offsetX, currentMarker.y + 0.5D + dir.offsetY, currentMarker.z + 0.5D + dir.offsetZ, golem.getAIMoveSpeed());
                    if(!path) {
                        if (blacklistCount > 10) {
                            blacklist.put(currentMarker, golem.ticksExisted);
                            return false;
                        }
                        blacklistCount++;
                    }
                }
                count++;
                clickCount = 0;
            }
        } else {
            if(golem.ticksExisted % Config.golemDelay > 0) {
                if(isInHomeRange()) {
                    IInventory homeChest = getHomeChest();
                    if(homeChest != null) {
                        trySwitchTool(homeChest);
                    }
                } else {
                    return false;
                }
            }
        }

        return true;
    }

    @Override
    public void resetTask() {
        currentMarker = null;
        blacklistCount = 0;
        cancelLeftClick();
    }

    public void doLeftClick() {
        ItemStack tool = golem.getCarried();
        player.setHeldItem(tool);

        player.theItemInWorldManager.updateBlockRemoving();

        if (this.player.theItemInWorldManager.durabilityRemainingOnBlock == -1 || !this.player.theItemInWorldManager.isDestroyingBlock)
        {
            this.player.theItemInWorldManager.onBlockClicked(currentMarker.x, currentMarker.y, currentMarker.z, currentMarker.side);
        }
        else if (this.player.theItemInWorldManager.durabilityRemainingOnBlock >= 9)
        {
            this.player.theItemInWorldManager.uncheckedTryHarvestBlock(currentMarker.x, currentMarker.y, currentMarker.z);
            this.player.theItemInWorldManager.durabilityRemainingOnBlock = -1;

            if (tool != null) {
                Block block = golem.worldObj.getBlock(currentMarker.x, currentMarker.y, currentMarker.z);
                tool.getItem().onBlockDestroyed(tool, golem.worldObj, block, currentMarker.x, currentMarker.y, currentMarker.z, this.player);
            }
        }
        hasValidTool();
        golem.updateCarried();
    }

    public void cancelLeftClick() {
        ItemInWorldManager manager = player.theItemInWorldManager;
        if(manager.isDestroyingBlock)
            player.theItemInWorldManager.cancelDestroyingBlock(manager.partiallyDestroyedBlockX, manager.partiallyDestroyedBlockY, manager.partiallyDestroyedBlockZ);
    }

    private Marker getNextMarker() {
        List<Marker> markers = golem.getMarkers();

        Collections.sort(markers, new Comparator<Marker>() {
            @Override
            public int compare(Marker m1, Marker m2) {
                return (int) (distanceSquaredToGolem(m1) - distanceSquaredToGolem(m2));
            }
        });

        for(Marker marker : markers) {
            if(isValid(marker)) {
                return marker;
            }
        }
        return null;
    }

    private double distanceSquaredToGolem(Marker marker) {
        return distanceSquaredToGolem(marker.x, marker.y, marker.z, marker.side);
    }

    private double distanceSquaredToGolem(double x, double y, double z, int facing) {
        ForgeDirection dir = ForgeDirection.getOrientation(facing);
        return this.golem.getDistanceSq(x + 0.5 + (0.5*dir.offsetX),
                y + 0.5 + (0.5*dir.offsetY), z + 0.5 + (0.5*dir.offsetZ));
    }

    private boolean isInHomeRange() {
        ChunkCoordinates home = golem.getHomePosition();
        return golem.getDistanceSq(home.posX + 0.5, home.posY + 0.5, home.posZ + 0.5) < 3;
    }

    private boolean hasBlock(Marker marker) {
        return marker != null && !golem.worldObj.isAirBlock(marker.x, marker.y, marker.z);
    }

    private boolean isValid(Marker marker) {
        if(marker == null) return false;

        if(blacklist.containsKey(marker)) {
            if(blacklist.get(marker) + BLACKLIST_TICKS >= golem.ticksExisted) {
                return false;
            } else {
                blacklist.remove(marker);
            }
        }

        float range = golem.getRange();
        if(golem.getHomePosition().getDistanceSquared(marker.x, marker.y, marker.z) > range * range) {
            return false;
        }

        Block block = golem.worldObj.getBlock(marker.x, marker.y, marker.z);

        if(!block.isAir(golem.worldObj, marker.x, marker.y, marker.z)) {
            ItemStack blockStack = new ItemStack(Item.getItemFromBlock(block));
            boolean empty = true;
            for(int slot = 0; slot < golem.inventory.slotCount; slot++) {
                ItemStack stack = golem.inventory.inventory[slot];

                if(stack != null && Block.getBlockFromItem(stack.getItem()) != Blocks.air) {
                    empty = false;
                    if((marker.color == -1 || marker.color == golem.colors[slot])
                            && InventoryUtils.areItemStacksEqual(blockStack, stack,
                            golem.checkOreDict(), golem.ignoreDamage(), golem.ignoreNBT())) {
                        return true;
                    }
                }
            }

            if(empty) {
                return true;
            }
        }
        return false;
    }

    private IInventory getHomeChest() {
        ChunkCoordinates coords = golem.getHomePosition();
        ForgeDirection facing = ForgeDirection.getOrientation(golem.homeFacing);
        TileEntity tile = golem.worldObj.getTileEntity(coords.posX - facing.offsetX, coords.posY - facing.offsetY, coords.posZ - facing.offsetZ);
        if(tile != null && tile instanceof IInventory) {
            return (IInventory) tile;
        }
        return null;
    }

    private void trySwitchTool(IInventory inv) {
        for(int slot = 0; slot < inv.getSizeInventory(); slot++) {
            ItemStack stack = inv.getStackInSlot(slot);

            ItemStack current = golem.getCarried();
            if(current != null) {
                current = InventoryUtils.insertStack(inv, current, golem.homeFacing, true);
                if(current != null) {
                    return;
                }
                golem.setCarried(null);

                if(hasValidTool()) {
                    hasValidTool = true;
                    return;
                }
            }

            if(stack != null && isValidTool(stack)) {
                stack = stack.copy();

                stack.stackSize = 1;
                stack = InventoryUtils.extractStack(inv, stack, golem.homeFacing, false, false, false, true);

                if(stack != null) {
                    golem.setCarried(stack);
                    golem.updateCarried();
                    player.setHeldItem(stack);

                    hasValidTool = true;
                    break;
                }
            }
        }
    }

    private boolean hasValidTool() {
        hasValidTool = isValidTool(golem.getCarried());
        return hasValidTool;
    }

    private boolean isValidTool(ItemStack tool) {
        if(tool == null || Block.getBlockFromItem(tool.getItem()) == Blocks.air) {
            boolean empty = true;
            for(int slot = 0; slot < golem.inventory.slotCount; slot++) {
                ItemStack stack = golem.inventory.inventory[slot];
                if(stack != null && Block.getBlockFromItem(stack.getItem()) == Blocks.air
                        && (currentMarker == null || golem.colors[slot] == -1 || currentMarker.color == -1 || golem.colors[slot] == currentMarker.color)) {
                    empty = false;

                    if(tool == null) {
                        return false;
                    }

                    if(InventoryUtils.areItemStacksEqual(tool, stack,
                                golem.checkOreDict(), golem.ignoreDamage(), golem.ignoreNBT())) {
                        return true;
                    }
                }
            }

            if(empty && tool == null) {
                return true;
            }
        }
        return false;
    }
}
