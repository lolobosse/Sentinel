package de.tum.in.i22.sentinel.android.app.file_explorer;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import java.io.File;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import de.tum.in.i22.sentinel.android.app.R;

/**
 * Created by Moderbord on 2016-01-06.
 */
public class DirectoryChooser extends ListActivity {

    public static final String FOLDER_PATH = "GetFolderPath";
    private File workingDir;
    private FileArrayAdapter adapter;
    private String setDirectory = "Choose this directory";
    private String storageDirectoryPath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        storageDirectoryPath = Environment.getExternalStorageDirectory().getPath();
        workingDir = new File(storageDirectoryPath);
        fill(workingDir);
    }

    private void fill(File workingDir) {

        File[]folder = workingDir.listFiles();
        this.setTitle("Current directory: " + workingDir.getName());
        List<MenuObj>dirs = new ArrayList<MenuObj>();

        try {
            for (File f: folder){
                // Sets the last modified date for each file and sub directory in current directory
                String formatedDate = "";

                // If there is a sub directory it displays number of containing files
                if (f.isDirectory() && !f.isHidden()){
                    File[] subDir = f.listFiles();
                    int amount = 0;
                    if (subDir != null){
                        amount = subDir.length;
                    }
                    String numItem = String.valueOf(amount);
                    if (amount <= 1) {
                        numItem = numItem + " item";
                    } else {
                        numItem = numItem + " items";
                    }

                    // Creates a directory MenuObj
                    dirs.add(new MenuObj(f.getName(), numItem, formatedDate, f.getAbsolutePath(), "directory_icon"));

                }
            }
        } catch (Exception e){

        }

        // Sort the list of folders
        Collections.sort(dirs);

        // If working directory isn't in root, append a MenuObj for user to select this directory
        // and another MenuObj to be able to navigate to parent directory
        if (!workingDir.getPath().equalsIgnoreCase(storageDirectoryPath)) {
            dirs.add(0, new MenuObj(setDirectory, "", "", workingDir.getParent(), "directory_pick"));
            dirs.add(1, new MenuObj("..", "Parent directory", "", workingDir.getParent(), "directory_up"));
        }
        adapter = new FileArrayAdapter(DirectoryChooser.this, R.layout.instrument_fragment, R.layout.file_explorer, dirs);
        this.setListAdapter(adapter);
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        Log.d("DirectoryChooser", "position:" + position);
        MenuObj obj = adapter.getItem(position);
        if (obj.getIcon().equalsIgnoreCase("directory_icon") || obj.getIcon().equalsIgnoreCase("directory_up")){
            workingDir = new File(obj.getPath());
            fill(workingDir);
        } else if (obj.getName().equals(setDirectory)) {
            onFileClick(obj);
        }
    }

    private void onFileClick(MenuObj obj) {
        Intent intent = new Intent();
        intent.putExtra(FOLDER_PATH, workingDir.toString());
        setResult(RESULT_OK, intent);
        finish();
    }
}
