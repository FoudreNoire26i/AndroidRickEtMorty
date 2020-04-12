package com.example.myrickandmortyapplication;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.util.Log;
import android.view.Display;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class MainActivityCharacter extends AppCompatActivity {
    private TextView name;
    private TextView status;
    private TextView species;
    private TextView gender;
    private TextView origin;
    private TextView location;
    private ImageView characterPicture;
    Character character;
    public static String KEY_FROM_CHARACTER = "character";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_character);
        name=findViewById(R.id.nameField);
        status=findViewById(R.id.statusField);
        species=findViewById(R.id.speciesField);
        gender=findViewById(R.id.genderField);
        characterPicture=findViewById(R.id.characterPicture);
        origin=findViewById(R.id.originField);
        location=findViewById(R.id.locationField);


        character = (Character) getIntent().getSerializableExtra(MainActivity.KEY_FROM_LIST);

        name.setText(character.getName());
        status.setText(character.getStatus());
        species.setText(character.getSpecies());
        gender.setText(character.getGender());
        origin.setText(character.getOrigin().getName());
        location.setText(character.getLocation().getName());
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width = 70*size.x/100;
        Picasso.get().load(character.getImage()).resize(width,width).into(characterPicture);
        int pad = 15*size.x/100;
        characterPicture.setPadding(0,pad,0,pad);
    }


    @Override
    public void onBackPressed() {
        Intent intentCharacter = new Intent();
        intentCharacter.putExtra(KEY_FROM_CHARACTER, character.getId()-1);
        setResult(RESULT_OK, intentCharacter);
        super.onBackPressed();
    }

}
