package br.example.camera;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;

import java.io.File;
import java.io.IOException;

public class MainActivity extends AppCompatActivity {
    File imageFile;
    File storageDir;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void chamaCamera(View view) throws IOException {
        // Referencia o diretório de armazenamento
        storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        // Cria o arquivo temporário, guardar essa variável, possivelmente como
        // atributo da Activity, para ser usada no onActivityResult()
        imageFile = File.createTempFile("imagem1", ".jpg", storageDir);
        // Obtém a URI do aquivo temporário no diretório de armazenamento
        Uri photoURI = FileProvider.getUriForFile(this,"br.example.camera.fileprovider",imageFile);

        Intent fotoIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Chama a câmera e passa como parâmetro o arquivo temporário
        fotoIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
        if (fotoIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(fotoIntent, 1);
        }

    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK) {
            // imageFile foi criado antes da chamada da câmera, possivelmente um
            // atributo da Activity
            String pathAbsoluto = imageFile.getAbsolutePath();
            // Cria-se um Intent para escanear o novo arquivo e atualizar a galeria
            Intent mediaScanIntent = new

                    Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);

            File f = new File(pathAbsoluto);
            Uri contentUri = Uri.fromFile(f);
            mediaScanIntent.setData(contentUri);
            this.sendBroadcast(mediaScanIntent);
        }
    }
    //§ Onde:
    //§ No código acima deve-se tratar a imagem completa, salvando-a
    //§ Perceba que o requestCode (1) deve ser igual ao que foi passado no startActivityForResult
    //(1)

}