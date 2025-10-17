package br.com.caelum.stella.boleto.bancos;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import br.com.caelum.stella.boleto.Beneficiario;
import br.com.caelum.stella.boleto.Boleto;
import br.com.caelum.stella.boleto.Datas;
import br.com.caelum.stella.boleto.Pagador;
import static org.hamcrest.CoreMatchers.is;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;

public class SicrediTest {

	private Sicredi banco = new Sicredi();
	private Beneficiario beneficiario;
	private Boleto boleto;
	
	@Rule
	public ExpectedException excecao = ExpectedException.none();

	@Before
	public void setUp() {
		Datas datas = Datas.novasDatas()
			.comDocumento(20, 10, 2025)
			.comProcessamento(20, 10, 2025)
			.comVencimento(27, 11, 2025);
		
		this.beneficiario = Beneficiario.novoBeneficiario()
			.comNomeBeneficiario("PETROBRAS")
			.comAgencia("0101")
			.comCarteira("1")
			.comCodigoBeneficiario("1430357")
			.comNossoNumero("25312186")
			.comDigitoNossoNumero("0");
		
		Pagador pagador = Pagador.novoPagador().comNome("Marcos Ribes"); 

		boleto = Boleto.novoBoleto()
			.comEspecieDocumento("DMI")
			.comBanco(banco)
			.comDatas(datas)
			.comBeneficiario(beneficiario)
			.comPagador(pagador)
			.comValorBoleto(2126.60)
			.comAceite(Boolean.FALSE)
			.comInstrucoes("instrucao 1", "instrucao 2", "instrucao 3", "instrucao 4")
			.comLocaisDePagamento("local 1", "local 2")
			.comNumeroDoDocumento("1234/4");
	}

	@Test 
	public void testCodigoDeBarraDoBancoSicredi() {
		String string = "74894127800002126601125312186001011430357100";
		assertEquals(string, banco.geraCodigoDeBarrasPara(boleto));
	}

	@Test
	public void testGetImage() {
		assertNotNull(banco.getImage());
	}

	@Test
	public void testCodigoDoBeneficiarioFormatado() {
		assertThat(banco.getCodigoBeneficiarioFormatado(beneficiario),
				is("1430357"));
	}
	
	@Test
	public void testNossoNumeroECodigoDocumento() {
		assertThat(banco.getNossoNumeroECodigoDocumento(boleto),
				is("25/312186"));
	}

	@Test
	public void testAgenciaECodigoBeneficiario() {
		assertThat(banco.getAgenciaECodigoBeneficiario(beneficiario), is("0101.14.30357"));
	}
}
