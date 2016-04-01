package application;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.InvocationTargetException;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Enumeration;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.regex.Pattern;

import javax.comm.*;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.sun.org.apache.xerces.internal.impl.dv.util.Base64;
import com.sun.org.apache.xerces.internal.impl.io.MalformedByteSequenceException;
import com.thoughtworks.xstream.XStream;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;

public class Main extends Application {

	static Logger log = Logger.getLogger(Main.class);

	BemaSAT Bema = BemaSAT.instance;
	MP2032 mp2032 = MP2032.instance;

	String retorno;
	int iRetorno;
	static String cfe = "";
	static String arquivoXml = "";
	static String xmlRetornoVenda;
	static File arquivo;
	int sessao;

	// Componentes
	Image logo;
	ImageView mostraLogo;
	Button BtSair;
	Label LbSessao;
	TextField TfSessao;
	Label LbCodAtivacao;
	TextField tfCodAtivacao;
	Random gerador;
	Button AbrePrograma;
	Button BtConsultaSAT;
	Button BtConsStatusOp;
	Button BtEnviarVenda;
	TextField TfChaveAcesso;
	Button BtImprimirVenda;
	String retornoStr[];
	TextField numSerieSat;
	Label lbNumSerieSat;

	Button localImpressora;
	Label TxCombo;
	ComboBox<String> CbModelo;
	Label TxPorta;
	TextField TfPorta;

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

	String serie;
	String xmlVenda;
	String xml;

