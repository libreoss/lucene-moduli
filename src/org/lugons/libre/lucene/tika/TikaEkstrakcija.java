/*
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.

 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.

 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses />.
 */

package org.lugons.libre.lucene.tika;

import org.lugons.libre.lucene.rawdokumenta.RawDokumenta;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.apache.tika.io.IOUtils;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.AutoDetectParser;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.sax.BodyContentHandler;

import org.xml.sax.ContentHandler;

/**
 * @author Dean Chugall
 * @version 0.1
 * 
 *          TikaEkstrakcija klasa koja upotrebljava TIKA biblioteku i koja
 *          ekstraktuje metadata, sadržaj, dokumenata. Nakon ekstrakcije upisuje
 *          u tekstualnu datoteku koja će biti prosleđena LUCENE biblioteci.
 */

public class TikaEkstrakcija implements Runnable {

	private static Logger log = Logger.getLogger(TikaEkstrakcija.class.getName());

	public static String putanjaZaEkstrakciju;
	public static String nadjeneDatoteke;

	private final static Charset ENCODING = StandardCharsets.UTF_8;
	private static RawDokumenta raw;
	private Map<String, String> metaData;

	public Map<String, String> getMetaData() {
		return metaData;
	}

	/**
	 * TIKA implementacija parsiranja dokumenata koja uzima za parametar putanju
	 * do datoteke i vraća String ekstraktovanih vrednosti (metapodataka i
	 * sadržaja) Implementacija core TIKA interfejsa - "Parser".
	 * 
	 * @param putanja
	 *            Putanja do datoteke koju treba parsirati.
	 * @return Ekstraktovane podatke iz datoteke koja je prosleđena
	 * @throws IOException
	 *             prilikom greške
	 */
	private String parsiranjeDokumenata(String putanja) throws IOException {

		InputStream is = null;
		ContentHandler nosacSadrzaja = null;
		Metadata metadata;
		AutoDetectParser parser;

		try {
			metadata = new Metadata();

			is = new FileInputStream(putanja);

			// Maksimalan broj karaktera za upis u InputStream. -1 za MAX
			nosacSadrzaja = new BodyContentHandler(-1);

			parser = new AutoDetectParser();
			parser.parse(is, nosacSadrzaja, metadata, new ParseContext());
			processMetaData(metadata);
			sviMetapodaci(getMetaData());
			// log.info(nosacSadrzaja.toString());
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (is != null)
				IOUtils.closeQuietly(is);
		}
		return nosacSadrzaja.toString();
	}

	/**
	 * Upis svih ekstraktovanih metapodataka u MAP strukturu. Poyiv u metodi
	 * "parsiranjeDokumenata".
	 * 
	 * @param metadata
	 *            TIKA Metadata ekstraktovani podaci
	 * 
	 */
	private void processMetaData(Metadata metadata) {

		if ((getMetaData() == null) || (!getMetaData().isEmpty())) {
			metaData = new HashMap<String, String>();
		}
		for (String name : metadata.names()) {
			getMetaData().put(name.toLowerCase(), metadata.get(name));
		}
	}

	/**
	 * Ispis svih metapodataka iz MAP strukture podataka.
	 * 
	 * @param metaPodaci
	 *            struktura koja poseduje metapodatke dokumenta.
	 * 
	 */
	public void sviMetapodaci(Map<String, String> metaPodaci) {
		for (Object key : metaPodaci.keySet()) {
			System.out.println("Key : " + key.toString() + " Value : " + metaPodaci.get(key));
		}
	}

	/**
	 * Upis parsiranog dokumenta u tekstualnu datoteku. Ako se ne unese
	 * "putanjaZaEkstrakciju", upisuje se u ROOT.
	 * 
	 * @param imeDatoteke
	 *            ime datoteka koja se trenutno upisuje potrebna za naziv txt
	 *            datoteke.
	 * @param parsiranTekst
	 *            Sadržaj parsiranog teksta bez metapodataka
	 * @throws IOException
	 *             prilikom greške upisa
	 */
	private void upisiParsiranTekst(String imeDatoteke, String parsiranTekst) throws IOException {

		// Ako se ništa ne unese upisuje se u ROOT projekta
		// TODO obrisati ekstenziju
		Path putanja = Paths.get(putanjaZaEkstrakciju + imeDatoteke);

		try (BufferedWriter writer = Files.newBufferedWriter(putanja, ENCODING)) {
			writer.write(parsiranTekst);
			writer.newLine();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Nit u kojoj se sve izvršava, poziv u nekom EventHandleru ili MAIN metodi.
	 */
	@Override
	public void run() {
		try {
			for (int i = 0; i < raw.getListaFajlova().size(); i++) {
				log.info("Nađeni fajl-ovi: " + raw.getListaFajlova().get(i).getName());
				nadjeneDatoteke = raw.getListaFajlova().get(i).getName();
				upisiParsiranTekst(raw.getListaFajlova().get(i).getName() + ".txt", parsiranjeDokumenata(raw
				        .getListaFajlova().get(i).getAbsolutePath()));
			}

		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public static void main(String[] args) throws IOException, InterruptedException {
		System.out.println("============================================");
		System.out.println("||           TIKA EKSTRAKCIJA             ||");
		System.out.println("============================================");

		System.out.println("Unesite putanju do direktorijuma ili fajla koje "
		        + " treba ekstraktovati: \n(npr. /tmp/Biblioteka ili C:\\temp\\Biblioteka)");

		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		String unosKorisnika = br.readLine();

		raw = new RawDokumenta(unosKorisnika);
		System.out.println("Unesite putanju do fascikle gde se upisuju ekstraktovane datoteke ?");
		String unosPutanjeZaUpisTxt = br.readLine();
		TikaEkstrakcija.putanjaZaEkstrakciju = unosPutanjeZaUpisTxt;

		System.out.println("Pronađeni fajlovi:  " + raw.getListaFajlova());
		System.out.println("");
		System.out.println("============================================");
		System.out.println("||               METAPODACI               ||");
		System.out.println("============================================");

		Thread tre = new Thread(new TikaEkstrakcija());
		tre.start();
		while (tre.isAlive()) {
			// Čekaj 5 sek. pa obavesti
			tre.join(5000);

			System.out.println("Ekstraktujem ...." + TikaEkstrakcija.nadjeneDatoteke);

		}
		System.out.println("");
		System.out.println("Datoteka ekstraktovana i upisana na lokaciji: " + TikaEkstrakcija.putanjaZaEkstrakciju);

	}

}