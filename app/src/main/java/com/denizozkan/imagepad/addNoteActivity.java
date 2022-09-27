package com.denizozkan.imagepad;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ImageDecoder;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Objects;


public class addNoteActivity extends AppCompatActivity {

    Toolbar toolbar;
    EditText textBaslik;
    EditText textDetay;
    Bitmap secilenGorsel;
    ImageView imageView;
    Calendar calendar;
    String simdikiZaman;
    String simdikiSaat;
    byte[] byteArray;
    ByteArrayOutputStream outputStream;
    EditText date_in;
    EditText time_in;
    EditText date_time_in;
    long reminderDateTimeInMilliseconds = 000;
    Date date;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_note);


        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitleTextColor(ContextCompat.getColor(this, R.color.beyaz));
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setTitle("Yeni Not");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        createNotificationChannel();

        textBaslik = findViewById(R.id.textBaslik);
        textDetay = findViewById(R.id.textDetay);
        imageView = findViewById(R.id.imageView);
        date_in=findViewById(R.id.tarihSec);
        time_in=findViewById(R.id.saatSec);
        date_time_in=findViewById(R.id.tarihSaatSec);
        date_time_in.setInputType(InputType.TYPE_NULL);
        date_in.setInputType(InputType.TYPE_NULL);
        time_in.setInputType(InputType.TYPE_NULL);


        // ImageView'a eklenen görselin Bitmap değişkenine atanması ve view'a ayarlanması

