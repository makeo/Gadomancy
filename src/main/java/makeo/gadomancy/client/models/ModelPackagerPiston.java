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
 * Created by makeo @ 29.11.2015 12:36
 */
public class ModelPackagerPiston extends ModelBase {
    ModelRenderer shape1;
    ModelRenderer shape2;

    public ModelPackagerPiston(boolean cut)
    {
        textureWidth = 64;
        textureHeight = 32;

        shape1 = new ModelRenderer(this, 0, 0);
        shape1.addBox(0F, 0F, 0F, 10, 2, 10);
        shape1.setRotationPoint(-5F, 16F, -5F);
        shape1.setTextureSize(64, 32);
        shape1.mirror = true;

        shape2 = new ModelRenderer(this, 41, cut ? 1 : 0);
        shape2.addBox(0F, 0F, 0F, 4, 4, 4);
        shape2.setRotationPoint(-2F, cut ? 13F : 12F, -2F);
        shape2.setTextureSize(64, 32);
        shape2.mirror = true;
    }

    public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5)
    {
        super.render(entity, f, f1, f2, f3, f4, f5);
        shape1.render(f5);
        shape2.render(f5);
    }
}
