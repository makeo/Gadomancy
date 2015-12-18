package makeo.gadomancy.common.data;

import makeo.gadomancy.common.registration.RegisteredPotions;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagIntArray;

import java.util.ArrayList;
import java.util.List;

/**
 * This class is part of the Gadomancy Mod
 * Gadomancy is Open Source and distributed under the
 * GNU LESSER GENERAL PUBLIC LICENSE
 * for more read the LICENSE file
 *
 * Created by HellFirePvP @ 18.12.2015 20:21
 */
public class DataAchromatic extends AbstractData {

    private List<Integer> achromaticEntities = new ArrayList<Integer>();

    private List<Integer> addClientQueue = new ArrayList<Integer>();
    private List<Integer> removeClientQueue = new ArrayList<Integer>();

    public void handleApplication(EntityLivingBase entity) {
        if(entity.worldObj.isRemote) return;

        int entityId = entity.getEntityId();

        boolean needsUpdate = false;
        if(!addClientQueue.contains(entityId)) {
            addClientQueue.add(entityId);
            needsUpdate = true;
        }
        if(!achromaticEntities.contains(entityId)) {
            achromaticEntities.add(entityId);
            needsUpdate = true;
        }
        if(needsUpdate) {
            markDirty();
        }
    }

    public void handleRemoval(EntityLivingBase entity) {
        if(entity.worldObj.isRemote) return;

        int entityId = entity.getEntityId();

        boolean needsUpdate = false;
        if(!removeClientQueue.contains(entityId)) {
            removeClientQueue.add(entityId);
            needsUpdate = true;
        }
        if(achromaticEntities.contains(entityId)) {
            achromaticEntities.remove(entityId);
            needsUpdate = true;
        }

        if(needsUpdate) {
            markDirty();
        }
    }

    public void checkPotionEffect(EntityPlayerMP p) {
        if(p.isPotionActive(RegisteredPotions.ACHROMATIC)) {
            handleApplication(p);
        }
    }

    @Override
    public void writeAllDataToPacket(NBTTagCompound compound) {
        int[] array = new int[achromaticEntities.size()];
        for (int i = 0; i < achromaticEntities.size(); i++) {
            array[i] = achromaticEntities.get(i);
        }
        compound.setTag("additions", new NBTTagIntArray(array));
        compound.setTag("removals", new NBTTagIntArray(new int[0]));
    }

    @Override
    public void writeToPacket(NBTTagCompound compound) {
        int[] array = new int[removeClientQueue.size()];
        for (int i = 0; i < removeClientQueue.size(); i++) {
            array[i] = removeClientQueue.get(i);
        }
        compound.setTag("removals", new NBTTagIntArray(array));
        array = new int[addClientQueue.size()];
        for (int i = 0; i < addClientQueue.size(); i++) {
            array[i] = addClientQueue.get(i);
        }
        compound.setTag("additions", new NBTTagIntArray(array));

        removeClientQueue.clear();
        addClientQueue.clear();
    }

    @Override
    public void readRawFromPacket(NBTTagCompound compound) {
        int[] array = compound.getIntArray("removals");
        if(array != null && array.length > 0) {
            for(int i : array) {
                removeClientQueue.add(i);
            }
        }
        array = compound.getIntArray("additions");
        if(array != null && array.length > 0) {
            for(int i : array) {
                addClientQueue.add(i);
            }
        }
    }

    @Override
    public void handleIncomingData(AbstractData serverData) {
        DataAchromatic achromatic = (DataAchromatic) serverData;
        List<Integer> toAdd = achromatic.addClientQueue;
        achromaticEntities.addAll(toAdd);
        List<Integer> toRemove = achromatic.removeClientQueue;
        achromaticEntities.removeAll(toRemove);
    }

    public static class Provider extends ProviderAutoAllocate<DataAchromatic> {

        public Provider(String key) {
            super(key);
        }

        @Override
        public DataAchromatic provideNewInstance() {
            return new DataAchromatic();
        }
    }

}
