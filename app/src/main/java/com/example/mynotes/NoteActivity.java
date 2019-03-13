package com.example.mynotes;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import java.util.Date;

public class NoteActivity extends AppCompatActivity {

    private Note currentNote;
    RadioButton rbHigh, rbMed, rbLow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note);

        initListButton();
        initSaveButton();
        initImportanceClick();
        initTextChangedEvents();

        rbHigh = (RadioButton) findViewById(R.id.radioButtonHigh);
        rbMed = (RadioButton) findViewById(R.id.radioButtonMedium);
        rbLow = (RadioButton) findViewById(R.id.radioButtonLow);

        Bundle extras = getIntent().getExtras();
        if(extras != null) {
            initNote(extras.getInt("noteid"));
        }
        else {
            currentNote = new Note();
            currentNote.setDate(new Date());
            rbLow.toggle();
        }
    }

    private void initNote(int id) {

        NoteDataSource ds = new NoteDataSource(NoteActivity.this);
        try {
            ds.open();
            currentNote = ds.getSpecificNote(id);
            ds.close();
        }
        catch (Exception e) {
            Toast.makeText(this, "Load Note Failed", Toast.LENGTH_LONG).show();
        }

        EditText editTitle = (EditText) findViewById(R.id.editTitle);
        EditText editContent = (EditText) findViewById(R.id.editContent);

        editTitle.setText(currentNote.getTitle());
        editContent.setText(currentNote.getContent() + currentNote.getDate());

        switch (currentNote.getImportance()){
            case 3: rbHigh.toggle();
            break;
            case 2: rbMed.toggle();
            break;
            default: rbLow.toggle();
        }

    }

    private void initListButton() {
        Button bNew = findViewById(R.id.button2);
        bNew.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(NoteActivity.this, ListActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });
    }

    private void initSaveButton() {
        Button bSave = findViewById(R.id.button);
        bSave.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                boolean wasSuccessful = false;
                NoteDataSource ds = new NoteDataSource(NoteActivity.this);
                try {
                    ds.open();

                    if (currentNote.getNoteID() == -1) {
                        wasSuccessful = ds.insertNote(currentNote);
                        int newId = ds.getLastNoteId();
                        currentNote.setNoteID(newId);
                    } else {
                        wasSuccessful = ds.updateNote(currentNote);
                    }
                    ds.close();
                }
                catch (Exception e) {
                    wasSuccessful = false;
                }

                Intent intent = new Intent(NoteActivity.this, ListActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);

            }
        });
    }

    private void initImportanceClick() {
        RadioGroup bgImportance = (RadioGroup) findViewById(R.id.radioGroupImportance);

        bgImportance.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup arg0, int arg1) {
                if (rbHigh.isChecked())
                    currentNote.setImportance(3);
                else if (rbMed.isChecked())
                    currentNote.setImportance(2);
                else if (rbLow.isChecked())
                    currentNote.setImportance(1);
            }
        });
    }

    private void initTextChangedEvents() {
        final EditText etTitle = (EditText) findViewById(R.id.editTitle);
        etTitle.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {
                currentNote.setTitle(etTitle.getText().toString());
            }

            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
                //  Auto-generated method stub
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //  Auto-generated method stub
            }
        });

        final EditText etContent = (EditText) findViewById(R.id.editContent);
        etContent.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {
                currentNote.setContent(etContent.getText().toString());
            }

            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
                //  Auto-generated method stub
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //  Auto-generated method stub
            }
        });
    }
}
