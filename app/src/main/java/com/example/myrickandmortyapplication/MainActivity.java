package com.example.myrickandmortyapplication;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    ArrayList<Character> characterPageArrayList = new ArrayList<Character>();
    ArrayList<Character> characterArrayList;
    public static String KEY_FROM_LIST = "list";
    private final int NB_OF_CHARACTERS_ON_A_PAGE = 20;
    private int currentPage = 1;
    private int endPage = 1;

    public static int RQC_B = 1;
    ListView listV;
    SharedPreferences mPrefs;
    SharedPreferences.Editor prefsEditor;
    CharacterAdapter adaptater;
    Spinner spinnerSort;
    public TextView pageIndicationView;

    Button lastPageButton;
    Button nextPageButton;
    LinearLayout layoutHeader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        layoutHeader = findViewById(R.id.layoutHeader);
        characterArrayList = (ArrayList<Character>) getIntent().getSerializableExtra(SplashScreen.CHARACTER_ARRAY_LIST);
        nextPageButton = findViewById(R.id.nextPage);
        lastPageButton = findViewById(R.id.lastPage);
        spinnerSort = (Spinner) findViewById(R.id.spinnerSort);
        pageIndicationView = findViewById(R.id.pageIndicationView);


        if (characterArrayList.size()>0) {
            initList();
            if (characterArrayList.size()>NB_OF_CHARACTERS_ON_A_PAGE) {
                initPageButtonDisplay();
            }
            TextView noCharacterView = findViewById(R.id.noCharacterTextView);
            noCharacterView.setVisibility(View.INVISIBLE);
        }
        else{
            nextPageButton.setVisibility(View.INVISIBLE);
            lastPageButton.setVisibility(View.INVISIBLE);
        }

        spinnerSort.setOnItemSelectedListener(new CustomOnItemSelectedListener(characterArrayList,adaptater, characterPageArrayList));
    }

    public void initList(){

        mPrefs = getSharedPreferences(SplashScreen.PREFERENCE_FILE_KEY, MODE_PRIVATE);
        prefsEditor = mPrefs.edit();

        listV = findViewById(R.id.characterList);
        adaptater = new CharacterAdapter(MainActivity.this, characterPageArrayList);
        listV.setAdapter(adaptater);
        
        
        listV.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                //affichage alert
                Toast.makeText(MainActivity.this, characterPageArrayList.get(position).getName(), Toast.LENGTH_SHORT).show();

                //transition activité
                Intent myI = new Intent(MainActivity.this, MainActivityCharacter.class);
                myI.putExtra(KEY_FROM_LIST, characterPageArrayList.get(position));
                startActivityForResult(myI, RQC_B);
                overridePendingTransition(R.anim.zoom_enter, R.anim.zoom_exit);
            }
        });
        listV.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int position, long l) {
                final Map<String, String> savedCharacters = (Map<String, String>) mPrefs.getAll();
                Gson gson = new Gson();
                if (savedCharacters.containsKey(""+characterPageArrayList.get(position).getId())){
                    prefsEditor.remove(""+characterPageArrayList.get(position).getId());
                    characterArrayList.get(position).setFavorite(0);
                    Toast.makeText(MainActivity.this, characterPageArrayList.get(position).getName() + " removed", Toast.LENGTH_SHORT).show();
                }
                else {
                    characterArrayList.get(position).setFavorite(1);
                    String json = gson.toJson(characterPageArrayList.get(position));
                    prefsEditor.putString("" + characterPageArrayList.get(position).getId(), json);
                    Toast.makeText(MainActivity.this, characterPageArrayList.get(position).getName() + " saved", Toast.LENGTH_SHORT).show();
                }
                prefsEditor.commit();
                return true;
            }
        });

        if (characterArrayList.size()>NB_OF_CHARACTERS_ON_A_PAGE) {
            characterPageArrayList.addAll(characterArrayList.subList(0, NB_OF_CHARACTERS_ON_A_PAGE));
            endPage = characterArrayList.size()/NB_OF_CHARACTERS_ON_A_PAGE;
            if (characterArrayList.size()%NB_OF_CHARACTERS_ON_A_PAGE!=0)
                endPage++;
        }
        else {
            characterPageArrayList.addAll(characterArrayList.subList(0, characterArrayList.size()));
            lastPageButton.setVisibility(View.INVISIBLE);
            nextPageButton.setVisibility(View.INVISIBLE);
        }
        adaptater.notifyDataSetChanged();
    }

    private void initPageButtonDisplay() {
        AbsListView.OnScrollListener scollListener = new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                if (characterArrayList.size()>NB_OF_CHARACTERS_ON_A_PAGE) {
                    if (scrollState != SCROLL_STATE_IDLE) {
                        lastPageButton.setVisibility(View.INVISIBLE);
                        nextPageButton.setVisibility(View.INVISIBLE);
                    } else {
                        lastPageButton.setVisibility(View.VISIBLE);
                        nextPageButton.setVisibility(View.VISIBLE);
                    }
                }
                if (scrollState != SCROLL_STATE_IDLE) {
                    layoutHeader.setVisibility(View.GONE);
                } else {
                    layoutHeader.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

            }
        };;
        listV.setOnScrollListener(scollListener);
    }


    public void goNextPage(android.view.View v){
        if (characterPageArrayList.contains(characterArrayList.get(characterArrayList.size()-1))){
            characterPageArrayList.clear();
            currentPage=0;
            if (characterArrayList.size()>NB_OF_CHARACTERS_ON_A_PAGE)
                characterPageArrayList.addAll(characterArrayList.subList(0,NB_OF_CHARACTERS_ON_A_PAGE));
            else
                characterPageArrayList.addAll(characterArrayList.subList(0,characterArrayList.size()));
        }
        else if (characterArrayList.size() < NB_OF_CHARACTERS_ON_A_PAGE + currentPage * NB_OF_CHARACTERS_ON_A_PAGE){
            characterPageArrayList.clear();
            characterPageArrayList.addAll(characterArrayList.subList(currentPage * NB_OF_CHARACTERS_ON_A_PAGE, characterArrayList.size()));
        }
        else {
            characterPageArrayList.clear();
            characterPageArrayList.addAll(characterArrayList.subList(currentPage * NB_OF_CHARACTERS_ON_A_PAGE, currentPage*NB_OF_CHARACTERS_ON_A_PAGE+NB_OF_CHARACTERS_ON_A_PAGE));
        }
        currentPage++;
        pageIndicationView.setText(currentPage+" sur "+endPage);
        adaptater.notifyDataSetChanged();
        Toast.makeText(MainActivity.this, "Page "+currentPage, Toast.LENGTH_SHORT).show();
    }

    public void goLastPage(android.view.View v){
        Character c = characterPageArrayList.get(0);
        characterPageArrayList.clear();
        if (currentPage==1){
            currentPage = endPage;
            characterPageArrayList.addAll(characterArrayList.subList( (currentPage - 1) * NB_OF_CHARACTERS_ON_A_PAGE, characterArrayList.size()));
        }
        else {
            currentPage--;
            characterPageArrayList.addAll(characterArrayList.subList(characterArrayList.lastIndexOf(c) - NB_OF_CHARACTERS_ON_A_PAGE , characterArrayList.lastIndexOf(c)));
        }
        adaptater.notifyDataSetChanged();
        pageIndicationView.setText(currentPage+" sur "+endPage);
        Toast.makeText(MainActivity.this, "Page "+currentPage, Toast.LENGTH_SHORT).show();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RQC_B && resultCode == RESULT_OK) {
            listV.smoothScrollToPosition(data.getIntExtra(MainActivityCharacter.KEY_FROM_CHARACTER, 0));
        }
    }

    public class CustomOnItemSelectedListener implements AdapterView.OnItemSelectedListener {

        ArrayList<Character> characterArrayList;
        ArrayList<Character> characterPageArrayList;
        CharacterAdapter adapter;

        public CustomOnItemSelectedListener(ArrayList<Character> characterArrayList, CharacterAdapter adapter, ArrayList<Character> characterPageArrayList) {
            this.characterArrayList=characterArrayList;
            this.adapter=adapter;
            this.characterPageArrayList=characterPageArrayList;
        }

        public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
            switch(pos){
                case 0:
                    Collections.sort(characterArrayList, Character.ChaIdComparator);
                    break;
                case 1:
                    Collections.sort(characterArrayList, Character.ChaNameComparator);
                    break;
                case 2:
                    Collections.sort(characterArrayList, Character.ChaStatusComparator);
                    break;
                case 3:
                    Collections.sort(characterArrayList, Character.ChaSpeciesComparator);
                    break;
                case 4:
                    Collections.sort(characterArrayList, Character.ChaFavComparator);
                    break;
                default: break;
            }
            characterPageArrayList.clear();
            if (characterArrayList.size()>NB_OF_CHARACTERS_ON_A_PAGE) {
                characterPageArrayList.addAll(characterArrayList.subList(0, NB_OF_CHARACTERS_ON_A_PAGE));
            }
            else {
                characterPageArrayList.addAll(characterArrayList.subList(0, characterArrayList.size()));
                lastPageButton.setVisibility(View.INVISIBLE);
                nextPageButton.setVisibility(View.INVISIBLE);
            }
            adapter.notifyDataSetChanged();
        }

        @Override
        public void onNothingSelected(AdapterView<?> arg0) {
            Collections.sort(characterArrayList, Character.ChaIdComparator);
            adapter.notifyDataSetChanged();
        }
    }


}

