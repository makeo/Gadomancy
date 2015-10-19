package makeo.gadomancy.common.entities.golems.types;

import makeo.gadomancy.api.golems.AdditionalGolemType;
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
 * Created by makeo @ 29.07.2015 00:15
 */
public class RemovedGolemType extends AdditionalGolemType {
    public RemovedGolemType() {
        super(0, 0, 0, false, 0, 0, 1000, 0);
    }

    @Override
    public String getUnlocalizedName() {
        return "";
    }

    private IIcon icon;
    @Override
    public void registerIcons(IIconRegister ir) {
        icon = ir.registerIcon("");
    }

    @Override
    public IIcon getIcon(ItemStack stack, int pass) {
        return icon;
    }

    @Override
    public ResourceLocation getEntityTexture() {
        return new ResourceLocation("");
    }

    @Override
    public ResourceLocation getInvSlotTexture() {
        return new ResourceLocation("");
    }
}
