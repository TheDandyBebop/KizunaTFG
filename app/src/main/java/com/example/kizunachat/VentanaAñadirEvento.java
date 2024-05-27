package com.example.kizunachat;
import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
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

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.ParseException;
import java.util.Calendar;

public class VentanaAñadirEvento extends AppCompatActivity {
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
        setContentView(R.layout.activity_agregar_eventos);
        et_Desc = findViewById(R.id.et_Desc_AE);
        et_Titulo = findViewById(R.id.et_Titulo_AE);
        et_hsFin = findViewById(R.id.et_HF_AE);
        et_hsInicio = findViewById(R.id.et_HI_AE);
        et_Ubi = findViewById(R.id.et_Ubi_AE);
        et_Fecha = findViewById(R.id.et_Date_AE);
        btnAceptar = findViewById(R.id.btn_Aceptar_AE);
        sp_privacy = findViewById(R.id.spinner_privacy);
        imagenAsubir = findViewById(R.id.iv_imgEvento);
        uri = obtenerUriPorDefecto(imagenAsubir);
        // Datos para el Spinner
        String[] items = new String[]{"Publico", "Privado"};
        // Crear un adaptador
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, items);
        // Configurar el adaptador
        sp_privacy.setAdapter(adapter);

        try {
            Intent intent = getIntent();
            //recupero ubicacion y fecha
            ubi = intent.getStringExtra("ubicacion");
            fecha = intent.getStringExtra("fecha");
            et_Fecha.setText(fecha);
            et_Ubi.setText(ubi);

            //Recupero el titulo y lo establezco
            titulo = intent.getStringExtra("Titulo");
            et_Titulo.setText(titulo);

            //Recupero la desc y la establezco
            description = intent.getStringExtra("Desc");
            et_Desc.setText(description);

            //recupero las fechas
            startDate = intent.getStringExtra("InicioFecha");
            endDate = intent.getStringExtra("FinFecha");
            //voy a formatearlas
            String[] fechasFormat = separateDateTime(startDate);
            if (fechasFormat[1] != null){
                et_Fecha.setText(fechasFormat[0]);
                et_hsInicio.setText(fechasFormat[1]);
                et_hsFin.setText(separateDateTime(endDate)[1]);
            }
            else {
                et_Fecha.setText(fechasFormat[0]);
            }
            //Si hay imagen, La cargo
            urlImagen = intent.getStringExtra("Imagen");
            if (urlImagen != null){
                descargarImagen(urlImagen);
            }
            else {
                Glide.with(this)
                        .load(getDrawable(R.drawable.img_standard))
                        .apply(RequestOptions.bitmapTransform(new RoundedCorners(40))) // Aplica la transformación para bordes redondeados
                        .override(200, 200)
                        .into(imagenAsubir)
                        .onLoadFailed(getDrawable(R.drawable.img_standard));
            }
        }
        catch (Exception e){
            e.getMessage();
        }
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
    public static String[] separateDateTime(String dateTimeString) throws ParseException {
        String[] fechasHS = new String[2];
        if (dateTimeString.contains("a las")) {
            String time = dateTimeString.substring(dateTimeString.indexOf("a las") + 7, dateTimeString.lastIndexOf("hs")).trim();
            String date = dateTimeString.substring(0, dateTimeString.indexOf("a las")).trim();
            fechasHS[1] = time;
            fechasHS[0] = date;
            return  fechasHS;
        } else if (dateTimeString.contains("de")) {
            String[] parts = dateTimeString.split(" de ");
            if (parts.length == 2) {
                String day = parts[0].trim();
                String month = convertMonthToNumber(parts[1].trim());
                String    date = day + "/" + month;
            fechasHS[0] = date;
            fechasHS[1] = null;
        }
            return fechasHS;
    }
        else {
            fechasHS[0] = dateTimeString;
            return fechasHS;
        }
    }

    public void subirEvento(View view){
        //Esta parte de aca verificara que no sean nulos los campos vacios
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        //para guardar los datos.
        DatabaseReference databaseReference = FirebaseDatabase.getInstance()
                .getReference()
                .child("usuarios")
                .child(user.getUid())
                .child("misEventos");
        //Para guardar la imagen
        // Referencia al almacenamiento en Firebase
        StorageReference storageRef = FirebaseStorage.getInstance().getReference();
        String rutaStorage = "eventos/Zaragoza/";
        String eventKey = databaseReference.push().getKey();
        rutaStorage = rutaStorage + eventKey;
        StorageReference imagenRef = storageRef.child(rutaStorage);
        String privacidad = (String) sp_privacy.getSelectedItem();
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
                            //Creo mi objeto evento
                            EventosUsuarios evento = new EventosUsuarios(titulo,
                                    description,
                                    fecha,
                                    startDate,
                                    endDate,
                                    ubi,
                                    urlImagen);

                            if (privacidad.equals("Publico")){
                                databaseReference.child("Publico").child(eventKey).setValue(evento);
                                Toast.makeText(VentanaAñadirEvento.this,"Evento agregado",Toast.LENGTH_SHORT).show();
                            }
                            else if (privacidad.equals("Privado")){
                                databaseReference.child("Privado").child(eventKey).setValue(evento);
                                Toast.makeText(VentanaAñadirEvento.this,"Evento agregado",Toast.LENGTH_SHORT).show();
                            }
                            finish();
                            }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        // No se pudo obtener la URL de la imagen
                        Toast.makeText(VentanaAñadirEvento.this,exception.getMessage(),Toast.LENGTH_LONG).show();
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
    public static String convertMonthToNumber(String monthString) {
        switch (monthString.toLowerCase()) {
            case "enero":
                return "01";
            case "febrero":
                return "02";
            case "marzo":
                return "03";
            case "abril":
                return "04";
            case "mayo":
                return "05";
            case "junio":
                return "06";
            case "julio":
                return "07";
            case "agosto":
                return "08";
            case "septiembre":
                return "09";
            case "octubre":
                return "10";
            case "noviembre":
                return "11";
            case "diciembre":
                return "12";
            default:
                return monthString;
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
    private class DescargarImagenTask extends AsyncTask<String, Void, Bitmap> {
        @Override
        protected Bitmap doInBackground(String... urls) {
            String imageUrl = urls[0];
            Bitmap bitmap = null;
            try {
                // Convierte la URL HTTPS a una URL de Java
                URL url = new URL(imageUrl);
                // Abre una conexión HTTP para descargar la imagen
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setDoInput(true);
                connection.connect();
                // Obtiene la entrada de la conexión y la convierte en un Bitmap
                InputStream input = connection.getInputStream();
                bitmap = BitmapFactory.decodeStream(input);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return bitmap;
        }

        @SuppressLint("WrongThread")
        @Override
        protected void onPostExecute(Bitmap bitmap) {
            if (bitmap != null) {
                // Guardar la imagen descargada localmente en el almacenamiento interno del dispositivo
                String filename = "imagen_descargada.jpg";
                try {
                    FileOutputStream outputStream = openFileOutput(filename, Context.MODE_PRIVATE);
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
                    outputStream.close();
                    // Obtener la URI del archivo guardado localmente
                    uri = FileProvider.getUriForFile(VentanaAñadirEvento.this, getApplicationContext().getPackageName() + ".provider", new File(getFilesDir(), filename));
                    // Actualiza la interfaz de usuario con la imagen descargada, desactivando el almacenamiento en caché
                    Glide.with(VentanaAñadirEvento.this)
                            .load(uri)
                            .apply(RequestOptions.bitmapTransform(new RoundedCorners(40))) // Aplica la transformación para bordes redondeados
                            .override(200, 200)
                            .skipMemoryCache(true)
                            .diskCacheStrategy(DiskCacheStrategy.NONE)
                            .into(imagenAsubir)
                            .onLoadFailed(getDrawable(R.drawable.img_standard));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                // Maneja el caso en que la descarga de la imagen falle
            }
        }

    }
    public void descargarImagen(String data){
        uri = Uri.parse(data);
        if (uri.getScheme().equals("http") || uri.getScheme().equals("https")) {
            new DescargarImagenTask().execute(data);
        } else {
            // Maneja otros tipos de URI (por ejemplo, recursos locales)
        }
    }
}
