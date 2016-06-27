package makeo.gadomancy.common.data;

import baubles.api.BaublesApi;
import makeo.gadomancy.client.util.FamiliarHandlerClient;
import makeo.gadomancy.common.familiar.FamiliarAIController_Old;
import makeo.gadomancy.common.familiar.FamiliarController;
import makeo.gadomancy.common.items.baubles.ItemEtherealFamiliar;
import makeo.gadomancy.common.items.baubles.ItemFamiliar_Old;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.world.World;
import thaumcraft.api.aspects.Aspect;

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
 * Created by HellFirePvP @ 14.12.2015 22:06
 */
public class DataFamiliar extends AbstractData {

    private List<FamiliarData> playersWithFamiliar = new ArrayList<FamiliarData>();
    private Map<EntityPlayer, FamiliarController> familiarControllers = new HashMap<EntityPlayer, FamiliarController>();

    private List<FamiliarData> addClientQueue = new ArrayList<FamiliarData>();
    private List<FamiliarData> removeClientQueue = new ArrayList<FamiliarData>();

    public boolean hasFamiliar(EntityPlayer player) {
        for(FamiliarData fam : playersWithFamiliar) {
            if(fam.owner.equals(player.getCommandSenderName())) return true;
        }
        return false;
    }

    public void handleEquip(World world, EntityPlayer player, Aspect aspect) {
        if(world.isRemote) return;

        String playerName = player.getCommandSenderName();
        FamiliarData data = new FamiliarData(playerName, aspect.getTag());

        if(!addClientQueue.contains(data)) addClientQueue.add(data);
        if(!playersWithFamiliar.contains(data)) playersWithFamiliar.add(data);
        markDirty();

        if(!familiarControllers.containsKey(player)) {
            FamiliarController controller = new FamiliarController(player);
            familiarControllers.put(player, controller);
        }
    }

    public void handleUnequip(World world, EntityPlayer player, Aspect aspect) {
        if(world.isRemote) return;

        String playerName = player.getCommandSenderName();
        FamiliarData data = new FamiliarData(playerName, aspect.getTag());

        if(!removeClientQueue.contains(data)) removeClientQueue.add(data);
        if(playersWithFamiliar.contains(data)) playersWithFamiliar.remove(data);
        markDirty();

        familiarControllers.remove(player);
    }

    public void equipTick(World world, EntityPlayer player, Aspect aspect) {
        if(world.isRemote) return;

        FamiliarData data = new FamiliarData(player.getCommandSenderName(), aspect.getTag());

        IInventory baublesInv = BaublesApi.getBaubles(player);
        if(baublesInv.getStackInSlot(0) == null) {
            handleUnequip(world, player, aspect);
            return;
        }

        if(familiarControllers.get(player) == null || !playersWithFamiliar.contains(data)) {
            handleEquip(world, player, aspect);
        }

        familiarControllers.get(player).tick();
    }

    public void checkPlayerEquipment(EntityPlayer p) {
        IInventory baublesInv = BaublesApi.getBaubles(p);
        if(baublesInv.getStackInSlot(0) != null) {
            ItemStack amulet = baublesInv.getStackInSlot(0);
            if(amulet.getItem() != null && amulet.getItem() instanceof ItemEtherealFamiliar) {
                Aspect a = ItemEtherealFamiliar.getFamiliarAspect(amulet);
                if(a != null) {
                    handleEquip(p.worldObj, p, a);
                }
            }
        }
    }

    @Override
    public boolean needsUpdate() {
        return true;
    }

    @Override
    public void writeAllDataToPacket(NBTTagCompound compound) {
        NBTTagList list = new NBTTagList();
        for(FamiliarData data : playersWithFamiliar) {
            NBTTagCompound cmp = new NBTTagCompound();
            cmp.setString("owner", data.owner);
            cmp.setString("aspect", data.aspectTag);
            list.appendTag(cmp);
        }
        compound.setTag("additions", list);
        compound.setTag("removals", new NBTTagList());
    }

    @Override
    public void writeToPacket(NBTTagCompound compound) {
        NBTTagList removal = new NBTTagList();
        for(FamiliarData data : removeClientQueue) {
            NBTTagCompound cmp = new NBTTagCompound();
            cmp.setString("owner", data.owner);
            cmp.setString("aspect", data.aspectTag);
            removal.appendTag(cmp);
        }
        compound.setTag("removals", removal);

        NBTTagList additions = new NBTTagList();
        for(FamiliarData data : addClientQueue) {
            NBTTagCompound cmp = new NBTTagCompound();
            cmp.setString("owner", data.owner);
            cmp.setString("aspect", data.aspectTag);
            additions.appendTag(cmp);
        }
        compound.setTag("additions", additions);

        removeClientQueue.clear();
        addClientQueue.clear();
    }

    @Override
    public void readRawFromPacket(NBTTagCompound compound) {
        NBTTagList remove = compound.getTagList("removals", new NBTTagCompound().getId());
        for (int i = 0; i < remove.tagCount(); i++) {
            NBTTagCompound cmp = remove.getCompoundTagAt(i);
            String owner = cmp.getString("owner");
            String aspect = cmp.getString("aspect");
            removeClientQueue.add(new FamiliarData(owner, aspect));
        }
        NBTTagList additions = compound.getTagList("additions", new NBTTagCompound().getId());
        for (int i = 0; i < additions.tagCount(); i++) {
            NBTTagCompound cmp = additions.getCompoundTagAt(i);
            String owner = cmp.getString("owner");
            String aspect = cmp.getString("aspect");
            addClientQueue.add(new FamiliarData(owner, aspect));
        }
    }

    @Override
    public void handleIncomingData(AbstractData serverData) {
        DataFamiliar familiarData = (DataFamiliar) serverData;
        List<FamiliarData> toAdd = familiarData.addClientQueue;
        playersWithFamiliar.addAll(toAdd);
        FamiliarHandlerClient.handleAdditions(toAdd);
        List<FamiliarData> toRemove = familiarData.removeClientQueue;
        playersWithFamiliar.removeAll(toRemove);
        FamiliarHandlerClient.handleRemovals(toRemove);
    }

    public void handleUnsafeUnequip(EntityPlayer player) {
        FamiliarData data = null;
        for(FamiliarData fam : playersWithFamiliar) {
            if(fam.owner.equals(player.getCommandSenderName())) {
                data = fam;
            }
        }
        if(data != null) {
            handleUnequip(player.worldObj, player, Aspect.getAspect(data.aspectTag));
        }
    }

    public static class FamiliarData {

        public final String owner;
        public final String aspectTag;

        public FamiliarData(String owner, String aspectTag) {
            this.owner = owner;
            this.aspectTag = aspectTag;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            FamiliarData that = (FamiliarData) o;

            return !(aspectTag != null ? !aspectTag.equals(that.aspectTag) : that.aspectTag != null) && !(owner != null ? !owner.equals(that.owner) : that.owner != null);

        }

        @Override
        public int hashCode() {
            int result = owner != null ? owner.hashCode() : 0;
            result = 31 * result + (aspectTag != null ? aspectTag.hashCode() : 0);
            return result;
        }
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
