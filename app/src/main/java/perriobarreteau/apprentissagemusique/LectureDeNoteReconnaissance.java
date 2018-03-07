package perriobarreteau.apprentissagemusique;

import android.graphics.Color;
import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Random;

public class LectureDeNoteReconnaissance extends AppCompatActivity {

    Note Do = new Note(1, "Do", R.drawable.ic_note_do, R.raw.aaa);
    Note Re = new Note(2, "Ré", R.drawable.ic_note_re, R.raw.re);
    Note Mi = new Note(3, "Mi", R.drawable.ic_note_mi, R.raw.mi);
    Note Fa = new Note(4, "Fa", R.drawable.ic_note_fa, R.raw.fa);
    Note Sol = new Note(5, "Sol", R.drawable.ic_note_sol, R.raw.sol);
    Note La = new Note(6, "La", R.drawable.ic_note_la, R.raw.la);
    Note Si = new Note(7, "Si", R.drawable.ic_note_si, R.raw.si);
    Note[] notes = {Do, Re, Mi, Fa, Sol, La, Si};
    int idRandom = 0; // Choix aléatoire de la note à deviner
    int nbFautes = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lecture_de_note_reconnaissance);

        // Affichage de la note à deviner
        ImageView imageViewNote = (ImageView) findViewById(R.id.imageViewNote);
        Random rand = new Random();
        this.idRandom = rand.nextInt(7) + 1;
        imageViewNote.setImageResource(notes[idRandom-1].image);

        Button buttonRecord = (Button) findViewById(R.id.buttonRecord);
        buttonRecord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                short[] signal = Note.enregistrement();

                int resultat = Note.reconnaissanceDeNote(signal, 8000);

                TextView textViewNote = (TextView) findViewById(R.id.textViewNote);
                textViewNote.setText(notes[resultat-1].nom);

                if (resultat == idRandom) {
                    textViewNote.setTextColor(Color.GREEN);
                }
                else {
                    textViewNote.setTextColor(Color.RED);
                }

            }
        });

    }

}