	public void start(Stage primaryStage) throws NullPointerException {

		BasicConfigurator.configure();
		PropertyConfigurator.configure("lib/log4j.properties");
		log.info(
				"=============================================================================================================================================");
		log.info("Inicio da aplicação de Teste com a BemaSAT32");
		log.info("Desenvolvido por Julio Cesar Bruno - Bematech Software Partners");

		gerador = new Random();

		File dirTesteACBemaSAT = new File("C:\\APPBEMASAT");
		File dirCfe = new File("C:\\APPBEMASAT\\CFE");
		if (dirTesteACBemaSAT.mkdir()) {

			log.info("Pasta " + dirTesteACBemaSAT.getName() + " criada com sucesso!");
		} else {
			log.info("Não foi possível criar o diretório " + dirTesteACBemaSAT.getName() + " ou ele já existe.");
		}
		
		if (dirCfe.mkdir()) {

			log.info("Pasta " + dirCfe.getName() + " criada com sucesso!");
		} else {
			log.info("Não foi possível criar o diretório " + dirCfe.getName() + " ou ele já existe.");
		}

		// Consultar o SAT antes de iniciar o programa

		/*
		 * try { sessao = gerador.nextInt(999) * 100; retorno =
		 * Bema.ConsultarSAT(sessao); Alert alerta = new
		 * Alert(AlertType.INFORMATION); alerta.setHeaderText("Status SAT"); //
		 * alerta.setContentText("Mensagem de Retorno: " + retornoStr[2]);
		 * alerta.setContentText(retorno); alerta.showAndWait();
		 * 
		 * } catch (ArrayIndexOutOfBoundsException e) { log.info(
		 * "ConsultarSAT: " + e); e.printStackTrace(); Alert alerta = new
		 * Alert(AlertType.ERROR); alerta.setHeaderText("Status SAT");
		 * alerta.setContentText(retorno); alerta.showAndWait();
		 * 
		 * } catch (RuntimeException e) { e.printStackTrace(); Alert alerta =
		 * new Alert(AlertType.ERROR); alerta.setHeaderText("Status SAT");
		 * alerta.setContentText(e.toString()); alerta.showAndWait(); }
		 */

		TextInputDialog codAtivacao = new TextInputDialog("900006420");
		codAtivacao.setHeaderText("Teste BemaSAT");
		codAtivacao.setHeaderText("Insira as informações");
		codAtivacao.setContentText("Número de série do SAT sem o digito:");						

		// maneira tradicional para obter o valor de resposta.
		Optional<String> result = codAtivacao.showAndWait();		
			
		numSerieSat = new TextField(result.get());
		lbNumSerieSat = new Label("Série do SAT ");	
			
		if (result.isPresent()) {

			try {

				/*
				 * Enumeration comLista =
				 * CommPortIdentifier.getPortIdentifiers(); CommPortIdentifier
				 * cpi = (CommPortIdentifier)comLista.nextElement();
				 * 
				 * ObservableList lista = FXCollections.observableArrayList();
				 * 
				 * 
				 * //lista.add(e);
				 * 
				 * ComboBox<String> combo = new ComboBox<String>(); String
				 * portas; for (int i = 0; i < retornoStr.length; i++) {
				 * 
				 * } combo.getItems().add(portas);
				 */

				TxCombo = new Label("Selecione o modelo da impressora: ");
				ObservableList<String> options = FXCollections.observableArrayList("MP-2500 TH", "MP-4200 TH",
						"MP-100S TH");
				CbModelo = new ComboBox<String>(options);
				CbModelo.setValue("MP-4200 TH");
				TxPorta = new Label("Selecione a porta: ");
				TfPorta = new TextField("COM4");

				localImpressora = new Button("Conectar Impressora");
				localImpressora.setOnAction(new EventHandler<ActionEvent>() {

					@Override
					public void handle(ActionEvent arg0) {
						/*int modelo = 0;
						if (CbModelo.getValue() == "MP-2500 TH") {
							modelo = 8;
						}
						if (CbModelo.getValue() == "MP-4200 TH") {
							modelo = 7;
						}
						if (CbModelo.getValue() == "MP-100S TH") {
							modelo = 7;
						}

						iRetorno = mp2032.ConfiguraModeloImpressora(modelo);
						if (iRetorno == 1) {
							Alert alerta = new Alert(AlertType.INFORMATION);
							alerta.setHeaderText("Retorno Impressora");
							alerta.setContentText(
									"Impressora modelo " + CbModelo.getValue() + " encontrada com sucesso!");
							alerta.showAndWait();
						} else {
							Alert alerta = new Alert(AlertType.ERROR);
							alerta.setHeaderText("Retorno Impressora");
							alerta.setContentText("Impressora não encontrada!");
							alerta.showAndWait();
						}*/

						iRetorno = mp2032.IniciaPorta(TfPorta.getText());
						if (iRetorno == 1) {
							Alert alerta = new Alert(AlertType.INFORMATION);
							alerta.setHeaderText("Retorno Impressora");
							alerta.setContentText(
									"Impressora conectada na porta " + TfPorta.getText() + " com sucesso!");
							alerta.showAndWait();
						} else {
							Alert alerta = new Alert(AlertType.ERROR);
							alerta.setHeaderText("Retorno Impressora");
							alerta.setContentText("Impressora não conectada!");
							alerta.showAndWait();
						}

					}
				});

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
						// gera número da sessão com 5 digitos
						try {
							int sessao = gerador.nextInt(999) * 100;
							log.info("Inicia a execução da função ConsultarSAT");
							log.info("Parâmetros da função ");
							log.info("Sessão: " + sessao);
							retorno = Bema.ConsultarSAT(sessao);
							byte ptext[] = null;
							try {
								ptext = retorno.getBytes("UTF8");
							} catch (UnsupportedEncodingException e) {
								e.printStackTrace();
							}
							for (int i = 0; i < ptext.length; i++) {
								System.out.print(ptext[i] + ",");
							}
							log.debug(BtConsultaSAT);
							log.info("Retorno da Execução");
							log.info(retorno);
							log.info("Fim da execução da Função ConsultarSAT");
							log.info("");
							String retornoStr[] = retorno.split(Pattern.quote("|"));

							TfSessao.setText(Integer.toString(sessao));
							Alert AlRetorno = new Alert(AlertType.INFORMATION);

							AlRetorno.setHeaderText("Retorno do S@T");
							AlRetorno.setHeaderText("Retorno da Função ConsultaSAT");

							AlRetorno.setContentText(
									"Número de Sessão: " + retornoStr[0] + "\n\r" + "Código de Retorno: "
											+ retornoStr[1] + "\n\r" + "Mensagem de Retorno: " + retornoStr[2]);
							AlRetorno.showAndWait();
						} catch (ArrayIndexOutOfBoundsException e) {
							log.info("ConsultarSAT: " + e);
							e.printStackTrace();
							Alert alerta = new Alert(AlertType.ERROR);
							alerta.setHeaderText("Status SAT");
							alerta.setContentText(retorno);
							alerta.showAndWait();
						}

					}
				});

