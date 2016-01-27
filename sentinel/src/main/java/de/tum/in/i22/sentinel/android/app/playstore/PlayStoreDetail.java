package de.tum.in.i22.sentinel.android.app.playstore;

import android.app.Activity;
import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.koushikdutta.async.http.AsyncHttpClient;
import com.koushikdutta.async.http.AsyncHttpResponse;
import com.squareup.picasso.Picasso;

import java.io.File;

import de.tum.in.i22.sentinel.android.app.Constants;
import de.tum.in.i22.sentinel.android.app.R;
import de.tum.in.i22.sentinel.android.app.backend.APKReceiver;
import de.tum.in.i22.sentinel.android.app.backend.APKUtils;
import de.tum.in.i22.sentinel.android.app.fragment.PlaystoreFragment;

public class PlayStoreDetail extends Activity implements View.OnClickListener {

    private ImageView packageView;
    private TextView packageName;
    private TextView description;
    private TextView license;
    private TextView category;
    private TextView web;
    private TextView source;
    private TextView size;
    private TextView permissions;
    private TextView features;
    private TextView permissionsLabel;
    private TextView featuresLabel;

    private PlaystoreFragment.ServerPackageInformation spi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.playstore_detail_layout);

        packageView = (ImageView) findViewById(R.id.packageView);
        packageName = (TextView) findViewById(R.id.packageName);
        description = (TextView) findViewById(R.id.description);
        license = (TextView) findViewById(R.id.license);
        category = (TextView) findViewById(R.id.category);
        web = (TextView) findViewById(R.id.web);
        source = (TextView) findViewById(R.id.source);
        size = (TextView) findViewById(R.id.size);
        permissions = (TextView) findViewById(R.id.permissions);
        permissionsLabel = (TextView) findViewById(R.id.permissionsLabel);
        features = (TextView) findViewById(R.id.features);
        featuresLabel = (TextView) findViewById(R.id.featuresLabel);

        findViewById(R.id.install).setOnClickListener(this);

        // Implementation
        Gson g = new Gson();
        spi = g.fromJson(getIntent().getStringExtra(Constants.DETAILS_TO_DISPLAY_KEY), PlaystoreFragment.ServerPackageInformation.class);
        Picasso.with(this).load(spi.logoUrl).into(packageView);
        packageName.setText(spi.appName);
        if (!TextUtils.isEmpty(spi.description)) {
            description.setText(Html.fromHtml(spi.description));
        } else {
            description.setVisibility(View.GONE);
        }
        if (!TextUtils.isEmpty(spi.license)) {
            license.setText(license.getText().toString().replace("@!", spi.license));
        } else {
            license.setVisibility(View.GONE);
        }
        if (!TextUtils.isEmpty(spi.appCategory)) {
            category.setText(category.getText().toString().replace("@!", spi.appCategory));
        } else {
            category.setVisibility(View.GONE);
        }
        if (!TextUtils.isEmpty(spi.webLink)) {
            web.setText(web.getText().toString().replace("@!", spi.webLink));
        } else {
            web.setVisibility(View.GONE);
        }
        if (!TextUtils.isEmpty(spi.sourceCodeLink)) {
            source.setText(license.getText().toString().replace("@!", spi.sourceCodeLink));
        } else {
            source.setVisibility(View.GONE);
        }
        if (spi.size != 0) {
            size.setText(size.getText().toString().replace("@!", String.valueOf(spi.size)));
        } else {
            size.setVisibility(View.GONE);
        }
        if (!TextUtils.isEmpty(spi.permissions)) {
            permissions.setText(toList(spi.permissions));
        } else {
            permissions.setVisibility(View.GONE);
            permissionsLabel.setVisibility(View.GONE);
        }
        if (!TextUtils.isEmpty(spi.features)) {
            features.setText(toList(spi.features));
        } else {
            features.setVisibility(View.GONE);
            featuresLabel.setVisibility(View.GONE);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.install:
                APKReceiver.getInstance().getFileFromDownloadUrl(spi.downloadUrl, new AsyncHttpClient.FileCallback() {
                    @Override
                    public void onCompleted(Exception e, AsyncHttpResponse source, File result) {
                        // If the server returns a 200 status => the app has been successfully
                        // instrumented and is successfully returned
                        if (e == null && source.code() == 200) {
                            if (APKUtils.isInstalled(PlayStoreDetail.this, spi.packageName)) {
                                APKReceiver.getInstance().uninstallApk(PlayStoreDetail.this, spi.packageName);
                            } else {
                                APKReceiver.getInstance().installApk(PlayStoreDetail.this, result.getAbsolutePath());
                            }
                        } else {
                            Toast.makeText(PlayStoreDetail.this, "Failed", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                break;
        }
    }

    /**
     * Takes a csv String and returns the same but with line breaks.
     *
     * @param s: the list to be splitted
     * @return Line Break list
     */
    private String toList(String s) {
        return s.replaceAll(",", "\n");
    }
}

