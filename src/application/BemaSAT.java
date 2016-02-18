package application;

import com.sun.jna.Library;
import com.sun.jna.Native;


public interface BemaSAT extends Library {
	
	public BemaSAT instance = (BemaSAT) Native.loadLibrary("BemaSAT32.dll", BemaSAT.class);
	
	public String AssociarAssinatura(int numSessao, String CNPJValue, String assinaturaCNPJs);
	public String AtivarSAT(int numSessao, String SubComando, String codigoDeAtivacao, String CNPJ, int cUF);
	public String AtualizarSoftwareSAT(int numSessao, String codigoDeAtivacao);
	public String BloquearSAT(int numSessao, String codigoDeAtivacao);
	public String CancelarUltimaVenda(int numSessao, String codigoDeAtivacao, String chave, String dadosCancelamento);
	public String ConfigurarInterfaceDeRede(int numSessao, String codigoDeAtivacao, String dadosConfiguracao);
	public String ConsultarNumeroSessao(int numSessao, String codigoDeAtivacao, String cNumeroDeSessao);	
	public String ConsultarSAT(int numSessao);
	public String ConsultarStatusOperacional(int numSessao, String CodigoAtivacaoSAT);
	public String DesbloquearSAT(int numSessao, String CodigoAtivacaoSAT);
	public String EnviarDadosVenda(int numSessao, String CodigoAtivacaoSAT, String dadosVenda);
	public String ExtrairLogs(int numSessao, String CodigoAtivacaoSAT);
	public String TesteFimAFim(int numSessao, String CodigoAtivacaoSAT, String dadosVenda);
	public String TrocarCodigoDeAtivacao(int numSessao, String CodigoAtivacaoSAT, String novoCodigo, String confNovoCodigo);

}
