package victorvs.com.rpsutil2;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by danie on 30/06/2016.
 */
public class MainController{
    static String BD_NAME = "base9";

    public static String consultaBoleta(Context context,String n_boleta) {
        String datos = "";
        CursorSQLHelper db = new CursorSQLHelper(context,
                BD_NAME, null, 1);
        SQLiteDatabase bd = db.getWritableDatabase();
        Cursor fila = bd.rawQuery(
                "SELECT id,boleta,valor,nula,tipo_turno,fecha_turno FROM Boleta WHERE boleta='" + n_boleta + "'", null);

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
            Toast.makeText(context, "No existe una boleta con dicho código",
                    Toast.LENGTH_SHORT).show();
        }
        bd.close();
        return datos;
    }

    public static ArrayList<String> getTurnobyPK(Context context, String fecha_turno, String tipo_turno){
        ArrayList<String> datos_turno=new ArrayList<>();
        CursorSQLHelper db = new CursorSQLHelper(context,BD_NAME, null, 1);
        SQLiteDatabase bd = db.getWritableDatabase();
        Cursor c = bd.rawQuery(
                "SELECT fecha,tipo,valor_total,cantidad_boletas,valor_total_nulas,cantidad_boletas_nulas FROM Turno WHERE fecha='"+fecha_turno+
                        "' AND tipo='"+tipo_turno+"' ORDER BY id DESC LIMIT 1", null);

        if (c.moveToFirst()) {
            String fecha = c.getString(0);
            String tipo = c.getString(1);
            String valor_total = String.valueOf(c.getInt(2));
            String cantidad_boletas = String.valueOf(c.getInt(3));
            String valor_total_nulas = String.valueOf(c.getInt(4));
            String cantidad_boletas_nulas = String.valueOf(c.getInt(5));
            datos_turno.add(0,fecha);
            datos_turno.add(1,tipo);
            datos_turno.add(2,valor_total);
            datos_turno.add(3,cantidad_boletas);
            datos_turno.add(4,valor_total_nulas);
            datos_turno.add(5,cantidad_boletas_nulas);
        }
        db.close();
        return datos_turno;
    }

    public static ArrayList<String> getUltimoTurnoActivo(Context context){
        ArrayList<String> datos_turno=new ArrayList<>();
        CursorSQLHelper db = new CursorSQLHelper(context,BD_NAME, null, 1);
        SQLiteDatabase bd = db.getWritableDatabase();
        Cursor c = bd.rawQuery(
                "SELECT fecha,tipo,valor_total,cantidad_boletas FROM Turno WHERE estado=1 ORDER BY id DESC LIMIT 1", null);

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

    public static void actualizarTurno(Context context,String tipo, String fecha,int valor_total,int valor_boletas_nulas,int cantidad_boletas,int cantidad_boletas_nulas){
        ContentValues values=new ContentValues();
        values.put("valor_total",valor_total);
        values.put("valor_total_nulas",valor_boletas_nulas);

        values.put("cantidad_boletas",cantidad_boletas);
        values.put("cantidad_boletas_nulas",cantidad_boletas_nulas);


        String where="tipo=? AND fecha=?";
        String[] whereArgs={tipo,fecha};
        CursorSQLHelper ch=new  CursorSQLHelper(context, BD_NAME, null, 1);
        SQLiteDatabase db=ch.getWritableDatabase();
        int nColumnasActualizadas=db.update("Turno",values,where,whereArgs);
        Log.d("N° columnas act: ",String.valueOf(nColumnasActualizadas));

        db.close();
    }

    public static void insertarBoleta(Context context,String boleta, String valor, int nula, String turno, String fecha) {
        CursorSQLHelper ch = new CursorSQLHelper(context, BD_NAME, null, 1);
        SQLiteDatabase db = ch.getWritableDatabase();
        try {
            ContentValues c = new ContentValues();
            c.put("boleta", boleta);
            c.put("valor", valor);
            c.put("nula", nula);
            c.put("tipo_turno", turno);
            c.put("fecha_turno", fecha);
            db.insert("Boleta", null, c);


        } catch (Exception e) {
            Log.d("insertarBoleta","Error");
        }
        db.close();
    }

    public static void insertarTurno(Context context, String fecha, String tipo, int estado) {
        CursorSQLHelper ch = new CursorSQLHelper(context, BD_NAME, null, 1);
        SQLiteDatabase db = ch.getWritableDatabase();

        ArrayList<String> datos_turno = getTurnobyPK(context,fecha,tipo);

        if(!datos_turno.isEmpty()){
            return;
        }
        try {
            ContentValues c = new ContentValues();
            c.put("fecha", fecha);
            c.put("tipo", tipo);
            c.put("valor_total", 0);
            c.put("cantidad_boletas", 0);
            c.put("valor_total_nulas", 0);
            c.put("cantidad_boletas_nulas", 0);
            c.put("estado",estado);

            db.insert("Turno", null, c);


        } catch (Exception e) {
            Log.d("insertarTurno","Error");
        }
        db.close();
    }

    public static int valorTotal(Context context,int nula,String tipo_turno,String fecha_turno) {
        CursorSQLHelper db = new CursorSQLHelper(context,
                BD_NAME, null, 1);
        SQLiteDatabase bd = db.getWritableDatabase();
        Cursor fila = bd.rawQuery(
                "SELECT SUM(valor) FROM Boleta WHERE nula='" + nula + "' AND tipo_turno='"
                        +tipo_turno+"' AND fecha_turno='"+fecha_turno+"'", null);

        int datos=0;
        if (fila.moveToFirst()) {
            datos = fila.getInt(0);
        }
        bd.close();
        return datos;
    }

    public static int cantidadBoletas(Context context,String tipo,String fecha) {
        int datos = 0;
        CursorSQLHelper db = new CursorSQLHelper(context,
                BD_NAME, null, 1);
        SQLiteDatabase bd = db.getWritableDatabase();
        Cursor fila = bd.rawQuery(
                "SELECT COUNT(DISTINCT boleta) FROM Boleta WHERE tipo_turno='"+tipo+"' AND fecha_turno='"+fecha+"'", null);

        if (fila.moveToFirst()) {
            datos = fila.getInt(0);
        }
        bd.close();
        return datos;
    }

    public static int cantidadBoletasNulas(Context context,String tipo,String fecha) {
        int datos = 0;
        CursorSQLHelper db = new CursorSQLHelper(context,
                BD_NAME, null, 1);
        SQLiteDatabase bd = db.getWritableDatabase();
        Cursor fila = bd.rawQuery(
                "SELECT COUNT(DISTINCT boleta) FROM Boleta WHERE tipo_turno='"+tipo+"' AND fecha_turno='"+fecha+"' AND nula=1", null);

        if (fila.moveToFirst()) {
            datos = fila.getInt(0);
        }
        bd.close();
        return datos;
    }

    public static String numeroBoletaFinal(Context context,String fecha,String tipo) {
        String datos = "";
        CursorSQLHelper db = new CursorSQLHelper(context,
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

    public static String numeroBoletaActual(Context context,String fecha,String tipo) {
        int dato = 0;
        CursorSQLHelper db = new CursorSQLHelper(context,
                BD_NAME, null, 1);
        SQLiteDatabase bd = db.getWritableDatabase();
        Cursor fila = bd.rawQuery(
                "SELECT boleta FROM Boleta WHERE fecha_turno='"+fecha+"' AND tipo_turno='"+tipo+"' ORDER BY id DESC LIMIT 1", null);

        if (fila.moveToFirst()) {
            dato = fila.getInt(0);
        }
        dato++;
        String datos=String.valueOf(dato);
        bd.close();
        return datos;
    }

    public static void eliminarUltimaBoleta(Context context,String tipo,String fecha) {
        String valor="";
        CursorSQLHelper db = new CursorSQLHelper(context,
                BD_NAME, null, 1);
        SQLiteDatabase bd = db.getWritableDatabase();
        Cursor fila = bd.rawQuery(
                "SELECT boleta FROM Boleta WHERE fecha_turno='"+fecha+"' AND tipo_turno='"+tipo+"'ORDER BY id DESC LIMIT 1", null);

        if (fila.moveToFirst()) {
            valor = fila.getString(0);
            Log.d("Boleta eliminada:",valor);
        }
        bd.execSQL("DELETE FROM Boleta WHERE boleta=" +valor);
        bd.close();
    }
}
