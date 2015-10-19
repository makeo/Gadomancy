package makeo.gadomancy.client.gui;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import makeo.gadomancy.api.GadomancyApi;
import makeo.gadomancy.api.golems.cores.AdditionalGolemCore;
import makeo.gadomancy.common.utils.Injector;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.StatCollector;
import org.lwjgl.opengl.GL11;
import thaumcraft.client.gui.GuiGolem;
import thaumcraft.common.entities.golems.ContainerGolem;
import thaumcraft.common.entities.golems.EntityGolemBase;

/**
 * This class is part of the Gadomancy Mod
 * Gadomancy is Open Source and distributed under the
 * GNU LESSER GENERAL PUBLIC LICENSE
 * for more read the LICENSE file
 *
 * Created by makeo @ 10.09.2015 14:15
 */
@SideOnly(Side.CLIENT)
public class AdditionalGolemGui extends GuiGolem {
    protected EntityPlayer player;
    protected EntityGolemBase golem;
    protected int threat;

    public AdditionalGolemGui(EntityPlayer player, EntityGolemBase golem) {
        super(player, golem);

        this.player = player;
        this.golem = golem;

        Injector injector = new Injector(this, GuiGolem.class);

        this.threat = injector.getField("threat");
    }

    protected void drawGuiContainerForegroundLayer(int par1, int par2) {
        GL11.glPushMatrix();
        GL11.glScaled(0.5D, 0.5D, 0.5D);
        if (this.threat >= 0) {
            drawInfoString("golemthreat." + this.threat + ".text");
        } else {
            AdditionalGolemCore core = GadomancyApi.getAdditionalGolemCore(this.golem);
            if(core != null) {
                drawInfoString(core.getUnlocalizedGuiText());
            } else {
                drawInfoString("golemblurb." + this.golem.getCore() + ".text");
            }
        }
        if (((ContainerGolem) this.inventorySlots).maxScroll > 0) {
            this.fontRendererObj.drawString(((ContainerGolem) this.inventorySlots).currentScroll + 1 + "/" + (((ContainerGolem) this.inventorySlots).maxScroll + 1), 323, 140, 14540253);
        }
        GL11.glPopMatrix();
    }

    private void drawInfoString(String unlocalized) {
        this.fontRendererObj.drawSplitString(StatCollector.translateToLocal(unlocalized), 80, 22, 110, 14540253);
    }
}
