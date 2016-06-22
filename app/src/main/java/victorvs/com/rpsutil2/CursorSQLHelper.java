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
            "valor integer," +
            "nula integer," +
            "tipo_turno text ," +
            "fecha_turno text," +
            "foreign key (tipo_turno) references Turno(tipo)," +
            "foreign key (fecha_turno) references Turno(fecha))";

    String sqlTablaTurno="CREATE TABLE Turno ("+
            "id integer primary key AUTOINCREMENT,"+
            "fecha text," +
            "tipo text," +
            "valor_total integer," +
            "cantidad_boletas integer," +
            "estado integer)";

    public CursorSQLHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(sqlTablaTurno);
        db.execSQL(sqlTablaBoleta);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE Boleta");
        db.execSQL("DROP TABLE Turno");
        db.execSQL(sqlTablaBoleta);
        db.execSQL(sqlTablaTurno);

    }
}
