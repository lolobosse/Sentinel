package de.tum.in.i22.sentinel.android.app.playstore;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import de.tum.in.i22.sentinel.android.app.R;
import de.tum.in.i22.sentinel.android.app.fragment.PlaystoreFragment;

public class PlayStoreFocusable extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.playstore_onclick);

        String title = getIntent().getStringExtra(PlaystoreFragment.PACKAGE_TEXT_FOCUSED);
        Bitmap bitmap = getIntent().getParcelableExtra(PlaystoreFragment.PACKAGE_IMAGE_FOCUSED);

        TextView titleTextView = (TextView) findViewById(R.id.packageName_focused);
        titleTextView.setText(title);

        ImageView imageView = (ImageView) findViewById(R.id.packageView_focused);
        imageView.setImageBitmap(bitmap);
    }
}
