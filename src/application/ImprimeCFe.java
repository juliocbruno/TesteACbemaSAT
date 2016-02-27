package application;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import application.Main;

public class ImprimeCFe {
	
	static Logger log = Logger.getLogger(ImprimeCFe.class);
	
	public List<ConteudoXML> leituraXML (String arquivoXML) throws ParserConfigurationException, SAXException, IOException{
		
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		DocumentBuilder db = dbf.newDocumentBuilder();
		Document doc = db.parse(arquivoXML);
		
		Element raiz = doc.getDocumentElement();
		NodeList listaConteudoXml = raiz.getElementsByTagName("CFe");
		
		List<ConteudoXML> lista = new ArrayList<ConteudoXML>(listaConteudoXml.getLength());
		
		for (int i = 0; i < listaConteudoXml.getLength(); i++) {
			
			Element elementoCFe = (Element) listaConteudoXml.item(i);
			
			ConteudoXML conteudoXML = criaConteudoEmit(elementoCFe);
			lista.add(conteudoXML);
		}
		
		return lista;
	}
	
	/**
	 * Obtém o valor do Text Node de um determinado elemento (se este 
	 * possuir um valor)
	 * @param elemento objeto que deseja-se obter o valor
	 * @param nomeElemento nome da tag cujo valor deseja-se obter
	 * @return valor da tag/elemento se esta existir ou null caso não exista
	 */
	public String obterValorElemento(Element elemento, String nomeElemento){
		//obtém a lista de elementos
		NodeList listaElemento = elemento.getElementsByTagName(nomeElemento);
		if (listaElemento == null){
			return null;
		}
		//obtém o elemento
		Element noElemento = (Element) listaElemento.item(0);
		if (noElemento == null){
			return null;
		}
		//obtém o nó com a informação
		Node no = noElemento.getFirstChild();
		return no.getNodeValue();
	}

	private ConteudoXML criaConteudoEmit(Element elementoCFe) {
		
		ConteudoXML contEmit = new ConteudoXML();
		contEmit.setNomeEmitente(obterValorElemento(elementoCFe, "xNome")); //nome do emitente
		contEmit.setLogradouroEmit(obterValorElemento(elementoCFe, "xLgr")); //rua do emitente
		contEmit.setNumeroEmit(obterValorElemento(elementoCFe, "nro")); //número do emitente
		contEmit.setComplEmit(obterValorElemento(elementoCFe, "xCpl")); //complemento do emitente
		contEmit.setBairroEmit(obterValorElemento(elementoCFe, "xBairro")); //bairro do emitente
		contEmit.setMunEmit(obterValorElemento(elementoCFe, "xMun")); //municipio do emitente
		contEmit.setCEPEmit(obterValorElemento(elementoCFe, "CEP")); //CEP do emitente
		contEmit.setCnpjEmitente(obterValorElemento(elementoCFe, "CNPJ")); //CNPJ do emitente
		contEmit.setIeEmitente(obterValorElemento(elementoCFe, "IE")); //inscrição estadual do emitente
		contEmit.setCodigoProd(obterValorElemento(elementoCFe, "cProd")); //código do produto
		contEmit.setDescProd(obterValorElemento(elementoCFe, "xProd")); //descrição do produto
		contEmit.setUndProd(obterValorElemento(elementoCFe, "uCom")); //unidade comercial
		contEmit.setQtdProd(obterValorElemento(elementoCFe, "qCom")); //quantidade
		contEmit.setValorUntProd(obterValorElemento(elementoCFe, "vProd")); //valor bruto dos produtos
		contEmit.setValorTribProd(obterValorElemento(elementoCFe, "vItem12741")); //total de tributos do produto
		contEmit.setValorItem(obterValorElemento(elementoCFe, "vItem")); //valor liquido do item		
		contEmit.setTotalBrutoItem(obterValorElemento(elementoCFe, "vProd")); //somatório dos produtos -> <total><vProd>
		contEmit.setCodMeioPag(obterValorElemento(elementoCFe, "cMP")); // código meio de pagamento
		contEmit.setValorMeioPag(obterValorElemento(elementoCFe, "vMP")); // valor meio de pagamento
		contEmit.setTroco(obterValorElemento(elementoCFe, "vTroco")); //troco
		
		
		return contEmit;
	}

}
