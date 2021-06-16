package f.l.tpfinal_c54;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Hashtable;

// Classe qui sert d'adapteur pour le recycler view. Ref: https://www.youtube.com/watch?v=Vyqz_-sJGFk
public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder>{
    private ArrayList<String> listNoms = new ArrayList<>();
    private ArrayList<String> listRegions = new ArrayList<>();
    private Hashtable<String, Bitmap> mImage = new Hashtable<String, Bitmap>();
    private Context ctx;

    // Constructeur
    public RecyclerViewAdapter(ArrayList<String> listNoms, ArrayList<String> listRegions, Hashtable<String, Bitmap> image, Context ctx) {
        this.listNoms = listNoms;
        this.listRegions = listRegions;
        this.mImage = image;
        this.ctx = ctx;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_pour_liste, parent, false);
        ViewHolder holder = new ViewHolder(view);

        return holder;
    }

    @Override
    public int getItemCount() {
        return listNoms.size();
    }

    // assigne à la Vue les éléments dans la liste
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.imgVille.setImageBitmap(mImage.get(listNoms.get(position).toLowerCase()));
        holder.nomVille.setText(listNoms.get(position));
        holder.nomRegion.setText(listRegions.get(position));

        // l'écouteur lorsqu'on clique sur un élément de la liste
        holder.parent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(ctx, listNoms.get(position), Toast.LENGTH_SHORT).show();
                gotoVille(listNoms.get(position), position); // Param pour extras
            }
        });
    }

    private void gotoVille (String ville, int pos){
        Intent i = new Intent(ctx, ConsultationVilles.class);
        // extras pour affichage dans la classe consultationVille
        i.putExtra("nom", ville);
        i.putExtra("index", pos);
        ctx.startActivity(i);
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        // ce que contient la vue
        TextView nomVille, nomRegion;
        ImageView imgVille;
        LinearLayout parent;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            nomVille = itemView.findViewById(R.id.nomVille);
            nomRegion = itemView.findViewById(R.id.nomRegion);
            imgVille = itemView.findViewById(R.id.imgVille);
            parent = itemView.findViewById(R.id.parentContenant);
        }
    }
}
