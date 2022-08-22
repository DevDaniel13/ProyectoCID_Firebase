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

public class MainActivity extends AppCompatActivity {
    private FirebaseDatabase basedatabase;
    private DatabaseReference referencia;
    private FloatingActionButton fbtnAgregar;
    final Context context = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.basedatabase = FirebaseDatabase.getInstance();
        this.referencia = this.basedatabase.getReferenceFromUrl
                (ReferenciasFirebase.URL_DATABASE +
                        ReferenciasFirebase.DATABASE_NAME + "/" +
                        ReferenciasFirebase.TABLE_NAME);
        fbtnAgregar = (FloatingActionButton) findViewById(R.id.agregarTrabajador);
        obtenerTrabajadores();

        fbtnAgregar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, Agregar.class);
                Trabajadores worker = null;
                Bundle oBundle = new Bundle();
                oBundle.putSerializable("trabajador", worker);
                i.putExtras(oBundle);
                startActivityForResult(i, 0);
            }
        });
    }

    public void obtenerTrabajadores(){
        final ArrayList<Trabajadores> trabajadores = new ArrayList<Trabajadores>();
        ChildEventListener listener = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Trabajadores trabajador = dataSnapshot.getValue(Trabajadores.class);
                trabajadores.add(trabajador);
                final MyArrayAdapter adapter = new MyArrayAdapter(context,R.layout.layout_trabajador,trabajadores);
                ListView lv = (ListView) findViewById(R.id.list);
                lv.setAdapter(adapter);
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
        referencia.addChildEventListener(listener);
    }
    class MyArrayAdapter extends ArrayAdapter<Trabajadores>{
        Context context;
        int textViewRecursoId;
        ArrayList<Trabajadores> objects;

        public MyArrayAdapter(Context context, int textViewResourseId, ArrayList<Trabajadores> objects){
            super(context, textViewResourseId, objects);
            this.context = context;
            this.textViewRecursoId = textViewResourseId;
            this.objects = objects;
        }

        public View getView(final int position, View convertView, ViewGroup viewGroup){
            LayoutInflater layoutInflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view = layoutInflater.inflate(this.textViewRecursoId, null);
            TextView lblNombre = (TextView)view.findViewById(R.id.lblNombreCompleto);
            TextView lblAreaTrabajo = (TextView)view.findViewById(R.id.lblAreaTrabajo);
            TextView lblCelular = (TextView) view.findViewById(R.id.lblCelular);
            Button btnModificar = (Button)view.findViewById(R.id.btnModificar);
            Button btnBorrar = (Button)view.findViewById(R.id.btnBorrar);
            if(objects.get(position).getDisponibilidad()>0){
                lblNombre.setTextColor(Color.YELLOW);
                lblAreaTrabajo.setTextColor(Color.YELLOW);
                lblCelular.setTextColor(Color.YELLOW);
            }
            lblNombre.setText(objects.get(position).getNombre());
            lblAreaTrabajo.setText(objects.get(position).getArea_trabajo());
            lblCelular.setText(objects.get(position).getCelular());

            btnBorrar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    borrarTrabajador(objects.get(position).get_ID());
                    objects.remove(position);
                    notifyDataSetChanged();
                    Toast.makeText(context, "Trabajador eliminado con exito", Toast.LENGTH_SHORT).show();
                }
            });

            btnModificar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(MainActivity.this, Agregar.class);
                    Bundle oBundle = new Bundle();
                    oBundle.putSerializable("trabajador", objects.get(position));
                    intent.putExtras(oBundle);
                    startActivityForResult(intent, 0);
                }
            });
            return view;
        }

        public void borrarTrabajador(String childIndex){
            referencia.child(String.valueOf(childIndex)).removeValue();
        }
    }
}