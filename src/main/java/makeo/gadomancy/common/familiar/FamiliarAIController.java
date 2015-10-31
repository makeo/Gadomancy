package makeo.gadomancy.common.familiar;

import makeo.gadomancy.common.familiar.ai.FamiliarAIProcess;
import net.minecraft.entity.player.EntityPlayer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * This class is part of the Gadomancy Mod
 * Gadomancy is Open Source and distributed under the
 * GNU LESSER GENERAL PUBLIC LICENSE
 * for more read the LICENSE file
 *
 * Created by HellFirePvP @ 31.10.2015 14:08
 */
public class FamiliarAIController {

    private static final Random RAND = new Random();

    private List<FamiliarAIProcess> availableTasks = new ArrayList<FamiliarAIProcess>();
    private Map<FamiliarAIProcess, Integer> cooldownProcesses = new HashMap<FamiliarAIProcess, Integer>();
    private FamiliarAIProcess runningTask;
    private int ticksInTask;
    private EntityPlayer owningPlayer;

    public FamiliarAIController(EntityPlayer owningPlayer) {
        this.owningPlayer = owningPlayer;
    }

    public void sheduleTick() {
        reduceRunningCooldowns();
        if(runningTask == null) {
            selectNewTask();
        } else {
            runningTask.tick(ticksInTask, owningPlayer.worldObj, owningPlayer);
            ticksInTask++;
            if(ticksInTask >= runningTask.getDuration()) {
                if(runningTask.getCooldownDuration() > 0) cooldownProcesses.put(runningTask, runningTask.getCooldownDuration());
                runningTask = null;
                ticksInTask = 0;
            }
        }
    }

    private void selectNewTask() {
        int size = availableTasks.size();
        int randIndex = RAND.nextInt(size);
        for (int i = 0; i < size; i++) {
            int index = (randIndex + i) % size;
            FamiliarAIProcess process = availableTasks.get(index);
            if(process.canRun(owningPlayer.worldObj, owningPlayer.posX, owningPlayer.posY, owningPlayer.posZ, owningPlayer) &&
                    !cooldownProcesses.containsKey(process)) {
                runningTask = process;
            }
        }
    }

    private void reduceRunningCooldowns() {
        Iterator<FamiliarAIProcess> itProcesses = cooldownProcesses.keySet().iterator();
        while(itProcesses.hasNext()) {
            FamiliarAIProcess process = itProcesses.next();
            int cd = cooldownProcesses.get(process) - 1;
            if(cd <= 0) {
                itProcesses.remove();
            } else {
                cooldownProcesses.put(process, cd);
            }
        }
    }

    public EntityPlayer getOwningPlayer() {
        return owningPlayer;
    }
}
