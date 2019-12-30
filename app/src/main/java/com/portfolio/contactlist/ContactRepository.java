package com.portfolio.contactlist;

import android.os.AsyncTask;
import java.util.List;
import android.arch.lifecycle.MutableLiveData;
import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.util.Log;

public class ContactRepository
{
    private MutableLiveData<List<Contact>> searchResults = new MutableLiveData<>();
    private LiveData<List<Contact>> allContacts;
    private ContactDao contactDao;
    private static final String NAME = "name";
    private static final String PHONE = "phone";
    private static final String EMAIL = "email";

    //CONSTRUCTOR
    public ContactRepository(Application application)
    {
        ContactRoomDatabase db;
        db = ContactRoomDatabase.getDatabase(application);
        contactDao = db.contactDao();
        allContacts = contactDao.getAllContacts();
    }

    //INSERT, FIND, DELETE
    public void insertContact(Contact newContact)
    {
        InsertAsyncTask task = new InsertAsyncTask(contactDao);
        task.execute(newContact);
    }
    public void deleteContact(String name)
    {
        DeleteAsyncTask task = new DeleteAsyncTask(contactDao);
        task.execute(name);
    }
    public void findName(String name)
    {
        QueryAsyncTask task = new QueryAsyncTask(contactDao, NAME);
        task.delegate = this;
        task.execute(name);
    }
    public void findPhone(String phone)
    {
        QueryAsyncTask task = new QueryAsyncTask(contactDao, PHONE);
        task.delegate = this;
        task.execute(phone);
    }
    public void findEmail(String email)
    {
        QueryAsyncTask task = new QueryAsyncTask(contactDao, EMAIL);
        task.delegate = this;
        task.execute(email);
    }


    //GETTERS & SETTERS
    public LiveData<List<Contact>> getAllContacts() { return allContacts;}
    public MutableLiveData<List<Contact>> getSearchResults() { return searchResults; }
    private void asyncFinished(List<Contact> results) { searchResults.setValue(results); }

    //ASYNC QUERY
    private static class QueryAsyncTask extends AsyncTask<String, Void, List<Contact>>
    {
        private String criterion;
        private ContactDao asyncTaskDao;
        private static ContactRepository delegate = null;
        QueryAsyncTask(ContactDao dao, String criterion)
        {
            this.criterion= criterion;
            asyncTaskDao = dao;
        }

        @Override
        protected List<Contact> doInBackground(final String... params)
        {
            if (criterion == PHONE)
                return asyncTaskDao.findPhone(params[0]);
            else if (criterion == EMAIL)
                return asyncTaskDao.findEmail(params[0]);
            else return asyncTaskDao.findName(params[0]);
        }

        @Override
        protected void onPostExecute(List<Contact> result)
        {
            delegate.asyncFinished(result);
        }
    }

    //ASYNC INSERT
    private static class InsertAsyncTask extends AsyncTask< Contact, Void, Void>
    {
        private  ContactDao asyncTaskDao;
        InsertAsyncTask( ContactDao dao) { asyncTaskDao = dao; }

        @Override
        protected Void doInBackground(final Contact... params)
        {
            asyncTaskDao.insertContact(params[0]);
            return null;
        }
    }

    //ASYNC DELETE
    private static class DeleteAsyncTask extends AsyncTask<String, Void, Void>
    {
        private ContactDao asyncTaskDao;
        DeleteAsyncTask(ContactDao dao) { asyncTaskDao = dao; }

        @Override
        protected Void doInBackground(final String... params)
        {
            asyncTaskDao.deleteContact(params[0]);
            return null;
        }
    }
}