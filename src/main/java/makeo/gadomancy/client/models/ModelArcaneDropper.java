package makeo.gadomancy.client.models;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

/**
 * This class is part of the Gadomancy Mod
 * Gadomancy is Open Source and distributed under the
 * GNU LESSER GENERAL PUBLIC LICENSE
 * for more read the LICENSE file
 *
 * Created by makeo @ 30.09.2015 17:04
 * using Techne: http://www.minecraftforum.net/forums/mapping-and-modding/minecraft-tools/1261123
 */
public class ModelArcaneDropper extends ModelBase {
    //fields
    ModelRenderer shape1;
    ModelRenderer shape2;
    ModelRenderer shape3;
    ModelRenderer shape4;
    ModelRenderer shape5;
    ModelRenderer shape6;
    ModelRenderer shape7;
    ModelRenderer shape8;
    ModelRenderer shape9;
    ModelRenderer shape10;
    ModelRenderer shape11;

    public ModelArcaneDropper() {
        textureWidth = 64;
        textureHeight = 64;

        shape1 = new ModelRenderer(this, 44, 0);
        shape1.addBox(0F, 0F, 0F, 2, 14, 8);
        shape1.setRotationPoint(-1F, 10F, -4F);
        shape1.setTextureSize(64, 64);
        shape1.mirror = true;
        setRotation(shape1, 0F, 0F, 0F);
        shape2 = new ModelRenderer(this, 0, 0);
        shape2.addBox(0F, 0F, 0F, 16, 16, 4);
        shape2.setRotationPoint(-8F, 8F, 4F);
        shape2.setTextureSize(64, 64);
        shape2.mirror = true;
        setRotation(shape2, 0F, 0F, 0F);
        shape3 = new ModelRenderer(this, 0, 20);
        shape3.addBox(0F, 0F, 0F, 16, 16, 4);
        shape3.setRotationPoint(-8F, 8F, -8F);
        shape3.setTextureSize(64, 64);
        shape3.mirror = true;
        setRotation(shape3, 0F, 0F, 0F);
        shape4 = new ModelRenderer(this, 34, 22);
        shape4.addBox(0F, 0F, 0F, 7, 2, 8);
        shape4.setRotationPoint(1F, 10F, -4F);
        shape4.setTextureSize(64, 64);
        shape4.mirror = true;
        setRotation(shape4, 0F, 0F, 0F);
        shape5 = new ModelRenderer(this, 34, 32);
        shape5.addBox(0F, 0F, 0F, 7, 4, 8);
        shape5.setRotationPoint(1F, 20F, -4F);
        shape5.setTextureSize(64, 64);
        shape5.mirror = true;
        setRotation(shape5, 0F, 0F, 0F);
        shape6 = new ModelRenderer(this, 34, 52);
        shape6.addBox(0F, 0F, 0F, 7, 4, 8);
        shape6.setRotationPoint(-8F, 20F, -4F);
        shape6.setTextureSize(64, 64);
        shape6.mirror = true;
        setRotation(shape6, 0F, 0F, 0F);
        shape7 = new ModelRenderer(this, 0, 40);
        shape7.addBox(0F, 0F, 0F, 7, 2, 8);
        shape7.setRotationPoint(-8F, 10F, -4F);
        shape7.setTextureSize(64, 64);
        shape7.mirror = true;
        setRotation(shape7, 0F, 0F, 0F);
        shape8 = new ModelRenderer(this, 21, 50);
        shape8.addBox(0F, 0F, 0F, 5, 2, 8);
        shape8.setRotationPoint(3F, 8F, -4F);
        shape8.setTextureSize(64, 64);
        shape8.mirror = true;
        setRotation(shape8, 0F, 0F, 0F);
        shape9 = new ModelRenderer(this, 0, 50);
        shape9.addBox(0F, 0F, 0F, 5, 2, 8);
        shape9.setRotationPoint(-8F, 8F, -4F);
        shape9.setTextureSize(64, 64);
        shape9.mirror = true;
        setRotation(shape9, 0F, 0F, 0F);
        shape10 = new ModelRenderer(this, 0, 61);
        shape10.addBox(0F, 0F, 0F, 6, 2, 1);
        shape10.setRotationPoint(-3F, 8F, -4F);
        shape10.setTextureSize(64, 64);
        shape10.mirror = true;
        setRotation(shape10, 0F, 0F, 0F);
        shape11 = new ModelRenderer(this, 14, 61);
        shape11.addBox(0F, 0F, 0F, 6, 2, 1);
        shape11.setRotationPoint(-3F, 8F, 3F);
        shape11.setTextureSize(64, 64);
        shape11.mirror = true;
        setRotation(shape11, 0F, 0F, 0F);
    }

    public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5) {
        super.render(entity, f, f1, f2, f3, f4, f5);
        setRotationAngles(f, f1, f2, f3, f4, f5, entity);
        shape1.render(f5);
        shape2.render(f5);
        shape3.render(f5);
        shape4.render(f5);
        shape5.render(f5);
        shape6.render(f5);
        shape7.render(f5);
        shape8.render(f5);
        shape9.render(f5);
        shape10.render(f5);
        shape11.render(f5);
    }

    private void setRotation(ModelRenderer model, float x, float y, float z) {
        model.rotateAngleX = x;
        model.rotateAngleY = y;
        model.rotateAngleZ = z;
    }

    public void setRotationAngles(float f, float f1, float f2, float f3, float f4, float f5, Entity entity) {
        super.setRotationAngles(f, f1, f2, f3, f4, f5, entity);
    }
}
