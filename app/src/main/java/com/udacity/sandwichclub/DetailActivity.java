package com.udacity.sandwichclub;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import com.udacity.sandwichclub.model.Sandwich;
import com.udacity.sandwichclub.utils.JsonUtils;

import java.util.List;

public class DetailActivity extends AppCompatActivity {

    public static final String EXTRA_POSITION = "extra_position";
    private static final int DEFAULT_POSITION = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        ImageView ingredientsIv = findViewById(R.id.image_iv);
        final ProgressBar progressBar = findViewById(R.id.progressBar);

        Intent intent = getIntent();
        if (intent == null) {
            closeOnError();
            return;
        }

        int position = intent.getIntExtra(EXTRA_POSITION, DEFAULT_POSITION);
        if (position == DEFAULT_POSITION) {
            // EXTRA_POSITION not found in intent
            closeOnError();
            return;
        }

        String[] sandwiches = getResources().getStringArray(R.array.sandwich_details);
        String json = sandwiches[position];
        Sandwich sandwich = JsonUtils.parseSandwichJson(json);
        if (sandwich == null) {
            // Sandwich data unavailable
            closeOnError();
            return;
        }

        populateUI(sandwich);
        Picasso.with(this)
                .load(sandwich.getImage())
                .into(ingredientsIv, new Callback() {
                    @Override
                    public void onSuccess() {
                        if (progressBar != null) {
                            progressBar.setVisibility(View.GONE);
                        }

                    }

                    @Override
                    public void onError() {
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(DetailActivity.this, "Unable to load image", Toast.LENGTH_SHORT).show();


                    }
                });

        setTitle(sandwich.getMainName());
    }

    private void closeOnError() {
        finish();
        Toast.makeText(this, R.string.detail_error_message, Toast.LENGTH_SHORT).show();
    }

    private void populateUI(Sandwich sandwich) {

        // Description
        TextView descTextView = findViewById(R.id.description_tv);
        String description = sandwich.getDescription();
        if (description == null || description.equals("")) {
            description = getString(R.string.no_description);
        }
        descTextView.setText(description);

        // Ingredients
        TextView ingredientsTextView = findViewById(R.id.ingredients_tv);
        String ingredients;
        List<String> ingredientsList = sandwich.getIngredients();
        if (ingredientsList.isEmpty()) {
            ingredients = getString(R.string.unknown_ingredients);
        } else {
            StringBuilder ingredientsBuilder = new StringBuilder();
            for (int i = 0; i < ingredientsList.size(); i++) {
                ingredientsBuilder
                        .append(" \u2022 ")
                        .append(ingredientsList.get(i));

                if (i != ingredientsList.size() - 1)
                    ingredientsBuilder.append('\n');
            }

            ingredients = ingredientsBuilder.toString();
        }
        ingredientsTextView.setText(ingredients);

        //PlaceOfOrigin
        TextView placeOfOriginTextView = findViewById(R.id.origin_tv);
        String placeOfOrigin = sandwich.getPlaceOfOrigin();

        if (placeOfOrigin == null || placeOfOrigin.equals("")) {
            placeOfOrigin = getString(R.string.unknown_origin);
        }
        placeOfOriginTextView.setText(placeOfOrigin);


        //AlsoKnownAs
        TextView alsoKnownAsTextView = findViewById(R.id.also_known_tv);
        String alsoKnownAs;
        List<String> alsoKnownAsList = sandwich.getAlsoKnownAs();

        if (alsoKnownAsList.isEmpty()) {
            alsoKnownAs = getString(R.string.no_aliases);
        } else {
            StringBuilder akaBuilder = new StringBuilder();
            for (int i = 0; i < alsoKnownAsList.size(); i++) {
                akaBuilder
                        .append(" \u2022 ")
                        .append(alsoKnownAsList.get(i));

                if (i != alsoKnownAsList.size() - 1)
                    akaBuilder.append('\n');
            }

            alsoKnownAs = akaBuilder.toString();
        }
        alsoKnownAsTextView.setText(alsoKnownAs);

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
