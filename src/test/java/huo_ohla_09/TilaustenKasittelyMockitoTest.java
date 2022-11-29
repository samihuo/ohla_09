package huo_ohla_09;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import mockesimerkki.Asiakas;
import mockesimerkki.IHinnoittelija;
import mockesimerkki.Tilaus;
import mockesimerkki.TilaustenKäsittely;
import mockesimerkki.Tuote;

public class TilaustenKasittelyMockitoTest {
	
		@Mock
		IHinnoittelija hinnoittelijaMock;
		
		@BeforeEach
		public void setup() {
			MockitoAnnotations.openMocks(this);
		}
		
		// Esimerkkitesti
		@Test
		public void testaaKäsittelijäWithMockitoHinnoittelija() {
			// Arrange
			float alkuSaldo = 100.0f;
			float listaHinta = 30.0f;
			float alennus = 20.0f;
			float loppuSaldo = alkuSaldo - (listaHinta * (1 - alennus / 100));
			Asiakas asiakas = new Asiakas(alkuSaldo);
			Tuote tuote = new Tuote("TDD in Action", listaHinta);
			
			// Record
			when(hinnoittelijaMock.getAlennusProsentti(asiakas, tuote))
			.thenReturn(alennus);
			
			// Act
			TilaustenKäsittely käsittelijä = new TilaustenKäsittely();
			käsittelijä.setHinnoittelija(hinnoittelijaMock);
			käsittelijä.käsittele(new Tilaus(asiakas, tuote));
			
			// Assert
			assertEquals(loppuSaldo, asiakas.getSaldo(), 0.001);
			verify(hinnoittelijaMock, times(2)).getAlennusProsentti(asiakas, tuote);
		}
		
		@Test
		public void testaaHintaVahemmanKuinSata() {
			float alkuSaldo = 100.0f;
			float listaHinta = 99.99f;
			float alennus = 20.0f;
			float loppuSaldo = alkuSaldo - (listaHinta * (1 - alennus / 100));
			Asiakas asiakas = new Asiakas(alkuSaldo);
			Tuote tuote = new Tuote("MelkeinSata", listaHinta);
			
			when(hinnoittelijaMock.getAlennusProsentti(asiakas, tuote))
			.thenReturn(alennus);
			
			TilaustenKäsittely käsittelijä = new TilaustenKäsittely();
			käsittelijä.setHinnoittelija(hinnoittelijaMock);
			käsittelijä.käsittele(new Tilaus(asiakas, tuote));
			
			assertEquals(loppuSaldo, asiakas.getSaldo(), 0.001);
			verify(hinnoittelijaMock, times(2)).getAlennusProsentti(asiakas, tuote);
		}
		
		@Test
		public void testaaHintaYhtaKuinSata() {
			float alkuSaldo = 100.0f;
			float listaHinta = 100.0f;
			float alennus = 20.0f;
			float loppuSaldo = alkuSaldo - (listaHinta * (1 - (alennus + 5.0f) / 100));
			Asiakas asiakas = new Asiakas(alkuSaldo);
			Tuote tuote = new Tuote("Sata", listaHinta);
			
			// Peräkkäiset kutsut palauttavat ensin alkuperäisen hinnan ja toisella
			// kutsulla päivitetyn hinnan.
			when(hinnoittelijaMock.getAlennusProsentti(asiakas, tuote))
			.thenReturn(alennus, alennus + 5.0f);
			
			TilaustenKäsittely käsittelijä = new TilaustenKäsittely();
			käsittelijä.setHinnoittelija(hinnoittelijaMock);
			käsittelijä.käsittele(new Tilaus(asiakas, tuote));
			
			assertEquals(loppuSaldo, asiakas.getSaldo(), 0.001);
			verify(hinnoittelijaMock, times(2)).getAlennusProsentti(asiakas, tuote);
			// Tarkistetaan päivittääkö hinnoittelija alennusprosentin oikein.
			verify(hinnoittelijaMock).setAlennusProsentti(asiakas, alennus + 5.0f);
		}
}
