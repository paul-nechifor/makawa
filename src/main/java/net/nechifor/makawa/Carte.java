package net.nechifor.makawa;

import java.util.Vector;
import java.lang.reflect.Array;

class ExceptieCarte extends RuntimeException { public ExceptieCarte(String mesaj) { super(mesaj); } }

/**
 * Ține minte tipul și numărul unei cărți. Simbolurile pentru cărți sunt alcătuite din tip și număr. 
 * Literele sunt I, R, F, T și J care reprezintă inimă, romb, frunză, treflă și jocher. Asul este văzut doar ca
 * 11, și nu 1. Simbolurile pentu jocheri sunt J0 pentru jocher negru și J1 pentru jocher roșu. Spre exemplu R4
 * este patru de romb și T14 este rege de treflă.
 * @author		Paul Nechifor
 * @version		1.0
 */
public class Carte
{
	private static String tipuri = "IRFTJ";

	protected String tip;
	protected int numar;

	/** Construiește cartea dintr-un simbol string. Literele IRFT au numere de la 2 la 14, iar J are 0 și 1.
	 * @param strCarte		simbolul
	 */
	public Carte(String strCarte)
	{
		tip = strCarte.substring(0, 1);
		if (tipuri.indexOf(tip) < 0)
			throw new ExceptieCarte("Nu există tipul de carte '" + tip +"'!");
		numar = Integer.parseInt(strCarte.substring(1));
		if (tip.equals("J"))
		{
			if ((numar < 0 || numar > 1))
				throw new ExceptieCarte("Nu există decât J0=jocher negru și J1=jocher roșu!");
		}
		else
		{
			if (numar < 2 || numar > 14)
				throw new ExceptieCarte("Nu există numărul de carte '" + numar + "'!");
		}
	}
	/**
	 * Întoarce numele unei cărți. Metoda are nevoie de un obiect <code>TipJoc</code> pentru că denumirile pentru
	 * cărți diferă. Eu zic treflă, alții zic ghindă.
	 * @param tipJoc		tipul jocului care specifică denumirile pentru cărți.
	 * @return				un <code>String</code> conținând numele
	 */
	public String nume(TipJoc tipJoc) // numele unei cărții depinde de regulile jocului
	{
		if (tip.equals("J")) return tipJoc.denumiriJocheri[numar];
		else return tipJoc.denumiriNumere[numar - 2] + " de " + tipJoc.denumiriTipuri[tipuri.indexOf(tip)];
	}

	/**
	 * Reconstituie simbolul pentru carte. Același simbol a fost folosit în constructor.
	 * @return				simbolul cărții
	 */
	public String simbol()
	{
		return tip + numar;
	}

	public String simbolFaraLitere() // quick and very dirty
	{
		String n;
		if (numar <= 1) n = "";
		else if (numar == 11) n = "A";
		else if (numar == 12) n = "V";
		else if (numar == 13) n = "Q";
		else if (numar == 14) n = "K";
		else n = "" + numar;
		String t = tip.replace("I", "♥").replace("R", "♦").replace("F", "♠").replace("T", "♣");
		if (tip.equals("I") || tip.equals("R") || numar == 1) return "\033[00;31m" + n + t + "\033[00m";
		return "\033[00;30m" + n + t + "\033[00m";
	}
	
	/**
	 * Verifică egalitatea cărților.
	 * @param c1			cartea cu care se face comparația
	 * @return				<code>true</code> dacă cărțile sunt aceleași, <code>false</code> altfel
	 */
	public boolean equals(Carte c1)
	{
		return this.simbol().equals(c1.simbol());
	}

	/** Întoarce tipul cărții (IRFT sau J pentru jocher). */
	public String getTip()
	{
		return tip;
	}

	/** Întoarce numărul cărții (de la 2 la 14, sau 0 și 1 pentru jocheri). */
	public int getNumar()
	{
		return numar;
	}

	public final static void scoate(Vector<Carte> carti, Carte c)
	{
		for (int i = 0; i < carti.size(); i++)
			if (carti.get(i).equals(c))
				carti.remove(i);
	}
	public final static Carte[] vectorToArray(Vector<Carte> carti)
	{
		Carte[] c = new Carte[carti.size()];
		for (int i = 0; i < carti.size(); i++)
			c[i] = carti.get(i);
		return c;
	}

	public final static Vector<Carte> arrayToVector(Carte[] carti)
	{
		Vector<Carte> c = new Vector<Carte>();
		for (int i = 0; i < Array.getLength(carti); i++)
			c.add(carti[i]);
		return c;
	}

	public final static String arataCarti(Vector<Carte> carti)
	{
		if (carti.size() == 0) return "";
		// ar trebui un string builder
		String r = "";
		for (int i = 0; i < carti.size(); i++) r += carti.get(i).simbolFaraLitere() + ",";
		return r.substring(0, r.length() - 1);
	}

	public final static String arataCarti(Carte[] carti)
	{
		if (carti == null) return "";
		String r = "";
		for (int i = 0; i < Array.getLength(carti); i++) r += carti[i].simbolFaraLitere() + ",";
		return r.substring(0, r.length() - 1);
	}
}
