package com.yushealth.prostate.fingerdanceforprostate;


import android.app.Activity;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class MusicListAdapter extends ArrayAdapter<MusicFileData> {
    private final List<MusicFileData> list;
    private final Activity context;

    public MusicListAdapter(Activity context, List<MusicFileData> list) {
        super(context, R.layout.music_item, list);
        this.list = list;
        this.context = context;
    }

    static class ViewHolder {
        protected TextView musicFile;
        protected ImageButton btnPlay;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            LayoutInflater layoutInflater = context.getLayoutInflater();
            convertView = layoutInflater.inflate(R.layout.music_item, null);

            viewHolder = new ViewHolder();
            viewHolder.musicFile = (TextView) convertView.findViewById(R.id.txtMusicfile);

            viewHolder.btnPlay = (ImageButton) convertView.findViewById(R.id.btnPlay);
            viewHolder.btnPlay.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int get_position = (Integer)view.getTag();
                    String f = list.get(get_position).getName();
                    Toast.makeText(context, f, Toast.LENGTH_SHORT).show();

                }
            });

            //
            convertView.setTag(viewHolder);
            convertView.setTag(R.id.txtMusicfile, viewHolder.musicFile);
            convertView.setTag(R.id.btnPlay, viewHolder.btnPlay);

        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.btnPlay.setTag(position);
        viewHolder.musicFile.setText(list.get(position).getName());

        return convertView;
    }
}
