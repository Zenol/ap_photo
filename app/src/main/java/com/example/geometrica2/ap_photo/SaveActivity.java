package com.example.geometrica2.ap_photo;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import java.io.File;
import java.io.IOException;

public class SaveActivity extends AppCompatActivity {

    EditText text = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_save);

        text = (EditText)findViewById(R.id.editText);
    }

    public void onCancel(View view) {
        //Retour a la première activité
        finish();
    }

    public void onOK(View view) {
        //On recupere le nom de fichier
        String name = text.getText().toString();
        //Recupère l'URI du fichier temporaire
        Uri uri = getIntent().getData();
        //Lien vers le futur nouveau fichier
        File f = Photo.getOutputMediaFile(name);
        if (f != null) {
            try {
                //On effectue la copie
                Photo.copy(new File(uri.getPath()), f);
                //Si tout s'est bien passé, on ajoute a la gallerie
                galleryAddPic(Uri.fromFile(f));
                //On rend la main a la première activité
                finish();
            } catch(IOException ex) {
            }
        }
    }

    //Ajoute une image a la gallerie
    private void galleryAddPic(Uri uri) {
        //Message a envoyer au MediaScanner
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);

        //On ajoute le nom du fichier à l'intention
        mediaScanIntent.setData(uri);

        //On envoi au broadcastReceiver
        this.sendBroadcast(mediaScanIntent);
    }
}
