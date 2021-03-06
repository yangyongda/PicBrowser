package com.fjsd.yyd.picbrowser.util;

import android.content.Context;
import android.graphics.PorterDuff;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.fjsd.yyd.picbrowser.R;
import com.fjsd.yyd.picbrowser.activity.ThemedActivity;

/**
 * Created by Administrator on 2017/6/10 0010.
 */

public class SecurityHelper {
    private boolean activeSecurity;
    private boolean passwordOnDelete;
    private boolean passwordOnHidden;
    private String passwordValue;

    private Context context;
    public SecurityHelper(Context context){
        this.context = context;
        updateSecuritySetting();
    }

    public boolean isActiveSecurity(){return activeSecurity;}
    public boolean isPasswordOnHidden(){return passwordOnHidden;}
    public boolean isPasswordOnDelete(){return passwordOnDelete;}

    //检查是否设置了安全并且密码是否正常
    public boolean checkPassword(String pass){
        return (isActiveSecurity() && pass.equals(passwordValue));
    }
    //获取保存的加密状态及密码
    public void updateSecuritySetting(){
        PreferenceUtil SP = PreferenceUtil.getInstance(context);
        this.activeSecurity = SP.getBoolean(context.getString(R.string.preference_use_password), false);
        this.passwordOnDelete = SP.getBoolean(context.getString(R.string.preference_use_password_on_delete), false);
        this.passwordOnHidden = SP.getBoolean(context.getString(R.string.preference_use_password_on_hidden), true);
        this.passwordValue = SP.getString(context.getString(R.string.preference_password_value), "");
    }
    //显示输入密码的对话框
    public EditText getInsertPasswordDialog(final ThemedActivity activity, AlertDialog.Builder passwordDialog){

        final View PasswordDialogLayout = activity.getLayoutInflater().inflate(R.layout.dialog_password, null);
        final TextView passwordDialogTitle = (TextView) PasswordDialogLayout.findViewById(R.id.password_dialog_title);
        final CardView passwordDialogCard = (CardView) PasswordDialogLayout.findViewById(R.id.password_dialog_card);
        final EditText editxtPassword = (EditText) PasswordDialogLayout.findViewById(R.id.password_edittxt);

        passwordDialogTitle.setBackgroundColor(activity.getPrimaryColor());
        passwordDialogCard.setBackgroundColor(activity.getCardBackgroundColor());
        ThemeHelper.setCursorDrawableColor(editxtPassword, activity.getTextColor());
        editxtPassword.getBackground().mutate().setColorFilter(activity.getTextColor(), PorterDuff.Mode.SRC_ATOP);
        editxtPassword.setTextColor(activity.getTextColor());
        passwordDialog.setView(PasswordDialogLayout);
        return editxtPassword;
    }
}