//        Bitmap gorselSec = BitmapFactory.decodeResource(getApplicationContext().getResources(), R.drawable.gorselsec);
//        imageView.setImageBitmap(gorselSec);

        // addTextChangedListener başlıkta geçilen yazıyı dinleyerek aynı başlığı Toolbar'a aktarır.

        textBaslik.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                //

                if(s.length() != 0) {
                    Objects.requireNonNull(getSupportActionBar()).setTitle(s);
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        // Calender sınıfından oluşturulan nesneyle notun oluşturulma tarihi ve saati alınır

        calendar = Calendar.getInstance();
        simdikiZaman = calendar.get(Calendar.DAY_OF_MONTH)+"/"+
                (calendar.get(Calendar.MONTH)+1)+"/"+calendar.get(Calendar.YEAR);
        simdikiSaat = pad(calendar.get(Calendar.HOUR_OF_DAY))+":"+
                pad(calendar.get(Calendar.MINUTE));

       Log.d("takvim", "Tarih ve Saat: " + simdikiZaman + " " +  simdikiSaat);


        date_time_in.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDateTimeDialog(date_time_in);
            }
        });


    }

    // Saat 0-10 arasında ise başına sıfır eklenir

    private String pad(int a) {
        if (a<10)
            return "0"+a;
        return String.valueOf(a);

    }

    // showDateTimeDialog metodu tarih ve saat seçmek için oluşturulur

    private void showDateTimeDialog(final EditText date_time_in) {
        final Calendar calendar=Calendar.getInstance();
        DatePickerDialog.OnDateSetListener dateSetListener=new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                calendar.set(Calendar.YEAR,year);
                calendar.set(Calendar.MONTH,month);
                calendar.set(Calendar.DAY_OF_MONTH,dayOfMonth);

                TimePickerDialog.OnTimeSetListener timeSetListener=new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        calendar.set(Calendar.HOUR_OF_DAY,hourOfDay);
                        calendar.set(Calendar.MINUTE,minute);

                        SimpleDateFormat simpleDateFormat=new SimpleDateFormat("dd-MM-yyyy HH:mm");

                        date_time_in.setText(simpleDateFormat.format(calendar.getTime()));
                    }
                };

                new TimePickerDialog(addNoteActivity.this,timeSetListener,
                        calendar.get(Calendar.HOUR_OF_DAY),
                        calendar.get(Calendar.MINUTE),false).show();
            }
        };

        new DatePickerDialog(addNoteActivity.this,dateSetListener,
                calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)).show();
    }


    // gorselSec metodunda kullanıcıdan izin istenir eğer verilirse galeriye gidilir, daha önceden izin verilmişse tekrar izin istenmeden doğrudan galeriye gidilir.

    public void gorselSec(View view) {

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this,
                    new String[] {Manifest.permission.READ_EXTERNAL_STORAGE},1);
        } else {
            Intent Gallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(Gallery,2);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.sil_kaydet,menu);

        return true;

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {


        // menu butonu olan notKaydet'e tıklandığında seçilen görsel imageKucult sınıfından küçültülerek Byte dizisine dönüştürülür.

        if(item.getItemId() == R.id.notKaydet) {

            try {
                    Bitmap kucukGorsel = new imageKucult().imageKucult(secilenGorsel, 800);

                    outputStream = new ByteArrayOutputStream();
                    kucukGorsel.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
                    byteArray = outputStream.toByteArray();

            } catch (Exception e) {}

            // Yazma işlemi ve görsel seçme işleminden sonra elde edilen veriler parametre olarak not sınıfının metoduna geçilir.

            Not not = new Not(textBaslik.getText().toString(), textDetay.getText().toString(),
                    simdikiZaman, simdikiSaat, byteArray, date_time_in.getText().toString());

            // Veriler Veritabanına eklenir.

            Database db = new Database(this);
            db.notEkle(not);

            // Veritabanına eklenirken oluşan id bildirim kanalına gönderilmek üzere bir değişkende tutulur.

            long id = db.getID();
            Log.d("InsertedID", "ID -> " + id);


            // Tarih ve saat formatı oluşturulur ve not ekleme ekranında string olarak seçilen tarih ve saat date formatına çevrilir ardından milisaniye cinsine dönüştürülür.

            DateFormat myDateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm");

                try {
                    date = myDateFormat.parse(date_time_in.getText().toString());
                    reminderDateTimeInMilliseconds = date.getTime();

                } catch (ParseException e) {
                    Log.e("Alarm", "Parse hatasi", e);
                }


            // Bir değişkende tutulan id bildirim kanalına gönderilir

            Intent intent = new Intent(addNoteActivity.this, ReminderBroadcast.class);
            intent.putExtra("Notification", id);
            Log.d("SentID", "ID -> " + id);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(this, (int) id, intent, 0);

            AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);

            Log.d("date", date_time_in.getText().toString());
            Log.i("milisaniye", String.valueOf(reminderDateTimeInMilliseconds));


            // Eğer kullanıcı tarih girmemişse gelecekte bildirim oluşmaması için gerekli kontrol sağlanır.


            if (reminderDateTimeInMilliseconds >= 1) {
                alarmManager.setExact(AlarmManager.RTC_WAKEUP,
                        reminderDateTimeInMilliseconds, pendingIntent);
            }

            goMainActivity();

            Toast.makeText(this, "Not kaydedildi", Toast.LENGTH_SHORT).show();

        }

        if(item.getItemId() == R.id.notSil) {

            Toast.makeText(this, "Not kaydedilmedi.", Toast.LENGTH_SHORT).show();
            goMainActivity();

        }



        return super.onOptionsItemSelected(item);
    }

    private void goMainActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }


    // İzin verildiğinde kullanıcı galeriye yönlendirilir.

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {

        if (requestCode == 1) {
            if (grantResults.length > 0 && grantResults[0]
                    == PackageManager.PERMISSION_GRANTED) {
                Intent intentToGallery = new Intent(Intent.ACTION_PICK,
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intentToGallery,2);
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    // görsel seçimi yapıldıktan sonra android versiyonuna göre imageView'a görsel ataması yapılır.

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        if (requestCode == 2 && resultCode == RESULT_OK && data != null) {

            Uri imageData = data.getData();

            try {

                if (Build.VERSION.SDK_INT >= 28) {

                    ImageDecoder.Source source = ImageDecoder.createSource(this.getContentResolver(),imageData);
                    secilenGorsel = ImageDecoder.decodeBitmap(source);
                    imageView.setImageBitmap(Bitmap.createScaledBitmap(secilenGorsel,
                            secilenGorsel.getWidth()/2, secilenGorsel.getHeight()/2, false));

                } else {
                    secilenGorsel = MediaStore.Images.Media.getBitmap(this.getContentResolver(),imageData);
                    imageView.setImageBitmap(Bitmap.createScaledBitmap(secilenGorsel,
                            secilenGorsel.getWidth()/2, secilenGorsel.getHeight()/2, false));
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    // Bildirim için kanal oluşturulur.

    private void createNotificationChannel() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            CharSequence name = "ReminderChannel";
            String description = "Channel for alarm";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel("notify", name, importance);
            channel.setDescription(description);

            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);

        }
    }
}
