package perriobarreteau.apprentissagemusique;

import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.Random;

public class DicteeDeNote extends AppCompatActivity implements View.OnClickListener {

    Note Do = new Note(1, "Do", R.drawable.ic_note_do, R.raw.aaa);
    Note Re = new Note(2, "Ré", R.drawable.ic_note_re, R.raw.re);
    Note Mi = new Note(3, "Mi", R.drawable.ic_note_mi, R.raw.mi);
    Note Fa = new Note(4, "Fa", R.drawable.ic_note_fa, R.raw.fa);
    Note Sol = new Note(5, "Sol", R.drawable.ic_note_sol, R.raw.sol);
    Note La = new Note(6, "La", R.drawable.ic_note_la, R.raw.la);
    Note Si = new Note(7, "Si", R.drawable.ic_note_si, R.raw.si);
    Note[] notes = {Do, Re, Mi, Fa, Sol, La, Si};
    MediaPlayer mediaplayer;
    int idRandom = 0; // Choix aléatoire de la note à deviner
    int nbEcoutes = 0;
    int nbFautes = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dictee_de_note);

        // Sélection de la note à deviner
        Random rand = new Random();
        this.idRandom = rand.nextInt(7) + 1;

        ImageButton imageButtonNote = (ImageButton) findViewById(R.id.imageButtonNote);
        imageButtonNote.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                nbEcoutes ++;
                if (nbEcoutes <= 5) {
                    mediaplayer = MediaPlayer.create(getApplicationContext(), notes[idRandom-1].son);
                    mediaplayer.start();
                }
                else {
                    Toast.makeText(getApplicationContext(), "Proposez une réponse", Toast.LENGTH_SHORT).show();
                }
            }
        });

        Button buttonDo = (Button) findViewById(R.id.buttonDo);
        buttonDo.setTag(Integer.valueOf(1));
        buttonDo.setOnClickListener(this);
        Button buttonRe = (Button) findViewById(R.id.buttonRe);
        buttonRe.setTag(Integer.valueOf(2));
        buttonRe.setOnClickListener(this);
        Button buttonMi = (Button) findViewById(R.id.buttonMi);
        buttonMi.setTag(Integer.valueOf(3));
        buttonMi.setOnClickListener(this);
        Button buttonFa = (Button) findViewById(R.id.buttonFa);
        buttonFa.setTag(Integer.valueOf(4));
        buttonFa.setOnClickListener(this);
        Button buttonSol = (Button) findViewById(R.id.buttonSol);
        buttonSol.setTag(Integer.valueOf(5));
        buttonSol.setOnClickListener(this);
        Button buttonLa = (Button) findViewById(R.id.buttonLa);
        buttonLa.setTag(Integer.valueOf(6));
        buttonLa.setOnClickListener(this);
        Button buttonSi = (Button) findViewById(R.id.buttonSi);
        buttonSi.setTag(Integer.valueOf(7));
        buttonSi.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {

        mediaplayer.release();

        this.nbEcoutes = 0;
        int idChoisi = (int) view.getTag();

        if (idChoisi == this.idRandom) {
            Toast.makeText(getApplicationContext(), "Bravo !", Toast.LENGTH_SHORT).show();
            // On choisi aléatoirement une nouvelle note à deviner et met à jour l'affichage
            Random rand = new Random();
            int idRandomNew = rand.nextInt(7) + 1;
            while (idRandomNew ==this.idRandom) {
                idRandomNew = rand.nextInt(7) + 1;
            }
            this.idRandom = idRandomNew;
            this.nbFautes = 0;
        }

        else {
            this.nbFautes ++;

            if (nbFautes == 1) {
                Toast.makeText(getApplicationContext(), "C'est faux... rééssayez", Toast.LENGTH_SHORT).show();
            }
            else {
                Toast.makeText(getApplicationContext(), "C'est faux... la bonne réponse était : " + notes[this.idRandom-1].nom, Toast.LENGTH_SHORT).show();
                Random rand = new Random();
                int idRandomNew = rand.nextInt(7) + 1;
                while (idRandomNew ==this.idRandom) {
                    idRandomNew = rand.nextInt(7) + 1;
                }
                this.idRandom = idRandomNew;
                this.nbFautes = 0;
            }
        }

    }

}