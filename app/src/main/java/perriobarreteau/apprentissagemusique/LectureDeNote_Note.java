package perriobarreteau.apprentissagemusique;

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

import org.w3c.dom.Text;

import java.util.Random;

public class LectureDeNote_Note extends AppCompatActivity implements View.OnClickListener {

    TextView textViewScore;
    ImageView imageViewNote;
    Button buttonRecord;

    Note Do = new Note(0, "Do", R.drawable.ic_note_do, R.raw.aaa);
    Note Dod = new Note(1, "Do#", R.drawable.ic_note_do, R.raw.aaad);
    Note Re = new Note(2, "Ré", R.drawable.ic_note_re, R.raw.re);
    Note Red = new Note(3, "Ré#", R.drawable.ic_note_re, R.raw.red);
    Note Mi = new Note(4, "Mi", R.drawable.ic_note_mi, R.raw.mi);
    Note Fa = new Note(5, "Fa", R.drawable.ic_note_fa, R.raw.fa);
    Note Fad = new Note(6, "Fa#", R.drawable.ic_note_fa, R.raw.fad);
    Note Sol = new Note(7, "Sol", R.drawable.ic_note_sol, R.raw.sol);
    Note Sold = new Note(8, "Sol#", R.drawable.ic_note_sol, R.raw.sold);
    Note La = new Note(9, "La", R.drawable.ic_note_la, R.raw.la);
    Note Lad = new Note(10, "La#", R.drawable.ic_note_la, R.raw.lad);
    Note Si = new Note(11, "Si", R.drawable.ic_note_si, R.raw.si);
    Note[] notes = {Do, Dod, Re, Red, Mi, Fa, Fad, Sol, Sold, La, Lad, Si};
    Note[] notesSansDieses = {Do, Re, Mi, Fa, Sol, La, Si};
    int[] sansDiese = {0, 0, 1, 1, 2, 3, 3, 4, 4, 5, 5, 6};
    Random random = new Random();
    int idQuestion = random.nextInt(7); // Choix aléatoire de la note à deviner
    int score = 0;
    int nbQuestions = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lecture_de_note__note);

        textViewScore = (TextView) findViewById(R.id.textViewScore);

        System.out.println(notesSansDieses[idQuestion].nom);

        // Affichage de la note à deviner
        imageViewNote = (ImageView) findViewById(R.id.imageViewNote);
        imageViewNote.setImageResource(notesSansDieses[idQuestion].image);

        buttonRecord = (Button) findViewById(R.id.buttonRecord);
        buttonRecord.setOnClickListener(this);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public void onClick(View view) {

        Traitement traitement = new Traitement();
        traitement.execute();

    }

    public int newIdQuestion(int idQuestion) {
        int idQuestionNew = random.nextInt(7);
        while (idQuestionNew == this.idQuestion) {
            idQuestionNew = random.nextInt(7);
        }
        System.out.println(notesSansDieses[idQuestionNew].nom);
        return(idQuestionNew);

    }

    private class Traitement extends AsyncTask<Void, Void, Integer> {


        @RequiresApi(api = Build.VERSION_CODES.M)
        @Override
        protected Integer doInBackground(Void... voids) {

            float[] signal = Note.enregistrement();

            int resultat = Note.reconnaissanceDeNote(signal, 8000);

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

            int idReponse = sansDiese[integer];

            buttonRecord.setText(notes[integer].nom);
            // Cas bonne réponse
            if (idQuestion == idReponse) {
                buttonRecord.getBackground().setColorFilter(Color.GREEN, PorterDuff.Mode.MULTIPLY);
                score++;
            }
            // Cas mauvaise réponse
            else{
                buttonRecord.getBackground().setColorFilter(Color.RED, PorterDuff.Mode.MULTIPLY);
                Toast.makeText(getApplicationContext(), "C'était un : "+notesSansDieses[idQuestion].nom, Toast.LENGTH_LONG).show();
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
                    imageViewNote.setImageResource(notesSansDieses[idQuestion].image);
                    // On remet le style d'origine du bouton
                    buttonRecord.setText("Appuyez et jouez la note");
                    buttonRecord.getBackground().clearColorFilter();
                }
            }, 1000);

            buttonRecord.setEnabled(true);

        }
    }

}
