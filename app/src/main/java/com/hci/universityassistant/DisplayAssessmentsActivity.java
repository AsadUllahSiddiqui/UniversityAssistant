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

public class DisplayAssessmentsActivity extends AppCompatActivity
{

    RecyclerView recyclerView;
    ArrayList<AssessmentModel> assessmentModels;
    AssessmentAdapter adapter;
    EditText search;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_assessments);

        assessmentModels = new ArrayList<>();

//        assessmentModels.add(new AssessmentModel("Human Computer Interaction", "Deliverable IV", "Complete the report and implementation", "Sunday, December 13, 2020"));
//        assessmentModels.add(new AssessmentModel("Human Computer Interaction", "Deliverable V", "Complete the report and implementation", "Sunday, December 13, 2020"));

        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        boolean allowManagement = false;

        if (getIntent().getStringExtra("allowManagement")!=null)
            allowManagement = true;

        adapter = new AssessmentAdapter(this, assessmentModels, allowManagement);

        recyclerView.setAdapter(adapter);

        getAssessmentsFromFirebase();

        search = findViewById(R.id.search_edit_text);

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

    private void getAssessmentsFromFirebase()
    {
        DatabaseReference assessmentsReference = FirebaseDatabase.getInstance().getReference().child("assessments");
        assessmentsReference.addListenerForSingleValueEvent(new ValueEventListener()
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
                        String description = snapshot.child("description").getValue().toString();
                        String title = snapshot.child("title").getValue().toString();
                        String dueDate = snapshot.child("dueDate").getValue().toString();

                        assessmentModels.add(new AssessmentModel(uID, subject, title, description, dueDate));
                        adapter.notifyDataSetChanged();

                        if (assessmentModels.size()>3)
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
        ArrayList<AssessmentModel> filteredAssessments = new ArrayList<>();

        for (AssessmentModel assessment : assessmentModels)
        {
            if ((assessment.getTitle()).toLowerCase().contains(toString.toLowerCase())
            || (assessment.getSubject()).toLowerCase().contains(toString.toLowerCase()))
            {
                filteredAssessments.add(assessment);
            }
        }

        adapter.filterList(filteredAssessments);
    }
}