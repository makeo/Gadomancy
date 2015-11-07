package makeo.gadomancy.common.events;

import cpw.mods.fml.common.eventhandler.EventPriority;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import makeo.gadomancy.common.familiar.FamiliarAIController;
import makeo.gadomancy.common.utils.world.TCMazeHandler;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.event.entity.EntityEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingSetAttackTargetEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;

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

    @SubscribeEvent
    public void on(LivingDeathEvent event) {
        if(!event.entity.worldObj.isRemote && event.entityLiving instanceof EntityPlayer) {
            TCMazeHandler.closeSession((EntityPlayer) event.entityLiving, false);
        }
    }

}
