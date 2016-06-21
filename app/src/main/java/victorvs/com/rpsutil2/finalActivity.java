package victorvs.com.rpsutil2;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class finalActivity extends AppCompatActivity {

    private String BD_NAME = "BD10";

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


    public String valorTotal(int nula) {
        CursorSQLHelper db = new CursorSQLHelper(this,
                BD_NAME, null, 1);
        SQLiteDatabase bd = db.getWritableDatabase();
        Cursor fila = bd.rawQuery(
                "SELECT SUM(valor) FROM Boleta WHERE nula='" + nula + "'", null);

        String datos = "";
        if (fila.moveToFirst()) {
            datos = fila.getString(0);
        }
        bd.close();
        return datos;
    }


    public String cantidadBoletas() {
        String datos = "";
        CursorSQLHelper db = new CursorSQLHelper(this,
                BD_NAME, null, 1);
        SQLiteDatabase bd = db.getWritableDatabase();
        Cursor fila = bd.rawQuery(
                "SELECT COUNT(DISTINCT boleta) FROM Boleta", null);

        if (fila.moveToFirst()) {
            datos = fila.getString(0);
        }
        bd.close();
        return datos;
    }
    public String cantidadBoletas(int nula) {
        String datos = "";
        CursorSQLHelper db = new CursorSQLHelper(this,
                BD_NAME, null, 1);
        SQLiteDatabase bd = db.getWritableDatabase();
        Cursor fila = bd.rawQuery(
                "SELECT COUNT(DISTINCT boleta) FROM Boleta WHERE nula='" + nula + "'", null);

        if (fila.moveToFirst()) {
            datos = fila.getString(0);
        }
        bd.close();
        return datos;
    }

    public String nBoletaI() {
        String datos = "";
        CursorSQLHelper db = new CursorSQLHelper(this,
                BD_NAME, null, 1);
        SQLiteDatabase bd = db.getWritableDatabase();
        Cursor fila = bd.rawQuery(
                "SELECT boleta FROM Boleta ORDER BY id ASC LIMIT 1", null);
        if (fila.moveToFirst()) {
            datos = fila.getString(0);
        }
        bd.close();
        return datos;
    }

    public String nBoletaF() {
        String datos = "";
        CursorSQLHelper db = new CursorSQLHelper(this,
                BD_NAME, null, 1);
        SQLiteDatabase bd = db.getWritableDatabase();
        Cursor fila = bd.rawQuery(
                "SELECT boleta FROM Boleta ORDER BY id DESC LIMIT 1", null);

        if (fila.moveToFirst()) {
            datos = fila.getString(0);
        }
        bd.close();
        return datos;
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
        final String turnos = bundle.getString("turno");

        b_i.setText(nBoletaI());
        b_f.setText(nBoletaF());
        n_bt.setText(cantidadBoletas());
        m_total.setText(valorTotal(0));
        turno.setText("Turno " + turnos);
        n_bn.setText(cantidadBoletas(1));
        m_nulas.setText(valorTotal(1));

        init.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent2 = new Intent(v.getContext(), firstActivity.class);
                startActivityForResult(intent2, 1);
            }
        });

    }
}
