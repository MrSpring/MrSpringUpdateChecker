package dk.mrspring.updater.core;

/**
 * Created on 24-09-2015 for MrSpringUpdateChecker.
 */
public abstract class QueuedAction
{
    final UpdatingMod MOD;

    public QueuedAction(UpdatingMod mod)
    {
        this.MOD = mod;
    }

    public abstract void perform();

    public String getModId()
    {
        return this.getMod().MOD_ID;
    }

    public UpdatingMod getMod()
    {
        return this.MOD;
    }
}
