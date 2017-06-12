package com.fjsd.yyd.picbrowser.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.SwitchCompat;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.fjsd.yyd.picbrowser.MyApplication;
import com.fjsd.yyd.picbrowser.R;
import com.fjsd.yyd.picbrowser.util.ColorPalette;
import com.fjsd.yyd.picbrowser.util.PreferenceUtil;
import com.fjsd.yyd.picbrowser.util.SecurityHelper;
import com.fjsd.yyd.picbrowser.util.ThemeHelper;
import com.mikepenz.community_material_typeface_library.CommunityMaterial;
import com.mikepenz.iconics.IconicsDrawable;
import com.mikepenz.iconics.view.IconicsImageView;

import uz.shift.colorpicker.LineColorPicker;
import uz.shift.colorpicker.OnColorChangedListener;

import static com.fjsd.yyd.picbrowser.util.ThemeHelper.AMOLED_THEME;
import static com.fjsd.yyd.picbrowser.util.ThemeHelper.DARK_THEME;
import static com.fjsd.yyd.picbrowser.util.ThemeHelper.LIGHT_THEME;

@SuppressWarnings("ResourceAsColor")
public class SettingsActivity extends ThemedActivity {

    private PreferenceUtil SP;
    private SecurityHelper securityObj;

    private Toolbar toolbar;
    private ScrollView scr;

    private TextView txtGT;
    private TextView txtTT;
    private TextView txtPT;
    private TextView txtVT;


    private SwitchCompat swStatusBar;
    private SwitchCompat swMaxLuminosity;
    private SwitchCompat swPictureOrientation;
    private SwitchCompat swInternalBrowser;
    private SwitchCompat swIncludeVideo;
    private SwitchCompat swShowFab;


    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        SP = PreferenceUtil.getInstance(getApplicationContext());

        securityObj = new SecurityHelper(SettingsActivity.this);

        txtTT = (TextView) findViewById(R.id.theme_setting_title);
        txtGT = (TextView) findViewById(R.id.general_setting_title);
        txtPT = (TextView) findViewById(R.id.picture_setting_title);
        txtVT = (TextView) findViewById(R.id.video_setting_title);

        scr = (ScrollView)findViewById(R.id.settingAct_scrollView);

