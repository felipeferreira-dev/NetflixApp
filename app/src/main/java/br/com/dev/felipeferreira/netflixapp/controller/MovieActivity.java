package br.com.dev.felipeferreira.netflixapp.controller;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import br.com.dev.felipeferreira.netflixapp.R;
import br.com.dev.felipeferreira.netflixapp.model.Filme;
import br.com.dev.felipeferreira.netflixapp.model.FilmeDetalhes;
import br.com.dev.felipeferreira.netflixapp.util.FilmesDetalhesTask;
import br.com.dev.felipeferreira.netflixapp.util.ImageDownloaderTask;

public class MovieActivity extends AppCompatActivity implements FilmesDetalhesTask.FilmesDetalhesLoader {

    private TextView txtTitulo;
    private TextView txtDescricao;
    private TextView txtElenco;
    private ImageView imgCapaHorizontal;
    private MovieAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie);

        txtTitulo = findViewById(R.id.txt_titulo_activity_movie);
        txtDescricao = findViewById(R.id.txt_descricao_movie);
        txtElenco = findViewById(R.id.txt_cast_movie);
        imgCapaHorizontal = findViewById(R.id.img_cover_horizontal_activity_movie);

        Toolbar toolbar = findViewById(R.id.toolbar_activity_movie);
        setSupportActionBar(toolbar);

        //Configurando a toolbar, para que seja possivel adicionar os itens diretamente nela
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_24);
            getSupportActionBar().setTitle(null);
        }

        //Pegando a referência de um drawable!!! Configurando uma nova imagem no Drawable
        LayerDrawable drawable = (LayerDrawable) ContextCompat.getDrawable(this, R.drawable.shadows);
        if (drawable != null) {
            Drawable movieCover = ContextCompat.getDrawable(this, R.drawable.shadows);
            drawable.setDrawableByLayerId(R.id.cover_drawable_shadows, movieCover);
          //  ((ImageView) findViewById(R.id.img_cover_horizontal_activity_movie)).setImageDrawable(drawable);
        }

//**********************************************************************************************************
        ArrayList<Filme> filmesAdd = new ArrayList<>();

        RecyclerView recyclerView = findViewById(R.id.recycler_view_movie);
        adapter = new MovieAdapter(filmesAdd);
        recyclerView.setAdapter(adapter);
        recyclerView.setFocusable(false);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 3));

        Bundle extras = getIntent().getExtras();
        if(extras != null) {
            int id = extras.getInt("id");
            FilmesDetalhesTask filmesDetalhesTask = new FilmesDetalhesTask(this);
            filmesDetalhesTask.setFilmesDetalhesLoader(this);
            filmesDetalhesTask.execute("https://tiagoaguiar.co/api/netflix/" + id);
        }
    }

    //**********************************************************************************************************

    //Buscando os dados do servidor, para a Descrição !!
    @Override
    public void onResult(FilmeDetalhes filmeDetalhes) {
        txtTitulo.setText(filmeDetalhes.getFilme().getTitulo());
        txtDescricao.setText(filmeDetalhes.getFilme().getDescricao());
        txtElenco.setText(filmeDetalhes.getFilme().getElenco());

        ImageDownloaderTask imageDownloaderTask = new ImageDownloaderTask(imgCapaHorizontal);
        imageDownloaderTask.setShadowEnabled(true);
        imageDownloaderTask.execute(filmeDetalhes.getFilme().getCoverUrl());

        adapter.setFilmes(filmeDetalhes.getFilmesSimilar());
        adapter.notifyDataSetChanged();
    }

    //**********************************************************************************************************
    private class MovieAdapter extends RecyclerView.Adapter<MovieHolder> {

        private final List<Filme> filmes;

        private MovieAdapter(List<Filme> filmes) {
            this.filmes = filmes;
        }

        public void setFilmes(List<Filme> filmes) {
            this.filmes.clear();
            this.filmes.addAll(filmes);
        }

        @NonNull
        @Override
        public MovieHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = getLayoutInflater().inflate(R.layout.movie_item_similar, parent, false);
            return new MovieHolder(view);
        }


        //onBindViewHolder carrega as alterações pro app
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
    }

    private static class MovieHolder extends RecyclerView.ViewHolder {

        private ImageView imgMovieUrl;

        public MovieHolder(@NonNull View itemView) {
            super(itemView);

            imgMovieUrl = itemView.findViewById(R.id.img_cover);

        }
    }
}
