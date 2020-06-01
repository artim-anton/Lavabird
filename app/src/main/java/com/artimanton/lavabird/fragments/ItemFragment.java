package com.artimanton.lavabird.fragments;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.artimanton.lavabird.App;
import com.artimanton.lavabird.AppDatabase;
import com.artimanton.lavabird.R;
import com.artimanton.lavabird.adapter.NotifAdapter;
import com.artimanton.lavabird.model.NotifDao;
import com.artimanton.lavabird.model.NotifEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;


/**
 * A simple {@link Fragment} subclass.
 */
public class ItemFragment extends Fragment {

    public static RecyclerView recyclerView;
    private List<NotifEntity> notifs;
    private NotifAdapter adapter;

    private NotifDao notifDao;
    private AppDatabase db;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        //return inflater.inflate(R.layout.fragment_item, container, false);
        ReadRoom readRoom = new ReadRoom();
        readRoom.execute();

        View rootView = inflater.inflate(R.layout.fragment_item, container, false);
        // 1. get a reference to recyclerView
        RecyclerView recyclerView = (RecyclerView) rootView.findViewById(R.id.notif_list);

        // 2. set layoutManger
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        // 3. create an adapter
        NotifAdapter notifAdapter = new NotifAdapter(notifs);
        // 4. set adapter
        recyclerView.setAdapter(notifAdapter);
        // 5. set item animator to DefaultAnimator
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        return rootView;
    }

    public class ReadRoom extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected Void doInBackground(Void... voids) {
            notifDao = App.getInstance().getNotifDao();
            notifs = notifDao.getAll();

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

        }
    }

}
