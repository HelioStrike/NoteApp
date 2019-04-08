package com.example.sande.noteapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    SharedPreferences sharedPreferences;
    ArrayList<String> notesList;
    ArrayList<String> notesDisplayList;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.main_menu, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.newnote:
                Intent intent = new Intent(getApplicationContext(), Main2Activity.class);
                intent.putExtra("edit", "0");
                startActivity(intent);
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ListView notes = (ListView) findViewById(R.id.notes);
        notesList = new ArrayList<String>();
        notesDisplayList = new ArrayList<String>();

        sharedPreferences = this.getSharedPreferences("com.example.sande.noteapp", Context.MODE_PRIVATE);
        try {
            notesList = (ArrayList<String>) ObjectSerializer.deserialize(sharedPreferences.getString("notes",
                    ObjectSerializer.serialize(new ArrayList<String>())));
        } catch (Exception e) {
            e.printStackTrace();
        }

        for(int i = 0; i < notesList.size(); i++) {
            notesDisplayList.add(notesList.get(i).substring(0, (20 < notesList.get(i).length())? 20:notesList.get(i).length()) + "...");
        }

        ArrayAdapter<String> notesAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, notesDisplayList);
        notes.setAdapter(notesAdapter);

        notes.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getApplicationContext(), Main2Activity.class);
                intent.putExtra("edit", "1");
                intent.putExtra("pos", Integer.toString(position));
                startActivity(intent);
            }
        });

        notes.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                notesList.remove(position);
                notesDisplayList.remove(position);
                try {
                    sharedPreferences.edit().putString("notes", ObjectSerializer.serialize(notesList)).apply();
                } catch(Exception e) {
                    e.printStackTrace();
                }

                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);

                return false;
            }
        });
    }
}
