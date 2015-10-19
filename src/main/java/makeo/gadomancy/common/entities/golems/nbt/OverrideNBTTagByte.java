package makeo.gadomancy.common.entities.golems.nbt;

import net.minecraft.nbt.NBTTagByte;

/**
 * This class is part of the Gadomancy Mod
 * Gadomancy is Open Source and distributed under the
 * GNU LESSER GENERAL PUBLIC LICENSE
 * for more read the LICENSE file
 *
 * Created by makeo @ 28.07.2015 17:03
 */
public class OverrideNBTTagByte extends NBTTagByte {
    private boolean override = true;
    private byte overrideData;


    public OverrideNBTTagByte(byte data, byte overrideData) {
        super(data);
        this.overrideData = overrideData;
    }

    @Override
    public byte func_150290_f() {
        if(override) {
            override = false;
            return overrideData;
        }
        return super.func_150290_f();
    }
}
