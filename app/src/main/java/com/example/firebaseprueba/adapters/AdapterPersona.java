package com.example.firebaseprueba.adapters;

import android.annotation.SuppressLint;
import android.app.Person;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.firebaseprueba.R;
import com.example.firebaseprueba.model.Persona;

import java.util.List;

public class AdapterPersona extends RecyclerView.Adapter<AdapterPersona.PersonaHolder>  {

    List<Persona> personas;


    public AdapterPersona(List<Persona> personas) {
        this.personas = personas;
    }

    @NonNull
    @Override
    public AdapterPersona.PersonaHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View vista = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_date_persona, parent,false );

        RecyclerView.LayoutParams layoutParams = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);

        vista.setLayoutParams(layoutParams);

        return new PersonaHolder(vista);
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public void onBindViewHolder(@NonNull AdapterPersona.PersonaHolder holder, int position) {
        holder.txtNruta.setText(personas.get(position).getNombre() + " " + personas.get(position).getApellidos() );
        holder.txtNombre.setText(personas.get(position).getCorreo());
//        holder.txtapellido.setText("Conductor");


    }

    @Override
    public int getItemCount() {
        return personas.size();
    }

    public class PersonaHolder extends RecyclerView.ViewHolder{

        TextView txtNruta,txtNombre,txtapellido;
        ImageView image, drawable;

        public PersonaHolder(View itemView) {
            super(itemView);
            txtNruta=itemView.findViewById(R.id.textViewNombreRuta);
            txtNombre=itemView.findViewById(R.id.textViewNombreConductor);
            txtapellido=itemView.findViewById(R.id.textViewApellido);


           /* itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //

                    //
                }
            });*/
        }
    }

}
