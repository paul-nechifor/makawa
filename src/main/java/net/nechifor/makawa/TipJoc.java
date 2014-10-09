package net.nechifor.makawa;

public class TipJoc
{
	/** Denumirile tipurilor de cărți, pentru că unii spun altfel. */
	public String[] denumiriTipuri = {"inimă", "romb", "frunză", "treflă"};

	/** Denumirile pentru toate numerele pentru că sunt și alte variante. */
	public String[] denumiriNumere = {"doi", "trei", "patru", "cinci", "șase", "șapte", "opt", "nouă", "zece",
	   	"as", "valet", "regină", "rege"};

	/** Denumirile pentru jocheri, poate uni vor să zică „joker“. */
	public String[] denumiriJocheri = {"jocher negru", "jocher roșu"};

	/** Limita absolută de jucători este 10, dar e greu de jucat așa. */
	public int numarMaximDeJucatori= 6;

	/** Dacă se joacă și cu cei doi jocheri. */
	public boolean cuJocheri = true;

	/**
	 * Regula cu stopare: când cineva are de luat cărți, poate să anuleze punând cartea de stopare care trebuie
	 * precizată în membrul <code>carteaDeStopare</code>.
	 */
	public boolean cuStopare = false;

	/**
	 * Regula cu „cere carte“: dacă cineva pune cartea precizată în <code>carteaCareCere</code> trebuie să pună
	 * și altă carte. Regula e un fel de „stai o tură“ amestecată cu „schimb în …“.
	 */
	public boolean cuCereCarte = false;

	/**
	 * Dacă trebuie să zică „stop macaua“ când a terminat de pus toate cărțile. Dacă nu zice trebuie să ia
	 * numărul de cărți precizat în membrul <code>pedeapsaStopMacaua</code>.
	 */
	public boolean cuStopMacaua = false;

	/** Înseamnă că jucatorul poate pune toate cărțile de același număr pe care le are. */
	public boolean cuCartiMultiple = true;

	/** Dacă cineva a dat de luat un jocher, pedeapsa poate fi pasată doar de celălalt jocher */
	public boolean pesteJocherDoarJocher = true;

	/**
	 * După ce cineva a luat pedeapsa care se termină în jocher, următorul jucător are voie să pună doar cărți
	 * de aceeași culoare cu acel jocher dacă e <code>true</code>. Dacă e <code>false</code> poate să pună ce vrea.
	 */
	public boolean pesteJocherDoarCuloareaLui = true;

	/** Peste o carte de luat, se poate pune orice altă carte de luat. */
	public boolean pesteLuatOriceLuat = false;

	/** Carte de luat se poate pune peste orice. Nu trebuie să se potrivească. */
	public boolean pesteOriceDeLuat = false;

	/**
	 * Dacă se amestecă după ce se termină teancul, când cineva ia mai multe cărți are șanse mai mici să se
	 * potrivească. Este mai bine pus pe <code>true</code> când se joacă cu roboți căci altfel vor ști ordinea în
	 * care vin cărțile.
	 */
	public boolean amestecareDupaTerminareTeanc = false;

	/** Cartea cu care se face schimbarea tipului de carte (de obicei 7 sau 11). */
	public int carteaDeSchimbat = 7;

	/** Numărul cărții care obligă la stat o tură (de obicei 11 sau 4). */
	public int carteaDeStatTura = 11;

	/** Cartea cu care se face stopare, dacă <code>cuStopare</code> este <code>true</code>. */
	public int carteaDeStopare;

	/** Numărul cărții peste care se pune altă carte imediat, dacă <code>cuCereCarte</code> este <code>true</code>. */
	public int carteaCareCere;

	/** Pedeapsa în număr de cărți pe care jucătorul care nu a zis „macaua“ trebuie s-o ia. */
	public int pedeapsaMacaua = 3;

	/** Pedeapsa în număr de cărți pe care jucătorul care nu a zis „stop macaua“ trebuie s-o ia. */
	public int pedeapsaStopMacaua;

	/** Pedeapsa în număr de cărți a jocherului negru (de obicei 5). */
	public int pedeapsaJocherNegru = 5;

	/** Pedeapsa în număr de cărți a jocherului roșu (de obicei 10 sau 5, ca la jocherul negru). */
	public int pedeapsaJocherRosu = 10;
}
