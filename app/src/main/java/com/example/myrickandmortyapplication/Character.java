package com.example.myrickandmortyapplication;

import android.media.Image;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.Comparator;

public class Character implements Serializable {
    static int MAX_CHARACTER =1000;
    public class State implements Serializable{
        private String name;
        private String url;
        State(String js){
            try {
                JSONObject jsO = new JSONObject(js);
                this.name=jsO.getString("name");
                this.url=jsO.getString("url");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        public String getInfo(){
            return "Name : "+name+" / "+"Url : "+url;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }
    }

    private int id;
    private String name;
    private String status;
    private String species;
    private String type;
    private String gender;
    private State origin;
    private State location;
    private String image;
    private Image imageObject;
    private String[] episode;
    private String url;
    private String created;
    int isFavorite = 0;

    Character (Character c){
        id = c.getId();
        name = c.getName();
        status = c.getStatus();
        species = c.getSpecies();
        type=c.getType();
        gender = c.getGender();
        origin = c.getOrigin();
        location = c.getLocation();
        url = c.getUrl();
        image = c.getImage();
        episode = c.getEpisode();
        created = c.getCreated();
        imageObject=c.getImageObject();
        isFavorite=c.isFavorite();
    }

    Character(String js) {
        try {
            JSONObject allInfo = new JSONObject(js);
            id = allInfo.getInt("id");
            name = allInfo.getString("name");
            status = allInfo.getString("status");
            species = allInfo.getString("species");
            gender = allInfo.getString("gender");
            type = allInfo.getString("type");
            origin = new State(allInfo.getString("origin"));
            location = new State(allInfo.getString("location"));
            url = allInfo.getString("url");
            image = allInfo.getString("image");
            JSONArray episodesArray = new JSONArray(allInfo.getString("episode"));
            episode = new String[episodesArray.length()];
            for (int i = 0; i < episodesArray.length(); i++) {
                episode[i] = episodesArray.getString(i);
            }
            created = allInfo.getString("created");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public int isFavorite() {
        return isFavorite;
    }

    public void setFavorite(int favorite) {
        isFavorite = favorite;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Image getImageObject() {
        return imageObject;
    }

    public void setImageObject(Image i) {
        this.imageObject = i;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getSpecies() {
        return species;
    }

    public void setSpecies(String species) {
        this.species = species;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public State getOrigin() {
        return origin;
    }

    public void setOrigin(State origin) {
        this.origin = origin;
    }

    public State getLocation() {
        return location;
    }

    public void setLocation(State location) {
        this.location = location;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String[] getEpisode() {
        return episode;
    }

    public void setEpisode(String[] episode) {
        this.episode = episode;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    public static Comparator<Character> ChaNameComparator = new Comparator<Character>() {
        @Override
        public int compare(Character c1, Character c2) {
            String characterName1 = c1.getName().toUpperCase();
            String characterName2 = c2.getName().toUpperCase();
            //ascending order
            return characterName1.compareTo(characterName2);

            //descending order
            //return StudentName2.compareTo(StudentName1);
        }
    };

    public static Comparator<Character> ChaIdComparator = new Comparator<Character>() {
        @Override
        public int compare(Character c1, Character c2) {
            int idCha1 = c1.getId();
            int idCha2 = c2.getId();

            /*For ascending order*/
            return idCha1-idCha2;

            /*For descending order*/
            //rollno2-rollno1;
        }
    };
    public static Comparator<Character> ChaStatusComparator = new Comparator<Character>() {
        @Override
        public int compare(Character c1, Character c2) {
            String characterName1 = c1.getStatus().toUpperCase();
            String characterName2 = c2.getStatus().toUpperCase();
            //ascending order
            return characterName1.compareTo(characterName2);

            /*For descending order*/
            //rollno2-rollno1;

        }
    };
    public static Comparator<Character> ChaFavComparator = new Comparator<Character>() {
        @Override
        public int compare(Character c1, Character c2) {
            int characterIsFavorite1 = c1.isFavorite();
            int characterIsFavorite2 = c2.isFavorite();
            //ascending order
            return characterIsFavorite2 - characterIsFavorite1;

            /*For descending order*/
            //rollno2-rollno1;

        }
    };

    public static Comparator<Character> ChaSpeciesComparator = new Comparator<Character>() {
        @Override
        public int compare(Character c1, Character c2) {
            String characterName1 = c1.getSpecies().toUpperCase();
            String characterName2 = c2.getSpecies().toUpperCase();
            //ascending order
            return characterName1.compareTo(characterName2);

            /*For descending order*/
            //rollno2-rollno1;
        }
    };





    public void affiche() {
        Log.i("id", ""+id);
        Log.i("name", name);
        Log.i("status", status);
        Log.i("species", species);
        Log.i("gender", gender);
        Log.i("origin", origin.getInfo());
        Log.i("location", location.getInfo());
        Log.i("image", image);
        Log.i("url", url);
        Log.i("episode", episode.toString());

    }

}
