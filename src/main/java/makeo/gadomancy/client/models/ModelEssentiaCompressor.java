package makeo.gadomancy.client.models;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

/**
 * HellFirePvP@Admin
 * Date: 22.04.2016 / 21:36
 * on Gadomancy
 * ModelEssentiaCompressor
 */
public class ModelEssentiaCompressor extends ModelBase {

    //Ugh techne...
    ModelRenderer Plate1;
    ModelRenderer Plate2;
    ModelRenderer Plate3;
    ModelRenderer EdgePillar1;
    ModelRenderer EdgePillar2;
    ModelRenderer EdgePillar3;
    ModelRenderer EdgePillar4;
    ModelRenderer InnerLL1;
    ModelRenderer InnerLL2;
    ModelRenderer InnerLSh1;
    ModelRenderer InnerLSH2;
    ModelRenderer InnerUp1;
    ModelRenderer InnerUp2;
    ModelRenderer InnerUp3;
    ModelRenderer InnerUp4;
    ModelRenderer InnerEdgePiece1;
    ModelRenderer InnerEdgePiece2;
    ModelRenderer InnerEdgePiece3;
    ModelRenderer InnerEdgePiece4;
    ModelRenderer InnerPeak1;
    ModelRenderer InnerPeak2;
    ModelRenderer InnerPeak3;
    ModelRenderer InnerPeak4;
    ModelRenderer InnerPillar1;
    ModelRenderer InnerPillar2;
    ModelRenderer InnerPillar3;
    ModelRenderer InnerPillar4;

