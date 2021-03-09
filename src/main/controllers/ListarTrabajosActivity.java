package main.controllers;

import java.util.ArrayList;

import main.helper.DBHelper;
import main.model.Grupo;
import main.model.Trabajo;
import android.app.ActionBar;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.app.R;

public class ListarTrabajosActivity extends Activity
{
    private int _id_grupo;
    private ArrayList<Spinner> listaSpiners;
    private ArrayList<TextView> listaTextView;
    private ArrayList<Trabajo> listaDeTrabajos;
    private DBHelper dbh;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
    	
    	  dbh = new DBHelper(this);
    	  listaDeTrabajos = new ArrayList<Trabajo>();
          listaTextView = new ArrayList<TextView>();
          listaSpiners = new ArrayList<Spinner>();
    	
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listar_trabajos);
        
        _id_grupo = this.getIntent().getIntExtra("id_grupo", 0);
        
        Grupo grupo = dbh.findGrupoById(_id_grupo);
        
        setCustomActivityTitle("Grupo " + grupo.get_numero());
        
        dbh = new DBHelper(getApplicationContext());
        
        TextView txtTP1 = (TextView) findViewById(R.id.textView1);
        TextView txtTP2 = (TextView) findViewById(R.id.textView2);
        TextView txtTP3 = (TextView) findViewById(R.id.textView3);
        TextView txtTP4 = (TextView) findViewById(R.id.textView4);
        TextView txtTP5 = (TextView) findViewById(R.id.textView5);
        TextView txtTP6 = (TextView) findViewById(R.id.textView6);
        TextView txtTP7 = (TextView) findViewById(R.id.textView7);

        listaTextView.add(txtTP1);
        listaTextView.add(txtTP2);
        listaTextView.add(txtTP3);
        listaTextView.add(txtTP4);
        listaTextView.add(txtTP5);
        listaTextView.add(txtTP6);
        listaTextView.add(txtTP7);

        //
        Button btnGuardar = (Button) findViewById(R.id.GuardarEstados);

        //
        Spinner spnEstados1 = (Spinner) findViewById(R.id.spinner1);
        Spinner spnEstados2 = (Spinner) findViewById(R.id.spinner2);
        Spinner spnEstados3 = (Spinner) findViewById(R.id.spinner3);
        Spinner spnEstados4 = (Spinner) findViewById(R.id.spinner4);
        Spinner spnEstados5 = (Spinner) findViewById(R.id.spinner5);
        Spinner spnEstados6 = (Spinner) findViewById(R.id.spinner6);
        Spinner spnEstados7 = (Spinner) findViewById(R.id.spinner7);

        listaSpiners.add(spnEstados1);
        listaSpiners.add(spnEstados2);
        listaSpiners.add(spnEstados3);
        listaSpiners.add(spnEstados4);
        listaSpiners.add(spnEstados5);
        listaSpiners.add(spnEstados6);
        listaSpiners.add(spnEstados7);

        //
        listaDeTrabajos = (ArrayList<Trabajo>) dbh.findTrabajosByIdGrupo(_id_grupo);

        //

        for (int i = 0; i < listaTextView.size(); i++)
        {
            listaTextView.get(i).setText(listaDeTrabajos.get(i).get_nombre());
        }

        int i = 0;
        for (Trabajo trabajo : listaDeTrabajos)
        {
            if (trabajo.get_estado().equals("Sin Calificacion"))
            {
                listaSpiners.get(i).setSelection(0);
            }
            else if (trabajo.get_estado().equals("Aprobado"))
            {
                listaSpiners.get(i).setSelection(1);
            }
            else if (trabajo.get_estado().equals("Incompleto"))
            {
                listaSpiners.get(i).setSelection(2);
            }
            else if (trabajo.get_estado().equals("Desaprobado"))
            {
                listaSpiners.get(i).setSelection(3);
            }
            else if (trabajo.get_estado().equals("Ausente"))
            {
                listaSpiners.get(i).setSelection(4);
            }
            i++;
        }

        btnGuardar.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                guardar(v);
                Toast.makeText(getApplicationContext(), "Cambios guardados correctamente!", Toast.LENGTH_LONG).show();
            }
        });

    }

    public void guardar(View v)
    {
        int i = 0;
        for (Spinner spinner : listaSpiners)
        {
            Trabajo trabajo = new Trabajo();
            trabajo = dbh.findTrabajoByIdGrupoNombre(_id_grupo, (String) listaTextView.get(i).getText());
            trabajo.set_estado(spinner.getSelectedItem().toString());

            dbh.updateTrabajo(trabajo);
            
            i++;
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
	/*
	public void nuevoTrabajo(View view)
	{
		Trabajo trabajo = new Trabajo(_id_grupo, "Nombre del TP","Estado Inicial");
		dbh.addTrabajo(trabajo);
		finish();
        startActivity(getIntent());
		//Toast.makeText(getApplicationContext(), grupo.get_numero() + " - " + grupo.get_id_materia(), Toast.LENGTH_SHORT).show();
	}*/
}
