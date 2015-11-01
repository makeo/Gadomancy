package makeo.gadomancy.common.events;

import cpw.mods.fml.common.eventhandler.EventPriority;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import makeo.gadomancy.common.familiar.FamiliarAIController;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.event.entity.EntityEvent;
import net.minecraftforge.event.entity.living.LivingSetAttackTargetEvent;

/**
 * This class is part of the Gadomancy Mod
 * Gadomancy is Open Source and distributed under the
 * GNU LESSER GENERAL PUBLIC LICENSE
 * for more read the LICENSE file
 * <p/>
 * Created by HellFirePvP @ 31.10.2015 15:28
 */
public class EventHandlerEntity {

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public void on(EntityEvent.EntityConstructing e) {
        if (e.entity instanceof EntityPlayer) {
            //e.entity.registerExtendedProperties(Gadomancy.MODID, new ExtendedPlayerProperties((EntityPlayer) e.entity));
        }
    }

    @SubscribeEvent
    public void on(LivingSetAttackTargetEvent targetEvent) {
        if(targetEvent.target instanceof EntityPlayer) {
            FamiliarAIController.notifyTargetEvent(targetEvent.entityLiving, (EntityPlayer) targetEvent.target);
        }
    }

}
