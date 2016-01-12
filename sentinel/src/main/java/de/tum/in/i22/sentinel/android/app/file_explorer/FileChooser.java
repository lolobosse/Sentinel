package de.tum.in.i22.sentinel.android.app.file_explorer;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
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
public class FileChooser extends ListActivity {

    private File workingDir;
    private FileArrayAdapter adapter;
    private String fileExt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fileExt = getIntent().getStringExtra("extension");
        workingDir = new File("/sdcard/");
        fill(workingDir);
    }

    private void fill(File workingDir) {

        File[]folder = workingDir.listFiles();
        this.setTitle("Current directory: " + workingDir.getName());
        List<MenuObj>dirs = new ArrayList<MenuObj>();
        List<MenuObj>files = new ArrayList<MenuObj>();

        try {
            for (File f: folder){
                // Sets the last modified date for each file and sub directory in current directory
                Date modifiedDate = new Date(f.lastModified());
                DateFormat formater = DateFormat.getDateTimeInstance();
                String formatedDate = formater.format(modifiedDate);

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

                } else  if (!f.isHidden()){
                    // Creates a file MenuObj
                    files.add(new MenuObj(f.getName(), f.length() / 1000 + " kB", formatedDate, f.getAbsolutePath(), "file_icon"));
                }
            }
        } catch (Exception e){

        }

        // Sort both lists and then appends all MenuObj's in 'files' to 'dirs' which should appears below directories
        Collections.sort(dirs);
        Collections.sort(files);
        dirs.addAll(files);

        // If working directory isn't in root, append an MenuObj for user to navigate to parent directory
        if (!workingDir.getName().equalsIgnoreCase("sdcard")) {
            dirs.add(0, new MenuObj("..", "Parent directory", "", workingDir.getParent(), "directory_up"));
        }
        adapter = new FileArrayAdapter(FileChooser.this, R.layout.instrument_fragment, R.layout.file_explorer, dirs);
        this.setListAdapter(adapter);
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);

        MenuObj obj = adapter.getItem(position);
        if (obj.getIcon().equalsIgnoreCase("directory_icon") || obj.getIcon().equalsIgnoreCase("directory_up")){
            workingDir = new File(obj.getPath());
            fill(workingDir);
        } else if (!obj.getName().endsWith(fileExt)) {
            Toast.makeText(this, "Only " + fileExt + " is allowed", Toast.LENGTH_SHORT).show();
        } else {
            onFileClick(obj);
        }

    }

    private void onFileClick(MenuObj obj) {
        Intent intent = new Intent();
        intent.putExtra("GetAbsolutePath", obj.getPath());
        intent.putExtra("GetPath", workingDir.toString());
        intent.putExtra("GetFileName", obj.getName());
        setResult(RESULT_OK, intent);
        finish();
    }
}
