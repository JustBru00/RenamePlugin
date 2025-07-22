/**
 * @author Justin "JustBru00" Brubaker
 *
 * This is licensed under the MPL Version 2.0. See license info in LICENSE.txt
 */
package com.gmail.justbru00.epic.rename.multiversion;

import com.gmail.justbru00.epic.rename.utils.v3.Messager;
import org.bukkit.Server;

public class ServerVersion {

    private final int major;
    private final int minor;
    private final int patch;

    public ServerVersion(int major, int minor, int patch) {
        this.major = major;
        this.minor = minor;
        this.patch = patch;
    }

    public boolean isAtLeast(int major, int minor, int patch) {
        if (this.major > major) return true;
        if (this.major < major) return false;
        if (this.minor > minor) return true;
        if (this.minor < minor) return false;
        return this.patch >= patch;
    }

    public boolean isLessThan(int major, int minor, int patch) {
        if (this.major < major) return true;
        if (this.major > major) return false;
        if (this.minor < minor) return true;
        if (this.minor > minor) return false;
        return this.patch < patch;
    }

    public boolean isLessThanOrEqualTo(int major, int minor, int patch) {
        if (this.major < major) return true;
        if (this.major > major) return false;
        if (this.minor < minor) return true;
        if (this.minor > minor) return false;
        return this.patch <= patch;
    }

    public static ServerVersion parseVersion(String version) {
        try {
            String[] parts = version.split("\\.");
            int major = Integer.parseInt(parts[0]);
            int minor = parts.length > 1 ? Integer.parseInt(parts[1]) : 0;
            int patch = parts.length > 2 ? Integer.parseInt(parts[2]) : 0;
            return new ServerVersion(major, minor, patch);
        } catch (NumberFormatException | ArrayIndexOutOfBoundsException e) {
            Messager.msgConsole("&c[CheckServerVersion] Failed to parse the MC version from '" + version + "'.  Assuming 1.20.5 or newer.");
            return new ServerVersion(1, 20, 5);
        }
    }
}
