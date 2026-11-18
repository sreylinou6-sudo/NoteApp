package com.example.note_app;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.note_app.categories.CategoryActivity;
import com.example.note_app.note.AddEditNoteActivity;
import com.example.note_app.note.NoteDetailActivity;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements NoteAdapter.Listener {

    private final NoteRepository repo = new NoteRepository();
    private final List<Note> allNotes = new ArrayList<>();
    private NoteAdapter adapter;
    private TextInputEditText etSearch;
    private View layoutEmpty;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RecyclerView rv = findViewById(R.id.rvNotes);
        etSearch = findViewById(R.id.etSearch);
        layoutEmpty = findViewById(R.id.layoutEmpty);
        ExtendedFloatingActionButton fab = findViewById(R.id.fabAddNote);
        MaterialButton btnCategories = findViewById(R.id.btnCategories);

        adapter = new NoteAdapter(this);
        rv.setLayoutManager(new LinearLayoutManager(this));
        rv.setAdapter(adapter);

        fab.setOnClickListener(v -> startActivity(new Intent(this, AddEditNoteActivity.class)));
        btnCategories.setOnClickListener(v -> startActivity(new Intent(this, CategoryActivity.class)));

        // Live filter while typing
        etSearch.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int a, int b, int c) {}
            @Override public void onTextChanged(CharSequence s, int a, int b, int c) { applyFilter(); }
            @Override public void afterTextChanged(Editable s) {}
        });
        // Keyboard "search" key opens full server-side search screen
        etSearch.setOnEditorActionListener((v, actionId, e) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                Intent i = new Intent(this, SearchActivity.class);
                i.putExtra(SearchActivity.EXTRA_QUERY, String.valueOf(etSearch.getText()).trim());
                startActivity(i);
                return true;
            }
            return false;
        });
    }

    // Reloads from the database every time you come back (e.g. after saving an edit)
    @Override
    protected void onResume() {
        super.onResume();
        loadNotes();
    }

    private void loadNotes() {
        repo.getNotes(new NoteRepository.ResultCallback<List<Note>>() {
            @Override public void onSuccess(List<Note> data) {
                allNotes.clear();
                if (data != null) allNotes.addAll(data);
                applyFilter();
            }
            @Override public void onError(String message) {
                Toast.makeText(MainActivity.this, message, Toast.LENGTH_SHORT).show();
                applyFilter();
            }
        });
    }

    private void applyFilter() {
        String q = String.valueOf(etSearch.getText()).trim().toLowerCase();
        List<Note> shown = new ArrayList<>();
        for (Note n : allNotes) {
            if (q.isEmpty() || n.getTitle().toLowerCase().contains(q)
                    || n.getContent().toLowerCase().contains(q)) shown.add(n);
        }
        adapter.setNotes(shown);
        layoutEmpty.setVisibility(shown.isEmpty() ? View.VISIBLE : View.GONE);
    }

    @Override
    public void onClick(Note note) {
        Intent i = new Intent(this, NoteDetailActivity.class);
        i.putExtra(NoteDetailActivity.EXTRA_NOTE, note);
        startActivity(i);
    }

    // NEW: edit button opens the edit screen with this note (its id is kept)
    @Override
    public void onEdit(Note note) {
        Intent i = new Intent(this, AddEditNoteActivity.class);
        i.putExtra(AddEditNoteActivity.EXTRA_NOTE, note);
        startActivity(i);
    }

    @Override
    public void onDelete(Note note) {
        new MaterialAlertDialogBuilder(this)
                .setTitle(R.string.delete)
                .setMessage(note.getTitle())
                .setNegativeButton(android.R.string.cancel, null)
                .setPositiveButton(R.string.delete, (d, w) ->
                        repo.deleteNote(note.getId(), new NoteRepository.ResultCallback<Void>() {
                            @Override public void onSuccess(Void data) { loadNotes(); }
                            @Override public void onError(String m) {
                                Toast.makeText(MainActivity.this, m, Toast.LENGTH_SHORT).show();
                            }
                        }))
                .show();
    }
}