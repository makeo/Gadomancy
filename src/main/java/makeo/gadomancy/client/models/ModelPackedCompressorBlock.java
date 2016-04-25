package makeo.gadomancy.client.models;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

/**
 * HellFirePvP@Admin
 * Date: 22.04.2016 / 22:59
 * on Gadomancy
 * ModelPackedCompressorBlock
 */
public class ModelPackedCompressorBlock extends ModelBase {

    ModelRenderer block;

    public ModelPackedCompressorBlock() {
        textureHeight = 16;
        textureWidth = 16;

        block = new ModelRenderer(this, 0, 0);
        block.addBox(0F, 0F, 0F, 16, 16, 16);
        block.setRotationPoint(-8F, 22F, -8F);
        //block.setTextureSize(16, 16);
        block.mirror = true;
    }

    @Override
    public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5) {
        super.render(entity, f, f1, f2, f3, f4, f5);
        setRotationAngles(f, f1, f2, f3, f4, f5, entity);
        block.render(f5);
    }

    @Override
    public void setRotationAngles(float f, float f1, float f2, float f3, float f4, float f5, Entity entity) {
        super.setRotationAngles(f, f1, f2, f3, f4, f5, entity);
    }

}