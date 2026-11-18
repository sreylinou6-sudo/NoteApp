package com.example.note_app;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;

public class NoteAdapter extends RecyclerView.Adapter<NoteAdapter.VH> {

    public interface Listener {
        void onClick(Note note);
        void onDelete(Note note);
        default void onEdit(Note note) {}
    }

    private final List<Note> notes = new ArrayList<>();
    private final Listener listener;

    public NoteAdapter(Listener listener) {
        this.listener = listener;
    }

    public void setNotes(List<Note> data) {
        notes.clear();
        if (data != null) notes.addAll(data);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_note, parent, false);
        return new VH(v);
    }

    @Override
    public void onBindViewHolder(@NonNull VH h, int position) {
        Note note = notes.get(position);
        h.tvTitle.setText(note.getTitle());
        h.tvContent.setText(note.getContent());
        String cat = note.getCategoryName();
        h.tvCategory.setText(cat == null || cat.trim().isEmpty() ? "Uncategorized" : cat);
        h.tvDate.setText(note.getDisplayDate());

        h.itemView.setOnClickListener(v -> listener.onClick(note));
        h.btnEdit.setOnClickListener(v -> listener.onEdit(note));
        h.btnDelete.setOnClickListener(v -> listener.onDelete(note));
    }

    @Override
    public int getItemCount() {
        return notes.size();
    }

    static class VH extends RecyclerView.ViewHolder {
        final TextView tvTitle, tvContent, tvCategory, tvDate;
        final ImageButton btnEdit, btnDelete;

        VH(@NonNull View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tvTitle);
            tvContent = itemView.findViewById(R.id.tvContent);
            tvCategory = itemView.findViewById(R.id.tvCategory);
            tvDate = itemView.findViewById(R.id.tvDate);
            btnEdit = itemView.findViewById(R.id.btnEdit);
            btnDelete = itemView.findViewById(R.id.btnDelete);
        }
    }
}