        /*** BASIC THEME ***/
        findViewById(R.id.ll_basic_theme).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                baseThemeDialog();
            }
        });

        /*** SECURITY ***/
        findViewById(R.id.ll_security).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!securityObj.isActiveSecurity())
                    startActivity(new Intent(getApplicationContext(), SecurityActivity.class));
                else
                    askPasswordDialog();

            }
        });

        /*** PRIMARY COLOR PIKER ***/
        findViewById(R.id.ll_primaryColor).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                primaryColorPiker();
            }
        });

        /*** ACCENT COLOR PIKER ***/
        findViewById(R.id.ll_accentColor).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                accentColorPiker();
            }
        });

        /*** EXCLUDED ALBUMS INTENT ***/
        findViewById(R.id.ll_excluded_album).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SettingsActivity.this, ExcludedAlbumsActivity.class));
            }
        });

        /*** CUSTOMIZE PICTURE VIEWER DIALOG ***/
        findViewById(R.id.ll_custom_thirdAct).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                customizePictureViewer();
            }
        });

        /*** SW Show Fab ***/
        swShowFab = (SwitchCompat) findViewById(R.id.sw_show_fab);
        swShowFab.setChecked(SP.getBoolean(getString(R.string.preference_show_fab), false));
        swShowFab.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                SP.putBoolean(getString(R.string.preference_show_fab), isChecked);
                updateSwitchColor(swShowFab, getAccentColor());
            }
        });

        /*** SW Internal Player ***/
        swInternalBrowser = (SwitchCompat) findViewById(R.id.set_internal_player);
        swInternalBrowser.setChecked(SP.getBoolean(getString(R.string.preference_internal_player), false));
        swInternalBrowser.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                SP.putBoolean(getString(R.string.preference_internal_player), isChecked);
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        ((MyApplication) getApplicationContext()).updateAlbums();
                    }
                }).start();
                updateSwitchColor(swInternalBrowser, getAccentColor());
            }
        });

        /*** SW INCLUDE VIDEO ***/
        swIncludeVideo = (SwitchCompat) findViewById(R.id.set_include_video);
        swIncludeVideo.setChecked(SP.getBoolean(getString(R.string.preference_include_video), true));
        swIncludeVideo.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                SP.putBoolean(getString(R.string.preference_include_video), isChecked);
                updateSwitchColor(swIncludeVideo, getAccentColor());
            }
        });

        /*** SW PICTURE ORIENTATION ***/
        swPictureOrientation = (SwitchCompat) findViewById(R.id.set_picture_orientation);
        swPictureOrientation.setChecked(SP.getBoolean(getString(R.string.preference_auto_rotate), false));
        swPictureOrientation.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                SP.putBoolean(getString(R.string.preference_auto_rotate), isChecked);
                updateSwitchColor(swPictureOrientation, getAccentColor());
            }
        });

        /*** SW MAX LUMINOSITY ***/
        swMaxLuminosity = (SwitchCompat) findViewById(R.id.set_max_luminosity);
        swMaxLuminosity.setChecked(SP.getBoolean(getString(R.string.preference_max_brightness), false));
        swMaxLuminosity.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                SP.putBoolean(getString(R.string.preference_max_brightness), isChecked);
                updateSwitchColor(swMaxLuminosity, getAccentColor());
            }
        });


        /*** SW TRANSLUCENT STATUS BAR ***/
        swStatusBar = (SwitchCompat) findViewById(R.id.SetTraslucentStatusBar);
        swStatusBar.setChecked(SP.getBoolean(getString(R.string.preference_translucent_status_bar), true));
        swStatusBar.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                SP.putBoolean(getString(R.string.preference_translucent_status_bar), isChecked);
                updateTheme();
                setStatusBarColor();
                updateSwitchColor(swStatusBar, getAccentColor());
            }
        });
    }


    private void askPasswordDialog() {
        AlertDialog.Builder passwordDialogBuilder = new AlertDialog.Builder(SettingsActivity.this, getDialogStyle());
        final EditText editTextPassword  = securityObj.getInsertPasswordDialog(SettingsActivity.this,passwordDialogBuilder);
        passwordDialogBuilder.setNegativeButton(getString(R.string.cancel).toUpperCase(), null);

        passwordDialogBuilder.setPositiveButton(getString(R.string.ok_action).toUpperCase(), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //This should br empty it will be overwrite later
                //to avoid dismiss of the dialog on wrong password
            }
        });

        final AlertDialog passwordDialog = passwordDialogBuilder.create();
        passwordDialog.show();

        passwordDialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (securityObj.checkPassword(editTextPassword.getText().toString())) {
                    passwordDialog.dismiss();
                    startActivity(new Intent(getApplicationContext(), SecurityActivity.class));
                } else {
                    Toast.makeText(getApplicationContext(), R.string.wrong_password, Toast.LENGTH_SHORT).show();
                    editTextPassword.getText().clear();
                    editTextPassword.requestFocus();
                }
            }
        });
    }


    private void baseThemeDialog(){
        final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(SettingsActivity.this, getDialogStyle());

        final View dialogLayout = getLayoutInflater().inflate(R.layout.dialog_basic_theme, null);
        final TextView dialogTitle = (TextView) dialogLayout.findViewById(R.id.basic_theme_title);
        final CardView dialogCardView = (CardView) dialogLayout.findViewById(R.id.basic_theme_card);

        final IconicsImageView whiteSelect = (IconicsImageView) dialogLayout.findViewById(R.id.white_basic_theme_select);
        final IconicsImageView darkSelect = (IconicsImageView) dialogLayout.findViewById(R.id.dark_basic_theme_select);
        final IconicsImageView darkAmoledSelect = (IconicsImageView) dialogLayout.findViewById(R.id.dark_amoled_basic_theme_select);

        switch (getBaseTheme()){
            case LIGHT_THEME:
                whiteSelect.setVisibility(View.VISIBLE);
                darkSelect.setVisibility(View.GONE);
                darkAmoledSelect.setVisibility(View.GONE);
                break;
            case DARK_THEME:
                whiteSelect.setVisibility(View.GONE);
                darkSelect.setVisibility(View.VISIBLE);
                darkAmoledSelect.setVisibility(View.GONE);
                break;
            case AMOLED_THEME:
                whiteSelect.setVisibility(View.GONE);
                darkSelect.setVisibility(View.GONE);
                darkAmoledSelect.setVisibility(View.VISIBLE);
                break;
        }

        /** SET OBJ THEME **/
        dialogTitle.setBackgroundColor(getPrimaryColor());
        dialogCardView.setCardBackgroundColor(getCardBackgroundColor());

        dialogLayout.findViewById(R.id.ll_white_basic_theme).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                whiteSelect.setVisibility(View.VISIBLE);
                darkSelect.setVisibility(View.GONE);
                darkAmoledSelect.setVisibility(View.GONE);
                setBaseTheme(LIGHT_THEME, false);
                //dialogCardView.setCardBackgroundColor(getCardBackgroundColor());
                //setTheme();

            }
        });
        dialogLayout.findViewById(R.id.ll_dark_basic_theme).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                whiteSelect.setVisibility(View.GONE);
                darkSelect.setVisibility(View.VISIBLE);
                darkAmoledSelect.setVisibility(View.GONE);
                setBaseTheme(DARK_THEME, false);
                //dialogCardView.setCardBackgroundColor(getCardBackgroundColor());
                //setTheme();
            }
        });
        dialogLayout.findViewById(R.id.ll_dark_amoled_basic_theme).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                whiteSelect.setVisibility(View.GONE);
                darkSelect.setVisibility(View.GONE);
                darkAmoledSelect.setVisibility(View.VISIBLE);
                setBaseTheme(AMOLED_THEME, false);
                //dialogCardView.setCardBackgroundColor(getCardBackgroundColor());
                //setTheme();

            }
        });
        dialogBuilder.setView(dialogLayout);
        dialogBuilder.setPositiveButton(getString(R.string.ok_action).toUpperCase(), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                SP.putInt(getString(R.string.preference_base_theme), getBaseTheme());
                setTheme();
            }
        });
        dialogBuilder.setNegativeButton(getString(R.string.cancel).toUpperCase(), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                setBaseTheme(ThemeHelper.getBaseTheme(getApplicationContext()), false);
                setTheme();
            }
        });
        dialogBuilder.setView(dialogLayout);
        dialogBuilder.show();
    }

    private void primaryColorPiker(){
        final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(SettingsActivity.this, getDialogStyle());

        final View dialogLayout = getLayoutInflater().inflate(R.layout.color_piker_primary, null);
        final LineColorPicker colorPicker = (LineColorPicker) dialogLayout.findViewById(R.id.color_picker_primary);
        final LineColorPicker colorPicker2 = (LineColorPicker) dialogLayout.findViewById(R.id.color_picker_primary_2);
        final TextView dialogTitle = (TextView) dialogLayout.findViewById(R.id.cp_primary_title);
        CardView dialogCardView = (CardView) dialogLayout.findViewById(R.id.cp_primary_card);
        dialogCardView.setCardBackgroundColor(getCardBackgroundColor());

        colorPicker.setColors(ColorPalette.getBaseColors(getApplicationContext()));
        for (int i : colorPicker.getColors())
            for (int i2 : ColorPalette.getColors(getBaseContext(), i))
                if (i2 == getPrimaryColor()) {
                    colorPicker.setSelectedColor(i);
                    colorPicker2.setColors(ColorPalette.getColors(getBaseContext(), i));
                    colorPicker2.setSelectedColor(i2);
                    break;}

        dialogTitle.setBackgroundColor(getPrimaryColor());

        colorPicker.setOnColorChangedListener(new OnColorChangedListener() {
            @Override
            public void onColorChanged(int c) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    if (isTranslucentStatusBar()) {
                        getWindow().setStatusBarColor(ColorPalette.getObscuredColor(getPrimaryColor()));
                    } else getWindow().setStatusBarColor(c);
                }

                toolbar.setBackgroundColor(c);
                dialogTitle.setBackgroundColor(c);
                colorPicker2.setColors(ColorPalette.getColors(getApplicationContext(), colorPicker.getColor()));
                colorPicker2.setSelectedColor(colorPicker.getColor());
            }
        });
        colorPicker2.setOnColorChangedListener(new OnColorChangedListener() {
            @Override
            public void onColorChanged(int c) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    if (isTranslucentStatusBar()) {
                        getWindow().setStatusBarColor(ColorPalette.getObscuredColor(c));
                    } else getWindow().setStatusBarColor(c);
                    if (isNavigationBarColored())
                        getWindow().setNavigationBarColor(c);
                    else
                        getWindow().setNavigationBarColor(ContextCompat.getColor(getApplicationContext(), R.color.md_black_1000));
                }
                toolbar.setBackgroundColor(c);
                dialogTitle.setBackgroundColor(c);
            }
        });
        dialogBuilder.setView(dialogLayout);

        dialogBuilder.setNeutralButton(getString(R.string.cancel).toUpperCase(), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    if (isTranslucentStatusBar()) {
                        getWindow().setStatusBarColor(ColorPalette.getObscuredColor(getPrimaryColor()));
                    } else getWindow().setStatusBarColor(getPrimaryColor());
                }
                toolbar.setBackgroundColor(getPrimaryColor());
                dialog.cancel();
            }
        });

        dialogBuilder.setPositiveButton(getString(R.string.ok_action).toUpperCase(), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                SP.putInt(getString(R.string.preference_primary_color), colorPicker2.getColor());
                updateTheme();
                setNavBarColor();
                setScrollViewColor(scr);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    if (isTranslucentStatusBar()) {
                        getWindow().setStatusBarColor(ColorPalette.getObscuredColor(getPrimaryColor()));
                    } else {
                        getWindow().setStatusBarColor(getPrimaryColor());
                    }
                }
            }
        });

        dialogBuilder.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    if (isTranslucentStatusBar()) {
                        getWindow().setStatusBarColor(ColorPalette.getObscuredColor(getPrimaryColor()));
                    } else getWindow().setStatusBarColor(getPrimaryColor());
                    if (isNavigationBarColored())
                        getWindow().setNavigationBarColor(getPrimaryColor());
                    else getWindow().setNavigationBarColor(ContextCompat.getColor(getApplicationContext(), R.color.md_black_1000));
                }
                toolbar.setBackgroundColor(getPrimaryColor());

            }
        });
        dialogBuilder.show();
    }

    private void accentColorPiker(){
        final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(SettingsActivity.this, getDialogStyle());

        final View dialogLayout = getLayoutInflater().inflate(R.layout.color_piker_accent, null);
        final LineColorPicker colorPicker = (LineColorPicker) dialogLayout.findViewById(R.id.color_picker_accent);
        final TextView dialogTitle = (TextView) dialogLayout.findViewById(R.id.cp_accent_title);
        CardView cv = (CardView) dialogLayout.findViewById(R.id.cp_accent_card);
        cv.setCardBackgroundColor(getCardBackgroundColor());

        colorPicker.setColors(ColorPalette.getAccentColors(getApplicationContext()));
        colorPicker.setSelectedColor(getAccentColor());
        dialogTitle.setBackgroundColor(getAccentColor());

        colorPicker.setOnColorChangedListener(new OnColorChangedListener() {
            @Override
            public void onColorChanged(int c) {
                dialogTitle.setBackgroundColor(c);
                updateViewswithAccentColor(colorPicker.getColor());

            }
        });
        dialogBuilder.setView(dialogLayout);

        dialogBuilder.setNeutralButton(getString(R.string.cancel).toUpperCase(), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
                updateViewswithAccentColor(getAccentColor());
            }
        });
        dialogBuilder.setPositiveButton(getString(R.string.ok_action).toUpperCase(), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                SP.putInt(getString(R.string.preference_accent_color), colorPicker.getColor());
                updateTheme();
                updateViewswithAccentColor(getAccentColor());
            }
        });
        dialogBuilder.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                updateViewswithAccentColor(getAccentColor());
            }
        });
        dialogBuilder.show();
    }

    private void customizePictureViewer(){

        final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(SettingsActivity.this, getDialogStyle());

        View dialogLayout = getLayoutInflater().inflate(R.layout.dialog_media_viewer_theme, null);
        final SwitchCompat swApplyTheme_Viewer = (SwitchCompat) dialogLayout.findViewById(R.id.apply_theme_3th_act_enabled);

        ((CardView) dialogLayout.findViewById(R.id.third_act_theme_card)).setCardBackgroundColor(getCardBackgroundColor());
        dialogLayout.findViewById(R.id.third_act_theme_title).setBackgroundColor(getPrimaryColor());//or GetPrimary
        ((TextView) dialogLayout.findViewById(R.id.apply_theme_3thAct_title)).setTextColor(getTextColor());
        ((TextView) dialogLayout.findViewById(R.id.apply_theme_3thAct_title_Sub)).setTextColor(getSubTextColor());
        ((IconicsImageView) dialogLayout.findViewById(R.id.ll_apply_theme_3thAct_icon)).setColor(getIconColor());

        swApplyTheme_Viewer.setChecked(isApplyThemeOnImgAct());
        swApplyTheme_Viewer.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                updateSwitchColor(swApplyTheme_Viewer, getAccentColor());
            }
        });
        updateSwitchColor(swApplyTheme_Viewer, getAccentColor());


        final LineColorPicker transparencyColorPicker = (LineColorPicker) dialogLayout.findViewById(R.id.pickerTransparent);
        transparencyColorPicker.setColors(ColorPalette.getTransparencyShadows(getPrimaryColor()));
        transparencyColorPicker.setSelectedColor(ColorPalette.getTransparentColor(getPrimaryColor(), getTransparency()));

        /**TEXT VIEWS**/
        ((TextView) dialogLayout.findViewById(R.id.seek_bar_alpha_title)).setTextColor(getTextColor());
        ((TextView) dialogLayout.findViewById(R.id.seek_bar_alpha_title_Sub)).setTextColor(getSubTextColor());

        dialogBuilder.setView(dialogLayout);
        dialogBuilder.setNeutralButton(getString(R.string.cancel).toUpperCase(), null);
        dialogBuilder.setPositiveButton(getString(R.string.ok_action).toUpperCase(), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                SharedPreferences.Editor editor = SP.getEditor();
                editor.putBoolean(getString(R.string.preference_apply_theme_pager), swApplyTheme_Viewer.isChecked());
                int c = Color.alpha(transparencyColorPicker.getColor());
                editor.putInt(getString(R.string.preference_transparency), 255 - c);
                editor.commit();
                updateTheme();
            }
        });

        dialogBuilder.show();
    }

    private void updateViewswithAccentColor(int color){
        txtGT.setTextColor(color);
        txtTT.setTextColor(color);
        txtPT.setTextColor(color);
        txtVT.setTextColor(color);

        updateSwitchColor(swStatusBar, color);
        updateSwitchColor(swMaxLuminosity, color);
        updateSwitchColor(swPictureOrientation, color);
        updateSwitchColor(swInternalBrowser, color);
        updateSwitchColor(swIncludeVideo, color);
        updateSwitchColor(swShowFab, color);
    }

    @Override
    public void onPostResume() {
        super.onPostResume();
        setTheme();
        securityObj.updateSecuritySetting();
    }

    private void setTheme(){

        /** BackGround **/
        findViewById(R.id.setting_background).setBackgroundColor(getBackgroundColor());

        /** Cards **/
        int color = getCardBackgroundColor();
        ((CardView) findViewById(R.id.general_setting_card)).setCardBackgroundColor(color);
        ((CardView) findViewById(R.id.theme_setting_card)).setCardBackgroundColor(color);
        ((CardView) findViewById(R.id.preview_picture_setting_card)).setCardBackgroundColor(color);
        ((CardView) findViewById(R.id.video_setting_card)).setCardBackgroundColor(color);

        toolbar.setBackgroundColor(getPrimaryColor());
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(
                new IconicsDrawable(this)
                        .icon(CommunityMaterial.Icon.cmd_arrow_left)
                        .color(Color.WHITE)
                        .sizeDp(19));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        setStatusBarColor();
        setNavBarColor();
        setRecentApp(getString(R.string.settings));
        setScrollViewColor(scr);
        updateViewswithAccentColor(getAccentColor());


        /** Icons **/
        color = getIconColor();
        ((IconicsImageView) findViewById(R.id.ll_switch_picture_orientation_icon)).setColor(color);
        ((IconicsImageView) findViewById(R.id.ll_switch_max_luminosity_icon)).setColor(color);
        ((IconicsImageView) findViewById(R.id.traslucent_statusbar_icon)).setColor(color);
        ((IconicsImageView) findViewById(R.id.custom_3thact_icon)).setColor(color);
        ((IconicsImageView) findViewById(R.id.primary_color_icon)).setColor(color);
        ((IconicsImageView) findViewById(R.id.accent_color_icon)).setColor(color);
        ((IconicsImageView) findViewById(R.id.basic_theme_icon)).setColor(color);
        ((IconicsImageView) findViewById(R.id.excluded_album_icon)).setColor(color);
        ((IconicsImageView) findViewById(R.id.internal_player_Icon)).setColor(color);
        ((IconicsImageView) findViewById(R.id.internal_include_video)).setColor(color);
        ((IconicsImageView) findViewById(R.id.security_icon)).setColor(color);
        ((IconicsImageView) findViewById(R.id.fab_options_icon)).setColor(color);

        /** TextViews **/
        color = getTextColor();
        ((TextView) findViewById(R.id.max_luminosity_Item)).setTextColor(color);
        ((TextView) findViewById(R.id.picture_orientation_Item)).setTextColor(color);
        ((TextView) findViewById(R.id.custom_3thAct_title)).setTextColor(color);
        ((TextView) findViewById(R.id.Traslucent_StatusBar_Item)).setTextColor(color);
        ((TextView) findViewById(R.id.PrimaryColor_Item)).setTextColor(color);
        ((TextView) findViewById(R.id.accentColor_Item)).setTextColor(color);
        ((TextView) findViewById(R.id.basic_theme_item)).setTextColor(color);
        ((TextView) findViewById(R.id.Excluded_Album_Item_Title)).setTextColor(color);
        ((TextView) findViewById(R.id.internal_player_Item)).setTextColor(color);
        ((TextView) findViewById(R.id.include_video_Item)).setTextColor(color);
        ((TextView) findViewById(R.id.security_item_title)).setTextColor(color);
        ((TextView) findViewById(R.id.fab_options_item_title)).setTextColor(color);

        /** Sub Text Views**/
        color = getSubTextColor();
        ((TextView) findViewById(R.id.max_luminosity_Item_Sub)).setTextColor(color);
        ((TextView) findViewById(R.id.custom_3thAct_Sub)).setTextColor(color);
        ((TextView) findViewById(R.id.picture_orientation_Item_Sub)).setTextColor(color);
        ((TextView) findViewById(R.id.Traslucent_StatusBar_Item_Sub)).setTextColor(color);
        ((TextView) findViewById(R.id.PrimaryColor_Item_Sub)).setTextColor(color);
        ((TextView) findViewById(R.id.accentColor_Item_Sub)).setTextColor(color);
        ((TextView) findViewById(R.id.basic_theme_item_sub)).setTextColor(color);
        ((TextView) findViewById(R.id.Excluded_Album_Item_Title_Sub)).setTextColor(color);
        ((TextView) findViewById(R.id.internal_player_Item_Sub)).setTextColor(color);
        ((TextView) findViewById(R.id.include_video_Item_Sub)).setTextColor(color);
        ((TextView) findViewById(R.id.security_item_sub)).setTextColor(color);
        ((TextView) findViewById(R.id.fab_options_item_sub)).setTextColor(color);

    }
}

