- SVG Datei einfach in Incscape �ffnen --> �ndern --> Speichern --> Quelltext in HTML kopieren
- In der ProductMap keine Ids aus dem OpenERP eintragen, sondern die passenden Locationnamen, wie sie in der Location hinterlegt sind (Beispiel: https://user.fablab.fau.de/~buildserver/pricelist/output/price_list-Alle_Produkte.html)
- Leerzeichen durch Bindestriche austauschen. "Kiste_Dioden" anstatt "Kiste Dioden"
- Umlaute auschreiben (ae statt �)

location = location.replace("  / ", "/");
        location = location.replace(" / ","/");
        location = location.replace(",", "");
        location = location.replace(" ","_");
        location = location.replace("�", "ae");
        location = location.replace("�", "oe");
        location = location.replace("�", "ue");
        location = location.replace("�", "Ae");
        location = location.replace("�", "Oe");
        location = location.replace("�", "Ue");
        location = location.replace("�", "ss");