    public ModelEssentiaCompressor() {
        textureWidth = 128;
        textureHeight = 32;

        Plate1 = new ModelRenderer(this, 64, 0);
        Plate1.addBox(0F, 0F, 0F, 16, 2, 16);
        Plate1.setRotationPoint(-8F, 22F, -8F);
        Plate1.setTextureSize(128, 32);
        Plate1.mirror = true;
        setRotation(Plate1, 0F, 0F, 0F);
        Plate2 = new ModelRenderer(this, 4, 0);
        Plate2.addBox(0F, 0F, 0F, 14, 2, 14);
        Plate2.setRotationPoint(-7F, 20F, -7F);
        Plate2.setTextureSize(128, 32);
        Plate2.mirror = true;
        setRotation(Plate2, 0F, 0F, 0F);
        Plate3 = new ModelRenderer(this, 4, 16);
        Plate3.addBox(0F, 0F, 0F, 12, 3, 12);
        Plate3.setRotationPoint(-6F, 17F, -6F);
        Plate3.setTextureSize(128, 32);
        Plate3.mirror = true;
        setRotation(Plate3, 0F, 0F, 0F);
        EdgePillar1 = new ModelRenderer(this, 0, 0);
        EdgePillar1.addBox(0F, 0F, 0F, 1, 22, 1);
        EdgePillar1.setRotationPoint(-8F, 0F, -8F);
        EdgePillar1.setTextureSize(128, 32);
        EdgePillar1.mirror = true;
        setRotation(EdgePillar1, 0F, 0F, 0F);
        EdgePillar2 = new ModelRenderer(this, 0, 0);
        EdgePillar2.addBox(0F, 0F, 0F, 1, 22, 1);
        EdgePillar2.setRotationPoint(7F, 0F, -8F);
        EdgePillar2.setTextureSize(128, 32);
        EdgePillar2.mirror = true;
        setRotation(EdgePillar2, 0F, 0F, 0F);
        EdgePillar3 = new ModelRenderer(this, 0, 0);
        EdgePillar3.addBox(0F, 0F, 0F, 1, 22, 1);
        EdgePillar3.setRotationPoint(7F, 0F, 7F);
        EdgePillar3.setTextureSize(128, 32);
        EdgePillar3.mirror = true;
        setRotation(EdgePillar3, 0F, 0F, 0F);
        EdgePillar4 = new ModelRenderer(this, 0, 0);
        EdgePillar4.addBox(0F, 0F, 0F, 1, 22, 1);
        EdgePillar4.setRotationPoint(-8F, 0F, 7F);
        EdgePillar4.setTextureSize(128, 32);
        EdgePillar4.mirror = true;
        setRotation(EdgePillar4, 0F, 0F, 0F);
        InnerLL1 = new ModelRenderer(this, 100, 18);
        InnerLL1.addBox(0F, 0F, 0F, 2, 3, 10);
        InnerLL1.setRotationPoint(-5F, 14F, -5F);
        InnerLL1.setTextureSize(128, 32);
        InnerLL1.mirror = true;
        setRotation(InnerLL1, 0F, 0F, 0F);
        InnerLL2 = new ModelRenderer(this, 100, 18);
        InnerLL2.addBox(0F, 0F, 0F, 2, 3, 10);
        InnerLL2.setRotationPoint(3F, 14F, -5F);
        InnerLL2.setTextureSize(128, 32);
        InnerLL2.mirror = true;
        setRotation(InnerLL2, 0F, 0F, 0F);
        InnerLSh1 = new ModelRenderer(this, 84, 18);
        InnerLSh1.addBox(0F, 0F, 0F, 6, 3, 2);
        InnerLSh1.setRotationPoint(-3F, 14F, -5F);
        InnerLSh1.setTextureSize(128, 32);
        InnerLSh1.mirror = true;
        setRotation(InnerLSh1, 0F, 0F, 0F);
        InnerLSH2 = new ModelRenderer(this, 84, 18);
        InnerLSH2.addBox(0F, 0F, 0F, 6, 3, 2);
        InnerLSH2.setRotationPoint(-3F, 14F, 3F);
        InnerLSH2.setTextureSize(128, 32);
        InnerLSH2.mirror = true;
        setRotation(InnerLSH2, 0F, 0F, 0F);
        InnerUp1 = new ModelRenderer(this, 64, 18);
        InnerUp1.addBox(0F, 0F, 0F, 1, 2, 6);
        InnerUp1.setRotationPoint(-5F, 12F, -3F);
        InnerUp1.setTextureSize(128, 32);
        InnerUp1.mirror = true;
        setRotation(InnerUp1, 0F, 0F, 0F);
        InnerUp2 = new ModelRenderer(this, 64, 18);
        InnerUp2.addBox(0F, 0F, 0F, 1, 2, 6);
        InnerUp2.setRotationPoint(4F, 12F, -3F);
        InnerUp2.setTextureSize(128, 32);
        InnerUp2.mirror = true;
        setRotation(InnerUp2, 0F, 0F, 0F);
        InnerUp3 = new ModelRenderer(this, 64, 18);
        InnerUp3.addBox(0F, 0F, 0F, 6, 2, 1);
        InnerUp3.setRotationPoint(-3F, 12F, 4F);
        InnerUp3.setTextureSize(128, 32);
        InnerUp3.mirror = true;
        setRotation(InnerUp3, 0F, 0F, 0F);
        InnerUp4 = new ModelRenderer(this, 64, 18);
        InnerUp4.addBox(0F, 0F, 0F, 6, 2, 1);
        InnerUp4.setRotationPoint(-3F, 12F, -5F);
        InnerUp4.setTextureSize(128, 32);
        InnerUp4.mirror = true;
        setRotation(InnerUp4, 0F, 0F, 0F);
        InnerEdgePiece1 = new ModelRenderer(this, 52, 18);
        InnerEdgePiece1.addBox(0F, 0F, 0F, 2, 5, 2);
        InnerEdgePiece1.setRotationPoint(3F, 9F, -5F);
        InnerEdgePiece1.setTextureSize(128, 32);
        InnerEdgePiece1.mirror = true;
        setRotation(InnerEdgePiece1, 0F, 0F, 0F);
        InnerEdgePiece2 = new ModelRenderer(this, 52, 18);
        InnerEdgePiece2.addBox(0F, 0F, 0F, 2, 5, 2);
        InnerEdgePiece2.setRotationPoint(3F, 9F, 3F);
        InnerEdgePiece2.setTextureSize(128, 32);
        InnerEdgePiece2.mirror = true;
        setRotation(InnerEdgePiece2, 0F, 0F, 0F);
        InnerEdgePiece3 = new ModelRenderer(this, 52, 18);
        InnerEdgePiece3.addBox(0F, 0F, 0F, 2, 5, 2);
        InnerEdgePiece3.setRotationPoint(-5F, 9F, 3F);
        InnerEdgePiece3.setTextureSize(128, 32);
        InnerEdgePiece3.mirror = true;
        setRotation(InnerEdgePiece3, 0F, 0F, 0F);
        InnerEdgePiece4 = new ModelRenderer(this, 52, 18);
        InnerEdgePiece4.addBox(0F, 0F, 0F, 2, 5, 2);
        InnerEdgePiece4.setRotationPoint(-5F, 9F, -5F);
        InnerEdgePiece4.setTextureSize(128, 32);
        InnerEdgePiece4.mirror = true;
        setRotation(InnerEdgePiece4, 0F, 0F, 0F);
        InnerPeak1 = new ModelRenderer(this, 124, 18);
        InnerPeak1.addBox(0F, 0F, 0F, 1, 5, 1);
        InnerPeak1.setRotationPoint(-5F, 4F, -5F);
        InnerPeak1.setTextureSize(128, 32);
        InnerPeak1.mirror = true;
        setRotation(InnerPeak1, 0F, 0F, 0F);
        InnerPeak2 = new ModelRenderer(this, 124, 18);
        InnerPeak2.addBox(0F, 0F, 0F, 1, 5, 1);
        InnerPeak2.setRotationPoint(-5F, 4F, 4F);
        InnerPeak2.setTextureSize(128, 32);
        InnerPeak2.mirror = true;
        setRotation(InnerPeak2, 0F, 0F, 0F);
        InnerPeak3 = new ModelRenderer(this, 124, 18);
        InnerPeak3.addBox(0F, 0F, 0F, 1, 5, 1);
        InnerPeak3.setRotationPoint(4F, 4F, -5F);
        InnerPeak3.setTextureSize(128, 32);
        InnerPeak3.mirror = true;
        setRotation(InnerPeak3, 0F, 0F, 0F);
        InnerPeak4 = new ModelRenderer(this, 124, 18);
        InnerPeak4.addBox(0F, 0F, 0F, 1, 5, 1);
        InnerPeak4.setRotationPoint(4F, 4F, 4F);
        InnerPeak4.setTextureSize(128, 32);
        InnerPeak4.mirror = true;
        setRotation(InnerPeak4, 0F, 0F, 0F);
        InnerPillar1 = new ModelRenderer(this, 0, 0);
        InnerPillar1.addBox(0F, 0F, 0F, 1, 17, 1);
        InnerPillar1.setRotationPoint(5F, 0F, 5F);
        InnerPillar1.setTextureSize(128, 32);
        InnerPillar1.mirror = true;
        setRotation(InnerPillar1, 0F, 0F, 0F);
        InnerPillar2 = new ModelRenderer(this, 0, 0);
        InnerPillar2.addBox(0F, 0F, 0F, 1, 17, 1);
        InnerPillar2.setRotationPoint(5F, 0F, -6F);
        InnerPillar2.setTextureSize(128, 32);
        InnerPillar2.mirror = true;
        setRotation(InnerPillar2, 0F, 0F, 0F);
        InnerPillar3 = new ModelRenderer(this, 0, 0);
        InnerPillar3.addBox(0F, 0F, 0F, 1, 17, 1);
        InnerPillar3.setRotationPoint(-6F, 0F, -6F);
        InnerPillar3.setTextureSize(128, 32);
        InnerPillar3.mirror = true;
        setRotation(InnerPillar3, 0F, 0F, 0F);
        InnerPillar4 = new ModelRenderer(this, 0, 0);
        InnerPillar4.addBox(0F, 0F, 0F, 1, 17, 1);
        InnerPillar4.setRotationPoint(-6F, 0F, 5F);
        InnerPillar4.setTextureSize(128, 32);
        InnerPillar4.mirror = true;
        setRotation(InnerPillar4, 0F, 0F, 0F);
    }

