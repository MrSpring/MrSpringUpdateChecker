package dk.mrspring.updater.core;

import dk.mrspring.api.Versions;
import dk.mrspring.api.type.ChangeLog;
import dk.mrspring.api.type.Version;

/**
 * Created by MrSpring on 08-07-2015 for MC Music Player.
 */
public class LoggedVersion
{
    public Version version;
    public ChangeLog log;

    public LoggedVersion(Version version, ChangeLog log)
    {
        this.version = version;
        this.log = log;
    }

    public LoggedVersion(Version version)
    {
        this.version = version;
        this.log = Versions.getChangeLog(version);
    }

    public LoggedVersion(String mod, String version)
    {
        this.version = Versions.getVersion(mod, version);
        this.log = Versions.getChangeLog(mod, version);
    }
}
