package com.example.cid;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cid.Objetos.ReferenciasFirebase;
import com.example.cid.Objetos.Trabajadores;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Agregar extends AppCompatActivity implements View.OnClickListener{
    private Button btnGuardar, btnLimpiar, btnRegresar;
    private TextView txtNombre, txtProfesion, txtCelular;
    private CheckBox cbxDisponibilidad;
    private FirebaseDatabase basedatabase;
    private DatabaseReference referencia;
    private Trabajadores savedTrabajador;
    private String id;
    private int disponibilidad = 0;

    private void Regresar(){
        AlertDialog.Builder confirmar=new AlertDialog.Builder(this);
        confirmar.setTitle("¿Regresar?");
        confirmar.setMessage("Se descartara toda la informacion ingresada");
        confirmar.setPositiveButton("Confirmar",new DialogInterface.OnClickListener(){
            public void onClick(DialogInterface dialogInterface, int i){
                finish();
            }
        });
        confirmar.setNegativeButton("Cancelar", new DialogInterface.OnClickListener(){
            public void onClick(DialogInterface dialogInterface, int i){

            }
        });
        confirmar.show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agregar);
        initComponents();
        setEvents();
        Bundle oBundle = getIntent().getExtras();
        savedTrabajador = (Trabajadores) oBundle.getSerializable("trabajador");
        if (savedTrabajador != null){
            id = savedTrabajador.get_ID();
            txtNombre.setText(savedTrabajador.getNombre());
            txtProfesion.setText(savedTrabajador.getArea_trabajo());
            txtCelular.setText(savedTrabajador.getCelular());
            if(savedTrabajador.getDisponibilidad()>0){
                cbxDisponibilidad.setChecked(true);
            } else {
                cbxDisponibilidad.setChecked(false);
            }
        }
    }
    public void initComponents(){
        // Se obtiene una instancia de la base de datos y se obtiene la referencia que apunta a la tabla de contactos
        this.basedatabase = FirebaseDatabase.getInstance();
        this.referencia = this.basedatabase.getReferenceFromUrl
                (ReferenciasFirebase.URL_DATABASE +
                        ReferenciasFirebase.DATABASE_NAME + "/" +
                        ReferenciasFirebase.TABLE_NAME);
        // ---------------------------------------------------------------------------------- \\
        this.txtNombre = findViewById(R.id.txtNombre);
        this.txtProfesion = findViewById(R.id.txtProfesion);
        this.txtCelular = findViewById(R.id.txtCel);
        this.cbxDisponibilidad = findViewById(R.id.cbxDisponibilidad);
        this.btnGuardar = findViewById(R.id.btnGuardar);
        this.btnLimpiar = findViewById(R.id.btnLimpiar);
        this.btnRegresar = findViewById(R.id.btnRegresar);
        savedTrabajador = null;
    }

    public void setEvents(){
        this.btnGuardar.setOnClickListener(this);
        this.btnLimpiar.setOnClickListener(this);
        this.btnRegresar.setOnClickListener(this);
    }

    @Override
    public void onClick(View view){
        if(isNetworkAvailable()){
            switch (view.getId()) {
                case R.id.btnGuardar:
                    boolean completo = true;
                    if (txtNombre.getText().toString().equals("")) {
                        txtNombre.setError("Introduce el nombre");
                        completo = false;
                    }
                    if (txtProfesion.getText().toString().equals("")) {
                        txtProfesion.setError("Introduce la profesion");
                        completo = false;
                    }
                    if (txtCelular.getText().toString().equals("")) {
                        txtCelular.setError("Introduce el celular");
                        completo = false;
                    }
                    if (completo) {
                        Trabajadores nTrabajador = new Trabajadores();
                        nTrabajador.setNombre(txtNombre.getText().toString());
                        nTrabajador.setArea_trabajo(txtProfesion.getText().toString());
                        nTrabajador.setCelular(txtCelular.getText().toString());
                        nTrabajador.setDisponibilidad(cbxDisponibilidad.isChecked() ? 1 : 0);
                        if (savedTrabajador == null) {
                            agregarTrabajador(nTrabajador);
                            Toast.makeText(this, "Trabajador guardado con exito", Toast.LENGTH_SHORT).show();
                            finish();
                        } else {
                            actualizarTrabajador(id, nTrabajador);
                            Toast.makeText(this, "Tradajador actualizado con exito", Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    }
                    break;

                case R.id.btnLimpiar:
                    limpiar();
                    break;

                case R.id.btnRegresar:
                    Regresar();
                    break;
            }
        }else{
            Toast.makeText(this, "Se necesita conexion a internet", Toast.LENGTH_SHORT).show();
        }
    }

    public void agregarTrabajador(Trabajadores t){
        DatabaseReference newTrabajadorReference = referencia.push();
        // Obtener el registro del ID y setearlo
        String id = newTrabajadorReference.getKey();
        t.set_ID(id);
        newTrabajadorReference.setValue(t);
    }

    public void actualizarTrabajador(String id, Trabajadores t){
        // Actualizar un objeto al nodo referencia
        t.set_ID(id);
        referencia.child(String.valueOf(id)).setValue(t);
    }

    public boolean isNetworkAvailable(){
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo ni = cm.getActiveNetworkInfo();

        return ni != null && ni.isConnected();
    }

    public void limpiar(){
        savedTrabajador = null;
        txtNombre.setText("");
        txtProfesion.setText("");
        txtCelular.setText("");
        cbxDisponibilidad.setChecked(false);
        id="";
    }
}