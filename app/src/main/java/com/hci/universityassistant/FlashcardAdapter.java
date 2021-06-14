package com.hci.universityassistant;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class FlashcardAdapter extends RecyclerView.Adapter<FlashcardAdapter.FlashcardHolder>
{

    ArrayList<FlashcardModel> flashcardModels;
    boolean allowManagement;
    Context context;

    public FlashcardAdapter(Context context, ArrayList<FlashcardModel> flashcardModels, boolean allowManagement)
    {
        this.flashcardModels = flashcardModels;
        this.allowManagement = allowManagement;
        this.context = context;
    }

    @NonNull
    @Override
    public FlashcardHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());

        View view = layoutInflater.inflate(R.layout.flashcard_item, parent, false);

        return new FlashcardHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FlashcardHolder holder, int position)
    {
        holder.subject.setText(flashcardModels.get(position).getSubject());
        holder.front.setText(flashcardModels.get(position).getFront());
//        holder.back.setText("Back: " + flashcardModels.get(position).getSubject());
        holder.notes.setText("Notes: " + flashcardModels.get(position).getNotes());

        ImageView editButton = holder.buttonLayout.findViewById(R.id.edit_button);
        ImageView deleteButton = holder.buttonLayout.findViewById(R.id.delete_button);

        editButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(context, DashboardActivity.class);
                intent.putExtra("goto", "EDIT_FLASHCARD");
                intent.putExtra("key", flashcardModels.get(position).getuID());
                intent.putExtra("front", flashcardModels.get(position).getFront());
                intent.putExtra("subject", flashcardModels.get(position).getSubject());
                intent.putExtra("back", flashcardModels.get(position).getBack());
                intent.putExtra("notes", flashcardModels.get(position).getNotes());
                context.startActivity(intent);
                return;
            }
        });

        deleteButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                new AlertDialog.Builder(context)
                        .setTitle("Confirm Deletion")
                        .setMessage("Do you really want to delete this flashcard?")
                        .setIcon(R.drawable.delete)
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                DatabaseReference deletionReference = FirebaseDatabase.getInstance().getReference().child("flashcards");
                                deletionReference.child(flashcardModels.get(position).getuID()).removeValue();

                                flashcardModels.remove(position);
                                notifyDataSetChanged();

                                Toast.makeText(context, "Flashcard deleted successfully!", Toast.LENGTH_SHORT).show();
                            }})
                        .setNegativeButton(android.R.string.no, null).show();
            }
        });

        holder.itemLayout.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                holder.back.setText("Back: " + flashcardModels.get(position).getBack());
            }
        });

        if (!allowManagement)
        {
            editButton.setVisibility(ImageView.INVISIBLE);
            deleteButton.setVisibility(ImageView.INVISIBLE);
        }
    }

    @Override
    public int getItemCount()
    {
        return flashcardModels.size();
    }

    class FlashcardHolder extends RecyclerView.ViewHolder
    {

        TextView subject, front, back, notes;
        LinearLayout buttonLayout;
        LinearLayout itemLayout;

        public FlashcardHolder(@NonNull View itemView)
        {
            super(itemView);

            subject = itemView.findViewById(R.id.flashcard_item_subject);
            front = itemView.findViewById(R.id.flashcard_item_front);
            back = itemView.findViewById(R.id.flashcard_item_back);
            notes = itemView.findViewById(R.id.flashcard_item_notes);

            buttonLayout = itemView.findViewById(R.id.button_layout);
            itemLayout = itemView.findViewById(R.id.item_layout);
        }
    }

    public void filterList(ArrayList<FlashcardModel> filteredModels)
    {
        this.flashcardModels = filteredModels;
        notifyDataSetChanged();
    }
}
