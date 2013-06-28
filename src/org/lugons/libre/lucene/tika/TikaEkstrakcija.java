package org.lugons.libre.lucene.tika;

import org.lugons.libre.lucene.rawdokumenta.RawDokumenta;
import java.io.*;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.parser.pdf.PDFParser;
import org.apache.tika.sax.BodyContentHandler;

import org.xml.sax.ContentHandler;

public class TikaEkstrakcija {

	public void tikaEkstrakt(String datoteka) throws IOException {

		InputStream is = null;
		ContentHandler nosacSadrzaja;
		Metadata metadata;
		PDFParser pdfParser;

		try {

			is = new FileInputStream(datoteka);
			nosacSadrzaja = new BodyContentHandler();
			metadata = new Metadata();
			pdfParser = new PDFParser();
			pdfParser.parse(is, nosacSadrzaja, metadata, new ParseContext());

			System.out.println(nosacSadrzaja.toString());
			sviMetapodaci(metadata);

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (is != null)
				is.close();
		}
	}

	public void sviMetapodaci(Metadata metadata) {
		for (int i = 0; i < metadata.names().length; i++) {
			String name = metadata.names()[i];
			System.out.println(name + " : " + metadata.get(name));
		}
	}

	public static void main(String[] args) throws IOException {
		System.out.println("Unesite putanju do direktorijuma ili fajla:"
				+ " (npr. /tmp/Biblioteka ili c:\\temp\\Biblioteka)");
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		String unosKorisnika = br.readLine();
		RawDokumenta raw = new RawDokumenta(unosKorisnika);
		TikaEkstrakcija tikaExt = new TikaEkstrakcija();

		System.out.println("Pronađeni fajlovi:  " + raw.getListaFajlova());
		System.out.println("============================================");
		System.out.println("||           TIKA EKSTRAKCIJA             ||");
		System.out.println("============================================");
		System.out.println("");
		System.out.println("============================================");
		System.out.println("||               METAPODACI               ||");
		System.out.println("============================================");

		for (File file : raw.getListaFajlova()) {
			System.out.println("Nađeni fajl-ovi: " + file.getName());
			tikaExt.tikaEkstrakt(file.getAbsolutePath());
		}

	}

}
