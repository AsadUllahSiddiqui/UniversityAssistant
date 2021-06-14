package com.hci.universityassistant;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.android.material.button.MaterialButton;
import com.google.firebase.database.FirebaseDatabase;

public class AddFlashcardFragment extends Fragment
{
    EditText subject, front, back, notes;
    MaterialButton saveButton, backButton;

    boolean updateExisting = false;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState)
    {
        View root = inflater.inflate(R.layout.fragment_add_flashcard, container, false);

        subject = root.findViewById(R.id.flashcard_subject);
        front = root.findViewById(R.id.flashcard_front);
        back = root.findViewById(R.id.flashcard_back);
        notes = root.findViewById(R.id.flashcard_note);

        Bundle args = getArguments();
        if (args!=null)
        {
            subject.setText(args.getString("subject"));
            front.setText(args.getString("front"));
            back.setText(args.getString("back"));
            notes.setText(args.getString("notes"));
            updateExisting = true;
        }

        saveButton = root.findViewById(R.id.flashcard_save_button);
        backButton = root.findViewById(R.id.flashcard_back_button);

        backButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(getActivity(), DashboardActivity.class);
                intent.putExtra("goto", "FLASHCARD_FRAGMENT");
                startActivity(intent);
                getActivity().finish();
            }
        });

        saveButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                String subjectString, frontString, backString, notesString;

                if (subject.getText().toString().isEmpty())
                {
                    subject.setError("Subject is missing!");
                    return;
                }
                else if (front.getText().toString().isEmpty())
                {
                    front.setError("Front is missing!");
                    return;
                }
                else if (back.getText().toString().isEmpty())
                {
                    back.setError("Back is missing!");
                    return;
                }
                else if (notes.getText().toString().isEmpty())
                {
                    notes.setError("Notes are missing!");
                    return;
                }
                else
                {
                    subjectString = subject.getText().toString();
                    frontString = front.getText().toString();
                    backString = back.getText().toString();
                    notesString = notes.getText().toString();

                    String key = null;
                    if (!updateExisting)
                    {
                        key = FirebaseDatabase.getInstance().getReference().child("flashcards").push().getKey();
                    }
                    else
                    {
                        key = args.getString("key");
                    }

                    FlashcardModel flashcardModel = new FlashcardModel(key, subjectString, frontString, backString, notesString);

                    FirebaseDatabase.getInstance().getReference().child("flashcards").child(key).setValue(flashcardModel);

                    backToDashboard();
                }
            }
        });

        return root;
    }

    private void backToDashboard()
    {
        Intent intent = new Intent(getActivity(), DashboardActivity.class);
        intent.putExtra("status", "FLASHCARD_SAVED");
        intent.putExtra("goto", "FLASHCARD_FRAGMENT");
        startActivity(intent);
        getActivity().finish();
    }

}