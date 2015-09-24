package dk.mrspring.updater.core;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by MrSpring on 24-09-2015 for MrSpringUpdateChecker.
 */
public class ModUpdater
{
    private static final ModUpdater INSTANCE = new ModUpdater();

    private List<UpdatingMod> mods = new ArrayList<>();
    private List<QueuedAction> actions = new ArrayList<>();
    private QueuedActionRunner running = null;

    public static ModUpdater getInstance()
    {
        return INSTANCE;
    }

    public void registerMod(UpdatingMod mod)
    {
        if (mod != null && !this.hasMod(mod.MOD_ID)) addMod(mod);
    }

    private void addMod(UpdatingMod mod)
    {
        System.out.printf("Registering mod: %s", mod.MOD_ID);
        mods.add(mod);
        addQueuedAction(mod.makeInfoDownloadAction());
    }

    public void addQueuedAction(QueuedAction action)
    {
        this.actions.add(action);
        this.checkRunningAction();
    }

    public List<UpdatingMod> getRegisteredMods()
    {
        return mods;
    }

    private boolean checkRunningAction()
    {
        if ((running == null || running.finished) && actions.size() > 0)
        {
            runNextAction();
            return true;
        } else return false;
    }

    private void runNextAction()
    {
        if (running != null && !running.finished) throw new IllegalStateException("Action not yet finished!");
        if (actions.size() == 0) return;
        QueuedAction action = actions.get(0);
        actions.remove(action);
        this.running = new QueuedActionRunner(action);
        this.running.start();
    }

    private boolean hasMod(String modId)
    {
        for (UpdatingMod mod : mods)
            if (mod.MOD_ID.equals(modId)) return true;
        return false;
    }

    protected void queuedActionCallback(QueuedAction action)
    {
        this.checkRunningAction();
    }
}
