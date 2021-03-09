package main.helper;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import main.model.Curso;
import main.model.Grupo;
import main.model.Materia;
import main.model.Trabajo;
import android.content.Context;
import android.os.Environment;
import android.widget.Toast;

/**
 * Ejemplo de uso:
 * 
 * IOHelper ioh = new IOHelper(this); File file = ioh.createLogFile("logs",
 * "log"); ioh.saveToFile(file, "linea 1", true); ioh.saveToFile(file,
 * "linea 2", true); ioh.saveToFile(file, "linea 3", true); String texto =
 * ioh.readFile(file); Toast.makeText(getBaseContext(), texto,
 * Toast.LENGTH_LONG).show();
 */

public class IOHelper
{
	private Context _context;

	public IOHelper(Context context)
	{
		this._context = context;
	}

	public File createLogFile(String folderName, String fileName)
	{
		File file = null;
		try
		{
			String externalStoragePath = Environment.getExternalStorageDirectory().toString();
			File mFolder = new File(externalStoragePath + "/" + folderName);
			if (!mFolder.exists())
			{
				mFolder.mkdir();
			}
			file = new File(mFolder.getAbsolutePath(), fileName);
			saveToFile(file, "", false);

		}
		catch (Exception e)
		{
			Toast.makeText(getBaseContext(), e.getMessage(), Toast.LENGTH_LONG).show();
		}
		return file;
	}

	public void saveToFile(File file, CharSequence texto, boolean append)
	{
		try
		{
			FileOutputStream fos = new FileOutputStream(file, append);
			//esto deberia hacer que se puedan grabar caracteres utf8 pero no funciona
			OutputStreamWriter myOutWriter = new OutputStreamWriter(fos, Charset.forName("UTF-8"));
			myOutWriter.append(texto);
			myOutWriter.append("\n");
			myOutWriter.close();
			fos.close();
		}
		catch (Exception e)
		{
			Toast.makeText(getBaseContext(), e.getMessage(), Toast.LENGTH_LONG).show();
		}
	}

	public String readFile(File file)
	{
		StringBuilder text = new StringBuilder();
		try
		{
			String line = "";
			BufferedReader br = new BufferedReader(new FileReader(file));

			while ((line = br.readLine()) != null)
			{
				text.append(line);
				text.append('\n');
			}
			br.close();
		}
		catch (IOException e)
		{
			Toast.makeText(getBaseContext(), e.getMessage(), Toast.LENGTH_LONG).show();
		}
		return text.toString();
	}

	private Context getBaseContext()
	{
		return this._context;
	}

	public void writeLineToCSV(File file, List<String> args)
	{
		String line = "";
		for (String arg : args)
		{
			line += arg;
			line += ", ";
		}
		
		if (!args.isEmpty())
		{
			line = line.substring(0, line.length() -2);
			saveToFile(file, line, true);
		}
	}
	
	public void dumpDBtoCSV()
	{
		List<String> csvLine;
		List<List<String>> lineas = new ArrayList<List<String>>();
		DBHelper dbh = new DBHelper(_context);
		File csvFile = createLogFile("com.ort.seguimiento", "dump.csv");
		
		//creo los titulos de la tabla
		csvLine = new ArrayList<String>();
		csvLine.add("CURSO");
		csvLine.add("GRUPO");
		csvLine.add("TRABAJO");
		csvLine.add("ESTADO");
		lineas.add(csvLine);
		
		List<Curso> cursos = dbh.getAllCursos();
		for (Curso curso : cursos)
		{
			List<Materia> materias = dbh.findMateriasByIdCurso(curso.get_id());
			for (Materia materia : materias) {
			
				List<Grupo> grupos = dbh.findGruposByIdMateria(materia.getId());
				for (Grupo grupo : grupos)
				{
					List<Trabajo> trabajos = dbh.findTrabajosByIdGrupo(grupo.get_id());
					{
						for (Trabajo trabajo : trabajos)
						{
							csvLine = new ArrayList<String>();
							csvLine.add(curso.getNombreResumido());
							csvLine.add(materia.getNombre());
							csvLine.add(grupo.get_numero());
							csvLine.add(trabajo.get_nombre());
							csvLine.add(trabajo.get_estado());
							lineas.add(csvLine);
						}
					}
				}
			}
			
		}
		
		for (List<String> linea : lineas)
		{
			this.writeLineToCSV(csvFile, linea);
		}
		
	}
}
