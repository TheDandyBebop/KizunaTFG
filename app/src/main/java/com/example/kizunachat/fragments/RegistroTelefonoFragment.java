package com.example.kizunachat.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.kizunachat.R;
import com.example.kizunachat.VentanaPrincipal;
import com.example.kizunachat.VerificacionActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.rilixtech.widget.countrycodepicker.CountryCodePicker;

import java.util.concurrent.TimeUnit;

public class RegistroTelefonoFragment extends Fragment {
    CountryCodePicker ccp;
    EditText tlf;
    Button btnEnviar;
    String telefono;

    //Objeto autenticador de firebase
    FirebaseAuth mAuth;
    PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_registro_telefono, container, false);
        // Inflate the layout for this fragment
        ccp = (CountryCodePicker) rootView.findViewById(R.id.ccp);
        tlf = (EditText) rootView.findViewById(R.id.phone_number_edt);
        ccp.registerPhoneNumberTextView(tlf);
        //Inicializo mi auth de firebase
        mAuth = FirebaseAuth.getInstance();
        btnEnviar = (Button) rootView.findViewById(R.id.btn_comprobarCodigo);
        //10:59 CONTINUAR VIENDDO Y SEGUIR CON AUTH
        mAuth.useAppLanguage();

        btnEnviar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                telefono = ccp.getSelectedCountryCodeWithPlus();
                telefono = telefono + ' ' + tlf.getText().toString();
                if (ccp.isValid()){
                    Toast.makeText(rootView.getContext(),telefono,Toast.LENGTH_SHORT).show();

                    //Comienzo autenticacion por telefono
                    PhoneAuthOptions options =
                            PhoneAuthOptions.newBuilder(mAuth)
                                    .setPhoneNumber(telefono)       // Phone number to verify
                                    .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                                    .setActivity(getActivity())                 // (optional) Activity for callback binding
                                    // If no activity is passed, reCAPTCHA verification can not be used.
                                    .setCallbacks(mCallbacks)          // OnVerificationStateChangedCallbacks
                                    .build();
                    PhoneAuthProvider.verifyPhoneNumber(options);
                }

            }
        });

        mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                //Se inicia Sesion
                iniciarSesion(phoneAuthCredential);
            }

            @Override
            public void onVerificationFailed(@NonNull FirebaseException e) {
                //Mensaje al fallar
                Toast.makeText(getContext(),e.toString(),Toast.LENGTH_SHORT).show();
                System.out.println(e.toString());
            }

            @Override
            public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                super.onCodeSent(s, forceResendingToken);
                //Corregir ese string
                Toast.makeText(getContext(),"Mensaje Enviado",Toast.LENGTH_SHORT).show();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Intent intent = new Intent(getContext(),VerificacionActivity.class);
                        intent.putExtra("auth", s);
                        intent.putExtra("TEL", telefono);
                        startActivity(intent);
                    }
                },1000);
            }
        };
        return rootView;

    }

    @Override
    public void onStart() {
        super.onStart();
        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null){
            inicioActivityHome();
        }
    }

    public void iniciarSesion(PhoneAuthCredential credenciales){
        mAuth.signInWithCredential(credenciales).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    inicioActivityHome();
                }
                else {
                    Toast.makeText(getContext(),task.getException().toString(),Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


    private void inicioActivityHome() {
        Intent intent = new Intent(getContext(), VentanaPrincipal.class);
        startActivity(intent);
    }

}