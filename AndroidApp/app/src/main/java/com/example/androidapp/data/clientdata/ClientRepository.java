package com.example.androidapp.data.clientdata;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.example.androidapp.data.AppDatabase;
import com.example.androidapp.data.menudata.Dish;
import com.example.androidapp.data.menudata.DishDao;
import com.example.androidapp.data.menudata.DishRepository;

import java.util.List;


public class ClientRepository {
    private ClientDao clientDao;
    private LiveData<List<Client>> allClients;

    public ClientRepository(Application application) {
        AppDatabase database = AppDatabase.getInstance(application);
        clientDao = database.clientDao();
        allClients = clientDao.getAllClients();
    }
    public void insertClient(Client client) {
        new ClientRepository.InsertClientAsyncTask(clientDao).execute(client);
    }

    public void updateClient(Client client) {
        new ClientRepository.UpdateClientAsyncTask(clientDao).execute(client);
    }

    public void deleteClient(Client client) {
        new ClientRepository.DeleteClientAsyncTask(clientDao).execute(client);
    }

    public void deleteAllClients() {
        new ClientRepository.DeleteAllClientesAsyncTask(clientDao).execute();
    }

    public LiveData<List<Client>> getAllClients() {
        return allClients;
    }

    private static class InsertClientAsyncTask extends AsyncTask<Client, Void, Void> {
        private ClientDao clientDao;

        private InsertClientAsyncTask(ClientDao clientDao) {
            this.clientDao = clientDao;
        }

        @Override
        protected Void doInBackground(Client... clientEntities) {
            clientDao.insertClient(clientEntities[0]);
            return null;
        }
    }

    private static class UpdateClientAsyncTask extends AsyncTask<Client, Void, Void> {
        private ClientDao clientDao;

        private UpdateClientAsyncTask(ClientDao clientDao) {
            this.clientDao = clientDao;
        }

        @Override
        protected Void doInBackground(Client... clientEntities) {
            clientDao.updateClient(clientEntities[0]);
            return null;
        }
    }

    private static class DeleteClientAsyncTask extends AsyncTask<Client, Void, Void> {
        private ClientDao clientDao;

        private DeleteClientAsyncTask(ClientDao clientDao) {
            this.clientDao = clientDao;
        }

        @Override
        protected Void doInBackground(Client... clientEntities) {
            clientDao.deleteClient(clientEntities[0]);
            return null;
        }
    }

    private static class DeleteAllClientesAsyncTask extends AsyncTask<Void, Void, Void> {
        private ClientDao clientDao;

        private DeleteAllClientesAsyncTask(ClientDao clientDao) {
            this.clientDao = clientDao;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            clientDao.deleteAllClients();
            return null;
        }
    }
}
