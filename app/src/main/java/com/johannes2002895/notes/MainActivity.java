package com.johannes2002895.notes;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.ProgressBar;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.johannes2002895.notes.adapter.Adapter;
import com.johannes2002895.notes.db.NoteHelper;
import com.johannes2002895.notes.entity.MyNotes;

import java.lang.ref.WeakReference;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import com.johannes2002895.notes.helper.MappingHelper;

public class MainActivity extends AppCompatActivity implements LoadNotesCallback {
    private ProgressBar progressBar;
    private RecyclerView rvNotes;
    private Adapter adapter;
    private static final String EXTRA_STATE = "EXTRA_STATE";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if(getSupportActionBar() != null){
            getSupportActionBar().setTitle("My Notes");

        }
        progressBar = findViewById(R.id.progressbar);
        rvNotes = findViewById(R.id.rv_notes);
        rvNotes.setLayoutManager(new LinearLayoutManager(this));
        rvNotes.setHasFixedSize(true);
        adapter  = new Adapter(this);
        rvNotes.setAdapter(adapter);
        FloatingActionButton fabAdd = findViewById(R.id.fab_add);

        fabAdd.setOnClickListener(view ->{
            Intent intent = new Intent(MainActivity.this, NoteAddUpdateActivity.class);
            startActivityForResult(intent, NoteAddUpdateActivity.REQUEST_ADD);
        });
        if(savedInstanceState == null){
            new LoadNotesAsync(this,this).execute();
        }else{
            ArrayList<MyNotes> list = savedInstanceState.getParcelableArrayList(EXTRA_STATE);
            if (list != null) {
                adapter.setMyNotes(list);
            }
        }
    }

    @Override
    public void preExecute(){
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void postExecute(ArrayList<MyNotes> notes) {
        progressBar.setVisibility(View.INVISIBLE);
        if(notes.size() > 0){
            adapter.setMyNotes(notes);
        }else{
            adapter.setMyNotes(new ArrayList<>());
            showSnackbarMessage("No Data");
        }
    }
    private void showSnackbarMessage(String message){
        Snackbar.make(rvNotes,message,Snackbar.LENGTH_SHORT).show();

    }
    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(EXTRA_STATE,adapter.getNotes());
    }

    private class LoadNotesAsync {
        private final WeakReference<Context> weakContext;
        private final WeakReference<LoadNotesCallback> weakCallback;
        public LoadNotesAsync(Context context, LoadNotesCallback loadNotesCallback) {
            weakContext = new WeakReference<>(context);
            weakCallback = new WeakReference<>(loadNotesCallback);
        }
        void execute(){


            ExecutorService executorService = Executors.newSingleThreadExecutor();
            Handler handler = new Handler(Looper.getMainLooper());
            weakCallback.get().preExecute();
            executorService.execute(()->{
                Context context = weakContext.get();
                NoteHelper noteHelper = NoteHelper.getInstance(context);
                noteHelper.open();
                //memasukan datanya disini cuy
                Cursor dataCursor = noteHelper.queryAll();
                ArrayList<MyNotes> myNotes = MappingHelper.mapCursorToArrayList(dataCursor);
                noteHelper.close();
                handler.post(() ->weakCallback.get().postExecute(myNotes));
            });






        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null) {
            // Akan dipanggil jika request codenya ADD
            if (requestCode == NoteAddUpdateActivity.REQUEST_ADD) {
                if (resultCode == NoteAddUpdateActivity.RESULT_ADD) {
                    MyNotes coffeeDrinkNote = data.getParcelableExtra(NoteAddUpdateActivity.EXTRA_NOTE);

                    adapter.addItem(coffeeDrinkNote);
                    rvNotes.smoothScrollToPosition(adapter.getItemCount() - 1);

                    showSnackbarMessage("Satu item berhasil ditambahkan");
                }
            }
            // Update dan Delete memiliki request code sama akan tetapi result codenya berbeda
            else if (requestCode == NoteAddUpdateActivity.REQUEST_UPDATE) {
                /*
                Akan dipanggil jika result codenya  UPDATE
                Semua data di load kembali dari awal
                */
                if (resultCode == NoteAddUpdateActivity.RESULT_UPDATE) {

                    MyNotes myNotes = data.getParcelableExtra(NoteAddUpdateActivity.EXTRA_NOTE);
                    int position = data.getIntExtra(NoteAddUpdateActivity.EXTRA_POSITION, 0);

                    adapter.updateItem(position, myNotes);
                    rvNotes.smoothScrollToPosition(position);

                    showSnackbarMessage("Satu item berhasil diubah");
                }
                /*
                Akan dipanggil jika result codenya DELETE
                Delete akan menghapus data dari list berdasarkan dari position
                */
                else if (resultCode == NoteAddUpdateActivity.RESULT_DELETE) {
                    int position = data.getIntExtra(NoteAddUpdateActivity.EXTRA_POSITION, 0);

                    adapter.deleteItem(position);

                    showSnackbarMessage("Satu item berhasil dihapus");
                }
            }
        }



    }
}



interface  LoadNotesCallback{
    void preExecute();
    void postExecute(ArrayList<MyNotes> notes);
}