package perriobarreteau.apprentissagemusique;

import android.media.MediaPlayer;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Random;

public class DicteeDeNote_Speech extends AppCompatActivity {


    // Il faut ajouter les images et les sons des notes #
    Note Do = new Note(0, "Do", R.drawable.ic_note_do, R.raw.aaa);
    Note Re = new Note(2, "Ré", R.drawable.ic_note_re, R.raw.re);
    Note Mi = new Note(4, "Mi", R.drawable.ic_note_mi, R.raw.mi);
    Note Fa = new Note(5, "Fa", R.drawable.ic_note_fa, R.raw.fa);
    Note Sol = new Note(7, "Sol", R.drawable.ic_note_sol, R.raw.sol);
    Note La = new Note(9, "La", R.drawable.ic_note_la, R.raw.la);
    Note Si = new Note(11, "Si", R.drawable.ic_note_si, R.raw.si);
    Note[] notes = {Do, Re, Mi, Fa, Sol, La, Si};
    MediaPlayer mediaplayer;
    Random random = new Random();
    int idQuestion = random.nextInt(7); // Choix aléatoire de la note à deviner
    int nbEcoutes = 0;
    int nbFautes = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dictee_de_note__speech);

        // Gestion du lecteur audio
        ImageButton imageButtonNote = (ImageButton) findViewById(R.id.imageButtonNote);
        imageButtonNote.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                nbEcoutes ++;
                if (nbEcoutes <= 5) {
                    mediaplayer = MediaPlayer.create(getApplicationContext(), notes[idQuestion].son);
                    mediaplayer.start();
                }
                else {
                    Toast.makeText(getApplicationContext(), "Proposez une réponse", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public void onClick(View view) {

        mediaplayer.release();
        nbEcoutes = 0;

        // Récupération de la réponse
        float[] signal = Note.enregistrement();
        float[][] mfcc = Speech.MFCC(signal, 8000);
        int idReponse = Speech.Resultat(mfcc,getApplicationContext());

        // Affichage de la réponse
        TextView textViewReponse = (TextView) findViewById(R.id.textViewReponse);
        textViewReponse.setText(notes[idReponse].nom);

        // Traitement de la réponse
        // Cas : Bonne réponse
        if (idReponse == idQuestion) {
            Toast.makeText(getApplicationContext(), "Bravo !", Toast.LENGTH_SHORT).show();
            // On choisi aléatoirement une nouvelle note à deviner
            idQuestion = newIdQuestion(idQuestion);
            this.nbFautes = 0;
        }
        // Cas : Mauvaise réponse
        else {
            this.nbFautes ++;

            if (nbFautes == 1) {
                Toast.makeText(getApplicationContext(), "C'est faux... rééssayez", Toast.LENGTH_SHORT).show();
            }
            else {
                Toast.makeText(getApplicationContext(), "C'est faux... la bonne réponse était : " + notes[this.idQuestion -1].nom, Toast.LENGTH_SHORT).show();
                idQuestion =  newIdQuestion(idQuestion);
                this.nbFautes = 0;
            }
        }

    }

    public int newIdQuestion(int idQuestion) {
        int idQuestionNew = random.nextInt(7);
        while (idQuestionNew == this.idQuestion) {
            idQuestionNew = random.nextInt(7);
        }
        return(idQuestionNew);

    }


}
