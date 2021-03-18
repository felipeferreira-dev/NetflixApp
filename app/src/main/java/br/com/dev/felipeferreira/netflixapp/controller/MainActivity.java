package br.com.dev.felipeferreira.netflixapp.controller;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import br.com.dev.felipeferreira.netflixapp.R;
import br.com.dev.felipeferreira.netflixapp.model.Categoria;
import br.com.dev.felipeferreira.netflixapp.model.Filme;
import br.com.dev.felipeferreira.netflixapp.util.CategoryTask;
import br.com.dev.felipeferreira.netflixapp.util.ImageDownloaderTask;

public class MainActivity extends AppCompatActivity implements CategoryTask.CategoryLoader {

    private MainAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ArrayList<Categoria> categoriasAdd = new ArrayList<>();

        RecyclerView recyclerView = findViewById(R.id.recycler_view_main);
        adapter = new MainAdapter(categoriasAdd);

        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));

        //Testando a conectividade da redemain
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        assert cm != null;
        NetworkInfo netInfo = cm.getActiveNetworkInfo();

        if (netInfo != null && netInfo.isConnectedOrConnecting()) {
            Toast.makeText(MainActivity.this, "Acessando NetflixApp!", Toast.LENGTH_LONG).show();
            CategoryTask categoryTask = new CategoryTask(this);
            categoryTask.setCategoryLoader(this);
            categoryTask.execute("https://tiagoaguiar.co/api/netflix/home");

        } else {
            Toast.makeText(MainActivity.this, "Você esta sem conexão à Internet", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    @Override
    public void onResult(List<Categoria> categories) {
        adapter.setCategories(categories);
        adapter.notifyDataSetChanged();
    }

    //****************************************************************************************************
    // MainAdapter referente à recycler VERTICAL que possui o titulo e a lista de filmes
    private class MainAdapter extends RecyclerView.Adapter<CategoryViewHolder>{

        private List<Categoria> categorias;

        private MainAdapter(List<Categoria> categorias) {
            this.categorias = categorias;
        }

        @NonNull
        @Override
        public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = getLayoutInflater().inflate(R.layout.category_item, parent, false);
            return new CategoryViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull CategoryViewHolder holder, int position) {
            Categoria categoria = categorias.get(position);
            holder.txtViewTitle.setText(categoria.getName());
            holder.recyclerViewFilme.setAdapter(new MovieAdapter(categoria.getFilmes()));
            holder.recyclerViewFilme.setLayoutManager(new LinearLayoutManager(getBaseContext(), RecyclerView.HORIZONTAL, false));
        }

        @Override
        public int getItemCount() {
            return categorias.size();
        }

        public void setCategories(List<Categoria> categories) {
            this.categorias.clear();
            this.categorias.addAll(categories);
        }
    }

    // MovieAdapter referente à recycler HORIZONTAL que possui as imagens de capa dos filmes
    private class MovieAdapter extends RecyclerView.Adapter<MovieHolder> implements OnItemClickListener {

        private final List<Filme> filmes;

        private MovieAdapter(List<Filme> filmes) {
            this.filmes = filmes;
        }

        @NonNull
        @Override
        public MovieHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = getLayoutInflater().inflate(R.layout.movie_item, parent, false);
            return new MovieHolder(view, this);
        }

        @Override
        public void onBindViewHolder(@NonNull MovieHolder holder, int position) {
            Filme filme = filmes.get(position);
            //holder.imgMovieUrl.setImageResource(filme.getCoverUrl());
            new ImageDownloaderTask(holder.imgMovieUrl).execute(filme.getCoverUrl());
        }

        @Override
        public int getItemCount() {
            return filmes.size();
        }

        @Override
        public void onClick(int position) {
            Filme filme = filmes.get(position);
            if(filme.getId() <= 3) {
                Intent intent = new Intent(MainActivity.this, MovieActivity.class);
                intent.putExtra("id", filme.getId());
                startActivity(intent);
            }
        }
    }

//****************************************************************************************************
    private static class CategoryViewHolder extends RecyclerView.ViewHolder {

        private TextView txtViewTitle;
        RecyclerView recyclerViewFilme;

        public CategoryViewHolder(@NonNull View itemView) {
            super(itemView);
            txtViewTitle = itemView.findViewById(R.id.txt_titulo_category);
            recyclerViewFilme = itemView.findViewById(R.id.recycler_view_movie_category);
        }
    }

    private static class MovieHolder extends RecyclerView.ViewHolder {

        private ImageView imgMovieUrl;

        public MovieHolder(@NonNull View itemView, final OnItemClickListener onItemClickListener) {
            super(itemView);

            imgMovieUrl = itemView.findViewById(R.id.img_cover);
            itemView.setOnClickListener(v -> onItemClickListener.onClick(getAdapterPosition()));
        }
    }

    public interface OnItemClickListener{
        void onClick(int position);
    }

    public void onBackPressed(){
        finish();
    }
}
