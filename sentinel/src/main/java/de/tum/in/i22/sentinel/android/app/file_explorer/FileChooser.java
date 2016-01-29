package de.tum.in.i22.sentinel.android.app.file_explorer;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import java.io.File;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import de.tum.in.i22.sentinel.android.app.Constants;
import de.tum.in.i22.sentinel.android.app.R;

/**
 * Created by Moderbord on 2016-01-06.
 * Class responsible for displaying the directories AND the files and allows to pickup ONLY files.
 * (Mandatory) Param to send in the {@see Intent}: "extension" : "the extension file you allow the user to pickup i.e. \".xml\""
 */
public class FileChooser extends ListActivity {

    private File workingDir;
    private FileArrayAdapter adapter;
    private String fileExt;
    private String storageDirectoryPath;

    /**
     * Retrieves and assigns the extension we're looking for
     * Fires the view creation methods
     * @param savedInstanceState: not used, inherited
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fileExt = getIntent().getStringExtra(Constants.EXTENSION);
        storageDirectoryPath = Environment.getExternalStorageDirectory().getPath();
        workingDir = new File(storageDirectoryPath);
        fill(workingDir);
    }

    /**
     * Fills the view with all available files and folders available in the current #workingDir
     * @param workingDir: where the explorer is.
     */
    private void fill(File workingDir) {

        File[] folder = workingDir.listFiles();
        this.setTitle("Current directory: " + workingDir.getName());
        List<MenuObj> dirs = new ArrayList<MenuObj>();
        List<MenuObj> files = new ArrayList<MenuObj>();

        try {
            // Look for every file in the current folder in order to display them
            for (File f : folder) {
                // Sets the last modified date in the directory
                Date modifiedDate = new Date(f.lastModified());
                DateFormat formatter = DateFormat.getDateTimeInstance();
                String formattedDate = formatter.format(modifiedDate);

                // If the current file is a non-hidden directory, we display the number it contains
                if (f.isDirectory() && !f.isHidden()) {
                    File[] subDir = f.listFiles();
                    int amount = 0;
                    if (subDir != null) {
                        amount = subDir.length;
                    }
                    String numItem = String.valueOf(amount);
                    // Singular/Plural
                    if (amount <= 1) {
                        numItem = numItem + " item";
                    } else {
                        numItem = numItem + " items";
                    }

                    // Creates a directory MenuObj
                    dirs.add(new MenuObj(f.getName(), numItem, formattedDate, f.getAbsolutePath(), Constants.DIRECTORY_ICON));

                    // Only displayed if not hidden
                } else if (!f.isHidden()) {
                    // Creates a file MenuObj
                    files.add(new MenuObj(f.getName(), f.length() / 1000 + " kB", formattedDate, f.getAbsolutePath(), Constants.FILE_ICON));
                }
            }
        } catch (Exception e) {

        }

        // Sort both lists in alphabetical order and then appends all MenuObj's in 'files' to 'dirs'
        // This makes all files appear below folders
        Collections.sort(dirs);
        Collections.sort(files);
        dirs.addAll(files);

        // If working directory isn't in root, append an MenuObj for user to navigate to parent directory
        if (!workingDir.getPath().equalsIgnoreCase(storageDirectoryPath)) {
            dirs.add(0, new MenuObj("..", Constants.PARENT_DIRECTORY, "", workingDir.getParent(), Constants.DIRECTORY_UP));
        }
        // Create the adapter with all the data we just collected
        adapter = new FileArrayAdapter(FileChooser.this, R.layout.instrument_fragment, R.layout.file_explorer, dirs);
        // Set it to the list view
        this.setListAdapter(adapter);
    }

    /**
     * Inherited method to handle item click events
     * We have 3 options:
     *  - It is a folder, so we do a recursive call to display its content
     *  - It is a file with the extension we were looking for, we return with OK
     *  - It is a file with a different file extension, we give visual feedback to the user ({@see android.widget.Toast})
     * @param l: listView being clicked
     * @param v: View clicked
     * @param position: position of the view in the listView
     * @param id: id of the view
     */
    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);

        MenuObj obj = adapter.getItem(position);
        if (obj.getState() == MenuObj.STATE.FOLDER) {
            // Display the content of this folder
            workingDir = new File(obj.getPath());
            fill(workingDir);
        } else if (!obj.getName().endsWith(fileExt)) {
            Toast.makeText(this, "Only " + fileExt + " is allowed", Toast.LENGTH_SHORT).show();
        } else {
            onFileClick(obj);
        }

    }

    /**
     * Return to the previous activity with result ok and the path to the selected file
     * @param obj: the Pseudo object containing the selected file
     */
    private void onFileClick(MenuObj obj) {
        Intent intent = new Intent();
        intent.putExtra(Constants.ABSOLUTE_PATH, obj.getPath());
        setResult(RESULT_OK, intent);
        // Close the file finder activity
        finish();
    }
}
