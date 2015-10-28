package makeo.gadomancy.client.models;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

/**
 * This class is part of the Gadomancy Mod
 * Gadomancy is Open Source and distributed under the
 * GNU LESSER GENERAL PUBLIC LICENSE
 * for more read the LICENSE file
 * <p/>
 * Created by makeo @ 27.10.2015 23:40
 */
public class ModelSmallCube extends ModelBase {
    //fields
    ModelRenderer shape1;

    public ModelSmallCube() {
        textureWidth = 8;
        textureHeight = 4;

        shape1 = new ModelRenderer(this, 0, 0);
        shape1.addBox(-1F, -3F, -1F, 2, 2, 2);
        shape1.setRotationPoint(0F, 0F, 0F);
        shape1.setTextureSize(8, 4);
        shape1.mirror = true;
        setRotation(shape1, 0F, 0F, 0F);
    }

    public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5) {
        super.render(entity, f, f1, f2, f3, f4, f5);
        setRotationAngles(f, f1, f2, f3, f4, f5, entity);
        shape1.render(f5);
    }

    private void setRotation(ModelRenderer model, float x, float y, float z) {
        model.rotateAngleX = x;
        model.rotateAngleY = y;
        model.rotateAngleZ = z;
    }
}
