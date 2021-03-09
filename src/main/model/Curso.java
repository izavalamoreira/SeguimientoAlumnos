package main.model;

public class Curso
{
    // private variables
    int _id;
    private String _anio;
    private String _cuatrimestre;
    private String _letra;

    public int get_id()
    {
        return _id;
    }

    public void set_id(int id)
    {
        this._id = id;
    }

    public String get_anio()
    {
        return _anio;
    }

    public void set_anio(String anio)
    {
        this._anio = anio;
    }

    public String get_cuatrimestre()
    {
        return _cuatrimestre;
    }

    public void set_cuatrimestre(String cuatrimestre)
    {
        this._cuatrimestre = cuatrimestre;
    }

    public String get_letra()
    {
        return _letra;
    }

    public void set_letra(String letra)
    {
        this._letra = letra;
    }

    public Curso()
    {
    };

    public Curso(int id, String anio, String cuatrimestre, String letra)
    {
        this._id = id;
        this._anio = anio;
        this._cuatrimestre = cuatrimestre;
        this._letra = letra;
    }
    
    public String getNombreResumido()
    {
    	return String.format("%s/%s/%s", _anio.substring(0, 1), _cuatrimestre.substring(0, 1), _letra.substring(_letra.length() - 1, _letra.length()));
    }

    @Override
    public String toString()
    {
        return String.format("%s - %s - %s", _anio, _cuatrimestre, _letra);
    }
}
