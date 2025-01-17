/*
 * Copyright 2017 Zhihu Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.lxj.matisse.internal.ui.adapter;

import android.content.Context;
import android.content.res.TypedArray;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.lxj.matisse.R;
import com.lxj.matisse.internal.entity.Album;
import com.lxj.matisse.internal.entity.SelectionSpec;

import java.io.File;

public class AlbumsAdapter extends CursorAdapter {

    private final Drawable mPlaceholder;
    private String albumId = null;
    private int selectedColor = 0;
    public AlbumsAdapter(Context context, Cursor c, boolean autoRequery) {
        super(context, c, autoRequery);
        TypedArray ta = context.getTheme().obtainStyledAttributes(
                new int[]{R.attr.album_thumbnail_placeholder,
                        R.attr.album_dropdown_selected_bg});
        mPlaceholder = ta.getDrawable(0);
        selectedColor = ta.getColor(1, Color.TRANSPARENT);
        ta.recycle();
    }

//    public AlbumsAdapter(Context context, Cursor c, int flags) {
//        super(context, c, flags);
//        TypedArray ta = context.getTheme().obtainStyledAttributes(
//                new int[]{R.attr.album_thumbnail_placeholder});
//        mPlaceholder = ta.getDrawable(0);
//        selectedColor = ta.getColor(1, Color.TRANSPARENT);
//        ta.recycle();
//    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.album_list_item, parent, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        Album album = Album.valueOf(cursor);
        if(this.albumId!=null && this.albumId.equals(album.getId())){
            view.setBackgroundColor(selectedColor);
        }else {
            view.setBackgroundColor(Color.TRANSPARENT);
        }

        ((TextView) view.findViewById(R.id.album_name)).setText(album.getDisplayName(context));
        ((TextView) view.findViewById(R.id.album_media_count)).setText(String.valueOf(album.getCount()));

        // do not need to load animated Gif
        SelectionSpec.getInstance().imageEngine.loadThumbnail(context, context.getResources().getDimensionPixelSize(R
                        .dimen.media_grid_size), mPlaceholder,
                (ImageView) view.findViewById(R.id.album_cover), album.getCoverUri());
    }
    public void updateSelection(String albumId){
        this.albumId = albumId;
        notifyDataSetChanged();
    }
}
