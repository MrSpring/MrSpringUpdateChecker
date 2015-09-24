package dk.mrspring.updater.core;

import dk.mrspring.api.type.Version;

/**
 * Created on 24-09-2015 for MrSpringUpdateChecker.
 */
public class UpdatingVersion implements FullVersionDownloadAction.Callback
{
    Version version;
    UpdatingMod mod;
    boolean downloading = false;

    public UpdatingVersion(UpdatingMod mod, Version version)
    {
        this.mod = mod;
        this.version = version;
    }

    public boolean isSimple()
    {
        return this.getVersion().isSimple();
    }

    public String getName()
    {
        return getVersion().getVersionString();
    }

    public Version getVersion()
    {
        return version;
    }

    public boolean shouldStartDownloading()
    {
        return isSimple() && !downloading;
    }

    public String[] getChangeLog()
    {
        return version.getChangeLog();
    }

    public void startDownloading()
    {
        downloading = true;
        ModUpdater.getInstance().addQueuedAction(new FullVersionDownloadAction(mod, getVersion(), this));
    }

    @Override
    public void callback(Version updated)
    {
        downloading = false;
    }
}
