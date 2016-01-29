package de.tum.in.i22.sentinel.android.app.file_explorer;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import de.tum.in.i22.sentinel.android.app.R;

/**
 * Created by Moderbord on 2016-01-06.
 * Generic class to display files and directories. It is used by both ListView of {@see de.tum.in.i22.sentinel.android.app.file_explorer.DirectoryChooser}
 * and {@see de.tum.in.i22.sentinel.android.app.file_explorer.FileChooser}
 */
public class FileArrayAdapter extends ArrayAdapter<MenuObj> {

    private final String DRAWABLE_IDENTIFIER = "drawable/";


    private Context c;
    private int id;
    private List<MenuObj> objects;

    public FileArrayAdapter(Context context, int resource, int textViewResourceId, List<MenuObj> objects) {
        super(context, resource, textViewResourceId, objects);
        this.c = context;
        this.id = textViewResourceId;
        this.objects = objects;
    }

    /**
     * Inherited method for creating the row view. It makes the difference between files and folders
     * @param position: which position will be drawn
     * @param convertView: the view which could be recycled
     * @param parent: the parent view containing all these subviews
     * @return: The row view
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Saves memory <- inflates only if needed
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(id, null);
        }

        final MenuObj obj = objects.get(position);
        if (obj != null) {
            // Finds the views
            TextView textName = (TextView) convertView.findViewById(R.id.TextViewName);
            TextView textSize = (TextView) convertView.findViewById(R.id.TextViewSize);
            TextView textDate = (TextView) convertView.findViewById(R.id.TextViewDate);

            ImageView icon = (ImageView) convertView.findViewById(R.id.Icon);
            String uri = DRAWABLE_IDENTIFIER + obj.getIcon();

            int imageResource = c.getResources().getIdentifier(uri, null, c.getPackageName());
            Drawable image = null;
            // To have no deprecated code
            try {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    image = c.getResources().getDrawable(imageResource, null);
                } else {
                    image = c.getResources().getDrawable(imageResource);
                }
                icon.setImageDrawable(image);
            } catch (Resources.NotFoundException e) {
                Log.d("FileArrayAdapter", "No valid icon resource was found");
            }

            if (textName != null) {
                textName.setText(String.valueOf(obj.getName()));
            }
            if (textSize != null) {
                textSize.setText(String.valueOf(obj.getData()));
            }
            if (textDate != null) {
                textDate.setText(String.valueOf(obj.getDate()));
            }

        }
        return convertView;
    }
}
