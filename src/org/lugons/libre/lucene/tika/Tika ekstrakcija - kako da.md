Klasa "TikaEkstrakcija", kako da...
===================
##### Kratko uputstvo kako koristiti klasu "TikaEkstrakcija".

 
 
![Libre_Lucene-Moduli](http://www.deanchugall.info/LibreSlike/LiBRE-Lucene-application-LOGO_.png "Logo Title Text 1")

## Pokretanje modula

Modul se pokreće iz IDE programerskog okruženja(Eclipse), i ceo rezultat se ispisuje u konzolnom ispisu istog.
Nakon pokretanja dobićete poruku:
#####"Unesite putanju do direktorijuma ili fajla koje treba ekstraktovati: (npr. /tmp/Biblioteka ili C:\\temp\\Biblioteka)"
					
Unosite putanju do fascikle gde vam se nalaze LiBRE! časopisi. Prvo bi trebalo da se u neku fasciklu ubaci samo jedan
broj časopisa, jer ekstrakciji ipak treba malo vremena pa da ne bih čekali...

Nakon unosa dobićete još jedno pitanje, a to je gde želite da ekstraktovan sadržaj bude upisan, a pitanje glasi:
#####"Unesite putanju do fascikle gde se upisuju ekstraktovane datoteke"

Ovako bi to izgledalo nako unošenja parametara, a na Windows platformi:

![Libre_Lucene-Moduli](http://www.deanchugall.info/LibreSlike/Modul_Tika/Tika-kako-da.png "Tika modul, kako da...")
######Obavezno stavite znak "\" na kraju putanje za Windows korisnike, dok "/" za GNU/Linux korisnike.
===================
Nakon unetih traženih parametara klikom na Enter započinje se ekstrakcija. U zavisnosti od veličine prosleđenog
dokumenta, ekstrakcija može da potraje, tako da je potrebno samo malo strpljenja.

Posle, nadamo se, uspešne ekstrakcije dobićete poruku:

##### "Datoteka ekstraktovana i upisana na lokaciji: " + vaša putanja do datoteke.

Slika ispod prikazuje kako bi to trebalo da izgleda u konzolnom ispisu IDE okruženja:

![Libre_Lucene-Moduli](http://www.deanchugall.info/LibreSlike/Modul_Tika/Nakon_ekstrakcije.png "Tika modul, kako da...")

U ovom trenutku trebalo bi da odete do fascikle koju ste zadali u početnim podešavanjima i da vas čeka tekstualni fajl sa 
ekstraktovanim sadržajem dokumenta.

===================

Toliko za ovaj modul, ako imate bilo kakve probleme otvorite ISSUES na adresi: https://github.com/libreoss/lucene-moduli/issues?page=1&state=open 
,pa ćemo pokušati da rešimo.
