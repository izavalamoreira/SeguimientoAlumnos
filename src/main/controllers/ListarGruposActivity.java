package main.controllers;

import java.util.ArrayList;
import java.util.List;

import main.helper.DBHelper;
import main.model.Curso;
import main.model.Grupo;
import main.model.Materia;
import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.app.R;

public class ListarGruposActivity extends Activity
{
    private ListView lvGrupos;
    private int id_materia;
    private DBHelper dbh;
    List<Grupo> gruposObjs;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listar_grupos);

        lvGrupos = (ListView) findViewById(R.id.listaDeCursos);
        dbh = new DBHelper(this);

        /* traigo el curso */
        id_materia = this.getIntent().getIntExtra("id_materia", 1);
        Materia mat = dbh.findMateriaById(id_materia);
        Curso curso = dbh.findCursoById(mat.getId_curso());

        /* seteo el nombre del curso como titulo */
        setCustomActivityTitle(curso.getNombreResumido()+ " - " +mat.getNombre());

        /* traigo los grupos */
        gruposObjs = dbh.findGruposByIdMateria(mat.getId());

       
        List<String> grupos = new ArrayList<String>();
        for (Grupo g : gruposObjs)
        {
            grupos.add("Grupo " + g.get_numero());

        }


        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, grupos);
        lvGrupos.setAdapter(adapter);

        lvGrupos.setOnItemClickListener(new OnItemClickListener()
        {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id)
            {
 	            	Grupo grupo = gruposObjs.get(position);
	            	
	            	Intent intent = new Intent(getApplicationContext(), ListarTrabajosActivity.class);
	                intent.putExtra("id_grupo", grupo.get_id());
	                startActivity(intent);
            }
        });
        
        
        lvGrupos.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view,int position, long id) {
				Grupo grupo = gruposObjs.get(position);
				AlertDialog dlgConfirmacion = crearDialogoDeConfirmacion(grupo);
				dlgConfirmacion.show();
				return true;
			}
		});
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
	
	public void nuevoGrupo(View view)
	{
		Grupo grupo = new Grupo(id_materia,String.valueOf(dbh.findUltimoGrupoByIdMateriaGrupo(id_materia)+1));
		dbh.addGrupo(grupo);
		finish();
        startActivity(getIntent());
		Toast.makeText(getApplicationContext(), grupo.get_numero() + " - " + grupo.get_id_materia(), Toast.LENGTH_SHORT).show();
	}

	private AlertDialog crearDialogoDeConfirmacion(final Grupo grupo)
	    {
	        AlertDialog deleteConfirmationDialogBox = new AlertDialog.Builder(this)
	        //set message, title, and icon
	        .setTitle("Borrar Grupo").setMessage("Está seguro que desea borrar el grupo y sus trabajos?").setIcon(R.drawable.ic_launcher)

	        .setPositiveButton("Borrar", new DialogInterface.OnClickListener()
	        {
	            public void onClick(DialogInterface dialog, int whichButton)
	            {
	                dbh.deleteGrupo(grupo);
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
}