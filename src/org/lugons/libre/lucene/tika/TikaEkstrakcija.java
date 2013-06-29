package org.lugons.libre.lucene.tika;

import org.lugons.libre.lucene.rawdokumenta.RawDokumenta;
import java.io.*;
import java.util.HashMap;
import java.util.Map;

import org.apache.tika.io.IOUtils;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.AutoDetectParser;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.sax.BodyContentHandler;

import org.xml.sax.ContentHandler;

public class TikaEkstrakcija implements Runnable {

	private static RawDokumenta raw;
	private String text;
	private Map<String, String> metaData;

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

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

				pdfParsiranje(raw.getListaFajlova().get(i).getAbsolutePath());
				System.out.println("Nađeni fajl-ovi: " + raw.getListaFajlova().get(i).getName());
			}

		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	private void pdfParsiranje(String putanja) throws IOException {

		InputStream is = null;
		ContentHandler nosacSadrzaja;
		Metadata md;
		AutoDetectParser parser;

		try {
			metaData = new HashMap<String, String>();
			md = new Metadata();

			is = new FileInputStream(putanja);
			nosacSadrzaja = new BodyContentHandler(-1);

			parser = new AutoDetectParser();
			parser.parse(is, nosacSadrzaja, md, new ParseContext());
			processMetaData(md);
			sviMetapodaci(getMetaData());
			System.out.println(nosacSadrzaja.toString());

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (is != null)
				IOUtils.closeQuietly(is);
		}
	}

	public void sviMetapodaci(Map<String, String> map) {
		for (Object key : map.keySet()) {
			System.out.println("Key : " + key.toString() + " Value : " + map.get(key));
		}
	}

	// Upisujem parsiran PDF u tekstualni fajl
	// TODO uraditi ovu metodu
	void writeTexttoFile(String pdfText, String fileName) {

		System.out.println("\nUpisujem parsiran PDF u tekstualni fajl " + fileName + "....");
		try {
			PrintWriter pw = new PrintWriter(fileName);
			pw.print(pdfText);
			pw.close();
		} catch (Exception e) {
			System.out.println("Exception uhvaćen prilikom upisivanja PDF teksta u datoteku.");
			e.printStackTrace();
		}
		System.out.println("Gotovo.");
	}

	public static void main(String[] args) throws IOException, InterruptedException {
		System.out.println("Unesite putanju do direktorijuma ili fajla:" + 
							" (npr. /tmp/Biblioteka ili c:\\temp\\Biblioteka)");
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

			System.out.println("Ekstraktujem ....");

			// Čekaj 4 sec. da se završi
			tre.join(4000);
		}

		System.out.println("GOOOOTOVO");

	}

}