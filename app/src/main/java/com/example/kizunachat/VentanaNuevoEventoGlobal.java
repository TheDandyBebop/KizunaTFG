package com.example.kizunachat;
import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.example.kizunachat.ClasesObjetos.EventosAyuntamiento;
import com.example.kizunachat.ClasesObjetos.EventosUsuarios;
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
import java.io.IOException;
import java.text.ParseException;
import java.util.Calendar;

public class VentanaNuevoEventoGlobal extends AppCompatActivity {
    private static final int PICK_IMAGE_REQUEST = 1;
    String titulo;
    String description;
    String startDate;
    String endDate;
    String fecha;
    String ubi;
    EditText et_Titulo, et_Desc,et_hsInicio,et_hsFin,et_Ubi,et_Fecha;
    Button btnAceptar;
    Spinner sp_privacy;
    ImageView imagenAsubir;
    Uri uri;//Uri de la imagen
    String urlImagen;//URL DE LA IMAGEN SUBIDA
    @SuppressLint({"MissingInflatedId", "ClickableViewAccessibility", "UseCompatLoadingForDrawables"})
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nuevo_evento_global);
        et_Desc = findViewById(R.id.et_Desc_AE);
        et_Titulo = findViewById(R.id.et_Titulo_AE);
        et_hsFin = findViewById(R.id.et_HF_AE);
        et_hsInicio = findViewById(R.id.et_HI_AE);
        et_Ubi = findViewById(R.id.et_Ubi_AE);
        et_Fecha = findViewById(R.id.et_Date_AE);
        btnAceptar = findViewById(R.id.btn_Aceptar_AE);
        imagenAsubir = findViewById(R.id.iv_imgEvento);
        uri = obtenerUriPorDefecto(imagenAsubir);
        imagenAsubir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                abrirGaleria();
            }
        });

        et_Titulo.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                et_Titulo.setBackgroundResource(R.drawable.caja_bordes);
                return false;
            }
        });
        et_Fecha.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                et_Fecha.setBackgroundResource(R.drawable.caja_bordes);
                return false;
            }
        });
        et_Fecha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog(et_Fecha);
            }
        });

        et_hsInicio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTimePickerDialog(et_hsInicio);
            }
        });

        et_hsFin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTimePickerDialog(et_hsFin);
            }
        });


    }
    private void showDatePickerDialog(final EditText editText) {
        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                String formattedDay = String.format("%02d", dayOfMonth);
                String formattedMonth = String.format("%02d", monthOfYear + 1); // monthOfYear is 0-based
                editText.setText(formattedDay + "/" + formattedMonth + "/" + year);
            }
        }, year, month, day);
        datePickerDialog.show();
    }

    private void showTimePickerDialog(final EditText timeText) {
        Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);

        TimePickerDialog timePickerDialog = new TimePickerDialog(
                this,
                new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        String time = String.format("%02d:%02d", hourOfDay, minute);
                        timeText.setText(time);
                    }
                },
                hour, minute, true);
        timePickerDialog.show();
    }


    public void subirEvento(View view){
        //Esta parte de aca verificara que no sean nulos los campos vacios
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        //para guardar los datos.
        DatabaseReference databaseReference = FirebaseDatabase.getInstance()
                .getReference()
                .child("eventos")
                .child("zaragoza")
                .child("ids");
        //Para guardar la imagen
        // Referencia al almacenamiento en Firebase
        StorageReference storageRef = FirebaseStorage.getInstance().getReference();
        String rutaStorage = "eventos/Zaragoza/";
        String eventKey = databaseReference.push().getKey();
        rutaStorage = rutaStorage + eventKey;
        StorageReference imagenRef = storageRef.child(rutaStorage);
        titulo = miToString(et_Titulo);
        description = miToString(et_Desc);
        String fecha = miToString(et_Fecha);
        startDate = miToString(et_hsInicio);
        endDate = miToString(et_hsFin);
        String ubi = miToString(et_Ubi);
        titulo = titulo.trim();
        if (titulo.equals("") || titulo.equals(" ") || titulo == null || fecha.equals("") || fecha.equals(" ") || fecha == null){
            Toast.makeText(this,"Titulo y fecha obligatorios", Toast.LENGTH_SHORT).show();
            et_Titulo.setBackgroundResource(R.drawable.caja_bordes_error);
            et_Fecha.setBackgroundResource(R.drawable.caja_bordes_error);
        }
        else {
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
                                if(startDate == null || startDate.equals("")){
                                    startDate = fecha;
                                }
                                else{
                                //Creo mi objeto evento
                                startDate = fecha + " a las " + startDate + "hs";
                                }
                                EventosAyuntamiento evento = new EventosAyuntamiento(titulo,
                                        description,
                                        urlImagen,
                                        urlImagen,
                                        startDate,
                                        endDate,
                                        ubi);
                                evento.setId(eventKey);

                                    databaseReference.child(eventKey).setValue(evento);
                                    Toast.makeText(getApplicationContext(),"Evento agregado",Toast.LENGTH_SHORT).show();

                                finish();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception exception) {
                                // No se pudo obtener la URL de la imagen
                                Toast.makeText(getApplicationContext(),exception.getMessage(),Toast.LENGTH_LONG).show();
                                urlImagen ="";
                            }
                        });
                    } else {
                        UploadTask uploadTask = imagenRef.putFile(uri);
                    }
                }
            });
        }}

    private String miToString (EditText elemento){
        try {
            String elementoString = elemento.getText().toString();
            return elementoString;
        }
        catch (Exception e){
            e.getMessage();
            return "";
        }
    }

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
                        .apply(RequestOptions.bitmapTransform(new RoundedCorners(40))) // Aplica la transformación para bordes redondeados
                        .override(200, 200)
                        .into(imagenAsubir)
                        .onLoadFailed(getDrawable(R.drawable.img_standard));
                //iv_FotoUsuario.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    private Uri obtenerUriPorDefecto(ImageView imagen) {
        // Obtener la URI de la imagen por defecto desde el tag del ImageView
        String uriString = ContentResolver.SCHEME_ANDROID_RESOURCE +
                "://" + getResources().getResourcePackageName(R.drawable.img_standard) +
                "/" + getResources().getResourceTypeName(R.drawable.img_standard) +
                "/" + getResources().getResourceEntryName(R.drawable.img_standard);

        return Uri.parse(uriString);
    }
    public void volverAtras(View v){
        super.finish();
    }
}
