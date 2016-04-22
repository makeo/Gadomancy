package makeo.gadomancy.common.event;

import makeo.gadomancy.common.data.DataAchromatic;
import makeo.gadomancy.common.data.SyncDataHolder;
import makeo.gadomancy.common.registration.RegisteredPotions;
import net.minecraft.entity.EntityLivingBase;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

/**
 * HellFirePvP@Admin
 * Date: 20.04.2016 / 18:49
 * on Gadomancy_1_8
 * EventHandlerGolem
 */
public class EventHandlerGolem {

    @SubscribeEvent
    public void onJoin(EntityJoinWorldEvent event) {
        if(!event.world.isRemote && event.entity instanceof EntityLivingBase) {
            if(((EntityLivingBase) event.entity).isPotionActive(RegisteredPotions.ACHROMATIC)) {
                ((DataAchromatic) SyncDataHolder.getDataServer("AchromaticData")).handleApplication((EntityLivingBase) event.entity);
            }
        }
    }

}
