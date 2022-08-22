package com.example.cid;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cid.Objetos.Trabajadores;
import com.example.cid.Objetos.ReferenciasFirebase;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class HuespedActivity extends AppCompatActivity {
    private FirebaseDatabase basedatabase;
    private DatabaseReference referencia;
    final Context context = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_huesped);
        this.basedatabase = FirebaseDatabase.getInstance();
        this.referencia = this.basedatabase.getReferenceFromUrl
                (ReferenciasFirebase.URL_DATABASE +
                        ReferenciasFirebase.DATABASE_NAME + "/" +
                        ReferenciasFirebase.TABLE_NAME);
        obtenerWorkers();
    }

    public void obtenerWorkers(){
        final ArrayList<Trabajadores> workers = new ArrayList<Trabajadores>();
        ChildEventListener huespedListener = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Trabajadores worker = dataSnapshot.getValue(Trabajadores.class);
                workers.add(worker);
                final MyArrayAdapter adapter = new MyArrayAdapter(context,R.layout.layout_huesped,workers);
                ListView lvHuesped = findViewById(R.id.huespedlist);
                lvHuesped.setAdapter(adapter);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError error) {

            }
        };
        referencia.addChildEventListener(huespedListener);
    }
    class MyArrayAdapter extends ArrayAdapter<Trabajadores>{
        Context huespedContext;
        int huespedTextViewRecursoId;
        ArrayList<Trabajadores> huespedObjects;

        public MyArrayAdapter(Context context, int huespedTextViewRecursoId, ArrayList<Trabajadores> huespedObjects){
            super(context, huespedTextViewRecursoId, huespedObjects);
            this.huespedContext = context;
            this.huespedTextViewRecursoId = huespedTextViewRecursoId;
            this.huespedObjects = huespedObjects;
        }

        public View getView(final int huespedPosition, View huespedConvertView, ViewGroup huespedViewGroup){
            LayoutInflater layoutInflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view = layoutInflater.inflate(this.huespedTextViewRecursoId, null);
            TextView lblName = view.findViewById(R.id.lblName);
            TextView lblWork = view.findViewById(R.id.lblWork);
            TextView lblPhone = view.findViewById(R.id.lblPhone);
            if(huespedObjects.get(huespedPosition).getDisponibilidad()>0){
                lblPhone.setTextColor(Color.GREEN);
                lblName.setText(huespedObjects.get(huespedPosition).getNombre());
                lblWork.setText(huespedObjects.get(huespedPosition).getArea_trabajo());
                lblPhone.setText(huespedObjects.get(huespedPosition).getCelular());
            } else {
                lblName.setTextColor(Color.GRAY);
                lblWork.setTextColor(Color.GRAY);
                lblPhone.setTextColor(Color.RED);
                lblName.setText(huespedObjects.get(huespedPosition).getNombre());
                lblWork.setText(huespedObjects.get(huespedPosition).getArea_trabajo());
                lblPhone.setText("Ocupado");
            }
            return view;
        }
    }
}