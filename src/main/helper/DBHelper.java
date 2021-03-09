package main.helper;

import java.util.ArrayList;
import java.util.List;

import main.model.Curso;
import main.model.Grupo;
import main.model.Materia;
import main.model.Trabajo;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper
{
    // All Static variables
    // Database Version
    private static final int DATABASE_VERSION = 2;

    // Database Name
    private static final String DATABASE_NAME = "mydb";

    // Cursos table name
    private static final String TABLE_CURSOS = "cursos";

    // Grupos table name
    private static final String TABLE_GRUPOS = "grupos";

    // Trabajos table name
    private static final String TABLE_TRABAJOS = "Trabajos";

    // Materias table name
    private static final String TABLE_MATERIAS = "Materias";

    // Common Table Columns names
    private static final String KEY_ID = "id";

    // Cursos Table Columns names
    private static final String KEY_ANIO = "anio";
    private static final String KEY_CUATRI = "cuatrimestre";
    private static final String KEY_LETRA = "letra";

    // Grupos Table Columns names
    private static final String KEY_NUMERO = "numero";
    //Tiene materia
    
    // Trabajos Table Columns names
    private static final String KEY_NOMBRE = "id_trabajo";
    private static final String KEY_ID_GRUPO = "id_grupo";
    private static final String KEY_ESTADO = "estado";
    
    // Materias Table Columns names
    private static final String KEY_MATERIA = "materia";
    private static final String KEY_CANT_GRUPOS= "cantGrupos";
    private static final String KEY_ID_CURSO= "id_curso";
    private static final String KEY_SELECTED= "selected";
   
    

    private static final int CANT_GRUPOS_POR_CURSO = 4;
    private static final int CANT_TRABAJOS_POR_GRUPO = 7;

    private static String CREATE_TABLE_CURSOS = "CREATE TABLE " + TABLE_CURSOS + "(" + KEY_ID + " INTEGER PRIMARY KEY," + KEY_ANIO + " TEXT," + KEY_CUATRI + " TEXT," + KEY_LETRA + " TEXT" + ")";

    private static String CREATE_TABLE_GRUPOS = "CREATE TABLE " + TABLE_GRUPOS + "(" + KEY_ID + " INTEGER PRIMARY KEY," + KEY_MATERIA + " INTEGER," + KEY_NUMERO + " TEXT" + ")";

    private static String CREATE_TABLE_TRABAJOS = "CREATE TABLE " + TABLE_TRABAJOS + "(" + KEY_ID + " INTEGER PRIMARY KEY," + KEY_ID_GRUPO + " INTEGER," + KEY_NOMBRE + " TEXT," + KEY_ESTADO + " TEXT" + ")";
    
    private static String CREATE_TABLE_MATERIAS = "CREATE TABLE " + TABLE_MATERIAS + "(" + KEY_ID + " INTEGER PRIMARY KEY," + KEY_ID_CURSO + " TEXT, " +KEY_MATERIA + " TEXT," +  KEY_CANT_GRUPOS + " INTEGER, " + KEY_SELECTED +  " INTEGER " +")";

    public DBHelper(Context context)
    {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db)
    {
        db.execSQL(CREATE_TABLE_CURSOS);
        db.execSQL(CREATE_TABLE_GRUPOS);
        db.execSQL(CREATE_TABLE_TRABAJOS);
        db.execSQL(CREATE_TABLE_MATERIAS);
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CURSOS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_GRUPOS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TRABAJOS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_MATERIAS);

        // Create tables again
        onCreate(db);
    }

    /**
     * All CRUD(Create, Read, Update, Delete) Operations for TRABAJOS
     */

    public void addTrabajo(Trabajo trabajo)
    {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_ID_GRUPO, trabajo.get_id_grupo());
        values.put(KEY_NOMBRE, trabajo.get_nombre());
        values.put(KEY_ESTADO, trabajo.get_estado());
        db.insert(TABLE_TRABAJOS, null, values);
        db.close();

    }

    public Trabajo getTrabajoById(String id)
    {
        SQLiteDatabase db = this.getReadableDatabase();
        Trabajo trabajo = new Trabajo();

        Cursor cursor = db.query(TABLE_TRABAJOS, new String[] { KEY_ID, KEY_ID_GRUPO, KEY_NOMBRE, KEY_ESTADO }, KEY_ID + "=?", new String[] { String.valueOf(id) }, null, null, null, null);
        if (cursor.moveToFirst())
        {
            trabajo.set_id(Integer.parseInt(cursor.getString(0)));
            trabajo.set_id_grupo(Integer.parseInt(cursor.getString(1)));
            trabajo.set_nombre(cursor.getString(2));
            trabajo.set_estado(cursor.getString(3));
            db.close();
        }
        else
            trabajo = null;
        // return Curso
        return trabajo;

    }

    public Trabajo findTrabajoByIdGrupoNombre(int id_grupo, String nombre)
    {
        SQLiteDatabase db = this.getReadableDatabase();
        Trabajo trabajo = null;

        String[] select = new String[] { KEY_ID, KEY_ID_GRUPO, KEY_NOMBRE, KEY_ESTADO };
        String where = String.format("%s=? AND %s=?", KEY_ID_GRUPO, KEY_NOMBRE);
        String[] whereArgs = new String[] { String.valueOf(id_grupo), nombre };

        Cursor cursor = null;
        cursor = db.query(TABLE_TRABAJOS, select, where, whereArgs, null, null, null, null);

        if (cursor != null)
        {
            cursor.moveToFirst();
            if (cursor.getCount() > 0)
            {
                trabajo = new Trabajo(Integer.parseInt(cursor.getString(0)), Integer.parseInt(cursor.getString(1)), cursor.getString(2), cursor.getString(3));
            }
        }
        return trabajo;
    }

    public List<Trabajo> findTrabajosByIdGrupo(String id_grupo)
    {
        SQLiteDatabase db = this.getReadableDatabase();
        List<Trabajo> trabajos = new ArrayList<Trabajo>();

        Cursor cursor = db.query(TABLE_TRABAJOS, new String[] { KEY_ID, KEY_ID_GRUPO, KEY_NOMBRE, KEY_ESTADO }, KEY_ID_GRUPO + "=?", new String[] { String.valueOf(id_grupo) }, null, null, null, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst())
        {
            do
            {
                Trabajo trabajo = new Trabajo();
                trabajo.set_id(Integer.parseInt(cursor.getString(0)));
                trabajo.set_id_grupo(Integer.parseInt(cursor.getString(1)));
                trabajo.set_nombre(cursor.getString(2));
                trabajo.set_estado(cursor.getString(3));
                // Adding Curso to list
                trabajos.add(trabajo);
            }
            while (cursor.moveToNext());
        }
        db.close();
        // return Trabajo list
        return trabajos;
    }

    public int updateTrabajo(Trabajo trabajo)
    {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_ESTADO, trabajo.get_estado());

        // updating row
        return db.update(TABLE_TRABAJOS, values, KEY_ID + " = ?", new String[] { String.valueOf(trabajo.get_id()) });

    }

    public void deleteTabajo(Trabajo trabajo)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_TRABAJOS, KEY_ID + " = ?", new String[] { String.valueOf(trabajo.get_id()) });
        db.close();
    }

    /**
     * All CRUD(Create, Read, Update, Delete) Operations for CURSOS
     */

    // Adding new Curso
    public void addCurso(Curso curso)
    {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_ANIO, curso.get_anio());
        values.put(KEY_CUATRI, curso.get_cuatrimestre());
        values.put(KEY_LETRA, curso.get_letra());

        // Inserting Row
        db.insert(TABLE_CURSOS, null, values);

        Curso cursoCreado = findCursoByAnioCuatriLetra(curso.get_anio(), curso.get_cuatrimestre(), curso.get_letra());

        // si el curso se creo con exito, le asigno una cantidad predeterminada
        // de grupos
        if (cursoCreado != null)
        {
        	List<String> MateriasCurso = getNombreMateriasPorCurso(curso.get_anio(),curso.get_cuatrimestre());
        	
        	for (String materia : MateriasCurso) {
				Materia mat = new Materia();
				mat.setGrupos(CANT_GRUPOS_POR_CURSO);
				mat.setId_curso(cursoCreado.get_id());
				mat.setNombre(materia.toString());
				addMateria(mat);
			}
        }
        db.close(); // Closing database connection
    }

    // Getting single Curso
    public Curso findCursoById(int id)
    {
        SQLiteDatabase db = this.getReadableDatabase();
        Curso curso = null;

        Cursor cursor = db.query(TABLE_CURSOS, new String[] { KEY_ID, KEY_ANIO, KEY_CUATRI, KEY_LETRA }, KEY_ID + "=?", new String[] { String.valueOf(id) }, null, null, null, null);
        if (cursor != null)
        {
            cursor.moveToFirst();
            if (cursor.getCount() > 0)
            {
                curso = new Curso(Integer.parseInt(cursor.getString(0)), cursor.getString(1), cursor.getString(2), cursor.getString(3));
            }
        }
        // return Curso
        return curso;
    }

    // Getting All Cursos
    public List<Curso> getAllCursos()
    {
        List<Curso> CursoList = new ArrayList<Curso>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_CURSOS;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst())
        {
            do
            {
                Curso Curso = new Curso();
                Curso.set_id(Integer.parseInt(cursor.getString(0)));
                Curso.set_anio(cursor.getString(1));
                Curso.set_cuatrimestre(cursor.getString(2));
                Curso.set_letra(cursor.getString(3));
                // Adding Curso to list
                CursoList.add(Curso);
            }
            while (cursor.moveToNext());
        }

        // return Curso list
        return CursoList;
    }

    // Updating single Curso
    public int updateCurso(Curso Curso)
    {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_ANIO, Curso.get_anio());
        values.put(KEY_CUATRI, Curso.get_cuatrimestre());
        values.put(KEY_LETRA, Curso.get_letra());

        // updating row
        return db.update(TABLE_CURSOS, values, KEY_ID + " = ?", new String[] { String.valueOf(Curso.get_id()) });
    }

    // Deleting single Curso
    public void deleteCurso(Curso curso)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        List<Materia> ListaMaterias = new ArrayList<Materia>();
        
        db.delete(TABLE_CURSOS, KEY_ID + " = ?", new String[] { String.valueOf(curso.get_id()) });
        
        ListaMaterias = findMateriasByIdCurso(curso.get_id());
        for (Materia materia : ListaMaterias) {
			deleteMateria(materia);
		}
        db.close();
    }

    // Getting Cursos Count
    public int getCursosCount()
    {
        String countQuery = "SELECT * FROM " + TABLE_CURSOS;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        cursor.moveToFirst();
        cursor.close();

        // return count
        return cursor.getCount();
    }

    public Curso findCursoByAnioCuatriLetra(String anio, String cuatri, String letra)
    {
        Curso curso = null;

        SQLiteDatabase db = this.getReadableDatabase();

        String[] select = new String[] { KEY_ID, KEY_ANIO, KEY_CUATRI, KEY_LETRA };
        String where = String.format("%s=? AND %s=? AND %s=?", KEY_ANIO, KEY_CUATRI, KEY_LETRA);
        String[] whereArgs = new String[] { anio, cuatri, letra };

        Cursor cursor = null;
        cursor = db.query(TABLE_CURSOS, select, where, whereArgs, null, null, null, null);

        if (cursor != null)
        {
            cursor.moveToFirst();
            if (cursor.getCount() > 0)
            {
                curso = new Curso(Integer.parseInt(cursor.getString(0)), cursor.getString(1), cursor.getString(2), cursor.getString(3));
            }
        }
        cursor.close();
        //
        return curso;
    }

    
    public List<String> getNombreMateriasPorCurso(String anio, String cuatrimestre)
    {
    	List<String> ListaMateras = new ArrayList<String>();
    	if (anio.equals("1° Año") && cuatrimestre.equals("1° Cuatrimestre"))
    	{
    		ListaMateras.add("Taller 1");
    		ListaMateras.add("Comunicaciones Efectivas");
    		ListaMateras.add("Algoritmos");
    		ListaMateras.add("Ingles 1");
    		ListaMateras.add("Matematica");
    		ListaMateras.add("Introduccion a la Informatica");
    		ListaMateras.add("Organizacion Empresarial");
    	}
    	else if(anio.equals("1° Año") && cuatrimestre.equals("2° Cuatrimestre"))
    	{
    		ListaMateras.add("Sistemas Operativos")  ;
    		ListaMateras.add("Sistemas Administrativos");
    		ListaMateras.add("Taller 2");
    		ListaMateras.add("Estructura de Datos");
    		ListaMateras.add("Estudios Judaicos 1");
    		ListaMateras.add("Ingles 2");
    		ListaMateras.add("Estadistica");
    	}
    	else if(anio.equals("2° Año") && cuatrimestre.equals("1° Cuatrimestre"))
    	{
    		ListaMateras.add("TAD");
    		ListaMateras.add("Base de Datos");
    		ListaMateras.add("Modelo de Negocios");
    		ListaMateras.add("Programacion orientada a objetos");
    		ListaMateras.add("Etica");
    		ListaMateras.add("Taller 3");
    	}
    	else if(anio.equals("2° Año") && cuatrimestre.equals("2° Cuatrimestre"))
    	{
    		ListaMateras.add("Analisis y Metodologias de Sistemas");
    		ListaMateras.add("Taller 4");
    		ListaMateras.add("Simulacion");
    		ListaMateras.add("Judaicas 2");
    		ListaMateras.add("Redes");
    		ListaMateras.add("Integracion de Programacion");	
    	}
    	else if(anio.equals("3° Año") && cuatrimestre.equals("1° Cuatrimestre"))
    	{
    		ListaMateras.add("Taller de Sistemas");
    		ListaMateras.add("Taller 5");
    		ListaMateras.add("Integracion de Tecnologia");
    		ListaMateras.add("Analisis de Proyectos");
    		ListaMateras.add("Orientacion Profesional");
    		ListaMateras.add("Diseño de Sistemas");	
    	}
    	else if(anio.equals("3° Año") && cuatrimestre.equals("2° Cuatrimestre"))
    	{
    		ListaMateras.add("Seminario de Sistemas");
    		ListaMateras.add("Taller 6");
    		ListaMateras.add("Seguridad e Integridad de Sistemas");
    		ListaMateras.add("Calidad de Software");
    		ListaMateras.add("Computacion Avanzada");
    	}
    	return ListaMateras;
    }
    /**
     * All CRUD(Create, Read, Update, Delete) Operations for GRUPOS
     */

    // Adding new Grupo
    public void addGrupo(Grupo grupo)
    {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_MATERIA, grupo.get_id_materia());
        values.put(KEY_NUMERO, grupo.get_numero());
        
        // Inserting Row
        db.insert(TABLE_GRUPOS, null, values);

        Grupo grupoCreado = findGrupoByIdMateriaNumero(grupo.get_id_materia(), grupo.get_numero());

        if (grupoCreado != null)
        {
            for (int i = 1; i <= CANT_TRABAJOS_POR_GRUPO; i++)
            {
                Trabajo trabajo = new Trabajo(grupoCreado.get_id(), "TP " + i, "Pendiente");
                this.addTrabajo(trabajo);
            }
        }

        db.close(); // Closing database connection
    }

    // Getting single Grupo
    public Grupo findGrupoById(int id)
    {
        SQLiteDatabase db = this.getReadableDatabase();
        Grupo grupo = null;

        Cursor cursor = db.query(TABLE_GRUPOS, new String[] { KEY_ID, KEY_MATERIA, KEY_NUMERO}, KEY_ID + "=?", new String[] { String.valueOf(id) }, null, null, null, null);
        if (cursor != null)
        {
            cursor.moveToFirst();
            if (cursor.getCount() > 0)
            {
                grupo = new Grupo(Integer.parseInt(cursor.getString(0)), Integer.parseInt(cursor.getString(1)), cursor.getString(2));
            }
        }
        // return Grupo
        return grupo;
    }

    public Grupo findGrupoByIdMateriaNumero(int id_materia, String numero)
    {
        SQLiteDatabase db = this.getReadableDatabase();
        Grupo grupo = null;

        String[] select = new String[] { KEY_ID, KEY_MATERIA, KEY_NUMERO};
        String where = String.format("%s=? AND %s=?", KEY_MATERIA, KEY_NUMERO);
        String[] whereArgs = new String[] { String.valueOf(id_materia), numero };

        Cursor cursor = null;
        cursor = db.query(TABLE_GRUPOS, select, where, whereArgs, null, null, null, null);

        if (cursor != null)
        {
            cursor.moveToFirst();
            if (cursor.getCount() > 0)
            {
                grupo = new Grupo(Integer.parseInt(cursor.getString(0)), Integer.parseInt(cursor.getString(1)), cursor.getString(2));
            }
        }
        // return Grupo
        return grupo;
    }

    public int findUltimoGrupoByIdMateriaGrupo(int id_materia)
    {
        SQLiteDatabase db = this.getReadableDatabase();
        int numeroGrupo =0;
        //String Query = "SELECT * FROM " + TABLE_GRUPOS + " WHERE " + KEY_NUMERO + " = (SELECT max(" + KEY_NUMERO + ") FROM " + TABLE_GRUPOS + ") AND " + KEY_MATERIA + " = " + id_materia;
    	
        String Query1 ="SELECT max(" + KEY_NUMERO + ") FROM " + TABLE_GRUPOS + " WHERE " + KEY_MATERIA + " = " + id_materia;
        Cursor cursor = db.rawQuery(Query1, null);
    	 
    	if (cursor != null)
        {
            cursor.moveToFirst();
            if (cursor.getCount() > 0)
            {
               numeroGrupo = Integer.parseInt(cursor.getString(0));
            }
        }
    			  	
    	return numeroGrupo;
    }
    
    // Getting All Grupos
    public List<Grupo> getAllGrupos()
    {
        List<Grupo> GrupoList = new ArrayList<Grupo>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_GRUPOS;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst())
        {
            do
            {
                Grupo Grupo = new Grupo();
                Grupo.set_id(Integer.parseInt(cursor.getString(0)));
                Grupo.set_id_curso(Integer.parseInt(cursor.getString(1)));
                Grupo.set_numero(cursor.getString(2));

                // Adding Grupo to list
                GrupoList.add(Grupo);
            }
            while (cursor.moveToNext());
        }

        // return Grupo list
        return GrupoList;
    }

    // Updating single Grupo
    public int updateGrupo(Grupo grupo)
    {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_MATERIA, grupo.get_id_materia());
        values.put(KEY_NUMERO, grupo.get_numero());

        // updating row
        return db.update(TABLE_GRUPOS, values, KEY_ID + " = ?", new String[] { String.valueOf(grupo.get_id()) });
    }

    // Deleting single Grupo
    public void deleteGrupo(Grupo grupo)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        List<Trabajo> TrabajosList = new ArrayList<Trabajo>();
        
        db.delete(TABLE_GRUPOS, KEY_ID + " = ?", new String[] { String.valueOf(grupo.get_id()) });
        TrabajosList= findTrabajosByIdGrupo(grupo.get_id());
        for (Trabajo trabajo :TrabajosList) {
			deleteTabajo(trabajo);
		}
        db.close();
    }

    public void regenerateDB()
    {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CURSOS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_GRUPOS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TRABAJOS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_MATERIAS);
        onCreate(db);
        db.close();
    }

    // Getting Grupos Count
    public int getGruposCount()
    {
        String countQuery = "SELECT * FROM " + TABLE_GRUPOS;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        cursor.moveToFirst();
        cursor.close();

        // return count
        return cursor.getCount();
    }

  /*  public List<Grupo> findGruposByIdMateria(int id_mmateria)
    {
        List<Grupo> GrupoList = new ArrayList<Grupo>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_GRUPOS, new String[] { KEY_ID, KEY_MATERIA, KEY_NUMERO }, KEY_MATERIA + "=?", new String[] { String.valueOf(id_curso) }, null, null, null, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst())
        {
            do
            {
                Grupo Grupo = new Grupo();
                Grupo.set_id(Integer.parseInt(cursor.getString(0)));
                Grupo.set_id_curso(Integer.parseInt(cursor.getString(1)));
                Grupo.set_numero(cursor.getString(2));

                // Adding Grupo to list
                GrupoList.add(Grupo);
            }
            while (cursor.moveToNext());
        }

        // return Grupo list
        return GrupoList;
    }*/

    public List<Grupo> findGruposByIdMateria(int id_materia)
    {
        List<Grupo> GrupoList = new ArrayList<Grupo>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_GRUPOS, new String[] { KEY_ID, KEY_MATERIA, KEY_NUMERO }, KEY_MATERIA + "=?", new String[] { String.valueOf(id_materia) }, null, null, null, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst())
        {
            do
            {
                Grupo Grupo = new Grupo();
                Grupo.set_id(Integer.parseInt(cursor.getString(0)));
                Grupo.set_id_curso(Integer.parseInt(cursor.getString(1)));
                Grupo.set_numero(cursor.getString(2));

                // Adding Grupo to list
                GrupoList.add(Grupo);
            }
            while (cursor.moveToNext());
        }

        // return Grupo list
        return GrupoList;
    }
    
    public List<Trabajo> findTrabajosByIdGrupo(int id_grupo)
    {
        List<Trabajo> trabajos = new ArrayList<Trabajo>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_TRABAJOS, new String[] { KEY_ID, KEY_ID_GRUPO, KEY_NOMBRE, KEY_ESTADO }, KEY_ID_GRUPO + "=?", new String[] { String.valueOf(id_grupo) }, null, null, null, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst())
        {
            do
            {
                Trabajo trabajo = new Trabajo();
                trabajo.set_id(Integer.parseInt(cursor.getString(0)));
                trabajo.set_id_grupo(Integer.parseInt(cursor.getString(1)));
                trabajo.set_nombre(cursor.getString(2));
                trabajo.set_estado(cursor.getString(3));

                // Adding Grupo to list
                trabajos.add(trabajo);
            }
            while (cursor.moveToNext());
        }
        return trabajos;
    }

    /**
     * All CRUD(Create, Read, Update, Delete) Operations for MATERIAS
     */
    
    public void addMateria(Materia materia)
    {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_MATERIA, materia.getNombre());
        values.put(KEY_ID_CURSO, materia.getId_curso());
        values.put(KEY_CANT_GRUPOS, materia.getGrupos());
        values.put(KEY_SELECTED, materia.getSelected()); //FDB

        // Me fijo si la materia existe.
        Materia materiaCreada = findMateriaByIdCursoNombre(materia.getId_curso(), materia.getNombre());
        

        // si la materia no existe la creo, le asigno una cantidad predeterminada de grupos (7)
        if (materiaCreada == null)
        {
        	db.insert(TABLE_MATERIAS, null, values);
        	materiaCreada = findMateriaByIdCursoNombre(materia.getId_curso(), materia.getNombre());
            int id_Materia = materiaCreada.getId();

            for (int i = 1; i <= materia.getGrupos(); i++)
            {
                Grupo grupo = new Grupo(id_Materia, String.valueOf(i));
                addGrupo(grupo);
            }
        }
        
        db.close(); // Closing database connection
    }

    // Getting single Materia
    public Materia findMateriaById(int id)
    {
        SQLiteDatabase db = this.getReadableDatabase();
        Materia materia = null;

        Cursor cursor = db.query(TABLE_MATERIAS, new String[] { KEY_ID, KEY_ID_CURSO, KEY_MATERIA,KEY_CANT_GRUPOS}, KEY_ID + "=?", new String[] { String.valueOf(id) }, null, null, null, null);
        if (cursor != null)
        {
            cursor.moveToFirst();
            if (cursor.getCount() > 0)
            {
                materia = new Materia(Integer.parseInt(cursor.getString(0)), Integer.parseInt(cursor.getString(1)), cursor.getString(2), Integer.parseInt(cursor.getString(3)));
            }
        }
        // return Curso
        return materia;
    }

    public List<Materia>findMateriasByIdCurso(int id_curso){
    	
    	SQLiteDatabase db = this.getReadableDatabase();
        List<Materia> MateriaList = new ArrayList<Materia>();

        Cursor cursor = db.query(TABLE_MATERIAS, new String[] { KEY_ID, KEY_ID_CURSO, KEY_MATERIA,KEY_CANT_GRUPOS,KEY_SELECTED}, KEY_ID_CURSO + "=?", new String[] { String.valueOf(id_curso) }, null, null, null, null);
        //String query = "SELECT * FROM " + TABLE_MATERIAS;// + " WHERE " + KEY_ID_CURSO + " = " + id_curso;
        //Cursor cursor = db.rawQuery(query, null);
        
        if (cursor.moveToFirst())
        {
        	do{
        		Materia materia = new Materia();
        		materia.setId(Integer.parseInt(cursor.getString(0)));
        		materia.setId_curso(Integer.parseInt(cursor.getString(1)));
        		materia.setNombre(cursor.getString(2));
        		materia.setGrupos(Integer.parseInt(cursor.getString(3)));
        		if(cursor.getString(4)=="null")
        			materia.setSelected(0);
        		else
        			materia.setSelected(Integer.parseInt(cursor.getString(4)));
                
                // Adding Materia to list
                MateriaList.add(materia);	
        	}while(cursor.moveToNext());
            cursor.moveToFirst();
       }
        return MateriaList;
    	
    }

    public Materia findMateriaByIdCursoNombre(int id_curso, String nombre){
    	
    	SQLiteDatabase db = this.getReadableDatabase();
        Materia materia = null;
        String[] select = new String[] { KEY_ID, KEY_ID_CURSO, KEY_MATERIA,KEY_CANT_GRUPOS, KEY_SELECTED };
        String where = String.format("%s=? AND %s=?", KEY_ID_CURSO, KEY_MATERIA);
        String[] whereArgs = new String[] { String.valueOf(id_curso), nombre };

        Cursor cursor = null;
        cursor = db.query(TABLE_MATERIAS, select, where, whereArgs, null, null, null, null);

        if (cursor != null)
        {
            cursor.moveToFirst();
            if (cursor.getCount() > 0)
            {
               materia = new Materia(Integer.parseInt(cursor.getString(0)), Integer.parseInt(cursor.getString(1)), cursor.getString(2),Integer.parseInt(cursor.getString(3)),Integer.parseInt(cursor.getString(4)));//FDB - agrego si está seleccionada
            }
        }
        // return Materia
        return materia;
    	
    }
    
    //FDB - Devuelve la lista de Materias seleccionadas por el profesor
    public Cursor findMateriasSelected()
    {
    	SQLiteDatabase db = this.getReadableDatabase();
       
    	//Creamos el cursor
    	
    	//Opción 1
    	String select = String.format("select %s.id AS _id, %s,%s,%s,%s from %s", TABLE_MATERIAS, KEY_MATERIA, KEY_ANIO,KEY_CUATRI,KEY_LETRA, TABLE_MATERIAS);
    	String join = String.format(" left join %s on %s=%s.%s ", TABLE_CURSOS, KEY_ID_CURSO, TABLE_CURSOS, KEY_ID);
    	String where = String.format("where %s=%s", KEY_SELECTED, String.valueOf(1));
    	
    	Cursor cursor = db.rawQuery(select + join + where, null);
    	
    	//Opcion 2 (La 1 no anduvo)
    	/*String select = String.format("select %s.id AS _id, %s,%s,%s,%s from %s, %s", TABLE_MATERIAS, KEY_MATERIA, KEY_ANIO,KEY_CUATRI,KEY_LETRA, TABLE_MATERIAS, TABLE_CURSOS);
    	String where = String.format(" where %s=%s and %s.%s = %s.%s", KEY_SELECTED, String.valueOf(1), TABLE_MATERIAS, KEY_ID_CURSO, TABLE_CURSOS, KEY_ID);
    	
    	Cursor cursor = db.rawQuery(select + where, null);*/
    	
    	//Opcion 3
    	
    	/*String select = String.format("select id AS _id, %s from %s", KEY_MATERIA, TABLE_MATERIAS);
    	String where = String.format(" where %s=%s ", KEY_SELECTED, String.valueOf(1));
    	
    	Cursor cursor = db.rawQuery(select + where, null);*/
    	
    	//Creamos el adaptador
    	
        return cursor;
    	
    }
    
    
 // Getting All Materias
    public List<Materia> getAllMaterias()
    {
        List<Materia> MateriaList = new ArrayList<Materia>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_MATERIAS;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst())
        {
            do
            {
                Materia materia = new Materia();
                materia.setId(Integer.parseInt(cursor.getString(0)));
                materia.setId_curso(Integer.parseInt(cursor.getString(1)));
                materia.setNombre(cursor.getString(2));
                materia.setGrupos(Integer.parseInt(cursor.getString(3)));
                materia.setSelected(Integer.parseInt(cursor.getString(4)));//FDB TODO: Setear si está seleccionada la materia
                // Adding Curso to list
                MateriaList.add(materia);
            }
            while (cursor.moveToNext());
        }

        // return Curso list
        return MateriaList;
    }

    // Updating single Materia
    public int updateMateria(Materia materia)
    {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_MATERIA, materia.getNombre());
        values.put(KEY_CANT_GRUPOS, materia.getGrupos());
        values.put(KEY_ID_CURSO, materia.getId_curso());
        values.put(KEY_SELECTED, materia.getSelected()); //FDB - actualiza si está seleccionada la materia

        // updating row
        return db.update(TABLE_MATERIAS, values, KEY_ID + " = ?", new String[] { String.valueOf(materia.getId()) });
    }
    
    
    // Deleting single Materia
    public void deleteMateria(Materia materia)
    {
        SQLiteDatabase db = this.getWritableDatabase();
                
        db.delete(TABLE_MATERIAS, KEY_ID + " = ?", new String[] { String.valueOf(materia.getId()) });
        
        List<Grupo> GrupoList = new ArrayList<Grupo>();
        GrupoList = findGruposByIdMateria(materia.getId());
        for (Grupo grupo : GrupoList) {
			deleteGrupo(grupo);
		}
        db.close();
    }
}
