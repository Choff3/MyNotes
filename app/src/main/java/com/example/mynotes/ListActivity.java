package com.example.mynotes;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class ListActivity extends AppCompatActivity {

    ArrayList<Note> notes;
    boolean isDeleting = false;
    NoteAdapter adapter;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        initNewButton();
    }

    @Override
    public void onResume() {
        super.onResume();
        String sortBy = getSharedPreferences("MyNotesPreferences", Context.MODE_PRIVATE).getString("sortfield", "date");

        NoteDataSource ds = new NoteDataSource(this);
        try {
            ds.open();
            notes = ds.getNotes(sortBy);
            ds.close();
            adapter = new NoteAdapter(this, notes);
            ListView listView = (ListView) findViewById(R.id.lvNotes);
            listView.setAdapter(adapter);
        }
        catch (Exception e) {
            Toast.makeText(this, "Error retrieving notes", Toast.LENGTH_LONG).show();
        }

        if (notes.size() > 0) {
            ListView listView = (ListView) findViewById(R.id.lvNotes);
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> parent, View itemClicked, int position, long id) {
                    Note selectedNote = notes.get(position);
                    adapter.showDelete(position, itemClicked, ListActivity.this, selectedNote);
                    Intent intent = new Intent(ListActivity.this, NoteActivity.class);
                    intent.putExtra("noteid", selectedNote.getNoteID());
                    startActivity(intent);
                }
            });
        }
        else {
            Intent intent = new Intent(ListActivity.this, NoteActivity.class);
            startActivity(intent);
        }
    }

    private void initNewButton() {
        Button bNew = findViewById(R.id.button3);
        bNew.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(ListActivity.this, NoteActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });
    }
}
