package com.arenterprise.backgroundnotification;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    private ImageView imageView;
    private Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        imageView = (ImageView) findViewById(R.id.imageView);
        button = (Button) findViewById(R.id.button);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DownloadAsyncTask downloadAsyncTask = new DownloadAsyncTask();
               // downloadAsyncTask.execute("https://homepages.cae.wisc.edu/~ece533/images/fruits.png");
                downloadAsyncTask.execute("https://homepages.cae.wisc.edu/~ece533/images/girl.png");
            }
        });

    }


    public void showNotification() {

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "11");
        builder .setSmallIcon(R.drawable.baseline)
                .setContentTitle("Image Downloaded")
                .setContentText("The image file downloaded successfully");

        Intent notificationIntent = new Intent(MainActivity.this, CompleteActivity.class);
        notificationIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        PendingIntent pendingIntent = PendingIntent.getActivity(this,0, notificationIntent,PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(pendingIntent);


        NotificationManager notificationManager = (NotificationManager) this.getSystemService(NOTIFICATION_SERVICE);
        notificationManager.notify(0, builder.build());



    }



    private class DownloadAsyncTask extends AsyncTask<String, String, Bitmap> {
        private ProgressDialog p;

        @Override
        protected void onPreExecute() {

            super.onPreExecute();
            p = new ProgressDialog(MainActivity.this);
            p.setMessage("Downloading...");
            p.setIndeterminate(false);
            p.setCancelable(false);
            p.show();
        }

        @Override
        protected Bitmap doInBackground(String... strings) {

            InputStream inputStream;
            Bitmap bitmap = null;

            try {
                URL ImageUrl = new URL(strings[0]);
                HttpURLConnection connection = (HttpURLConnection) ImageUrl.openConnection();
                connection.setDoInput(true);
                connection.connect();
                inputStream = connection.getInputStream();
                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inPreferredConfig = Bitmap.Config.RGB_565;
                bitmap = BitmapFactory.decodeStream(inputStream, null,
                        options);
            } catch (IOException e) {
                e.printStackTrace();
            }

            return bitmap;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            //super.onPostExecute(bitmap);

            p.hide();
            imageView.setImageBitmap(bitmap);
            showNotification();

        }


    }
}
