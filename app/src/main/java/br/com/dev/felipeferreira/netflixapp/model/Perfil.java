package br.com.dev.felipeferreira.netflixapp.model;

import android.graphics.drawable.Drawable;

public class Perfil {

    private String txtNome;
    private Drawable imgPerfil;
    private int id;

    public Perfil(int id, String txtNome, Drawable imgPerfil) {
        this.id = id;
        this.txtNome = txtNome;
        this.imgPerfil = imgPerfil;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTxtNome() {
        return txtNome;
    }

    public void setTxtNome(String txtNome) {
        this.txtNome = txtNome;
    }

    public Drawable getImgPerfil() {
        return imgPerfil;
    }

    public void setImgPerfil(Drawable imgPerfil) {
        this.imgPerfil = imgPerfil;
    }
}
