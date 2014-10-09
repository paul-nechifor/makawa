package net.nechifor.makawa;

import java.util.Vector;
import java.lang.reflect.Array;

public class StareJoc
{
	protected TipJoc tipJoc;
	public Vector<Carte> cartiPuse = new Vector<Carte>();
	public Vector<Mutare> mutari = new Vector<Mutare>();
	public Vector<Boolean> estePrimaTura = new Vector<Boolean>();
	public Vector<Jucator> jucatori = new Vector<Jucator>();
	public Vector<Integer> numarCartiJucatori = new Vector<Integer>();
	public Vector<Integer> tureDeStat = new Vector<Integer>(); 

	public StareJoc(TipJoc tipJoc, Jucator[] jucatori, Carte primaCarte)
	{
		this.tipJoc = tipJoc;

		for (int i = 0; i < Array.getLength(jucatori); i++)
		{
			this.jucatori.add(jucatori[i]);
			numarCartiJucatori.add(5);
			tureDeStat.add(0);
		}

		cartiPuse.add(primaCarte);

		//prima carte trebuie adaugată ca o mutare, dacă este cartea de schimbat, tipul de schimbat va fi null
		Mutare m = new Mutare();
		m.cartiDePus = new Carte[1];
		m.cartiDePus[0] = primaCarte;
		
		mutari.add(m);
		estePrimaTura.add(false);
	}

	public void mutareaJucatorului(Jucator jucator, Mutare mutare)
	{
		int j = indiceJucator(jucator);

		int deStat = tureDeStat();
		if (mutare.staTura && tureDeStat.get(j) == 0)
		{
			estePrimaTura.add(true);
			tureDeStat.set(j, deStat);
		}
		else estePrimaTura.add(false);

		mutari.add(mutare);

		if (tureDeStat.get(j) > 0)
			tureDeStat.set(j, tureDeStat.get(j) - 1);

		if (mutare.cartiDePus != null)
		{
			numarCartiJucatori.set(j, numarCartiJucatori.get(j) - Array.getLength(mutare.cartiDePus));
			for (int i = 0; i < Array.getLength(mutare.cartiDePus); i++)
				cartiPuse.add(mutare.cartiDePus[i]);
		}

		if (mutare.numarDeCarti > 0)
			numarCartiJucatori.set(j, numarCartiJucatori.get(j) + mutare.numarDeCarti);
		
		//dacă a ieșit din joc
		if (numarCartiJucatori.get(j) < 1)
		{
			jucatori.remove(j);
			numarCartiJucatori.remove(j);
			tureDeStat.remove(j);
		}
	}

	public Carte ultimaCarte()
	{
		return cartiPuse.get(cartiPuse.size() - 1);
	}

	protected int indiceJucator(Jucator jucator)
	{
		for (int i = 0; i < jucatori.size(); i++)
			if (jucatori.get(i) == jucator)
				return i;
		return -1; // oops
	}

	/**
	 * Se uită în vectorul de mutări și calculează câte cărți trebuiesc luate.
	 * @return				numărul de cărți pe care trebuie să-l ia dacă nu poate să dea mai departe sau să stopeze
	 */
	public int numarDeLuat()
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
	public int tureDeStat()
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
				if (mutari.get(i).staTura && !estePrimaTura.get(i)) ; //pass
				else break;
		}
		return total;
	}
}
