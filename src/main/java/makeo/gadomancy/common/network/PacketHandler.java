package makeo.gadomancy.common.network;

import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import cpw.mods.fml.relauncher.Side;
import makeo.gadomancy.common.Gadomancy;
import makeo.gadomancy.common.network.packets.*;

/**
 * This class is part of the Gadomancy Mod
 * Gadomancy is Open Source and distributed under the
 * GNU LESSER GENERAL PUBLIC LICENSE
 * for more read the LICENSE file
 *
 * Created by makeo @ 03.07.2015 19:13
 */
public class PacketHandler {
    public static final SimpleNetworkWrapper INSTANCE = NetworkRegistry.INSTANCE.newSimpleChannel(Gadomancy.MODID.toLowerCase());

    public static void init() {
        int id = 0;

        //Random stuff
        INSTANCE.registerMessage(PacketUpdateGolemTypeOrder.class, PacketUpdateGolemTypeOrder.class, id++, Side.CLIENT);
        INSTANCE.registerMessage(PacketStartAnimation.class, PacketStartAnimation.class, id++, Side.CLIENT);
        INSTANCE.registerMessage(PacketAnimationAbsorb.class, PacketAnimationAbsorb.class, id++, Side.CLIENT);

        //Using TC Clientside content
        INSTANCE.registerMessage(PacketTCNodeBolt.class, PacketTCNodeBolt.class, id++, Side.CLIENT);
        INSTANCE.registerMessage(PacketTCWispyLine.class, PacketTCWispyLine.class, id++, Side.CLIENT);
        INSTANCE.registerMessage(PacketTCNotificationText.class, PacketTCNotificationText.class, id++, Side.CLIENT);

        //Familiar
        INSTANCE.registerMessage(PacketFamiliarBolt.class, PacketFamiliarBolt.class, id++, Side.CLIENT);

        //Sync data
        INSTANCE.registerMessage(PacketSyncConfigs.class, PacketSyncConfigs.class, id++, Side.CLIENT);
        INSTANCE.registerMessage(PacketSyncData.class, PacketSyncData.class, id++, Side.CLIENT);
    }
}
