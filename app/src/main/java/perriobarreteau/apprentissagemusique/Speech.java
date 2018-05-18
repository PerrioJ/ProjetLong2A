package perriobarreteau.apprentissagemusique;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.Arrays;
import java.util.Map;

import static java.lang.Math.PI;
import static java.lang.Math.cos;
import static java.lang.Math.log10;
import static java.lang.Math.min;
import static java.lang.Math.pow;
import static java.lang.Math.sqrt;

public class Speech {

    @RequiresApi(api = Build.VERSION_CODES.M)
    public static float[] enregistrement() {

        // Paramètres d'enregistrement
        int source = MediaRecorder.AudioSource.DEFAULT;
        int sampleRate = 8000;
        int channel = AudioFormat.CHANNEL_IN_MONO;
        int encoding = AudioFormat.ENCODING_PCM_FLOAT;
        int taille = 8192;

        AudioRecord audioRecord = new AudioRecord(source,sampleRate,channel,encoding,2*taille);

        audioRecord.startRecording();

        float[] signal = new float[taille];

        long depart = System.currentTimeMillis();

        int compteur = 0;
        while((compteur < taille)) {
            compteur += audioRecord.read(signal,0,taille,AudioRecord.READ_BLOCKING);
        }

        audioRecord.stop();
        audioRecord.release();

        return(signal);
    }

    public static float[][] MFCC(float[] signal, int Fe) {

        // Paramètres
        float alpha = (float) 0.97;
        int length_signal = signal.length;
        int length_frame = (int) (Fe*0.032);
        int step = (int) (Fe*0.016);
        int nb_filters = 26;
        int nb_coeffs = 12;
        int M = 3;

        // Ressources
        float[] window = hamming(length_frame);
        float[][] Filters = MelFilters(Fe, nb_filters, length_frame);

        //// Algorithme

        // Pré-emphasis
        float[] signalB = new float[length_signal];
        signalB[0] = signal[0];
        for (int k = 1; k<length_signal; k++) {
            signalB[k] = (signal[k]-alpha*signal[k-1]);
        }

        float[][] mfcc = new float[(length_signal - length_frame) / step][nb_coeffs];

        int l = 0;
        for (int N = 0; N < (length_signal-length_frame); N += step) {

            // Framing
            float[] frame = new float[length_frame];
            for (int k = 0; k < length_frame; k++) {
                frame[k] = signalB[k + N];
            }

            // Fenêtrage
            float[] frame_window = new float[length_frame];
            for (int k = 0; k<length_frame; k++) {
                frame_window[k] = frame[k]*window[k];
            }

            // FFT
            Complex[] specter = FFT.fft(frame_window);
            double[] specterAbs;
            specterAbs = Complex.abs(specter);

            // Filtrage Mel
            float[] energies = new float[nb_filters];
            for (int n = 0; n<nb_filters; n++) {
                for (int k = 0; k<(length_frame/2+1); k++) {
                    energies[n] += Filters[n][k]*specterAbs[k];
                }
            }

            // Log
            float[] logspecter = new float[nb_filters];
            for (int k = 0; k<nb_filters; k++) {
                logspecter[k] = (float) log10(energies[k]+0.000001);
            }

            // iDCT
            float[] coefficients = IDCT(logspecter, nb_coeffs);

            // Sauvegarde du résultat
            mfcc[l] = coefficients;
            l++;

        }

        return(mfcc);

    }

    private static float[] IDCT(float[] X, int nb_coeffs) {

        int N = X.length;

        float[] x = new float[nb_coeffs];

        for (int n = 0; n<nb_coeffs; n++) {
            for (int k = 0; k<N; k++) {
                double w;
                if (k == 0) {
                    w = sqrt(1.0/N);
                }
                else {
                    w = sqrt(2.0/N);
                }
                x[n] += w * X[k] * cos(PI * (2 * n - 1) * k / (2 * N));
            }
        }

        return(x);

    }

    private static float[] hamming(int length) {

        float[] window = new float[length];
        for (int k = 0; k<length; k++) {
            window[k] = (float) (0.54 - 0.46*cos(2*PI*k/(length-1)));
        }
        return(window);
    }

