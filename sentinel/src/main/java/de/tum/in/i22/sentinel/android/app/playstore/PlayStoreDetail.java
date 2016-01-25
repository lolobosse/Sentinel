package de.tum.in.i22.sentinel.android.app.playstore;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import de.tum.in.i22.sentinel.android.app.Constants;
import de.tum.in.i22.sentinel.android.app.R;

public class PlayStoreDetail extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.playstore_onclick);

        String title = getIntent().getStringExtra(Constants.PACKAGE_TEXT_PLAY_STORE_DETAIL);
        String logoUrl = getIntent().getStringExtra(Constants.PACKAGE_IMAGE_PLAY_STORE_DETAIL);

        TextView titleTextView = (TextView) findViewById(R.id.packageName_focused);
        titleTextView.setText(title);

        ImageView imageView = (ImageView) findViewById(R.id.packageView_focused);
        Picasso.with(this).load(logoUrl).into(imageView);
    }
}
