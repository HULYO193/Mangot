package com.example.mangot;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import androidx.appcompat.widget.SearchView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class Discovery extends BaseActivity {
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    private ArrayList<Manga> discoveryManga = new ArrayList<>();
    //RecyclerView recyclerdiscovery = findViewById(R.id.discoveryrecycler);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_discovery);
        // Set Home selected
        // Initialize and assign variable
        BottomNavigationView bottomNavigationView=findViewById(R.id.bottom_navigation);

        // Set Home selected
        bottomNavigationView.setSelectedItemId(R.id.discovery);

        // Perform item selected listener
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch(item.getItemId())
                {
                    case R.id.dashboard:
                        startActivity(new Intent(getApplicationContext(),DashboardActivity.class));
                        overridePendingTransition(0,0);
                        return true;

                    case R.id.discovery:
                        return true;

                    case R.id.createNewManga:
                        startActivity(new Intent(getApplicationContext(),CreateMangaActivity.class));
                        overridePendingTransition(0,0);
                        return true;
                }
                return false;
            }
        });

        db.collection("mangot").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {


                    discoveryManga = (ArrayList<Manga>) task.getResult().toObjects(Manga.class);
                    RecyclerView recyclerdiscovery = findViewById(R.id.discoveryrecycler);
                    RecyclerView.LayoutManager layoutManager = new GridLayoutManager(Discovery.this, 2, RecyclerView.VERTICAL, false);
                    recyclerdiscovery.setLayoutManager(layoutManager);

                    DiscoveryAdapter discoveryadapter = new DiscoveryAdapter(discoveryManga,Discovery.this);
                    recyclerdiscovery.setAdapter(discoveryadapter);
                }
                Toast.makeText(Discovery.this, "hello", Toast.LENGTH_SHORT).show();
            }

        });

        showSearchResult();

    }

        public void showSearchResult(){
            SearchView searchView = (SearchView) findViewById(R.id.discoverysearch);
            // below line is to call set on query text listener method.
            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                // inside on query text change method we are
                // calling a method to filter our recycler view.
                filter(newText);
                return false;
            }
        });

    }

    private void filter(String text) {
        // creating a new array list to filter our data.
        ArrayList<Manga> filteredlist = new ArrayList<Manga>();

        // running a for loop to compare elements.
        for (Manga item : discoveryManga) {
            // checking if the entered string matched with any item of our recycler view.
            if (item.getName().toLowerCase().contains(text.toLowerCase())) {
                // if the item is matched we are
                // adding it to our filtered list.
                filteredlist.add(item);
            }
        }
        if (filteredlist.isEmpty()) {
            // if no item is added in filtered list we are
            // displaying a toast message as no data found.
            Toast.makeText(this, "No Data Found..", Toast.LENGTH_SHORT).show();
        } else {
            // at last we are passing that filtered
            // list to our adapter class.
            RecyclerView recyclerdiscovery = findViewById(R.id.discoveryrecycler);
            DiscoveryAdapter discoveryadapter = new DiscoveryAdapter(filteredlist,Discovery.this);
            recyclerdiscovery.setAdapter(discoveryadapter);
        }
    }
}