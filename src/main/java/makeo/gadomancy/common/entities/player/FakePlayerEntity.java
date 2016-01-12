package makeo.gadomancy.common.entities.player;

import com.mojang.authlib.GameProfile;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.util.IChatComponent;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

import java.util.UUID;

/**
 * This class is part of the Gadomancy Mod
 * Gadomancy is Open Source and distributed under the
 * GNU LESSER GENERAL PUBLIC LICENSE
 * for more read the LICENSE file
 * <p/>
 * Created by makeo @ 06.12.2015 01:01
 */
public class FakePlayerEntity extends EntityPlayer {
    public FakePlayerEntity(World world) {
        super(world, new GameProfile(UUID.fromString("d36a4fa5-c70e-4358-8418-9ed683687d0a"), "bla"));
    }

    @Override
    public void addChatMessage(IChatComponent component) {

    }

    @Override
    public boolean canCommandSenderUseCommand(int p_70003_1_, String p_70003_2_) {
        return false;
    }

    @Override
    public ChunkCoordinates getPlayerCoordinates() {
        return new ChunkCoordinates(MathHelper.floor_double(this.posX), MathHelper.floor_double(this.posY + 0.5D), MathHelper.floor_double(this.posZ));
    }
}
