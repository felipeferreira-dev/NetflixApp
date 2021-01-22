package br.com.dev.felipeferreira.netflixapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.media.Image;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import br.com.dev.felipeferreira.netflixapp.model.Perfil;

public class PerfilActivity extends AppCompatActivity {

    private ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil);

        ArrayList<Perfil> perfilAdd = new ArrayList<>();
        perfilAdd.add(new Perfil(1, "Felipe Ferreira", R.drawable.perfil));
        perfilAdd.add(new Perfil(2, "José Carlos", R.drawable.background_perfil));
        perfilAdd.add(new Perfil(3, "Adicionar perfil", R.drawable.ic_baseline_add_circle_24));

        RecyclerView recyclerView = findViewById(R.id.recycler_view_perfil);
        PerfilAdapter adapter = new PerfilAdapter(perfilAdd);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
    }

    public class PerfilAdapter extends RecyclerView.Adapter<PerfilViewHolder> implements PerfilViewHolder.OnItemClickListener {

        private final List<Perfil> perfilAdd;

        public PerfilAdapter(List<Perfil> perfilAdd) {
            this.perfilAdd = perfilAdd;
        }

        @NonNull
        @Override
        public PerfilViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = getLayoutInflater().inflate(R.layout.perfil_item, parent, false);
            return new PerfilViewHolder(view, this);
        }

        @Override
        public void onBindViewHolder(@NonNull PerfilViewHolder holder, int position) {
            Perfil perfil = perfilAdd.get(position);
            holder.bind(perfil);
        }

        @Override
        public int getItemCount() {
            return perfilAdd.size();
        }

        @Override
        public void onClick(int position) {
            Perfil perfil = perfilAdd.get(position);
            switch (perfil.getId()){
                case 1: {
                    startActivity(new Intent(PerfilActivity.this, MainActivity.class));
                } break;

                case 2: {
                    startActivity(new Intent(PerfilActivity.this, MainActivity.class));
                } break;
            }
        }
    }

    public static class PerfilViewHolder extends RecyclerView.ViewHolder{

        interface OnItemClickListener{
            void onClick(int position);
        }

        private TextView txtViewHolder;
        private ImageView imgViewHolder;

        public PerfilViewHolder(@NonNull View itemView, final OnItemClickListener listener) {
            super(itemView);

            txtViewHolder = itemView.findViewById(R.id.txt_nome_perfil);
            imgViewHolder = itemView.findViewById(R.id.img_perfil);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onClick(getAdapterPosition());
                }
            });
        }

        public void bind(Perfil perfil) {
            txtViewHolder.setText(perfil.getTxtNome());
            imgViewHolder.setImageResource(perfil.getImgPerfil());
        }
    }
}