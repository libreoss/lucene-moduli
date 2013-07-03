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

public class TikaEkstrakcija implements Runnable {

	static Logger log = Logger.getLogger(TikaEkstrakcija.class.getName());

	//final static String DIREKTORIJUM = "C:\\Temp\\input.txt";
	//final static String OUTPUT_FILE_NAME = "C:\\Temp\\output.txt";
	final static Charset ENCODING = StandardCharsets.UTF_8;

	public static String text;
	private static RawDokumenta raw;
	private Map<String, String> metaData;

	public Map<String, String> getMetaData() {
		return metaData;
	}

	public void setMetaData(Map<String, String> metaData) {
		this.metaData = metaData;
	}

	private void processMetaData(Metadata metadata) {
		if ((getMetaData() == null) || (!getMetaData().isEmpty())) {
			setMetaData(new HashMap<String, String>());
		}
		for (String name : metadata.names()) {
			getMetaData().put(name.toLowerCase(), metadata.get(name));

		}

	}

	@Override
	public void run() {
		try {
			for (int i = 0; i < raw.getListaFajlova().size(); i++) {
				// System.out.println(pdfParsiranje(raw.getListaFajlova().get(i).getAbsolutePath()));
				log.info("Nađeni fajl-ovi: " + raw.getListaFajlova().get(i).getName());
				//System.out.println("Nađeni fajl-ovi: " + raw.getListaFajlova().get(i).getName());
				text = raw.getListaFajlova().get(i).getName();
				upisiParsiranTekst(raw.getListaFajlova().get(i).getName() + ".txt", pdfParsiranje(raw.getListaFajlova().get(i).getAbsolutePath()));
			}

		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	private String pdfParsiranje(String putanja) throws IOException {

		InputStream is = null;
		ContentHandler nosacSadrzaja = null;
		Metadata md;
		AutoDetectParser parser;

		try {
			md = new Metadata();

			is = new FileInputStream(putanja);

			/**
			 * Maksimalan broj karaktera za upis u stream. -1 za MAX
			 */
			nosacSadrzaja = new BodyContentHandler(-1);

			parser = new AutoDetectParser();
			parser.parse(is, nosacSadrzaja, md, new ParseContext());
			processMetaData(md);
			sviMetapodaci(getMetaData());
			//log.info(nosacSadrzaja.toString());
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (is != null)
				IOUtils.closeQuietly(is);
		}
		return nosacSadrzaja.toString();
	}

	public void sviMetapodaci(Map<String, String> map) {
		for (Object key : map.keySet()) {
			System.out.println("Key : " + key.toString() + " Value : " + map.get(key));
		}
	}

	// Upisujem parsiran PDF u tekstualni fajl
	private void upisiParsiranTekst(String imeDatoteke, String parsiranTekst) throws IOException {
		Path path = Paths.get(imeDatoteke);
		try (BufferedWriter writer = Files.newBufferedWriter(path, ENCODING)) {

			writer.write(parsiranTekst);
			writer.newLine();

		}
	}

	public static void main(String[] args) throws IOException, InterruptedException {
		System.out.println("Unesite putanju do direktorijuma ili fajla:" + " (npr. /tmp/Biblioteka ili c:\\temp\\Biblioteka)");
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		String unosKorisnika = br.readLine();
		raw = new RawDokumenta(unosKorisnika);
		System.out.println("Pronađeni fajlovi:  " + raw.getListaFajlova());
		System.out.println("============================================");
		System.out.println("||           TIKA EKSTRAKCIJA             ||");
		System.out.println("============================================");
		System.out.println("");
		System.out.println("============================================");
		System.out.println("||               METAPODACI               ||");
		System.out.println("============================================");

		Thread tre = new Thread(new TikaEkstrakcija());
		tre.start();
		while (tre.isAlive()) {
			// Čekaj 5 sek. pa obavesti
			tre.join(5000);

			System.out.println("Ekstraktujem ...." + TikaEkstrakcija.text);

		}

		System.out.println("GOOOOTOVO");

	}

}