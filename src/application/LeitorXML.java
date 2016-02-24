package application;

import java.io.IOException;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.log4j.Logger;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class LeitorXML {
	
	static Logger log = Logger.getLogger(LeitorXML.class);
	
public void LeituraXml(String  arquivoXml) throws SAXException, IOException, ParserConfigurationException {
		
		//fazer o parse do arquivo e criar o documento XML
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();		
		DocumentBuilder db = dbf.newDocumentBuilder();
		Document doc = db.parse(arquivoXml);
		
		//Passo 1: obter o elemento raiz
		Element raiz = doc.getDocumentElement();
		log.info("O elemento raiz é: "+raiz.getNodeName());				
		
		//Passo 2: obter nome do emitente
		NodeList listaNome = raiz.getElementsByTagName("xNome");
		Node nome = listaNome.item(0).getFirstChild();
		log.info("Nome: " + nome.getNodeValue());
		
		//Passo 3: obter endereço do emitente	
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
		
		log.info("Endereço: " + rua.getNodeValue()+", "+num.getNodeValue()+" "
		+compl.getNodeValue()+" "+bairro.getNodeValue()+" "+cidade.getNodeValue()+" CEP: "+
		cep.getNodeValue());
		
		//Passo 4: obter ie e cnpj o emitente
		//obter cnpj do emitente
		NodeList ListaCnpj = raiz.getElementsByTagName("CNPJ");
		Node cnpj = ListaCnpj.item(0).getFirstChild();
		
		NodeList listaIe = raiz.getElementsByTagName("IE");
		Node ie = listaIe.item(0).getFirstChild();
		
		log.info("CNPJ: " + cnpj.getNodeValue()+" IE: "+ie.getNodeValue());
		
		//Passo 5: obter cpf do destinatário
		NodeList listaCpf = raiz.getElementsByTagName("CPF");
		if (listaCpf==null) {
			Node cpf=null;
			return;
		}
		Node cpf = listaCpf.item(0).getFirstChild();
		
		log.info("CPF/CNPJ do consumidor: "+cpf.getNodeValue());
		
		//Passo 6: obter dados da venda
		
		//Passo 7: localizar os elemento filhos do det
		NodeList listaDet = raiz.getElementsByTagName("det");
		
		//Passo 8: obter cada elemento do elemento det
		for (int i = 0; i < listaDet.getLength(); i++) {
			
			//como cada elemento do NodeList é um nó, precisamos fazer o cast
			Element venda = (Element) listaDet.item(i);
			
			//Passo 9: obter o atributo id do det
			Attr id = venda.getAttributeNode("nItem");
			log.info("\nID do produto: "+id.getValue());
			
			//Passo 10: obtem os elementos da venda
			NodeList listaCod = raiz.getElementsByTagName("cProd");
			Node cod = listaCod.item(0).getFirstChild();
			log.info("codigo: "+cod.getNodeValue());
			
			NodeList listaDescri = raiz.getElementsByTagName("xProd");
			Node descri = listaDescri.item(0).getFirstChild();
			log.info("Descrição: "+descri.getNodeValue());
			
			NodeList listaNcm = raiz.getElementsByTagName("NCM");
			Node ncm = listaNcm.item(0).getFirstChild();
			log.info("NCM: "+ncm.getNodeValue());
			
			NodeList listaCfop = raiz.getElementsByTagName("CFOP");
			Node cfop = listaCfop.item(0).getFirstChild();
			log.info("CFOP: "+cfop.getNodeValue());
			
			NodeList listaUn = raiz.getElementsByTagName("uCom");
			Node un = listaUn.item(0).getFirstChild();
			log.info("UN: "+un.getNodeValue());
			
			NodeList listaQtde = raiz.getElementsByTagName("qCom");
			Node qtde = listaQtde.item(0).getFirstChild();
			log.info("Quantidade: "+qtde.getNodeValue());
			
			NodeList listaValorUn = raiz.getElementsByTagName("vProd");
			Node valorUn = listaValorUn.item(0).getFirstChild();
			log.info("Valor Unit.: "+valorUn.getNodeValue());

			NodeList listaAliq = raiz.getElementsByTagName("vItem12741");
			Node aliq = listaAliq.item(0).getFirstChild();
			log.info("Aliquota: "+aliq.getNodeValue());
			
			NodeList listaValorItem = raiz.getElementsByTagName("vItem");
			Node valorItem = listaValorItem.item(0).getFirstChild();
			log.info("Valor do Item: "+valorItem.getNodeValue());
			
			NodeList listaIndRegra = raiz.getElementsByTagName("indRegra");
			Node indRegra = listaIndRegra.item(0).getFirstChild();
			log.info("Regra: "+indRegra.getNodeValue());
			
			NodeList listaDesc = raiz.getElementsByTagName("vDesc");
			Node indDesc = listaDesc.item(0).getFirstChild();
			log.info("Desconto: "+indDesc.getNodeValue());
			
			NodeList listaOutro = raiz.getElementsByTagName("vOutro");
			Node outro = listaOutro.item(0).getFirstChild();
			log.info("Outros: "+outro.getNodeValue());
		}				
		
		//Passo 11: obter totais da venda
		//obter total bruta da venda
		NodeList listaTotalizador = raiz.getElementsByTagName("total");
		Element total = (Element) listaTotalizador.item(0);
		
		NodeList listaTotal = total.getElementsByTagName("vProd");
		Node totalVenda = listaTotal.item(0).getFirstChild();				
		log.info("Total: "+totalVenda.getNodeValue());
		
		
		//obter o valor total de descontos, se houver	
		NodeList listaDescTotal = total.getElementsByTagName("vDesc");
		if (listaDescTotal==null) {
			log.info("Desconto nulo");
		}		
		Node descTotal = listaDescTotal.item(0).getFirstChild();
		log.info("Desconto total: "+descTotal.getNodeValue());
		
		
		//obter o valor total de acréscimos, se houver		
		NodeList listaAcrescTotal = total.getElementsByTagName("vOutro");
		if (listaAcrescTotal==null) {
			log.info("Acréscimo nulo");
		}
		Node acrescTotal = listaAcrescTotal.item(0).getFirstChild();
		log.info("Acréscimo total: "+acrescTotal.getNodeValue());
		
		//obter o valor total de tributos
		NodeList listaTrib = total.getElementsByTagName("vCFeLei12741");
		Node trib = listaTrib.item(0).getFirstChild();
		log.info("Total de tributos: "+trib.getNodeValue());
		
		//Passo 12: obter informações do final do cupom		
		// obter elemento infAdic
		NodeList listaInfo = raiz.getElementsByTagName("infAdic");
		Element info = (Element) listaInfo.item(0);
		
		//obter informações complementares
		NodeList listaInfCompl = info.getElementsByTagName("infCpl");
		Node infCompl = listaInfCompl.item(0).getFirstChild();
		log.info("Informações Compl.: "+infCompl.getNodeValue());
		
		//obter observações fisco
		NodeList listaObsFisco = info.getElementsByTagName("obsFisco");
		Node obsFisco = listaObsFisco.item(0).getFirstChild();
		log.info("Obs Fisco: "+obsFisco.getNodeValue());
		
		
		//Passo 13: Obter chave de acesso
		NodeList listaInfCFeChave = raiz.getElementsByTagName("infCFe");
		//como cada elemento do NodeList é um nó, precisamos fazer o cast
		Element chave = (Element) listaInfCFeChave.item(0);		
		//obter o atributo id do infCFe
		Attr id = chave.getAttributeNode("Id");
		log.info("Chave de acesso: "+id.getValue());
		
		//Passo 14: obter o QR Code
		NodeList listaAssQRCode = raiz.getElementsByTagName("assinaturaQRCODE");
		Node assQRCode = listaAssQRCode.item(0).getFirstChild();
		log.info("Ass. QRCode: "+assQRCode.getNodeValue());
}
		
	
	
}