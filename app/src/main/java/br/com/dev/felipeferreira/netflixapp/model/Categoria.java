/*
 * *
 *  * Março, 03 2021
 *  *
 *  * @author dev.felipeferreira@gmail.com (Felipe Ferreira).
 *
 */

package br.com.dev.felipeferreira.netflixapp.model;

import java.util.List;

public class Categoria {

    private String name;
    private List<Filme> filmes;

    public String getName() { return name; }

    public List<Filme> getFilmes() { return filmes; }

    public void setName(String name) { this.name = name; }

    public void setFilmes(List<Filme> filmes) { this.filmes = filmes; }

}
