package com.example.myrickandmortyapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;

public class SplashScreen extends Activity {
// Splash screen timer
    private static int SPLASH_TIME_OUT = 3000;
    ArrayList<Character> characterArrayList = new ArrayList<Character>();
    private final int NB_OF_CHARACTERS_ON_A_PAGE = 20;
    public static String PREFERENCE_FILE_KEY = "preference";
    private int currentPage = 1;
    SharedPreferences mPrefs;
    SharedPreferences.Editor prefsEditor;
    public static String CHARACTER_ARRAY_LIST = "charracterArrayList";
    public static String IS_CONNECTED = "connected";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        ConnectivityManager connectivityManager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);


        mPrefs = getSharedPreferences(PREFERENCE_FILE_KEY, MODE_PRIVATE);
        prefsEditor = mPrefs.edit();

        if(connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
            //we are connected to a network
            new AsyncApi().execute("https://rickandmortyapi.com/api/character/", characterArrayList);
        }
        else {
            Toast.makeText(SplashScreen.this, "No internet connection", Toast.LENGTH_SHORT).show();
            Gson gson = new Gson();
            final Map<String, String> savedCharacters = (Map<String, String>) mPrefs.getAll();
            for (String json : savedCharacters.values()) {
                Character savedCharacter = gson.fromJson(json, Character.class);
                characterArrayList.add(savedCharacter);
            }
            new Handler().postDelayed(new Runnable() {

                /*
                 * Showing splash screen with a timer. This will be useful when you
                 * want to show case your app logo / company
                 */

                @Override
                public void run() {
                    // This method will be executed once the timer is over
                    // Start your app main activity
                    Intent i = new Intent(SplashScreen.this, MainActivity.class);
                    i.putExtra(CHARACTER_ARRAY_LIST,characterArrayList);
                    startActivity(i);
                    // close this activity
                    finish();
                }
            }, SPLASH_TIME_OUT);
        }
    }


    private class AsyncApi extends AsyncTask<Object, Void, ArrayList<Character>> {

        //si reste à "" fait rien, sinon la page suivante est chargée (pour pouvoir lire tous les perso)
        private String nextPage="";
        @Override
        protected ArrayList<Character> doInBackground(Object... params) {
            ArrayList<Character> result = (ArrayList<Character>) params[1];
            try {
                URL url = new URL((String)params[0]);
                HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
                try {
                    if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                        BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                        StringBuilder builder = new StringBuilder();
                        String aux = "";
                        while ((aux = reader.readLine()) != null) {
                            builder.append(aux);
                        }
                        String text = builder.toString();
                        JSONObject all = new JSONObject(text);
                        JSONObject info = new JSONObject((all.getString("info")));
                        nextPage = info.getString("next");
                        addCharactersToResult(all.getString("results"),result);
                        reader.close();
                    }

                }
                catch (Exception e) {
                    Log.i("error url", e.getMessage());
                }
                finally
                {
                    connection.disconnect();
                }


            } catch (Exception ex) {
                Log.i("RssReaderTask", "********** doInBackground: Feed error!");
            }

            return result;
        }

        void addCharactersToResult(String js, ArrayList<Character> result) {
            try {
                JSONArray characters = new JSONArray(js);
                for (int i = 0; i<characters.length();i++){
                    Character c = new Character(characters.getString(i));
                    result.add(c);
                }
            }
            catch (Exception e){
                Log.i("Error load characters", e.getMessage());
            }
        }


        @Override
        protected void onPostExecute(ArrayList<Character> result) {
            if (nextPage.contains("https://")){
                Log.i("nextpage", nextPage);
                new AsyncApi().execute(nextPage, result);
            }
            else {
                Log.i("finish message", "ok");

                SharedPreferences mPrefsA = getSharedPreferences(PREFERENCE_FILE_KEY, MODE_PRIVATE);
                SharedPreferences.Editor prefsEditorA = mPrefs.edit();

                Gson gson = new Gson();
                final Map<String, String> savedCharacters = (Map<String, String>) mPrefsA.getAll();
                for (String json : savedCharacters.values()) {
                    characterArrayList.get(gson.fromJson(json, Character.class).getId()-1).setFavorite(1);
                }

                Intent i = new Intent(SplashScreen.this, MainActivity.class);
                i.putExtra(CHARACTER_ARRAY_LIST,characterArrayList);
                startActivity(i);

                Log.i("on passe la", "oui oui");
                finish();
            }
        }

    }
}

