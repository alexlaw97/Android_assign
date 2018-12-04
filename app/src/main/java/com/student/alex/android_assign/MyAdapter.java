package com.student.alex.android_assign;

import android.app.Activity;
import android.app.ListActivity;
import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MyAdapter extends BaseAdapter {

    Context context;
    ArrayList<note_inp> note;
    private static LayoutInflater inflater = null;

    public MyAdapter (Activity context, ArrayList<note_inp> note)
    {
        this.context = context;
        this.note = note;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }


    @Override
    public int getCount() {

        return note.size();
    }

    @Override
    public Object getItem(int position) {
        return note.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }
    public View getView(int position, View convertView, ViewGroup parent) {
        View listItem = convertView;
        if(listItem == null)
            listItem = LayoutInflater.from(context).inflate(R.layout.cardview2,parent,false);

        note_inp currentpos = note.get(position);
        TextView titname = listItem.findViewById(R.id.title2);
        titname.setText(currentpos.title);

        TextView contname = listItem.findViewById(R.id.content2);
        contname.setText(currentpos.content);

        return listItem;
    }
}
