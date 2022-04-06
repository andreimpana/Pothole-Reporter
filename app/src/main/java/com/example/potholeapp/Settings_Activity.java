package com.example.potholeapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.Toast;

public class Settings_Activity extends AppCompatActivity {
    private final String colourKey = "colour";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        setTitle("Settings");
        Spinner spinner =  findViewById(R.id.spinner);
        String fileName = getString(R.string.user_preference);
        SharedPreferences userPreference = getSharedPreferences(fileName, MODE_PRIVATE);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.colours_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String colour = adapterView.getItemAtPosition(i).toString();
                String colourKey = "colour";
                //String fileName = getString(R.string.user_preference);
                //SharedPreferences userPreference = getSharedPreferences(fileName, MODE_PRIVATE);
                SharedPreferences.Editor preferenceEditor = userPreference.edit();
                preferenceEditor.clear();
                preferenceEditor.putString(colourKey, colour);
                preferenceEditor.commit();
                Toast.makeText(getApplicationContext(), colour, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        String defaultColour = userPreference.getString(colourKey,"Blue");
        int spinnerPosition = adapter.getPosition(defaultColour);
        spinner.setSelection(spinnerPosition);
    }
}