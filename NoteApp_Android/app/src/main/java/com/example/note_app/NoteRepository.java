package com.example.note_app;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NoteRepository {

    public interface ResultCallback<T> {
        void onSuccess(T data);
        void onError(String message);
    }

    private final ApiService api = ApiClient.getService();

    private static String msg(Throwable t) {
        return t.getMessage() == null ? "Network error" : t.getMessage();
    }

    /** GET calls that return JSON we parse. */
    private <T> void enqueue(Call<T> call, ResultCallback<T> cb) {
        call.enqueue(new Callback<T>() {
            @Override public void onResponse(Call<T> c, Response<T> r) {
                if (r.isSuccessful()) cb.onSuccess(r.body());
                else cb.onError("Server error " + r.code());
            }
            @Override public void onFailure(Call<T> c, Throwable t) { cb.onError(msg(t)); }
        });
    }

    /** create / update / delete: success = HTTP 2xx, body ignored. */
    private void enqueueSimple(Call<ResponseBody> call, ResultCallback<Void> cb) {
        call.enqueue(new Callback<ResponseBody>() {
            @Override public void onResponse(Call<ResponseBody> c, Response<ResponseBody> r) {
                if (r.isSuccessful()) cb.onSuccess(null);
                else cb.onError("Server error " + r.code());
            }
            @Override public void onFailure(Call<ResponseBody> c, Throwable t) { cb.onError(msg(t)); }
        });
    }

    private static Map<String, Object> idBody(int id) {
        Map<String, Object> m = new HashMap<>();
        m.put("id", id);
        return m;
    }

    // ---------------- Notes ----------------

    /** Loads notes, then fills in category names (your API only returns category_id). */
    public void getNotes(ResultCallback<List<Note>> cb) {
        enqueue(api.getNotes(), new ResultCallback<List<Note>>() {
            @Override public void onSuccess(List<Note> notes) {
                List<Note> list = notes == null ? new ArrayList<>() : notes;
                api.getCategories().enqueue(new Callback<List<Category>>() {
                    @Override public void onResponse(Call<List<Category>> c, Response<List<Category>> r) {
                        if (r.isSuccessful() && r.body() != null) fillNames(list, r.body());
                        cb.onSuccess(list);
                    }
                    @Override public void onFailure(Call<List<Category>> c, Throwable t) {
                        cb.onSuccess(list);
                    }
                });
            }
            @Override public void onError(String message) { cb.onError(message); }
        });
    }

    private static void fillNames(List<Note> notes, List<Category> cats) {
        for (Note n : notes) {
            if (n.getCategoryId() == null) continue;
            for (Category c : cats) {
                if (c.getId() == n.getCategoryId()) { n.setCategoryName(c.getName()); break; }
            }
        }
    }

    public void getNote(int id, ResultCallback<Note> cb) {
        getNotes(new ResultCallback<List<Note>>() {
            @Override public void onSuccess(List<Note> data) {
                for (Note n : data) if (n.getId() == id) { cb.onSuccess(n); return; }
                cb.onError("Note not found");
            }
            @Override public void onError(String m) { cb.onError(m); }
        });
    }

    public void searchNotes(String q, ResultCallback<List<Note>> cb) {
        String query = q == null ? "" : q.toLowerCase();
        getNotes(new ResultCallback<List<Note>>() {
            @Override public void onSuccess(List<Note> data) {
                List<Note> out = new ArrayList<>();
                for (Note n : data) {
                    if (n.getTitle().toLowerCase().contains(query)
                            || n.getContent().toLowerCase().contains(query)) out.add(n);
                }
                cb.onSuccess(out);
            }
            @Override public void onError(String m) { cb.onError(m); }
        });
    }

    public void saveNote(Note n, ResultCallback<Note> cb) {
        Call<ResponseBody> call = n.getId() == 0 ? api.createNote(n) : api.updateNote(n);
        enqueueSimple(call, new ResultCallback<Void>() {
            @Override public void onSuccess(Void d) { cb.onSuccess(n); }
            @Override public void onError(String m) { cb.onError(m); }
        });
    }

    public void deleteNote(int id, ResultCallback<Void> cb) {
        enqueueSimple(api.deleteNote(idBody(id)), cb);
    }

    // ---------------- Categories ----------------

    public void getCategories(ResultCallback<List<Category>> cb) {
        enqueue(api.getCategories(), cb);
    }

    public void saveCategory(Category c, ResultCallback<Category> cb) {
        Call<ResponseBody> call = c.getId() == 0 ? api.createCategory(c) : api.updateCategory(c);
        enqueueSimple(call, new ResultCallback<Void>() {
            @Override public void onSuccess(Void d) { cb.onSuccess(c); }
            @Override public void onError(String m) { cb.onError(m); }
        });
    }

    public void deleteCategory(int id, ResultCallback<Void> cb) {
        enqueueSimple(api.deleteCategory(idBody(id)), cb);
    }
}
