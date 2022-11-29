package huo_ohla_09;

import static org.junit.Assert.assertEquals;
import org.junit.jupiter.api.Test;
import mockesimerkki.Asiakas;
import mockesimerkki.IHinnoittelija;
import mockesimerkki.Tilaus;
import mockesimerkki.TilaustenKäsittely;
import mockesimerkki.Tuote;

class TilaustenKasittelyFakeTest {

	@Test
	public void testaaKäsittelijäWithFakeHinnoittelija() {
		// Arrange
		float alkuSaldo = 100.0f;
		float listaHinta = 30.0f;
		float alennus = 20.0f;
		float loppuSaldo = alkuSaldo - (listaHinta * (1 - alennus / 100));
		Asiakas asiakas = new Asiakas(alkuSaldo);
		Tuote tuote = new Tuote("TDD in Action", listaHinta);
		IHinnoittelija hinnoittelija = new FakeHinnoittelija(alennus);
		
		// Act
		TilaustenKäsittely käsittelijä = new TilaustenKäsittely();
		käsittelijä.setHinnoittelija(hinnoittelija);
		käsittelijä.käsittele(new Tilaus(asiakas, tuote));
		
		// Assert
		assertEquals(loppuSaldo, asiakas.getSaldo(), 0.001);
	}

}
