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

package org.lugons.libre.lucene.rawdokumenta;

import java.io.*;
import java.util.ArrayList;

/**
 * @author Dean Chugall
 * @version 0.1
 * 
 *          RawDokumenta klasa koja pronalazi sve PDF datoteke u fasciklama i
 *          podfasciklama. Ništa specijalno i verovatno će biti izmenjena u
 *          budućim verzijama.
 */

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