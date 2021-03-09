package main.controllers;

import main.helper.DBHelper;
import main.model.Curso;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.app.R;

public class AltaCursoActivity extends Activity
{
    private Spinner spAnios;
    private Spinner spCuatrimestres;
    private Spinner spCursos;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alta_curso);
        
        setCustomActivityTitle("Alta de Cursos");

        spAnios = (Spinner) findViewById(R.id.spAnios);
        spCuatrimestres = (Spinner) findViewById(R.id.spCuatrimestres);
        spCursos = (Spinner) findViewById(R.id.spCursos);
    }

    public void altaCurso(View v)
    {
        DBHelper dbh = new DBHelper(this);

        String anio = spAnios.getSelectedItem().toString();
        String cuatrimestre = spCuatrimestres.getSelectedItem().toString();
        String letra = spCursos.getSelectedItem().toString();

        Curso cursoTmp = dbh.findCursoByAnioCuatriLetra(anio, cuatrimestre, letra);

        int cant;

        if (cursoTmp == null)
        {
            Curso curso = new Curso();
            curso.set_anio(anio);
            curso.set_cuatrimestre(cuatrimestre);
            curso.set_letra(letra);
            dbh.addCurso(curso);
            cant = dbh.getCursosCount();
            Toast.makeText(this, "Curso creado. nueva cantidad: " + cant, Toast.LENGTH_SHORT).show();
            //FDB - Si se pudo crear el curso, lo busco y listo las materias del curso creado
            // creo un intent y le los adjunto los datos
           curso = dbh.findCursoByAnioCuatriLetra(anio, cuatrimestre, letra);

            if (curso == null)
            {
                Toast.makeText(getApplicationContext(), "El curso seleccionado no existe", Toast.LENGTH_SHORT).show();
            }
            else
            {
            	// creo un intent y le adjunto los datos
                Intent intent = new Intent(this, ListarMateriasActivity.class);
                intent.putExtra("id_curso", curso.get_id());

                // inicio el intent
                startActivity(intent);
            }
            
        }
        else
        {
            cant = dbh.getCursosCount();
            Toast.makeText(this, "El curso ya existe. sigue habiendo " + cant + " cursos.", Toast.LENGTH_SHORT).show();
        }
    }
    
	private void setCustomActivityTitle(String title)
	{
		ActionBar ab = getActionBar();
	    ab.setDisplayShowTitleEnabled(false);
	    ab.setDisplayShowCustomEnabled(true);
	    View customTitle = getLayoutInflater().inflate(R.layout.activity_titles, null);
	    TextView tv = (TextView) customTitle.findViewById(R.id.title);
	    tv.setText(title);
		ab.setCustomView(customTitle);
	}
}
