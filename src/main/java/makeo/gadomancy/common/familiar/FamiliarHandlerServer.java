package makeo.gadomancy.common.familiar;

import baubles.api.BaublesApi;
import makeo.gadomancy.common.network.PacketHandler;
import makeo.gadomancy.common.network.packets.PacketFamiliar;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This class is part of the Gadomancy Mod
 * Gadomancy is Open Source and distributed under the
 * GNU LESSER GENERAL PUBLIC LICENSE
 * for more read the LICENSE file
 *
 * Created by HellFirePvP @ 31.10.2015 12:10
 */
public class FamiliarHandlerServer {

    private List<String> activeFamiliars = new ArrayList<String>();
    private Map<EntityPlayer, FamiliarAIController> familiarAI;

    public void setupPostInit() {
        familiarAI = new HashMap<EntityPlayer, FamiliarAIController>();
    }

    public void notifyEquip(World world, EntityPlayer player) {
        if(!world.isRemote) {
            if(!familiarAI.containsKey(player)) {
                FamiliarAIController controller = new FamiliarAIController(player);
                controller.registerDefaultTasks();
                familiarAI.put(player, controller);
            }

            if(!activeFamiliars.contains(player.getCommandSenderName())) activeFamiliars.add(player.getCommandSenderName());

            PacketFamiliar.PacketFamiliarSyncSingle pkt;
            List entityPlayers = MinecraftServer.getServer().getConfigurationManager().playerEntityList;
            for(Object playerEntity : entityPlayers) {
                if(!(playerEntity instanceof EntityPlayer)) continue;

                pkt = new PacketFamiliar.PacketFamiliarSyncSingle(true, player.getCommandSenderName());
                PacketHandler.INSTANCE.sendTo(pkt, (net.minecraft.entity.player.EntityPlayerMP) playerEntity);
            }
        }
    }

    public void notifyUnequip(World world, EntityPlayer player) {
        if(!world.isRemote) {
            familiarAI.remove(player);

            activeFamiliars.remove(player.getCommandSenderName());

            PacketFamiliar.PacketFamiliarSyncSingle pkt;
            List entityPlayers = MinecraftServer.getServer().getConfigurationManager().playerEntityList;
            for(Object playerEntity : entityPlayers) {
                if(!(playerEntity instanceof EntityPlayer)) continue;

                pkt = new PacketFamiliar.PacketFamiliarSyncSingle(false, player.getCommandSenderName());
                PacketHandler.INSTANCE.sendTo(pkt, (net.minecraft.entity.player.EntityPlayerMP) playerEntity);
            }
        }
    }

    public void equippedTick(World world, EntityPlayer player) {
        if(!world.isRemote) {
            IInventory baublesInv = BaublesApi.getBaubles(player);
            if(baublesInv.getStackInSlot(0) == null) {
                notifyUnequip(world, player);
                return;
            }

            if(familiarAI.get(player) == null || !activeFamiliars.contains(player.getCommandSenderName())) {
                notifyEquip(world, player);
            }

            familiarAI.get(player).sheduleTick();
        }
    }

    public List<String> getCurrentActiveFamiliars() {
        return activeFamiliars;
    }

    public void checkPlayerEquipment(EntityPlayer p) {
        IInventory baublesInv = BaublesApi.getBaubles(p);
        if(baublesInv.getStackInSlot(0) != null) {
            notifyEquip(p.worldObj, p);
        }
    }
}
