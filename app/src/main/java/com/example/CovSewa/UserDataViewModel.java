package com.example.CovSewa;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class UserDataViewModel extends ViewModel {
    private final MutableLiveData<UserData> selectedItem = new MutableLiveData<UserData>();
    public void selectItem(UserData item) {
        selectedItem.setValue(item);
    }
    public LiveData<UserData> getSelectedItem() {
        return selectedItem;
    }

    private final MutableLiveData<String []> emailPass = new MutableLiveData<>();
    public void selectEmailPass(String [] item) {
        emailPass.setValue(item);
    }
    public LiveData<String []> getEmailPass() {
        return emailPass;
    }

}
