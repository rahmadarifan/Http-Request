package com.android.rahmadarifanhr.httprequest;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity {
    private final static String LOG_TAG = MainActivity.class.getSimpleName();
    EditText hasil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        hasil = (EditText)findViewById(R.id.hasil);

        Button ambil = (Button) findViewById(R.id.ambil);
        final EditText link = (EditText) findViewById(R.id.link);
        ambil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = link.getText().toString();
                if (url.equals("")) {
                    Toast.makeText(getBaseContext(), "URL Kosong", Toast.LENGTH_SHORT).show();
                } else {
                    new HttpAsyncTask().execute(url);
                }

            }
        });

    }

    private class HttpAsyncTask extends AsyncTask<String, Void, String> {
        String data = null;

        @Override
        protected String doInBackground(String... params) {
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;

            try {
                URL url = new URL(params[0]);
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();
                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();
                if (inputStream == null) return null;
                reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while ((line = reader.readLine()) != null) buffer.append(line + "\n");
                if (buffer.length() == 0) return null;
                data = buffer.toString();
            } catch (IOException e) {
                Log.e(LOG_TAG, "Error", e);
                return null;
            } finally {
                if (urlConnection != null) urlConnection.disconnect();
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (final IOException e) {
                        Log.e(LOG_TAG, "Error closing stream", e);
                    }
                }
            }
            return data;
        }

        @Override
        protected void onPostExecute(String result) {
            Toast.makeText(getBaseContext(), "Data diterima", Toast.LENGTH_LONG).show();
            hasil.setText(result);
        }
    }

}
