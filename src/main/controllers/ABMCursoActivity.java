package main.controllers; 

import java.util.ArrayList;
import java.util.List;

import main.helper.DBHelper;
import main.model.Curso;
import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.app.R;

public class ABMCursoActivity extends Activity
{
	private ListView lvCursos;
	private DBHelper dbh;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_abm_curso);
		
		setCustomActivityTitle("ABM de Cursos");
		
		dbh = new DBHelper(this);

		lvCursos = (ListView) findViewById(R.id.listaDeCursos);
		
		lvCursos.setOnItemClickListener(new OnItemClickListener()
		{
			public void onItemClick(AdapterView<?> parent, View v, int position, long id)
			{
				AlertDialog dlgConfirmacion = crearDialogoDeConfirmacion(traerCurso(position));
			    dlgConfirmacion.show();
			}
		});
		
		cargarCursos();		
	}

	@Override
	public void onResume()
	{
		super.onResume();
		cargarCursos();
	}
	
	private void cargarCursos()
	{
		List<Curso> cursosObjs = dbh.getAllCursos();

		List<String> cursos = new ArrayList<String>();
		for (Curso curso : cursosObjs)
		{
			cursos.add(curso.toString());
		}

		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, cursos);
		lvCursos.setAdapter(adapter);
	}

	public void goToAltaCurso(View v)
	{
		Intent intent = new Intent(this, AltaCursoActivity.class);
		startActivity(intent);
	}
	
    private AlertDialog crearDialogoDeConfirmacion(final Curso curso)
    {
        AlertDialog deleteConfirmationDialogBox = new AlertDialog.Builder(this)
        //set message, title, and icon
        .setTitle("Borrar curso").setMessage("Está seguro que desea borrar el curso?").setIcon(R.drawable.ic_launcher)

        .setPositiveButton("Borrar", new DialogInterface.OnClickListener()
        {
            public void onClick(DialogInterface dialog, int whichButton)
            {
                borrarCurso(curso);
                dialog.dismiss();
                finish();
                startActivity(getIntent());
            }
        })

        .setNegativeButton("Cancelar", new DialogInterface.OnClickListener()
        {
            public void onClick(DialogInterface dialog, int which)
            {
                dialog.dismiss();
            }
        }).create();
        return deleteConfirmationDialogBox;
    }

    public void borrarCurso(Curso curso)
    {
        dbh.deleteCurso(curso);
        finish();
        startActivity(getIntent());
        Toast.makeText(getApplicationContext(), "Curso borrado correctamente!", Toast.LENGTH_LONG).show();

    }

    private Curso traerCurso(int posicion)
    {
    	int i =0;
		List<Curso> cursosObjs = dbh.getAllCursos();
		String scurso = lvCursos.getItemAtPosition(posicion).toString();
		
		for (Curso curso : cursosObjs) {

			if (curso.toString().equals(scurso.toString()))
				break;
			else
				i++;
		}

		
		return cursosObjs.get(i);
		
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
	
	private AlertDialog crearBorrarTodosAlertDialog()
	{
		AlertDialog deleteConfirmationDialogBox = new AlertDialog.Builder(this)
		// set message, title, and icon
		.setTitle("Limpiar BD").setMessage("Está seguro que desea eliminar todos los datos de la BD?").setIcon(R.drawable.ic_launcher)

		.setPositiveButton("Limpiar", new DialogInterface.OnClickListener()
		{
			public void onClick(DialogInterface dialog, int whichButton)
			{
				dbh.regenerateDB();
				Toast.makeText(getApplicationContext(), "Base de datos reiniciada.", Toast.LENGTH_SHORT).show();
				dialog.dismiss();
				finish();
				startActivity(getIntent());
			}
		})

		.setNegativeButton("Cancelar", new DialogInterface.OnClickListener()
		{
			public void onClick(DialogInterface dialog, int which)
			{
				dialog.dismiss();
			}
		}).create();
		return deleteConfirmationDialogBox;
	}
	
	public void reiniciarBD(View view)
	{
		AlertDialog dlgConfirmacion = crearBorrarTodosAlertDialog();
	    dlgConfirmacion.show();
	}
	
	public void goToBuscarCursos(View v)
	{
		Intent intent = new Intent(this, BuscarCursosActivity.class);
		startActivity(intent);
	}
}
