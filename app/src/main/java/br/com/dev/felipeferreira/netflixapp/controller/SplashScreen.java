/*
 * *
 *  * Março, 03 2021
 *  *
 *  * @author dev.felipeferreira@gmail.com (Felipe Ferreira).
 *
 */

package br.com.dev.felipeferreira.netflixapp.controller;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import br.com.dev.felipeferreira.netflixapp.R;

public class SplashScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        Thread tempoCarregamento = new Thread() {

            @Override
            public void run() {
                try {
                    sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    Intent intent = new Intent(SplashScreen.this, PerfilActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        };
        tempoCarregamento.start();
    }
}