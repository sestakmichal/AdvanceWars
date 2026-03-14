# AI Audit Log - Tým xlogin00 (vzor)

**Datum poslední aktualizace:** 14. 3. 2025

---

## 1. Pathfinding (Dijkstra)
* **Nástroj:** ChatGPT-4o
* **Datum:** 12. 3. 2025
* **Prompt:**
  > "Implementuj Dijkstrův algoritmus v Javě pro hledání cesty na 2D mřížce, kde každé políčko má jinou cenu průchodu."
* **Úprava studentem:**
  Kód z ChatGPT používal `int[][] graph`, musel jsem to přepsat na naše objekty `Tile` a `GameMap`. Opravena chyba v prioritní frontě (nekonečná smyčka při `cost == 0`).
* **Míra generování:** 60 % (kostra algoritmu), 40 % (implementace do našeho modelu).

---

## 2. Observer Pattern
* **Nástroj:** GitHub Copilot
* **Datum:** 14. 3. 2025
* **Použití:** Autocomplete při psaní metod `addObserver` a `notifyObservers`.
* **Úprava studentem:** Žádná, šlo o boilerplate kód vygenerovaný IDE pluginem.
* **Míra generování:** 90 % (pouze kontrola syntaxe).

---

## 3. Basic principles
* **Nástroj:** ChatGPT-5 mini.
* **Datum:** 10. 3. 2025
* * **Prompt:** "Java best way to define rules for movement in strategy game"
* **Použití:** Porovnanie základných princípov a spôsobov použiteľných v jave pre .
* **Míra generování:** 10 %.

---

## 4. Unit Testy
* **Nástroj:** -
* **Datum:** 15. 3. 2025
* **Použití:** Bez použití AI.
* **Úprava studentem:** Napsáno ručně dle zadání `Homework2Test.java`.
* **Míra generování:** 0 %.