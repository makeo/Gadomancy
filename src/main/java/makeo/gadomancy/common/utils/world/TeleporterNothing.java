package makeo.gadomancy.common.utils.world;

import net.minecraft.entity.Entity;
import net.minecraft.world.Teleporter;
import net.minecraft.world.WorldServer;

/**
 * This class is part of the Gadomancy Mod
 * Gadomancy is Open Source and distributed under the
 * GNU LESSER GENERAL PUBLIC LICENSE
 * for more read the LICENSE file
 *
 * Created by HellFirePvP @ 05.11.2015 13:57
 */
public class TeleporterNothing extends Teleporter {

    public TeleporterNothing(WorldServer worldServer) {
        super(worldServer);
    }

    @Override
    public void removeStalePortalLocations(long p_85189_1_) {}

    @Override
    public void placeInPortal(Entity p_77185_1_, double p_77185_2_, double p_77185_4_, double p_77185_6_, float p_77185_8_) {}

    @Override
    public boolean placeInExistingPortal(Entity p_77184_1_, double p_77184_2_, double p_77184_4_, double p_77184_6_, float p_77184_8_) {
        return true;
    }

    @Override
    public boolean makePortal(Entity entity) {
        return true;
    }
}
