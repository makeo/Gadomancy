package makeo.gadomancy.common.potions;

import makeo.gadomancy.common.data.DataAchromatic;
import makeo.gadomancy.common.data.SyncDataHolder;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.attributes.BaseAttributeMap;
import thaumcraft.api.aspects.Aspect;

/**
 * This class is part of the Gadomancy Mod
 * Gadomancy is Open Source and distributed under the
 * GNU LESSER GENERAL PUBLIC LICENSE
 * for more read the LICENSE file
 * <p/>
 * Created by makeo @ 11.12.2015 17:32
 */
public class PotionAchromatic extends PotionCustomTexture {
    public PotionAchromatic(int id) {
        super(id, false, Aspect.CRYSTAL.getColor(), Aspect.CRYSTAL.getImage());
        setPotionName("potion.achromatic");
    }

    @Override
    public void applyAttributesModifiersToEntity(EntityLivingBase entity, BaseAttributeMap p_111185_2_, int p_111185_3_) {
        super.applyAttributesModifiersToEntity(entity, p_111185_2_, p_111185_3_);

        ((DataAchromatic) SyncDataHolder.getDataServer("AchromaticData")).handleApplication(entity);
    }

    @Override
    public void removeAttributesModifiersFromEntity(EntityLivingBase entity, BaseAttributeMap p_111187_2_, int p_111187_3_) {
        super.removeAttributesModifiersFromEntity(entity, p_111187_2_, p_111187_3_);

        ((DataAchromatic) SyncDataHolder.getDataServer("AchromaticData")).handleRemoval(entity);
    }
}
