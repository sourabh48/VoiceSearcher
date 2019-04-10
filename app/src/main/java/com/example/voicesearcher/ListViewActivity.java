package com.example.voicesearcher;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ExpandableListView;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ListViewActivity extends AppCompatActivity {

    DatabaseReference databaseReference;
    ProgressDialog progressDialog;
    List<DataFile> list = new ArrayList<>();
    RecyclerView recyclerView;
    RecyclerView.Adapter adapter ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_view);

        recyclerView = (RecyclerView) findViewById(R.id.recycle);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(ListViewActivity.this));

        progressDialog = new ProgressDialog(ListViewActivity.this);
        progressDialog.setMessage("Loading Data from Firebase Database");
        progressDialog.show();

        String value = getIntent().getStringExtra("value");

        databaseReference = FirebaseDatabase.getInstance().getReference("Places").child(value);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {

                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    DataFile studentDetails = dataSnapshot.getValue(DataFile.class);
                    list.add(studentDetails);
                }

                adapter = new RecyclerViewAdapter(ListViewActivity.this, list);
                recyclerView.setAdapter(adapter);
                progressDialog.dismiss();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                progressDialog.dismiss();
            }
        });

    }

    @Override
    public void onBackPressed()
    {
        Intent list = new Intent( ListViewActivity.this,MainActivity.class);
        list.putExtra("value", "");
        list.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(list);
        finish();
    }
}