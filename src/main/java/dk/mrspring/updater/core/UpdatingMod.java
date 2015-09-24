package dk.mrspring.updater.core;

import com.google.gson.internal.LinkedTreeMap;
import dk.mrspring.api.type.LatestVersion;
import dk.mrspring.api.type.Mod;
import dk.mrspring.api.type.Version;
import dk.mrspring.api.type.Versions;

import java.io.File;
import java.util.Map;

/**
 * Created by MrSpring on 24-09-2015 for MrSpringUpdateChecker.
 */
public class UpdatingMod implements ModInfoDownloadAction.Callback
{
    public final String FALLBACK_NAME;
    public final String MOD_ID;
    public final String CURRENT_VERSION;
    public final String[] FALLBACK_CHANGE_LOG = {"Loading..."};
    public final File SOURCE_FILE;
    private Mod modInfo = null;
    private Map<String, UpdatingVersion> versions = new LinkedTreeMap<>();
    private LatestVersion latestVersion;

    public UpdatingMod(String name, String modId, String currentVersion, File modFile)
    {
        this.FALLBACK_NAME = name;
        this.MOD_ID = modId;
        this.CURRENT_VERSION = currentVersion;
        this.SOURCE_FILE = modFile;
    }

    public String getDisplayName()
    {
        return modInfo == null ? FALLBACK_NAME : modInfo.getModName();
    }

    public boolean hasLatest()
    {
        return latestVersion.isLatestVersion();
    }

    public String getCurrent()
    {
        return CURRENT_VERSION;
    }

    public String getLatest()
    {
        return latestVersion.getLatestVersion().getVersionString();
    }

    public QueuedAction makeInfoDownloadAction()
    {
        return new ModInfoDownloadAction(this, this);
    }

    public String[] getChangeLogForVersion(String version)
    {
        return this.getChangeLogForVersion(this.getUpdatingVersion(version));
    }

    private UpdatingVersion getUpdatingVersion(String version)
    {
        return this.versions.get(version);
    }

    public String[] getLatestChangeLog()
    {
        return this.getChangeLogForVersion(latestVersion.getLatestVersion().getVersionString());
    }

    public Version getVersion(String version)
    {
        return versions.get(version).getVersion();
    }

    public String[] getChangeLogForVersion(UpdatingVersion version)
    {
//        System.out.println("Getting changelog for: "+version.getVersionString());
        if (version.isSimple())
        {
            if (version.shouldStartDownloading())
                version.startDownloading();
            return FALLBACK_CHANGE_LOG;
        } else return version.getChangeLog();
    }

    public String[] getVersionIds()
    {
        String[] versions = new String[this.versions.size()];
        int i = 0;
        for (Map.Entry<String, UpdatingVersion> entry : this.versions.entrySet()) versions[i++] = entry.getKey();
        return versions;
    }

    public String[] getVersionsForDisplay()
    {
        String[] versions = getVersionIds();
        versions[0] += " (Latest)";
        return versions;
    }

    @Override
    public void callback(Mod mod, LatestVersion version, Versions simpleVersions)
    {
        this.modInfo = mod;
        this.latestVersion = version;
//        this.versions = simpleVersions.getVersionMap();
        this.versions = new LinkedTreeMap<>();
        for (Map.Entry<String, Version> entry : simpleVersions.getVersionMap().entrySet())
            this.versions.put(entry.getKey(), new UpdatingVersion(this, entry.getValue()));
    }
}
