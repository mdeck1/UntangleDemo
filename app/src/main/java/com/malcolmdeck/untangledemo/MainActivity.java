package com.malcolmdeck.untangledemo;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {

    EditText numVerticesText;
    Button button;
    GameView gameView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        numVerticesText = findViewById(R.id.edit_text_num_points);
        button = findViewById(R.id.btn_draw_graph);
        gameView = findViewById(R.id.game_view);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!TextUtils.isEmpty(numVerticesText.getText().toString())) {
                    int numVertices = Integer.parseInt(numVerticesText.getText().toString());
                    gameView.setGameState(new GameState(numVertices));
                    gameView.invalidate();
                }
            }
        });
    }
}
