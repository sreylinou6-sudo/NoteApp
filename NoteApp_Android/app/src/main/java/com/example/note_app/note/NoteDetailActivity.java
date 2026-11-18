package com.example.note_app.note;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.note_app.Note;
import com.example.note_app.NoteRepository;
import com.example.note_app.R;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class NoteDetailActivity extends AppCompatActivity {

    public static final String EXTRA_NOTE = "note";

    private final NoteRepository repo = new NoteRepository();
    private TextView tvTitle, tvContent, tvCategory, tvDate;
    private Note note;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_detail);

        note = (Note) getIntent().getSerializableExtra(EXTRA_NOTE);
        if (note == null) { finish(); return; }

        MaterialToolbar toolbar = findViewById(R.id.toolbar);
        tvTitle = findViewById(R.id.tvTitle);
        tvContent = findViewById(R.id.tvContent);
        tvCategory = findViewById(R.id.tvCategory);
        tvDate = findViewById(R.id.tvDate);
        FloatingActionButton fabEdit = findViewById(R.id.fabEdit);
        FloatingActionButton fabDelete = findViewById(R.id.fabDelete);

        toolbar.setNavigationOnClickListener(v -> finish());

        fabEdit.setOnClickListener(v -> {
            Intent i = new Intent(this, AddEditNoteActivity.class);
            i.putExtra(AddEditNoteActivity.EXTRA_NOTE, note);
            startActivity(i);
        });
        fabDelete.setOnClickListener(v -> confirmDelete());
        bind();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (note == null) return;
        // Refresh after returning from the edit screen
        repo.getNote(note.getId(), new NoteRepository.ResultCallback<Note>() {
            @Override public void onSuccess(Note data) {
                if (data != null) { note = data; bind(); }
            }
            @Override public void onError(String m) { /* keep showing local copy */ }
        });
    }

    private void bind() {
        tvTitle.setText(note.getTitle());
        tvContent.setText(note.getContent());
        tvDate.setText(note.getDisplayDate());
        String cat = note.getCategoryName();
        tvCategory.setText(cat == null || cat.isEmpty() ? getString(R.string.uncategorized) : cat);
    }

    private void confirmDelete() {
        new MaterialAlertDialogBuilder(this)
                .setTitle(R.string.delete)
                .setMessage(note.getTitle())
                .setNegativeButton(android.R.string.cancel, null)
                .setPositiveButton(R.string.delete, (d, w) ->
                        repo.deleteNote(note.getId(), new NoteRepository.ResultCallback<Void>() {
                            @Override public void onSuccess(Void data) { finish(); }
                            @Override public void onError(String m) {
                                Toast.makeText(NoteDetailActivity.this, m, Toast.LENGTH_SHORT).show();
                            }
                        }))
                .show();
    }
}
