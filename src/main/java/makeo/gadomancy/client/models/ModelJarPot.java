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
 * Created by makeo @ 23.10.2015 23:13
 */
public class ModelJarPot extends ModelBase {
    //fields
    ModelRenderer shape1;
    ModelRenderer shape2;
    ModelRenderer shape3;
    ModelRenderer shape4;
    ModelRenderer shape5;

    public ModelJarPot() {
        textureWidth = 32;
        textureHeight = 64;

        shape1 = new ModelRenderer(this, 0, 0);
        shape1.addBox(0F, 0F, 0F, 10, 0, 10);
        shape1.setRotationPoint(-5F, 24F, -5F);
        shape1.setTextureSize(32, 64);
        shape1.mirror = true;
        setRotation(shape1, 0F, 0F, 0F);
        shape2 = new ModelRenderer(this, 0, 28);
        shape2.addBox(0F, 0F, 0F, 1, 6, 12);
        shape2.setRotationPoint(-6F, 18F, -6F);
        shape2.setTextureSize(32, 64);
        shape2.mirror = true;
        setRotation(shape2, 0F, 0F, 0F);
        shape3 = new ModelRenderer(this, 0, 10);
        shape3.addBox(0F, 0F, 0F, 1, 6, 12);
        shape3.setRotationPoint(5F, 18F, -6F);
        shape3.setTextureSize(32, 64);
        shape3.mirror = true;
        setRotation(shape3, 0F, 0F, 0F);
        shape4 = new ModelRenderer(this, 0, 53);
        shape4.addBox(0F, 0F, 0F, 10, 6, 1);
        shape4.setRotationPoint(-5F, 18F, 5F);
        shape4.setTextureSize(32, 64);
        shape4.mirror = true;
        setRotation(shape4, 0F, 0F, 0F);
        shape5 = new ModelRenderer(this, 0, 46);
        shape5.addBox(0F, 0F, 0F, 10, 6, 1);
        shape5.setRotationPoint(-5F, 18F, -6F);
        shape5.setTextureSize(32, 64);
        shape5.mirror = true;
        setRotation(shape5, 0F, 0F, 0F);
    }

    public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5) {
        super.render(entity, f, f1, f2, f3, f4, f5);
        setRotationAngles(f, f1, f2, f3, f4, f5, entity);
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
