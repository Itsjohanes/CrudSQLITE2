package com.johannes2002895.notes.adapter;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.johannes2002895.notes.CustomOnItemClickListener;
import com.johannes2002895.notes.NoteAddUpdateActivity;
import com.johannes2002895.notes.R;
import com.johannes2002895.notes.entity.MyNotes;

import java.util.ArrayList;

public class Adapter extends RecyclerView.Adapter<Adapter.NoteViewHolder> {

    private final ArrayList<MyNotes> myNotes = new ArrayList<>();
    private final Activity activity;
    public Adapter(Activity activity){
        this.activity = activity;
    }
    public ArrayList<MyNotes> getNotes(){
        return this.myNotes;
    }
    public void setMyNotes(ArrayList<MyNotes> myNotes){
        if(myNotes.size()>0){
            this.myNotes.clear();
        }
        this.myNotes.addAll(myNotes);
        notifyDataSetChanged();
    }
    public void addItem(MyNotes myNotes){
        this.myNotes.add(myNotes);
        notifyItemInserted(this.myNotes.size() -1);
    }
    public void updateItem(int position, MyNotes myNotes){
        this.myNotes.set(position,myNotes);
        notifyItemChanged(position, myNotes);
    }
    public void deleteItem(int position){
        this.myNotes.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position,myNotes.size());
    }






    @NonNull
    @Override
    public Adapter.NoteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_notes,parent,false);
        return new NoteViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull Adapter.NoteViewHolder holder, int position) {
        holder.tvTitle.setText(myNotes.get(position).getTitle());
        holder.tvDate.setText(myNotes.get(position).getDate());
        holder.tvDescription.setText(myNotes.get(position).getDescription());
        holder.cvNote.setOnClickListener(new CustomOnItemClickListener(position, (view, position1) -> {
            Intent intent = new Intent(activity, NoteAddUpdateActivity.class);
            intent.putExtra(NoteAddUpdateActivity.EXTRA_POSITION, position1);
            intent.putExtra(NoteAddUpdateActivity.EXTRA_NOTE, myNotes.get(position1));
            activity.startActivityForResult(intent, NoteAddUpdateActivity.REQUEST_UPDATE);
        }));
    }
    @Override
    public int getItemCount() {
        return this.myNotes.size();
    }


    static class NoteViewHolder extends RecyclerView.ViewHolder {
        final TextView tvTitle,tvDescription,tvDate;
        final CardView cvNote;
        public NoteViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tv_item_title);
            tvDescription = itemView.findViewById(R.id.tv_item_description);
            tvDate = itemView.findViewById(R.id.tv_item_date);
            cvNote = itemView.findViewById(R.id.cv_item_note);
        }
    }
}
