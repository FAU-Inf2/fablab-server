- SVG Datei einfach in Incscape öffnen --> Ändern --> Speichern --> Quelltext in HTML kopieren
- In der ProductMap keine Ids aus dem OpenERP eintragen, sondern die passenden Locationnamen, wie sie in der Location hinterlegt sind (Beispiel: https://user.fablab.fau.de/~buildserver/pricelist/output/price_list-Alle_Produkte.html)
- Leerzeichen durch Bindestriche austauschen. "Kiste_Dioden" anstatt "Kiste Dioden"
- Umlaute auschreiben (ae statt ä)

location = location.replace("  / ", "/");
        location = location.replace(" / ","/");
        location = location.replace(",", "");
        location = location.replace(" ","_");
        location = location.replace("ä", "ae");
        location = location.replace("ö", "oe");
        location = location.replace("ü", "ue");
        location = location.replace("Ä", "Ae");
        location = location.replace("Ö", "Oe");
        location = location.replace("Ü", "Ue");
        location = location.replace("ß", "ss");