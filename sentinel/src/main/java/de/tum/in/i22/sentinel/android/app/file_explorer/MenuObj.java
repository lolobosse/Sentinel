package de.tum.in.i22.sentinel.android.app.file_explorer;

/**
 * Created by Moderbord on 2016-01-06.
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

    @Override
    public int compareTo(MenuObj obj) {
        if (this.name != null){
            return this.name.toLowerCase().compareTo(obj.getName().toLowerCase());
        } else {
            throw new IllegalArgumentException();
        }
    }

    public STATE getState(){
        if (this.getIcon().equalsIgnoreCase("directory_icon") || this.getIcon().equalsIgnoreCase("directory_up")){
            return STATE.FOLDER;
        }
        else{
            return STATE.FILE;
        }
    }

    public enum STATE {FOLDER, FILE}



}
