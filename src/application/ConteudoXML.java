package application;

/**
 * @author JúlioCésar
 * @since 20/02/2016
 * Objeto para fazer a impressão do extrato
 * 
 */
public class ConteudoXML {
	
	private String nomeEmitente;
	private String logradouroEmit;
	private String numeroEmit;
	private String complEmit;
	private String bairroEmit;
	private String MunEmit;
	private String CEPEmit;
	private String cnpjEmitente;
	private String ieEmitente;
	private String cnpjConsumidor;
	private String codigoProd;
	private String descProd;
	private String qtdProd;
	private String undProd;
	private String valorUntProd;
	private String valorItem;
	private String valorTribProd;
	private String valorTotalProd;
	private String TotalBrutoItem;
	private String descAcrescItem;
	private String totalCupom;
	private String troco;
	private String xTexto;
	private String textoContrib;
	private String valorAproxTrib;
	private String numSerieSat;
	private String dataHora;
	private String chaveAcesso;
	private String QRCode;
	private String codMeioPag;
	private String valorMeioPag;	
	private String infoCompl;
	
	private String nomeArquivo;
	
	//Getters and Setters============================================
	
	public String getNomeEmitente() {
		return nomeEmitente;
	}
	public void setNomeEmitente(String nomeEmitente) {
		this.nomeEmitente = nomeEmitente;
	}
	public String getLogradouroEmit() {
		return logradouroEmit;
	}
	public void setLogradouroEmit(String logradouroEmit) {
		this.logradouroEmit = logradouroEmit;
	}
	public String getNumeroEmit() {
		return numeroEmit;
	}
	public void setNumeroEmit(String numeroEmit) {
		this.numeroEmit = numeroEmit;
	}
	public String getComplEmit() {
		return complEmit;
	}
	public void setComplEmit(String complEmit) {
		this.complEmit = complEmit;
	}
	public String getBairroEmit() {
		return bairroEmit;
	}
	public void setBairroEmit(String bairroEmit) {
		this.bairroEmit = bairroEmit;
	}
	public String getMunEmit() {
		return MunEmit;
	}
	public void setMunEmit(String munEmit) {
		MunEmit = munEmit;
	}
	public String getCEPEmit() {
		return CEPEmit;
	}
	public void setCEPEmit(String cEPEmit) {
		CEPEmit = cEPEmit;
	}
	public String getCnpjEmitente() {
		return cnpjEmitente;
	}
	public void setCnpjEmitente(String cnpjEmitente) {
		this.cnpjEmitente = cnpjEmitente;
	}
	public String getIeEmitente() {
		return ieEmitente;
	}
	public void setIeEmitente(String ieEmitente) {
		this.ieEmitente = ieEmitente;
	}
	public String getCnpjConsumidor() {
		return cnpjConsumidor;
	}
	public void setCnpjConsumidor(String cnpjConsumidor) {
		this.cnpjConsumidor = cnpjConsumidor;
	}
	public String getCodigoProd() {
		return codigoProd;
	}
	public void setCodigoProd(String codigoProd) {
		this.codigoProd = codigoProd;
	}
	public String getDescProd() {
		return descProd;
	}
	public void setDescProd(String descProd) {
		this.descProd = descProd;
	}
	public String getQtdProd() {
		return qtdProd;
	}
	public void setQtdProd(String qtdProd) {
		this.qtdProd = qtdProd;
	}
	public String getUndProd() {
		return undProd;
	}
	public void setUndProd(String undProd) {
		this.undProd = undProd;
	}
	public String getValorUntProd() {
		return valorUntProd;
	}
	public void setValorUntProd(String valorUntProd) {
		this.valorUntProd = valorUntProd;
	}
	public String getValorTribProd() {
		return valorTribProd;
	}
	public void setValorTribProd(String valorTribProd) {
		this.valorTribProd = valorTribProd;
	}
	public String getValorTotalProd() {
		return valorTotalProd;
	}
	public void setValorTotalProd(String valorTotalProd) {
		this.valorTotalProd = valorTotalProd;
	}
	public String getTotalBrutoItem() {
		return TotalBrutoItem;
	}
	public void setTotalBrutoItem(String totalBrutoItem) {
		TotalBrutoItem = totalBrutoItem;
	}
	public String getDescAcrescItem() {
		return descAcrescItem;
	}
	public void setDescAcrescItem(String descAcrescItem) {
		this.descAcrescItem = descAcrescItem;
	}
	public String getTotalCupom() {
		return totalCupom;
	}
	public void setTotalCupom(String totalCupom) {
		this.totalCupom = totalCupom;
	}
	public String getTroco() {
		return troco;
	}
	public void setTroco(String troco) {
		this.troco = troco;
	}
	public String getxTexto() {
		return xTexto;
	}
	public void setxTexto(String xTexto) {
		this.xTexto = xTexto;
	}
	public String getTextoContrib() {
		return textoContrib;
	}
	public void setTextoContrib(String textoContrib) {
		this.textoContrib = textoContrib;
	}
	public String getValorAproxTrib() {
		return valorAproxTrib;
	}
	public void setValorAproxTrib(String valorAproxTrib) {
		this.valorAproxTrib = valorAproxTrib;
	}
	public String getNumSerieSat() {
		return numSerieSat;
	}
	public void setNumSerieSat(String numSerieSat) {
		this.numSerieSat = numSerieSat;
	}
	public String getDataHora() {
		return dataHora;
	}
	public void setDataHora(String dataHora) {
		this.dataHora = dataHora;
	}
	public String getChaveAcesso() {
		return chaveAcesso;
	}
	public void setChaveAcesso(String chaveAcesso) {
		this.chaveAcesso = chaveAcesso;
	}
	public String getQRCode() {
		return QRCode;
	}
	public void setQRCode(String qRCode) {
		QRCode = qRCode;
	}
	public String getNomeArquivo() {
		return nomeArquivo;
	}
	public void setNomeArquivo(String nomeArquivo) {
		this.nomeArquivo = nomeArquivo;
	}
	public String getValorItem() {
		return valorItem;
	}
	public void setValorItem(String valorItem) {
		this.valorItem = valorItem;
	}
	public String getCodMeioPag() {
		return codMeioPag;
	}
	public void setCodMeioPag(String codMeioPag) {
		this.codMeioPag = codMeioPag;
	}
	public String getValorMeioPag() {
		return valorMeioPag;
	}
	public void setValorMeioPag(String valorMeioPag) {
		this.valorMeioPag = valorMeioPag;
	}
	public String getInfoCompl() {
		return infoCompl;
	}
	public void setInfoCompl(String infoCompl) {
		this.infoCompl = infoCompl;
	}
	
}
