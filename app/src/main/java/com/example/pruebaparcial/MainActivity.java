package com.example.pruebaparcial;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements IMLController {

    private static final int PICK_IMAGE = 100;
    Button btnimagen;
    Button btnprocesar;
    ImageView imagen;
    Uri imageUri;

    TextView textView;

    MLController mlController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnprocesar = findViewById(R.id.procesarImagen);

        mlController = new MLController(this);

        textView = (TextView) findViewById(R.id.textResult);

        imagen = (ImageView) findViewById(R.id.imgart);
        btnimagen = (Button) findViewById(R.id.seleccionarimg);

        btnimagen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGallery();
            }
        });

        btnprocesar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imagen.buildDrawingCache(true);
                Bitmap bitmap = imagen.getDrawingCache(true);
                BitmapDrawable drawable = (BitmapDrawable) imagen.getDrawable();
                bitmap = drawable.getBitmap();
                if (bitmap != null) {
                    mlController.send_image(bitmap);
                }
            }
        });
    }

    private void openGallery() {
        Intent gallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        startActivityForResult(gallery, PICK_IMAGE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == PICK_IMAGE) {
            imageUri = data.getData();
            imagen.setImageURI(imageUri);
        }
    }

    @Override
    public void get_data_result_succes(float[] floats, String message) {
        if (floats != null) {
            System.out.println(floats);
            textView.setText(floats.toString() + "--");
            Intent intent = new Intent(MainActivity.this, MapsActivity.class);
            Bundle bundle = new Bundle();
            bundle.putFloatArray("floats", floats);
            intent.putExtras(bundle);
            startActivity(intent);
        }
        System.out.println(message);
    }
}