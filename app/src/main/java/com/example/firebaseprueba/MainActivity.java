package com.example.firebaseprueba;

import android.app.Person;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.firebaseprueba.adapters.AdapterPersona;
import com.example.firebaseprueba.model.Persona;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class MainActivity extends AppCompatActivity implements RecyclerView.OnItemTouchListener{

    EditText nom,ape,cor,pass;

    Menu menui;

    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    RecyclerView recyclerView;
    GestureDetector mGestureDetector;

    private List<Persona> personas = new ArrayList<>();

    Persona personaSelect;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        nom = findViewById(R.id.input_nombre);
        ape = findViewById(R.id.input_apellidos);
        cor = findViewById(R.id.input_correo);
        pass = findViewById(R.id.input_pass);

        recyclerView = findViewById(R.id.recyclerViewPersonas);
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getApplicationContext()));
        recyclerView.setHasFixedSize(true);
        recyclerView.addOnItemTouchListener(this);

        mGestureDetector = new GestureDetector(MainActivity.this, new GestureDetector.SimpleOnGestureListener() {
            @Override public boolean onSingleTapUp(MotionEvent e) {
                return true;
            }
        });

        inicializarFirebaseServices();
        cargarDatos();
    }

    /*INICIOALIZAR FIREBASE*/
    private void inicializarFirebaseServices() {
        //inicio
        FirebaseApp.initializeApp(this);
        //instancia
        firebaseDatabase = FirebaseDatabase.getInstance();
        //persistencia de datos
        //firebaseDatabase.setPersistenceEnabled(true);
        //referencia
        databaseReference = firebaseDatabase.getReference();
    }

    /*REGISTRO*/
    private void registroPersonas() {

        String nombre = nom.getText().toString();
        String apellidos = ape.getText().toString();
        String correo = cor.getText().toString();
        String password = pass.getText().toString();

        if (!validacion(nombre,apellidos,correo,password)) {
            return;
        }else {
            Persona p = new Persona();
            p.setUid(UUID.randomUUID().toString());
            p.setNombre(nombre);
            p.setApellidos(apellidos);
            p.setCorreo(correo);
            p.setPassword(password);

            databaseReference.child("Persona").child(p.getUid()).setValue(p);

            Toast.makeText(this, "Agregado", Toast.LENGTH_SHORT).show();
            limpiarEdittext();
        }
    }

    /*CARGAR LA INFO REGISTRADA DE LA BD*/
    private void cargarDatos() {
        databaseReference.child("Persona").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                personas.clear();
                for (DataSnapshot objSnapshot : dataSnapshot.getChildren()){
                    Persona p = objSnapshot.getValue(Persona.class);
                    personas.add(p);
                }

                AdapterPersona adapter = new AdapterPersona(personas);
                recyclerView.setAdapter(adapter);

                esconderMenuDeleteSave();
                //Toast.makeText(getApplicationContext(), "Listo", Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    /*ACTUALIZACION*/
    private void updatePersonas() {
        Persona p = new Persona();
        p.setUid(personaSelect.getUid());
        p.setNombre(nom.getText().toString().trim());
        p.setApellidos(ape.getText().toString().trim());
        p.setCorreo(cor.getText().toString().trim());
        p.setPassword(pass.getText().toString().trim());

        databaseReference.child("Persona").child(p.getUid()).setValue(p);

        Toast.makeText(getApplicationContext(), "Actualizado", Toast.LENGTH_SHORT).show();

        limpiarEdittext();

        esconderMenuDeleteSave();
    }

    /*ELIMINACION*/

    private void deletePersona() {
        Persona p = new Persona();
        p.setUid(personaSelect.getUid());

        databaseReference.child("Persona").child(p.getUid()).removeValue();

        Toast.makeText(getApplicationContext(), "Eliminado",  Toast.LENGTH_SHORT).show();
        limpiarEdittext();

        esconderMenuDeleteSave();
    }

    /*ESCONDER MENU*/
    private void esconderMenuAdd() {
        menui.findItem(R.id.icon_add).setVisible(false);
        menui.findItem(R.id.icon_save).setVisible(true);
        menui.findItem(R.id.icon_delete).setVisible(true);
    }
    /**/
    private void esconderMenuDeleteSave() {
        menui.findItem(R.id.icon_add).setVisible(true);
        menui.findItem(R.id.icon_save).setVisible(false);
        menui.findItem(R.id.icon_delete).setVisible(false);
    }

    /*LIMPIAR LOS CAMPOS DE TEXTO*/
    private void limpiarEdittext() {
        nom.setText("");
        ape.setText("");
        cor.setText("");
        pass.setText("");//
        //
    }

    /*VALIDAR LOS CAMPOS SI NO ESTAN LLENOS CORRECTAMENTE*/
    private Boolean validacion(String nombre, String apellidos, String correo, String password) {

        boolean valid = true;

        int rtaPass = 0;
        for (int i=0; i < personas.size(); i++){
            if (personas.get(i).getCorreo().equals(correo)){
                rtaPass=1;
            }
        }

        if (nombre.equals("")){
            nom.setError("Requerido");
            valid = false;
        }else{
            nom.setError(null);
        }

        if (apellidos.equals("")){
            ape.setError("Requerido");
            valid = false;
        }else{
            ape.setError(null);
        }

        if (correo.equals("")){
            cor.setError("Requerido");
            valid = false;
        }else {
            cor.setError(null);
        }

        if (password.equals("")){
            pass.setError("Requerido");
            valid = false;
        }else{
            pass.setError(null);
        }

        if (rtaPass != 0){
            cor.setError("Ya existe.");
            valid = false;
        }else {
            cor.setError(null);
        }

        return valid;
    }

    /*CLICK DEL RECYCLER*/
    @Override
    public boolean onInterceptTouchEvent(@NonNull RecyclerView recyclerView, @NonNull MotionEvent motionEvent) {
        try {
            View child = recyclerView.findChildViewUnder(motionEvent.getX(), motionEvent.getY());

            if (child != null && mGestureDetector.onTouchEvent(motionEvent)) {

                int position = recyclerView.getChildAdapterPosition(child);

                //
                int numeroID = (int)personas.get(position).getUid().hashCode();
                Toast.makeText(getApplicationContext(), "nom=" + numeroID, Toast.LENGTH_SHORT).show();

                personaSelect = (Persona) personas.get(position);

                nom.setText(personaSelect.getNombre());
                ape.setText(personaSelect.getApellidos());
                cor.setText(personaSelect.getCorreo());
                pass.setText(personaSelect.getPassword());

                esconderMenuAdd();
                //

                return true;
            }
        }catch (Exception e){
            e.printStackTrace();
        }

        return false;
    }




    @Override
    public void onTouchEvent(@NonNull RecyclerView recyclerView, @NonNull MotionEvent motionEvent) {
        View child = recyclerView.findChildViewUnder(motionEvent.getX(), motionEvent.getY());
        int position = recyclerView.getChildAdapterPosition(child);

        //Toast.makeText(getApplicationContext(),"name conductor: "+ subColes.get(position).getName_conductor() ,Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onRequestDisallowInterceptTouchEvent(boolean b) {
    }

    /*MENU OPTIONS*/
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        menui = menu;
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case R.id.icon_add:
                registroPersonas();
                break;
            case R.id.icon_save:
                updatePersonas();
                break;
            case R.id.icon_delete:
                deletePersona();
                break;
        }

        return true;
    }
    /*FIN*/

}
