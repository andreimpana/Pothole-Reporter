package com.example.potholeapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class PotholeDetails extends AppCompatActivity {
    private Button delButton;
    private TextView markTitle;
    private TextView markDesc;
    private TextView markLoc;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference potholesRef = db.collection("Potholes");

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String title = getIntent().getStringExtra("name");
        MapPoint mp = (MapPoint) getIntent().getSerializableExtra("obj");
        setContentView(R.layout.activity_pothole_details);
        setTitle(title);
        delButton = findViewById(R.id.markDelete);
        markTitle = findViewById(R.id.markTitle);
        markDesc = findViewById(R.id.markDesc);
        markLoc = findViewById(R.id.markLoc);
        markTitle.setText("Title: " + title.toString());
        markDesc.setText("Description: " + mp.getDescription().toString());
        markLoc.setText("Longitude: " + mp.getLon() + "\nLatitude: " + mp.getLat());
        delButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(PotholeDetails.this);
                builder.setMessage(R.string.DialogMessage);
                builder.setTitle(R.string.DialogTitle);
                builder.setPositiveButton(R.string.OK, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        potholesRef.document(mp.getMapPointId()).delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if(task.isSuccessful()){
                                    Toast.makeText(getApplicationContext(),"deleted",Toast.LENGTH_LONG).show();
                                }else{
                                    Toast.makeText(getApplicationContext(),"failed",Toast.LENGTH_LONG).show();
                                }

                            }
                        });
                        Intent resultIntent = new Intent();
                        resultIntent.putExtra("Response", "Pin deleted successfully.");
                        setResult(Activity.RESULT_OK, resultIntent);
                        finish();
                    }
                });
                builder.setNegativeButton(R.string.CANCEL, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                    }
                });
                builder.show();
            }

        });
    }
}
