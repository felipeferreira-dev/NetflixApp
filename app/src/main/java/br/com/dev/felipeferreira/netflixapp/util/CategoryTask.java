package br.com.dev.felipeferreira.netflixapp.util;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

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

import br.com.dev.felipeferreira.netflixapp.model.Categoria;
import br.com.dev.felipeferreira.netflixapp.model.Filme;

// Task é uma tarefa que vai acontecer em background (Esta classe é uma classe utilitária, para fazer downloads da internet)
public class CategoryTask extends AsyncTask <String, Void, List<Categoria>> {

    private final WeakReference <Context> context;
    private ProgressDialog dialog;
    private CategoryLoader categoryLoader;

    //Criando construtor para passar o Contexto
    public CategoryTask(Context context){
        this.context = new WeakReference<>(context);
    }

    public void setCategoryLoader(CategoryLoader categoryLoader) {
        this.categoryLoader = categoryLoader;
    }

    //Main-Thread....Criando a progress bar (onPreExecute - é executada antes da thread)
    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        Context context = this.context.get();

        if(context != null)
            dialog = ProgressDialog.show(context, "Carregando", "", true);
    }

    // Este método vai ser executado em uma outra Thread (em background) - Vai renderizar as imagens de um servidor
    @Override
    protected List<Categoria> doInBackground(String... strings) {
        String url = strings[0]; /*Pegando os parâmetros da URL*/

        //Fazendo o try catch pra capturar algum erro, caso a url esteja mal formada
        try {
            //Abrindo uma requisição de url, passando a string com os parâmetros
            URL requestUrl = new URL(url);

            //Posso retornar tanto um HTTP quanto um HTTPS
            HttpsURLConnection urlConnection = (HttpsURLConnection) requestUrl.openConnection(); //Abrindo uma conexão com a url
            urlConnection.setReadTimeout(2000); //Tempo de espera de leitura 2 segundos
            urlConnection.setConnectTimeout(2000); //Tempo para mostrar uma mensagem de erro, caso a internet tenha caído

            int responseCode = urlConnection.getResponseCode(); //Pegando a resposta do servidor da url que esta hospedando as imagens

            //Se a minha resposta for maior que 400 (Ou seja caso haja um erro na página do servidor)..
            if(responseCode > 400){
                throw new IOException("Erro na comunicação com o servidor"); //Lançando uma exeção para informar o erro
            }

            InputStream inputStream = urlConnection.getInputStream(); //inputStream é uma sequência de bytes que é tratada como imagem, texto etc..

            BufferedInputStream in = new BufferedInputStream(inputStream); //Buffer para alocar um espaço de memória pra poder manipular o Stream

            String jsonAsString = toString(in); // Chamando o método toString, para passar os bytes de dados já convertidos do Stream em String
            Log.i("Teste", jsonAsString);

            //                          Retornando a lista de categoria de filmes
            List<Categoria> categorias = getCategorias(new JSONObject(jsonAsString)); //Convertendo toda a String gigantesca em um JSON Object
            in.close();

            return categorias; //Se der tudo certo, retorno a lista pronta de categorias.

        } catch (MalformedURLException e) { // Capturando erro de má formação do URL
            e.printStackTrace();
        } catch (IOException e) { //Capturando erro de conexão de internet ou com o servidor.
            e.printStackTrace();
        } catch (JSONException e) { // Capturando um possivel erro ao passar as Strings para o JSON object
            e.printStackTrace();
        }

        return null; //Se algo de errado, retorno null
    }

    //Criando um método para retornar uma lista pronta de categorias de filmes
    //Dica: para a dicionar o JSONArray, devo olhar no meu arquivo JSON do servidor, e verificar os objetos e os seus devidos tipos! neste casso...
    // "Category" é do tipo array, já "Titulo" é do tipo String e assim por diante.
   private List<Categoria> getCategorias(JSONObject json) throws JSONException {

       List<Categoria> categorias = new ArrayList<>(); //Criando a variavel de campo, para retornar a lista de categorias

       JSONArray categoryArray = json.getJSONArray("category"); //Carregando o array category

       for (int i = 0; i < categoryArray.length(); i++) {
           JSONObject categoria = categoryArray.getJSONObject(i); //Interando em todos os JSON object que vão me retornar um objeto 'categoria'
           String titulo = categoria.getString("title"); //Iterando nos titulos que são os 'Titulos das Categorias'

           List<Filme> filmes = new ArrayList<>();
           JSONArray filmeArray = categoria.getJSONArray("movie");// Iterando no JSON Array de filmes

           for (int j = 0; j < filmeArray.length(); j++) { // Loop para iterar em todos os filmes internos
               JSONObject filme = filmeArray.getJSONObject(j);

               String coverUrl = filme.getString("cover_url"); // Carregando cada uma das URLs de filmes
               int id = filme.getInt("id"); // Carregando cada um dos Ids

               //Adicionando cada url, para dentro da lista de Filmes
               Filme filmeObjeto = new Filme();
               filmeObjeto.setCoverUrl(coverUrl);
               filmeObjeto.setId(id);

               filmes.add(filmeObjeto);
           }
            // Carregando os dados para dentro da classe Categoria
           Categoria categoriaObjeto = new Categoria();
           categoriaObjeto.setName(titulo); //Settando o titulo das categorias
           categoriaObjeto.setFilmes(filmes); //Settando a lista de imagens dos filmes

           categorias.add(categoriaObjeto); //Trazendo tudo para dentro da lista de categorias
       }

       return categorias; // Retornando a lista completa dos dados
   }

    //Main-Thread....Escondendo a progress bar (onPostExecute - é executada depois da thread)
    @Override
    protected void onPostExecute(List<Categoria> categories) {
        super.onPostExecute(categories);
        dialog.dismiss();

        // listener
        if (categoryLoader != null)
            categoryLoader.onResult(categories);
    }

    // Método responsável por transformar os bytes do inputStream em String
    private String toString(InputStream is) throws IOException {
        byte[] bytes = new byte[1024]; //Alocando um array de 1024 bytes
        ByteArrayOutputStream baos = new ByteArrayOutputStream(); //Criando um array de Output SAÍDA

        int lidos; //Criando todos os bytes lidos e interando dentro dele
        while ((lidos = is.read(bytes)) > 0) {
            baos.write(bytes, 0, lidos); //Escrevo na SAÍDA esses bytes lidos
        }

        return new String(baos.toByteArray()); //Retornando uma instância de uma string, utilizando o toByteArray()
    }

    // Delegando uma interface para obrigar a MainActivity a importar os dados direto para o adapter! e assim carregar a lista do servidor, para o app!!!!!!!!!!
    public interface CategoryLoader {
        void onResult(List<Categoria> categories);
    }
}

/* OBSERVAÇÕES

- JSON = Java Script Object Notation - é uma notação padrão do java Script! O JSON é um conjunto de dados (Dentro de um banco de dados ou servidor),
onde podemos buscar esses dados através de uma URL e implementar dentro do aplicativo!

- WeakReference = Transforma em uma referência fraca!
Usado quando quero destruir uma classe, caso a minha Activity ou Fragment tenham sido destruídas no seu Ciclo de vida
EX: isso evida de fazer com que o usuário fique travado aqui na CategoryTask, caso a activity tenha sido destruida!

 */