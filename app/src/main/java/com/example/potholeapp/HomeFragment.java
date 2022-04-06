package com.example.potholeapp;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class HomeFragment extends Fragment {

    private EditText editTextTitle;
    private EditText editTextDescription;
    private TextView textViewData;

    private static final String TAG = "MainActivity";

    private static final String KEY_TITLE = "title";
    private static final String KEY_DESCRIPTION = "description";

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference potholesRef = db.collection("Potholes");

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        getActivity().setTitle(R.string.HomeFragmentName);
        editTextTitle = view.findViewById(R.id.editText1);
        editTextDescription = view.findViewById(R.id.editText2);
        textViewData = view.findViewById(R.id.PotholeText);

        potholesRef.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(QuerySnapshot queryDocumentSnapshots, FirebaseFirestoreException e) {
                if (e != null) {
                    return;
                }

                String data = "";

                for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                    MapPoint mapPoint = documentSnapshot.toObject(MapPoint.class);
                    mapPoint.setMapPointId(documentSnapshot.getId());

                    String potholeId = mapPoint.getMapPointId();
                    String title = mapPoint.getTitle();
                    String description = mapPoint.getDescription();

                    data += "ID: " + potholeId
                            + "\nTitle: " + title + "\nDescription: " + description + "\n\n";
                }

                textViewData.setText(data);
            }
        });

        return view;
    }

    public void savePothole(View v){
        String title = editTextTitle.getText().toString();
        String description = editTextDescription.getText().toString();

        MapPoint mapPoint = new MapPoint(title, description,1,1);

        potholesRef.add(mapPoint);
    }

    public void retrievePothole(View V){
        potholesRef.get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        String data = "";

                        for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                            MapPoint mapPoint = documentSnapshot.toObject(MapPoint.class);
                            mapPoint.setMapPointId(documentSnapshot.getId());

                            String potholeId = mapPoint.getMapPointId();
                            String title = mapPoint.getTitle();
                            String description = mapPoint.getDescription();

                            data += "ID: " + potholeId + "\nTitle: " + title + "\nDescription: " + description + "\n\n";
                        }

                        textViewData.setText(data);
                    }
                });
    }
}