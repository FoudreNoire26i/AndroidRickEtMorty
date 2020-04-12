package com.example.myrickandmortyapplication;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class CharacterAdapter extends BaseAdapter {
    ArrayList<Character> characterArrayList = new ArrayList<Character>();
    Context contexte;

    public CharacterAdapter(Context contexte, ArrayList<Character> list) {
        this.contexte = contexte;
        this.characterArrayList = list;
    }

    @Override
    public int getCount() {
        return characterArrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return characterArrayList.get(position);
    }
    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        LinearLayout layoutItem;
        LayoutInflater mInflater = LayoutInflater.from(contexte);

        if (view == null) {
            layoutItem = (LinearLayout) mInflater.inflate(R.layout.character_layout, viewGroup, false);
        } else {
            layoutItem = (LinearLayout) view;
        }


        ViewHolder viewHolder = (ViewHolder) layoutItem.getTag();

        if(viewHolder == null) {
            viewHolder = new ViewHolder();
            //(2) : Récupération des TextView de notre layout
            viewHolder.name = (TextView) layoutItem.findViewById(R.id.nameField);
            viewHolder.status = (TextView) layoutItem.findViewById(R.id.statusField);
            viewHolder.species = (TextView) layoutItem.findViewById(R.id.speciesField);
            viewHolder.gender = (TextView) layoutItem.findViewById(R.id.genderField);
            viewHolder.image = layoutItem.findViewById(R.id.characterPicture);
            layoutItem.setTag(viewHolder);
            //(3) : Mise à jour des valeurs
            //On retourne l'item créé.
        }

        Character currentCharacter = characterArrayList.get(position);

        viewHolder.name.setText(currentCharacter.getName());
        viewHolder.status.setText(currentCharacter.getStatus());
        viewHolder.species.setText(currentCharacter.getSpecies());
        viewHolder.gender.setText(currentCharacter.getGender());

        Picasso.get().load(currentCharacter.getImage()).resize(150,150).into(viewHolder.image);


        return layoutItem;
    }




    private class ViewHolder {
        public TextView name;
        public TextView status;
        public TextView species;
        public TextView gender;
        public ImageView image;

    }
}
