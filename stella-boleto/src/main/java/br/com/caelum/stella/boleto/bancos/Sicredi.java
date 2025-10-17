package br.com.caelum.stella.boleto.bancos;

import java.net.URL;

import br.com.caelum.stella.boleto.Banco;
import br.com.caelum.stella.boleto.Beneficiario;
import br.com.caelum.stella.boleto.Boleto;
import static br.com.caelum.stella.boleto.utils.StellaStringUtils.leftPadWithZeros;
import static java.lang.String.format;

import java.math.BigDecimal;

public class Sicredi extends AbstractBanco implements Banco {

	private static final long serialVersionUID = 1L;

	private static final String NUMERO_SICREDI = "748";
	private static final String DIGITO_SICREDI = "X";

	@Override
	public String geraCodigoDeBarrasPara(Boleto boleto) {
		Beneficiario beneficiario = boleto.getBeneficiario();

		StringBuilder campoLivre = new StringBuilder();
		campoLivre.append("1");
		campoLivre.append(beneficiario.getCarteira());
		campoLivre.append(leftPadWithZeros(beneficiario.getNossoNumero(),8));
		campoLivre.append(beneficiario.getDigitoNossoNumero());
		campoLivre.append(beneficiario.getAgenciaFormatada());
		campoLivre.append(getCodigoBeneficiarioFormatado(beneficiario));
		campoLivre.append(boleto.getValorBoleto().compareTo(BigDecimal.ZERO) == 0 ? "0" : "1");
		campoLivre.append("0");
		campoLivre.append(geradorDeDigito.geraDigitoMod11AceitandoRestoZero(campoLivre.toString()));

		return new CodigoDeBarrasBuilder(boleto).comCampoLivre(campoLivre);
	}

	@Override
	public String getNumeroFormatadoComDigito() {
		StringBuilder builder = new StringBuilder();
		builder.append(getNumeroFormatado()).append("-");
		return builder.append(DIGITO_SICREDI).toString();
	}

	@Override
	public String getCarteiraFormatado(Beneficiario beneficiario) {
		return leftPadWithZeros(beneficiario.getCarteira(), 1);
	}

	@Override
	public String getCodigoBeneficiarioFormatado(Beneficiario beneficiario) {
		return leftPadWithZeros(beneficiario.getCodigoBeneficiario(), 7);
	}

	@Override
	public URL getImage() {
		String arquivo = "/br/com/caelum/stella/boleto/img/%s.png";
		String imagem = format(arquivo, getNumeroFormatado());
		return getClass().getResource(imagem);
	}

	@Override
	public String getNossoNumeroFormatado(Beneficiario beneficiario) {
		String nossoNumero = leftPadWithZeros(beneficiario.getNossoNumero(), 8);
		StringBuilder nossoNumeroFormatado = new StringBuilder();
		nossoNumeroFormatado.append(nossoNumero.substring(0, 2));
		nossoNumeroFormatado.append("/");
		nossoNumeroFormatado.append(nossoNumero.substring(2));
		return nossoNumeroFormatado.toString();
	}

	@Override
	public String getNumeroFormatado() {
		return NUMERO_SICREDI;
	}

	@Override
	public String getNossoNumeroECodigoDocumento(Boleto boleto) {
		Beneficiario beneficiario = boleto.getBeneficiario();

		return getNossoNumeroFormatado(beneficiario);
	}

	@Override
	public String getAgenciaECodigoBeneficiario(Beneficiario beneficiario) {
		String codigoBeneficiario = getCodigoBeneficiarioFormatado(beneficiario);
		StringBuilder builder = new StringBuilder();
		builder.append(beneficiario.getAgenciaFormatada());
		builder.append(".");
		builder.append(codigoBeneficiario.substring(0, 2));
		builder.append(".");
		builder.append(codigoBeneficiario.substring(2));
		return builder.toString();
	}
}
