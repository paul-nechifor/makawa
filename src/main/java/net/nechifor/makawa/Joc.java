package net.nechifor.makawa;

import java.util.Vector;
import java.lang.reflect.Array;

class ExceptieJoc extends RuntimeException { public ExceptieJoc(String mesaj) { super(mesaj); } }

/**
 * Rolul unui obiect <code>Joc</code> este de a verifica dacă jucătorii fac mutări corecte.
 * @author			Paul Nechifor
 * @version			1.0
 */
public class Joc
{
	protected TipJoc tipJoc;
	protected Jucator[] jucatori;
	protected Vector<Jucator> jucatoriPrezenti;
	protected Vector<Jucator> jucatoriIesiti;
	/** Conține vectorul de cărți care alcătuiește teancul din care se iau cărți (se extrage de la zero). */
	protected Vector<Carte> teanc;
	protected Vector<Carte> cartiPuse;
	/** Conține un vector de vectori ce conțin cărțile curente ale tuturor jucătorilor care mai sunt în joc. */
	protected Vector<Vector<Carte>> cartiJucatori;
	protected Vector<Integer> tureDeStat = new Vector<Integer>(); 
	protected Vector<Mutare> mutari = new Vector<Mutare>();
	/**
	 * Ține minte indecși pentru mutările în care s-a stat prima tură pentru a fi folosit de către funcția
	 * <code>tureDeStat</code>.
	 */
	protected Vector<Integer> marcatorPrimaTura = new Vector<Integer>();

	public Joc(TipJoc tipJoc, Jucator[] jucatori)
	{
		this.tipJoc = tipJoc;
		this.jucatori = jucatori;

		if (Array.getLength(jucatori) < 2) throw new ExceptieJoc("Este nevoie de cel puțin doi jucători!");
		if (Array.getLength(jucatori) > tipJoc.numarMaximDeJucatori)
			throw new ExceptieJoc("Regula jocului spune că nu pot fi mai mult de " + tipJoc.numarMaximDeJucatori
				+ " jucatori!");

		jucatoriPrezenti = new Vector<Jucator>();
		jucatoriIesiti = new Vector<Jucator>();
		for (int i = 0; i < Array.getLength(jucatori); i++)
		{
			jucatoriPrezenti.add(jucatori[i]);
			tureDeStat.add(0);
		}
	}

	public void incepeJocul()
	{
		//adaugă cărțile în joc
		teanc = new Vector<Carte>();
		for (int i=2; i<=14; i++)
		{
			teanc.add(new Carte("I" + i));
			teanc.add(new Carte("R" + i));
			teanc.add(new Carte("F" + i));
			teanc.add(new Carte("T" + i));
		}
		if (tipJoc.cuJocheri)
		{
			teanc.add(new Carte("J0"));
			teanc.add(new Carte("J1"));
		}
		
		amestecareTeanc();

		//împarte cărțile
		cartiJucatori = new Vector<Vector<Carte>>();
		for (int i = 0; i < Array.getLength(jucatori); i++)
		{
			cartiJucatori.add(new Vector<Carte>());
			for (int j=0; j<5; j++)
			{
				Carte scoasa = teanc.remove(0);
				cartiJucatori.get(i).add(scoasa);
			}
		}

		//pune cartea jos
		cartiPuse = new Vector<Carte>();
		cartiPuse.add(teanc.remove(0));

		//prima carte trebuie adaugată ca o mutare, problema este dacă este cartea de schimbat ??????????????????????///////////////////////////////////
		Mutare m = new Mutare();
		m.cartiDePus = new Carte[1];
		m.cartiDePus[0] = cartiPuse.get(0);
		mutari.add(m);

		//trimite-le cărțile
		for (int i = 0; i < Array.getLength(jucatori); i++)
			jucatori[i].start(tipJoc, jucatori, Carte.vectorToArray(cartiJucatori.get(i)), cartiPuse.get(0));

		//marea buclă
		while (jucatoriPrezenti.size() > 1)
		{
			for (int i = 0; i < jucatoriPrezenti.size(); i++)
			{
				Mutare mutare = jucatoriPrezenti.get(i).muta();
				//////////////////////////////////////////////////////////////////////////
				System.out.printf("%s[are %s]: %s\n", jucatoriPrezenti.get(i).getNume(), Carte.arataCarti(cartiJucatori.get(i)), mutare.notatie());
				//////////////////////////////////////////////////////////////////////////
				String motiv = esteMutareIlegala(mutare, i);
				if (motiv.equals("legal"))
				{
					// dacă nu spune macaua trebuie pedepsit


					//se adaugă cărțile
					if (mutare.cartiDePus != null)
					{
						for (int j = 0; j < Array.getLength(mutare.cartiDePus); j++)
						{
							cartiPuse.add(mutare.cartiDePus[j]); //pune cărțile jos
							Carte.scoate(cartiJucatori.get(i), mutare.cartiDePus[j]);
						}
					}

					//se trimit cărțile din teanc
					if (mutare.numarDeCarti > 0)
						trimitereCarti(i, mutare.numarDeCarti);

					//trebuie anunțați ceilanți jucatori de mutare
					for (int j = 0; j < jucatoriPrezenti.size(); j++)
						if (i != j)
							jucatoriPrezenti.get(j).mutareEfectuata(jucatoriPrezenti.get(i), mutare);

					//dacă a rămas fără nicio carte, a câștigat
					if (cartiJucatori.get(i).size() == 0)
					{
						cartiJucatori.remove(i);
						jucatoriIesiti.add(jucatoriPrezenti.remove(i));
						tureDeStat.remove(i);
					}
				}
				else
				{
					// trebuie să mai dau șanse dacă este jucator uman. ////////////////////////////////////////////////////
					jucatoriPrezenti.get(i).mutareIlegala(motiv);
					System.out.println("Sa efectuat o mutare ilegală");
					System.exit(1);
				}
			}
		}
		System.out.println("S-a terminat jocul!==========================================================================");
	}

