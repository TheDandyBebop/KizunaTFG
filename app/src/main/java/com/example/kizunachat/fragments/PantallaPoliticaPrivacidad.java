package com.example.kizunachat.fragments;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.kizunachat.R;

public class PantallaPoliticaPrivacidad extends Fragment {

    Button btnAceptar;
    private static final int REQUEST_PERMISSIONS_CODE = 100;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_politica_privacidad, container, false);

        btnAceptar = rootView.findViewById(R.id.btn_AceptarPrivacidad);
        btnAceptar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkPermissions()) {
                    // Si los permisos ya están concedidos, continuar con la siguiente acción
                    continuar();
                } else {
                    // Si los permisos no están concedidos, solicitarlos al usuario
                    ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.READ_CONTACTS}, REQUEST_PERMISSIONS_CODE);
                }
            }
        });

        return rootView;
    }

    private void continuar() {
        // Crear una instancia del nuevo fragmento
        RegistroTelefonoFragment nuevoFragmento = new RegistroTelefonoFragment();

        // Obtener el FragmentManager
        FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();

        // Iniciar una transacción de fragmentos
        FragmentTransaction transaction = fragmentManager.beginTransaction();

        // Agregar animaciones de transición personalizadas
        transaction.setCustomAnimations(
                R.anim.slide_out,  // animación de salida
                R.anim.slide_in  // animación de entrada
        );

        // Reemplazar el fragmento actual por el nuevo fragmento
        transaction.replace(R.id.fragmentManager, nuevoFragmento);

        // Agregar la transacción a la pila de retroceso
        transaction.addToBackStack(null);

        // Realizar la transacción
        transaction.commit();
    }

    private boolean checkPermissions() {
        // Verificar si los permisos ya están concedidos
        int readContactsPermission = ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.READ_CONTACTS);
        return readContactsPermission == PackageManager.PERMISSION_GRANTED;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_PERMISSIONS_CODE) {
            // Verificar si el usuario concedió los permisos solicitados
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Si los permisos están concedidos, continuar con la siguiente acción
                continuar();
            }
        }
    }
}
