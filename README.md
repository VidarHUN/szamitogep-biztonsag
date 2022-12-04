# Számítógép biztonság - hitmit

## Git clone

```
git clone git@github.com:VidarHUN/szamitogep-biztonsag.git --recursive
```

## Követelmények

- **Funkcionalitás**
  - felhasználóknak kell tudni regisztrálni és belépni
  - felhasználóknak kell tudni CAFF fájlt feltölteni, letölteni, keresni
  - felhasználóknak kell tudni CAFF fájlhoz megjegyzést hozzáfűzni
  - a rendszerben legyen adminisztrátor felhasználó, aki tud adatokat módosítani, törölni
- **Szerver oldali funkciókövetelmények**
  - CAFF feldolgozási képesség
  - teljesítménymegfontolásokból C/C++ nyelven kell implementálni
  - feladat: a CAFF fájlból egy előnézet generálása a webshopban megjelenítéshez
- **Kliens oldali követelmények**
  - vagy egy webes vagy iOS vagy Android implementáció

## Tervezés - 2022.10.19.
Az első fázis során a tervezésen van a hangsúly. Ebben a részben a rendszer architektúraterveit kell elkészíteni. A beadáskor az SDL folyamat requirements és design fázisaihoz tartozó dokumentumokat, valamint egy tesztelési tervet kell leadni. Ezeket az anyagokat a projekt wiki oldalán kell elkészíteni. (A beadás a wiki git commit.)

## Natív komponens - 2022.11.09.
A második fázisban a szerver oldali komponensek közül a natív nyelven implementált C/C++ parsert kell leadni a hozzá tartozó Makefile-lal együtt.

## Implementáció és tesztelés - 2022.12.04.
A harmadik fázis a végleges projektleadás. Ekkorra a rendszer egészének működőképesnek kell lennie (gitben), és az ehhez tartozó dokumentumoknak is el kell készülniük (wikiben). A projektet a félév végén be kell mutatni működés közben. A leadás előtt várunk egy felosztást (százalékban), hogy melyik csapattag mennyit dolgozott a projekten, ami alapján a végső pontszámból részesülni fog.

## Demo - 2022.12.07.
A félév végén minden csapatnak be kell mutatnia az elkészült alkalmazást. A demón friss tesztinputokat is kapnak a csapatok, és megnézzük, hogy jól kezeli-e azokat a programjuk. A demóra az utolsó előadás órarendi idejében kerül sor, és Teams-en keresztül fog zajlani.