				// ConsultaStatusOperacional--------------------------------------------------------------
				BtConsStatusOp = new Button("Consultar Status Operacional");
				BtConsStatusOp.setOnAction(new EventHandler<ActionEvent>() {

					public void handle(ActionEvent event) {
						// Consulta Status operacional

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
							AlRetorno.setHeaderText("Retorno do S@T");
							AlRetorno.setHeaderText("Retorno da Função ConsultarStatusOperacional");
							AlRetorno.setContentText("Mensagem de Retorno: " + retornoStr[2]);
							TextArea textArea = new TextArea();
							textArea.setEditable(false);
							textArea.setWrapText(true);
							textArea.setText("Número de Sessão: " + retornoStr[0] + "\n\r" + "Código de Retorno: "
									+ retornoStr[1] + "\n\r" + "Mensagem de Retorno: " + retornoStr[2] + "\n\r"
									+ "Código Mensagem SEFAZ:  " + retornoStr[3] + "\n\r" + "Mensagem da Sefaz: "
									+ retornoStr[4] + "\n\r" + "Número de Série: " + retornoStr[5] + "\n\r"
									+ "Tipo da Lan: " + retornoStr[6] + "\n\r" + "IP: " + retornoStr[7] + "\n\r"
									+ "Mac Address: " + retornoStr[8] + "\n\r" + "Máscara de Rede: " + retornoStr[9]
									+ "\n\r" + "Gateway: " + retornoStr[10] + "\n\r" + "DNS Primário: " + retornoStr[11]
									+ "\n\r" + "DNS Secundário: " + retornoStr[12] + "\n\r" + "Status da Lan: "
									+ retornoStr[13] + "\n\r" + "Nível da Bateria: " + retornoStr[14] + "\n\r"
									+ "Total de Memória: " + retornoStr[15] + "\n\r" + "Memória Utilizada: "
									+ retornoStr[16] + "\n\r" + "Data e Hora Atual: " + retornoStr[17] + "\n\r"
									+ "Versão Software Básico: " + retornoStr[18] + "\n\r" + "Versão do Layout: "
									+ retornoStr[19] + "\n\r" + "Último CF-e Enviado: " + retornoStr[20] + "\n\r"
									+ "Primeiro CF-e Armazenado: " + retornoStr[21] + "\n\r"
									+ "Último CF-e Armazenado: " + retornoStr[22] + "\n\r"
									+ "Última Transmissão de CF-e: " + retornoStr[23] + "\n\r"
									+ "Última Comunicação com SEFAZ: " + retornoStr[24] + "\n\r"
									+ "Emissão do Certificado: " + retornoStr[25] + "\n\r"
									+ "Vencimento do Certificado: " + retornoStr[26] + "\n\r");

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
							alerta.setHeaderText("Status SAT");
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

					public void handle(ActionEvent event) {
						// Enviar dados venda

						try {
							// Pega o xml escolhido pelo usuário
							FileChooser abreArq = new FileChooser();
							abreArq.setTitle("Abrir arquivo XML");
							abreArq.getExtensionFilters().addAll(new ExtensionFilter("Arquivo XML", "*.xml"));
							File arqXML = abreArq.showOpenDialog(null);

							FileReader fr = null;
							BufferedReader br = null;
							StringBuffer Retorno = new StringBuffer();
							try {
								fr = new FileReader(arqXML);
								br = new BufferedReader(fr);
								String linha;
								while ((linha = br.readLine()) != null) {
									Retorno.append(linha.trim());
								}
							} catch (FileNotFoundException e) {
								e.printStackTrace();
							} catch (IOException e) {
								e.printStackTrace();
							} finally {
								try {
									if (br != null)
										br.close();
									if (fr != null)
										fr.close();
								} catch (IOException e) {
									e.printStackTrace();
								}
							}
							xml = Retorno.toString();

							log.info("Arquivo aberto: " + xml.toString());

							int sessao = gerador.nextInt(999) * 100;
							log.info("Inicia a execução da função EnviarDadosVenda");
							log.info("Parâmetros da função ");
							log.info("Sessão: " + sessao);
							log.info("Código de ativação: " + tfCodAtivacao.getText());
							log.info("XML de venda: " + xml);
							retorno = Bema.EnviarDadosVenda(sessao, tfCodAtivacao.getText(), xml);
							log.debug(BtEnviarVenda);
							log.info("Retorno da Execução: ");
							log.info(retorno);
							TfSessao.setText(Integer.toString(sessao));

							String retornoStr[] = retorno.split(Pattern.quote("|"));

							Alert AlRetorno = new Alert(AlertType.INFORMATION);
							AlRetorno.setWidth(550);
							AlRetorno.setHeaderText("Retorno do S@T");
							AlRetorno.setHeaderText("Retorno da Função EnviarDadosVenda");
							AlRetorno.setContentText("Mensagem de Retorno: " + retornoStr[3]);
							TextArea textArea = new TextArea();
							textArea.setEditable(false);
							textArea.setWrapText(true);
							textArea.setText("Número de Sessão: " + retornoStr[0] + "\n\r" + "Código de Retorno: "
									+ retornoStr[1] + "\n\r" + "Código de retorno de cancelamento: " + retornoStr[2]
									+ "\n\r" + "Mensagem de Retorno: " + retornoStr[3] + "\n\r" + "Código SEFAZ: "
									+ retornoStr[4] + "\n\r" + "Mensagem SEFAZ: " + retornoStr[5] + "\n\r"
									+ "Data e hora da emissão: " + retornoStr[7] + "\n\r" + "Chave de acesso: "
									+ retornoStr[8] + "\n\r" + "Valor Total CF-e: " + retornoStr[9] + "\n\r"
									+ "Número do CPF ou CNPJ do adquirente: " + retornoStr[10] + "\n\r"
									+ "Assinatura QRCode: " + retornoStr[11] + "\n\r");

							textArea.setMaxWidth(Double.MAX_VALUE);
							textArea.setMaxHeight(Double.MAX_VALUE);
							GridPane.setVgrow(textArea, Priority.ALWAYS);
							GridPane.setHgrow(textArea, Priority.ALWAYS);

							GridPane expContent = new GridPane();
							expContent.setMaxWidth(Double.MAX_VALUE);
							expContent.add(textArea, 0, 0);
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

								// Cria o arquivo xml com a chave de acesso no
								// C:\
								cfe = retornoStr[8];
								arquivo = new File("C:\\APPBEMASAT\\CFE\\" + cfe + ".xml");
								log.info("Criando o arquivo: " + arquivo.toString());
								FileOutputStream fos = new FileOutputStream(arquivo);
								fos.write(decoded.getBytes());

								// LEITURA XML
								// +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
								// fazer o parse do arquivo e criar o documento
								// XML
								Document docx = null;
								try {
									DocumentBuilderFactory dbfx = DocumentBuilderFactory.newInstance();
									DocumentBuilder dbx = dbfx.newDocumentBuilder();
									docx = dbx.parse(arquivo.toString());
									
								} catch (MalformedByteSequenceException e) {
									e.printStackTrace();
									Alert alerta = new Alert(AlertType.ERROR);
									alerta.setHeaderText("Erro XML");
									alerta.setContentText("XML com acentuações ou caracteres inválidos, por favor verifique!");
									alerta.showAndWait();
								}
								

								// Passo 1: obter o elemento raiz
								Element raiz = docx.getDocumentElement();

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
										+ compl.getNodeValue() + " " + bairro.getNodeValue() + " "
										+ cidade.getNodeValue() + " CEP: " + cep.getNodeValue());

								// Passo 4: obter ie e cnpj o emitente
								// obter cnpj do emitente
								NodeList ListaCnpj = raiz.getElementsByTagName("CNPJ");
								Node cnpj = ListaCnpj.item(0).getFirstChild();

								NodeList listaIe = raiz.getElementsByTagName("IE");
								Node ie = listaIe.item(0).getFirstChild();
								log.info("CNPJ: " + cnpj.getNodeValue() + " IE: " + ie.getNodeValue());

								// Passo 5: obter cpf do destinatário											
								NodeList listaCpf = raiz.getElementsByTagName("CPF");
								Node cpf = listaCpf.item(0).getFirstChild();
								String sCpf;
								if (cpf == null) {
									sCpf = "Não informado";
									log.info("Sem CPF");
								} else {
									sCpf = cpf.getNodeValue();
									log.info("CPF/CNPJ do consumidor: "+cpf.getNodeValue());									
								}																

								// Passo 6: obter dados da venda

								// Passo 7: localizar os elemento filhos do det
								NodeList listaDet = raiz.getElementsByTagName("det");

								// Passo 8: obter cada elemento do elemento
								// det
								for (int i = 0; i < listaDet.getLength(); i++) {

									// como cada elemento do NodeList é um nó,
									// precisamos fazer o cast
									Element venda = (Element) listaDet.item(i);

									// Passo 9: obter o atributo id do det
									id = venda.getAttributeNode("nItem");
									log.info("\n\rID do produto: " + id.getValue());

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

								// obter o valor total do CF-e
								NodeList totalCFe = total.getElementsByTagName("vCFe");
								Node vCFe = totalCFe.item(0).getFirstChild();
								log.info("Total CF-e: " + vCFe.getNodeValue());

								// obter valor total de tributos
								NodeList listaTotalTrib = total.getElementsByTagName("vCFeLei12741");
								Node totalTrib = listaTotalTrib.item(0).getFirstChild();
								log.info("Valor total de tributos em R$: " + totalTrib.getNodeValue());

								// Passo 12: obter dados do pagamento
								NodeList listaPag = raiz.getElementsByTagName("pgto");
								Element pgto = (Element) listaPag.item(0);

								// obter a forma de pagamento
								NodeList listaMeioPag = pgto.getElementsByTagName("cMP");
								Node meioPag = listaMeioPag.item(0).getFirstChild();
								log.info("Inidice Forma Pagamento: " + meioPag.getNodeValue());

								// obter o valor do pagamento
								NodeList listaValorPag = pgto.getElementsByTagName("vMP");
								Node valorPag = listaValorPag.item(0).getFirstChild();
								log.info("Valor Pagamento: " + valorPag.getNodeValue());

								// Passo 13: obter dados do pagamento
								// obter a forma de pagamento
								NodeList listaTroco = pgto.getElementsByTagName("vTroco");
								Node troco = listaTroco.item(0).getFirstChild();
								log.info("Troco: " + troco.getNodeValue());

								// Passo 14: obter informações do final do
								// cupom
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

								// Passo 15: obter chave de acesso
								NodeList listaInfCFeChave = raiz.getElementsByTagName("infCFe");
								// como cada elemento do NodeList é um nó,
								// precisamos fazer o cast
								Element chave = (Element) listaInfCFeChave.item(0);
								// obter o atributo id do infCFe
								Attr idKeyAccess = chave.getAttributeNode("Id");
								log.info("Chave de acesso: " + idKeyAccess.getValue());

								// Passo 16: obter o QR Code
								NodeList listaAssQRCode = raiz.getElementsByTagName("assinaturaQRCODE");
								Node assQRCode = listaAssQRCode.item(0).getFirstChild();
								log.info("Ass. QRCode: " + assQRCode.getNodeValue());
								
								//Passo 17: obter número do caixa
								NodeList listaNumCaixa = raiz.getElementsByTagName("numeroCaixa");
								Node numCaixa = listaNumCaixa.item(0).getFirstChild();
								log.info("Num. Caixa: " + numCaixa.getNodeValue());

								/*
								 * COMANDOS DE FORMATAÇÃO===============
								 * 
								 * (char)27+(char)97+(char)1 = centralizado
								 * (char)27+(char)51+(char)18 = espaço entre
								 * linhas menor (char)27+(char)69 = negrito
								 * (char)27+(char)70 = fecha negrito
								 * (char)27+(char)15 = condensado
								 * (char)27+(char)80 = fecha condensado
								 * (char)27+(char)64 = reset impressora
								 * 
								 * ======================================
								 */
								/*
								 * FORMATATX==========================
								 * TipoLetra: 1 = comprimido 2 = normal 3 =
								 * elite
								 * 
								 * Itálico: 1 = ativa o modo itálico. 0 =
								 * desativa o modo itálico
								 * 
								 * Sublinhado: 1 = ativa o modo sublinhado 0 =
								 * desativa o modo sublinhado
								 * 
								 * Expandido: 1 = ativa o modo expandido 0 =
								 * desativa o modo expandido
								 * 
								 * Enfatizado (negrito): 1 = ativa o modo
								 * enfatizado 0 = desativa o modo enfatizado
								 * =====================================
								 */	
								

								String name = "logo.bmp";
								mp2032.ImprimeBitmap(name , 0);
								
								String BufTrans = "" + (char) 27 + (char) 51 + (char) 18 + (char) 27 + (char) 97 + (char) 1;
								mp2032.ComandoTX(BufTrans, BufTrans.length());
								
								String cabecalho = "" + nome.getNodeValue() + "\n\r";
								cabecalho += rua.getNodeValue() + " ";
								cabecalho += num.getNodeValue() + " ";
								cabecalho += compl.getNodeValue() + " ";
								cabecalho += bairro.getNodeValue() + "\n\r";
								cabecalho += cidade.getNodeValue();
								cabecalho += " CEP: " + cep.getNodeValue() + "\n\r";
								cabecalho += "CNPJ: " + cnpj.getNodeValue() + " IE " + ie.getNodeValue() + "\n\r";
								cabecalho += "__________________________________________________\n\r";
								cabecalho += "EXTRATO No. 000000";
								cabecalho += " \n\rCUPOM FISCAL ELETRÔNICO - SAT\n\r";
								cabecalho += "= TESTE =\n\r";
								cabecalho += ">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>";
								cabecalho += ">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>";
								cabecalho += ">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>";
								cabecalho += "__________________________________________________";
								mp2032.BematechTX(cabecalho);
								log.info("\n"+cabecalho);							

								String infoCpf = "CPF/CNPJ do consumidor: "+sCpf+"\n";
								infoCpf += 		 "___________________________________________________________________";
								infoCpf += 		 "ITEM |COD|  DESC  | QTD | UN | VL UN R$ |(VL TR R$) * | VL ITEM R$ ";
								infoCpf += 		 "___________________________________________________________________";
								mp2032.FormataTX(infoCpf, 1, 0, 0, 0, 0);
								log.info("\n"+infoCpf);								
								
								String infoVenda = "";
								String infoVenda2 = "";
								String infoVenda3 = "";
								String somaLinha = "";
							//	int cont = Integer.parseInt(id.getValue());
								for (int i = 0; i < listaDet.getLength(); i++) 
								{
									//System.out.printf("%-30s : %50s%n", prop.getKey(), prop.getValue());
									
									String impVenda = String.format("%03d", i+1)  + " " + cod.getNodeValue() + " " + descri.getNodeValue() + " ";
									infoVenda = String.format("%-65s", impVenda);
									mp2032.FormataTX(infoVenda, 1, 0, 0, 0, 0);
									
									String infoUn = "\n" + qtde.getNodeValue() + " " + un.getNodeValue() + " X " + valorUn.getNodeValue() + " ("
											  + aliq.getNodeValue() + ") ";																	
									String infoItem = valorItem.getNodeValue();
									//infoVenda3 = String.format("%"+soma+"s ", infoItem)+"\n";	
									infoVenda3 = String.format("%-30s  %34s%n",infoUn, infoItem);
									mp2032.FormataTX(infoVenda3, 1, 0, 0, 0, 0);
								}																

								String it = "\nTotal bruto do item ";
								String item = String.format("%-20s  %28s%n",it, totalVenda.getNodeValue());
								
								item += "\n\r                  ------------------------------";
								mp2032.BematechTX(item);
								log.info("\n"+item);

								String iTot = "\nTOTAL R$ ";
								String infoTot = String.format("%-24s  %24s%n",iTot , vCFe.getNodeValue());

								mp2032.FormataTX(infoTot, 2, 0, 0, 0, 1);
								log.info("\n"+infoTot);

								String pag = "";
								switch (meioPag.getNodeValue()) {
								case "01":
									pag = "\nDinheiro ";
									break;
								case "02":
									pag = "\nCheque ";
									break;
								case "03":
									pag = "\nCartão de Crédito ";
									break;
								case "04":
									pag = "\nCartão de Débito ";
									break;
								case "05":
									pag = "\nCrédito Loja ";
									break;
								case "10":
									pag = "\nVale Alimentação ";
									break;
								case "11":
									pag = "\nVale Refeição ";
									break;
								case "12":
									pag = "\nVale Presente ";
									break;
								case "13":
									pag = "\nVale Combustível ";
									break;
								case "99":
									pag = "\nOutros ";
									break;
								default:
									break;
								}
								String pagto = String.format("%-24s  %24s%n",pag, valorPag.getNodeValue());
								mp2032.BematechTX(pagto);
								log.info("\n"+pagto);
								
								String pagInfTr = "Troco R$";
								String pagamento = String.format("%-24s  %24s%n", pagInfTr, troco.getNodeValue());
								mp2032.BematechTX(pagamento);
								log.info("\n"+pagamento);

								String mensFis = "__________________________________________________";
								String fisco = "";
								if ((obsFisco.getNodeValue() == "xTexto1") || (obsFisco.getNodeValue() == null)){
									fisco =      "\nNão há mensagem do Fisco                          ";												
								} else {
									fisco = obsFisco.getNodeValue();
								}
								mensFis += fisco;
								mp2032.BematechTX(mensFis);
								log.info("\n"+mensFis);
								
								String mensagens = "_______________________________________________________________";
								mensagens += "\n\rOBSERVAÇÕES DO CONTRIBUINTE\n\r";
								mensagens += infCompl.getNodeValue() + "\n\r";
								mensagens += "Valor aproximado dos tributos deste cupom em R$ ";
								mensagens += totalTrib.getNodeValue();
								mensagens += "\n\r(Conforme Lei Fed. 12.741/2012)";
								mensagens += "\n\rValor aproximado dos tributos do item\n\r";
								mensagens += "_______________________________________________________________";
								mp2032.FormataTX(mensagens, 1, 0, 0, 0, 0);
								log.info("\n"+mensagens);							

								String centro2 = ""+(char)27+(char)97+(char)0;
								mp2032.ComandoTX(centro2, centro2.length());
								
								String numSerie = numSerieSat.getText();
								String serie1 = numSerie.substring(0, 3) + ".";
								String serie2 = numSerie.substring(3, 6) + ".";
								String serie3 = numSerie.substring(6, 9);
								serie = serie1 + serie2 + serie3;
								String satNum = "\n\rSAT No. " + serie + "\n\r";
								mp2032.FormataTX(satNum, 2, 0, 0, 0, 1);
								log.info("\n"+satNum);

								// Data formatada
								SimpleDateFormat sd = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
								Date dataAtual = new Date();
								String BufData = sd.format(dataAtual) + "\n";
								mp2032.BematechTX(BufData);
								log.info("\n"+BufData);

								String textoChave = idKeyAccess.getValue().substring(3, 47);
								log.info("textoChave: " + textoChave);
								String infoChave = textoChave.replaceAll("(.{4})", "$1 ");
								mp2032.FormataTX(infoChave, 1, 0, 0, 0, 1);
								log.info("\n"+infoChave);

								int Altura = 60;
								int Largura = 0;
								int PosicaoCaracteres = 0;
								int Fonte = 0;
								int Margem = 0;
								iRetorno = mp2032.ConfiguraCodigoBarras(Altura, Largura, PosicaoCaracteres, Fonte,
										Margem);
								String Codigo = idKeyAccess.getValue();
								StringBuffer key2 = new StringBuffer(Codigo);
								key2.deleteCharAt(3); // exclui as 3 primeiras
														// letras

								log.info("Chave de Acesso: " + idKeyAccess.getValue());
								iRetorno = mp2032.ImprimeCodigoBarrasCODE128(textoChave);

								/*
								 * A cadeia de caracteres para geração do QR
								 * Code , quando o CPF ou CNPJ é informado,
								 * possui a formatação:
								 * 
								 * chaveConsulta|timeStamp|valorTotal|
								 * CPFCNPJValue| assinaturaQRCODE
								 * 
								 * Para o caso em que nem o CPF e nem o CNPJ são
								 * informados, a formatação a ser seguida é
								 * conforme abaixo (observar o pipe duplo):
								 * 
								 * chaveConsulta|timeStamp|valorTotal||
								 * assinaturaQRCODE
								 */

								int errorCorrectionLevel = 1;
								int moduleSize = 5;
								int codeType = 0;
								int QRCodeVersion = 10;
								int encodingModes = 1;
								String codeQr = "";
								if (retornoStr[10] != null) {
									codeQr = textoChave + "|" + retornoStr[7] + "|" + retornoStr[9] + "|" + retornoStr[10] + "|"
											+ retornoStr[11];
								} else {
									codeQr = textoChave + "|" + retornoStr[7] + "|" + retornoStr[9] + "||" + retornoStr[11] + "|";
								}
								iRetorno = mp2032.ImprimeCodigoQRCODE(errorCorrectionLevel, moduleSize, codeType,
										QRCodeVersion, encodingModes, codeQr);
																
								/*String fCentro2 = "" + (char) 27 + (char) 64;// fecha
																				// centralização
								mp2032.ComandoTX(fCentro2, fCentro2.length());*/
								
								String sNumCaixa = "\nNúmero do Caixa: " + numCaixa.getNodeValue()+" - Aplicativo de testes - BSP Bematech S/A";
								mp2032.FormataTX(sNumCaixa, 1, 0, 0, 0, 0);

								int Modo = 1;
								iRetorno = mp2032.AcionaGuilhotina(Modo);


							} catch (IOException e) {
								log.error("Erro de IOException na decodificação do retorno: ", e);
								e.printStackTrace();
							} catch (SAXException e) {
								// SAXException
								e.printStackTrace();
							} catch (ParserConfigurationException e) {
								// ParserConfigurationException
								e.printStackTrace();
							}

						} catch (ArrayIndexOutOfBoundsException e) {
							e.printStackTrace();
							log.error(e);
							Alert alerta = new Alert(AlertType.ERROR);
							alerta.setHeaderText("Erro ArrayIndexOutOfBoundsException");
							alerta.setContentText(retorno);
							alerta.showAndWait();
							
						} 
					}

				});


