package com.example.kizunachat.ClasesApoyo;

import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.provider.ContactsContract;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.kizunachat.ClasesObjetos.AmigosChats;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class BusquedaContactos {
    private static ArrayList<AmigosChats> listaContactos;
    private static ArrayList<String> telContactos;
    private static DatabaseReference contactosRef;

    public static void recuperarContactos(Context context, OnContactosCargadosListener listener) {
        contactosRef = FirebaseDatabase.getInstance().getReference().child("usuarios");
        listaContactos = new ArrayList<>();
        cargarContactos(context, listener);
    }

    private static void cargarContactos(Context context, OnContactosCargadosListener listener) {
        telContactos = new ArrayList<>();
        ContentResolver contentResolver = context.getContentResolver();
        String[] columnas = {
                ContactsContract.Contacts.DISPLAY_NAME,
                ContactsContract.Contacts._ID
        };

        Cursor cursor = contentResolver.query(
                ContactsContract.Contacts.CONTENT_URI,
                columnas,
                null,
                null,
                null
        );

        if (cursor != null && cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                @SuppressLint("Range") String nombre = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
                @SuppressLint("Range") String contactoId = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));

                Cursor phones = contentResolver.query(
                        ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                        null,
                        ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?",
                        new String[]{contactoId},
                        null
                );

                if (phones != null && phones.moveToFirst()) {
                    do {
                        @SuppressLint("Range") String numeroTelefono = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                        telContactos.add(numeroTelefono);
                    } while (phones.moveToNext());
                    phones.close();
                }
            }
            cursor.close();
        }

        for (int i = 0; i < telContactos.size(); i++) {
            Query query = contactosRef.orderByChild("telefono").equalTo(telContactos.get(i));

            query.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        String userID = snapshot.getKey();
                        String nombre = snapshot.child("nombre").getValue(String.class);
                        String urlImage = snapshot.child("urlImagen").getValue(String.class);
                        String tlf = snapshot.child("telefono").getValue(String.class);
                        AmigosChats nuevoAmigo = new AmigosChats(nombre, tlf, urlImage, userID);
                        listaContactos.add(nuevoAmigo);
                    }
                    listener.onContactosCargados(listaContactos);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Toast.makeText(context, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}
