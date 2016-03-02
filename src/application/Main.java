package application;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.regex.Pattern;

import javax.comm.CommPortIdentifier;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.Unmarshaller;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;
import org.apache.log4j.PropertyConfigurator;
import org.apache.log4j.RollingFileAppender;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.sun.org.apache.xerces.internal.impl.dv.util.Base64;

import javafx.application.Application;
import javafx.beans.Observable;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.stage.Stage;
import jdk.internal.org.xml.sax.SAXParseException;

public class Main extends Application {

	static Logger log = Logger.getLogger(Main.class);

	BemaSAT Bema = BemaSAT.instance;
	MP2032 mp2032 = MP2032.instance;

	String retorno = "";
	int iRetorno;
	static String cfe = "";
	static String arquivoXml = "";
	static String xmlRetornoVenda;
	static File arquivo;

	// Componentes
	Image logo;
	ImageView mostraLogo;
	Button BtSair;
	Label LbSessao;
	TextField TfSessao;
	Label LbCodAtivacao;
	TextField tfCodAtivacao;
	Random gerador;
	Button BtConsultaSAT;
	Button BtConsStatusOp;
	Button BtEnviarVenda;
	TextField TfChaveAcesso;
	Button BtImprimirVenda;
	String retornoStr[];

	DocumentBuilderFactory dbf;
	DocumentBuilder docBuilder;
	Document doc;
	Node cod;
	Node descri;
	Node ncm;
	Node cfop;
	Node un;
	Node qtde;
	Node valorUn;
	Node aliq;
	Node valorItem;
	Node indRegra;
	Node indDesc;
	Node outro;
	Attr id;

	/*
	 * ComboBox<String> comboBox; ObservableList<String> options;
	 */

