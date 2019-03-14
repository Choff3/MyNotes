package com.example.mynotes;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import java.util.ArrayList;

public class ListActivity extends AppCompatActivity {

    ArrayList<Note> notes;
    NoteAdapter adapter;
    Note selectedNote;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        initSortByClick();
        initNewButton();
        initSettings();
    }

    @Override
    public void onResume() {
        super.onResume();
        String sortBy = getSharedPreferences("MyNoteListPreferences", Context.MODE_PRIVATE).getString("sortfield", "date");

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

                    adapter.Delete(position, itemClicked, ListActivity.this, selectedNote);
                    selectedNote = notes.get(position);
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

    private void initSortByClick() {
        RadioGroup rgSortBy = (RadioGroup) findViewById(R.id.radioGroupSortBy);
        rgSortBy.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup arg0, int arg1) {
                RadioButton rbImp = (RadioButton) findViewById(R.id.radioButtonImportance);
                if (rbImp.isChecked()) {
                    getSharedPreferences("MyNoteListPreferences", Context.MODE_PRIVATE).edit().putString("sortfield", "importance").commit();
                    onResume();
                }
                else {
                    getSharedPreferences("MyNoteListPreferences", Context.MODE_PRIVATE).edit().putString("sortfield", "date").commit();
                    onResume();
                }
            }
        });
    }

    private void initSettings() {
        String sortBy = getSharedPreferences("MyNoteListPreferences", Context.MODE_PRIVATE).getString("sortfield","date");

        RadioButton rbDate = (RadioButton) findViewById(R.id.radioButtonDate);
        RadioButton rbImp = (RadioButton) findViewById(R.id.radioButtonImportance);

        if (sortBy.equalsIgnoreCase("importance")) {
            rbImp.toggle();
        }
        else {
            rbDate.toggle();
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
