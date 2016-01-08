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
 * <p/>
 * Created by makeo @ 21.12.2015 19:29
 */
public class ObsidianGolemType extends AdditionalGolemType {
    public ObsidianGolemType() {
        super(50, 18, 0.27F, true, 2, 32, 100, 6);
    }

    @Override
    public String getUnlocalizedName() {
        return "item.ItemGolemPlacer.obsidian";
    }

    private IIcon icon;
    @Override
    public void registerIcons(IIconRegister ir) {
        icon = ir.registerIcon(Gadomancy.MODID + ":golem_obsidian");
    }

    @Override
    public IIcon getIcon(ItemStack stack, int pass) {
        return icon;
    }

    @Override
    public ResourceLocation getEntityTexture() {
        return new SimpleResourceLocation("models/golem_obsidian.png");
    }

    @Override
    public ResourceLocation getInvSlotTexture() {
        return new SimpleResourceLocation("gui/obsidian_slot.png");
    }
}
