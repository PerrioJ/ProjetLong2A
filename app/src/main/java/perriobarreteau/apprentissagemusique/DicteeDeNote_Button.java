package perriobarreteau.apprentissagemusique;

import android.graphics.Color;
import android.graphics.PorterDuff;
import android.media.MediaPlayer;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Random;

public class DicteeDeNote_Button extends AppCompatActivity implements View.OnClickListener {

    TextView textViewScore;
    ImageButton imageButtonNote;
    Button buttonDo;
    Button buttonDod;
    Button buttonRe;
    Button buttonRed;
    Button buttonMi;
    Button buttonFa;
    Button buttonFad;
    Button buttonSol;
    Button buttonSold;
    Button buttonLa;
    Button buttonLad;
    Button buttonSi;

    // Il faut ajouter les images et les sons des notes #
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
    MediaPlayer mediaplayer;
    Random random = new Random();
    int idQuestion = random.nextInt(12); // Choix aléatoire de la note à deviner
    int score = 0;
    int nbQuestions = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dictee_de_note_button);

        textViewScore = (TextView) findViewById(R.id.textViewScore);

        System.out.println(notes[idQuestion].nom);

        // Gestion du lecteur audio
        imageButtonNote = (ImageButton) findViewById(R.id.imageButtonNote);
        imageButtonNote.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                imageButtonNote.setEnabled(false);
                mediaplayer = MediaPlayer.create(getApplicationContext(), notes[idQuestion].son);
                mediaplayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mediaPlayer) {
                        mediaPlayer.release();
                        imageButtonNote.setEnabled(true);
                    }
                });
                mediaplayer.start();
            }
        });

        // Boutons de réponse
        buttonDo = (Button) findViewById(R.id.buttonDo);
        buttonDo.setTag(Integer.valueOf(0));
        buttonDo.setOnClickListener(this);
        buttonDod = (Button) findViewById(R.id.buttonDod);
        buttonDod.setTag(Integer.valueOf(1));
        buttonDod.setOnClickListener(this);
        buttonRe = (Button) findViewById(R.id.buttonRe);
        buttonRe.setTag(Integer.valueOf(2));
        buttonRe.setOnClickListener(this);
        buttonRed = (Button) findViewById(R.id.buttonRed);
        buttonRed.setTag(Integer.valueOf(3));
        buttonRed.setOnClickListener(this);
        buttonMi = (Button) findViewById(R.id.buttonMi);
        buttonMi.setTag(Integer.valueOf(4));
        buttonMi.setOnClickListener(this);
        buttonFa = (Button) findViewById(R.id.buttonFa);
        buttonFa.setTag(Integer.valueOf(5));
        buttonFa.setOnClickListener(this);
        buttonFad = (Button) findViewById(R.id.buttonFad);
        buttonFad.setTag(Integer.valueOf(6));
        buttonFad.setOnClickListener(this);
        buttonSol = (Button) findViewById(R.id.buttonSol);
        buttonSol.setTag(Integer.valueOf(7));
        buttonSol.setOnClickListener(this);
        buttonSold = (Button) findViewById(R.id.buttonSold);
        buttonSold.setTag(Integer.valueOf(8));
        buttonSold.setOnClickListener(this);
        buttonLa = (Button) findViewById(R.id.buttonLa);
        buttonLa.setTag(Integer.valueOf(9));
        buttonLa.setOnClickListener(this);
        buttonLad = (Button) findViewById(R.id.buttonLad);
        buttonLad.setTag(Integer.valueOf(10));
        buttonLad.setOnClickListener(this);
        buttonSi = (Button) findViewById(R.id.buttonSi);
        buttonSi.setTag(Integer.valueOf(11));
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
                // On remet la couleur d'origine des boutons
                buttonDo.getBackground().clearColorFilter();
                buttonDod.getBackground().clearColorFilter();
                buttonRe.getBackground().clearColorFilter();
                buttonRed.getBackground().clearColorFilter();
                buttonMi.getBackground().clearColorFilter();
                buttonFa.getBackground().clearColorFilter();
                buttonFad.getBackground().clearColorFilter();
                buttonSol.getBackground().clearColorFilter();
                buttonSold.getBackground().clearColorFilter();
                buttonLa.getBackground().clearColorFilter();
                buttonLad.getBackground().clearColorFilter();
                buttonSi.getBackground().clearColorFilter();

            }
        }, 1000);

    }

    public int newIdQuestion(int idQuestion) {
        int idQuestionNew = random.nextInt(12);
        while (idQuestionNew == this.idQuestion) {
            idQuestionNew = random.nextInt(12);
        }
        System.out.println(notes[idQuestionNew].nom);
        return(idQuestionNew);
    }

}