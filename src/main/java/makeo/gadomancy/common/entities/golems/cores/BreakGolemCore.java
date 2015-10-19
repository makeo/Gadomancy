package makeo.gadomancy.common.entities.golems.cores;

import makeo.gadomancy.api.golems.cores.AdditionalGolemCore;
import makeo.gadomancy.common.entities.ai.AIBreakBlock;
import makeo.gadomancy.common.registration.RegisteredItems;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import thaumcraft.common.entities.ai.misc.AIOpenDoor;
import thaumcraft.common.entities.ai.misc.AIReturnHome;
import thaumcraft.common.entities.golems.EntityGolemBase;

/**
 * This class is part of the Gadomancy Mod
 * Gadomancy is Open Source and distributed under the
 * GNU LESSER GENERAL PUBLIC LICENSE
 * for more read the LICENSE file
 *
 * Created by makeo @ 31.08.2015 13:38
 */
public class BreakGolemCore extends AdditionalGolemCore {
    @Override
    public void setupGolem(EntityGolemBase golem) {
        golem.tasks.addTask(0, new AIBreakBlock(golem));
        golem.tasks.addTask(5, new AIOpenDoor(golem, true));
        golem.tasks.addTask(6, new AIReturnHome(golem));
        golem.tasks.addTask(7, new EntityAIWatchClosest(golem, EntityPlayer.class, 6.0F));
        golem.tasks.addTask(8, new EntityAILookIdle(golem));
    }

    @Override
    public boolean hasGui() {
        return true;
    }

    @Override
    public ItemStack getToolItem(EntityGolemBase golem) {
        return golem.getCarriedForDisplay();
    }

    @Override
    public String getUnlocalizedGuiText() {
        return "gadomancy.golemblurb.breakcore";
    }

    @Override
    public String getUnlocalizedName() {
        return "gadomancy.golem.breakcore";
    }

    @Override
    public ItemStack getItem() {
        return new ItemStack(RegisteredItems.itemGolemCoreBreak);
    }
}
