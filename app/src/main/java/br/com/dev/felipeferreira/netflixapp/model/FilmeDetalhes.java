package br.com.dev.felipeferreira.netflixapp.model;

import java.util.List;

public class FilmeDetalhes {

    private final Filme filme;
    private final List<Filme> filmesSimilar;

    public FilmeDetalhes(Filme filme, List<Filme> filmesSimilar) {
        this.filme = filme;
        this.filmesSimilar = filmesSimilar;
    }

    public Filme getFilme() {
        return filme;
    }

    public List<Filme> getFilmesSimilar() {
        return filmesSimilar;
    }
}
