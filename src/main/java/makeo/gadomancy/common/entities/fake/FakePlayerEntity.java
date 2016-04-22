package makeo.gadomancy.common.entities.fake;

import com.mojang.authlib.GameProfile;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.IChatComponent;
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
    public boolean isSpectator() {
        return false;
    }
}