	public void start(Stage primaryStage) {

		BasicConfigurator.configure();
		PropertyConfigurator.configure("lib/log4j.properties");
		log.info(
				"=============================================================================================================================================");
		log.info("Inicio da aplicação de Teste com a BemaSAT32");
		log.info("Desenvolvido por Julio Cesar Bruno - Bematech Software Partners");

		gerador = new Random();

		File dirTesteACBemaSAT = new File("C:\\APPBEMASAT");
		if (dirTesteACBemaSAT.mkdir()) {

			log.info("Pasta " + dirTesteACBemaSAT.getName() + " criada com sucesso!");
		} else {
			log.info("Não foi possível criar o diretório " + dirTesteACBemaSAT.getName() + " ou ele já existe.");
		}

		iRetorno = mp2032.ConfiguraModeloImpressora(7);
		if (iRetorno == 1) {
			Alert alerta = new Alert(AlertType.INFORMATION);
			alerta.setTitle("Retorno Impressora");
			alerta.setContentText("Impressora modelo 7 encontrada com sucesso!");
			alerta.showAndWait();
		} else {
			Alert alerta = new Alert(AlertType.ERROR);
			alerta.setTitle("Retorno Impressora");
			alerta.setContentText("Impressora não encontrada!");
			alerta.showAndWait();
		}

		iRetorno = mp2032.IniciaPorta("COM10");
		if (iRetorno == 1) {
			Alert alerta = new Alert(AlertType.INFORMATION);
			alerta.setTitle("Retorno Impressora");
			alerta.setContentText("Impressora conectada com sucesso!");
			alerta.showAndWait();
		} else {
			Alert alerta = new Alert(AlertType.ERROR);
			alerta.setTitle("Retorno Impressora");
			alerta.setContentText("Impressora não conectada!");
			alerta.showAndWait();
		}

		int sessao = gerador.nextInt(999) * 100;
		retorno = Bema.ConsultarSAT(sessao);
		try {
			String retornoStr[] = retorno.split(Pattern.quote("|"));

			Alert alerta = new Alert(AlertType.INFORMATION);
			alerta.setTitle("Status SAT");
			alerta.setContentText("Mensagem de Retorno: " + retornoStr[2]);
			alerta.showAndWait();

		} catch (ArrayIndexOutOfBoundsException e) {
			log.info("ConsultarSAT: " + e);
			e.printStackTrace();
			Alert alerta = new Alert(AlertType.ERROR);
			alerta.setTitle("Status SAT");
			alerta.setContentText(retorno);
			alerta.showAndWait();
		}

		try {

			/*
			 * ///PORTAS
			 * SERIAIS==========================================================
			 * ==========================
			 * 
			 * // captura a lista de portas disponíveis, // pelo método estético
			 * em CommPortIdentifier. Enumeration pList =
			 * CommPortIdentifier.getPortIdentifiers();
			 * 
			 * // Um mapping de nomes para CommPortIdentifiers. HashMap map =
			 * new HashMap();
			 * 
			 * // Procura pela porta desejada while (pList.hasMoreElements()) {
			 * CommPortIdentifier cpi = (CommPortIdentifier)pList.nextElement();
			 * map.put(cpi.getName(), cpi); if (cpi.getPortType() ==
			 * CommPortIdentifier.PORT_SERIAL) { ObservableList<String> options
			 * = FXCollections.observableArrayList(cpi.getName()); final
			 * ComboBox<String> comboBox = new ComboBox(options); } }
			 * 
			 * //===============================================================
			 * ====================================
			 */

			// Logo
			logo = new Image("application/B_MARCA_H_POS_CMYK_150x63.png");
			mostraLogo = new ImageView(logo);

			TfChaveAcesso = new TextField();
			TfChaveAcesso.setPromptText("Chave de acesso do CF-e");
			TfChaveAcesso.setEditable(false);

			// Campo texto para número da sessão (não editável)
			LbSessao = new Label("Número da Sessão: ");
			TfSessao = new TextField();
			TfSessao.setEditable(false);
			TfSessao.setMaxWidth(60);
			TfSessao.getStyleClass().add("TfSessao");

			// Capo texto para o código de ativação
			LbCodAtivacao = new Label("Código de Ativação");
			tfCodAtivacao = new TextField("bema1234");
			tfCodAtivacao.setMaxWidth(100);

			// ConsultaSAT-------------------------------------------------------------------------------
			BtConsultaSAT = new Button("Consultar SAT");
			BtConsultaSAT.setOnAction(new EventHandler<ActionEvent>() {

				public void handle(ActionEvent event) {
					// TODO ConsultaSAT

					// gera número da sessão com 5 digitos
					try {
						int sessao = gerador.nextInt(999) * 100;
						log.info("Inicia a execução da função ConsultarSAT");
						log.info("Parâmetros da função ");
						log.info("Sessão: " + sessao);
						retorno = Bema.ConsultarSAT(sessao);
						log.debug(BtConsultaSAT);
						log.info("Retorno da Execução");
						log.info(retorno);
						log.info("Fim da execução da Função ConsultarSAT");
						log.info("");
						String retornoStr[] = retorno.split(Pattern.quote("|"));

						TfSessao.setText(Integer.toString(sessao));
						Alert AlRetorno = new Alert(AlertType.INFORMATION);

						AlRetorno.setTitle("Retorno do S@T");
						AlRetorno.setHeaderText("Retorno da Função ConsultaSAT");

						AlRetorno.setContentText("Número de Sessão: " + retornoStr[0] + "\n" + "Código de Retorno: "
								+ retornoStr[1] + "\n" + "Mensagem de Retorno: " + retornoStr[2]);
						AlRetorno.showAndWait();
					} catch (ArrayIndexOutOfBoundsException e) {
						log.info("ConsultarSAT: " + e);
						e.printStackTrace();
						Alert alerta = new Alert(AlertType.ERROR);
						alerta.setTitle("Status SAT");
						alerta.setContentText(retorno);
						alerta.showAndWait();
					}

				}
			});

			// ConsultaStatusOperacional--------------------------------------------------------------
			BtConsStatusOp = new Button("Consultar Status Operacional");
			BtConsStatusOp.setOnAction(new EventHandler<ActionEvent>() {

				public void handle(ActionEvent event) {
					// TODO ConsultaStatusOperacional

					try {
						// gera numero da sessão com 5 digitos
						int sessao = gerador.nextInt(999) * 100;
						log.info("Inicia a execução da função ConsultarStatusOperacional");
						log.info("Parâmetros da função ");
						log.info("Sessão: " + sessao);
						log.info("Código de ativação: " + tfCodAtivacao.getText());
						retorno = Bema.ConsultarStatusOperacional(sessao, tfCodAtivacao.getText());
						String retornoStr[] = retorno.split(Pattern.quote("|"));
						log.info(retornoStr[1]);
						TfSessao.setText(Integer.toString(sessao));
						Alert AlRetorno = new Alert(AlertType.INFORMATION);
						AlRetorno.setWidth(550);
						AlRetorno.setTitle("Retorno do S@T");
						AlRetorno.setHeaderText("Retorno da Função ConsultarStatusOperacional");
						AlRetorno.setContentText("Mensagem de Retorno: " + retornoStr[2]);
						TextArea textArea = new TextArea();
						textArea.setEditable(false);
						textArea.setWrapText(true);
						textArea.setText("Número de Sessão: " + retornoStr[0] + "\n" + "Código de Retorno: "
								+ retornoStr[1] + "\n" + "Mensagem de Retorno: " + retornoStr[2] + "\n"
								+ "Código Mensagem SEFAZ:  " + retornoStr[3] + "\n" + "Mensagem da Sefaz: "
								+ retornoStr[4] + "\n" + "Número de Série: " + retornoStr[5] + "\n" + "Tipo da Lan: "
								+ retornoStr[6] + "\n" + "IP: " + retornoStr[7] + "\n" + "Mac Address: " + retornoStr[8]
								+ "\n" + "Máscara de Rede: " + retornoStr[9] + "\n" + "Gateway: " + retornoStr[10]
								+ "\n" + "DNS Primário: " + retornoStr[11] + "\n" + "DNS Secundário: " + retornoStr[12]
								+ "\n" + "Status da Lan: " + retornoStr[13] + "\n" + "Nível da Bateria: "
								+ retornoStr[14] + "\n" + "Total de Memória: " + retornoStr[15] + "\n"
								+ "Memória Utilizada: " + retornoStr[16] + "\n" + "Data e Hora Atual: " + retornoStr[17]
								+ "\n" + "Versão Software Básico: " + retornoStr[18] + "\n" + "Versão do Layout: "
								+ retornoStr[19] + "\n" + "Último CF-e Enviado: " + retornoStr[20] + "\n"
								+ "Primeiro CF-e Armazenado: " + retornoStr[21] + "\n" + "Último CF-e Armazenado: "
								+ retornoStr[22] + "\n" + "Última Transmissão de CF-e: " + retornoStr[23] + "\n"
								+ "Última Comunicação com SEFAZ: " + retornoStr[24] + "\n" + "Emissão do Certificado: "
								+ retornoStr[25] + "\n" + "Vencimento do Certificado: " + retornoStr[26] + "\n");

						textArea.setMaxWidth(Double.MAX_VALUE);
						textArea.setMaxHeight(Double.MAX_VALUE);
						GridPane.setVgrow(textArea, Priority.ALWAYS);
						GridPane.setHgrow(textArea, Priority.ALWAYS);

						GridPane expContent = new GridPane();
						expContent.setMaxWidth(Double.MAX_VALUE);
						expContent.add(textArea, 0, 0);

						// AlRetorno.getDialogPane().setExpandableContent(expContent);
						AlRetorno.getDialogPane().setContent(expContent);

						AlRetorno.showAndWait();

					} catch (ArrayIndexOutOfBoundsException e) {
						log.info("ConsultaStatusOperacional: " + e);
						e.printStackTrace();
						Alert alerta = new Alert(AlertType.ERROR);
						alerta.setTitle("Status SAT");
						alerta.setContentText(retorno);
						alerta.showAndWait();
					}

					log.debug(BtConsStatusOp);
					log.info("Retorno da Execução: ");
					log.info(retorno);
					log.info("Fim da execução da Função ConsultarStatusOperacional");
					log.info("");

				}
			});

			// EnviarDadosVenda-------------------------------------------------------------------
			BtEnviarVenda = new Button("EnviarDadosVenda");
			BtEnviarVenda.setOnAction(new EventHandler<ActionEvent>() {

				public void handle(ActionEvent event) throws ArrayIndexOutOfBoundsException {
					// TODO Auto-generated method stub

					try {

						String xmlVenda = "<CFe> <infCFe versaoDadosEnt=\"00.06\"> <ide> <CNPJ>16716114000172</CNPJ> <signAC>SGR-SAT SISTEMA DE GESTAO E RETAGUARDA DO SAT</signAC> <numeroCaixa>001</numeroCaixa> </ide> <emit> <CNPJ>82373077000171</CNPJ> <IE>111111111111</IE> <indRatISSQN>S</indRatISSQN> </emit> <dest> <CPF></CPF> </dest> <det nItem=\"1\"> <prod> <cProd>1234567890</cProd> <xProd>AGUA MINERAL SEM GAS - COPO 200 ML</xProd> <NCM>22011000</NCM> <CFOP>5403</CFOP> <uCom>UN</uCom> <qCom>1.0000</qCom> <vUnCom>1.00</vUnCom> <indRegra>A</indRegra> <vDesc>0.00</vDesc> <vOutro>0.00</vOutro> </prod> <imposto> <vItem12741>0.00</vItem12741> <ICMS> <ICMS40> <Orig>0</Orig> <CST>60</CST> </ICMS40> </ICMS> <PIS> <PISNT> <CST>04</CST> </PISNT> </PIS> <COFINS> <COFINSNT> <CST>04</CST> </COFINSNT> </COFINS> </imposto> </det> <total> <vCFeLei12741>0.00</vCFeLei12741> </total> <pgto> <MP> <cMP>01</cMP> <vMP>10.00</vMP> </MP> </pgto> <infAdic> <infCpl>Obrigado, volte sempre</infCpl> </infAdic> </infCFe> </CFe>";
						int sessao = gerador.nextInt(999) * 100;
						log.info("Inicia a execução da função EnviarDadosVenda");
						log.info("Parâmetros da função ");
						log.info("Sessão: " + sessao);
						log.info("Código de ativação: " + tfCodAtivacao.getText());
						log.info("XML de venda: " + xmlVenda);
						retorno = Bema.EnviarDadosVenda(sessao, tfCodAtivacao.getText(), xmlVenda);
						log.debug(BtEnviarVenda);
						log.info("Retorno da Execução: ");
						log.info(retorno);
						TfSessao.setText(Integer.toString(sessao));

						String retornoStr[] = retorno.split(Pattern.quote("|"));

						Alert AlRetorno = new Alert(AlertType.INFORMATION);
						AlRetorno.setWidth(550);
						AlRetorno.setTitle("Retorno do S@T");
						AlRetorno.setHeaderText("Retorno da Função EnviarDadosVenda");
						AlRetorno.setContentText("Mensagem de Retorno: " + retornoStr[3]);
						TextArea textArea = new TextArea();
						textArea.setEditable(false);
						textArea.setWrapText(true);
						textArea.setText("Número de Sessão: " + retornoStr[0] + "\n" + "Código de Retorno: "
								+ retornoStr[1] + "\n" + "Código de retorno de cancelamento: " + retornoStr[2] + "\n"
								+ "Mensagem de Retorno: " + retornoStr[3] + "\n" + "Código SEFAZ: " + retornoStr[4]
								+ "\n" + "Mensagem SEFAZ: " + retornoStr[5] + "\n" + "Data e hora da emissão: "
								+ retornoStr[7] + "\n" + "Chave de acesso: " + retornoStr[8] + "\n"
								+ "Valor Total CF-e: " + retornoStr[9] + "\n" + "Número do CPF ou CNPJ do adquirente: "
								+ retornoStr[10] + "\n" + "Assinatura QRCode: " + retornoStr[11] + "\n");

						textArea.setMaxWidth(Double.MAX_VALUE);
						textArea.setMaxHeight(Double.MAX_VALUE);
						GridPane.setVgrow(textArea, Priority.ALWAYS);
						GridPane.setHgrow(textArea, Priority.ALWAYS);

						GridPane expContent = new GridPane();
						expContent.setMaxWidth(Double.MAX_VALUE);
						expContent.add(textArea, 0, 0);

						// AlRetorno.getDialogPane().setExpandableContent(expContent);
						AlRetorno.getDialogPane().setContent(expContent);

						AlRetorno.showAndWait();

						TfChaveAcesso.setText(retornoStr[8]);
						
						// Decodificando retorno Base64
						xmlRetornoVenda = retornoStr[6];
						log.info("Decodificando arquivo de retorno");
						byte[] decodeBytes = Base64.decode(xmlRetornoVenda);
						try {
							String decoded = new String(decodeBytes, "UTF-8");

							log.info("XML de Retorno:");
							log.info(decoded);
							log.info("Fim da execução da Função EnviarDadosVenda");
							log.info("");

							// Cria o arquivo xml com a chave de acesso no C:\
							cfe = retornoStr[8];
							arquivo = new File("C:\\APPBEMASAT\\" + cfe + ".xml");
							log.info("Criando o arquivo: " + arquivo.toString());
							FileOutputStream fos = new FileOutputStream(arquivo);
							fos.write(decoded.getBytes());

							// LEITURA XML
							// +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
							// fazer o parse do arquivo e criar o documento XML
							DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
							DocumentBuilder db = dbf.newDocumentBuilder();
							Document doc = db.parse(arquivo.toString());

							// Passo 1: obter o elemento raiz
							Element raiz = doc.getDocumentElement();
							// log.info("O elemento raiz é:
							// "+raiz.getNodeName());

							// Passo 2: obter nome do emitente
							NodeList listaNome = raiz.getElementsByTagName("xNome");
							Node nome = listaNome.item(0).getFirstChild();
							log.info("Nome: " + nome.getNodeValue());

							// Passo 3: obter endereço do emitente
							NodeList listaRua = raiz.getElementsByTagName("xLgr");
							Node rua = listaRua.item(0).getFirstChild();

							NodeList listaNum = raiz.getElementsByTagName("nro");
							Node num = listaNum.item(0).getFirstChild();

							NodeList listaCompl = raiz.getElementsByTagName("xCpl");
							Node compl = listaCompl.item(0).getFirstChild();

							NodeList listaBairro = raiz.getElementsByTagName("xBairro");
							Node bairro = listaBairro.item(0).getFirstChild();

							NodeList listaCidade = raiz.getElementsByTagName("xMun");
							Node cidade = listaCidade.item(0).getFirstChild();

							NodeList listaCep = raiz.getElementsByTagName("CEP");
							Node cep = listaCep.item(0).getFirstChild();

							log.info("Endereço: " + rua.getNodeValue() + ", " + num.getNodeValue() + " "
									+ compl.getNodeValue() + " " + bairro.getNodeValue() + " " + cidade.getNodeValue()
									+ " CEP: " + cep.getNodeValue());

							// Passo 4: obter ie e cnpj o emitente
							// obter cnpj do emitente
							NodeList ListaCnpj = raiz.getElementsByTagName("CNPJ");
							Node cnpj = ListaCnpj.item(0).getFirstChild();

							NodeList listaIe = raiz.getElementsByTagName("IE");
							Node ie = listaIe.item(0).getFirstChild();

							log.info("CNPJ: " + cnpj.getNodeValue() + " IE: " + ie.getNodeValue());

							// Passo 5: obter cpf do destinatário
							/*
							 * NodeList listaCpf =
							 * raiz.getElementsByTagName("CPF"); if
							 * (listaCpf.item(0)!=null) { Node cpf =
							 * listaCpf.item(0).getFirstChild(); log.info(
							 * "CPF/CNPJ do consumidor: "+cpf.getNodeValue()); }
							 * 
							 * Node cpf=null;
							 */

							// Passo 6: obter dados da venda

							// Passo 7: localizar os elemento filhos do det
							NodeList listaDet = raiz.getElementsByTagName("det");

							// Passo 8: obter cada elemento do elemento det
							for (int i = 0; i < listaDet.getLength(); i++) {

								// como cada elemento do NodeList é um nó,
								// precisamos fazer o cast
								Element venda = (Element) listaDet.item(i);

								// Passo 9: obter o atributo id do det
								id = venda.getAttributeNode("nItem");
								log.info("\nID do produto: " + id.getValue());

								// Passo 10: obtem os elementos da venda
								NodeList listaCod = raiz.getElementsByTagName("cProd");
								cod = listaCod.item(0).getFirstChild();
								log.info("codigo: " + cod.getNodeValue());

								NodeList listaDescri = raiz.getElementsByTagName("xProd");
								descri = listaDescri.item(0).getFirstChild();
								log.info("Descrição: " + descri.getNodeValue());

								NodeList listaNcm = raiz.getElementsByTagName("NCM");
								ncm = listaNcm.item(0).getFirstChild();
								log.info("NCM: " + ncm.getNodeValue());

								NodeList listaCfop = raiz.getElementsByTagName("CFOP");
								cfop = listaCfop.item(0).getFirstChild();
								log.info("CFOP: " + cfop.getNodeValue());

								NodeList listaUn = raiz.getElementsByTagName("uCom");
								un = listaUn.item(0).getFirstChild();
								log.info("UN: " + un.getNodeValue());

								NodeList listaQtde = raiz.getElementsByTagName("qCom");
								qtde = listaQtde.item(0).getFirstChild();
								log.info("Quantidade: " + qtde.getNodeValue());

								NodeList listaValorUn = raiz.getElementsByTagName("vProd");
								valorUn = listaValorUn.item(0).getFirstChild();
								log.info("Valor Unit.: " + valorUn.getNodeValue());

								NodeList listaAliq = raiz.getElementsByTagName("vItem12741");
								aliq = listaAliq.item(0).getFirstChild();
								log.info("Aliquota: " + aliq.getNodeValue());

								NodeList listaValorItem = raiz.getElementsByTagName("vItem");
								valorItem = listaValorItem.item(0).getFirstChild();
								log.info("Valor do Item: " + valorItem.getNodeValue());

								NodeList listaIndRegra = raiz.getElementsByTagName("indRegra");
								indRegra = listaIndRegra.item(0).getFirstChild();
								log.info("Regra: " + indRegra.getNodeValue());

								NodeList listaDesc = raiz.getElementsByTagName("vDesc");
								indDesc = listaDesc.item(0).getFirstChild();
								log.info("Desconto: " + indDesc.getNodeValue());

								NodeList listaOutro = raiz.getElementsByTagName("vOutro");
								outro = listaOutro.item(0).getFirstChild();
								log.info("Outros: " + outro.getNodeValue());
							}

							// Passo 11: obter totais da venda
							// obter total bruta da venda
							NodeList listaTotalizador = raiz.getElementsByTagName("total");
							Element total = (Element) listaTotalizador.item(0);

							NodeList listaTotal = total.getElementsByTagName("vProd");
							Node totalVenda = listaTotal.item(0).getFirstChild();
							log.info("Total: " + totalVenda.getNodeValue());

							// obter o valor total de descontos, se houver
							NodeList listaDescTotal = total.getElementsByTagName("vDesc");
							if (listaDescTotal == null) {
								log.info("Desconto nulo");
							}
							Node descTotal = listaDescTotal.item(0).getFirstChild();
							log.info("Desconto total: " + descTotal.getNodeValue());

							// obter o valor total de acréscimos, se houver
							NodeList listaAcrescTotal = total.getElementsByTagName("vOutro");
							if (listaAcrescTotal == null) {
								log.info("Acréscimo nulo");
							}
							Node acrescTotal = listaAcrescTotal.item(0).getFirstChild();
							log.info("Acréscimo total: " + acrescTotal.getNodeValue());

							// obter o valor total de tributos
							NodeList listaTrib = total.getElementsByTagName("vCFeLei12741");
							Node trib = listaTrib.item(0).getFirstChild();
							log.info("Total de tributos: " + trib.getNodeValue());

							// Passo 12: obter informações do final do cupom
							// obter elemento infAdic
							NodeList listaInfo = raiz.getElementsByTagName("infAdic");
							Element info = (Element) listaInfo.item(0);

							// obter informações complementares
							NodeList listaInfCompl = info.getElementsByTagName("infCpl");
							Node infCompl = listaInfCompl.item(0).getFirstChild();
							log.info("Informações Compl.: " + infCompl.getNodeValue());

							// obter observações fisco
							NodeList listaObsFisco = info.getElementsByTagName("obsFisco");
							Node obsFisco = listaObsFisco.item(0).getFirstChild();
							log.info("Obs Fisco: " + obsFisco.getNodeValue());

							// Passo 13: Obter chave de acesso
							NodeList listaInfCFeChave = raiz.getElementsByTagName("infCFe");
							// como cada elemento do NodeList é um nó,
							// precisamos fazer o cast
							Element chave = (Element) listaInfCFeChave.item(0);
							// obter o atributo id do infCFe
							Attr idKeyAccess = chave.getAttributeNode("Id");
							log.info("Chave de acesso: " + idKeyAccess.getValue());

							// Passo 14: obter o QR Code
							NodeList listaAssQRCode = raiz.getElementsByTagName("assinaturaQRCODE");
							Node assQRCode = listaAssQRCode.item(0).getFirstChild();
							log.info("Ass. QRCode: " + assQRCode.getNodeValue());


							String BufTrans = "" +(char)27+(char)97+(char)1+nome.getNodeValue()+ (char) 27 + (char) 64;
							BufTrans += "\n"+(char)27+(char)97+(char)1+(char )27+(char)15+rua.getNodeValue()+(char)27+( char)80+ (char) 27 + (char) 64;
							BufTrans += " "+(char)27+(char)97+(char)1+(char )27+(char)15+num.getNodeValue()+(char)27+( char)80+ (char) 27 + (char) 64;
							BufTrans += " "+(char)27+(char)97+(char)1+(char )27+(char)15+compl.getNodeValue()+(char)27+( char)80+ (char) 27 + (char) 64;
							BufTrans += " "+(char)27+(char)97+(char)1+(char )27+(char)15+bairro.getNodeValue()+(char)27+( char)80+ (char) 27 + (char) 64;
							BufTrans += "\n"+(char)27+(char)97+(char)1+(char )27+(char)15+cidade.getNodeValue()+(char)27+( char)80+ (char) 27 + (char) 64;
							BufTrans += " CEP: "+(char)27+(char)97+(char)1 +(char )27+(char)15+ cep.getNodeValue()+(char)27+( char)80+ (char) 27 + (char) 64 + "\n";
							BufTrans += "CNPJ: "+(char)27+(char)97+(char)1 + (char )27+(char)15+cnpj.getNodeValue()+(char)27+( char)80+ (char) 27 + (char) 64 + " IE " + ie.getNodeValue() + "\n";													
							BufTrans = "__________________________________________________\n"+ (char) 27 + (char) 64;
							BufTrans += ""+(char)27+(char)97+(char)1+"EXTRATO No. 000000"+ (char) 27 + (char) 64;
							BufTrans += ""+(char)27+(char)97+(char)1+" \nCUPOM FISCAL ELETRÔNICO - SAT\n"+ (char) 27 + (char) 64;
							BufTrans += ""+(char)27+(char)97+(char)1+"= TESTE =\n"+ (char) 27 + (char) 64;	
							iRetorno = mp2032.ComandoTX(BufTrans, BufTrans.length());
							
							String TextoF2 = ">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>";
							TextoF2 += ">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>";
							TextoF2 += ">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>";
							iRetorno = mp2032.FormataTX(TextoF2, 2, 0, 0, 0, 1);
							
							String TextoF3 = "__________________________________________________";							
							TextoF3 += "CPF/CNPJ do consumidor: \n";
							TextoF3 = "__________________________________________________";
							iRetorno = mp2032.FormataTX(TextoF3, 2, 0, 0, 0, 0);
							
							String TextoF4 = ""+(char)27+(char)45+(char)1+"|COD  |  DESC  | QTD  |  UN  | VL UN R$ |(VL TR R$) * |  VL ITEM R$ \n"+(char)27+(char)45+(char)0;
							//TextoF4 += "-----------------------------------------------------------------\n";
							TextoF4 += id.getValue();
							TextoF4 += " " + cod.getNodeValue();
							TextoF4 += " " + descri.getNodeValue();
							TextoF4 += "\n " + qtde.getNodeValue();
							TextoF4 += " " + un.getNodeValue();
							TextoF4 += " X " + valorUn.getNodeValue();
							TextoF4 += " (" + aliq.getNodeValue() + ")";
							TextoF4 += "   "+valorItem.getNodeValue();
							iRetorno = mp2032.FormataTX(TextoF4, 1, 0, 0, 0, 0);

							
						} catch (IOException e) {
							log.error("Erro de IOException na decodificação do retorno: ", e);
							e.printStackTrace();
						} catch (SAXException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (ParserConfigurationException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}

					} catch (ArrayIndexOutOfBoundsException e) {
						e.printStackTrace();
						log.error(e);
						Alert alerta = new Alert(AlertType.ERROR);
						alerta.setTitle("Erro ArrayIndexOutOfBoundsException");
						alerta.setContentText(retorno);
						alerta.showAndWait();
					}
				}

			});

			// Botão imprimir
			// venda-----------------------------------------------------------

			BtImprimirVenda = new Button("Imprimir Cupom");
			BtImprimirVenda.setOnAction(new EventHandler<ActionEvent>() {

				@Override
				public void handle(ActionEvent event) {

					ImprimeCFe parserImprimeCFe = new ImprimeCFe();

					try {
						List<ConteudoXML> conteudo = parserImprimeCFe.leituraXML(arquivo.toString());
						for (ConteudoXML conteudoXML : conteudo) {
							System.out.println(conteudoXML);
							log.info(conteudoXML);
						}

					} catch (ParserConfigurationException e) {
						log.error(e);
						e.printStackTrace();
					} catch (SAXException e) {
						log.error(e);
						e.printStackTrace();
					} catch (IOException e) {
						log.error(e);
						e.printStackTrace();
					}

					/*
					 * LeitorXML leitorXml = new LeitorXML(); try {
					 * 
					 * leitorXml.LeituraXml(arquivo.toString());
					 * log.info(arquivo.toString()); } catch (SAXException e) {
					 * log.info(e); e.printStackTrace(); } catch (IOException e)
					 * { log.info(e); e.printStackTrace(); } catch
					 * (ParserConfigurationException e) { log.info(e);
					 * e.printStackTrace(); }
					 */

				}
			});

			// Posicionamento dos componentes no
			// Pane=======================================
			LbSessao.setTranslateX(20); // define orientação horizontal
			LbSessao.setTranslateY(10); // define orientação vertical

			BtImprimirVenda.setTranslateX(20);
			BtImprimirVenda.setTranslateY(160);

			mostraLogo.setTranslateX(470);
			mostraLogo.setTranslateY(10);

			TfSessao.setTranslateX(130);
			TfSessao.setTranslateY(10);

			LbCodAtivacao.setTranslateX(200);
			LbCodAtivacao.setTranslateY(10);

			tfCodAtivacao.setTranslateX(320);
			tfCodAtivacao.setTranslateY(10);

			BtConsultaSAT.setTranslateX(20);
			BtConsultaSAT.setTranslateY(50);
			BtConsultaSAT.setMinWidth(200);

			BtConsStatusOp.setTranslateX(240);
			BtConsStatusOp.setTranslateY(50);
			BtConsStatusOp.setMinWidth(200);

			TfChaveAcesso.setTranslateX(240);
			TfChaveAcesso.setTranslateY(100);
			TfChaveAcesso.setMinWidth(360);

			BtEnviarVenda.setTranslateX(20);
			BtEnviarVenda.setTranslateY(100);
			BtEnviarVenda.setMinWidth(200);

			Pane root = new Pane();
			root.getChildren().addAll(LbSessao, TfSessao, LbCodAtivacao, tfCodAtivacao, mostraLogo, BtConsultaSAT,
					BtConsStatusOp, TfChaveAcesso, BtEnviarVenda, BtImprimirVenda);
			root.getStyleClass().add("root");
			Scene scene = new Scene(root, 650, 650);
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			primaryStage.setScene(scene);
			primaryStage.show();

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public static void main(String[] args) {
		launch(args);

	}
}
