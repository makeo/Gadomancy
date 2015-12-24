package makeo.gadomancy.common.entities.golems.types;

import makeo.gadomancy.api.golems.AdditionalGolemType;
import makeo.gadomancy.common.Gadomancy;
import makeo.gadomancy.common.utils.SimpleResourceLocation;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.util.ResourceLocation;

/**
 * This class is part of the Gadomancy Mod
 * Gadomancy is Open Source and distributed under the
 * GNU LESSER GENERAL PUBLIC LICENSE
 * for more read the LICENSE file
 *
 * Created by makeo @ 28.07.2015 02:39
 */
public class SilverwoodGolemType extends AdditionalGolemType {
    public SilverwoodGolemType() {
        super(20, 9, 0.38f, false, 3, 8, 75, 1);
    }

    @Override
    public String getUnlocalizedName() {
        return "item.ItemGolemPlacer.silverwood";
    }

    private IIcon icon;
    @Override
    public void registerIcons(IIconRegister ir) {
        icon = ir.registerIcon(Gadomancy.MODID + ":golem_silverwood");
    }

    @Override
    public IIcon getIcon(ItemStack stack, int pass) {
        return icon;
    }

    @Override
    public ResourceLocation getEntityTexture() {
        return new SimpleResourceLocation("models/golem_silverwood.png");
    }

    @Override
    public ResourceLocation getInvSlotTexture() {
        return new SimpleResourceLocation("gui/silverwood_slot.png");
    }
}
