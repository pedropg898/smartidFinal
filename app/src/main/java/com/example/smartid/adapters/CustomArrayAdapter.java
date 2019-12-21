package com.example.smartid.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.smartid.R;
import com.example.smartid.entities.Aluno;


import java.util.ArrayList;

public class CustomArrayAdapter extends ArrayAdapter<Aluno> {
    public CustomArrayAdapter(@NonNull Context context, ArrayList<Aluno> resource) {
        super(context,0, resource);
    }

    @Override

    public View getView(int position, View convertView, ViewGroup parent){
        Aluno p = getItem(position);
        if(convertView==null)
        {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.layout_linha,parent,false);
        }

        ((TextView) convertView.findViewById(R.id.uic)).setText(p.getUc());
        ((TextView) convertView.findViewById(R.id.prof)).setText(p.getProf());
        ((TextView) convertView.findViewById(R.id.dat)).setText(p.getData_p());
        return convertView;
    }
}
