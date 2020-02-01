package com.example.firebaseprueba;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import com.example.firebaseprueba.model.Persona;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.UUID;

public class MainActivity extends AppCompatActivity {

    EditText nom,ape,cor,pass;

    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        nom = findViewById(R.id.input_nombre);
        ape = findViewById(R.id.input_apellidos);
        cor = findViewById(R.id.input_correo);
        pass = findViewById(R.id.input_pass);

        inicializarFirebaseServices();
    }

    private void inicializarFirebaseServices() {
        FirebaseApp.initializeApp(this);
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        String nombre = nom.getText().toString();
        String apellidos = ape.getText().toString();
        String correo = cor.getText().toString();
        String password = pass.getText().toString();

        switch (item.getItemId()){
            case R.id.icon_add: {
                    if (nombre.equals("") || apellidos.equals("") || correo.equals("") || password.equals("")) {
                        validacion(nombre, apellidos, correo, password);
                    } else {

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
                break;
            case R.id.icon_save:
                break;
            case R.id.icon_delete:
                break;
        }

        return true;
    }

    private void limpiarEdittext() {
        nom.setText("");
        ape.setText("");
        cor.setText("");
        pass.setText("");//
        //
    }

    private void validacion(String nombre, String apellidos, String correo, String password) {
        if (nombre.equals("")){
            nom.setError("Requerido");
        }
        if (apellidos.equals("")){
            ape.setError("Requerido");
        }
        if (correo.equals("")){
            cor.setError("Requerido");
        }
        if (password.equals("")){
            pass.setError("Requerido");
        }

    }
}
