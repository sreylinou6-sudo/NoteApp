package com.example.note_app;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.note_app.note.NoteDetailActivity;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textfield.TextInputEditText;
import java.util.List;

public class SearchActivity extends AppCompatActivity implements NoteAdapter.Listener {

    public static final String EXTRA_QUERY = "query";

    private final NoteRepository repo = new NoteRepository();
    private NoteAdapter adapter;
    private TextInputEditText etSearch;
    private View layoutEmpty;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        MaterialToolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setNavigationOnClickListener(v -> finish());

        etSearch = findViewById(R.id.etSearch);
        layoutEmpty = findViewById(R.id.layoutEmpty);
        RecyclerView rv = findViewById(R.id.rvNotes);
        adapter = new NoteAdapter(this);
        rv.setLayoutManager(new LinearLayoutManager(this));
        rv.setAdapter(adapter);

        etSearch.setOnEditorActionListener((v, actionId, e) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH) { search(); return true; }
            return false;
        });

        String q = getIntent().getStringExtra(EXTRA_QUERY);
        if (q != null && !q.isEmpty()) {
            etSearch.setText(q);
            search();
        }
    }

    private void search() {
        String q = String.valueOf(etSearch.getText()).trim();
        repo.searchNotes(q, new NoteRepository.ResultCallback<List<Note>>() {
            @Override public void onSuccess(List<Note> data) {
                adapter.setNotes(data);
                layoutEmpty.setVisibility(data == null || data.isEmpty() ? View.VISIBLE : View.GONE);
            }
            @Override public void onError(String m) {
                Toast.makeText(SearchActivity.this, m, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onClick(Note note) {
        Intent i = new Intent(this, NoteDetailActivity.class);
        i.putExtra(NoteDetailActivity.EXTRA_NOTE, note);
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
                            @Override public void onSuccess(Void data) { search(); }
                            @Override public void onError(String m) {
                                Toast.makeText(SearchActivity.this, m, Toast.LENGTH_SHORT).show();
                            }
                        }))
                .show();
    }
}
