package org.lugons.libre.lucene.rawdokumenta;
 
import java.io.*;
import java.util.ArrayList;
 
public class RawDokumenta {
 
	private ArrayList<File> listaFajlova = new ArrayList<File>();
 
	public RawDokumenta(String putanja) {
		pronadjiFajlove(new File(putanja));
	}
 
	public ArrayList<File> getListaFajlova() {
		return listaFajlova;
	}
 
	private void pronadjiFajlove(File file) {
		// ako fascikla ili datoteka ne postoje.
		if (!file.exists()) {
			System.out.println(file + " ne postoji.");
		}
		// ako je fascikla - Recursion 
		if (file.isDirectory()) {
			for (File f : file.listFiles()) {
				pronadjiFajlove(f);
			}
		} else {
			String imeFajla = file.getName().toLowerCase();
			// ===================================================
			// Samo pronađi PDF datoteke
			// ===================================================
			if (imeFajla.endsWith(".pdf")) {
				// System.out.println("Nađen fajl: " + file.getName());
				getListaFajlova().add(file);
			} else {
				// System.out.println("Preskočeno " + filename);
			}
		}
 
	}
 
	public static void main(String[] args) throws IOException {
 
		System.out.println("Unesite putanju do direktorijuma ili fajla: (npr. /tmp/Biblioteka ili c:\\temp\\Biblioteka)");
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		String unosKorisnika = br.readLine();
		RawDokumenta raw = new RawDokumenta(unosKorisnika);
 
		System.out.println("Pronađeni fajlovi:  " + raw.getListaFajlova());
	}
}