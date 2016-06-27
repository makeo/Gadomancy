package makeo.gadomancy.client.renderers.tile;

import makeo.gadomancy.common.blocks.tiles.TileKnowledgeBook;
import net.minecraft.client.model.ModelBook;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

/**
 * HellFirePvP@Admin
 * Date: 19.04.2016 / 14:57
 * on Gadomancy
 * RenderTileKnowledgeBook
 */
public class RenderTileKnowledgeBook extends TileEntitySpecialRenderer {

    private static final ResourceLocation bookTexture = new ResourceLocation("minecraft", "textures/entity/enchanting_table_book.png");
    private static final ModelBook modelBook = new ModelBook();

    @Override
    public void renderTileEntityAt(TileEntity te, double x, double y, double z, float partialTicks) {
        if(te == null || !(te instanceof TileKnowledgeBook)) return;

        renderFloatingBook((TileKnowledgeBook) te, x, y, z, partialTicks);
    }

    private void renderFloatingBook(TileKnowledgeBook te, double x, double y, double z, float partialTicks) {
        TileKnowledgeBook.FloatingBookAttributes attributes = te.getBookAttributes();

        GL11.glPushMatrix();
        GL11.glTranslatef((float)x + 0.5F, (float)y + 0.1F, (float)z + 0.5F);
        float f1 = (float)attributes.field_145926_a + partialTicks;
        GL11.glTranslatef(0.0F, 0.1F + MathHelper.sin(f1 * 0.1F) * 0.03F, 0.0F);
        float f2;

        for (f2 = attributes.field_145928_o - attributes.field_145925_p; f2 >= (float)Math.PI; f2 -= ((float)Math.PI * 2F)) {}

        while (f2 < -(float)Math.PI)
        {
            f2 += ((float)Math.PI * 2F);
        }

        float f3 = attributes.field_145925_p + f2 * partialTicks;
        GL11.glRotatef(-f3 * 180.0F / (float)Math.PI, 0.0F, 1.0F, 0.0F);
        GL11.glRotatef(80.0F, 0.0F, 0.0F, 1.0F);
        this.bindTexture(bookTexture);
        float f4 = attributes.field_145931_j + (attributes.field_145933_i - attributes.field_145931_j) * partialTicks + 0.25F;
        float f5 = attributes.field_145931_j + (attributes.field_145933_i - attributes.field_145931_j) * partialTicks + 0.75F;
        f4 = (f4 - (float)MathHelper.truncateDoubleToInt((double)f4)) * 1.6F - 0.3F;
        f5 = (f5 - (float)MathHelper.truncateDoubleToInt((double)f5)) * 1.6F - 0.3F;

        if (f4 < 0.0F)
        {
            f4 = 0.0F;
        }

        if (f5 < 0.0F)
        {
            f5 = 0.0F;
        }

        if (f4 > 1.0F)
        {
            f4 = 1.0F;
        }

        if (f5 > 1.0F)
        {
            f5 = 1.0F;
        }

        //float f6 = attributes.field_145927_n + (attributes.field_145930_m - attributes.field_145927_n) * partialTicks; We don't want it to close..
        GL11.glEnable(GL11.GL_CULL_FACE);
        modelBook.render(null, 45F, f4, f5, 0.9F, 0.0F, 0.0625F);
        GL11.glPopMatrix();
    }

}
