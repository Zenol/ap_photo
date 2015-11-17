package com.example.geometrica2.ap_photo;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Photo extends AppCompatActivity {

    private File tmpFile = null;


    //Notre image view
    ImageView imageView = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo);

        imageView = (ImageView)findViewById(R.id.imageView);
        tmpFile = getOutputMediaFile(null);
        tmpFile.deleteOnExit();
    }

    public void saveClick(View view) {
        Intent saveI = new Intent(this, SaveActivity.class);
        saveI.setData(Uri.fromFile(tmpFile));
        startActivity(saveI);;
    }

    public void camClick(View view) {
        //Creé une intention de capture
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        //Construit une uri depuis le fichier
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(tmpFile));

        //Lance la capture
        startActivityForResult(intent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (data != null) {
            //Supprime l'image affiché
            imageView.setImageURI(null);
            //Affiche l'image prise a l'instant
            imageView.setImageURI(Uri.fromFile(tmpFile));
        }
    }


    /** Create a File for saving an image or video */
    public static File getOutputMediaFile(String name){
        //Choisit le dossier où enregistrer l'image
        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES), "Photos-APP");

        //Créé un dossier temporaire
        if (! mediaStorageDir.exists()){
            if (! mediaStorageDir.mkdirs()){
                Log.d("Photos-APP", "failed to create directory");
                return null;
            }
        }

        //Créer un fichier a nom unique
        File mediaFile;
        if (name == null || name == "")
            mediaFile = new File(mediaStorageDir.getPath() + File.separator
                    + "tmp.IMG.jpg");
        else
            mediaFile = new File(mediaStorageDir.getPath() + File.separator
                    + "IMG_"+ name + ".jpg");

        return mediaFile;
    }

    //Copie un fichier
    public static void copy(File src, File dst) throws IOException {
        //Flux In et Out
        InputStream in = new FileInputStream(src);
        OutputStream out = new FileOutputStream(dst);

        // Copie de blocs de données
        byte[] buf = new byte[1024];
        int len;
        while ((len = in.read(buf)) > 0) {
            out.write(buf, 0, len);
        }

        //On ferme les flux
        in.close();
        out.close();
    }

    @Override
    protected void onDestroy() {
        super.onStop();
        Log.d("Suppression - Stop : ", String.valueOf(tmpFile.delete()));
        imageView.setImageURI(null);

    }
}
