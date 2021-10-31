package com.umb.applicationtodolist.repository;

import android.os.Build;

import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.umb.applicationtodolist.model.Agenda;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

public class AgendaRepository implements IAgendaRepository{
    FirebaseDatabase database;
    DatabaseReference myRef;

    public AgendaRepository() {
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("agenda");
    }


    public void Crear(Agenda agenda){
        myRef.child(agenda.getUid()).setValue(agenda);
    }

    public ValueEventListener Consultar(ValueEventListener eventListener) {
      return  myRef.addValueEventListener(eventListener);
    }
}
