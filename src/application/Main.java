package application;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Enumeration;
import java.util.HashMap;
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

	String retorno = "";
	static String cfe = "";
	static String arquivoXml = "";
	static String xmlRetornoVenda;
	File arquivo; 

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
	ComboBox<String> comboBox;
	ObservableList<String> options;
	
	
	public void start(Stage primaryStage) {
		
		BasicConfigurator.configure();
		PropertyConfigurator.configure("lib/log4j.properties");
		log.info("=============================================================================================================================================");
		log.info("Inicio da aplicação de Teste com a BemaSAT32");
		log.info("Desenvolvido por Julio Cesar Bruno - Bematech Software Partners");
		
		
		gerador = new Random();
		
		File dirTesteACBemaSAT = new File("C:\\APPBEMASAT");
		if (dirTesteACBemaSAT.mkdir()) {
			
			log.info("Pasta "+ dirTesteACBemaSAT.getName()+" criada com sucesso!");
		}else {
			log.info("Não foi possível criar o diretório "+dirTesteACBemaSAT.getName()+" ou ele já existe.");
		}
		
		try {
			
		/*///PORTAS SERIAIS====================================================================================
		
		// captura a lista de portas disponíveis, 
		// pelo método estético em CommPortIdentifier. 
		Enumeration pList = CommPortIdentifier.getPortIdentifiers(); 
		
		// Um mapping de nomes para CommPortIdentifiers. 
		HashMap map = new HashMap(); 
		
		// Procura pela porta desejada 
		while (pList.hasMoreElements()) {         
			CommPortIdentifier cpi = (CommPortIdentifier)pList.nextElement(); 
			map.put(cpi.getName(), cpi);         
			if (cpi.getPortType() == CommPortIdentifier.PORT_SERIAL) {                 
				ObservableList<String> options = FXCollections.observableArrayList(cpi.getName());
				final ComboBox<String> comboBox = new ComboBox(options);    
				} 
		}
		
		//===================================================================================================
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
					int sessao = gerador.nextInt(999)*100;
					log.info("Inicia a execução da função ConsultarSAT");
					log.info("Parâmetros da função ");
					log.info("Sessão: "+sessao);
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
					
					AlRetorno.setContentText("Número de Sessão: "+retornoStr[0] + "\n" +"Código de Retorno: "+retornoStr[1] + "\n" +
							"Mensagem de Retorno: "+retornoStr[2]);
					AlRetorno.showAndWait();

				}
			});

			// ConsultaStatusOperacional--------------------------------------------------------------
			BtConsStatusOp = new Button("Consultar Status Operacional");
			BtConsStatusOp.setOnAction(new EventHandler<ActionEvent>() {

				public void handle(ActionEvent event) {
					// TODO ConsultaStatusOperacional

					// gera n�mero da sess�o com 5 digitos
					int sessao = gerador.nextInt(999) * 100;
					log.info("Inicia a execução da função ConsultarStatusOperacional");
					log.info("Parâmetros da função ");
					log.info("Sessão: "+sessao);
					log.info("Código de ativação: "+tfCodAtivacao.getText());					
					retorno = Bema.ConsultarStatusOperacional(sessao,
							tfCodAtivacao.getText());					
					String retornoStr[] = retorno.split(Pattern.quote("|"));
					log.info(retornoStr[1]);
					TfSessao.setText(Integer.toString(sessao));					
					Alert AlRetorno = new Alert(AlertType.INFORMATION);
					AlRetorno.setWidth(550);
					AlRetorno.setTitle("Retorno do S@T");
					AlRetorno
							.setHeaderText("Retorno da Função ConsultarStatusOperacional");
					AlRetorno.setContentText("Mensagem de Retorno: " + retornoStr[2]);
					TextArea textArea = new TextArea();
					textArea.setEditable(false);
					textArea.setWrapText(true);
					textArea.setText("Número de Sessão: "
							+ retornoStr[0] + "\n" + "Código de Retorno: "
							+ retornoStr[1] + "\n" + "Mensagem de Retorno: "
							+ retornoStr[2] + "\n" + "Código Mensagem SEFAZ:  "
							+ retornoStr[3] + "\n" + "Mensagem da Sefaz: "
							+ retornoStr[4] + "\n" + "Número de Série: "
							+ retornoStr[5] + "\n" + "Tipo da Lan: "
							+ retornoStr[6] + "\n" + "IP: " + retornoStr[7]
							+ "\n" + "Mac Address: " + retornoStr[8] + "\n"
							+ "Máscara de Rede: " + retornoStr[9] + "\n"
							+ "Gateway: " + retornoStr[10] + "\n"
							+ "DNS Primário: " + retornoStr[11] + "\n"
							+ "DNS Secundário: " + retornoStr[12] + "\n"
							+ "Status da Lan: " + retornoStr[13] + "\n"
							+ "Nível da Bateria: " + retornoStr[14] + "\n"
							+ "Total de Memória: " + retornoStr[15] + "\n"
							+ "Memória Utilizada: " + retornoStr[16] + "\n"
							+ "Data e Hora Atual: " + retornoStr[17] + "\n"
							+ "Versão Software Básico: " + retornoStr[18]
							+ "\n" + "Versão do Layout: " + retornoStr[19]
							+ "\n" + "Último CF-e Enviado: " + retornoStr[20]
							+ "\n" + "Primeiro CF-e Armazenado: "
							+ retornoStr[21] + "\n"
							+ "Último CF-e Armazenado: " + retornoStr[22]
							+ "\n" + "Última Transmissão de CF-e: "
							+ retornoStr[23] + "\n"
							+ "Última Comunicação com SEFAZ: " + retornoStr[24]
							+ "\n" + "Emissão do Certificado: "
							+ retornoStr[25] + "\n"
							+ "Vencimento do Certificado: " + retornoStr[26]
							+ "\n");

					textArea.setMaxWidth(Double.MAX_VALUE);
					textArea.setMaxHeight(Double.MAX_VALUE);
					GridPane.setVgrow(textArea, Priority.ALWAYS);
					GridPane.setHgrow(textArea, Priority.ALWAYS);

					GridPane expContent = new GridPane();
					expContent.setMaxWidth(Double.MAX_VALUE);
					expContent.add(textArea, 0, 0);

					//AlRetorno.getDialogPane().setExpandableContent(expContent);
					AlRetorno.getDialogPane().setContent(expContent);

					AlRetorno.showAndWait();
					
					log.debug(BtConsStatusOp);
					log.info("Retorno da Execução: ");
					log.info(retorno);
					log.info("Fim da execução da Função ConsultarStatusOperacional");
					log.info("");

				}
			});
			
			//EnviarDadosVenda-------------------------------------------------------------------
			BtEnviarVenda = new Button("EnviarDadosVenda");
            BtEnviarVenda.setOnAction(new EventHandler<ActionEvent>() {
			
				public void handle(ActionEvent event) {
					// TODO Auto-generated method stub					
					
					try {
															
						String xmlVenda = "<CFe> <infCFe versaoDadosEnt=\"00.06\"> <ide> <CNPJ>16716114000172</CNPJ> <signAC>SGR-SAT SISTEMA DE GESTAO E RETAGUARDA DO SAT</signAC> <numeroCaixa>001</numeroCaixa> </ide> <emit> <CNPJ>82373077000171</CNPJ> <IE>111111111111</IE> <indRatISSQN>S</indRatISSQN> </emit> <dest> <CPF></CPF> </dest> <det nItem=\"1\"> <prod> <cProd>1234567890</cProd> <xProd>AGUA MINERAL SEM GAS - COPO 200 ML</xProd> <NCM>22011000</NCM> <CFOP>5403</CFOP> <uCom>UN</uCom> <qCom>1.0000</qCom> <vUnCom>1.00</vUnCom> <indRegra>A</indRegra> <vDesc>0.00</vDesc> <vOutro>0.00</vOutro> </prod> <imposto> <vItem12741>0.00</vItem12741> <ICMS> <ICMS40> <Orig>0</Orig> <CST>60</CST> </ICMS40> </ICMS> <PIS> <PISNT> <CST>04</CST> </PISNT> </PIS> <COFINS> <COFINSNT> <CST>04</CST> </COFINSNT> </COFINS> </imposto> </det> <total> <vCFeLei12741>0.00</vCFeLei12741> </total> <pgto> <MP> <cMP>01</cMP> <vMP>10.00</vMP> </MP> </pgto> <infAdic> <infCpl>Obrigado, volte sempre</infCpl> </infAdic> </infCFe> </CFe>";
							int sessao = gerador.nextInt(999)*100;
							log.info("Inicia a execução da função EnviarDadosVenda");
							log.info("Parâmetros da função ");
							log.info("Sessão: "+sessao);
							log.info("Código de ativação: "+tfCodAtivacao.getText());
							log.info("XML de venda: "+xmlVenda);
							retorno = Bema.EnviarDadosVenda(sessao, tfCodAtivacao.getText(), xmlVenda);
							log.debug(BtEnviarVenda);
							log.info("Retorno da Execução: ");
							log.info(retorno);							
							TfSessao.setText(Integer.toString(sessao));
							
							String retornoStr[] = retorno.split(Pattern.quote("|"));
							
							Alert AlRetorno = new Alert(AlertType.INFORMATION);
							AlRetorno.setWidth(550);
							AlRetorno.setTitle("Retorno do S@T");
							AlRetorno
									.setHeaderText("Retorno da Função EnviarDadosVenda");
							AlRetorno.setContentText("Mensagem de Retorno: " + retornoStr[3]);
							TextArea textArea = new TextArea();
							textArea.setEditable(false);
							textArea.setWrapText(true);
							textArea.setText("Número de Sessão: "+retornoStr[0] + "\n"+
									"Código de Retorno: "+retornoStr[1] + "\n"+
									"Código de retorno de cancelamento: "+retornoStr[2] + "\n"+
									"Mensagem de Retorno: "+retornoStr[3] + "\n"+
									"Código SEFAZ: "+retornoStr[4] + "\n"+
									"Mensagem SEFAZ: "+retornoStr[5] + "\n"+
									"Data e hora da emissão: "+retornoStr[7] + "\n"+
									"Chave de acesso: "+retornoStr[8] + "\n"+
									"Valor Total CF-e: "+retornoStr[9] + "\n"+
									"Número do CPF ou CNPJ do adquirente: "+retornoStr[10] + "\n"+
									"Assinatura QRCode: "+retornoStr[11] + "\n");

							textArea.setMaxWidth(Double.MAX_VALUE);
							textArea.setMaxHeight(Double.MAX_VALUE);
							GridPane.setVgrow(textArea, Priority.ALWAYS);
							GridPane.setHgrow(textArea, Priority.ALWAYS);

							GridPane expContent = new GridPane();
							expContent.setMaxWidth(Double.MAX_VALUE);
							expContent.add(textArea, 0, 0);

							//AlRetorno.getDialogPane().setExpandableContent(expContent);
							AlRetorno.getDialogPane().setContent(expContent);

							AlRetorno.showAndWait();
		
							TfChaveAcesso.setText(retornoStr[8]);
							//Decodificando retorno Base64
							
							xmlRetornoVenda = retornoStr[6];
							log.info("Decodificando arquivo de retorno");
							byte[] decodeBytes = Base64.decode(xmlRetornoVenda);
							try {
								String decoded = new String(decodeBytes, "UTF-8");							
							
								log.info("XML de Retorno:");
								log.info(decoded);
								log.info("Fim da execução da Função EnviarDadosVenda");
								log.info("");
							
							//Criando arquivo xml de retorno
							 
							 
							//Cria o arquivo xml com a chave de acesso no C:\
							cfe = retornoStr[8];
				            arquivo = new File("C:\\APPBEMASAT\\"+cfe+".xml");  
				            log.info("Criando o arquivo: "+arquivo.toString());
				            FileOutputStream fos = new FileOutputStream(arquivo); 
				            fos.write(decoded.getBytes());				            				            				           			           				            
				            
							} catch (IOException e) {
								log.error("Erro de IOException na decodificação do retorno: ",e);									
								e.printStackTrace();
							}
														
					}
					catch (Exception e) {

						log.error("Erro de Exception: ",e);
					}
				}	
				
			});
            
            //Botão imprimir venda-----------------------------------------------------------
            
            BtImprimirVenda = new Button("Imprimir Cupom");
            BtImprimirVenda.setOnAction(new EventHandler<ActionEvent>() {

				@Override
				public void handle(ActionEvent event) {
					
					LeitorXML leitorXml = new LeitorXML();
					try {
						
						leitorXml.LeituraXml(arquivo.toString());
						log.info(arquivo.toString());
					} catch (SAXException e) {
						log.info(e);
						e.printStackTrace();
					} catch (IOException e) {
						log.info(e);
						e.printStackTrace();
					} catch (ParserConfigurationException e) {
						log.info(e);
						e.printStackTrace();
					}

					/*LeitorXML parser = new LeitorXML();
					log.info("Parse: "+ parser);
					
					try {						
						parser.LeituraXml(xmlRetornoVenda);
						if (parser!=null) {
							log.info("leitura do XML realizada com sucesso");
						}else {
							log.info("Erro de leitura do parse xml");
						}					
				} catch (ParserConfigurationException e) {
					log.info("O parser não foi configurado corretamente");
					log.fatal(e);
				}  catch (SAXException e) {
					log.fatal("Problema ao fazer o parser");
					log.fatal(e);
				} catch (IOException e) {
					log.fatal("O arquivo não pode ser lido");
					log.fatal(e);
				}*/
				
			}
			});

			// Posicionamento dos componentes no Pane=======================================
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
			root.getChildren().addAll(LbSessao, TfSessao, LbCodAtivacao,
					tfCodAtivacao, mostraLogo, BtConsultaSAT, BtConsStatusOp, TfChaveAcesso, BtEnviarVenda, BtImprimirVenda);
			root.getStyleClass().add("root");
			Scene scene = new Scene(root, 650, 650);
			scene.getStylesheets().add(
					getClass().getResource("application.css").toExternalForm());
			primaryStage.setScene(scene);
			primaryStage.show();

		} catch (Exception e) {
			e.printStackTrace();
		}
	
}
	
	public static void main(String[] args) throws SAXParseException {
		launch(args);	
		
		/*LeitorXML parser = new LeitorXML();
		
		try {
			//parser.LeituraXml("C:\\cfe\\CFe35160200735540000113590001246230000745496827.xml");
			parser.LeituraXml(xmlRetornoVenda);
			if (parser!=null) {
				log.info("leitura do XML realizada com sucesso");
			}else {
				log.info("Erro de leitura do parse xml");
			}			
			
		} catch (ParserConfigurationException e) {
			log.info("O parser não foi configurado corretamente");
			log.fatal(e);
		}  catch (SAXException e) {
			log.fatal("Problema ao fazer o parser");
			log.fatal(e);
		} catch (IOException e) {
			log.fatal("O arquivo não pode ser lido");
			log.fatal(e);
		}*/
		
	}
}
