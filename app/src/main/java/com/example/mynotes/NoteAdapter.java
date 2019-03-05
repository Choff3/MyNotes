package com.example.mynotes;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class NoteAdapter extends ArrayAdapter<Note> {

    private ArrayList<Note> items;
    private Context adapterContext;

    public NoteAdapter(Context context, ArrayList<Note> items) {
        super(context, R.layout.list_item, items);
        adapterContext = context;
        this.items = items;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;
        try {
            Note note = items.get(position);

            if (v == null) {
                LayoutInflater vi = (LayoutInflater) adapterContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                v = vi.inflate(R.layout.list_item, null);
            }

            TextView noteTitle = (TextView) v.findViewById(R.id.textNoteTitle);
            noteTitle.setText(note.getTitle());
        }
        catch (Exception e) {
            e.printStackTrace();
            e.getCause();
        }

        final Button bDelete = (Button) v.findViewById(R.id.buttonDeleteNote);
        final Note note = items.get(position);

        bDelete.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                items.remove(note);
                deleteOption(note.getNoteID(), adapterContext);
            }
        });

        return v;
    }

    public void Delete(final int position, final View view, final Context context, final Note note) {
        Button bDelete = view.findViewById(R.id.buttonDeleteNote);
        bDelete.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                items.remove(note);
                deleteOption(note.getNoteID(), context);
            }
        });
    }

    private void deleteOption(int noteToDelete, Context context) {
        NoteDataSource db = new NoteDataSource(context);
        try {
            db.open();
            db.deleteNote(noteToDelete);
            db.close();
        }
        catch (Exception e) {
            Toast.makeText(adapterContext, "Delete Note Failed", Toast.LENGTH_LONG).show();
        }
        this.notifyDataSetChanged();
    }

}
