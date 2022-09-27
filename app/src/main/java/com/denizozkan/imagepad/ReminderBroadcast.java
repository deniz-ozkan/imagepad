package com.denizozkan.imagepad;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;


public class ReminderBroadcast extends BroadcastReceiver {

    Not not;
    Database db;
    long id;


    @Override
    public void onReceive(Context context, Intent intent) {


        // ilgili not'un bildirimde çağrılması için gerekli id veritabanına geçilirken buraya da gönderilir.

        try {
            id = intent.getLongExtra("Notification", 0);
            Log.d("GelenID", "ID -> " + id);
            db = new Database(context);
            not = db.notGetir(id);

            //bildirimi geçilmiş notu çağırmak için kullanılan yapı kullanıcıyı ilgili nota götürür.

            Intent notificationIntent = new Intent(context, NoteDetailActivity.class);
            notificationIntent.putExtra("notificationIntent", id);
            PendingIntent contentIntent = PendingIntent.getActivity(context, 1,
                    notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

            // addNoteActivity sınıfında oluşturulan bildirim kanalının verileri kullanılarak bildirim arayüzü oluşturulur.

            NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "notify")
                    .setSmallIcon(R.drawable.ic_notifications_active_black_24dp)
                    .setContentTitle(not.getBaslik())
                    .setContentText(not.getIcerik())
                    .setContentIntent(contentIntent)
                    .setAutoCancel(true)
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT);

            NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(context);

            notificationManagerCompat.notify(200, builder.build());

            } catch (Exception e) {
            Log.e("Hata", "Hata", e);
        }
    }


}
