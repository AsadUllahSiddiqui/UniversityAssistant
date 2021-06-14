package com.hci.universityassistant.ui.flashcard;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class FlashcardViewModel extends ViewModel
{

    private MutableLiveData<String> mText;

    public FlashcardViewModel()
    {
        mText = new MutableLiveData<>();
        mText.setValue("This is gallery fragment");
    }

    public LiveData<String> getText()
    {
        return mText;
    }
}