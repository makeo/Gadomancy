package makeo.gadomancy.client.renderers.item;

import makeo.gadomancy.client.renderers.tile.RenderTileKnowledgeBook;
import makeo.gadomancy.common.blocks.tiles.TileKnowledgeBook;
import net.minecraft.item.ItemStack;
import org.lwjgl.opengl.GL11;

/**
 * HellFirePvP@Admin
 * Date: 19.04.2016 / 16:02
 * on Gadomancy
 * ItemRenderTEKnowledgeBook
 */
public class ItemRenderTEKnowledgeBook extends ItemRenderTileEntity<TileKnowledgeBook> {

    public ItemRenderTEKnowledgeBook(RenderTileKnowledgeBook bookRender) {
        super(bookRender, new TileKnowledgeBook());
    }

    @Override
    public void renderItem(ItemRenderType type, ItemStack item, Object... data) {
        GL11.glPushMatrix();
        if(type.equals(ItemRenderType.INVENTORY)) {
            GL11.glScalef(1.4F, 1.4F, 1.4F);
        } else if(type.equals(ItemRenderType.ENTITY)) {
            GL11.glScalef(2F, 2F, 2F);
        } else if(type.equals(ItemRenderType.EQUIPPED)) {
            GL11.glRotatef(15F, 1, 0, 0);
            GL11.glRotatef(-50F, 0, 1, 0);
            GL11.glTranslatef(-0.4F, 0F, -1.25F);
            GL11.glScalef(1.8F, 1.8F, 1.8F);
        } else if(type.equals(ItemRenderType.EQUIPPED_FIRST_PERSON)) {
            GL11.glTranslatef(-0.2F, 0.4F, 0);
            GL11.glRotatef(30F, 0, 1, 0);
            GL11.glRotatef(55F, 0, 0, 1);
        }
        super.renderItem(type, item, data);
        GL11.glPopMatrix();
    }
}
