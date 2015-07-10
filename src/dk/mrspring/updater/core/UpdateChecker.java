package dk.mrspring.updater.core;

import dk.mrspring.api.MCVersion;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by MrSpring on 07-07-2015 for MC Music Player.
 */
public class UpdateChecker
{
    private static UpdateChecker ourInstance = new UpdateChecker();

    public static UpdateChecker getInstance()
    {
        return ourInstance;
    }

    private List<UpdatingMod> registeredMods = new ArrayList<UpdatingMod>();
    private List<QueuedAction> queuedActions = new ArrayList<QueuedAction>();
    private QueuedAction runningAction = null;
    private MCVersion mcVersion;

    private UpdateChecker()
    {
    }

    public List<UpdatingMod> getRegisteredMods()
    {
        return registeredMods;
    }

    public void registerUpdateMod(String modId, String modName, String currentVersion)
    {
        if (modId == null)
            throw new IllegalArgumentException("Mod id cannot be null");
        if (currentVersion == null)
            throw new IllegalArgumentException("Current version cannot be null");
        UpdatingMod mod = new UpdatingMod(modId, modName, currentVersion);
        queueModInfoDownload(mod);
        this.registeredMods.add(mod);
    }

    private void actionPerformedCallback(QueuedAction performed)
    {
        System.out.println(performed.id() + " called back.");
        if (performed == runningAction)
            runningAction = null;
        queuedActions.remove(performed);
        checkRunQueued();
    }

    private void checkRunQueued()
    {
        if (runningAction == null && queuedActions.size() > 0)
        {
            final QueuedAction ac = queuedActions.get(0);
            runningAction = ac;
            System.out.println("Running action: " + ac.id());
            new Thread(new Runnable()
            {
                QueuedAction action = ac;

                @Override
                public void run()
                {
                    action.perform();
                    actionPerformedCallback(action);
                }
            }).start();
        }
    }

    private void queueAction(final QueuedAction registering)
    {
        this.queuedActions.add(registering);
        checkRunQueued();
    }

    protected void queueModInfoDownload(UpdatingMod mod)
    {
        queueAction(mod.makeDownloadAction());
    }

    public void setMCVersion(String mcVersion)
    {
        this.mcVersion = new MCVersion(mcVersion);
    }
}
