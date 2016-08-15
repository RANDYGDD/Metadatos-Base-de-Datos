
package administradorbd;

import java.sql.*;
import java.util.ArrayList;

public class MostrarConsulta
{
    private Connection conexion,con; 
    private ResultSet resultadoConsulta;
    private ResultSetMetaData metaDatos;
    DatabaseMetaData DatosMeta;
    private String consulta;
    private String[][] datosDevueltos;
    private String [] nombresColumnas;
    private String error;

    public MostrarConsulta(Connection conRecibida, String consultaRecibida)
    {
    	conexion = conRecibida;
    	consulta = consultaRecibida;
   
    	try 
        {
            DatosMeta = conRecibida.getMetaData();
            
            //Crea una instancia para mandar sentencias al servidor Oracle//
            Statement sentencia = conexion.createStatement();
           
            //Ejecuta la consulta y devuelve el ResultSet//
            resultadoConsulta = sentencia.executeQuery(consulta);

            //Obtiene los metadatos del ResultSet//	
            metaDatos = resultadoConsulta.getMetaData();
            error = null;
	}

	catch (SQLException e) 
        {
            error = e.getMessage();
	}  	
    }
    
    
    public MostrarConsulta(Connection conRecibida)
    {
    	conexion = conRecibida;
        
    	try 
        {
            DatosMeta = conRecibida.getMetaData();
            Statement sentencia = conexion.createStatement();
            error = null;
	}
        
        catch (SQLException e) 
        {
            error = e.getMessage();
	} 
    }
       
    
    public String[] getNombresColumnas()
    {
    	if(error == null)
        {
            try
            {    	
                //Devuelve el número de columnas//
                int columnas = metaDatos.getColumnCount();	
		nombresColumnas = new String[columnas];
		
                for(int i = 0; i < columnas; i++)
                {
                    //Obtiene el nombre de cada una de las columna//
                    nombresColumnas[i] = metaDatos.getColumnLabel(i + 1);
		}
                
             
	    }
            
            catch(SQLException ex)
            {

	    }
	}	

    	return nombresColumnas; ///retorna los nombre de la columna de la tablas en un arreglo unidimencional
    }
    
    
    public String[][] getDatosDevueltos()
    {
        ArrayList<String>array = new ArrayList(); 
        //ArrayList<ArrayList<String>> arreglo = new ArrayList(); 
	
        if(error == null)
        {
            int filas = 0;  
	    
            try 
            {
                //numeros de columnas//
                int columnas = metaDatos.getColumnCount();
		
                //El next solo se usa una vez//
                while (resultadoConsulta.next()) 
                {
                    for (int i = 0; i < columnas; i++) 
                    {
                        String Nombre = resultadoConsulta.getString(nombresColumnas[i]);
                        
                        //Ingresa datos de manera vertical en este arraylist//
                        array.add(Nombre);      
                    }
                    
                    ///La filas me estan dando cero entonces es la unica manera de calcular
                    filas++;        
                }
                  
                datosDevueltos = new String[filas][columnas];   
                int m = 0;
                  
                for(int i = 0; i < filas; i++)
                {
                    //Aqui se recorren las filas y columnas//
                    for (int k = 0; k < columnas; k++) 
                    {
                        //Agregando los datos al arreglo bidimencional para ponerlos en la tabla//
                        datosDevueltos[i][k] = array.get(m);   
                        
                        //m es porque el arreglo esta de manera vertival y debo recorrerlo asi ya que K inicia cada vez que se cumple la funcion.
                        m++;  
                    }
                }
                   
            }

            catch (Exception e)
            {

            }
	}

    	return datosDevueltos; //Retorna los datos en forma de un arreglo bidimencional
    }   

    
    public String Usuario() throws SQLException
    {
        return DatosMeta.getUserName();   //Retorna el nombre del usuario que esta conectado.
    }

    
    public ArrayList<String> retornarTablas(Connection conRecibida) throws SQLException
    {  ///Este metodo  retorna el nombre de todas la tablas creadas  por un  usario.
        con = conRecibida;
        ArrayList<String> tablas = new ArrayList();
        Statement Stm = con.createStatement();
        ResultSet resulatdo = Stm.executeQuery("Select TABLE_NAME from user_tables");  ///Aqui se trae todo los nombre de las tablas de un usuario
     
        while(resulatdo.next())
        {
           String nom = resulatdo.getString("TABLE_NAME");
           tablas.add(nom);///Se almecenan eso nombre de las tablas  en este arraylist
        }
    
        return tablas; //se retornan los nombre de las tablas en el arraylist
    }
    
    
    public String getMensajeError()
    {
    	return error;
    }  
}
