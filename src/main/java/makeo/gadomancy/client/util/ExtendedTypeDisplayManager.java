package makeo.gadomancy.client.util;

import makeo.gadomancy.client.events.ResourceReloadListener;
import makeo.gadomancy.common.node.ExtendedNodeType;
import net.minecraft.util.StatCollector;
import thaumcraft.api.nodes.NodeType;

/**
 * This class is part of the Gadomancy Mod
 * Gadomancy is Open Source and distributed under the
 * GNU LESSER GENERAL PUBLIC LICENSE
 * for more read the LICENSE file
 *
 * Created by HellFirePvP @ 23.10.2015 22:53
 */
public class ExtendedTypeDisplayManager {

    private static int timeout = 7;
    private static int currTime = 0;

    private static boolean changedLanguageFile = false;
    private static String currentNodeId = null;
    private static String changedEntry = null;
    private static String oldName = null;

    public static void notifyRenderTick() {
        if(currTime > timeout) {
            if(changedLanguageFile) {
                resetLanguageFile();
            }
        } else {
            currTime++;
        }
    }

    private static void resetLanguageFile() {
        currentNodeId = null;
        ResourceReloadListener.languageList.put(changedEntry, oldName);
        changedLanguageFile = false;
    }

    public static void notifyDisplayTick(String id, NodeType nodeType, ExtendedNodeType extendedNodeType) {
        currTime = 0;

        if(currentNodeId != null && !currentNodeId.equals(id)) {
            //New node.
            resetLanguageFile();
        }

        if(!changedLanguageFile) {
            String toChance = "nodetype." + nodeType + ".name";
            String name = StatCollector.translateToLocal(toChance);
            String growingStr = StatCollector.translateToLocal("gadomancy.nodes." + extendedNodeType.name());
            String newName = name + ", " + growingStr;
            ResourceReloadListener.languageList.put(toChance, newName);

            oldName = name;
            changedEntry = toChance;
            changedLanguageFile = true;
            currentNodeId = id;
        }
    }
}
