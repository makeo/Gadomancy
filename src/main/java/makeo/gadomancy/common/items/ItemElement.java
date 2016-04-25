package makeo.gadomancy.common.items;

import makeo.gadomancy.common.Gadomancy;
import makeo.gadomancy.common.entities.EntityItemElement;
import makeo.gadomancy.common.registration.RegisteredItems;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.world.World;

import java.util.List;

/**
 * HellFirePvP@Admin
 * Date: 23.04.2016 / 01:52
 * on Gadomancy
 * ItemElementVoid
 */
public class ItemElement extends Item {

    public ItemElement() {
        setMaxStackSize(1);
        setHasSubtypes(true);
        setUnlocalizedName("ItemElement");
        setTextureName(Gadomancy.MODID + ":element");
        setCreativeTab(RegisteredItems.creativeTab);
    }

    @Override
    public String getUnlocalizedName(ItemStack stack) {
        return getUnlocalizedName() + "." + stack.getItemDamage();
    }

    @Override
    public int getMetadata(int metadata) {
        return metadata;
    }

    @Override
    public void onUpdate(ItemStack stack, World world, Entity entity, int slot, boolean isHolding) {
        if(stack == null || !(stack.getItem() instanceof ItemElement)) return;
        int meta = stack.getItemDamage();
        ItemElement.EnumElementType element = ItemElement.EnumElementType.values()[meta % ItemElement.EnumElementType.values().length];

        if(!world.isRemote) {
            if((world.getTotalWorldTime() & 15) == 0) {
                EntityItemElement.doElementServerEffects(element, world, entity.posX, entity.posY, entity.posZ);
            }
        }
    }

    @Override
    public void getSubItems(Item item, CreativeTabs tabs, List list) {
        for (EnumElementType type : EnumElementType.values()) {
            list.add(new ItemStack(this, 1, type.ordinal()));
        }
    }

    @Override
    public EnumRarity getRarity(ItemStack p_77613_1_) {
        return EnumRarity.epic;
    }

    @Override
    public int getColorFromItemStack(ItemStack stack, int pass) {
        int dmg = stack.getItemDamage();
        if(dmg < EnumElementType.values().length) {
            return EnumElementType.values()[dmg].getRenderColor();
        }
        return 0xFFFFFF;
    }

    public static abstract class ElementRunnable {

        public abstract void affectEntity(EntityLivingBase livingBase);

    }

    public static enum EnumElementType {

        DARKNESS(0x000000, new ElementRunnable() {
            @Override
            public void affectEntity(EntityLivingBase livingBase) {
                livingBase.addPotionEffect(new PotionEffect(Potion.blindness.getId(), 100, 0, true));
            }
        });
        /*ORDER(0xFFFFFF, new ElementRunnable() {
            @Override
            public void affectEntity(EntityLivingBase livingBase) {
                livingBase.addPotionEffect(new PotionEffect(Potion.nightVision.getId(), 300, 0, true));
            }
        }),
        FIRE(0x970000, new ElementRunnable() {
            @Override
            public void affectEntity(EntityLivingBase livingBase) {
                livingBase.setFire(10);
            }
        });*/

        private final int renderColor;
        private final ElementRunnable runnable;

        private EnumElementType(int color, ElementRunnable runnable) {
            this.renderColor = color;
            this.runnable = runnable;
        }

        public ElementRunnable getRunnable() {
            return runnable;
        }

        public int getRenderColor() {
            return renderColor;
        }
    }

}
