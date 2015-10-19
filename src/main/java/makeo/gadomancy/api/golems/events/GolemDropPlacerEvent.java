package makeo.gadomancy.api.golems.events;

import cpw.mods.fml.common.eventhandler.Event;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import thaumcraft.common.entities.golems.EntityGolemBase;

/**
 * This class is part of the Gadomancy Mod
 * Gadomancy is Open Source and distributed under the
 * GNU LESSER GENERAL PUBLIC LICENSE
 * for more read the LICENSE file
 *
 * Created by makeo @ 14.10.2015 14:09
 */
public class GolemDropPlacerEvent extends Event {
    public final EntityPlayer player;
    public final EntityItem placer;
    public final EntityGolemBase golem;

    public GolemDropPlacerEvent(EntityPlayer player, EntityItem placer, EntityGolemBase golem) {
        this.player = player;
        this.placer = placer;
        this.golem = golem;
    }
}
