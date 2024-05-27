package com.example.kizunachat;

import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.request.RequestOptions;
import com.example.kizunachat.ClasesObjetos.Usuario;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import org.checkerframework.checker.nullness.qual.NonNull;

public class DatosPersonales extends AppCompatActivity {
    private static final int PICK_IMAGE_REQUEST = 1;
    String telefono;
    EditText et_Usuario;
    ImageView iv_FotoUsuario;
    Button btnEnviar;
    Uri uri;//Uri de la imagen
    String urlImagen;//URL DE LA IMAGEN SUBIDA

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_datos_personales);
        telefono = getIntent().getStringExtra("TEL");
        et_Usuario = findViewById(R.id.etUsernamer);
        iv_FotoUsuario = findViewById(R.id.ivFotoPerfil);
        uri = obtenerUriPorDefecto(iv_FotoUsuario);
        btnEnviar = findViewById(R.id.btnEnviarNombreFoto);

        iv_FotoUsuario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                abrirGaleria();
            }
        });

        btnEnviar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                volverAlRegistro();
            }
        });

    }

    private void volverAlRegistro() {
        //Creo el intent
        Intent intent = new Intent(this, VentanaPrincipal.class);

        String nomUser = et_Usuario.getText().toString();
        //Cargo los datos del usuario a la BD y voy a la pantalla principal. Tambien debo de guardarlos en la BD
        // Obtener una referencia a la ubicación donde deseas guardar el usuario en tu base de datos en tiempo real
        DatabaseReference usuariosRef = FirebaseDatabase.getInstance().getReference("usuarios");

        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        // Generar una nueva clave única para el usuario
        String nuevoUsuarioKey = user.getUid();

        //Primero guardo la imagen en Storage
        // Referencia al almacenamiento en Firebase
        StorageReference storageRef = FirebaseStorage.getInstance().getReference();

        // Obtener la referencia a la ubicación en la que deseas almacenar la imagen en el almacenamiento
        String rutaStorage = "usuarios/";
        String nomFotoPerfil =nuevoUsuarioKey + "/fotoPerfil/" + nomUser+nuevoUsuarioKey;

        rutaStorage = rutaStorage+nomFotoPerfil;
        StorageReference imagenRef = storageRef.child(rutaStorage);
        // Subir la imagen al almacenamiento
        UploadTask uploadTask = imagenRef.putFile(uri);
        // Agrego un listener para gestionar el éxito o el fracaso de la operación de carga
        uploadTask.addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                if (task.isSuccessful()) {
                    // La imagen se ha subido correctamente
                    // Obtener la URL de la imagen
                    imagenRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            // La URL de la imagen
                            urlImagen = uri.toString();
                            // Crear un nuevo objeto Usuario
                            Usuario usuario = new Usuario(nomUser,telefono,urlImagen,null);

                            // Guardar el usuario en la base de datos en tiempo real utilizando la clave generada
                            usuariosRef.child(nuevoUsuarioKey).setValue(usuario);
                            startActivity(intent);
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {
                            // No se pudo obtener la URL de la imagen
                            Toast.makeText(DatosPersonales.this,exception.getMessage(),Toast.LENGTH_LONG).show();
                            urlImagen ="";
                        }
                    });
                } else {
                    UploadTask uploadTask = imagenRef.putFile(uri);
                }
            }
        });


    }

    // Método para abrir la galería de imágenes del dispositivo
    private void abrirGaleria() {
        Intent intent = new Intent();
        intent.setType("image/*"); // Tipo de archivos a seleccionar (en este caso, imágenes)
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Seleccionar imagen"), PICK_IMAGE_REQUEST);
    }

    // Método que se llama cuando se selecciona una imagen desde la galería
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            // Obtener la URI de la imagen seleccionada
            uri = data.getData();
            // Establecer la imagen seleccionada en el ImageView
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                Glide.with(this)
                        .load(bitmap)
                        .apply(RequestOptions.bitmapTransform(new CircleCrop()).override(150, 150))
                        .into(iv_FotoUsuario);
                //iv_FotoUsuario.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    private Uri obtenerUriPorDefecto(ImageView imagen) {
        // Obtener la URI de la imagen por defecto desde el tag del ImageView
        String uriString = ContentResolver.SCHEME_ANDROID_RESOURCE +
                "://" + getResources().getResourcePackageName(R.drawable.hotline) +
                "/" + getResources().getResourceTypeName(R.drawable.hotline) +
                "/" + getResources().getResourceEntryName(R.drawable.hotline);

        return Uri.parse(uriString);
    }

}