package perriobarreteau.apprentissagemusique;

public class mfcc {

    private int cle;
    private int classe;
    private String mfcc;

    public mfcc(int cle, int classe, String mfcc) {
        this.cle = cle;
        this.classe = classe;
        this.mfcc = mfcc;
    }

    public int getCle() {
        return cle;
    }

    public void setCle(int cle) {
        this.cle = cle;
    }

    public int getClasse() {
        return classe;
    }

    public void setClasse(int classe) {
        this.classe = classe;
    }

    public String getMfcc() {
        return mfcc;
    }

    public void setMfcc(String mfcc) {
        this.mfcc = mfcc;
    }
}
