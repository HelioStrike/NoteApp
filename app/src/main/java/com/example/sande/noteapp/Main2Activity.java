package com.example.sande.noteapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;

public class Main2Activity extends AppCompatActivity {

    EditText noteData;
    String edit;
    int pos;

    public void toNoteList(View view) {
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);
    }

    public void addNote(View view) {
        ArrayList<String> notesList = new ArrayList<String>();

        SharedPreferences sharedPreferences = this.getSharedPreferences("com.example.sande.noteapp", Context.MODE_PRIVATE);
        try {
            notesList = (ArrayList<String>) ObjectSerializer.deserialize(sharedPreferences.getString("notes",
                    ObjectSerializer.serialize(new ArrayList<String>())));
        } catch (Exception e) {
            e.printStackTrace();
        }

        if(edit.equals("0")) {
            notesList.add(noteData.getText().toString());
        } else if(edit.equals("1")) {
            notesList.set(pos, noteData.getText().toString());
            notesList.set(pos, noteData.getText().toString());
        }

        try {
            sharedPreferences.edit().putString("notes", ObjectSerializer.serialize(notesList)).apply();
        } catch(Exception e) {
            e.printStackTrace();
        }

        if(edit.equals("1")) {
            Toast.makeText(this, "Note Updated", Toast.LENGTH_SHORT).show();
        } else if (edit.equals("0")) {
            Toast.makeText(this, "New note created", Toast.LENGTH_SHORT).show();
            edit = "1";
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        noteData = (EditText) findViewById(R.id.noteEditText);
        Intent intent = getIntent();
        edit = intent.getStringExtra("edit");
        if(edit.equals("1")) {
            pos = Integer.parseInt(intent.getStringExtra("pos"));
        }

        EditText noteData = (EditText) findViewById(R.id.noteEditText);

        if(edit.equals("0")) {
            noteData.setText("");
            noteData.setHint("Your Note here...");
        } else if (edit.equals("1")) {
            ArrayList<String> notesList = new ArrayList<String>();

            SharedPreferences sharedPreferences = this.getSharedPreferences("com.example.sande.noteapp", Context.MODE_PRIVATE);
            try {
                notesList = (ArrayList<String>) ObjectSerializer.deserialize(sharedPreferences.getString("notes",
                        ObjectSerializer.serialize(new ArrayList<String>())));
            } catch (Exception e) {
                e.printStackTrace();
            }

            noteData.setText(notesList.get(pos));
        }
    }
}
