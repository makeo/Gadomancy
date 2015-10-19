package makeo.gadomancy.client.renderers.entity;

import makeo.gadomancy.api.GadomancyApi;
import makeo.gadomancy.api.golems.AdditionalGolemType;
import makeo.gadomancy.api.golems.cores.AdditionalGolemCore;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import thaumcraft.client.renderers.entity.RenderGolemBase;
import thaumcraft.client.renderers.models.entities.ModelGolem;
import thaumcraft.common.entities.golems.EntityGolemBase;

/**
 * This class is part of the Gadomancy Mod
 * Gadomancy is Open Source and distributed under the
 * GNU LESSER GENERAL PUBLIC LICENSE
 * for more read the LICENSE file
 *
 * Created by makeo @ 12.03.2015 15:57
 */
public class RenderAdditionalGolemBase extends RenderGolemBase {
    private ItemStack toolItem = null;

    public RenderAdditionalGolemBase() {
        super(new ModelGolem(false));
    }

    protected int shouldRenderPass(EntityLivingBase entity, int pass, float par3) {
        if (pass != 0) {
            return super.shouldRenderPass(entity, pass, par3);
        }

        EntityGolemBase golem = (EntityGolemBase) entity;

        AdditionalGolemCore core = GadomancyApi.getAdditionalGolemCore(golem);
        if (core != null) {
            golem.getDataWatcher().getWatchedObject(21).setObject((byte) -1);
        }

        int ret = super.shouldRenderPass(entity, pass, par3);

        //fix for render bug in tc
        if (RenderGolemHelper.requiresRenderFix((EntityGolemBase) entity)) {
            RenderGolemHelper.renderCarriedItemsFix(golem);

            if(toolItem != null) {
                RenderGolemHelper.renderToolItem(golem, toolItem, mainModel, renderManager);
            }
        }

        if (core != null) {
            golem.getDataWatcher().getWatchedObject(21).setObject((byte) 1);
            RenderGolemHelper.renderCore(golem, core);
        }

        return ret;
    }

    @Override
    protected void renderCarriedItems(EntityGolemBase golem, float par2) {
        if (!RenderGolemHelper.requiresRenderFix(golem)) {
            if(toolItem != null) {
                RenderGolemHelper.renderToolItem(golem, toolItem, mainModel, renderManager);
            }
            super.renderCarriedItems(golem, par2);
        }
    }

    @Override
    public void render(EntityGolemBase golem, double par2, double par4, double par6, float par8, float par9) {
        AdditionalGolemCore core = GadomancyApi.getAdditionalGolemCore(golem);
        ItemStack carriedItem = null;
        if(core != null) {
            toolItem = core.getToolItem(golem);

            carriedItem = golem.getCarriedForDisplay();
            golem.getDataWatcher().getWatchedObject(16).setObject(null);
        }

        super.render(golem, par2, par4, par6, par8, par9);

        if(carriedItem != null) {
            golem.getDataWatcher().getWatchedObject(16).setObject(carriedItem);
            toolItem = null;
        }
    }

    @Override
    protected ResourceLocation getEntityTexture(Entity entity) {
        AdditionalGolemType type = GadomancyApi.getAdditionalGolemType(((EntityGolemBase) entity).getGolemType());
        if (type != null) {
            return type.getEntityTexture();
        }
        return super.getEntityTexture(entity);
    }
}
