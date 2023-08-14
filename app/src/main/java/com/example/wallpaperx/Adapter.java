package com.example.wallpaperx;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import java.util.List;

public class Adapter extends ArrayAdapter<HelpclassPhoto> {
    Activity context;   //this is the adapter used for listview favorites
    List<HelpclassPhoto> list;

    Adapter(Activity context, List<HelpclassPhoto>list){
        super(context, R.layout.sample, list);
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater layoutInflater = context.getLayoutInflater();
        View view = layoutInflater.inflate(R.layout.sample, null, true);
        HelpclassPhoto r = list.get(position);
        TextView I;
        I = view.findViewById(R.id.text);
        I.setText(r.name);

        return view;
    }
}
