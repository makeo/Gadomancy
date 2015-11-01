package makeo.gadomancy.common.familiar;

import makeo.gadomancy.common.familiar.ai.FamiliarAIProcess;
import makeo.gadomancy.common.registration.RegisteredFamiliarAI;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
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

    private static Map<EntityPlayer, LinkedList<EntityLivingBase>> targetMap = new HashMap<EntityPlayer, LinkedList<EntityLivingBase>>();

    private List<FamiliarAIProcess> availableTasks = new ArrayList<FamiliarAIProcess>();
    private Map<FamiliarAIProcess, Integer> cooldownProcesses = new HashMap<FamiliarAIProcess, Integer>();
    private LinkedList<FamiliarAIProcess> requestedLoop = new LinkedList<FamiliarAIProcess>();
    private FamiliarAIProcess runningTask;
    private int ticksInTask;
    private EntityPlayer owningPlayer;

    public FamiliarAIController(EntityPlayer owningPlayer) {
        this.owningPlayer = owningPlayer;
    }

    public void registerDefaultTasks() {
        this.availableTasks.add(RegisteredFamiliarAI.familiarAIIdle);
        this.availableTasks.add(RegisteredFamiliarAI.familiarAIZapAttackingMonsters);
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
                if(runningTask.tryLoop() && !requestedLoop.contains(runningTask)) requestedLoop.addLast(runningTask);
                runningTask = null;
                ticksInTask = 0;
            }
        }
    }

    private void selectNewTask() {
        if(!requestedLoop.isEmpty()) {
            Iterator<FamiliarAIProcess> it = requestedLoop.iterator();
            while(it.hasNext()) {
                FamiliarAIProcess process = it.next();
                if(!cooldownProcesses.containsKey(process)) {
                    runningTask = process;
                    it.remove();
                    return;
                }
            }
        }
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

    public static boolean hasLastTargetter(EntityPlayer player) {
        return targetMap.containsKey(player) && targetMap.get(player).size() >= 1;
    }

    public static LinkedList<EntityLivingBase> getLastTargetters(EntityPlayer player) {
        if(!targetMap.containsKey(player)) return null;
        if(targetMap.get(player).size() < 1) return null;
        return targetMap.get(player);
    }

    public static void cleanTargetterList(EntityPlayer player) {
        if(!targetMap.containsKey(player)) return;
        Iterator<EntityLivingBase> it = targetMap.get(player).iterator();
        while(it.hasNext()) {
            EntityLivingBase living = it.next();
            if(living.isDead) it.remove();
        }
    }

    public static void notifyTargetEvent(EntityLivingBase targetter, EntityPlayer targetted) {
        LinkedList<EntityLivingBase> targetters;
        boolean needsUpdate = false;
        if(targetMap.containsKey(targetted)) {
            targetters = targetMap.get(targetted);
        } else {
            targetters = new LinkedList<EntityLivingBase>();
            needsUpdate = true;
        }

        if(!targetters.contains(targetter)) {
            targetters.addLast(targetter);
        }
        if(needsUpdate) {
            targetMap.put(targetted, targetters);
        }
    }
}
