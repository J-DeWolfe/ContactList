package com.portfolio.contactlist;

import android.app.Application;
import java.util.List;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.util.Log;

public class MainViewModel extends AndroidViewModel
{
    private static ContactRepository repository;
    private LiveData<List<Contact>> allContacts;
    private MutableLiveData<List<Contact>> searchResults;

    //CONSTRUCTOR
    public MainViewModel (Application application)
    {
        super(application);
        repository = new ContactRepository(application);
        allContacts = repository.getAllContacts();
        searchResults = repository.getSearchResults();
    }

    //GETTERS
    MutableLiveData<List<Contact>> getSearchResults() { return searchResults; }
    LiveData<List<Contact>> getAllContacts() { return allContacts; }

    //INSERT, FIND, DELETE, SORT
    public void insertContact(Contact contact)
    {
        repository.insertContact(contact);
    }
    public void findName(String name)
    {
        repository.findName(name);
    }
    public void findPhone(String phone)
    {
        repository.findPhone(phone);
    }
    public void findEmail(String email)
    {
        repository.findEmail(email);
    }
    public void deleteContact(String name) { repository.deleteContact(name); }
}