package main.controllers;

import main.helper.DBHelper;
import main.helper.IOHelper;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.widget.SimpleCursorAdapter;
import android.view.View;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.app.R;

public class HomeMenuActivity extends Activity
{
	private Spinner matSel; //FDB
	private DBHelper dbh; //FDB
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home_menu);
		setCustomActivityTitle("Seguimiento de Alumnos");
		matSel = (Spinner) findViewById(R.id.spinnerMaterias);
		dbh = new DBHelper(this);
		Cursor c;
		c = dbh.findMateriasSelected();
		
		//Creamos el adaptador
		SimpleCursorAdapter adapter2 = new SimpleCursorAdapter(this,android.R.layout.simple_spinner_item,c,new String[] {"materia"},    new int[] {android.R.id.text1}, 0); 
		//Añadimos el layout para el menú
		adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		//Le indicamos al spinner el adaptador a usar
		matSel.setAdapter(adapter2);
	}

	//FDB - Ahora esta funcionalidad se pasa a a ABMCursoActivity
	public void goToBuscarCursos(View v)
	{
		Intent intent = new Intent(this, BuscarCursosActivity.class);
		startActivity(intent);
	}

	public void goToABMCursos(View v)
	{
		Intent intent = new Intent(this, ABMCursoActivity.class);
		startActivity(intent);
	}

	public void dumpDBtoCSV(View v)
	{
		IOHelper ioh = new IOHelper(this);
		ioh.dumpDBtoCSV();
		Toast.makeText(getApplicationContext(), "Archivo generado correctamente", Toast.LENGTH_LONG).show();
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
	private void buscarMateriasSelected()
	{
		
		Cursor c;
		c = dbh.findMateriasSelected();
		
		//Creamos el adaptador
		SimpleCursorAdapter adapter2 = new SimpleCursorAdapter(this,android.R.layout.simple_spinner_item,c,new String[] {"materia"},    new int[] {android.R.id.text1}, 0); 
		//Añadimos el layout para el menú
		adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		//Le indicamos al spinner el adaptador a usar
		matSel.setAdapter(adapter2);
	}
}
