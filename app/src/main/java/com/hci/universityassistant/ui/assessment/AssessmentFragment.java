package com.hci.universityassistant.ui.assessment;

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

import com.hci.universityassistant.AddAssessmentFragment;
import com.hci.universityassistant.DisplayAssessmentsActivity;
import com.hci.universityassistant.R;

public class AssessmentFragment extends Fragment
{

    private AssessmentViewModel assessmentViewModel;

    TextView add_asssment_button, view_assessments_button, manage_assessments_button;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState)
    {
        assessmentViewModel =
                new ViewModelProvider(this).get(AssessmentViewModel.class);

        View root = inflater.inflate(R.layout.fragment_assessments, container, false);

        assessmentViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>()
        {
            @Override
            public void onChanged(@Nullable String s)
            {
                /* No action on text change */
            }
        });

        add_asssment_button = root.findViewById(R.id.add_assessment_button);

        add_asssment_button.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Fragment fragment = new AddAssessmentFragment();
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.hide(getParentFragment());
                fragmentTransaction.replace(R.id.main_fragment_holder, fragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });

        view_assessments_button = root.findViewById(R.id.view_assessments_button);

        view_assessments_button.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(getActivity(), DisplayAssessmentsActivity.class);
                startActivity(intent);
            }
        });

        manage_assessments_button = root.findViewById(R.id.manage_assessments_button);

        manage_assessments_button.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(getActivity(), DisplayAssessmentsActivity.class);
                intent.putExtra("allowManagement", "true");
                startActivity(intent);
            }
        });

        return root;
    }
}