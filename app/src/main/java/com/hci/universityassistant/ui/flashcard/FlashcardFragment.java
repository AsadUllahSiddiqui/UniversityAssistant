package com.hci.universityassistant.ui.flashcard;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.hci.universityassistant.AddFlashcardFragment;
import com.hci.universityassistant.DisplayFlashcardsActivity;
import com.hci.universityassistant.R;

public class FlashcardFragment extends Fragment
{

    private FlashcardViewModel flashcardViewModel;

    TextView add_flashcard_button, view_flashcards_button, manage_flashcards_button;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState)
    {
        flashcardViewModel =
                new ViewModelProvider(this).get(FlashcardViewModel.class);
        View root = inflater.inflate(R.layout.fragment_flashcards, container, false);

        //        final TextView textView = root.findViewById(R.id.text_gallery);

        flashcardViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>()
        {
            @Override
            public void onChanged(@Nullable String s)
            {
                /* No action on text change */
            }
        });

        add_flashcard_button = root.findViewById(R.id.add_flashcard_button);

        add_flashcard_button.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Fragment fragment = new AddFlashcardFragment();
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.hide(getParentFragment());
                fragmentTransaction.replace(R.id.main_fragment_holder, fragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });

        view_flashcards_button = root.findViewById(R.id.view_flashcards_button);

        view_flashcards_button.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(getActivity(), DisplayFlashcardsActivity.class);
                startActivity(intent);
            }
        });

        manage_flashcards_button = root.findViewById(R.id.manage_flashcards_button);

        manage_flashcards_button.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(getActivity(), DisplayFlashcardsActivity.class);
                intent.putExtra("allowManagement", "true");
                startActivity(intent);
            }
        });

        return root;
    }
}