package com.example.note_app.categories;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.note_app.Category;
import com.example.note_app.R;
import java.util.ArrayList;
import java.util.List;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.VH> {

    public interface Listener {
        void onEdit(Category category);
        void onDelete(Category category);
    }

    private final List<Category> items = new ArrayList<>();
    private final Listener listener;

    public CategoryAdapter(Listener listener) { this.listener = listener; }

    public void setCategories(List<Category> list) {
        items.clear();
        if (list != null) items.addAll(list);
        notifyDataSetChanged();
    }

    @NonNull @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new VH(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_category, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull VH h, int position) {
        Category c = items.get(position);
        h.tvName.setText(c.getName());
        h.tvDesc.setText(c.getDescription());
        h.tvDesc.setVisibility(c.getDescription().isEmpty() ? View.GONE : View.VISIBLE);
        h.btnEdit.setOnClickListener(v -> listener.onEdit(c));
        h.btnDelete.setOnClickListener(v -> listener.onDelete(c));
    }

    @Override public int getItemCount() { return items.size(); }

    static class VH extends RecyclerView.ViewHolder {
        final TextView tvName, tvDesc;
        final ImageButton btnEdit, btnDelete;
        VH(View v) {
            super(v);
            tvName = v.findViewById(R.id.tvCategoryName);
            tvDesc = v.findViewById(R.id.tvCategoryDescription);
            btnEdit = v.findViewById(R.id.btnEdit);
            btnDelete = v.findViewById(R.id.btnDelete);
        }
    }
}
