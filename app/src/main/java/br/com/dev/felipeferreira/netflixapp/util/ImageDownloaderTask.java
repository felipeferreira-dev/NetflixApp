/*
 * *
 *  * Março, 03 2021
 *  *
 *  * @author dev.felipeferreira@gmail.com (Felipe Ferreira).
 *
 */

package br.com.dev.felipeferreira.netflixapp.util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.LayerDrawable;
import android.os.AsyncTask;
import android.widget.ImageView;

import androidx.core.content.ContextCompat;

import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.URL;

import br.com.dev.felipeferreira.netflixapp.R;


public class ImageDownloaderTask extends AsyncTask<String, Void, Bitmap> {

    private final WeakReference<ImageView> imageViewReference;
    private boolean shadowEnabled;

    public ImageDownloaderTask(ImageView imageView) {
        this.imageViewReference = new WeakReference<>(imageView);
    }

    public void setShadowEnabled(boolean shadowEnabled) {
        this.shadowEnabled = shadowEnabled;
    }

    @Override
    protected Bitmap doInBackground(String... strings) {
        String urlImg = strings[0];

        HttpURLConnection urlConnection = null;

        try {

            URL requestUrl = new URL(urlImg);

            urlConnection = (HttpURLConnection) requestUrl.openConnection();

            int statusCode = urlConnection.getResponseCode();

            if(statusCode != 200)
                return null;

            InputStream is = urlConnection.getInputStream();
            if(is != null)
                return BitmapFactory.decodeStream(is);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if(urlConnection != null)
                urlConnection.disconnect(); //Sempre fechar a conexão idependente de sucesso ou falha
        }

        return null;
    }

    @Override
    protected void onPostExecute(Bitmap bitmap) {
        if(isCancelled())
        bitmap = null;

        ImageView imageView = imageViewReference.get();

        if(imageView != null && bitmap != null){

            if(shadowEnabled) {
                LayerDrawable drawable = (LayerDrawable) ContextCompat.getDrawable(imageView.getContext(), R.drawable.shadows);
                if(drawable != null){
                    BitmapDrawable bitmapDrawable = new BitmapDrawable(bitmap);
                    drawable.setDrawableByLayerId(R.id.cover_drawable_shadows, bitmapDrawable);
                    imageView.setImageDrawable(drawable);
                }
            } else {

                // Ajustando o tamanho da imagem, para que fique tudo proporcional
                if (bitmap.getWidth() < imageView.getWidth() || bitmap.getHeight() < imageView.getHeight()) {
                    Matrix matrix = new Matrix();
                    matrix.postScale((float) imageView.getWidth() / (float) bitmap.getWidth(),
                            (float) imageView.getHeight() / (float) bitmap.getHeight());

                    bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, false);
                }

                imageView.setImageBitmap(bitmap);
            }
        }
    }
}