    @Override
    public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5) {
        super.render(entity, f, f1, f2, f3, f4, f5);
        setRotationAngles(f, f1, f2, f3, f4, f5, entity);
        Plate1.render(f5);
        Plate2.render(f5);
        Plate3.render(f5);
        EdgePillar1.render(f5);
        EdgePillar2.render(f5);
        EdgePillar3.render(f5);
        EdgePillar4.render(f5);
        InnerLL1.render(f5);
        InnerLL2.render(f5);
        InnerLSh1.render(f5);
        InnerLSH2.render(f5);
        InnerUp1.render(f5);
        InnerUp2.render(f5);
        InnerUp3.render(f5);
        InnerUp4.render(f5);
        InnerEdgePiece1.render(f5);
        InnerEdgePiece2.render(f5);
        InnerEdgePiece3.render(f5);
        InnerEdgePiece4.render(f5);
        InnerPeak1.render(f5);
        InnerPeak2.render(f5);
        InnerPeak3.render(f5);
        InnerPeak4.render(f5);
        InnerPillar1.render(f5);
        InnerPillar2.render(f5);
        InnerPillar3.render(f5);
        InnerPillar4.render(f5);
    }

    private void setRotation(ModelRenderer model, float x, float y, float z) {
        model.rotateAngleX = x;
        model.rotateAngleY = y;
        model.rotateAngleZ = z;
    }

    @Override
    public void setRotationAngles(float f, float f1, float f2, float f3, float f4, float f5, Entity entity) {
        super.setRotationAngles(f, f1, f2, f3, f4, f5, entity);
    }

}
