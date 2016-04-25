package makeo.gadomancy.common.items;

import makeo.gadomancy.common.Gadomancy;
import makeo.gadomancy.common.registration.RegisteredItems;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IIcon;
import net.minecraft.util.StatCollector;
import org.lwjgl.opengl.GL11;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.common.blocks.BlockCustomOreItem;

import java.util.List;

/**
 * This class is part of the Gadomancy Mod
 * Gadomancy is Open Source and distributed under the
 * GNU LESSER GENERAL PUBLIC LICENSE
 * for more read the LICENSE file
 *
 * Created by HellFirePvP @ 16.11.2015 15:23
 */
public class ItemAuraCore extends Item {

    public ItemAuraCore() {
        setUnlocalizedName("ItemAuraCore");
        setMaxDamage(0);
        setMaxStackSize(1);
        setHasSubtypes(true);
        setCreativeTab(RegisteredItems.creativeTab);
    }

    @Override
    public EnumRarity getRarity(ItemStack p_77613_1_) {
        return RegisteredItems.raritySacred;
    }

    @Override
    public void addInformation(ItemStack stack, EntityPlayer player, List list, boolean flag) {
        AuraCoreType type = getCoreType(stack);
        if(type != null) {
            list.add(EnumChatFormatting.GRAY + type.getLocalizedName());
        }
    }

    @Override
    public void getSubItems(Item item, CreativeTabs tab, List list) {
        for (int i = 0; i < AuraCoreType.values().length; i++) {
            ItemStack stack = new ItemStack(item);
            setCoreType(stack, AuraCoreType.values()[i]);
            list.add(stack);
        }
    }

    private IIcon coreBlankIcon;
    private IIcon coreIcon;
    private IIcon borderIcon;

    @Override
    public void registerIcons(IIconRegister ir) {
        coreBlankIcon = ir.registerIcon(Gadomancy.MODID + ":core_core_blank");
        coreIcon = ir.registerIcon(Gadomancy.MODID + ":core_core");
        borderIcon = ir.registerIcon(Gadomancy.MODID + ":core_border");
    }

    @Override
    public IIcon getIcon(ItemStack stack, int pass) {
        //shhhhh don't tell anyone!
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

        if(getCoreType(stack) == AuraCoreType.BLANK && pass == 0) {
            return coreBlankIcon;
        }
        return pass == 0 ? coreIcon : borderIcon;
    }

    @Override
    public boolean requiresMultipleRenderPasses() {
        return true;
    }

    @Override
    public int getColorFromItemStack(ItemStack stack, int pass) {
        if(pass == 0) {
            AuraCoreType type = getCoreType(stack);
            if(type.isAspect()) {
                if(type.ordinal() < 7) {
                    return BlockCustomOreItem.colors[type.ordinal()];
                }
                return type.getAspect().getColor();
            }
        }
        return super.getColorFromItemStack(stack, pass);
    }

    public ItemStack setCoreType(ItemStack itemStack, AuraCoreType type) {
        itemStack.setItemDamage(type.ordinal());
        return itemStack;
    }

    public AuraCoreType getCoreType(ItemStack itemStack) {
        return AuraCoreType.values()[itemStack.getItemDamage()];
    }

    public boolean isBlank(ItemStack itemStack) {
        AuraCoreType type = getCoreType(itemStack);
        return type == AuraCoreType.BLANK || type == null;
    }

    public static enum AuraCoreType {
        BLANK("blank"),

        AIR(Aspect.AIR),
        FIRE(Aspect.FIRE),
        WATER(Aspect.WATER),
        EARTH(Aspect.EARTH, true),
        ORDER(Aspect.ORDER),
        ENTROPY(Aspect.ENTROPY, true);

        private final Aspect aspect;
        private final String unlocName;
        private final boolean unused;

        AuraCoreType(String unlocName) {
            this.unlocName = unlocName;
            this.aspect = null;
            this.unused = false;
        }

        AuraCoreType(Aspect aspect) {
            this(aspect, false);
        }

        AuraCoreType(Aspect aspect, boolean unused) {
            this.aspect = aspect;
            this.unlocName = null;
            this.unused = unused;
        }

        public boolean isUnused() {
            return unused;
        }

        public boolean isAspect() {
            return aspect != null;
        }

        public Aspect getAspect() {
            return aspect;
        }

        public String getLocalizedName() {
            String name = isAspect() ? aspect.getName() : StatCollector.translateToLocal("gadomancy.auracore." + unlocName);
            if(unused) {
                name += " " + StatCollector.translateToLocal("gadomancy.auracore.unused");
            }
            return name;
        }
    }
}
