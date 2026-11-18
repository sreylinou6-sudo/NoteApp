package com.example.note_app.categories;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.note_app.Category;
import com.example.note_app.NoteRepository;
import com.example.note_app.R;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import java.util.List;

public class CategoryActivity extends AppCompatActivity implements CategoryAdapter.Listener {

    private final NoteRepository repo = new NoteRepository();
    private CategoryAdapter adapter;
    private View layoutEmpty;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);

        MaterialToolbar toolbar = findViewById(R.id.toolbar);
        RecyclerView rv = findViewById(R.id.rvCategories);
        layoutEmpty = findViewById(R.id.layoutEmpty);
        ExtendedFloatingActionButton fab = findViewById(R.id.fabAddCategory);

        toolbar.setNavigationOnClickListener(v -> finish());
        adapter = new CategoryAdapter(this);
        rv.setLayoutManager(new LinearLayoutManager(this));
        rv.setAdapter(adapter);

        fab.setOnClickListener(v ->
                startActivity(new Intent(this, AddEditCategoriesActivity.class)));
    }

    @Override
    protected void onResume() {
        super.onResume();
        load();
    }

    private void load() {
        repo.getCategories(new NoteRepository.ResultCallback<List<Category>>() {
            @Override public void onSuccess(List<Category> data) {
                adapter.setCategories(data);
                layoutEmpty.setVisibility(data == null || data.isEmpty() ? View.VISIBLE : View.GONE);
            }
            @Override public void onError(String m) {
                Toast.makeText(CategoryActivity.this, m, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onEdit(Category category) {
        Intent i = new Intent(this, AddEditCategoriesActivity.class);
        i.putExtra(AddEditCategoriesActivity.EXTRA_CATEGORY, category);
        startActivity(i);
    }

    @Override
    public void onDelete(Category category) {
        new MaterialAlertDialogBuilder(this)
                .setTitle(R.string.delete)
                .setMessage(category.getName())
                .setNegativeButton(android.R.string.cancel, null)
                .setPositiveButton(R.string.delete, (d, w) ->
                        repo.deleteCategory(category.getId(), new NoteRepository.ResultCallback<Void>() {
                            @Override public void onSuccess(Void data) { load(); }
                            @Override public void onError(String m) {
                                Toast.makeText(CategoryActivity.this, m, Toast.LENGTH_SHORT).show();
                            }
                        }))
                .show();
    }
}
