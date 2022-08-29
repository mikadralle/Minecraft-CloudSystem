package de.leantwi.cloudsystem.wrapper.core;

import de.leantwi.cloudsystem.CloudSystem;
import de.leantwi.cloudsystem.api.CloudSystemAPI;
import de.leantwi.cloudsystem.api.gameserver.GameServerData;
import de.leantwi.cloudsystem.wrapper.WrapperBootstrap;
import lombok.Getter;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;

public class FolderUtils {

    public final File TEMP_PATH = new File("temp/");
    public final File LIVE_PATH = new File("live/");
    public final File LIVE_STATIC_PATH = new File("static/");
    public final File DEFAULT_PLUGINS = new File("default/plugins/");
    public final File DEFAULT_VERSIONS = new File("default/versions");
    public final File DEFAULT_SERVER = new File("default/server");
    @Getter

    private final CloudSystemAPI cloudSystemAPI = CloudSystem.getAPI();

    public FolderUtils() {
        this.deleteFolder(new File("live/"));

        if (!TEMP_PATH.exists()) {
            TEMP_PATH.mkdirs();
        }
        if (!LIVE_PATH.exists()) {
            LIVE_PATH.mkdirs();
        }

        if (!DEFAULT_PLUGINS.exists()) {
            DEFAULT_PLUGINS.mkdirs();
        }
        if (!DEFAULT_VERSIONS.exists()) {
            DEFAULT_VERSIONS.mkdirs();
            WrapperBootstrap.getInstance().getLogger().warning("the default version folder is §cempty§f.");
        }

        if (!DEFAULT_SERVER.exists()) {

            DEFAULT_SERVER.mkdirs();
            WrapperBootstrap.getInstance().getLogger().warning("the default server folder is §cempty§f.");

        }

        //load new groups folder.
        this.checkForNewGroups();

    }


    public File getPath(GameServerData gameServerData) {

        if (cloudSystemAPI.getSubGroupByName(gameServerData.getSubGroupDB()).get().getServerDB().isStaticMode()) {
            return LIVE_STATIC_PATH;
        }
        return LIVE_PATH;

    }

    public void deleteFolder(File folder) {
        File[] files = folder.listFiles();
        if (files != null) {
            File[] arrayOfFile1;
            int j = (arrayOfFile1 = files).length;
            for (int i = 0; i < j; i++) {
                File files2 = arrayOfFile1[i];
                if (files2.isDirectory()) {
                    deleteFolder(files2);
                } else {
                    files2.delete();
                }
            }
        }
        folder.delete();
    }

    private void checkForNewGroups() {

        WrapperBootstrap.getInstance().getLogger().info("Checking and loading groups ...");


        this.cloudSystemAPI.getAllGroups().forEach(groups -> {

            groups.getSubGroupDBList().forEach(subGroupDB -> {
                createGroupFolder(groups.getGroupName(), subGroupDB.getSubGroupName());
            });


        });

        WrapperBootstrap.getInstance().getLogger().info("all groups has been loaded.");

    }

    public void createGroupFolder(String groupName, String subGroupName) {
        File currentFolder = new File(this.TEMP_PATH + "/" + groupName + "/" + subGroupName);
        if (!currentFolder.exists()) {
            currentFolder.mkdirs();

            try {
                //copy the default server folder to new current subgroup folder
                FileUtils.copyDirectory(this.DEFAULT_SERVER, currentFolder);
                WrapperBootstrap.getInstance().getLogger().info("the template from group " + groupName + " has been created.");
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

        }
    }

}