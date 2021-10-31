package com.umb.applicationtodolist.service;

import android.os.Build;
import android.util.Log;
import android.view.View;
import android.widget.CalendarView;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.annotation.RequiresApi;

import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.umb.applicationtodolist.databinding.FragmentFirstBinding;
import com.umb.applicationtodolist.model.Agenda;
import com.umb.applicationtodolist.repository.AgendaRepository;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

public class AgendaService {
    private AgendaRepository repository;

    public AgendaService() {
        repository = new AgendaRepository();
    }

    public ValueEventListener Consultar(FragmentFirstBinding binding) {
        ValueEventListener valueEventListener =  new ValueEventListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<Agenda> agendas = new ArrayList<>();
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                dataSnapshot.getChildren().forEach(x -> {
                    agendas.add(x.getValue(Agenda.class));
                });
                binding.tableMain.removeViews(1, binding.tableMain.getChildCount()-1);
                for (Agenda agenda :agendas.stream().sorted(Comparator.comparing(Agenda::getFecha).reversed()).collect(Collectors.toCollection(LinkedHashSet::new))) {
                    TableRow tbrow = new TableRow(binding.tableMain.getContext());
                    SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
                    addColumnValue(tbrow, formatter.format(agenda.getFecha()));
                    addColumnValue(tbrow, agenda.getAsunto());
                    addColumnValue(tbrow, agenda.getActividad());
                    binding.tableMain.addView(tbrow);
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {

            }
        };
        return repository.Consultar(valueEventListener);
    }

    public View.OnClickListener onClickListener(FragmentFirstBinding binding){
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String uuid = UUID.randomUUID().toString();
                long datePicker = binding.calendarView.getDate();
                Date d = new Date(datePicker);
                String asunto = binding.editTextTextPersonName.getText().toString();
                String activity = binding.editTextTextMultiLine.getText().toString();
                Agenda agenda = new Agenda(uuid,d,asunto,activity);
                if(asunto.isEmpty() || activity.isEmpty()){
                    Snackbar.make(view, "El asunto y la actividad son obligatorias.", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }else{
                    Crear(agenda);
                    Snackbar.make(view, "Información almacenada.", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }
            }
        };
    }

    public CalendarView.OnDateChangeListener onDateChangeListener(FragmentFirstBinding binding){
        return new CalendarView.OnDateChangeListener() {
            //show the selected date as a toast
            @Override
            public void onSelectedDayChange(CalendarView view, int year, int month, int day) {
                Calendar c = Calendar.getInstance();
                c.set(year, month, day);
                binding.calendarView.setDate(c.getTimeInMillis());
            }
        };
    }


    private void addColumnValue(TableRow tbrow, String value){
        TextView t1v = new TextView(tbrow.getContext());
        t1v.setText(value);
        tbrow.addView(t1v);
    }

    public void Crear(Agenda agenda){
        repository.Crear(agenda);
    }
}
