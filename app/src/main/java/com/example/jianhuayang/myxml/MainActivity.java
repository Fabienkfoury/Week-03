package com.example.jianhuayang.myxml;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MyCarActivity";
    private EditText editTextMake;
    private EditText editTextYear;
    private EditText editTextColor;
    //private Button button;
    private EditText editTextPrice;
    private EditText editTextEngine;
    private TextView textViewBlock;

    private Vehicle vehicle;
    // the diamond syntax: because the empty angle brackets have the shape of a diamond, "core java for the impatient" C. Horstmann
    private ArrayList<Vehicle> vehicleList = new ArrayList<>(); // Déclaration d'un tableau de type : véhicle
    private StringBuilder outputs;  // Pour pouvoir modifier un string car normalement un string est immuable
    private static Double depreciation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);             // R est une class genere automatiquement
        Toolbar myToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);

        editTextMake = (EditText) findViewById(R.id.inputMake);
        editTextYear = (EditText) findViewById(R.id.inputYear);
        editTextColor = (EditText) findViewById(R.id.inputColor);
        editTextPrice = (EditText) findViewById(R.id.inputPrice);
        editTextEngine = (EditText) findViewById(R.id.inputEngine);
        textViewBlock = (TextView) findViewById(R.id.textBlock);
        textViewBlock.setMovementMethod(new ScrollingMovementMethod());
        depreciation = getResources().getInteger(R.integer.depreciation) / 100.00;

//        button = (Button) findViewById(R.id.buttonRunPetrol);   // Autre méthode pour "onButonClick"
//        button.setOnClickListener(new View.OnClickListener() {
//            public void onClick(View v) {
//                // Perform action on click
//            }
//        });

        String[] manufacturers = getResources().getStringArray(R.array.manufacturer_array);   // On a aussi la méthode toString()
        String[] descriptions = getResources().getStringArray(R.array.description_array);
        for (int i = 0; i < manufacturers.length; i++ )
        {
            mapCarMaker.put(manufacturers[i], descriptions[i]);
        }
        /*What the code above does is to read car manufacturer names and their info. These are then put into a dictionary for later use. Note in here:

        We use String array instead of ArrayList as the size of the array is fixed.
        For an array, it has a field (i.e. a member variable) called length for its size. But for ArrayList, to get its size we need to call the size() method.
        */
    }

    public void onButtonClick(View view) {
        String make = editTextMake.getText().toString();
        String strYear = editTextYear.getText().toString();
        int intYear = Integer.parseInt(strYear);
        String color = editTextColor.getText().toString();
        /* String strPrice=edtitTextYear.getText().toString();
        int inPrice=Integer.parseInt(strPrice);
         */
        Integer price = new Integer(editTextPrice.getText().toString());
        Double engine = new Double(editTextEngine.getText().toString());

        switch (view.getId()) {
            case R.id.buttonRunPetrol:
                vehicle = new Car(make, intYear, color,price,engine);
                break;
            case R.id.buttonRunDiesel:
                vehicle = new Diesel(make, intYear,price,engine);
                break;
            default:
                vehicle = new Vehicle();
                break;
        }

        if (Vehicle.counter == 5) {
            vehicle = new Vehicle() {
                @Override
                public String getMessage() {
                    return "You have pressed 5 times, stop it!";
                }
            };
        }

        Toast.makeText(getApplicationContext(), vehicle.getMessage(), Toast.LENGTH_SHORT).show();
        Log.d(TAG, "User clicked " + Vehicle.counter + " times.");
        Log.d(TAG, "User message is \"" + vehicle + "\".");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {       // Methode Overridée et qui fais fonctioner les boutons du menu
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        switch (id) {
            case R.id.menu_add: // menu_add provient du menu_main.xml
                addVehicle();   // fonction crée juste en dessous
                return true;
            case R.id.menu_clear:
                return clearVehicleList();
            case R.id.action_settings:
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void addVehicle() {
        vehicleList.add(vehicle);       // Ajoute dans le tableau de vehicle crée au dessus
        resetOutputs();
    }

    private boolean clearVehicleList() {
        vehicleList.clear();            // fonction qui vide le tableau de vehicle
        resetOutputs();
        return true;
    }

    private void resetOutputs() {
        if (vehicleList.size() == 0) {
            outputs = new StringBuilder("Your vehicle list is currently empty.;");
        } else {
            outputs = new StringBuilder();
            for (Vehicle v : vehicleList) {
                String vehicleDescription = mapCarMaker.get(v.getMake());
                if (vehicleDescription == null){
                    vehicleDescription = "No description available.";
                }
                outputs.append("This is vehicle No. " + (vehicleList.indexOf(v) + 1) + System.getProperty("line.separator"));
                outputs.append("Manufacturer: " + v.getMake());
//                outputs.append("; Current value: " + depreciatePrice(v.getPrice()));
//                outputs.append("; Effective engine size: " + depreciateEngine(v.getEngine()));
                outputs.append("; Current value: " + depreciateAnything(v.getPrice()));
                outputs.append("; Effective engine size: " + depreciateAnything(v.getEngine()));
                outputs.append(System.getProperty("line.separator"));
                outputs.append(System.getProperty("line.separator"));
            }
        }
        textViewBlock.setText(outputs); // Affichage du texte
    }

//    private int depreciatePrice(int price) {
//        return (int) (price * depreciation);
//    }   // Dépréciation Price
//
//    private double depreciateEngine(double engine) { // Dépréciation engine
//        return (double) Math.round(engine * depreciation * 100) / 100 ;
//    }

    private <T extends Number> Double depreciateAnything(T originalValue) {
        Double result;
        if (originalValue instanceof Double) {
            result = Math.round(originalValue.doubleValue() * 0.8 * 100) / 100.00;
        } else {
            result = originalValue.intValue() * 0.8;
        }
        return result;
    }

    public void onClearClick(View v){           // Boutton du bas de la page
        clearVehicleList();
    }
    private Map<String, String> mapCarMaker = new HashMap<>(); //

}
