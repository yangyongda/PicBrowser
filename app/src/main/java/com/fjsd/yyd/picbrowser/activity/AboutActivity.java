package com.fjsd.yyd.picbrowser.activity;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ScrollView;
import android.widget.TextView;

import com.fjsd.yyd.picbrowser.BuildConfig;
import com.fjsd.yyd.picbrowser.R;
import com.fjsd.yyd.picbrowser.util.CustomTabService;
import com.mikepenz.google_material_typeface_library.GoogleMaterial;
import com.mikepenz.iconics.IconicsDrawable;

public class AboutActivity extends ThemedActivity {

    private Toolbar toolbar;

    /**** CustomTabService*/
    private CustomTabService cts;

    /**** Scroll View*/
    private ScrollView scr;

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        toolbar = (Toolbar) findViewById(R.id.toolbar);

        setNavBarColor();
        cts = new CustomTabService(AboutActivity.this,getPrimaryColor());

    }

    @Override
    public void onPostResume() {
        super.onPostResume();
        setTheme();
    }

    private void setTheme(){
        /**** ToolBar *****/
        toolbar.setBackgroundColor(getPrimaryColor());
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(
                new IconicsDrawable(this)
                        .icon(GoogleMaterial.Icon.gmd_arrow_back)
                        .color(Color.WHITE)
                        .sizeDp(19));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        /**** Status Bar ****/
        setStatusBarColor();

        /**** Nav Bar *******/
        setNavBarColor();

        /**** Recent App ****/
        setRecentApp(getString(R.string.about));

        /**** Title Cards ***/
        int color=getAccentColor();
        ((TextView) findViewById(R.id.about_app_title)).setTextColor(color);

        /***** ScrolView *****/
        setScrollViewColor(scr);

        setThemeOnChangeListener();
    }


    private void setThemeOnChangeListener(){

        /** BackGround **/
        findViewById(R.id.about_background).setBackgroundColor(getBackgroundColor());

        /** Cards **/
        int color = getCardBackgroundColor();
        ((CardView) findViewById(R.id.about_app_card)).setCardBackgroundColor(color);

        /** TextViews **/
        color = getTextColor();
        ((TextView) findViewById(R.id.about_app_light_description)).setTextColor(color);

        /** Sub Text Views**/
        color = getSubTextColor();
        ((TextView) findViewById(R.id.about_version_item_sub)).setTextColor(color);
        ((TextView) findViewById(R.id.about_version_item_sub)).setText(BuildConfig.VERSION_NAME); //版本

    }

}
