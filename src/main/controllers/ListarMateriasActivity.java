package main.controllers;

import java.util.ArrayList;
import java.util.List;

import main.helper.DBHelper;
import main.model.Curso;
import main.model.Materia;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import com.app.R;



public class ListarMateriasActivity extends Activity {
	private ListView lvlMaterias;
	private int id_curso;
	private DBHelper dbh;
	private ArrayAdapter<Materia> listAdapter; //FDB
	private CheckBox cBox;//FDB

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_listar_materias);

		lvlMaterias =(ListView)findViewById(R.id.ListaMaterias);

		/*****************************/
		//FDB
		// When item is tapped, toggle checked properties of CheckBox and Planet.  
		lvlMaterias.setOnItemClickListener(new AdapterView.OnItemClickListener() {  
			@Override  
			public void onItemClick( AdapterView<?> parent, View item,   
					int position, long id) {  
				Materia materia = listAdapter.getItem( position );  
				materia.toggleChecked();  
				MateriaViewHolder viewHolder = (MateriaViewHolder) item.getTag();  
				viewHolder.getCheckBox().setChecked( materia.getSelected() );  
			}  
		});  
		/*****************************/

		dbh = new DBHelper(this);

		//Traigo el curso -> me lo podria traer del intent !!!
		id_curso = this.getIntent().getIntExtra("id_curso", 1);
		Curso curso = dbh.findCursoById(id_curso);
		// seteo el nombre del curso como titulo //
		setCustomActivityTitle(curso.getNombreResumido());

		//traigo las Materias//
		id_curso = this.getIntent().getIntExtra("id_curso", 1);
		final List<Materia> MateriaLista = dbh.findMateriasByIdCurso(id_curso);

		/*FDB - Esto cargaba la lista con Strings
	        List<String> materiaslist = new ArrayList<String>();
	        for (Materia materia : MateriaLista) {
				materiaslist.add(materia.getNombre().toString());
			}*/

		List<Materia> materiaslist = new ArrayList<Materia>();
		for (Materia materia : MateriaLista) {
			materiaslist.add(materia);
		}

		/*FDB - Esto cargaba los srings de Materias
	        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, materiaslist);
		 */

		MateriaArrayAdapter adapter = new MateriaArrayAdapter(this, materiaslist);
		lvlMaterias.setAdapter(adapter);

		/*
	           // Set our custom array adapter as the ListView's adapter.  
    			listAdapter = new PlanetArrayAdapter(this, planetList);  
    			mainListView.setAdapter( listAdapter );
		 */

		lvlMaterias.setOnItemClickListener(new OnItemClickListener()
		{
			public void onItemClick(AdapterView<?> parent, View v, int position, long id)
			{
				Intent intent = new Intent(getApplicationContext(), ListarGruposActivity.class);

				Materia materia = dbh.findMateriaById(MateriaLista.get(position).getId());
				intent.putExtra("id_materia",materia.getId());
				startActivity(intent);
			}
		});

		//FDB - Un listener del check
		/*cBox = (CheckBox)findViewById(R.id.cboxMaterias);

	        cBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

	            @Override
	            public void onCheckedChanged(CompoundButton buttonView,boolean isChecked) {
	            	if(cBox.isChecked()){
	                    System.out.println("Checked");
	                }else{
	                    System.out.println("Un-Checked");
	                }
	            }
	        }
	     );  */   
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
	/****************************************/
	//FDB
	/** Holds child views for one row. */  
	private static class MateriaViewHolder {  
		private CheckBox checkBox ;  
		private TextView textView ;  
		public MateriaViewHolder() {}  
		public MateriaViewHolder( TextView textView, CheckBox checkBox ) {  
			this.checkBox = checkBox ;  
			this.textView = textView ;  
		}  
		public CheckBox getCheckBox() {  
			return checkBox;  
		}  
		public void setCheckBox(CheckBox checkBox) {  
			this.checkBox = checkBox;  
		}  
		public TextView getTextView() {  
			return textView;  
		}  
		public void setTextView(TextView textView) {  
			this.textView = textView;  
		}      
	}  

	/** Custom adapter for displaying an array of Planet objects. */  
	private static class MateriaArrayAdapter extends ArrayAdapter<Materia> {  

		private LayoutInflater inflater;
		private DBHelper dbh;

		public MateriaArrayAdapter( Context context, List<Materia> materiaList ) {  
			super( context, R.layout.activity_materias_info, R.id.txtVMaterias, materiaList );  
			// Cache the LayoutInflate to avoid asking for a new one each time.  
			inflater = LayoutInflater.from(context) ;  
			dbh = new DBHelper(context);
		}  

		@Override  
		public View getView(int position, View convertView, ViewGroup parent) {  
			// Planet to display  
			Materia materia = (Materia) this.getItem( position ); 
			
			
			// The child views in each row.  
			CheckBox isChecked ;   
			TextView nombre ;   

			// Create a new row view  
			if ( convertView == null ) {  
				convertView = inflater.inflate(R.layout.activity_materias_info, null);  

				// Find the child views.  
				nombre = (TextView) convertView.findViewById( R.id.txtVMaterias );  
				isChecked = (CheckBox) convertView.findViewById( R.id.cboxMaterias );  

				// Optimization: Tag the row with it's child views, so we don't have to   
				// call findViewById() later when we reuse the row.  
				convertView.setTag( new MateriaViewHolder(nombre,isChecked) );  

				// If CheckBox is toggled, update the planet it is tagged with.  
				isChecked.setOnClickListener( new View.OnClickListener() {  
					public void onClick(View v) {  

						CheckBox cb = (CheckBox) v ;  
						Materia materia = (Materia) cb.getTag();  
						materia.setSelected( cb.isChecked()?1:0); 
						dbh.updateMateria(materia);
					} 
				});  
				
			}  
			// Reuse existing row view  
			else {  
				// Because we use a ViewHolder, we avoid having to call findViewById().  
				MateriaViewHolder viewHolder = (MateriaViewHolder) convertView.getTag();  
				isChecked = viewHolder.getCheckBox() ;  
				nombre = viewHolder.getTextView() ;  
			}  

			// Tag the CheckBox with the Planet it is displaying, so that we can  
			// access the planet in onClick() when the CheckBox is toggled.  
			isChecked.setTag( materia );   

			// Display planet data  
			isChecked.setChecked( materia.getSelected());  
			nombre.setText( materia.getNombre());        

			return convertView;  
		}  

	}  


}
