package com.denizozkan.imagepad;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import android.os.Bundle;

import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Objects;

public class NoteDetailActivity extends AppCompatActivity {

    TextView notDetaylari;
    long id;
    Database db;
    Not not;
    AlertDialog.Builder builder;
    ImageView gorselDetaylari;
    Bitmap bmp;
    TextView tarihSaatDetay;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_detail);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        notDetaylari = findViewById(R.id.notDetaylari);
        tarihSaatDetay = findViewById(R.id.tarihSaatDetay);
        gorselDetaylari = findViewById(R.id.gorselDetaylari);

        builder = new AlertDialog.Builder(this);

        // Adapter sınıfından gönderilen ID değeri id değişkenine atanır.

        Intent intent = getIntent();
        id = intent.getLongExtra("ID", 0);
        id = intent.getLongExtra("notificationIntent", id);

        // id değişkenindeki değer sayesinde Database sınıfınının notGetir() metodu kullanılarak ilgili not getirilir.

        db = new Database(this);
        not = db.notGetir(id);
        Objects.requireNonNull(getSupportActionBar()).setTitle(not.getBaslik());

        try {

            // Eğer kullanıcı not kaydederken görsel seçmediyse görsel için ayrılan bölüm saydam yapılır, eğer seçtiyse ilgili görsel Byte dizisi olarak getirilir ve çözümlenerek değişkene atanır.

        if (not.getGorsel() == null)
        {
            gorselDetaylari.setImageAlpha(0);
        } else {

                bmp = BitmapFactory.decodeByteArray(not.getGorsel(), 0, not.getGorsel().length);
                gorselDetaylari.setImageBitmap(Bitmap.createScaledBitmap(bmp, bmp.getWidth(), bmp.getHeight(), true));

            }
        } catch (Exception e) {

        }

        notDetaylari.setText(not.getIcerik());
        tarihSaatDetay.setText(not.getAlarmAktif());

        builder.setTitle(not.getBaslik());
        builder.setMessage("Silmek istediginize emin misiniz?");

        Toast.makeText(this, not.getBaslik() + " isimli not açıldı", Toast.LENGTH_SHORT).show();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.sil,menu);

        return true;

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(item.getItemId() == R.id.deleteNot) {

            builder.setPositiveButton("Evet", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    db.deleteNot(not.getId());
                    Toast.makeText(getApplicationContext(),
                            "Not silindi.",Toast.LENGTH_SHORT).show();
                    goMainActivity();
                }
            });
            builder.setNegativeButton("Hayir", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Toast.makeText(getApplicationContext(),
                            "Not silinmedi.",Toast.LENGTH_SHORT).show();
                    goMainActivity();
                }
            });
            AlertDialog alertDialog = builder.create();
            alertDialog.show();
        }
        return super.onOptionsItemSelected(item);
    }

    // yapılan işlemlerin ardından ana ekrana gitmek için yazılmış bir metot.

    private void goMainActivity() {
        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);

    }
}
