package makeo.gadomancy.common.familiar;

import baubles.api.BaublesApi;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
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
        if(!world.isRemote) {
            FamiliarAIController controller = new FamiliarAIController(player);
            controller.registerDefaultTasks();
            familiarAI.put(player, controller);
        }
    }

    @Override
    public void notifyUnequip(World world, ItemStack familiarStack, EntityPlayer player) {
        if(!world.isRemote) familiarAI.remove(player);
    }

    @Override
    public void equippedTick(World world, ItemStack familiarStack, EntityPlayer player) {
        if(!world.isRemote) {
            IInventory baublesInv = BaublesApi.getBaubles(player);
            if(baublesInv.getStackInSlot(0) == null) {
                notifyUnequip(world, familiarStack, player);
                return;
            }

            if(familiarAI.get(player) == null) {
                notifyEquip(world, familiarStack, player);
            }
            familiarAI.get(player).sheduleTick();
        }
    }

}
