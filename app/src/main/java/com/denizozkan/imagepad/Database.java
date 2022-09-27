package com.denizozkan.imagepad;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import java.util.ArrayList;
import java.util.List;

public class Database extends SQLiteOpenHelper {

    private static final int DB_Version = 2;
    private static final String DB_NAME = "notedatabase";
    private static final String DB_TABLE = "notetable";
    private static final String Key_Id = "id";
    private static final String Key_Baslik = "baslik";
    private static final String Key_Icerik = "icerik";
    private static final String Key_Tarih = "tarih";
    private static final String Key_Zaman = "zaman";
    private static final String Key_Gorsel = "gorsel";
    private static final String Key_Alarm_Aktif = "aktif";
    long ID;

    Database(Context context) {
        super(context, DB_NAME, null, DB_Version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String query = "CREATE TABLE "+ DB_TABLE +" ("+
                Key_Id+" INTEGER PRIMARY KEY,"+
                Key_Baslik+" TEXT,"+
                Key_Icerik+" TEXT,"+
                Key_Tarih+" TEXT,"+
                Key_Zaman+" TEXT,"+
                Key_Gorsel+" BLOB,"+
                Key_Alarm_Aktif+" TEXT"
                +" )";
        db.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        if(oldVersion >= newVersion)
            return;

        db.execSQL(" DROP TABLE IF EXISTS "+ DB_TABLE );
        onCreate(db);

    }

    public long notEkle(Not not) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(Key_Baslik, not.getBaslik());
        contentValues.put(Key_Icerik, not.getIcerik());
        contentValues.put(Key_Tarih, not.getTarih());
        contentValues.put(Key_Zaman, not.getZaman());
        contentValues.put(Key_Gorsel, not.getGorsel());
        contentValues.put(Key_Alarm_Aktif, not.getAlarmAktif());

        ID = db.insert(DB_TABLE, null,contentValues);

        Log.d("SetID", "ID -> " + ID);
        setID(ID);

        getID();
        Log.d("GetID", "ID -> " + getID());

        return ID;
    }


    public long getID() {

        return ID;
    }

    public void setID(long ID) {
        this.ID = ID;
    }

    public Not notGetir(long id) {

        SQLiteDatabase database = this.getReadableDatabase();
        Cursor c = database.query(DB_TABLE, new String[]{Key_Id, Key_Baslik, Key_Icerik, Key_Tarih,
                Key_Zaman, Key_Gorsel, Key_Alarm_Aktif}, Key_Id+"=?",
                new String[] {String.valueOf(id)}, null, null, null);

        if (c != null)
            c.moveToFirst();

        Not not = new Not(c.getLong(0), c.getString(1),c.getString(2),
                c.getString(3),c.getString(4),
                c.getBlob(5), c.getString(6));

                return not;
    }

    public List<Not> notlariGetir() {

        List<Not> butunNotlar = new ArrayList<>();

        String query = "SELECT * FROM "+DB_TABLE+ " ORDER BY "+Key_Id+" DESC";

        SQLiteDatabase database = this.getReadableDatabase();

        Cursor c = database.rawQuery(query, null);

        if (c.moveToFirst()) {

            do {Not not = new Not();
                not.setId(c.getLong(0));
                not.setBaslik(c.getString(1));
                not.setIcerik(c.getString(2));
                not.setTarih(c.getString(3));
                not.setZaman(c.getString(4));
                not.setGorsel(c.getBlob(5));
                not.setAlarmAktif(c.getString(6));

                butunNotlar.add(not);}
            while (c.moveToNext());

        }
        return butunNotlar;
    }

    void deleteNot(long id){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(DB_TABLE,Key_Id+"=?",new String[]{String.valueOf(id)});
        db.close();
    }

    void deleteAllNot() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("delete from "+ DB_TABLE);

    }
}
