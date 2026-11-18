package com.example.note_app.note;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.note_app.Category;
import com.example.note_app.Note;
import com.example.note_app.NoteRepository;
import com.example.note_app.R;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.MaterialAutoCompleteTextView;
import com.google.android.material.textfield.TextInputEditText;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class AddEditNoteActivity extends AppCompatActivity {

    public static final String EXTRA_NOTE = "note";

    private final NoteRepository repo = new NoteRepository();
    private final List<Category> categories = new ArrayList<>();
    private TextInputEditText etTitle, etContent;
    private MaterialAutoCompleteTextView actvCategory;
    private MaterialButton btnSave;
    private Category selected;
    private Note editing;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit_note);

        MaterialToolbar toolbar = findViewById(R.id.toolbar);
        etTitle = findViewById(R.id.etTitle);
        etContent = findViewById(R.id.etContent);
        actvCategory = findViewById(R.id.actvCategory);
        btnSave = findViewById(R.id.btnSave);

        toolbar.setNavigationOnClickListener(v -> finish());

        editing = (Note) getIntent().getSerializableExtra(EXTRA_NOTE);
        if (editing != null) {
            toolbar.setTitle(R.string.edit_note);
            etTitle.setText(editing.getTitle());
            etContent.setText(editing.getContent());
        }

        actvCategory.setOnItemClickListener((parent, v, pos, id) -> selected = categories.get(pos));
        btnSave.setOnClickListener(v -> save());
        loadCategories();
    }

    private void loadCategories() {
        repo.getCategories(new NoteRepository.ResultCallback<List<Category>>() {
            @Override public void onSuccess(List<Category> data) {
                categories.clear();
                if (data != null) categories.addAll(data);
                actvCategory.setAdapter(new ArrayAdapter<>(AddEditNoteActivity.this,
                        android.R.layout.simple_list_item_1, categories));
                if (editing != null && editing.getCategoryId() != null) {
                    for (Category c : categories) {
                        if (Objects.equals(c.getId(), editing.getCategoryId())) {
                            selected = c;
                            actvCategory.setText(c.getName(), false);
                            break;
                        }
                    }
                }
            }
            @Override public void onError(String m) {
                Toast.makeText(AddEditNoteActivity.this, m, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void save() {
        String title = String.valueOf(etTitle.getText()).trim();
        if (title.isEmpty()) {
            etTitle.requestFocus();
            Toast.makeText(this, R.string.title, Toast.LENGTH_SHORT).show();
            return;
        }
        Note n = editing != null ? editing : new Note();
        n.setTitle(title);
        n.setContent(String.valueOf(etContent.getText()).trim());
        n.setCategoryId(selected != null ? selected.getId() : null);
        n.setCategoryName(selected != null ? selected.getName() : null);

        btnSave.setEnabled(false);
        repo.saveNote(n, new NoteRepository.ResultCallback<Note>() {
            @Override public void onSuccess(Note data) {
                setResult(RESULT_OK);   // tell the previous screen to reload
                finish();
            }
            @Override public void onError(String m) {
                btnSave.setEnabled(true);
                Toast.makeText(AddEditNoteActivity.this, m, Toast.LENGTH_SHORT).show();
            }
        });
    }
}