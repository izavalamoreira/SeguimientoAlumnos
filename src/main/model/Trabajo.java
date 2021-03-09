package main.model;

public class Trabajo
{
	private int _id;
	private int _id_grupo;
	private String _nombre;
	private String _estado;	

	public int get_id()
	{
		return _id;
	}

	public void set_id(int _id)
	{
		this._id = _id;
	}

	public int get_id_grupo()
	{
		return _id_grupo;
	}

	public void set_id_grupo(int _id_grupo)
	{
		this._id_grupo = _id_grupo;
	}

	public String get_nombre()
	{
		return _nombre;
	}

	public void set_nombre(String _nombre)
	{
		this._nombre = _nombre;
	}

	public String get_estado()
	{
		return _estado;
	}

	public void set_estado(String _estado)
	{
		this._estado = _estado;
	}
	
	public Trabajo()
	{		
	}
	
	public Trabajo(int id_grupo, String nombre, String estado)
	{
		this._id_grupo = id_grupo;
		this._nombre = nombre;
		this._estado = estado;		
	}

	public Trabajo(int id, int id_grupo, String nombre, String estado)
	{
		this._id = id;
		this._id_grupo = id_grupo;
		this._nombre = nombre;
		this._estado = estado;
	}
}
