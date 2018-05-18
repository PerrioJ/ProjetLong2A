package perriobarreteau.apprentissagemusique;

import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.Random;

public class LectureDeNote_Note extends AppCompatActivity implements View.OnClickListener {

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
    Random random = new Random();
    int idQuestion = random.nextInt(12); // Choix aléatoire de la note à deviner
    int nbFautes = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lecture_de_note__note);

        // Affichage de la note à deviner
        ImageView imageViewNote = (ImageView) findViewById(R.id.imageViewNote);
        imageViewNote.setImageResource(notes[idQuestion].image);

        Button buttonRecord = (Button) findViewById(R.id.buttonRecord);
        buttonRecord.setOnClickListener(this);

    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public void onClick(View view) {

        // Récupération de la réponse
        float[] signal = Note.enregistrement();
        int idReponse = Note.reconnaissanceDeNote(signal,8000);

        // Traitement de la réponse
        // Cas : Bonne réponse
        if (idReponse == idQuestion) {
            Toast.makeText(getApplicationContext(), "Bravo !", Toast.LENGTH_SHORT).show();
            // On choisi aléatoirement une nouvelle note à deviner
            idQuestion = newIdQuestion(idQuestion);
            // On met à jour l'affichage
            ImageView imageViewNote = (ImageView) findViewById(R.id.imageViewNote);
            imageViewNote.setImageResource(notes[idQuestion].image);
            this.nbFautes = 0;
        }
        // Cas : Mauvaise réponse
        else {
            this.nbFautes ++;

            if (nbFautes == 1) {
                Toast.makeText(getApplicationContext(), "C'est faux... rééssayez", Toast.LENGTH_SHORT).show();
            }
            else {
                Toast.makeText(getApplicationContext(), "C'est faux... la bonne réponse était : " + notes[idQuestion].nom, Toast.LENGTH_SHORT).show();
                // On choisi aléatoirement une nouvelle note à deviner
                idQuestion = newIdQuestion(idQuestion);
                // On met à jour l'affichage
                ImageView imageViewNote = (ImageView) findViewById(R.id.imageViewNote);
                imageViewNote.setImageResource(notes[idQuestion].image);
                this.nbFautes = 0;
            }
        }

    }

    public int newIdQuestion(int idQuestion) {
        int idQuestionNew = random.nextInt(12);
        while (idQuestionNew == this.idQuestion) {
            idQuestionNew = random.nextInt(12);
        }
        return(idQuestionNew);

    }

}
