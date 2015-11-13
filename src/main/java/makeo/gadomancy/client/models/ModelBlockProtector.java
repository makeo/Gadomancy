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
 * Created by makeo @ 11.11.2015 17:15
 */
public class ModelBlockProtector extends ModelBase {
    //fields
    ModelRenderer Shape1;
    ModelRenderer Shape2;
    ModelRenderer Shape3;
    ModelRenderer Shape4;
    ModelRenderer Shape5;
    ModelRenderer Shape6;
    ModelRenderer Shape7;
    ModelRenderer Shape8;
    ModelRenderer Shape9;
    ModelRenderer Shape10;
    ModelRenderer Shape11;
    ModelRenderer Shape12;
    ModelRenderer Shape13;

    public ModelBlockProtector() {
        textureWidth = 64;
        textureHeight = 64;

        Shape1 = new ModelRenderer(this, 0, 0);
        Shape1.addBox(0F, 0F, 0F, 16, 3, 16);
        Shape1.setRotationPoint(-8F, 21F, -8F);
        Shape1.setTextureSize(64, 64);
        Shape1.mirror = true;
        setRotation(Shape1, 0F, 0F, 0F);
        Shape2 = new ModelRenderer(this, 0, 20);
        Shape2.addBox(0F, 0F, 0F, 4, 1, 1);
        Shape2.setRotationPoint(-2F, 20F, -8F);
        Shape2.setTextureSize(64, 64);
        Shape2.mirror = true;
        setRotation(Shape2, 0F, 0F, 0F);
        Shape3 = new ModelRenderer(this, 11, 20);
        Shape3.addBox(0F, 0F, 0F, 4, 1, 3);
        Shape3.setRotationPoint(-2F, 20F, -7F);
        Shape3.setTextureSize(64, 64);
        Shape3.mirror = true;
        setRotation(Shape3, -0.3490659F, 0F, 0F);
        Shape4 = new ModelRenderer(this, 27, 20);
        Shape4.addBox(0F, 0F, 0F, 4, 1, 1);
        Shape4.setRotationPoint(-2F, 20F, 7F);
        Shape4.setTextureSize(64, 64);
        Shape4.mirror = true;
        setRotation(Shape4, 0F, 0F, 0F);
        Shape5 = new ModelRenderer(this, 38, 20);
        Shape5.addBox(0F, 0F, -3F, 4, 1, 3);
        Shape5.setRotationPoint(-2F, 20F, 7F);
        Shape5.setTextureSize(64, 64);
        Shape5.mirror = true;
        setRotation(Shape5, 0.3490659F, 0F, 0F);
        Shape6 = new ModelRenderer(this, 0, 25);
        Shape6.addBox(0F, 0F, 0F, 1, 1, 4);
        Shape6.setRotationPoint(-8F, 20F, -2F);
        Shape6.setTextureSize(64, 64);
        Shape6.mirror = true;
        setRotation(Shape6, 0F, 0F, 0F);
        Shape7 = new ModelRenderer(this, 11, 25);
        Shape7.addBox(0F, 0F, 0F, 3, 1, 4);
        Shape7.setRotationPoint(-7F, 20F, -2F);
        Shape7.setTextureSize(64, 64);
        Shape7.mirror = true;
        setRotation(Shape7, 0F, 0F, 0.3490659F);
        Shape8 = new ModelRenderer(this, 38, 25);
        Shape8.addBox(-3F, 0F, 0F, 3, 1, 4);
        Shape8.setRotationPoint(7F, 20F, -2F);
        Shape8.setTextureSize(64, 64);
        Shape8.mirror = true;
        setRotation(Shape8, 0F, 0F, -0.3490659F);
        Shape9 = new ModelRenderer(this, 27, 25);
        Shape9.addBox(0F, 0F, 0F, 1, 1, 4);
        Shape9.setRotationPoint(7F, 20F, -2F);
        Shape9.setTextureSize(64, 64);
        Shape9.mirror = true;
        setRotation(Shape9, 0F, 0F, 0F);
        Shape10 = new ModelRenderer(this, 0, 31);
        Shape10.addBox(0F, -7F, 0F, 2, 8, 2);
        Shape10.setRotationPoint(3F, 22F, 3F);
        Shape10.setTextureSize(64, 64);
        Shape10.mirror = true;
        setRotation(Shape10, 0.3490659F, 0F, -0.3490659F);
        Shape11 = new ModelRenderer(this, 10, 31);
        Shape11.addBox(-2F, -7F, -2F, 2, 8, 2);
        Shape11.setRotationPoint(-3F, 22F, -3F);
        Shape11.setTextureSize(64, 64);
        Shape11.mirror = true;
        setRotation(Shape11, -0.3490659F, 0F, 0.3490659F);
        Shape12 = new ModelRenderer(this, 20, 31);
        Shape12.addBox(0F, -7F, -2F, 2, 8, 2);
        Shape12.setRotationPoint(3F, 22F, -3F);
        Shape12.setTextureSize(64, 64);
        Shape12.mirror = true;
        setRotation(Shape12, -0.3490659F, 0F, -0.3490659F);
        Shape13 = new ModelRenderer(this, 30, 31);
        Shape13.addBox(-2F, -7F, 0F, 2, 8, 2);
        Shape13.setRotationPoint(-3F, 22F, 3F);
        Shape13.setTextureSize(64, 64);
        Shape13.mirror = true;
        setRotation(Shape13, 0.3490659F, 0F, 0.3490659F);
    }

    public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5) {
        super.render(entity, f, f1, f2, f3, f4, f5);
        setRotationAngles(f, f1, f2, f3, f4, f5, entity);
        Shape1.render(f5);
        Shape2.render(f5);
        Shape3.render(f5);
        Shape4.render(f5);
        Shape5.render(f5);
        Shape6.render(f5);
        Shape7.render(f5);
        Shape8.render(f5);
        Shape9.render(f5);
        Shape10.render(f5);
        Shape11.render(f5);
        Shape12.render(f5);
        Shape13.render(f5);
    }

    private void setRotation(ModelRenderer model, float x, float y, float z) {
        model.rotateAngleX = x;
        model.rotateAngleY = y;
        model.rotateAngleZ = z;
    }
}