				// Posicionamento dos componentes no
				// Pane=======================================
				
				mostraLogo.setTranslateX(470);
				mostraLogo.setTranslateY(20);

				TxPorta.setTranslateX(20); // define orientação horizontal
				TxPorta.setTranslateY(20); // define orientação vertical

				TfPorta.setTranslateX(120);
				TfPorta.setTranslateY(20);
				TfPorta.setMaxWidth(80);

				localImpressora.setTranslateX(210);
				localImpressora.setTranslateY(20);

				LbSessao.setTranslateX(20); 
				LbSessao.setTranslateY(60);
				
				TfSessao.setTranslateX(130);
				TfSessao.setTranslateY(60);

				LbCodAtivacao.setTranslateX(200);
				LbCodAtivacao.setTranslateY(60);

				tfCodAtivacao.setTranslateX(320);
				tfCodAtivacao.setTranslateY(60);												

				BtConsultaSAT.setTranslateX(20);
				BtConsultaSAT.setTranslateY(100);
				BtConsultaSAT.setMinWidth(160);

				BtConsStatusOp.setTranslateX(210);
				BtConsStatusOp.setTranslateY(100);
				BtConsStatusOp.setMinWidth(160);
				
				lbNumSerieSat.setTranslateX(410);
				lbNumSerieSat.setTranslateY(110);
				
