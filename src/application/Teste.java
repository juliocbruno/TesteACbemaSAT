package application;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.PrintWriter;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import sun.launcher.resources.launcher;

public class Teste {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		try 
        {                
           //Abertura da impressora 
           FileOutputStream fos = new FileOutputStream("COM4" ); 
           PrintWriter ps = new PrintWriter(fos); 
   
           //Impressao
           ps.print( "Texto normal");
           ps.print( "\n"+(char )27+(char)15+"Texto condensado"+(char)27+( char)80);
           ps.print( "\n"+(char )27+(char)69+"Texto negrito"+(char)27+( char)70);
           ps.print( "\n"+(char )27+(char)86+"Texto expandido dupla-altura"+(char)27+( char)86);
           ps.print( "\n"+(char )27+(char)14+"Texto expandido dupla-largura"+(char)27+( char)14);
           ps.print( "\n"+(char )27+(char)52+"Texto italico"+(char)27+( char)53);
           ps.print( "\n"+(char )27+(char)14+(char)27+( char)86+"Texto expandido dupla-altura-largura"+(char)27+( char)53);
           ps.print( ""+(char )13+(char)10+(char)13+( char)10); // pula linha (2 vezes)
           ps.print( ""+(char )27+(char)119); // aciona guilhotina
          System.out.println("deu certo");
           //fim da impressao 
           ps.close(); 
        } 
        catch (Exception e) 
        { 
           System. out.println("Erro" ); 
        }  


	}

}
