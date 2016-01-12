package makeo.gadomancy.common.data.config;

import com.google.gson.Gson;
import cpw.mods.fml.common.FMLLog;
import makeo.gadomancy.common.Gadomancy;
import net.minecraft.server.MinecraftServer;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * This class is part of the Gadomancy Mod
 * Gadomancy is Open Source and distributed under the
 * GNU LESSER GENERAL PUBLIC LICENSE
 * for more read the LICENSE file
 *
 * Created by makeo @ 26.07.2015 18:15
 */
public class ModData {
    private static final Gson GSON = new Gson();

    private String name;
    private File file;

    private Map<String, Object> data = new HashMap<String, Object>();

    public ModData(String name, File directory) {
        if(directory == null)
            throw new IllegalArgumentException("Directory is null!");

        this.name = name;
        this.file = new File(directory, name + ".dat");
    }

    public ModData(String name) {
        this(name, geDefaultDirectory());
    }

    private static File geDefaultDirectory() {
        MinecraftServer server = MinecraftServer.getServer();
        if(server != null && server.getEntityWorld() != null) {
            File file = server.getEntityWorld().getSaveHandler().getWorldDirectory();
            if(file != null) {
                return new File(file, Gadomancy.MODID);
            }
        }
        return null;
    }

    public <T> T get(String key, T defaultValue) {
        if(data.containsKey(key)) {
            return (T) data.get(key);
        }
        data.put(key, defaultValue);
        return defaultValue;
    }

    public <T> T get(String key) {
        return get(key, null);
    }

    public void set(String key, Object value) {
        data.put(key, value);
    }

    public boolean contains(String key) {
        return data.containsKey(key);
    }

    public boolean load() {
        if(file.exists()) {

            FileInputStream in = null;
            try {
                in = new FileInputStream(file);
                data = (Map<String, Object>) new ObjectInputStream(in).readObject();
            } catch (Exception e) { //IOException | ClassCastException | JsonSyntaxException | ClassNotFoundException
                e.printStackTrace();
                return false;
            } finally {
                if(in != null)
                    try {
                        in.close();
                    } catch (IOException ignored) { }
            }
        }
        return true;
    }

    public boolean save() {
        if(!file.getParentFile().exists() && !file.getParentFile().mkdirs()) {
            FMLLog.warning("Failed to create directory: \"" + file.toString() + "\"!");
            return false;
        }

        FileOutputStream out = null;
        try {
            if(!file.exists())
                file.createNewFile();

            out = new FileOutputStream(file);
            new ObjectOutputStream(out).writeObject(data);

        } catch (Exception e) {//JsonIOException | IOException
            e.printStackTrace();
            return false;
        } finally {
            if(out != null)
                try {
                    out.close();
                } catch (IOException ignored) { }
        }
        return true;
    }
}
