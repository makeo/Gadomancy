package makeo.gadomancy.common.familiar;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import java.util.HashMap;
import java.util.Map;

/**
 * This class is part of the Gadomancy Mod
 * Gadomancy is Open Source and distributed under the
 * GNU LESSER GENERAL PUBLIC LICENSE
 * for more read the LICENSE file
 *
 * Created by HellFirePvP @ 31.10.2015 12:10
 */
public class FamiliarHandlerServer implements FamiliarHandler {

    private Map<EntityPlayer, FamiliarAIController> familiarAI;
    //TODO has to handle attacks, protection, ...

    @Override
    public void setupPostInit() {
        familiarAI = new HashMap<EntityPlayer, FamiliarAIController>();
    }

    @Override
    public void notifyEquip(World world, ItemStack familiarStack, EntityPlayer player) {
        if(!world.isRemote) familiarAI.put(player, new FamiliarAIController(player));
    }

    @Override
    public void notifyUnequip(World world, ItemStack familiarStack, EntityPlayer player) {
        if(!world.isRemote) familiarAI.remove(player);
    }

    @Override
    public void equippedTick(World world, ItemStack familiarStack, EntityPlayer player) {
        if(!world.isRemote) familiarAI.get(player).sheduleTick();
    }

}
