package de.tum.in.i22.sentinel.android.app.file_explorer;

import android.content.Context;
import android.graphics.drawable.Drawable;
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
 */
public class FileArrayAdapter extends ArrayAdapter<MenuObj> {

    private Context c;
    private int id;
    private List<MenuObj>objects;

    public FileArrayAdapter(Context context, int resource, int textViewResourceId, List<MenuObj> objects) {
        super(context, resource, textViewResourceId, objects);

        this.c = context;
        this.id = textViewResourceId;
        this.objects = objects;
    }

    public MenuObj getObj(int i){
        return objects.get(i);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;

        if (v == null){
            LayoutInflater inflater = (LayoutInflater)c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = inflater.inflate(id, null);
        }

        /* create a new view of my layout and inflate it in the row */
        //convertView = ( RelativeLayout ) inflater.inflate( resource, null );

        final MenuObj obj = objects.get(position);
        if (obj != null){
            // Finds the views
            TextView textName = (TextView) v.findViewById(R.id.TextViewName);
            TextView textSize = (TextView) v.findViewById(R.id.TextViewSize);
            TextView textDate = (TextView) v.findViewById(R.id.TextViewDate);

            ImageView icon = (ImageView) v.findViewById(R.id.Icon);
            String uri = "drawable/" + obj.getIcon();

            int imageResource = c.getResources().getIdentifier(uri, null, c.getPackageName());
            Drawable image = c.getResources().getDrawable(imageResource, null);
            icon.setImageDrawable(image);

            if (textName != null){
                textName.setText(String.valueOf(obj.getName()));
            }
            if (textSize != null){
                textSize.setText(String.valueOf(obj.getData()));
            }
            if (textDate != null){
                textDate.setText(String.valueOf(obj.getDate()));
            }

        }
    return v;
    }
}