	/**
	 * Verifică daca mutarea este ilegală. Dacă este ilegală, întoarce motivul. Dacă întoarce <code>"legal"</code>.
	 * înseamnă mutarea este legală.
	 * @param mutare			mutarea care trebuie analizată
	 * @param i					indicele jucătorului
	 * @return					motivul pentru care mutarea este ilegală, sau <code>"legal"</code> dacă-i legală
	 */
	protected String esteMutareIlegala(Mutare mutare, int i)
	{
		String motiv = mutare.verificareLegalitate(tipJoc);
		if (!motiv.equals("legal")) return motiv;
		
		int deLuat = numarDeLuat();
		if (deLuat == 0)
		{
			if (mutare.numarDeCarti > 1)
				return "Nu ai voie să iei mai mult de o carte dacă nu ai fost pedepsit!";
		}
		else
		{
			boolean raspunde = false;
			if (mutare.cartiDePus != null)
			{
				if (mutare.cartiDePus[0].getNumar() <= 3) raspunde = true; //este jocher, 2 sau 3
				if (tipJoc.cuStopare && mutare.cartiDePus[0].getNumar() == tipJoc.carteaDeStopare) raspunde = true;
			}
			if (deLuat == mutare.numarDeCarti) raspunde = true;
			if (mutare.staTura) raspunde = true;
			if (!raspunde) return "Ai fost pedepsit și în loc să dai mai departe sau să iei ai făcut altceva!";
		}

		int deStat = tureDeStat();
		if (deStat > 0)
		{
			boolean raspunde = false;
			if (mutare.cartiDePus != null && mutare.cartiDePus[0].getNumar() == tipJoc.carteaDeStatTura)
				raspunde = true;
			if (mutare.staTura)
			{
				raspunde = true;
				//dacă el deja stă trebuie să pice următorului
				if (tureDeStat.get(i) == 0)
				{
					tureDeStat.set(i, deStat);
					adaugaCaPrimaTura(mutari.size());
				}
			}
			if (!raspunde) return "Trebuie să stai tura, dar n-ai dat mai departe și nici n-ai stat!";
		}

		if (mutare.staTura)
		{
			if (tureDeStat.get(i) == 0)
				return "Ai stat tura fără motiv!";
		}
		else
		{
			if (tureDeStat.get(i) > 0)
			{
				System.out.println(tureDeStat.get(i));
				return "Trebuie să stai tura și n-ai stat";
			}
		}
		if (tureDeStat.get(i) > 0)
			tureDeStat.set(i, tureDeStat.get(i) - 1);

		//dacă nu este o carte specială trebuie să se potrivească peste celelalte cărți.

		//trebuie verificat dacă chiar are cărțile pe care vrea să le pună
		if (!contineCartile(mutare.cartiDePus, cartiJucatori.get(i)))
			return "Nu ai cărțile pe care vrei să le pui jos!";
		
		mutari.add(mutare);
		return "legal";
	}

