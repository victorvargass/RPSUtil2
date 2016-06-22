package victorvs.com.rpsutil2;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

public class finalActivity extends AppCompatActivity {

    private String BD_NAME = "base9";

    private TextView b_i;
    private TextView b_f;
    private TextView n_bt;
    private TextView m_total;
    private TextView turno;

    private TextView n_bn;
    private TextView m_nulas;

    private Button init;

    @Override
    public void onBackPressed() {
        overridePendingTransition(R.anim.right_in, R.anim.right_out);
    }

    public String nBoletaF(String fecha,String tipo) {
        String datos = "";
        CursorSQLHelper db = new CursorSQLHelper(this,
                BD_NAME, null, 1);
        SQLiteDatabase bd = db.getWritableDatabase();
        Cursor fila = bd.rawQuery(
                "SELECT boleta FROM Boleta WHERE fecha_turno='"+fecha+"' AND tipo_turno='"+tipo+"' ORDER BY id DESC LIMIT 1", null);

        if (fila.moveToFirst()) {
            datos = fila.getString(0);
        }
        bd.close();
        return datos;
    }
    public String nBoletaI(String fecha,String tipo) {
        String datos = "";
        CursorSQLHelper db = new CursorSQLHelper(this,
                BD_NAME, null, 1);
        SQLiteDatabase bd = db.getWritableDatabase();
        Cursor fila = bd.rawQuery(
                "SELECT boleta FROM Boleta WHERE fecha_turno='"+fecha+"' AND tipo_turno='"+tipo+"' ORDER BY id ASC LIMIT 1", null);

        if (fila.moveToFirst()) {
            datos = fila.getString(0);
        }
        bd.close();
        return datos;
    }



    public ArrayList<String> getTurnobyPK(String fecha_turno,String tipo_turno){
        ArrayList<String> datos_turno=new ArrayList<>();
        CursorSQLHelper db = new CursorSQLHelper(this,BD_NAME, null, 1);
        SQLiteDatabase bd = db.getWritableDatabase();
        Cursor c = bd.rawQuery(
                "SELECT fecha,tipo,valor_total,cantidad_boletas FROM Turno WHERE fecha='"+fecha_turno+
                        "' AND tipo='"+tipo_turno+"' ORDER BY id DESC LIMIT 1", null);

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

    public void finalizarTurno(String tipo, String fecha){
        ContentValues values=new ContentValues();
        values.put("estado",0);

        String where="tipo=? AND fecha=?";
        String[] whereArgs={tipo,fecha};
        CursorSQLHelper ch=new  CursorSQLHelper(this, BD_NAME, null, 1);
        SQLiteDatabase db=ch.getWritableDatabase();
        int nColumnasActualizadas=db.update("Turno",values,where,whereArgs);
        Log.d("FIn T N columnas act: ",String.valueOf(nColumnasActualizadas));
        db.close();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_final);

        b_i = (TextView) findViewById(R.id.b_i);
        b_f = (TextView) findViewById(R.id.b_f);
        n_bt = (TextView) findViewById(R.id.n_bt);
        m_total = (TextView) findViewById(R.id.m_total);
        turno = (TextView) findViewById(R.id.turno);

        n_bn = (TextView) findViewById(R.id.n_bn);
        m_nulas = (TextView) findViewById(R.id.m_nulas);

        init = (Button) findViewById(R.id.init);


        Bundle bundle = getIntent().getExtras();
        final String tipo = bundle.getString("tipo");
        final String fecha = bundle.getString("fecha");

        ArrayList<String> datos_turno = getTurnobyPK(fecha,tipo);

        Log.d("Final :",fecha+" "+tipo);
        String valor_total = datos_turno.get(2);
        String cantidad_boletas = datos_turno.get(3);

        b_i.setText(nBoletaI(fecha,tipo));
        b_f.setText(nBoletaF(fecha,tipo));
        n_bt.setText(cantidad_boletas);
        m_total.setText(valor_total);
        turno.setText("Turno " + tipo);
        n_bn.setText("prox");
        m_nulas.setText("prox");
        finalizarTurno(tipo,fecha);

        init.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent2 = new Intent(v.getContext(), firstActivity.class);
                startActivityForResult(intent2, 1);
            }
        });

    }
}
