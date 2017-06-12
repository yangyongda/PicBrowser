package com.fjsd.yyd.picbrowser.adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.fjsd.yyd.picbrowser.R;
import com.fjsd.yyd.picbrowser.data.Album;
import com.fjsd.yyd.picbrowser.data.Media;
import com.fjsd.yyd.picbrowser.util.ThemeHelper;

import java.util.ArrayList;

/**
 * Created by Administrator on 2017/6/10 0010.
 */

public class AlbumsAdapter extends RecyclerView.Adapter<AlbumsAdapter.ViewHolder> {

    private ArrayList<Album> albums;

    private View.OnClickListener mOnClickListener;
    private View.OnLongClickListener mOnLongClickListener;
    private ThemeHelper theme;

    private BitmapDrawable placeholder;

    public AlbumsAdapter(ArrayList<Album> ph, Context context) {
        albums = ph;
        theme = new ThemeHelper(context);
        updateTheme();
    }

    public void updateTheme() {
        theme.updateTheme();
        placeholder = ((BitmapDrawable) theme.getPlaceHolder());
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_album, parent, false);
        v.setOnClickListener(mOnClickListener);
        v.setOnLongClickListener(mOnLongClickListener);
        return new ViewHolder(v);
    }
    //填充数据
    @Override
    public void onBindViewHolder(final AlbumsAdapter.ViewHolder holder, int position) {
        Album a = albums.get(position);
        Media f = a.getCoverAlbum();
        //加载封面图片
        Glide.with(holder.picture.getContext())
                .load(f.getPath())
                .asBitmap()
                .diskCacheStrategy(DiskCacheStrategy.RESULT)
                .priority(Priority.HIGH)
                .signature(f.getSignature())
                .centerCrop()
                .error(R.drawable.ic_error)
                .placeholder(placeholder)
                .animate(R.anim.fade_in)
                .into(holder.picture);

        holder.name.setTag(a);

        String hexPrimaryColor = String.format("#%06X", (0xFFFFFF & theme.getPrimaryColor()));
        String hexAccentColor = String.format("#%06X", (0xFFFFFF & theme.getAccentColor()));

        if (hexAccentColor.equals(hexPrimaryColor)) {
            float[] hsv = new float[3];
            int color = theme.getAccentColor();
            Color.colorToHSV(color, hsv);
            hsv[2] *= 0.72f; // value component
            color = Color.HSVToColor(hsv);
            hexAccentColor= String.format("#%06X", (0xFFFFFF & color));
        }

        String textColor = theme.getBaseTheme() != ThemeHelper.LIGHT_THEME ? "#FAFAFA" : "#2b2b2b";

        if (a.isSelected()) {
            holder.layout.setBackgroundColor(Color.parseColor(hexPrimaryColor));
            holder.picture.setColorFilter(0x77000000, PorterDuff.Mode.SRC_ATOP);
            if (theme.getBaseTheme() == ThemeHelper.LIGHT_THEME ) textColor = "#FAFAFA";
            //Log.v("TAG","isSelect");
        } else {
            holder.picture.clearColorFilter();
            holder.layout.setBackgroundColor(theme.getCardBackgroundColor());
            //Log.v("TAG","noSelect");
        }

        String albumNameHtml = "<i><font color='" + textColor + "'>" + a.getName() + "</font></i>";
        String albumPhotoCountHtml = "<b><font color='" + hexAccentColor + "'>" + a.getCount();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            holder.name.setText(Html.fromHtml(albumNameHtml, Html.FROM_HTML_MODE_LEGACY));
            holder.nPhotos.setText(Html.fromHtml(albumPhotoCountHtml, Html.FROM_HTML_MODE_LEGACY));
        } else {
            holder.name.setText(Html.fromHtml(albumNameHtml));
            holder.nPhotos.setText(Html.fromHtml(albumPhotoCountHtml));
        }

    }

    public void setOnClickListener(View.OnClickListener lis) {
        mOnClickListener = lis;
    }

    public void setOnLongClickListener(View.OnLongClickListener lis) {
        mOnLongClickListener = lis;
    }

    public void swapDataSet(ArrayList<Album> asd) {
        albums = asd;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return albums.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView picture;
        View  layout;
        TextView name, nPhotos;

        ViewHolder(View itemView) {
            super(itemView);
            picture = (ImageView) itemView.findViewById(R.id.album_preview);
            layout = itemView.findViewById(R.id.item_album_layout);
            name = (TextView) itemView.findViewById(R.id.album_name);
            nPhotos = (TextView) itemView.findViewById(R.id.album_photos_count);
        }
    }
}




