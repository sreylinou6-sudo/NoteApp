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
import java.util.Map;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.HTTP;
import retrofit2.http.POST;
import retrofit2.http.PUT;

public interface ApiService {

    // ----- Notes -----
    @GET(Endpoints.NOTE_READ)
    Call<List<Note>> getNotes();

    @POST(Endpoints.NOTE_CREATE)
    Call<ResponseBody> createNote(@Body Note note);

    @PUT(Endpoints.NOTE_UPDATE)
    Call<ResponseBody> updateNote(@Body Note note);

    @HTTP(method = "DELETE", path = Endpoints.NOTE_DELETE, hasBody = true)
    Call<ResponseBody> deleteNote(@Body Map<String, Object> body);

    // ----- Categories -----
    @GET(Endpoints.CATEGORY_READ)
    Call<List<Category>> getCategories();

    @POST(Endpoints.CATEGORY_CREATE)
    Call<ResponseBody> createCategory(@Body Category category);

    @PUT(Endpoints.CATEGORY_UPDATE)
    Call<ResponseBody> updateCategory(@Body Category category);

    @HTTP(method = "DELETE", path = Endpoints.CATEGORY_DELETE, hasBody = true)
    Call<ResponseBody> deleteCategory(@Body Map<String, Object> body);

    class NoteAdapter extends RecyclerView.Adapter<NoteAdapter.VH> {

        public interface Listener {
            void onClick(Note note);
            void onDelete(Note note);
            // default so other screens (e.g. SearchActivity) still compile without changes
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
}
