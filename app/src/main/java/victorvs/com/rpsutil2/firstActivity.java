package victorvs.com.rpsutil2;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class firstActivity extends AppCompatActivity {

    private String BD_NAME = "base1";
    private Button ini_button;
    private EditText num_boleta;

    @Override
    public void onBackPressed() {
        overridePendingTransition(R.anim.right_in, R.anim.right_out);
    }

    public int getLastEstado(){
        CursorSQLHelper db=new CursorSQLHelper(this,BD_NAME,null,1);
        SQLiteDatabase bd=db.getWritableDatabase();
        Cursor c=bd.rawQuery("SELECT estado FROM Turno ORDER BY id DESC LIMIT 1",null);

        int estado =0;
        if (c.moveToFirst()) {
            estado = c.getInt(0);
        }
        bd.close();
        return estado;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first);
        ini_button = (Button) findViewById(R.id.ini_button);
        final EditText num_boleta = (EditText) findViewById(R.id.num_boleta);

        if(getLastEstado()==1){
            Intent intent = new Intent(this, MainActivity.class);
            intent.putExtra("estado", 1);
            startActivityForResult(intent, 0);
        }

        ini_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (num_boleta.getText().length() != 4) {
                    if (num_boleta.getText().length() == 0) {
                        Toast.makeText(getApplicationContext(), "Ingrese número de boleta", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getApplicationContext(), "Número de boleta incorrecta\nIngrese nuevamente", Toast.LENGTH_SHORT).show();
                        num_boleta.setText("");
                    }
                } else {
                    Intent intent = new Intent(v.getContext(), MainActivity.class);
                    intent.putExtra("n_boleta", num_boleta.getText().toString());
                    intent.putExtra("estado", 0);
                    startActivityForResult(intent, 0);
                    overridePendingTransition(R.anim.left_in, R.anim.left_out);
                }
            }
        });

        /*
        reanudar_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });*/

    }
}