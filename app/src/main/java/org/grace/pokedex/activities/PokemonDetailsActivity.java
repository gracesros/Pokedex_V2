package org.grace.pokedex.activities;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import org.grace.pokedex.R;
import org.grace.pokedex.database.AppDatabase;
import org.grace.pokedex.entities.Pokemon;
import org.grace.pokedex.entities.PokemonDetails;
import org.grace.pokedex.interfaces.AsyncTaskHandler;
import org.grace.pokedex.network.PokemonDetailsAsyncTask;

public class PokemonDetailsActivity extends AppCompatActivity implements AsyncTaskHandler {

    ImageView image, favorite, imageType1, imageType2;
    TextView name, types, height, experience, id;

    AppDatabase database;

    // Pokemon info
    String url;
    String pokemonName;
    Pokemon favoritePokemon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pokemon_details);

        image = findViewById(R.id.details_image);
        imageType1 = findViewById(R.id.imageType1);
        imageType2 = findViewById(R.id.imageType2);
        favorite = findViewById(R.id.details_favorite);
        name = findViewById(R.id.details_name);
        types = findViewById(R.id.detatils_type);
        height = findViewById(R.id.detatils_height);
        experience = findViewById(R.id.detatils_experience);
        id = findViewById(R.id.details_id);

        url = getIntent().getStringExtra("URL");

        PokemonDetailsAsyncTask pokemonDetailsAsyncTask = new PokemonDetailsAsyncTask();
        pokemonDetailsAsyncTask.handler = this;
        pokemonDetailsAsyncTask.execute(url);

        database = AppDatabase.getDatabase(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_top, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.item_favorites:
                Intent intent = new Intent(this, FavoritesActivity.class);
                startActivity(intent);
                return true;

            case R.id.inicio:
                Intent intent2 = new Intent(this, MainActivity.class);
                startActivity(intent2);
                return true;

            case R.id.favorites:
                Toast.makeText(this, "Favorites selected", Toast.LENGTH_SHORT);
                Intent intent4 = new Intent(this, FavoritesActivity.class);
                startActivity(intent4);
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onTaskEnd(Object result) {
        PokemonDetails details = (PokemonDetails) result;
        pokemonName = details.getName().toUpperCase();
        Glide.with(this).load(details.getImage()).into(image);
        Glide.with(this).load(details.getImage()).into(imageType1);
        name.setText(details.getName());
        height.setText("Altura: " + details.getHeight() + " cm");
        experience.setText("Experiencia: "  + details.getBaseExperience() + "xp");
        id.setText("ID: #" + details.getId());

        String typesString = "";

        for (int i = 0; i < details.getTypes().length; i++) {
            typesString += details.getTypes()[i] + " ";
        }

        types.setText("Tipo: " + typesString);

        favoritePokemon = database.pokemonDao().findByName(details.getName());

        if (favoritePokemon != null) {
            Glide.with(this).load(R.drawable.fav_filled).into(favorite);
        }
    }

    public void onClickType(View view) {
            Intent intent = new Intent(this, PokemonTypeActivity.class);
            startActivity(intent);
    }

    public void onClickFavorite(View view) {
        if(favoritePokemon != null) {
            showAlert(this);
        }
        else {
            Pokemon pokemon = new Pokemon(pokemonName, url);
            database.pokemonDao().insertAll(pokemon);
            Glide.with(this).load(R.drawable.fav_filled).into(favorite);
        }
    }

    private void showAlert(final Context context) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setMessage("¿Estas seguro que quieres eliminar a " + pokemonName+ " de tus favoritos?");

        // Add the buttons
        builder.setPositiveButton("Sí", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                database.pokemonDao().delete(favoritePokemon);
                favoritePokemon = null;
                Glide.with(context).load(R.drawable.fav_empty).into(favorite);
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User cancelled the dialog
            }
        });

        // Create the AlertDialog
        AlertDialog dialog = builder.create();

        // Show
        dialog.show();
    }
}
