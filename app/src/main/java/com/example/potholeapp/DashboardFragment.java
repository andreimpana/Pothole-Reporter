package com.example.potholeapp;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.os.Parcelable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.io.Serializable;
import java.util.ArrayList;

public class DashboardFragment extends Fragment{
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference potholesRef = db.collection("Potholes");
                static ArrayList<String> potholes = new ArrayList<String>();
    static ArrayList<MapPoint> potPoints = new ArrayList<MapPoint>();
    ArrayAdapter adapter;
    ListView listView;
    public DashboardFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        Log.d("DASH", "onCreateView: ");
        View view =  inflater.inflate(R.layout.fragment_dashboard, container, false);
        getActivity().setTitle(R.string.DashboardFragmentName);
        adapter = new ArrayAdapter<String>(getActivity(),R.layout.list_item,R.id.message_text,potholes);
        listView = (ListView) view.findViewById(R.id.listView);
        listView.setAdapter(adapter);
        populateLV();
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(), PotholeDetails.class);
                //based on item add info to intent
                intent.putExtra("name", (String) adapter.getItem(position));
                intent.putExtra("obj", (Serializable) potPoints.get(position));
                startActivity(intent);
            }
        });

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d("DASH", "onResume: ");
//        populateLV();
        adapter.notifyDataSetChanged();
    }

    public void populateLV(){
        potholes = new ArrayList<String>();
        potPoints = new ArrayList<MapPoint>();
        potholesRef.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                            MapPoint mapPoint = documentSnapshot.toObject(MapPoint.class);
                            mapPoint.setMapPointId(documentSnapshot.getId());
                            String title = mapPoint.getTitle();
//                            potholes.add(title);
                            adapter.add(title);
                            potPoints.add(mapPoint);
                            adapter.notifyDataSetChanged();
                        }
                    }
                });
    }
}