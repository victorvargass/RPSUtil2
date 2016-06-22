package victorvs.com.rpsutil2;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

public class firstActivity extends AppCompatActivity {

    private String BD_NAME = "base9";
    private Button ini_button;
    private EditText num_boleta;
    private String turno;
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
        Spinner spinner = (Spinner) findViewById(R.id.spinTurno);

        String[] valores = {"A","B","C"};
        spinner.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, valores));

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id)
            {
                turno=(String) adapterView.getItemAtPosition(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


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
                    intent.putExtra("turno",turno);
                    if(getTurnobyPK(getFechaActual(),turno).isEmpty()) {
                        startActivityForResult(intent, 0);
                        overridePendingTransition(R.anim.left_in, R.anim.left_out);
                    }
                    else{
                        Toast.makeText(getApplicationContext(), "Turno actualmente finalizado\nSeleccione turno", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

    }
    public ArrayList<String> getTurnobyPK(String fecha_turno, String tipo_turno){
        ArrayList<String> datos_turno=new ArrayList<>();
        CursorSQLHelper db = new CursorSQLHelper(this,BD_NAME, null, 1);
        SQLiteDatabase bd = db.getWritableDatabase();
        Cursor c = bd.rawQuery(
                "SELECT fecha,tipo,valor_total,cantidad_boletas FROM Turno WHERE fecha='"+fecha_turno+
                        "' AND tipo='"+tipo_turno+"' AND estado=0 ORDER BY id DESC LIMIT 1", null);

        if (c.moveToFirst()) {
            String fecha = c.getString(0);
            String tipo = c.getString(1);
            String valor_total = String.valueOf(c.getInt(2));
            String cantidad_boletas = String.valueOf(c.getInt(3));
            datos_turno.add(0,fecha);
            datos_turno.add(1,tipo);
            datos_turno.add(2,valor_total);
            datos_turno.add(3,cantidad_boletas);
        }
        db.close();
        return datos_turno;
    }

    public String getFechaActual() {
        return new SimpleDateFormat("yyyy-MM-dd",
                Locale.getDefault()).format(Calendar.getInstance().getTime());
    }
}