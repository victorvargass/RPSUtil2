package victorvs.com.rpsutil2;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class finalActivity extends AppCompatActivity {

    private TextView b_i;
    private TextView b_f;
    private TextView n_bt;
    private TextView m_total;
    private TextView turno;

    private TextView n_bn;
    private TextView m_nulas;

    private Button init;

    private ExpandableListView lista_boletas;
    ExpandableListAdapter listAdapter;
    List<String> listDataHeader;
    HashMap<String, List<String>> listDataChild;


    @Override
    public void onBackPressed() {
        overridePendingTransition(R.anim.right_in, R.anim.right_out);
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

        lista_boletas = (ExpandableListView)findViewById(R.id.listView);

        init = (Button) findViewById(R.id.init);


        Bundle bundle = getIntent().getExtras();
        final String tipo = bundle.getString("tipo");
        final String fecha = bundle.getString("fecha");

        ArrayList<String> datos_turno = MainController.getTurnobyPK(this,fecha,tipo);

        Log.d("Final :",fecha+" "+tipo);
        String valor_total = datos_turno.get(2);
        String cantidad_boletas = datos_turno.get(3);
        String valor_total_nulas=datos_turno.get(4);
        String cantidad_boletas_nulas = datos_turno.get(5);


        b_i.setText(MainController.numeroBoletaInicial(this,fecha,tipo));
        b_f.setText(MainController.numeroBoletaFinal(this,fecha,tipo));
        n_bt.setText(cantidad_boletas);
        m_total.setText(valor_total);
        turno.setText("Turno " + tipo);
        n_bn.setText(cantidad_boletas_nulas);
        m_nulas.setText(valor_total_nulas);
        MainController.finalizarTurno(this,tipo,fecha);

        init.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent2 = new Intent(v.getContext(), firstActivity.class);
                startActivityForResult(intent2, 1);
            }
        });

        listDataHeader = new ArrayList<>();
        listDataChild = new HashMap<>();

        JSONArray boletas = MainController.getBoletas(getApplicationContext(),fecha,tipo);
        for(int i = 0;i<boletas.length();i++){
            try {
                JSONObject boleta_actual = boletas.getJSONObject(i);
                listDataHeader.add("Boleta: " + boleta_actual.getString("boleta"));

                List<String> list_child = new ArrayList<>();
                list_child.add("Valor: "+boleta_actual.getString("valor")+ "\n"
                            + "Fecha: "+boleta_actual.getString("fecha_turno")+ "\n"
                            + "Tipo: "+boleta_actual.getString("tipo_turno"));
                listDataChild.put(listDataHeader.get(i), list_child);
            }
            catch(Exception e){
                Log.d("JSON","Error al agregar valores en listDataChild");
            }
        }

        listAdapter = new ExpandableListAdapter(this, listDataHeader, listDataChild);

        lista_boletas.setAdapter(listAdapter);
    }

}
