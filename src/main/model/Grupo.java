package main.model;

public class Grupo
{
    private int _id;
    private int _id_materia;
    private String _numero;
  
    
    
    public int get_id()
    {
        return _id;
    }

    public void set_id(int id)
    {
        this._id = id;
    }

    public int get_id_materia()
    {
        return _id_materia;
    }

    public void set_id_curso(int id_materia)
    {
        this._id_materia = id_materia;
    }

    public String get_numero()
    {
        return _numero;
    }

    public void set_numero(String _numero)
    {
        this._numero = _numero;
    }

    public Grupo()
    {
    }
    
     public Grupo(int id_materia, String numero)
    {
        this._id_materia = id_materia;
        this._numero = numero;

    }

    public Grupo(int id, int id_materia, String numero)
    {
        this._id = id;
        this._id_materia = id_materia;
        this._numero = numero;
    }
}
