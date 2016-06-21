package victorvs.com.rpsutil2;


import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.net.Uri;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private String BD_NAME = "BD10";

    private int año;
    private int mes;
    private int dia;
    private final static int TIPO_DIALOGO = 0;
    private static DatePickerDialog.OnDateSetListener oyenteSelectorFecha;

    private TextView textView_horas;
    private TextView textView_precio;
    private TextView textView_total;

    private TextView num_boleta;

    private TextView textView_fechas;

    private Button button1;
    private Button button2;
    private Button button3;
    private Button button4;
    private Button button5;
    private Button button6;
    private Button button7;
    private Button button8;
    private Button button9;
    private Button button0;
    private Button button_ac;
    private Button button_del;

    private Button button_sd;
    private Button button_50;
    private Button button_75;
    private Button button_guardar;
    private Button calendar;

    private String primera_hora;
    private String segunda_hora;
    private Integer hrs1, hrs2, mins1, mins2, precio, precio_dsc;
    private Integer total = 0;
    private Integer total_boletas = 0;

    private String turno = "";
    private boolean nul;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;


    public void insertarBoleta(String boleta, String valor, int nula, String turno, String fecha) {
        CursorSQLHelper ch = new CursorSQLHelper(this, BD_NAME, null, 1);
        SQLiteDatabase db = ch.getWritableDatabase();
        try {
            ContentValues c = new ContentValues();
            c.put("boleta", boleta);
            c.put("valor", valor);
            c.put("nula", nula);
            c.put("turno", turno);
            c.put("fecha", fecha);
            db.insert("Boleta", null, c);
            db.close();

        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_SHORT).show();
        }
    }

    /*
    public void actualizarBoleta(String boleta, String valor, int nula, String turno, String fecha) {
        CursorSQLHelper ch = new CursorSQLHelper(this, BD_NAME, null, 1);
        SQLiteDatabase db = ch.getWritableDatabase();

        try {
            ContentValues c = new ContentValues();
            c.put("boleta", boleta);
            c.put("valor", valor);
            c.put("nula", nula);
            c.put("turno", turno);
            c.put("fecha", fecha);
            db.insert("Boleta", null, c);
            db.close();

        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_SHORT).show();
        }
        // updating row
        return db.update();
    }*/


    public String consultaBoleta(String n_boleta) {
        String datos = "";
        CursorSQLHelper db = new CursorSQLHelper(this,
                BD_NAME, null, 1);
        SQLiteDatabase bd = db.getWritableDatabase();
        Cursor fila = bd.rawQuery(
                "SELECT id,boleta,valor,nula,turno,fecha FROM Boleta WHERE boleta='" + n_boleta + "'", null);

        if (fila.moveToFirst()) {
            String n = fila.getString(0);
            String c = fila.getString(1);
            String v = fila.getString(2);
            String nul = fila.getString(3);
            String t = fila.getString(4);
            String f = fila.getString(5);

            String nula = "";
            switch (nul) {
                case "0":
                    nula = "No";
                    break;
                case "1":
                    nula = "Si";
                    break;
            }
            datos = "id: " + n + "\n" +
                    "Numero boleta: " + c + "\n" +
                    "Valor: " + v + "\n" +
                    "Nula: " + nula + "\n" +
                    "Turno: " + t + "\n" +
                    "Fecha: " + f;

        } else {
            Toast.makeText(this, "No existe una boleta con dicho código",
                    Toast.LENGTH_SHORT).show();
        }
        bd.close();
        return datos;
    }

