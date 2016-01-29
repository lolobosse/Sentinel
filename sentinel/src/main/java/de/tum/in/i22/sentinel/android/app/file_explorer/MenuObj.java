package de.tum.in.i22.sentinel.android.app.file_explorer;

import de.tum.in.i22.sentinel.android.app.Constants;

/**
 * Created by Moderbord on 2016-01-06.
 */

/**
 * This class acts as a model for {@see de.tum.in.i22.sentinel.android.app.file_explorer.DirectoryChooser}
 * and {@see de.tum.in.i22.sentinel.android.app.file_explorer.FileChooser}
 * It provides the path to the folder/files and some useful information like name and icon.
 */
public class MenuObj implements Comparable<MenuObj> {

    private String name, data, date, path, icon;

    public MenuObj(String name, String data, String date, String path, String icon) {
        this.name = name;
        this.data = data;
        this.date = date;
        this.path = path;
        this.icon = icon;
    }

    public String getName() {
        return name;
    }

    public String getData() {
        return data;
    }

    public String getDate() {
        return date;
    }

    public String getPath() {
        return path;
    }

    public String getIcon() {
        return icon;
    }

    /**
     * Comparision based on the name of the different item (usage of the implementation of
     * {@see java.lang.String#compareTo(java.lang.String)}
     * @param obj: the object to be compared to
     * @return int like for standard comparison
     */
    @Override
    public int compareTo(MenuObj obj) {
        if (this.name != null) {
            return this.name.toLowerCase().compareTo(obj.getName().toLowerCase());
        } else {
            throw new IllegalArgumentException();
        }
    }


    /**
     * Helper class to make the code a bit cleaner, it basically uses the legacy structure
     * which consists in defining the type of an item by looking the icon representing it and package it
     * in a nice two elements enum
     * @return: STATE of the item (Folder or File)
     * LIMITATIONS: the Choose this Directory is represented as file... :(
     */
    public STATE getState() {
        if (this.getIcon().equalsIgnoreCase(Constants.DIRECTORY_ICON) || this.getIcon().equalsIgnoreCase(Constants.DIRECTORY_UP)) {
            return STATE.FOLDER;
        } else {
            return STATE.FILE;
        }
    }

    public enum STATE {FOLDER, FILE}


}
