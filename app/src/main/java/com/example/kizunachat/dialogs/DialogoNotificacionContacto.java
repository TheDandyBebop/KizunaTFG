package com.example.kizunachat.dialogs;


import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

import com.example.kizunachat.ClasesObjetos.AmigosChats;
import com.example.kizunachat.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.checkerframework.checker.nullness.qual.Nullable;


public class DialogoNotificacionContacto extends DialogFragment {
    private AmigosChats amigoContacto;

    public DialogoNotificacionContacto(AmigosChats amigoContacto) {
        this.amigoContacto = amigoContacto;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        // Suponiendo que tienes el ID del usuario y del contacto
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();

        AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialogo_notf_contacto, null);

        TextView tvUsuario = view.findViewById(R.id.tvUsuario);
        EditText editTextNumber = view.findViewById(R.id.editTextNumber);

        // Rellenar el campo de usuario con el nombre del contacto
        tvUsuario.setText(amigoContacto.getNombre());

        builder.setView(view)
                .setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        // Aquí puedes añadir la lógica para cuando se presiona "Aceptar"
                        int diasSinContacto = Integer.parseInt(editTextNumber.getText().toString());
                        databaseReference.child("usuarios")
                                .child(userId)
                                .child("contacto")
                                .child(amigoContacto.getUID())
                                .child("notfContacto")
                                .setValue(diasSinContacto);
                        amigoContacto.setNotfContacto(diasSinContacto);
                        dialog.dismiss();
                    }
                })
                .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                    }
                });
        return builder.create();
    }
}
