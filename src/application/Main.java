package application;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Random;
import java.util.regex.Pattern;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.stage.Stage;

import com.sun.org.apache.xerces.internal.impl.dv.util.Base64;

public class Main extends Application {

	BemaSAT Bema = BemaSAT.instance;
	MP2032 mp2032 = MP2032.instance;

	String retorno = "";

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
	

	public void start(Stage primaryStage) {
		
		gerador = new Random();
		
		try {

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
					retorno = Bema.ConsultarSAT(sessao);
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
					retorno = Bema.ConsultarStatusOperacional(sessao,
							tfCodAtivacao.getText());
					String retornoStr[] = retorno.split(Pattern.quote("|"));
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

				}
			});
			
			//EnviarDadosVenda
			BtEnviarVenda = new Button("EnviarDadosVenda");
            BtEnviarVenda.setOnAction(new EventHandler<ActionEvent>() {
			
				public void handle(ActionEvent event) {
					// TODO Auto-generated method stub					
					
					
							/*try {*/
										
							String xmlVenda = "<CFe>\n"
					+ "<infCFe versaoDadosEnt=\"00.06\">\n"
					+ "<ide>\n"
					+ "<CNPJ>16716114000172</CNPJ>\n"
					+ "<signAC>SGR-SAT SISTEMA DE GESTAO E RETAGUARDA DO SAT</signAC>\n"
					+ "<numeroCaixa>001</numeroCaixa>\n"
					+ "</ide>\n"
					+ "<emit>\n"
					+ "<CNPJ>82373077000171</CNPJ>\n"
					+ "<IE>111111111111</IE>\n"
					+ "<indRatISSQN>S</indRatISSQN>\n"
					+ "</emit>\n"
					+ "<dest>\n"
					+ "<CPF></CPF>\n"
					+ "</dest>\n"
					+ "<det nItem=\"1\">\n"
					+ "<prod>\n"
					+ "<cProd>1234567890</cProd>\n"
					+ "<xProd>AGUA MINERAL SEM GAS - COPO 200 ML</xProd>\n"
					+ "<NCM>22011000</NCM>\n"
					+ "<CFOP>5403</CFOP>\n"
					+ "<uCom>UN</uCom>\n"
					+ "<qCom>1.0000</qCom>\n"
					+ "<vUnCom>1.00</vUnCom>\n"
					+ "<indRegra>A</indRegra>\n"
					+ "<vDesc>0.00</vDesc>\n"
					+ "<vOutro>0.00</vOutro>\n"
					+ "</prod>\n"
					+ "<imposto>\n"
					+ "<vItem12741>0.00</vItem12741>\n"
					+ "<ICMS>\n"
					+ "<ICMS40>\n"
					+ "<Orig>0</Orig>\n"
					+ "<CST>60</CST>\n"
					+ "</ICMS40>\n"
					+ "</ICMS>\n"
					+ "<PIS>\n"
					+ "<PISNT>\n"
					+ "<CST>04</CST>\n"
					+ "</PISNT>\n"
					+ "</PIS>\n"
					+ "<COFINS>\n"
					+ "<COFINSNT>\n"
					+ "<CST>04</CST>\n"
					+ "</COFINSNT>\n"
					+ "</COFINS>\n"
					+ "</imposto>\n"
					+ "</det>\n"
					+ "<total>\n"
					+ "<vCFeLei12741>0.00</vCFeLei12741>\n"
					+ "</total>\n"
					+ "<pgto>\n"
					+ "<MP>\n"
					+ "<cMP>01</cMP>\n"
					+ "<vMP>10.00</vMP>\n"
					+ "</MP>\n"
					+ "</pgto>\n"
					+ "<infAdic>\n"
					+ "<infCpl>Obrigado, volte sempre</infCpl>\n"
					+ "</infAdic>\n"
					+ "</infCFe>\n"
					+ "</CFe>\n";
							int sessao = gerador.nextInt(999)*100;
							retorno = Bema.EnviarDadosVenda(sessao, tfCodAtivacao.getText(), xmlVenda);
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
							String xmlRetornoVenda = retornoStr[6];
							byte[] decodeBytes = Base64.decode(xmlRetornoVenda);
							try {
								String decoded = new String(decodeBytes, "UTF-8");							
							
							//Criando arquivo xml de retorno
							 File arquivo;  
							  
							//Cria o arquivo xml com a chave de acesso no C:\
				            arquivo = new File("C:\\"+retornoStr[8]+".xml");  
				            FileOutputStream fos = new FileOutputStream(arquivo); 
				            fos.write(decoded.getBytes());
				            
				            //Montando a impressão
				            File arquivoXml = new File("C:\\"+retornoStr[8]+".xml");
				            
				          //fazer o parse do arquivo e criar o documento XML
				        	DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
				        	DocumentBuilder db = dbf.newDocumentBuilder();
				        	try {
								Document doc = db.parse(arquivoXml);
								//Passo 1: obter o elemento raiz
								Element raiz = doc.getDocumentElement();
								NodeList listaContatos = raiz.getElementsByTagName("CNPJ");
								Alert alerta = new Alert(AlertType.INFORMATION);
								alerta.setContentText(listaContatos.toString());
								
							} catch (SAXException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
				          

							} catch (IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							} catch (ParserConfigurationException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}
							
				}
			});
		
            //Botão Imprimir---------------------------------------------------------------
            BtImprimirVenda = new Button("Imprimir CF-e");
            BtImprimirVenda.setOnAction(new EventHandler<ActionEvent>() {

				public void handle(ActionEvent arg0) {
					// TODO Imprimir CF-e
					int iRetorno;
									
					
					String BufTrans = "";
					iRetorno = mp2032.BematechTX(BufTrans );
										
				}
			});
            

			// Posicionamento dos componentes no Pane=======================================
			LbSessao.setTranslateX(20); // define orientação horizontal
			LbSessao.setTranslateY(10); // define orientação vertical

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
					tfCodAtivacao, mostraLogo, BtConsultaSAT, BtConsStatusOp, TfChaveAcesso, BtEnviarVenda);
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

	public static void main(String[] args) {
		launch(args);
	}
}
