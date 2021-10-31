package com.umb.applicationtodolist.repository;

import com.google.firebase.database.ValueEventListener;
import com.umb.applicationtodolist.model.Agenda;

public interface IAgendaRepository {

    void Crear(Agenda agenda);

    ValueEventListener Consultar(ValueEventListener eventListener);
}
