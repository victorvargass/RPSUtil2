package victorvs.com.rpsutil2;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Victor Vargas on 5/27/2016.
 */
public class CursorSQLHelper extends SQLiteOpenHelper{
    String sqlTablaBoleta="CREATE TABLE Boleta(" +
            "id integer primary key AUTOINCREMENT," +
            "boleta text," +
            "valor text," +
            "nula integer," +
            "turno text," +
            "fecha text)";
    String sqlTablaTurno="CREATE TABLE Turno(" +
            "fecha text," +
            "tipo text" +
            "valor_total integer" +
            "cantidad_boletas" +
            "estado text";

    public CursorSQLHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(sqlTablaBoleta);
        db.execSQL(sqlTablaTurno);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE Boleta");
        db.execSQL(sqlTablaBoleta);
        //holi fskjdfhadskff imbecil
    }
}
