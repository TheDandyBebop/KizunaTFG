package com.example.kizunachat;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.kizunachat.Adaptadores.TabAdapter;
import com.example.kizunachat.fragments.Calendario;
import com.example.kizunachat.fragments.Chats;
import com.example.kizunachat.fragments.Eventos;
import com.example.kizunachat.fragments.PantallaPoliticaPrivacidad;
import com.example.kizunachat.fragments.RegistroTelefonoFragment;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.rilixtech.widget.countrycodepicker.CountryCodePicker;

import org.checkerframework.common.returnsreceiver.qual.This;

public class MainActivity extends AppCompatActivity {


    CountryCodePicker ccp;
    EditText tlf;
    Button btnAceptar, btnEnviarTelefono;
    //Objeto autenticador de firebase
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.inicio_app); // Establece la vista de la actividad como el diseño definido en activity_main.xml
        //Inicializo mi auth de firebase
        mAuth = FirebaseAuth.getInstance();
        mAuth.useAppLanguage();

        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            //Si ya esta logeado en firebase:
            inicioActivityHome();
        } else {
            //Si NO esta logeado en firebase:
            PantallaPoliticaPrivacidad fragment = new PantallaPoliticaPrivacidad();


        // Obtener el FragmentManager
        FragmentManager fragmentManager = getSupportFragmentManager();

        // Iniciar una transacción de fragmentos
        FragmentTransaction transaction = fragmentManager.beginTransaction();

        // Reemplazar el contenido del contenedor de fragmentos con el fragmento deseado
        transaction.replace(R.id.fragmentManager, fragment);

        // Realizar la transacción
        transaction.commit();
         }
    }
    private void inicioActivityHome() {
        Intent intent = new Intent(this, VentanaPrincipal.class);
        startActivity(intent);
    }


}

