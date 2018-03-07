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

        Button buttonLecture = (Button) findViewById(R.id.buttonLecture);
        Button buttonDictee = (Button) findViewById(R.id.buttonDictee);
        Button buttonLecture2 = (Button) findViewById(R.id.buttonLecture2);
        Button buttonTest = (Button) findViewById(R.id.buttonTest);

        buttonLecture.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent passageLecture = new Intent(MenuActivity.this, LectureDeNote.class);
                startActivity(passageLecture);
            }
        });

        buttonDictee.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent passageDictee = new Intent(MenuActivity.this, DicteeDeNote.class);
                startActivity(passageDictee);
            }
        });

        buttonLecture2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent passageLecture2 = new Intent(MenuActivity.this, LectureDeNoteReconnaissance.class);
                startActivity(passageLecture2);
            }
        });

        buttonTest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent passageTest = new Intent(MenuActivity.this, TestRecord.class);
                startActivity(passageTest);
            }
        });



    }
}