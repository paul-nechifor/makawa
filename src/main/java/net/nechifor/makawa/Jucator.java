package net.nechifor.makawa;

/**
 * Aceasta este clasa abstractă pe care trebuie s-o moștenească jucătorii de Macaua. Fiecare clasă este
 * responsabilă să țină minte care sunt cărțile puse joc, câți jucători mai sunt în joc și altele. Obiectele
 * <code>Jucator</code> sunt controlate în totalitate de către un obiect <code>Joc</code>. Un 
 * <code>Jucator</code> este responsabil de propria strategia și de datele pe care trebuie să le memoreze pentru a o
 * pune în aplicare. El poate să încerce să facă orice tip de mutare. Este rolul obiectului <code>Joc</code> să
 * vadă dacă mutările sunt legale sau nu.
 * @author		Paul Nechifor
 * @version		1.0
 * @see Joc
 */
public abstract class Jucator
{
	protected String nume;
	/** Dacă este <code>true</code> atunci când jucatorul face o mutare ilegală va primi alte șanse. */
	protected boolean jucatorUman = false;

	public Jucator(String nume)
	{
		this.nume = nume;
	}

	/**
	 * Metoda prin care jucătorul primește cărțile și este anunțat de începerea jocului. Ordinea jucătorilor din
	 * parametrul <code>jucatori</code> contează și este inclus jucătorul acesta. Primul jucător este cel care face
	 * prima mutare. Jucatorul își poate afla ordinea în joc căutându-se în vector.
	 * @param tipJoc		tipul de joc de macaua
	 * @param jucatori		vectorul de jucători
	 * @param carti			vectorul de cinci cărți pe care le primește la începutul jocului
	 * @param primaCarte	prima carte pusă jos
	 */
	public abstract void start(TipJoc tipJoc, Jucator[] jucatori, Carte[] carti, Carte primaCarte);

	/**
	 * Metoda prin care jucătorul este anunțat de mutările celorlalți. Implementarea bună a metodei este esențială
	 * pentru un „AI“ pentru a analiza jocul bine.
	 * @param jucator		care jucător face mutarea
	 * @param mutare		ce mutare a făcut jucătorul
	 */
	public abstract void mutareEfectuata(Jucator jucator, Mutare mutare);

	/**
	 * Metoda care este apelată de către un obiect <code>Joc</code> pentru a afla ce mutare face acest jucător.
	 * @return				un obiect ce descrie mutarea ce vrea să o efectueze
	 */
	public abstract Mutare muta();

	/**
	 * Metoda prin care jucătorul primește carți din teanc (nu primele cinci). Vectorul de cărți poate fi 
	 * <code>null</code> sau mai mic decât numărul de cărți cerut în mutare atunci când nu sunt destule cărți de luat.
	 * @param carti			un vector care conține cărțile
	 */
	public abstract void primireCarti(Carte[] carti);

	/**
	 * Metoda prin care jucătorul este anunțat că a încercat o mutare ilegală. Legalitatea unei mutări depinde de
	 * tipul de joc. După asta va mai fi apelată metoda <code>muta()</code> pentru a mai încerca o mutare. 
	 * @param motiv			motivul pentru care mutarea precedentă nu a fost legală
	 */
	public abstract void mutareIlegala(String motiv);

	/**
	 * Întoarce numele acestui jucător pentru a ține evidența pentru depanare și altele.
	 * @return				numele acestui jucător
	 */
	public final String getNume()
	{
		return nume;
	}
}
