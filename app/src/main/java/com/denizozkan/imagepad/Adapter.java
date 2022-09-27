package com.denizozkan.imagepad;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;


// RecyclerView'ın adapter yardımıyla ilgili notların değerleri birbirine bağlanarak tekil not ana ekranda gösterilir.

public class Adapter extends RecyclerView.Adapter<Adapter.ViewHolder> {

    private LayoutInflater layoutInflater;
    private List<Not> notList;
    private List<Not> filtreliNotListesi;
    long id;

    Adapter(Context context, List<Not> notList) {

        this.layoutInflater = LayoutInflater.from(context);
        this.filtreliNotListesi = notList;
        this.notList = notList;

    }

    @NonNull
    @Override
    public Adapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = layoutInflater.inflate(R.layout.list_view, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Adapter.ViewHolder holder, int position) {

        // filtreliNotListesi yardımıyla oluşturulan yeni listede
        // ID'si geçilen not'un değerlerinin ilgili yerlere ataması yapılır.

        String baslik = filtreliNotListesi.get(position).getBaslik();
        String tarih = filtreliNotListesi.get(position).getTarih();
        String zaman = filtreliNotListesi.get(position).getZaman();

        id = filtreliNotListesi.get(position).getId();

        holder.cardBaslik.setText(baslik);
        holder.cardTarih.setText(tarih);
        holder.cardZaman.setText(zaman);
        holder.cardId.setText(String.valueOf(id));
    }

    @Override
    public int getItemCount() {
        return filtreliNotListesi.size();
    }


    //Notlar arasında arama yapmak için filtreleme işlemi uygulanır. Filtreleme işlemine başlıklar tabi tutulur.

    public Filter getFilter() {

        // Eğer arama yapılmadıysa tüm liste filtreli listeye eşitlenerek bütün notların gözükmesi sağlanır, arama yapıldıysa geçici bir liste oluşturularak o liste gösterilir.

        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {

                String baslikAra = charSequence.toString();

                if (baslikAra.isEmpty()) {
                    filtreliNotListesi = notList;

                } else {

                    ArrayList<Not> tempFiltreListesi = new ArrayList<>();

                    for (Not not : notList) {

                        if (not.getBaslik().toLowerCase().contains(charSequence.toString().toLowerCase())) {
                            tempFiltreListesi.add(not);
                        }
                    }
                    filtreliNotListesi = tempFiltreListesi;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = filtreliNotListesi;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                filtreliNotListesi = (List<Not>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView cardBaslik, cardTarih, cardZaman, cardId;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            cardBaslik = itemView.findViewById(R.id.cardBaslik);
            cardTarih = itemView.findViewById(R.id.cardTarih);
            cardZaman = itemView.findViewById(R.id.cardZaman);
            cardId = itemView.findViewById(R.id.cardId);

            // İlgili notu açmak için bir ID gönderilerek NoteDetailActivity sınıfı çağrılır.

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                  //  Toast.makeText(v.getContext(), "Not açıldı", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(v.getContext(), NoteDetailActivity.class);
                    intent.putExtra("ID", filtreliNotListesi.get(getAdapterPosition()).getId());
                    v.getContext().startActivity(intent);
                }

            });

        }

    }
}