				numSerieSat.setTranslateX(490);
				numSerieSat.setTranslateY(110);
				numSerieSat.setMaxWidth(80);

				TfChaveAcesso.setTranslateX(210);
				TfChaveAcesso.setTranslateY(160);
				TfChaveAcesso.setMinWidth(360);

				BtEnviarVenda.setTranslateX(20);
				BtEnviarVenda.setTranslateY(160);
				BtEnviarVenda.setMinWidth(150);
				
				/*BtImprimirVenda.setTranslateX(20);
				BtImprimirVenda.setTranslateY(220);*/

				Pane root = new Pane();
				root.getChildren().addAll(TxPorta, TfPorta, localImpressora, LbSessao,
						TfSessao, LbCodAtivacao, tfCodAtivacao, mostraLogo, BtConsultaSAT, BtConsStatusOp,
						TfChaveAcesso, BtEnviarVenda, lbNumSerieSat, numSerieSat);
				root.getStyleClass().add("root");
				Scene scene = new Scene(root, 650, 350);
				scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
				primaryStage.setScene(scene);
				primaryStage.setTitle("Teste BemaSAT    SAT: " + result.get());
				primaryStage.getIcons().add(logo);
				primaryStage.show();

			} catch (Exception e) {
				e.printStackTrace();
			}
		}

	}

	public static void main(String[] args) {
		launch(args);

	}
}
