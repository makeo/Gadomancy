package makeo.gadomancy.api.golems.events;

import cpw.mods.fml.common.eventhandler.Event;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import thaumcraft.common.entities.golems.EntityGolemBase;

/**
 * This class is part of the Gadomancy Mod
 * Gadomancy is Open Source and distributed under the
 * GNU LESSER GENERAL PUBLIC LICENSE
 * for more read the LICENSE file
 *
 * Created by makeo @ 14.10.2015 14:09
 */
public class PlacerCreateGolemEvent extends Event {
    public final EntityPlayer player;
    public final ItemStack placer;
    public final EntityGolemBase golem;

    public PlacerCreateGolemEvent(EntityPlayer player, EntityGolemBase golem, ItemStack placer) {
        this.player = player;
        this.golem = golem;
        this.placer = placer;
    }
}
