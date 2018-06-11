package perriobarreteau.apprentissagemusique;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.Random;

public class LectureDeNote_Button extends AppCompatActivity implements View.OnClickListener {

    TextView textViewScore;
    ImageView imageViewNote;
    Button buttonDo;
    Button buttonRe;
    Button buttonMi;
    Button buttonFa;
    Button buttonSol;
    Button buttonLa;
    Button buttonSi;

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
        setContentView(R.layout.activity_lecture_de_note_button);

        textViewScore = (TextView) findViewById(R.id.textViewScore);

        // Affichage de la note à deviner
        imageViewNote = (ImageView) findViewById(R.id.imageViewNote);
        imageViewNote.setImageResource(notes[idQuestion].image);
        System.out.println(notes[idQuestion].nom);

        // Boutons de réponse
        buttonDo = (Button) findViewById(R.id.buttonDo);
        buttonDo.setTag(Integer.valueOf(0));
        buttonDo.setOnClickListener(this);
        buttonRe = (Button) findViewById(R.id.buttonRe);
        buttonRe.setTag(Integer.valueOf(1));
        buttonRe.setOnClickListener(this);
        buttonMi = (Button) findViewById(R.id.buttonMi);
        buttonMi.setTag(Integer.valueOf(2));
        buttonMi.setOnClickListener(this);
        buttonFa = (Button) findViewById(R.id.buttonFa);
        buttonFa.setTag(Integer.valueOf(3));
        buttonFa.setOnClickListener(this);
        buttonSol = (Button) findViewById(R.id.buttonSol);
        buttonSol.setTag(Integer.valueOf(4));
        buttonSol.setOnClickListener(this);
        buttonLa = (Button) findViewById(R.id.buttonLa);
        buttonLa.setTag(Integer.valueOf(5));
        buttonLa.setOnClickListener(this);
        buttonSi = (Button) findViewById(R.id.buttonSi);
        buttonSi.setTag(Integer.valueOf(6));
        buttonSi.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {

        // Récupération de la réponse
        int idReponse = (int) view.getTag();

        // Traitement de la réponse
            // Cas : Bonne réponse
        if (idReponse == idQuestion) {
            view.getBackground().setColorFilter(Color.GREEN, PorterDuff.Mode.MULTIPLY);
            score++;
        }
            // Cas : Mauvaise réponse
        else {
            view.getBackground().setColorFilter(Color.RED, PorterDuff.Mode.MULTIPLY);
            Toast.makeText(this, "C'était un : "+notes[idQuestion].nom, Toast.LENGTH_LONG).show();
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
                // On remet la couleur d'origine des boutons
                buttonDo.getBackground().clearColorFilter();
                buttonRe.getBackground().clearColorFilter();
                buttonMi.getBackground().clearColorFilter();
                buttonFa.getBackground().clearColorFilter();
                buttonSol.getBackground().clearColorFilter();
                buttonLa.getBackground().clearColorFilter();
                buttonSi.getBackground().clearColorFilter();

            }
        }, 1000);

    }

    public int newIdQuestion(int idQuestion) {
        int idQuestionNew = random.nextInt(7);
        while (idQuestionNew == this.idQuestion) {
            idQuestionNew = random.nextInt(7);
        }
        System.out.println(notes[idQuestionNew].nom);
        return(idQuestionNew);
    }

}