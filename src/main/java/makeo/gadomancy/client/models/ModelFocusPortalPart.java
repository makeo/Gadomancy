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
 * Created by makeo @ 09.11.2015 23:08
 */
public class ModelFocusPortalPart extends ModelBase {
    //fields
    ModelRenderer shape1;
    ModelRenderer shape2;
    ModelRenderer shape3;
    ModelRenderer shape4;
    ModelRenderer shape5;

    public ModelFocusPortalPart() {
        textureWidth = 32;
        textureHeight = 32;

        shape1 = new ModelRenderer(this, 0, 3);
        shape1.addBox(-1F, -2F, 0.41F, 2, 5, 1);
        shape1.setRotationPoint(0F, 20F, 3.533333F);
        shape1.setTextureSize(64, 32);
        shape1.mirror = true;
        setRotation(shape1, 0F, 0F, 0F);
        shape2 = new ModelRenderer(this, 0, 0);
        shape2.addBox(-1F, -1F, 0F, 2, 2, 1);
        shape2.setRotationPoint(0F, 23F, 3.5F);
        shape2.setTextureSize(64, 32);
        shape2.mirror = true;
        setRotation(shape2, -0.7853982F, 0F, 0F);
        shape3 = new ModelRenderer(this, 0, 12);
        shape3.addBox(0F, 0F, 0F, 2, 2, 1);
        shape3.setRotationPoint(-1F, 21F, 2.5F);
        shape3.setTextureSize(64, 32);
        shape3.mirror = true;
        setRotation(shape3, 0.7853982F, 0F, 0F);
        shape4 = new ModelRenderer(this, 0, 9);
        shape4.addBox(0F, 0F, 0F, 2, 2, 1);
        shape4.setRotationPoint(-1F, 19F, 2.5F);
        shape4.setTextureSize(64, 32);
        shape4.mirror = true;
        setRotation(shape4, 0.7853982F, 0F, 0F);
        shape5 = new ModelRenderer(this, 6, 0);
        shape5.addBox(0F, -1F, 0.03F, 2, 4, 1);
        shape5.setRotationPoint(-1F, 18F, 3.5F);
        shape5.setTextureSize(64, 32);
        shape5.mirror = true;
        setRotation(shape5, 0.7853982F, 0F, 0F);
    }

    public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5) {
        super.render(entity, f, f1, f2, f3, f4, f5);
        shape1.render(f5);
        shape2.render(f5);
        shape3.render(f5);
        shape4.render(f5);
        shape5.render(f5);
    }

    private void setRotation(ModelRenderer model, float x, float y, float z) {
        model.rotateAngleX = x;
        model.rotateAngleY = y;
        model.rotateAngleZ = z;
    }
}
