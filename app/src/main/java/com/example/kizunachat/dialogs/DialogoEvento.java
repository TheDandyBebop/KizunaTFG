package com.example.kizunachat.dialogs;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.example.kizunachat.R;
import com.example.kizunachat.VentanaAñadirEvento;

public class DialogoEvento extends Activity {
    String titulo;
    String imagen;
    String description;
    String startDate;
    String endDate;
    String ubicacion;
    String fecha;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_evento);

        // Obtén los extras del intent
        Intent intent = getIntent();
        fecha = intent.getStringExtra("fecha");
        titulo = intent.getStringExtra("titulo");
        imagen = intent.getStringExtra("imagen");
        description = intent.getStringExtra("description");
        startDate = intent.getStringExtra("startDate");
        endDate = intent.getStringExtra("endDate");
        ubicacion = intent.getStringExtra("ubicacion");

        // Asigna los datos a los TextViews
        TextView ubiTextView = findViewById(R.id.tv_ubi);
        TextView tituloUbi = findViewById(R.id.tv_titulo_Ubi);
        TextView descTextView = findViewById(R.id.tv_Desc_dialog);
        TextView startTextView = findViewById(R.id.textView4);
        TextView endTextView = findViewById(R.id.textView5);
        TextView tituloInicio = findViewById(R.id.tv_titulo_Inicio);
        TextView tituloFin = findViewById(R.id.tv_titulo_Fin);
        ImageView imgEvento = findViewById(R.id.iv_dialog);
        Glide.with(this)
                .load(imagen)
                .apply(new RequestOptions()
                        .error(R.drawable.img_standard)
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        )
                .into(imgEvento);

        descTextView.setText(description);
        //Compruebo si hay fecha de inicio o de fin
        if (startDate == null || startDate.equals("")){
            startTextView.setVisibility(View.GONE);
            tituloInicio.setVisibility(View.GONE);
        }
        else{
            startTextView.setText(startDate);
        }
        if (endDate == null || endDate.equals("")){
            endTextView.setVisibility(View.GONE);
            tituloFin.setVisibility(View.GONE);
        }
        else {
            endTextView.setText(endDate);
        }
        //Compruebo si hay o no Ubicacion
        if (ubicacion == null || ubicacion.equals("")){
            ubiTextView.setVisibility(View.GONE);
            tituloUbi.setVisibility(View.GONE);
        }
        else{
            ubiTextView.setText(ubicacion);
        }

        // Configura el botón de cerrar
        Button closeButton = findViewById(R.id.btn_close);
        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        // Configura el botón de añadir al calendario
        Button addToCalendarButton = findViewById(R.id.btn_add_to_calendar);
        addToCalendarButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), VentanaAñadirEvento.class);
                intent.putExtra("Titulo", titulo);
                intent.putExtra("Desc", description);
                intent.putExtra("InicioFecha", startDate);
                intent.putExtra("FinFecha", endDate);
                intent.putExtra("Imagen", imagen);
                intent.putExtra("ubicacion",ubicacion);
                intent.putExtra("fecha",fecha);
                startActivity(intent);
            }
        });

        // Configura el botón de compartir
        Button shareButton = findViewById(R.id.btn_share);
        shareButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }
}
