package com.denizozkan.imagepad;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.app.AlertDialog;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    Toolbar toolbar;
    RecyclerView recyclerView;
    Adapter adapter;
    Database db;
    AlertDialog.Builder builder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        recyclerView = findViewById(R.id.noteList);

        builder = new AlertDialog.Builder(this);

        builder.setTitle("Bütün notlar silinecek!");
        builder.setMessage("Bütün notları silmek istediğinize emin misiniz?");
        // Kaydedilen bütün notlar çağrılır.

        db = new Database(this);
        List<Not> butunNotlar = db.notlariGetir();
        display(butunNotlar);

    }

    //display metodu adapter yardımıyla ilgili not'u ve özelliklerini gösterir.

    private void display(List<Not> filtreliNot) {
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new Adapter(this,filtreliNot);
        recyclerView.setAdapter(adapter);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        //Menü butonlarının tanımlanması

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_add,menu);
        inflater.inflate(R.menu.menu_arama,menu);
        inflater.inflate(R.menu.deleteallnot,menu);

        MenuItem baslikAra = menu.findItem(R.id.arama);

        //SearchView sınıfından oluşturulan nesne ile herhangi bir yazı değişiminde RecyclerView güncellenerek aranılan notu gösterir.

        SearchView searchView = null;
        if (baslikAra != null) {

            searchView = (SearchView) baslikAra.getActionView();

        }

            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

                @Override
                public boolean onQueryTextSubmit(String query) {
                    return false;
                }

                @Override
                public boolean onQueryTextChange(String newText) {

                    adapter.getFilter().filter(newText);
                    return false;
                }
            });
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        // Ekle butonuna basılması durumunda addNoteActivity sınıfı intent ile çağrılarak not ekleme işlemi yapılır

        if(item.getItemId() == R.id.add) {

            Intent intent = new Intent(this,addNoteActivity.class);
            startActivity(intent);

           // Toast.makeText(this, "Ekle butonuna basildi", Toast.LENGTH_SHORT).show();

        }

        // Sil butonu bütün notları siler.

        if(item.getItemId() == R.id.deleteallnote) {

            builder.setPositiveButton("Evet", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    db.deleteAllNot();

                    finish();
                    overridePendingTransition( 0, 0);
                    startActivity(getIntent());
                    overridePendingTransition( 0, 0);
                    Toast.makeText(getApplicationContext(),"Notlar silindi.",Toast.LENGTH_SHORT).show();
                }
            });
            builder.setNegativeButton("Hayir", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Toast.makeText(getApplicationContext(),"Notlar silinmedi.",Toast.LENGTH_SHORT).show();

                }
            });
            AlertDialog alertDialog = builder.create();
            alertDialog.show();
        }

        return super.onOptionsItemSelected(item);
    }

}
