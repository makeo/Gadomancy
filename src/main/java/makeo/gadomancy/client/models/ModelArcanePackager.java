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
 * Created by makeo @ 28.11.2015 19:04
 */
public class ModelArcanePackager extends ModelBase {
    ModelRenderer shape1;
    ModelRenderer shape2;
    ModelRenderer shape3;
    ModelRenderer shape4;
    ModelRenderer shape5;
    ModelRenderer shape6;
    ModelRenderer shape7;
    ModelRenderer shape8;
    ModelRenderer shape9;

    public ModelArcanePackager() {
        textureWidth = 64;
        textureHeight = 64;

        shape1 = new ModelRenderer(this, 0, 0);
        shape1.addBox(0F, 0F, 0F, 16, 3, 16);
        shape1.setRotationPoint(-8F, 21F, -8F);
        shape1.setTextureSize(64, 64);
        shape1.mirror = true;
        setRotation(shape1, 0F, 0F, 0F);
        shape2 = new ModelRenderer(this, 0, 20);
        shape2.addBox(0F, 0F, 0F, 16, 3, 16);
        shape2.setRotationPoint(-8F, 12F, -8F);
        shape2.setTextureSize(64, 64);
        shape2.mirror = true;
        setRotation(shape2, 0F, 0F, 0F);
        shape3 = new ModelRenderer(this, 0, 40);
        shape3.addBox(0F, 0F, 0F, 1, 5, 5);
        shape3.setRotationPoint(7F, 13.5F, -2.5F);
        shape3.setTextureSize(64, 64);
        shape3.mirror = true;
        setRotation(shape3, 0F, 0F, 0F);
        shape4 = new ModelRenderer(this, 13, 40);
        shape4.addBox(-2F, 0F, 0F, 3, 2, 2);
        shape4.setRotationPoint(7F, 15F, -1F);
        shape4.setTextureSize(64, 64);
        shape4.mirror = true;
        setRotation(shape4, 0F, 0F, 0.7853982F);
        shape5 = new ModelRenderer(this, 13, 45);
        shape5.addBox(0.3F, 0.12F, 0F, 1, 1, 2);
        shape5.setRotationPoint(6F, 16F, -1F);
        shape5.setTextureSize(64, 64);
        shape5.mirror = true;
        setRotation(shape5, 0F, 0F, 0F);
        shape6 = new ModelRenderer(this, 7, 40);
        shape6.addBox(0F, 0F, 0F, 1, 6, 16);
        shape6.setRotationPoint(-8F, 15F, -8F);
        shape6.setTextureSize(64, 64);
        shape6.mirror = true;
        setRotation(shape6, 0F, 0F, 0F);
        shape7 = new ModelRenderer(this, 24, 40);
        shape7.addBox(0F, 0F, 0F, 1, 6, 16);
        shape7.setRotationPoint(7F, 15F, -8F);
        shape7.setTextureSize(64, 64);
        shape7.mirror = true;
        setRotation(shape7, 0F, 0F, 0F);
        shape8 = new ModelRenderer(this, -9, 56);
        shape8.addBox(0F, 0F, 0F, 16, 6, 0);
        shape8.setRotationPoint(-8F, 15F, 8F);
        shape8.setTextureSize(64, 64);
        shape8.mirror = true;
        setRotation(shape8, 0F, 0F, 0F);
        shape9 = new ModelRenderer(this, 6, 55);
        shape9.addBox(0F, 0F, 0F, 16, 6, 1);
        shape9.setRotationPoint(-8F, 15F, -8F);
        shape9.setTextureSize(64, 64);
        shape9.mirror = true;
        setRotation(shape9, 0F, 0F, 0F);
    }

    public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5) {
        super.render(entity, f, f1, f2, f3, f4, f5);
        setRotationAngles(f, f1, f2, f3, f4, f5, entity);
        shape1.render(f5);
        shape2.render(f5);
        shape3.render(f5);
        shape4.render(f5);
        shape5.render(f5);
        shape9.render(f5);
        shape6.render(f5);
        shape7.render(f5);
        shape8.render(f5);
    }

    private void setRotation(ModelRenderer model, float x, float y, float z) {
        model.rotateAngleX = x;
        model.rotateAngleY = y;
        model.rotateAngleZ = z;
    }
}
