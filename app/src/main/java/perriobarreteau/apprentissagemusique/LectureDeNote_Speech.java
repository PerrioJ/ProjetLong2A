package perriobarreteau.apprentissagemusique;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Handler;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Random;

public class LectureDeNote_Speech extends AppCompatActivity implements View.OnClickListener {

    TextView textViewScore;
    ImageView imageViewNote;
    Button buttonRecord;

    Note Do = new Note(0, "Do", R.drawable.ic_note_do, R.raw.aaa);
    Note Re = new Note(2, "Ré", R.drawable.ic_note_re, R.raw.re);
    Note Mi = new Note(4, "Mi", R.drawable.ic_note_mi, R.raw.mi);
    Note Fa = new Note(5, "Fa", R.drawable.ic_note_fa, R.raw.fa);
    Note Sol = new Note(7, "Sol", R.drawable.ic_note_sol, R.raw.sol);
    Note La = new Note(9, "La", R.drawable.ic_note_la, R.raw.la);
    Note Si = new Note(11, "Si", R.drawable.ic_note_si, R.raw.si);
    Note[] notes = {Do, Re, Mi, Fa, Sol, La, Si};
    Random random = new Random();
    int idQuestion = random.nextInt(7); // Choix aléatoire de la note à deviner
    int score = 0;
    int nbQuestions = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lecture_de_note__speech);

        System.out.println(notes[idQuestion].nom);

        textViewScore = (TextView) findViewById(R.id.textViewScore);

        // Affichage de la note à deviner
        imageViewNote = (ImageView) findViewById(R.id.imageViewNote);
        imageViewNote.setImageResource(notes[idQuestion].image);

        buttonRecord = (Button) findViewById(R.id.buttonRecord);
        buttonRecord.setOnClickListener(this);

    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onClick(View view) {

        Traitement traitement = new Traitement();
        traitement.execute(getApplicationContext());

    }

    public int newIdQuestion(int idQuestion) {
        int idQuestionNew = random.nextInt(7); // Choix aléatoire de la note à deviner
        while (idQuestionNew == this.idQuestion) {
            idQuestionNew = random.nextInt(7);
        }
        System.out.println(notes[idQuestionNew].nom);
        return(idQuestionNew);

    }

    public class Traitement extends AsyncTask<Context, Void, Integer> {

        @RequiresApi(api = Build.VERSION_CODES.M)
        @Override
        protected Integer doInBackground(Context... contexts) {

            System.out.println("Start");
            float[] signal = perriobarreteau.apprentissagemusique.Speech.enregistrement();
            System.out.println("Stop");

            float[][] mfcc = perriobarreteau.apprentissagemusique.Speech.MFCC(signal,8000);

            int resultat = perriobarreteau.apprentissagemusique.Speech.Resultat(mfcc, contexts[0]);
            System.out.println(resultat);

            return(resultat);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            buttonRecord.setEnabled(false);
            buttonRecord.setText("...");

        }

        @Override
        protected void onPostExecute(Integer integer) {
            super.onPostExecute(integer);

            buttonRecord.setText(notes[integer].nom);

            if (idQuestion == integer) {
                score++;
                buttonRecord.getBackground().setColorFilter(Color.GREEN, PorterDuff.Mode.MULTIPLY);
            }
            else {
                buttonRecord.getBackground().setColorFilter(Color.RED, PorterDuff.Mode.MULTIPLY);
                Toast.makeText(getApplicationContext(), "C'était un : "+notes[idQuestion].nom, Toast.LENGTH_LONG).show();
            }

            nbQuestions++;
            textViewScore.setText("Score : "+score+"/"+nbQuestions);

            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                public void run() {
                    // On choisi aléatoirement une nouvelle note à deviner
                    idQuestion = newIdQuestion(idQuestion);
                    // On met à jour l'affichage
                    ImageView imageViewNote = (ImageView) findViewById(R.id.imageViewNote);
                    imageViewNote.setImageResource(notes[idQuestion].image);
                    // On remet le style d'origine du bouton
                    buttonRecord.setText("Appuyez et jouez la note");
                    buttonRecord.getBackground().clearColorFilter();
                }
            }, 1000);

            buttonRecord.setEnabled(true);

        }
    }

}