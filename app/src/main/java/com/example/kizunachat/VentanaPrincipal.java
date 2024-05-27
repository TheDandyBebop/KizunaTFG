package com.example.kizunachat;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Intent;
import android.os.Bundle;

import com.example.kizunachat.Adaptadores.TabAdapter;
import com.example.kizunachat.fragments.Calendario;
import com.example.kizunachat.fragments.Chats;
import com.example.kizunachat.fragments.Eventos;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class VentanaPrincipal extends AppCompatActivity {

    ViewPager2 myViewPager2;
    TabAdapter myAdapter;
    TabLayout myTabs;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main); // Establece la vista de la actividad como el diseño definido en activity_main.xml

        mAuth = FirebaseAuth.getInstance();//Defino el firebaseAuth

        // Inicio de la definición de las ventanas
        // Instancio mi ViewPager2, es el encargado de gestionar qué se muestra cuando se pulsan las ventanas
        myViewPager2 = findViewById(R.id.ViewPager); // Encuentra el ViewPager2 en el diseño utilizando su ID
        myAdapter = new TabAdapter(getSupportFragmentManager(), getLifecycle()); // Crea un nuevo adaptador para el ViewPager2
        myAdapter.addFragment(new Calendario()); // Agrega un fragmento "Calendario" al adaptador
        myAdapter.addFragment(new Chats()); // Agrega un fragmento "Chats" al adaptador
        myAdapter.addFragment(new Eventos()); // Agrega un fragmento "Eventos" al adaptador
        myViewPager2.setAdapter(myAdapter); // Establece el adaptador en el ViewPager2
        myViewPager2.setCurrentItem(1); // Esto establece el fragmento de "Chats" como la primera ventana
        myTabs = findViewById(R.id.layoutTabs); // Encuentra el TabLayout en el diseño utilizando su ID

        myTabs.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                myViewPager2.setCurrentItem(tab.getPosition()); // Cambia la página actual del ViewPager2 cuando se selecciona una pestaña en el TabLayout
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                // Método vacío requerido por la interfaz
                // Este método se llama cuando una pestaña que estaba seleccionada pierde el foco
                // Ejemplo: Podrías utilizar este método para realizar alguna acción cuando se deselecciona una pestaña, como detener la reproducción de un video en el fragmento deseleccionado
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                // Método vacío requerido por la interfaz
            }
        });

        myViewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                myTabs.getTabAt(position).select(); // Selecciona la pestaña correspondiente en el TabLayout cuando se cambia la página en el ViewPager2
            }
        });
        // Fin de la definición de las ventanas
    }

    @Override
    public void onStart() {
        super.onStart();
        FirebaseUser user = mAuth.getCurrentUser();
        if (user == null){
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finish();
        }
    }


    /*
    * Para cuando quiera cerrar sesion, estre es el metodo que hay que metener en el boton
    * //Primero hay que definir un objeto Firebase auth
    *   Firebaseauth mAuth;
    * //Luego en el oncreate lo definimos
    *   mAuth = FirebaseAuth.getInstance();
    * //Luego ya en el oncreate:
    *   mAuth.signOut();
    *   Intent intent = new Intent (this, MainActivity.class);
    *   startActivity(intent)
    *   finish();
    * */

}