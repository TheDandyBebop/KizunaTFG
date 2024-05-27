package com.example.kizunachat;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

public class VerificacionActivity extends AppCompatActivity {

    EditText etCodigo;
    FirebaseAuth mAuth;
    String intentAuth;
    Button btnVerificarCodigo;
    String telefono;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verificacion);
        etCodigo = findViewById(R.id.etCodVerificacion);
        btnVerificarCodigo = (Button) findViewById(R.id.btn_comprobarCodigo);
        mAuth = FirebaseAuth.getInstance();
        intentAuth = getIntent().getStringExtra("auth");
        telefono = getIntent().getStringExtra("TEL");

        btnVerificarCodigo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String codVerificacion = etCodigo.getText().toString();
                if (!codVerificacion.isEmpty()){
                    PhoneAuthCredential credencial = PhoneAuthProvider.getCredential(intentAuth, codVerificacion);
                    iniciarSesion(credencial);
                }
                else{
                    //Corregir el string
                    Toast.makeText(VerificacionActivity.this, "Codigo Incorrecto", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
    @Override
    public void onStart() {
        super.onStart();
        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null){
            inicioActivityHome();
        }
    }

    private void iniciarSesion(PhoneAuthCredential credencial) {
        mAuth.signInWithCredential(credencial).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){


                    //Aca tengo que introducir los datos del usuario
                    inicioDatosPersonales();

                    //inicioActivityHome();
                }
                else{
                    //Corregir el string
                    Toast.makeText(VerificacionActivity.this, "Error al verificar", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    private void inicioActivityHome() {
        Intent intent = new Intent(this, VentanaPrincipal.class);
        startActivity(intent);
    }

    private void inicioDatosPersonales() {
        Intent intent = new Intent(this, DatosPersonales.class);
        intent.putExtra("TEL", telefono);
        startActivity(intent);
    }
}