package makeo.gadomancy.common.data;

import baubles.api.BaublesApi;
import makeo.gadomancy.client.util.FamiliarHandlerClient;
import makeo.gadomancy.common.familiar.FamiliarAIController;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
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
 * <p/>
 * Created by HellFirePvP @ 14.12.2015 22:06
 */
public class DataFamiliar extends AbstractData {

    private List<String> playersWithFamiliar = new ArrayList<String>();
    private Map<EntityPlayer, FamiliarAIController> familiarAI = new HashMap<EntityPlayer, FamiliarAIController>();

    private List<String> addClientQueue = new ArrayList<String>();
    private List<String> removeClientQueue = new ArrayList<String>();

    public void handleEquip(World world, EntityPlayer player) {
        if(world.isRemote) return;

        String playerName = player.getCommandSenderName();

        if(!addClientQueue.contains(playerName)) addClientQueue.add(playerName);
        if(!playersWithFamiliar.contains(playerName)) playersWithFamiliar.add(playerName);
        markDirty();

        if(!familiarAI.containsKey(player)) {
            FamiliarAIController controller = new FamiliarAIController(player);
            controller.registerDefaultTasks();
            familiarAI.put(player, controller);
        }
    }

    public void handleUnequip(World world, EntityPlayer player) {
        if(world.isRemote) return;

        String playerName = player.getCommandSenderName();

        if(!removeClientQueue.contains(playerName)) removeClientQueue.add(playerName);
        if(playersWithFamiliar.contains(playerName)) playersWithFamiliar.remove(playerName);
        markDirty();

        familiarAI.remove(player);
    }

    public void equipTick(World world, EntityPlayer player) {
        if(world.isRemote) return;

        IInventory baublesInv = BaublesApi.getBaubles(player);
        if(baublesInv.getStackInSlot(0) == null) {
            handleUnequip(world, player);
            return;
        }

        if(familiarAI.get(player) == null || !playersWithFamiliar.contains(player.getCommandSenderName())) {
            handleEquip(world, player);
        }

        familiarAI.get(player).scheduleTick();
    }

    public void checkPlayerEquipment(EntityPlayer p) {
        IInventory baublesInv = BaublesApi.getBaubles(p);
        if(baublesInv.getStackInSlot(0) != null) {
            handleEquip(p.worldObj, p);
        }
    }

    @Override
    public void writeAllDataToPacket(NBTTagCompound compound) {
        NBTTagList list = new NBTTagList();
        for(String s : playersWithFamiliar) {
            list.appendTag(new NBTTagString(s));
        }
        compound.setTag("additions", list);
        compound.setTag("removals", new NBTTagList());
    }

    @Override
    public void writeToPacket(NBTTagCompound compound) {
        NBTTagList removal = new NBTTagList();
        for(String s : removeClientQueue) {
            removal.appendTag(new NBTTagString(s));
        }
        compound.setTag("removals", removal);

        NBTTagList additions = new NBTTagList();
        for(String s : addClientQueue) {
            additions.appendTag(new NBTTagString(s));
        }
        compound.setTag("additions", additions);

        removeClientQueue.clear();
        addClientQueue.clear();
    }

    @Override
    public void readRawFromPacket(NBTTagCompound compound) {
        NBTTagList remove = compound.getTagList("removals", new NBTTagString().getId());
        for (int i = 0; i < remove.tagCount(); i++) {
            removeClientQueue.add(remove.getStringTagAt(i));
        }
        NBTTagList additions = compound.getTagList("additions", new NBTTagString().getId());
        for (int i = 0; i < additions.tagCount(); i++) {
            addClientQueue.add(additions.getStringTagAt(i));
        }
    }

    @Override
    public void handleIncomingData(AbstractData serverData) {
        DataFamiliar familiarData = (DataFamiliar) serverData;
        List<String> toAdd = familiarData.addClientQueue;
        playersWithFamiliar.addAll(toAdd);
        FamiliarHandlerClient.handleAdditions(toAdd);
        List<String> toRemove = familiarData.removeClientQueue;
        playersWithFamiliar.removeAll(toRemove);
        FamiliarHandlerClient.handleRemovals(toRemove);
    }

    public static class Provider extends ProviderAutoAllocate<DataFamiliar> {

        public Provider(String key) {
            super(key);
        }

        @Override
        public DataFamiliar provideNewInstance() {
            return new DataFamiliar();
        }
    }

}
