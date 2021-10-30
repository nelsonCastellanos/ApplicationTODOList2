package com.umb.applicationtodolist;

import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.umb.applicationtodolist.databinding.FragmentFirstBinding;
import com.umb.applicationtodolist.model.Agenda;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class FirstFragment extends Fragment {

    private static final String TAG = "FirstFragment";
    private FragmentFirstBinding binding;

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {

        binding = FragmentFirstBinding.inflate(inflater, container, false);

        // Write a message to the database
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("agenda");

        binding.calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            //show the selected date as a toast
            @Override
            public void onSelectedDayChange(CalendarView view, int year, int month, int day) {
                Calendar c = Calendar.getInstance();
                c.set(year, month, day);
                binding.calendarView.setDate(c.getTimeInMillis());
            }
        });

        binding.button.setOnClickListener(new View.OnClickListener() {
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
                    myRef.child(uuid).setValue(agenda);
                    Snackbar.make(view, "Información almacenada.", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }
            }
        });

        // Read from the database
        myRef.addValueEventListener(new ValueEventListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<Agenda> agendas = new ArrayList<>();
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                dataSnapshot.getChildren().forEach(x -> {
                    agendas.add(x.getValue(Agenda.class));
                });
                Agenda value = dataSnapshot.getValue(Agenda.class);
                Log.d(TAG, "Value is: " + value);
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
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });
        return binding.getRoot();

    }

    private void addColumnValue(TableRow tbrow, String value){
        TextView t1v = new TextView(tbrow.getContext());
        t1v.setText(value);
        tbrow.addView(t1v);
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}