package perriobarreteau.apprentissagemusique;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MenuActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        Button buttonLecture_Button = (Button) findViewById(R.id.buttonLecture_Button);
        Button buttonLecture_Note = (Button) findViewById(R.id.buttonLecture_Note);
        Button buttonLecture_Speech = (Button) findViewById(R.id.buttonLecture_Speech);
        Button buttonDictee_Button = (Button) findViewById(R.id.buttonDictee_Button);
        Button buttonAccorder = (Button) findViewById(R.id.buttonAccordeur);
        Button buttonVocal = (Button) findViewById(R.id.buttonVocal);

        buttonLecture_Button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent intent = new Intent(MenuActivity.this, LectureDeNote_Button.class);
                startActivity(intent);
            }
        });

        buttonLecture_Note.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent intent = new Intent(MenuActivity.this, LectureDeNote_Note.class);
                startActivity(intent);
            }
        });

        buttonLecture_Speech.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent intent = new Intent(MenuActivity.this, LectureDeNote_Speech.class);
                startActivity(intent);
            }
        });

        buttonDictee_Button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent intent = new Intent(MenuActivity.this, DicteeDeNote_Button.class);
                startActivity(intent);
            }
        });

        buttonAccorder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MenuActivity.this, Accordeur.class);
                startActivity(intent);
            }
        });

        buttonVocal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MenuActivity.this, DicteeVocale.class);
                startActivity(intent);
            }
        });

    }
}