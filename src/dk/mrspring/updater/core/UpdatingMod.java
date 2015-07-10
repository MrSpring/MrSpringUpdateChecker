package dk.mrspring.updater.core;

import dk.mrspring.api.Mods;
import dk.mrspring.api.Versions;
import dk.mrspring.api.type.ChangeLog;
import dk.mrspring.api.type.LatestVersion;
import dk.mrspring.api.type.Mod;
import dk.mrspring.api.type.Version;

/**
 * Created by MrSpring on 07-07-2015 for MC Music Player.
 */
public class UpdatingMod
{
    public String modId;
    public String modName;
    public String currentVersion;
    public Mod downloadedMod;
    public LatestVersion downloadedLatestVersion;
    public ChangeLog downloadedLatestChangeLog;
    public Version[] versions;

    public UpdatingMod(String modId, String modName, String currentVersion)
    {
        this.modId = modId;
        this.modName = modName;
        this.currentVersion = currentVersion;
    }

    public boolean hasLatest()
    {
        return downloadedLatestVersion != null && downloadedLatestVersion.isLatest();
    }

    public String getLatest()
    {
        return downloadedLatestVersion != null ? downloadedLatestVersion.getLatest().getName() : null;
    }

    public void downloadAdditionalInfo()
    {
        downloadedMod = Mods.getMod(modId);
        downloadedLatestVersion = Versions.getLatestVersion(modId, currentVersion);
        downloadedLatestChangeLog = Versions.getChangeLog(downloadedLatestVersion.getLatest());
        downloadVersions(true);
    }

    public void downloadVersions(boolean simple)
    {
        this.versions = Versions.getVersions(modId, simple);
        System.out.println("Downloaded versions for: "+modId+".");
        System.out.println(versions.length);
    }

    public void downloadVersion(String version)
    {
        if (versions == null)
            downloadVersions(true);
        for (int i = 0; i < versions.length; i++)
            if (versions[i].getName().equals(version))
                versions[i] = Versions.getVersion(modId, version);
    }

    public String getDisplayName()
    {
        return modName == null ? getDownloadedName() : modName;
    }

    public String getDownloadedName()
    {
        if (downloadedMod == null)
            throw new IllegalStateException("Mod info cannot be accessed before it has been downloaded");
        return downloadedMod.getFullName();
    }

    public QueuedAction makeDownloadAction()
    {
        return new QueuedAction()
        {
            @Override
            public void perform()
            {
                downloadAdditionalInfo();
                try
                {
                    Thread.sleep(500);
                } catch (InterruptedException e)
                {
                    e.printStackTrace();
                }
            }

            @Override
            public String id()
            {
                return modId + ":" + currentVersion;
            }
        };
    }

    public String[] getLatestChangelog()
    {
        return downloadedLatestChangeLog.getChangeLog();
    }

    public String getCurrent()
    {
        return currentVersion;
    }

    public String[] getAvailableVersions(boolean format)
    {
        String[] versions = new String[this.versions.length];
        for (int i = 0; i < versions.length; i++)
        {
            String versionName = this.versions[i].getName();
            if (format && i == 0) versionName += " (Latest)";
            versions[i] = versionName;
        }
        return versions;
    }
}
