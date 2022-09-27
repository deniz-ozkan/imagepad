package com.denizozkan.imagepad;

public class Not {

    private long id;
    private String baslik;
    private String icerik;
    private String tarih;
    private String zaman;
    private byte[] gorsel;
    private String alarmAktif;

    // Notların hepsi getirilip listeye atanırken kullanılan metot

    Not() {

    }

    // Not eklenirken kullanılan metot

    Not(String baslik, String icerik, String tarih, String zaman, byte[] gorsel, String alarmAktif) {

        this.baslik = baslik;
        this.icerik = icerik;
        this.tarih = tarih;
        this.zaman = zaman;
        this.gorsel = gorsel;
        this.alarmAktif = alarmAktif;
    }

    // ilgili not çağrılırken kullanılan metot

    Not(long id, String baslik, String icerik, String tarih, String zaman, byte[] gorsel, String alarmAktif) {

        this.id = id;
        this.baslik = baslik;
        this.icerik = icerik;
        this.tarih = tarih;
        this.zaman = zaman;
        this.gorsel = gorsel;
        this.alarmAktif = alarmAktif;
    }

    public long getId() {

        return id;
    }

    public void setId(long id) {

        this.id = id;
    }

    public String getBaslik() {

        return baslik;
    }

    public void setBaslik(String baslik) {

        this.baslik = baslik;
    }

    public String getIcerik() {

        return icerik;
    }

    public void setIcerik(String icerik) {

        this.icerik = icerik;
    }

    public String getTarih() {

        return tarih;
    }

    public void setTarih(String tarih) {

        this.tarih = tarih;
    }

    public String getZaman() {

        return zaman;
    }

    public void setZaman(String zaman) {

        this.zaman = zaman;
    }

    public byte[] getGorsel() {
        return gorsel;
    }

    public void setGorsel(byte[] gorsel) {
        this.gorsel = gorsel;

    }

    public String getAlarmAktif() {
        return alarmAktif;
    }

    public void setAlarmAktif(String alarmAktif) {
        this.alarmAktif = alarmAktif;
    }
}
