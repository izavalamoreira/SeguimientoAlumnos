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

public class BuscarCursosActivity extends Activity
{
    private Spinner spAnios;
    private Spinner spCuatrimestres;
    private Spinner spCursos;
    private DBHelper dbh;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buscar_cursos);
        
        setCustomActivityTitle("Busqueda de Cursos");

        spAnios = (Spinner) findViewById(R.id.spAnios);
        spCuatrimestres = (Spinner) findViewById(R.id.spCuatrimestres);
        spCursos = (Spinner) findViewById(R.id.spCursos);

        dbh = new DBHelper(this);
    }

    public void buscar(View v)
    {
        String anio = spAnios.getSelectedItem().toString();
        String cuatrimestre = spCuatrimestres.getSelectedItem().toString();
        String letra = spCursos.getSelectedItem().toString();

        Curso curso = dbh.findCursoByAnioCuatriLetra(anio, cuatrimestre, letra);

        if (curso == null)
        {
            Toast.makeText(getApplicationContext(), "El curso seleccionado no existe", Toast.LENGTH_SHORT).show();
        }
        else
        {
        	// creo un intent y le los adjunto los datos
            Intent intent = new Intent(this, ListarMateriasActivity.class);
            intent.putExtra("id_curso", curso.get_id());

            // inicio el intent
            startActivity(intent);
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