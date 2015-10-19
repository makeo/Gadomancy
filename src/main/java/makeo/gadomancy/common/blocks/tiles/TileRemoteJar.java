package makeo.gadomancy.common.blocks.tiles;

import thaumcraft.common.tiles.TileJarFillable;

import java.util.*;

/**
 * This class is part of the Gadomancy Mod
 * Gadomancy is Open Source and distributed under the
 * GNU LESSER GENERAL PUBLIC LICENSE
 * for more read the LICENSE file
 *
 * Created by makeo @ 14.10.2015 15:06
 */
public class TileRemoteJar extends TileJarFillable {
    public UUID networkId = null;

    private int count = 0;

    @Override
    public void updateEntity() {
        super.updateEntity();
        if (count % 5 == 0 && !getWorldObj().isRemote && networkId != null && amount < maxAmount) {
            count = 0;

            boolean containsThis = false;

            List<TileRemoteJar> network = getNetwork(networkId);

            Collections.sort(network, new Comparator<TileRemoteJar>() {
                @Override
                public int compare(TileRemoteJar o1, TileRemoteJar o2) {
                    return o2.amount - o1.amount;
                }
            });

            for(int i = 0; i < network.size(); i++) {
                TileRemoteJar jar = network.get(i);

                if(jar == this) {
                    containsThis = true;
                    continue;
                }

                if(jar.isInvalid()) {
                    network.remove(i);
                    i--;
                    continue;
                }

                if((amount+1) < jar.amount && this.addToContainer(jar.aspect, 1) == 0) {
                    jar.takeFromContainer(jar.aspect, 1);
                    break;
                }
            }

            if(!containsThis) {
                network.add(this);
            }
        }
        count++;
    }

    private static Map<UUID, List<TileRemoteJar>> networks = new HashMap<UUID, List<TileRemoteJar>>();

    private static List<TileRemoteJar> getNetwork(UUID id) {
        List<TileRemoteJar> network = networks.get(id);

        if(network == null) {
            network = new ArrayList<TileRemoteJar>();
            networks.put(id, network);
        }
        return network;
    }

    public void markForUpdate() {
        markDirty();
        getWorldObj().markBlockForUpdate(xCoord, yCoord, zCoord);
    }
}
