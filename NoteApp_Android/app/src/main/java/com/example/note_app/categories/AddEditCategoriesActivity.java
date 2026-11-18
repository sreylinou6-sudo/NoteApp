package com.example.note_app.categories;

import android.os.Bundle;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.note_app.Category;
import com.example.note_app.NoteRepository;
import com.example.note_app.R;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

public class AddEditCategoriesActivity extends AppCompatActivity {

    public static final String EXTRA_CATEGORY = "category";

    private final NoteRepository repo = new NoteRepository();
    private TextInputEditText etName, etDescription;
    private MaterialButton btnSave;
    private Category editing;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit_categories);

        MaterialToolbar toolbar = findViewById(R.id.toolbar);
        etName = findViewById(R.id.etCategoryName);
        etDescription = findViewById(R.id.etCategoryDescription);
        btnSave = findViewById(R.id.btnSave);

        toolbar.setNavigationOnClickListener(v -> finish());

        editing = (Category) getIntent().getSerializableExtra(EXTRA_CATEGORY);
        if (editing != null) {
            toolbar.setTitle(R.string.edit_category);
            etName.setText(editing.getName());
            etDescription.setText(editing.getDescription());
        }
        btnSave.setOnClickListener(v -> save());
    }

    private void save() {
        String name = String.valueOf(etName.getText()).trim();
        if (name.isEmpty()) {
            etName.requestFocus();
            Toast.makeText(this, R.string.category_name, Toast.LENGTH_SHORT).show();
            return;
        }
        Category c = editing != null ? editing : new Category();
        c.setName(name);
        c.setDescription(String.valueOf(etDescription.getText()).trim());

        btnSave.setEnabled(false);
        repo.saveCategory(c, new NoteRepository.ResultCallback<Category>() {
            @Override public void onSuccess(Category data) { finish(); }
            @Override public void onError(String m) {
                btnSave.setEnabled(true);
                Toast.makeText(AddEditCategoriesActivity.this, m, Toast.LENGTH_SHORT).show();
            }
        });
    }
}