    public static float[][] MelFilters(int Fe, int nb_filters, int length_frame) {

        float fmin = 0;
        float fmax = Fe/2; // Critère de Shannon

        float Fmin = HzToMel(fmin);
        float Fmax = HzToMel(fmax);
        float W = 2*(Fmax-Fmin)/(nb_filters+1);

        float[][] Filters = new float[nb_filters][length_frame/2+1];

        for (int n = 1; n <= nb_filters; n++) {
            float Fleft = Fmin + (n-1)*W/2;
            float Fcenter = Fmin + n*W/2;
            float Fright = Fmin + (n+1)*W/2;

            float fleft = MelToHz(Fleft);
            float fcenter = MelToHz(Fcenter);
            float fright = MelToHz(Fright);

            for (int k = 0; k<(length_frame/2+1); k++) {
                double f = Fe * k / length_frame;

                if (f>fleft && f<fcenter) {
                    Filters[n-1][k] = (float) ((f-fleft)/(fcenter-fleft));
                }
                else if (f<fright & f>fcenter) {
                    Filters[n-1][k] = (float) ((fright-f)/(fright-fcenter));
                }
                else {
                    Filters[n-1][k] = 0;
                }

            }
        }

        return(Filters);

    }

    private static float MelToHz(float F) {
        return (float) (700*(pow(10,F/2595.0)-1));
    }

    private static float HzToMel(float f) {
        return (float) (2595*log10(1+f/700));
    }

    public static float DTW(float[][] X, float[][] Y) {

        int ax = X.length;
        int bx = X[0].length;
        int ay = Y.length;
        int by = Y[0].length;

        // Matrice des distances
        float[][] d = new float[ax][ay];
        for (int i = 0; i<ax; i++) {
            for (int j = 0; j<ay; j++) {
                for (int k = 0; k<bx; k++) {
                    d[i][j] += (X[i][k]-Y[j][k])*(X[i][k]-Y[j][k]);
                }
                d[i][j] = (float) sqrt(d[i][j]);
            }
        }

        // Meilleure distance globale
        float[][] D = new float[ax][ay];
        for (int i = 0; i<ax; i++) {
            for (int j = 0; j<ay; j++) {
                if (i==0 && j==0) {
                    D[i][j] = d[i][j];
                }
                else if (i==0) {
                    D[i][j] = (d[i][j] + D[i][j-1]);
                }
                else if (j==0) {
                    D[i][j] = (d[i][j] + D[i-1][j]);
                }
                else {
                    D[i][j] = (d[i][j] + min(min(D[i-1][j],D[i][j-1]),D[i-1][j-1]));
                }
            }
        }

        return(D[ax-1][ay-1]);

    }

    public static int Resultat(float[][] mfcc, Context applicationContext) {

        Gson gson = new Gson();

        DBManager db = new DBManager(applicationContext);
        db.open();

        int[] classes = new int[35];
        float[] distances = new float[35];

        int n = 0;
        float[][] mfccRef;
        Cursor cursor = db.getAll();
        if (cursor.moveToFirst())
        {
            do {
                classes[n] = cursor.getInt(cursor.getColumnIndex(DBManager.KEY_classe));
                String json = cursor.getString(cursor.getColumnIndex(DBManager.KEY_mfcc));
                mfccRef = gson.fromJson(json,new TypeToken<float[][]>() {}.getType());
                distances[n] = DTW(mfcc,mfccRef);
                n++;
            }
            while (cursor.moveToNext());
        }
        cursor.close();

        db.close();

        int K = 5;
        int resultat = KNN(K, classes, distances);

        return (resultat);

    }

    private static int KNN(int K, int[] classes, float[] distances) {

        int[] resultats = new int[12];

        // Recherche des K plus proches voisins
        for (int k = 0; k<K; k++) {
            int position = 0;
            int classe = classes[0];
            float distance = distances[0];
            for (int n = 1; n<distances.length; n++) {
                if (distances[n]<distance) {
                    classe = classes[n];
                    distance = distances[n];
                    position = n;
                }
            }
            distances[position] = (float) Double.MAX_VALUE;
            resultats[classe]++;
        }

        // Détermination du résultat
        int resultat = 0;
        for (int n = 0; n<12; n++) {
            if (resultats[n]> resultat) {
                resultat = n;
            }
        }
        System.out.println("Rapport : "+resultats[resultat]+"/"+K);

        return(resultat);

    }

}