/*
    public ArrayList<String> getBoletasTotal() {
        //Open connection to read only
        CursorSQLHelper db = new CursorSQLHelper(this,
                BD_NAME, null, 1);
        SQLiteDatabase bd = db.getWritableDatabase();
        ArrayList<HashMap<String, String>> listaBoletas = new ArrayList<>();

        Cursor cursor = bd.rawQuery("SELECT id,boleta,valor,nula,turno,fecha FROM Boleta", null);

        if (cursor.moveToFirst()) {
            do {
                HashMap<String, String> boleta = new HashMap<String, String>();
                boleta.put("id", cursor.getString(cursor.getColumnIndex("id")));
                boleta.put("boleta", cursor.getString(cursor.getColumnIndex("boleta")));
                boleta.put("valor", cursor.getString(cursor.getColumnIndex("valor")));
                boleta.put("nula", cursor.getString(cursor.getColumnIndex("nula")));
                boleta.put("turno", cursor.getString(cursor.getColumnIndex("turno")));
                boleta.put("fecha", cursor.getString(cursor.getColumnIndex("fecha")));

                listaBoletas.add(boleta);

            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();

        String a= String.valueOf(listaBoletas.size());

        Log.d("hola",a);
        return listaBoletas;
    }
    */

    public int valorTotal(int nula) {
        CursorSQLHelper db = new CursorSQLHelper(this,
                BD_NAME, null, 1);
        SQLiteDatabase bd = db.getWritableDatabase();
        Cursor fila = bd.rawQuery(
                "SELECT SUM(valor) FROM Boleta WHERE nula='" + nula + "'", null);

        int datos=0;
        if (fila.moveToFirst()) {
            datos = fila.getInt(0);
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

    public int cantidadBoletas() {
        int datos = 0;
        CursorSQLHelper db = new CursorSQLHelper(this,
                BD_NAME, null, 1);
        SQLiteDatabase bd = db.getWritableDatabase();
        Cursor fila = bd.rawQuery(
                "SELECT COUNT(DISTINCT boleta) FROM Boleta", null);

        if (fila.moveToFirst()) {
            datos = fila.getInt(0);
        }
        bd.close();
        return datos;
    }

    public int nBoletaI() {
        int datos = 0;
        CursorSQLHelper db = new CursorSQLHelper(this,
                BD_NAME, null, 1);
        SQLiteDatabase bd = db.getWritableDatabase();
        Cursor fila = bd.rawQuery(
                "SELECT boleta FROM Boleta ORDER BY id ASC LIMIT 1", null);
        if (fila.moveToFirst()) {
            datos = fila.getInt(0);
        }
        bd.close();
        return datos;
    }

    public int nBoletaF() {
        int datos = 0;
        CursorSQLHelper db = new CursorSQLHelper(this,
                BD_NAME, null, 1);
         SQLiteDatabase bd = db.getWritableDatabase();
        Cursor fila = bd.rawQuery(
                "SELECT boleta FROM Boleta ORDER BY id DESC LIMIT 1", null);

        if (fila.moveToFirst()) {
            datos = fila.getInt(0);
        }
        bd.close();
        return datos;
    }

    public void eliminarUltimaBoleta() {
        String valor="";
        CursorSQLHelper db = new CursorSQLHelper(this,
                BD_NAME, null, 1);
        SQLiteDatabase bd = db.getWritableDatabase();
        Cursor fila = bd.rawQuery(
                "SELECT boleta FROM Boleta ORDER BY id DESC LIMIT 1", null);

        if (fila.moveToFirst()) {
            valor = fila.getString(0);
        }
        bd.execSQL("DELETE FROM Boleta WHERE boleta=" +valor);
        bd.close();
    }


    public String getTurno() {
        String horaA = new SimpleDateFormat("HH:mm",
                Locale.getDefault()).format(Calendar.getInstance().getTime());
        Calendar horaActual = Calendar.getInstance();

        String h = horaA.substring(0, 2);
        String m = horaA.substring(3, 5);
        int horas = Integer.parseInt(h);
        int minutos = Integer.parseInt(m);

        if ((horas >= 0 && horas <= 7) && (minutos >= 0 && minutos <= 59)) {
            turno = "A";
        }
        if ((horas >= 8 && horas <= 15) && (minutos >= 0 && minutos <= 59)) {
            turno = "B";
        }
        if ((horas >= 16 && horas <= 23) && (minutos >= 0 && minutos <= 59)) {
            turno = "C";
        }

        return turno;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("TURNO " + getTurno());  // provide compatibility to all the versions


        textView_horas = (TextView) findViewById(R.id.textView_horas);
        textView_precio = (TextView) findViewById(R.id.textView_precio);
        textView_total = (TextView) findViewById(R.id.textView_total);

        num_boleta = (TextView) findViewById(R.id.num_boleta);

        textView_fechas = (TextView) findViewById(R.id.textView_fechas);
        button0 = (Button) findViewById(R.id.button0);
        button1 = (Button) findViewById(R.id.button1);
        button2 = (Button) findViewById(R.id.button2);
        button3 = (Button) findViewById(R.id.button3);
        button4 = (Button) findViewById(R.id.button4);
        button5 = (Button) findViewById(R.id.button5);
        button6 = (Button) findViewById(R.id.button6);
        button7 = (Button) findViewById(R.id.button7);
        button8 = (Button) findViewById(R.id.button8);
        button9 = (Button) findViewById(R.id.button9);

        button_ac = (Button) findViewById(R.id.button_ac);
        button_del = (Button) findViewById(R.id.button_del);

        button_sd = (Button) findViewById(R.id.button_sd);
        button_50 = (Button) findViewById(R.id.button_50);
        button_75 = (Button) findViewById(R.id.button_75);

        button_guardar = (Button) findViewById(R.id.button_guardar);

        calendar = (Button) findViewById(R.id.calendar);
        Calendar calendario = Calendar.getInstance();
        año = calendario.get(Calendar.YEAR);
        mes = calendario.get(Calendar.MONTH);
        dia = calendario.get(Calendar.DAY_OF_MONTH);

        oyenteSelectorFecha = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                año = year;
                mes = monthOfYear;
                dia = dayOfMonth;
                getFechaLlegada(año, mes, dia);
            }
        };


        //Importar datos de boleta desde firstActivity
        Bundle bundle = getIntent().getExtras();
        final String texto = bundle.getString("frase");
        num_boleta.setText(texto);

        textView_total.setText("$ "+valorTotal(0));
        restaurarEstado(savedInstanceState);
        //Numeros
        button0.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textView_horas.setText(textView_horas.getText() + "0");
            }
        });

        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textView_horas.setText(textView_horas.getText() + "1");
            }
        });

        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textView_horas.setText(textView_horas.getText() + "2");
            }
        });

        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textView_horas.setText(textView_horas.getText() + "3");
            }
        });

        button4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textView_horas.setText(textView_horas.getText() + "4");
            }
        });

        button5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textView_horas.setText(textView_horas.getText() + "5");
            }
        });

        button6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textView_horas.setText(textView_horas.getText() + "6");
            }
        });

        button7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textView_horas.setText(textView_horas.getText() + "7");
            }
        });

        button8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textView_horas.setText(textView_horas.getText() + "8");
            }
        });

        button9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textView_horas.setText(textView_horas.getText() + "9");
            }
        });

        button_ac.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textView_horas.setText("");
                precio = 0;
                precio_dsc = 0;
                textView_fechas.setText("");

            }
        });

        button_del.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textView_fechas.setText("");
                if (textView_horas.getText().length() == 13) {
                    textView_horas.setText(textView_horas.getText().toString().substring(0, textView_horas.getText().length() - 5));
                }
                if (textView_horas.getText().length() != 0) {
                    if (textView_horas.getText().toString().substring(textView_horas.length() - 1, textView_horas.length()).equals(":")) {
                        textView_horas.setText(textView_horas.getText().toString().substring(0, textView_horas.getText().length() - 2));
                    } else if (textView_horas.getText().toString().substring(textView_horas.length() - 1, textView_horas.length()).equals(" ")) {
                        textView_horas.setText(textView_horas.getText().toString().substring(0, textView_horas.getText().length() - 4));
                    } else {
                        textView_horas.setText(textView_horas.getText().toString().substring(0, textView_horas.getText().length() - 1));
                    }
                }
            }
        });

        //DESCUENTOS
        button_sd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (itsEmpty()) {
                    Toast.makeText(getApplicationContext(), "Ingrese hora", Toast.LENGTH_SHORT).show();
                } else {
                    if (textView_horas.getText().length() == 13) {
                        precio_dsc = precio;
                        textView_precio.setText("$ " + precio_dsc.toString());
                        textView_precio.setTextColor(Color.WHITE);
                    } else {
                        Toast.makeText(getApplicationContext(), "Ingrese hora correctamente", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        button_50.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (itsEmpty()) {
                    Toast.makeText(getApplicationContext(), "Ingrese hora", Toast.LENGTH_SHORT).show();
                } else {
                    if (textView_horas.getText().length() == 13) {
                        precio_dsc = precio / 2;
                        textView_precio.setText("$ " + precio_dsc.toString());
                        textView_precio.setTextColor(Color.BLUE);
                    } else {
                        Toast.makeText(getApplicationContext(), "Ingrese hora correctamente", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        button_75.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (itsEmpty()) {
                    Toast.makeText(getApplicationContext(), "Ingrese hora", Toast.LENGTH_SHORT).show();
                } else {
                    if (textView_horas.getText().length() == 13) {
                        precio_dsc = precio / 4;
                        textView_precio.setText("$ " + precio_dsc.toString());
                        textView_precio.setTextColor(Color.YELLOW);
                    } else {
                        Toast.makeText(getApplicationContext(), "Ingrese hora correctamente", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });


        //GUARDAR BOLETA
        button_guardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (textView_horas.getText().length() < 5 && num_boleta.getText() != "") {
                    Toast.makeText(getApplicationContext(), "Ingrese hora", Toast.LENGTH_SHORT).show();
                    textView_horas.setText("");
                }

                if ((itsEmpty() || precio == 0) && (textView_total.getText() == "")
                        || (textView_total.getText() != "") && (textView_horas.getText().length() < 5)) {
                    Toast.makeText(getApplicationContext(), "Ingrese hora", Toast.LENGTH_SHORT).show();
                    textView_horas.setText("");
                } else {
                    String boleta = num_boleta.getText().toString();
                    Integer n_bol = Integer.parseInt(boleta);
                    if (precio_dsc != 0) {
                        String bol = n_bol.toString();
                        n_bol = n_bol + 1;
                        String prec = precio_dsc.toString();
                        num_boleta.setText(n_bol.toString());
                        insertarBoleta(bol, prec, 0, getTurno(), getFechaActual().substring(0, 11));
                        Toast.makeText(getApplicationContext(), "Boleta ingresada con éxito", Toast.LENGTH_SHORT).show();
                        textView_total.setText("$ " + valorTotal(0));

                    }
                    textView_horas.setText("");
                    textView_fechas.setText("");
                    precio = 0;
                    precio_dsc = 0;
                    nul = false;
                }
            }
        });


        //MENU DESPLEGABLE OPCIONES
        assert toolbar != null;
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {

                switch (menuItem.getItemId()) {


                    case R.id.boleta_nula:
                        textView_horas.setText("");
                        textView_fechas.setText("");
                        precio = 0;
                        precio_dsc = 0;

                        View view = (LayoutInflater.from(MainActivity.this)).inflate(R.layout.input_boleta_nula, null);

                        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                        builder.setView(view);
                        builder.setTitle("Boleta nula");
                        builder.setMessage("Ingrese valor");

                        final EditText ingreso_valor = (EditText) view.findViewById(R.id.ingreso_valor);
                        ingreso_valor.setInputType(InputType.TYPE_CLASS_NUMBER);

                        builder.setCancelable(true)
                                .setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {

                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        String boleta = num_boleta.getText().toString();
                                        Integer n_bol = Integer.parseInt(boleta);
                                        String valor_nula = ingreso_valor.getText().toString();
                                        nul = true;
                                        if (valor_nula.length() == 0) {
                                            valor_nula = "0";
                                            n_bol = n_bol + 1;
                                            String bol = n_bol.toString();
                                            num_boleta.setText(bol);
                                            insertarBoleta(boleta, valor_nula, 1, getTurno(), getFechaActual().substring(0, 11));
                                            Toast.makeText(getApplicationContext(), "Boleta nula ingresada con éxito", Toast.LENGTH_SHORT).show();
                                        } else {
                                            int v = Integer.parseInt(valor_nula);
                                            if (v % 125 == 0) {
                                                n_bol = n_bol + 1;
                                                String bol = n_bol.toString();
                                                num_boleta.setText(bol);
                                                Toast.makeText(getApplicationContext(), "Boleta nula ingresada con éxito", Toast.LENGTH_SHORT).show();
                                                insertarBoleta(boleta, valor_nula, 1, getTurno(), getFechaActual().substring(0, 11));
                                            } else {
                                                Toast.makeText(getApplicationContext(), "Valor de boleta erróneo", Toast.LENGTH_SHORT).show();

                                            }

                                        }

                                    }
                                });

                        Dialog dialog = builder.create();
                        dialog.show();
                        return true;

                    case R.id.consultar_boleta:
                        View view2 = (LayoutInflater.from(MainActivity.this)).inflate(R.layout.input_consultar_boleta, null);

                        AlertDialog.Builder builder4 = new AlertDialog.Builder(MainActivity.this);
                        builder4.setView(view2);
                        builder4.setTitle("Consultar boleta");
                        builder4.setMessage("Ingrese numero de boleta");

                        final EditText input_boleta = (EditText) view2.findViewById(R.id.input_boleta);
                        input_boleta.setInputType(InputType.TYPE_CLASS_NUMBER);

                        builder4.setCancelable(true)
                                .setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {

                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        String cod = input_boleta.getText().toString();
                                        String c = consultaBoleta(cod);
                                        nul = true;
                                        if ((cod.length() != 0) && (c != "")) {
                                            Toast.makeText(getApplicationContext(), "Datos boleta: \n" + c, Toast.LENGTH_LONG).show();
                                        }
                                    }
                                });
                        Dialog dialog3 = builder4.create();
                        dialog3.show();


                        return true;


                    case R.id.eliminar_boleta:
                        if (cantidadBoletas()==0) {
                            Toast.makeText(getApplicationContext(), "Genere boletas", Toast.LENGTH_SHORT).show();
                        } else {
                            AlertDialog.Builder builder2 = new AlertDialog.Builder(MainActivity.this);
                            builder2.setMessage("¿Está seguro?");
                            builder2.setTitle("Eliminar boleta");
                            builder2.setPositiveButton("Eliminar", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                    if (nul == false) {
                                        num_boleta.setText(String.valueOf(nBoletaF()));

                                        eliminarUltimaBoleta();
                                        textView_total.setText("$ " + valorTotal(0));
                                    }
                                }
                            });

                            builder2.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel();
                                }
                            });
                            AlertDialog dialog2 = builder2.create();
                            dialog2.show();


                        }
                        return true;


                    case R.id.fin_turno:
                        if (cantidadBoletas()==0) {
                            Toast.makeText(getApplicationContext(), "Genere boletas", Toast.LENGTH_SHORT).show();
                        } else {
                            AlertDialog.Builder builder1 = new AlertDialog.Builder(MainActivity.this);
                            builder1.setMessage("¿Está seguro?");
                            builder1.setTitle("Finalizar turno");
                            builder1.setPositiveButton("Finalizar", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Intent intent2 = new Intent(getBaseContext(), finalActivity.class);
                                    intent2.putExtra("turno", getTurno());
                                    overridePendingTransition(R.anim.zoom_back_in, R.anim.zoom_back_out);
                                    startActivityForResult(intent2, 1);

                                }
                            });
                            builder1.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel();
                                }
                            });
                            AlertDialog dialog2 = builder1.create();
                            dialog2.show();

                        }
                        return true;


                }

                return false;
            }
        });


        textView_horas.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                textView_horas.setGravity(Gravity.CENTER);
                if (s.length() < 13) {
                    textView_precio.setText("");
                    textView_fechas.setText("");
                    if (s.length() == 2) {
                        textView_horas.setText(textView_horas.getText().toString() + ":");
                    }
                    if (s.length() == 5) {
                        textView_horas.setText(textView_horas.getText().toString() + " - ");
                        Calendar horaActual = Calendar.getInstance();
                        textView_horas.setText(textView_horas.getText().toString() + zerosAtLeft(horaActual.get(Calendar.HOUR_OF_DAY), 2));
                    }
                    if (s.length() == 10) {
                        textView_horas.setText(textView_horas.getText().toString() + ":");
                        Calendar horaActual = Calendar.getInstance();

                        textView_horas.setText(textView_horas.getText().toString() + zerosAtLeft(horaActual.get(Calendar.MINUTE), 2));
                    }
                } else {
                    textView_fechas.setText(
                            "     " + getFechaLlegada(año, mes, dia)
                                    + "         " + getFechaActual().substring(0, 10));
                    String fecha1 = getFechaLlegada(año, mes, dia) + " " + textView_horas.getText().toString().substring(0, 5) + ":00";
                    String fecha2 = getFechaActual().substring(0, 16) + ":00";
                    String fechaA = fecha2.substring(0, 10);


                    primera_hora = s.toString().substring(0, 2) + s.toString().substring(3, 5);
                    segunda_hora = s.toString().substring(8, 10) + s.toString().substring(11, 13);

                    hrs1 = Integer.parseInt(primera_hora.substring(0, 2));
                    hrs2 = Integer.parseInt(segunda_hora.substring(0, 2));

                    mins1 = Integer.parseInt(primera_hora.substring(2, 4));
                    mins2 = Integer.parseInt(segunda_hora.substring(2, 4));

                    //Validar hora
                    Integer minutos = Integer.parseInt(getDiferenciaMinutos(fecha1, fecha2));

                    if ((hrs1 > 23) || (mins1 > 59)) {
                        textView_horas.setText("");
                        Toast.makeText(getApplicationContext(), "Hora incorrecta, ingrese nuevamente", Toast.LENGTH_SHORT).show();
                    } else {
                        textView_precio.setTextColor(Color.WHITE);
                        Integer p = (minutos / 30);
                        Integer resto_minutos = (minutos % 30);
                        if (minutos <= 0) {
                            if (minutos > -1440) {
                                minutos = (minutos) + 1440;
                                Integer q = (minutos / 30);

                                if (minutos <= 10) {
                                    precio = 0;
                                    Toast.makeText(getApplicationContext(), "Salida en gracia", Toast.LENGTH_SHORT).show();
                                    textView_horas.setText("");
                                    textView_precio.setText("");
                                    precio_dsc = 0;
                                    textView_fechas.setText("");
                                } else {

                                    textView_fechas.setText("                AYER            " + fechaA);

                                    if ((minutos > 10) && (minutos <= 30)) {

                                        precio = 500;

                                    }
                                    if (((minutos > 30) && (minutos <= 360))) {

                                        if (resto_minutos == 0) {
                                            precio = q * 500;
                                        } else {
                                            precio = (q + 1) * 500;
                                        }
                                    }
                                    if (minutos > 360) {
                                        precio = 6000;
                                    }
                                }
                            } else {
                                precio = 0;
                                Toast.makeText(getApplicationContext(), "Fecha excede a fecha actual\nElija nuevamente", Toast.LENGTH_SHORT).show();
                                textView_horas.setText("");
                                textView_precio.setText("");
                                precio_dsc = 0;
                                textView_fechas.setText("");
                                calendar.setSelected(true);
                            }
                        } else {
                            if (minutos <= 10) {
                                precio = 0;
                                Toast.makeText(getApplicationContext(), "Salida en gracia", Toast.LENGTH_SHORT).show();
                                textView_horas.setText("");
                                textView_precio.setText("");
                                precio_dsc = 0;
                                textView_fechas.setText("");
                            } else {
                                if ((minutos > 10) && (minutos <= 30)) {
                                    precio = 500;
                                    textView_fechas.setText("                HOY               " + fechaA);
                                }
                                if (((minutos > 30) && (minutos <= 360))) {
                                    textView_fechas.setText("                HOY               " + fechaA);

                                    if (resto_minutos == 0) {
                                        precio = p * 500;
                                    } else {
                                        precio = (p + 1) * 500;
                                    }
                                }
                                if (minutos > 360) {
                                    Integer dias = (minutos / 1440);
                                    if (dias < 1) {
                                        textView_fechas.setText("                HOY               " + fechaA);
                                        precio = 6000;
                                    } else {
                                        precio = 6000 * (dias);
                                    }
                                }
                            }
                        }
                        precio_dsc = precio;
                        if (precio != 0) {
                            textView_precio.setText("$ " + precio.toString());

                        }

                    }
                }
            }
        });
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }


    public String zerosAtLeft(int number, int size) {
        String str_number = Integer.toString(number);
        for (int i = 0; i < size; i++) {
            if (str_number.length() < size) {
                str_number = "0" + str_number;
            }
        }
        return str_number;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.right_in, R.anim.right_out);
    }

    public boolean itsEmpty() {
        boolean empty = true;
        String precio = textView_precio.getText().toString();
        String hora = textView_horas.getText().toString();
        if (precio.length() != 0 || hora.length() != 0) {
            empty = false;
        } else if (textView_horas.getText().length() <= 4) {
            empty = true;
        } else if (precio.equals("$ 0")) {
            Toast.makeText(getApplicationContext(), "Salida en gracia", Toast.LENGTH_SHORT).show();
            textView_horas.setText("");
            textView_fechas.setText("");
            textView_precio.setText("");

            empty = false;
        }
        return empty;
    }


    public String getFechaActual() {
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss",
                Locale.getDefault()).format(Calendar.getInstance().getTime());
    }

    public String getFechaLlegada(int año, int mes, int dia) {
        String fechaL = año + "-" + (mes + 1) + "-" + dia;
        return fechaL;
    }


    public String getDiferenciaMinutos(String llegada, String actual) {
        Date fechaLlegada = null;
        Date fechaActual = null;
        // configuramos el formato en el que esta guardada la fecha en los strings que nos pasan
        SimpleDateFormat formato = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            // aca realizamos el parse, para obtener objetos de tipo Date de las Strings
            fechaLlegada = formato.parse(llegada);
            fechaActual = formato.parse(actual);

        } catch (ParseException e) {
            // Log.e(TAG, "Funcion getDiferenciaMinutos: Error Parse " + e);
        } catch (Exception e) {
            // Log.e(TAG, "Funcion getDiferenciaMinutos: Error " + e);
        }
        // tomamos la instancia del tipo de calendario
        Calendar calendarInicio = Calendar.getInstance();
        Calendar calendarFinal = Calendar.getInstance();

        // Configramos la fecha del calendatio, tomando los valores del date que generamos en el parse
        calendarInicio.setTime(fechaLlegada);
        calendarFinal.setTime(fechaActual);

        // obtenemos el valor de las fechas en milisegundos
        long milisegundos1 = calendarInicio.getTimeInMillis();
        long milisegundos2 = calendarFinal.getTimeInMillis();

        // tomamos la diferencia
        long diferenciaMilisegundos = milisegundos2 - milisegundos1;
        long diferenciaSegundos = (diferenciaMilisegundos / 1000);

        // calcular la diferencia en minutos
        long diferenciaMinutos = (diferenciaMilisegundos / (60 * 1000));
        long restominutos = diferenciaMinutos % 60;

        // calcular la diferencia en horas
        long diferenciaHoras = (diferenciaMilisegundos / (60 * 60 * 1000));

        // calcular la diferencia en dias
        long diferenciadias = (diferenciaMilisegundos / (24 * 60 * 60 * 1000));

        // devolvemos el resultado en un string
        return String.valueOf(diferenciaMinutos);
    }


    public String getDiferenciaDias(String llegada, String actual) {
        Date fechaLlegada = null;
        Date fechaActual = null;
        // configuramos el formato en el que esta guardada la fecha en los strings que nos pasan
        SimpleDateFormat formato = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            // aca realizamos el parse, para obtener objetos de tipo Date de las Strings
            fechaLlegada = formato.parse(llegada);
            fechaActual = formato.parse(actual);

        } catch (ParseException e) {
            // Log.e(TAG, "Funcion getDiferenciaMinutos: Error Parse " + e);
        } catch (Exception e) {
            // Log.e(TAG, "Funcion getDiferenciaMinutos: Error " + e);
        }
        // tomamos la instancia del tipo de calendario
        Calendar calendarInicio = Calendar.getInstance();
        Calendar calendarFinal = Calendar.getInstance();

        // Configramos la fecha del calendatio, tomando los valores del date que generamos en el parse
        calendarInicio.setTime(fechaLlegada);
        calendarFinal.setTime(fechaActual);

        // obtenemos el valor de las fechas en milisegundos
        long milisegundos1 = calendarInicio.getTimeInMillis();
        long milisegundos2 = calendarFinal.getTimeInMillis();

        // tomamos la diferencia
        long diferenciaMilisegundos = milisegundos2 - milisegundos1;

        // Despues va a depender en que formato queremos  mostrar esa
        // diferencia, minutos, segundo horas, dias, etc, aca van algunos
        // ejemplos de conversion

        // calcular la diferencia en segundos
        long diffSegundos = Math.abs(diferenciaMilisegundos / 1000);

        // calcular la diferencia en minutos
        long diffMinutos = Math.abs(diferenciaMilisegundos / (60 * 1000));
        long restominutos = diffMinutos % 60;

        // calcular la diferencia en horas
        long diffHoras = (diferenciaMilisegundos / (60 * 60 * 1000));

        // calcular la diferencia en dias
        long diffdias = Math.abs(diferenciaMilisegundos / (24 * 60 * 60 * 1000));

        // devolvemos el resultado en un string
        return String.valueOf(diffdias);
    }


    public void mostrarCalendario(View control) {
        showDialog(TIPO_DIALOGO);
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case 0:
                return new DatePickerDialog(this, oyenteSelectorFecha, año, mes, dia);
        }
        return null;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("bol", num_boleta.getText().toString());
        outState.putString("total_tv", textView_total.getText().toString());
        outState.putInt("t_bol", total_boletas);
        outState.putInt("total", total);
    }


    public void restaurarEstado(Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            String b = savedInstanceState.getString("bol");
            String t_tv = savedInstanceState.getString("total_tv");
            Integer tb = savedInstanceState.getInt("t_bol");
            Integer t = savedInstanceState.getInt("total");
            num_boleta.setText(b);
            total = t;
            textView_total.setText(t_tv);
            total_boletas = tb;
        }
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Main Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app URL is correct.
                Uri.parse("android-app://victorvs.com.rpsutil2/http/host/path")
        );
        AppIndex.AppIndexApi.start(client, viewAction);
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Main Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app URL is correct.
                Uri.parse("android-app://victorvs.com.rpsutil2/http/host/path")
        );
        AppIndex.AppIndexApi.end(client, viewAction);
        client.disconnect();
    }
}
