/*
 * *
 *  * Março, 03 2021
 *  *
 *  * @author dev.felipeferreira@gmail.com (Felipe Ferreira).
 *
 */

package br.com.dev.felipeferreira.netflixapp.controller;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import br.com.dev.felipeferreira.netflixapp.R;
import br.com.dev.felipeferreira.netflixapp.model.Perfil;

public class PerfilActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil);

        ArrayList<Perfil> perfilAdd = new ArrayList<>();

        Drawable cover_perfil_felipe = setDrawablePerfil(R.drawable.perfil);
        perfilAdd.add(new Perfil(1, "Felipe Ferreira", cover_perfil_felipe));

        Drawable cover_perfil_jose = setDrawablePerfil(R.drawable.imagem_perfil_login_01);
        perfilAdd.add(new Perfil(2, "José Carlos", cover_perfil_jose));

        Drawable cover_add_perfil = setDrawablePerfil(R.drawable.ic_baseline_add_circle_24);
        perfilAdd.add(new Perfil(3, "Adicionar perfil", cover_add_perfil));

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
                case 1:

                case 2:
                    startActivity(new Intent(PerfilActivity.this, MainActivity.class));
                 break;
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
            itemView.setOnClickListener(v -> listener.onClick(getAdapterPosition()));
        }

        public void bind(Perfil perfil) {
            txtViewHolder.setText(perfil.getTxtNome());
            imgViewHolder.setImageDrawable(perfil.getImgPerfil());
        }
    }

    private Drawable setDrawablePerfil(int cover) {
        LayerDrawable drawable = (LayerDrawable) ContextCompat.getDrawable(this, R.drawable.background_perfil);
        if (drawable != null) {
            Drawable movieCover = ContextCompat.getDrawable(this, cover);
            drawable.setDrawableByLayerId(R.id.cover_drawable_perfil, movieCover);
        }

        return drawable;
    }
}