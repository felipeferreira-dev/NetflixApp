/*
 * *
 *  * Março, 03 2021
 *  *
 *  * @author dev.felipeferreira@gmail.com (Felipe Ferreira).
 *
 */

package br.com.dev.felipeferreira.netflixapp.util;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

import br.com.dev.felipeferreira.netflixapp.model.Filme;
import br.com.dev.felipeferreira.netflixapp.model.FilmeDetalhes;

public class FilmesDetalhesTask extends AsyncTask<String, Void, FilmeDetalhes> {

    private final WeakReference<Context> context;
    private ProgressDialog dialog;
    private FilmesDetalhesLoader filmesDetalhesLoader;

    public FilmesDetalhesTask(Context context) {
        this.context = new WeakReference<>(context);
    }

    public void setFilmesDetalhesLoader(FilmesDetalhesLoader filmesDetalhesLoader) {
        this.filmesDetalhesLoader = filmesDetalhesLoader;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        Context context = this.context.get();

        if(context != null)
        dialog = ProgressDialog.show(context, "Carregando dados", "", true);
    }

    @Override
    protected FilmeDetalhes doInBackground(String... strings) {
        String url = strings[0];

        try {

            URL requestUrl = new URL(url);

            HttpsURLConnection urlConnection = (HttpsURLConnection) requestUrl.openConnection();
            urlConnection.setReadTimeout(2000);
            urlConnection.setConnectTimeout(2000);

            int statusCode = urlConnection.getResponseCode();

            if (statusCode > 400) {
                throw new IOException("Erro de comunicação com o servidor");
            }

            InputStream is = urlConnection.getInputStream();
            BufferedInputStream in = new BufferedInputStream(is);

            String jsonAsString = toString(in);

            FilmeDetalhes filmeDetalhes = getFilmeDetalhes(new JSONObject(jsonAsString));
            in.close();

            return filmeDetalhes;

        } catch (MalformedURLException e){
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private FilmeDetalhes getFilmeDetalhes(JSONObject jason) throws JSONException{
        int id = jason.getInt("id");
        String titulo = jason.getString("title");
        String descricao = jason.getString("desc");
        String elenco = jason.getString("cast");
        String coverUrl = jason.getString("cover_url");

        List<Filme> filmes = new ArrayList<>();
        JSONArray filmeArray = jason.getJSONArray("movie");

        for (int i = 0; i < filmeArray.length(); i++) {
            JSONObject filme = filmeArray.getJSONObject(i);
            String capaDoFilme = filme.getString("cover_url");
            int idSimilar = filme.getInt("id");

            Filme filmeSimilar = new Filme();
            filmeSimilar.setId(idSimilar);
            filmeSimilar.setCoverUrl(capaDoFilme);

            filmes.add(filmeSimilar);
        }

        Filme filme = new Filme();
        filme.setId(id);
        filme.setCoverUrl(coverUrl);
        filme.setTitulo(titulo);
        filme.setDescricao(descricao);
        filme.setElenco(elenco);

        return new FilmeDetalhes(filme, filmes);
    }

    @Override
    protected void onPostExecute(FilmeDetalhes filmeDetalhes) {
        super.onPostExecute(filmeDetalhes);
        dialog.dismiss();

        if(filmesDetalhesLoader != null)
            filmesDetalhesLoader.onResult(filmeDetalhes);
    }

    private String toString(InputStream is) throws IOException{
        byte[] bytes = new byte[1024];

        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        int lidos;

        while((lidos = is.read(bytes)) > 0){
            baos.write(bytes, 0, lidos);
        }

        return new String(baos.toByteArray());
    }

    public interface FilmesDetalhesLoader {
        void onResult(FilmeDetalhes filmeDetalhes);
    }
}
