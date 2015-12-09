package makeo.gadomancy.client.models;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

public class ModelAuraPylonTop extends ModelBase {
    //fields
    ModelRenderer outerpylon;
    ModelRenderer lowerconduit;
    ModelRenderer inlower1;
    ModelRenderer inlower2;
    ModelRenderer outlowersmooth2;
    ModelRenderer outlowersmooth1;
    ModelRenderer inlowersmooth1;
    ModelRenderer inlowersmooth2;
    ModelRenderer outerpylonmiddle1;
    ModelRenderer outerpylonmiddle2;
    ModelRenderer outerpylonupper;

    public ModelAuraPylonTop()
    {
        textureWidth = 32;
        textureHeight = 32;

        outerpylon = new ModelRenderer(this, 0, 0);
        outerpylon.addBox(0F, 0F, 0F, 2, 8, 2);
        outerpylon.setRotationPoint(5F, 16F, 5F);
        outerpylon.setTextureSize(64, 32);
        outerpylon.mirror = true;
        setRotation(outerpylon, 0F, 0F, 0F);
        lowerconduit = new ModelRenderer(this, 9, 0);
        lowerconduit.addBox(0F, 0F, 0.2F, 6, 1, 1);
        lowerconduit.setRotationPoint(6F, 20F, 7F);
        lowerconduit.setTextureSize(64, 32);
        lowerconduit.mirror = true;
        setRotation(lowerconduit, 0F, 2.356194F, 0F);
        inlower1 = new ModelRenderer(this, 9, 3);
        inlower1.addBox(0F, 0F, 0F, 1, 1, 3);
        inlower1.setRotationPoint(3F, 20F, 3F);
        inlower1.setTextureSize(64, 32);
        inlower1.mirror = true;
        setRotation(inlower1, 0F, 3.141593F, 0F);
        inlower2 = new ModelRenderer(this, 9, 11);
        inlower2.addBox(0F, 0F, 0F, 3, 1, 1);
        inlower2.setRotationPoint(3F, 20F, 3F);
        inlower2.setTextureSize(64, 32);
        inlower2.mirror = true;
        setRotation(inlower2, 0F, 3.141593F, 0F);
        outlowersmooth2 = new ModelRenderer(this, 9, 8);
        outlowersmooth2.addBox(0F, 0F, 0.1F, 2, 1, 1);
        outlowersmooth2.setRotationPoint(5F, 20F, 7F);
        outlowersmooth2.setTextureSize(64, 32);
        outlowersmooth2.mirror = true;
        setRotation(outlowersmooth2, 0F, 1.919862F, 0F);
        outlowersmooth1 = new ModelRenderer(this, 9, 8);
        outlowersmooth1.addBox(-0.5F, 0F, 0.2F, 2, 1, 1);
        outlowersmooth1.setRotationPoint(6F, 20F, 6F);
        outlowersmooth1.setTextureSize(64, 32);
        outlowersmooth1.mirror = true;
        setRotation(outlowersmooth1, 0F, 2.792527F, 0F);
        inlowersmooth1 = new ModelRenderer(this, 9, 14);
        inlowersmooth1.addBox(0.25F, 0F, -0.5F, 1, 1, 2);
        inlowersmooth1.setRotationPoint(2F, 20F, 2F);
        inlowersmooth1.setTextureSize(64, 32);
        inlowersmooth1.mirror = true;
        setRotation(inlowersmooth1, 0F, 0.3490659F, 0F);
        inlowersmooth2 = new ModelRenderer(this, 9, 14);
        inlowersmooth2.addBox(0.15F, 0F, 0.3F, 1, 1, 2);
        inlowersmooth2.setRotationPoint(1F, 20F, 3F);
        inlowersmooth2.setTextureSize(64, 32);
        inlowersmooth2.mirror = true;
        setRotation(inlowersmooth2, 0F, 1.22173F, 0F);
        outerpylonmiddle1 = new ModelRenderer(this, 0, 11);
        outerpylonmiddle1.addBox(0F, 0F, 0F, 2, 4, 1);
        outerpylonmiddle1.setRotationPoint(5F, 12F, 5F);
        outerpylonmiddle1.setTextureSize(64, 32);
        outerpylonmiddle1.mirror = true;
        setRotation(outerpylonmiddle1, 0F, 0F, 0F);
        outerpylonmiddle2 = new ModelRenderer(this, 0, 17);
        outerpylonmiddle2.addBox(0F, 0F, 0F, 1, 4, 1);
        outerpylonmiddle2.setRotationPoint(5F, 12F, 6F);
        outerpylonmiddle2.setTextureSize(64, 32);
        outerpylonmiddle2.mirror = true;
        setRotation(outerpylonmiddle2, 0F, 0F, 0F);
        outerpylonupper = new ModelRenderer(this, 5, 17);
        outerpylonupper.addBox(0F, 0F, 0F, 1, 4, 1);
        outerpylonupper.setRotationPoint(5F, 8F, 5F);
        outerpylonupper.setTextureSize(64, 32);
        outerpylonupper.mirror = true;
        setRotation(outerpylonupper, 0F, 0F, 0F);
    }

    public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5)
    {
        super.render(entity, f, f1, f2, f3, f4, f5);
        setRotationAngles(f, f1, f2, f3, f4, f5, entity);
        outerpylon.render(f5);
        lowerconduit.render(f5);
        inlower1.render(f5);
        inlower2.render(f5);
        outlowersmooth2.render(f5);
        outlowersmooth1.render(f5);
        inlowersmooth1.render(f5);
        inlowersmooth2.render(f5);
        outerpylonmiddle1.render(f5);
        outerpylonmiddle2.render(f5);
        outerpylonupper.render(f5);
    }

    private void setRotation(ModelRenderer model, float x, float y, float z)
    {
        model.rotateAngleX = x;
        model.rotateAngleY = y;
        model.rotateAngleZ = z;
    }

    public void setRotationAngles(float f, float f1, float f2, float f3, float f4, float f5, Entity entity)
    {
        super.setRotationAngles(f, f1, f2, f3, f4, f5, entity);
    }
}