	/** Amestecă vectorul care conține teancul de cărți. */
	protected void amestecareTeanc()
	{
		int marime = teanc.size();
		for (int i=0; i<marime; i++)
		{
			Carte scoasa = teanc.remove(i);
			teanc.add(randint(0, marime-1), scoasa);
		}
	}

	protected void trimitereCarti(int indexJucator, int cate)
	{
		Vector<Carte> trimite = new Vector<Carte>();
		//ia câte cărți poți să iei din teanc
		for (int i = 0; i < cate && teanc.size() > 0; i++)
			trimite.add(teanc.remove(0));

		//dacă mai trebuiesc,
		if (trimite.size() < cate)
		{
			refacereTeanc();
			//se extrag până când le-am luat pe toate cele necesare, dar să mai rămână una
			int ori = cate - trimite.size();
			if (ori > teanc.size() - 1) ori = teanc.size() - 1;
			for (int i = 0; i < ori; i++)
				trimite.add(teanc.remove(0));
		}
		jucatoriPrezenti.get(indexJucator).primireCarti(Carte.vectorToArray(trimite));
		for (int i = 0; i < trimite.size(); i++)
			cartiJucatori.get(indexJucator).add(trimite.get(i));

	}

	protected void refacereTeanc()
	{
		int ori = cartiPuse.size() - 1;
		for (int i=0; i<ori; i++)
			teanc.add(cartiPuse.remove(0));
		if (tipJoc.amestecareDupaTerminareTeanc)
			amestecareTeanc();
	}

	protected Vector<Jucator> ordineIesire()
	{
		return jucatoriIesiti;
	}

	protected boolean contineCartile(Carte[] care, Vector<Carte> inCare)
	{
		if (care == null) return true;
		boolean are;
		for (int i = 0; i < Array.getLength(care); i++)
		{
			are = false;
			for (int j = 0; j < inCare.size(); j++)
			{
				if (care[i].equals(inCare.get(j)))
				{
					are = true;
					break;
				}
			}
			if (!are) return false;
		}
		return true;
	}

	protected int numarDeLuat()
	{
		int total = 0;
		for (int i = mutari.size() - 1; i >= 0; i--)
		{
			int acum = 0;
			if (mutari.get(i).cartiDePus != null)
				for (int j = 0; j < Array.getLength(mutari.get(i).cartiDePus); j++)
				{
					int c = mutari.get(i).cartiDePus[j].getNumar();
					if (c == 2) acum += 2;
					else if (c == 3) acum += 3;
					else if (c == 0) acum += tipJoc.pedeapsaJocherNegru;
					else if (c == 1) acum += tipJoc.pedeapsaJocherRosu;
				}
			total += acum;
			if (acum == 0 && !mutari.get(i).staTura) break; //dacă nu mai sunt cărți de luat și nu s-a stat tura, gata
		}
		return total;
	}
	protected int tureDeStat()
	{
		int total = 0;
		for (int i = mutari.size() - 1; i >= 0; i--)
		{
			int acum = 0;
			if (mutari.get(i).cartiDePus != null)
				for (int j = 0; j < Array.getLength(mutari.get(i).cartiDePus); j++)
					if (mutari.get(i).cartiDePus[j].getNumar() == tipJoc.carteaDeStatTura)
						acum++;
			total += acum;

			if (acum == 0)
				if (mutari.get(i).staTura && !estePrimaTura(i)) ; //pass
				else break;
		}
		return total;
	}
	protected void adaugaCaPrimaTura(int care)
	{
		marcatorPrimaTura.add(care);
	}
	protected boolean estePrimaTura(int care)
	{
		if (marcatorPrimaTura.size() == 0) return false;
		for (int i = marcatorPrimaTura.size() - 1; i >= 0; i--)
			if (marcatorPrimaTura.get(i) == care)
				return true;
		return false;
	}

	/** Întoarce un întreg aleator cuprins între parametrul <code>a</code> și <code>b</code> (inclusiv).*/
	public final static int randint(int a, int b)
	{
		return (int)Math.round(Math.random() * (b-a)) + a;
	}
}
