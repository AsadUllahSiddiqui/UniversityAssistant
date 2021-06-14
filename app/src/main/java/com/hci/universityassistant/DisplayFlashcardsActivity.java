package com.hci.universityassistant;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class DisplayFlashcardsActivity extends AppCompatActivity
{

    RecyclerView recyclerView;
    ArrayList<FlashcardModel> flashcardModels;
    FlashcardAdapter adapter;
    EditText search;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_flashcards);

        flashcardModels = new ArrayList<>();

//        assessmentModels.add(new AssessmentModel("Human Computer Interaction", "Deliverable IV", "Complete the report and implementation", "Sunday, December 13, 2020"));
//        assessmentModels.add(new AssessmentModel("Human Computer Interaction", "Deliverable V", "Complete the report and implementation", "Sunday, December 13, 2020"));

        recyclerView = findViewById(R.id.flashcard_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        boolean allowManagement = false;

        if (getIntent().getStringExtra("allowManagement") != null)
            allowManagement = true;

        adapter = new FlashcardAdapter(this, flashcardModels, allowManagement);

        recyclerView.setAdapter(adapter);

        getFlashcardsFromFirebase();

        search = findViewById(R.id.flashcard_search_edit_text);

        search.addTextChangedListener(new TextWatcher()
        {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after)
            {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count)
            {

            }

            @Override
            public void afterTextChanged(Editable s)
            {
                filter(s.toString());
            }
        });
    }

    private void getFlashcardsFromFirebase()
    {
        DatabaseReference flashcardsReference = FirebaseDatabase.getInstance().getReference().child("flashcards");
        flashcardsReference.addListenerForSingleValueEvent(new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                if (dataSnapshot.exists())
                {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren())
                    {
                        String uID = snapshot.getKey();
                        String subject = snapshot.child("subject").getValue().toString();
                        String front = snapshot.child("front").getValue().toString();
                        String back = snapshot.child("back").getValue().toString();
                        String notes = snapshot.child("notes").getValue().toString();

                        flashcardModels.add(new FlashcardModel(uID, subject, front, back, notes));
                        adapter.notifyDataSetChanged();

                        if (flashcardModels.size() > 3)
                        {
                            recyclerView.smoothScrollToPosition(adapter.getItemCount() - 1);
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError)
            {

            }
        });
    }

    private void filter(String toString)
    {
        ArrayList<FlashcardModel> filteredFlashcards = new ArrayList<>();

        for (FlashcardModel flashcard : flashcardModels)
        {
            if ((flashcard.getFront()).toLowerCase().contains(toString.toLowerCase())
                    || (flashcard.getSubject()).toLowerCase().contains(toString.toLowerCase())
                    || (flashcard.getNotes()).toLowerCase().contains(toString.toLowerCase())
            )
            {
                filteredFlashcards.add(flashcard);
            }
        }

        adapter.filterList(filteredFlashcards);
    }
}