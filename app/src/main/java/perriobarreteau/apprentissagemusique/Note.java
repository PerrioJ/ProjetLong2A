package perriobarreteau.apprentissagemusique;

import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import java.util.Arrays;

public class Note {

    public int id;
    public String nom;
    public int image;
    public int son;

    public Note(int id, String nom, int image, int son) {

        this.id = id;
        this.nom = nom;
        this.image = image;
        this.son  = son;

    }

    public static short[] enregistrement() {

        // Paramètres d'enregistrement
        int source = MediaRecorder.AudioSource.DEFAULT;
        int sampleRate = 8000;
        int channel = AudioFormat.CHANNEL_IN_MONO;
        int encoding = AudioFormat.ENCODING_PCM_16BIT;
        int taille = 8192;

        AudioRecord audioRecord = new AudioRecord(source,sampleRate,channel,encoding,2*taille);

        audioRecord.startRecording();

        short[] signal = new short[taille];

        long depart = System.currentTimeMillis();

        while((System.currentTimeMillis()-depart < 2000)) {
            audioRecord.read(signal,0,taille);
        }

        audioRecord.stop();
        audioRecord.release();

        return(signal);
    }

    public static int reconnaissanceDeNote(short[] signal, int Fe) {

        int Ny = signal.length;

        Complex[] Y = FFT.fft(signal);
        double[] Yabs2side = Complex.abs(Y);
        double[] Yabs1side = Arrays.copyOfRange(Yabs2side,0,Ny/2);

        int indiceMax = Note.getMax(Yabs1side);
        int F = indiceMax*Fe/Ny;
        System.out.println("Fréquence : "+F);

        float Fn = (float) (F/440.0);
        int MIDI = (int) (69.0+12*Math.log(F/440.0)/Math.log(2.0) + 0.5);
        int numero = MIDI%12;

        return(numero);

    }

    public static int getMax(double[] y) {
        int indiceMax = 0;
        double valeurMax = 0;
        for (int k=1; k<y.length; k++) {
            if (y[k]>valeurMax) {
                indiceMax = k;
                valeurMax = y[k];
            }
        }
        return(indiceMax);
    }

}
