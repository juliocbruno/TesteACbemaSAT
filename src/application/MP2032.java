package application;

import com.sun.jna.Library;
import com.sun.jna.Native;

public interface MP2032 extends Library {

	public MP2032 instance = (MP2032) Native.loadLibrary("mp2032.dll",MP2032.class);
	
	public int IniciaPorta( String Porta );
	public int FechaPorta();
	public int BematechTX( String BufTrans );
	public int ComandoTX( String BufTrans, int TamBufTrans);
	public int CaracterGrafico( String BufTrans, int TamBufTrans);
	public int DocumentInserted();
	public int Le_Status();
	public int AutenticaDoc( String texto, int tempo);
	public int Le_Status_Gaveta();
	public int ConfiguraTamanhoExtrato(int NumeroLinhas);
	public int HabilitaExtratoLongo(int Flag);
	public int HabilitaEsperaImpressao(int Flag);
	public int EsperaImpressao();
	public int ConfiguraModeloImpressora( int ModeloImpressora);
	public int AcionaGuilhotina(int Modo);
	public int FormataTX (String BufTras, int TpoLtra, int Italic, int Sublin, int expand, int enfat);
	public int HabilitaPresenterRetratil(int iFlag);
	public int ProgramaPresenterRetratil(int iTempo);
	public int VerificaPapelPresenter();
	
	// Função para Configuração dos Códigos de Barras
	public int ConfiguraCodigoBarras( int Altura, int Largura, int PosicaoCaracteres, int Fonte, int Margem);
	
	// Funções para impressão dos códigos de barras
	public int ImprimeCodigoBarrasUPCA(String Codigo);
	public int ImprimeCodigoBarrasUPCE(String Codigo);
	public int ImprimeCodigoBarrasEAN13(String Codigo);
	public int ImprimeCodigoBarrasEAN8(String Codigo);
	public int ImprimeCodigoBarrasCODE39(String Codigo);
	public int ImprimeCodigoBarrasCODE93(String Codigo);
	public int ImprimeCodigoBarrasCODE128(String Codigo);
	public int ImprimeCodigoBarrasITF(String Codigo);
	public int ImprimeCodigoBarrasCODABAR(String Codigo);
	public int ImprimeCodigoBarrasISBN(String Codigo);
	public int ImprimeCodigoBarrasMSI(String Codigo);
	public int ImprimeCodigoBarrasPLESSEY(String Codigo);
	public int ImprimeCodigoBarrasPDF417(int NivelCorrecaoErros, int Altura, int Largura, int Colunas, String Codigo);
	public int ImprimeCodigoQRCODE(int errorCorrectionLevel, int moduleSize, int codeType, int QRCodeVersion, int encodingModes, String codeQr);
	
	// Funções para impressão de BitMap
	public int ImprimeBitmap (String name, int mode);
	public int ImprimeBmpEspecial (String name, String xScale, int yScale, int angle);
	public int AjustaLarguraPapel (int width);
	public int SelectDithering (int mode);
	public int PrinterReset();
	public int LeituraStatusEstendido(byte A);
	public int IoControl (int flag, boolean mode);
	public int DefineNVBitmap (int count, String[] filenames);
	public int PrintNVBitmap (String image, int mode);
	public int Define1NVBitmap (String filename);
	public int DefineDLBitmap (String filename);
	public int PrintDLBitmap (String mode);
	
	// Função de Firmware
	public int AtualizaFirmware (String fileName);
}
