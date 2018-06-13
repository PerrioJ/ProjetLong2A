package perriobarreteau.apprentissagemusique;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Color;
import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.w3c.dom.Text;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;

import static java.lang.Math.PI;
import static java.lang.Math.cos;
import static java.lang.Math.log10;
import static java.lang.Math.min;
import static java.lang.Math.pow;
import static java.lang.Math.sqrt;

public class DicteeVocale extends AppCompatActivity {

    private TextView textViewResultat;
    private Button buttonEnregistrement;

    Note Do = new Note(0, "Do", R.drawable.ic_note_do, R.raw.aaa);
    Note Dod = new Note(1, "Do#", R.drawable.ic_note_do, R.raw.aaa);
    Note Re = new Note(2, "Ré", R.drawable.ic_note_re, R.raw.re);
    Note Red = new Note(3, "Ré#", R.drawable.ic_note_re, R.raw.re);
    Note Mi = new Note(4, "Mi", R.drawable.ic_note_mi, R.raw.mi);
    Note Fa = new Note(5, "Fa", R.drawable.ic_note_fa, R.raw.fa);
    Note Fad = new Note(6, "Fa#", R.drawable.ic_note_fa, R.raw.fa);
    Note Sol = new Note(7, "Sol", R.drawable.ic_note_sol, R.raw.sol);
    Note Sold = new Note(8, "Sol#", R.drawable.ic_note_sol, R.raw.sol);
    Note La = new Note(9, "La", R.drawable.ic_note_la, R.raw.la);
    Note Lad = new Note(10, "La#", R.drawable.ic_note_la, R.raw.la);
    Note Si = new Note(11, "Si", R.drawable.ic_note_si, R.raw.si);
    Note[] notes = {Do, Dod, Re, Red, Mi, Fa, Fad, Sol, Sold, La, Lad, Si};
    int n = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dictee_vocale);

        textViewResultat = (TextView) findViewById(R.id.textViewResultat);
        buttonEnregistrement = (Button) findViewById(R.id.buttonEnregistrement);

        buttonEnregistrement.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View view) {

                Traitement traitement = new Traitement();
                traitement.execute(getApplicationContext());

            }
        });

    }

    public class Traitement extends AsyncTask<Context, Void, Integer> {

        @RequiresApi(api = Build.VERSION_CODES.M)
        @Override
        protected Integer doInBackground(Context... contexts) {

            System.out.println("Start");
            float[] signal = perriobarreteau.apprentissagemusique.Speech.enregistrement();
            System.out.println("Stop");

            float[][] mfcc = perriobarreteau.apprentissagemusique.Speech.MFCC(signal, 8000);

            Gson gson = new Gson();
            SharedPreferences SP = getSharedPreferences("Do2",0);
            SharedPreferences.Editor editor = SP.edit();
            editor.putString(String.valueOf(n),gson.toJson(mfcc));
            editor.commit();
            n++;

            int resultat = perriobarreteau.apprentissagemusique.Speech.Resultat(mfcc, contexts[0]);
            System.out.println(resultat);

            return(resultat);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            buttonEnregistrement.setEnabled(false);

        }

        @Override
        protected void onPostExecute(Integer integer) {
            super.onPostExecute(integer);

            textViewResultat.setText("Mot : "+notes[integer].nom);
            buttonEnregistrement.setEnabled(true);

        }
    }

}