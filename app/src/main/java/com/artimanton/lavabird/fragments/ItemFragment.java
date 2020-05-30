package com.artimanton.lavabird.fragments;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.artimanton.lavabird.App;
import com.artimanton.lavabird.AppDatabase;
import com.artimanton.lavabird.R;
import com.artimanton.lavabird.adapter.NotifAdapter;
import com.artimanton.lavabird.model.NotifEntity;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class ItemFragment extends Fragment {

    private RecyclerView recyclerView;
    private List<NotifEntity> result;
    private NotifAdapter adapter;

    private App database;
    private AppDatabase reference;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_item, container, false);
    }

}
