package application;

import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class LeitorXML {
	
	public void LeituraXml(String arquivoXml) throws SAXException, IOException, ParserConfigurationException {
		
		//fazer o parse do arquivo e criar o documento XML
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		DocumentBuilder db = dbf.newDocumentBuilder();
		Document doc = db.parse(arquivoXml);
		
		//Passo 1: obter o elemento raiz
		Element raiz = doc.getDocumentElement();
		System.out.println("O elemento raiz é: "+raiz.getNodeName());				
		
		//Passo 2: obter nome do emitente
		NodeList listaNome = raiz.getElementsByTagName("xNome");
		Node nome = listaNome.item(0).getFirstChild();
		System.out.println("Nome: " + nome.getNodeValue());
		
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
		
		System.out.println("Endereço: " + rua.getNodeValue()+", "+num.getNodeValue()+" "
		+compl.getNodeValue()+" "+bairro.getNodeValue()+" "+cidade.getNodeValue()+" CEP: "+
		cep.getNodeValue());
		
		//Passo 4: obter ie e cnpj o emitente
		//obter cnpj do emitente
		NodeList ListaCnpj = raiz.getElementsByTagName("CNPJ");
		Node cnpj = ListaCnpj.item(0).getFirstChild();
		
		NodeList listaIe = raiz.getElementsByTagName("IE");
		Node ie = listaIe.item(0).getFirstChild();
		
		System.out.println("CNPJ: " + cnpj.getNodeValue()+" IE: "+ie.getNodeValue());
		
		//Passo 5: obter cpf do destinatário
		NodeList listaCpf = raiz.getElementsByTagName("CPF");
		if (listaCpf==null) {
			Node cpf=null;
			return;
		}
		Node cpf = listaCpf.item(0).getFirstChild();
		
		System.out.println("CPF/CNPJ do consumidor: "+cpf.getNodeValue());
		
		//Passo 6: obter dados da venda
		NodeList listaCod = raiz.getElementsByTagName("cProd");
		Node cod = listaCod.item(0).getFirstChild();
		
		NodeList listaDescri = raiz.getElementsByTagName("xProd");
		Node descri = listaDescri.item(0).getFirstChild();
		
		NodeList listaNcm = raiz.getElementsByTagName("NCM");
		Node ncm = listaNcm.item(0).getFirstChild();
		
		NodeList listaCfop = raiz.getElementsByTagName("CFOP");
		Node cfop = listaCfop.item(0).getFirstChild();
		
		NodeList listaUn = raiz.getElementsByTagName("uCom");
		Node un = listaUn.item(0).getFirstChild();
		
		NodeList listaQtde = raiz.getElementsByTagName("qCom");
		Node qtde = listaQtde.item(0).getFirstChild();
		
		NodeList listaValorUn = raiz.getElementsByTagName("vProd");
		Node valorUn = listaValorUn.item(0).getFirstChild();

		NodeList listaAliq = raiz.getElementsByTagName("vItem12741");
		Node aliq = listaAliq.item(0).getFirstChild();
		
		NodeList listaValorItem = raiz.getElementsByTagName("vItem");
		Node valorItem = listaValorItem.item(0).getFirstChild();
		
		NodeList listaIndRegra = raiz.getElementsByTagName("indRegra");
		Node indRegra = listaIndRegra.item(0).getFirstChild();
		
		NodeList listaDesc = raiz.getElementsByTagName("vDesc");
		Node indDesc = listaDesc.item(0).getFirstChild();
		
		NodeList listaOutro = raiz.getElementsByTagName("vOutro");
		Node outro = listaOutro.item(0).getFirstChild();
		
		//Passo 7: obter totais da venda
		//obter total bruta da venda
		NodeList listaTotalizador = raiz.getElementsByTagName("ICMSTot");
		Element total = (Element) listaTotalizador.item(0);
		NodeList listaTotal = total.getElementsByTagName("vProd");
		Node totalVenda = listaTotal.item(0).getFirstChild();
		
		System.out.println("Tag pai: "+total.getNodeName());
		System.out.println(" Tag: "+totalVenda.getNodeName());
		System.out.println(" Total: "+totalVenda.getNodeValue());
		
		NodeList listaDescTotal = raiz.getElementsByTagName("vDescSubtot");
		if (listaDescTotal==null){
			Node descTotal = null;
		}
		Node descTotal = listaDescTotal.item(0).getFirstChild();
		
		NodeList listaAcrescTotal = raiz.getElementsByTagName("vAcresSubtot");
		if (listaAcrescTotal==null) {
			Node acrescTotal = null;
			return;
		}
		Node acrescTotal = listaAcrescTotal.item(0).getFirstChild();
		
	}
		public static void main(String[] args){
			LeitorXML parser = new LeitorXML();
			
			try {
				parser.LeituraXml("C:\\cfe\\CFe35160200735540000113590001246230000745496827.xml");
				//parser.LeituraXml("CFe35160282373077000171599000039140003427198508.xml");
				
			} catch (ParserConfigurationException e) {
				System.out.println("O parser não foi configurado corretamente");
				e.printStackTrace();
			}  catch (SAXException e) {
				System.out.println("Problema ao fazer o parser");
				e.printStackTrace();
			} catch (IOException e) {
				System.out.println("O arquivo não pode ser lido");
				e.printStackTrace();
			}
		}
		
	
	
}