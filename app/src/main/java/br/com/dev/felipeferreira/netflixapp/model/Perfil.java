package br.com.dev.felipeferreira.netflixapp.model;

public class Perfil {

    private String txtNome;
    private int imgPerfil;
    private int id;

    public Perfil(int id, String txtNome, int imgPerfil) {
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

    public int getImgPerfil() {
        return imgPerfil;
    }

    public void setImgPerfil(int imgPerfil) {
        this.imgPerfil = imgPerfil;
    }
}
