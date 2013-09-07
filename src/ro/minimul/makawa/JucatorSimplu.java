package ro.minimul.makawa;

import java.util.Vector;
import java.lang.reflect.Array;

/**
 * Acesta este cel mai simplu jucator de macaua artificial. 
 * @author		Paul Nechifor
 * @version		1.0
 */
public class JucatorSimplu extends Jucator
{
	protected TipJoc tipJoc;
	protected StareJoc stareJoc;

	protected Vector<Carte> carti;
	protected int maiStau = 0;

	public JucatorSimplu(String nume)
	{
		super(nume);
	}
	public void start(TipJoc tipJoc, Jucator[] jucatori, Carte[] carti, Carte primaCarte)
	{
		this.tipJoc = tipJoc;

		this.carti = Carte.arrayToVector(carti);

		stareJoc = new StareJoc(tipJoc, jucatori, primaCarte);
	}
	public void mutareEfectuata(Jucator jucator, Mutare mutare)
	{
		stareJoc.mutareaJucatorului(jucator, mutare);
	}
	public Mutare muta()
	{
		Mutare mutare = new Mutare();
		Vector<Carte> posibilitati;

		if (maiStau > 0)
		{
			mutare.staTura = true;
			maiStau--;
		}
		else
		{
			int deLuat = stareJoc.numarDeLuat();
			if (deLuat > 0)
			{
				posibilitati = daMaiDeparte(stareJoc.ultimaCarte());
				if (posibilitati.size() > 0) continuaCuPosibilitatile(posibilitati, mutare);
				else mutare.numarDeCarti = deLuat;
			}
			else
			{
				int deStat = stareJoc.tureDeStat();
				if (deStat > 0)
				{
					posibilitati = new Vector<Carte>();
					for (int i = 0; i < carti.size(); i++)
						if (carti.get(i).getNumar() == tipJoc.carteaDeStatTura)
							posibilitati.add(carti.get(i));
					if (posibilitati.size() > 0)
					{
						mutare.cartiDePus = new Carte[1];
						mutare.cartiDePus[0] = posibilitati.get(Joc.randint(0, posibilitati.size() - 1));
					}
					else
					{
						mutare.staTura = true;
						maiStau = deStat - 1;
					}
				}
				else
				{
					if (stareJoc.mutari.get(stareJoc.mutari.size() - 1).cartiDePus != null &&
							stareJoc.mutari.get(stareJoc.mutari.size() - 1).cartiDePus[0].getNumar() == tipJoc.carteaDeSchimbat)
						posibilitati = dePusPeste(stareJoc.mutari.get(stareJoc.mutari.size() - 1).tipDeSchimbat);
					else
						posibilitati = mutariPosibile(stareJoc.ultimaCarte());

					if (posibilitati.size() > 0) continuaCuPosibilitatile(posibilitati, mutare);
					else mutare.numarDeCarti = 1;
				}
			}

			if (mutare.cartiDePus != null)
			{
				//dac-am pus o carte de schimbat, trebuie să aleg în ce
				if (mutare.cartiDePus[0].getNumar() == tipJoc.carteaDeSchimbat)
					mutare.tipDeSchimbat = inCeSaSchimb();
				//scoate cărțile pe care le-am pus
				for (int i = 0; i < Array.getLength(mutare.cartiDePus); i++)
					Carte.scoate(carti, mutare.cartiDePus[i]);
			}
			//calculează dacă rămân cu o singură carte, ca să spun macaua
			if (carti.size() == 1)
				mutare.spuneMacaua = true;
			//oare să spun stop macaua
			if (tipJoc.cuStopMacaua && carti.size() == 0)
				mutare.spuneStopMacaua = true;
		}
		
		stareJoc.mutareaJucatorului(this, mutare);
		return mutare;
	}
	public void primireCarti(Carte[] carti)
	{
		for (int i = 0; i < Array.getLength(carti); i++)
			this.carti.add(carti[i]);
	}
	public void mutareIlegala(String motiv)
	{
		System.out.println("Oops: " + motiv);
	}

	/** Acest calcul presupune că dacă ultima carte este de luat, ia a fost luată de cel dinainte.
	 Deocamdată fac fără carți multiple. Unele mutări pot fi duplicate, trebuie să văd dacă Java are un tip de date mulțime.*/ /////////////////////////////////////
	protected Vector<Carte> mutariPosibile(Carte ultima)
	{
		Vector<Carte> multime = new Vector<Carte>();

		for (int i = 0; i < carti.size(); i++)
		{
			// dacă are același tip sau număr
			if (ultima.getNumar() == carti.get(i).getNumar() || ultima.getTip().equals(carti.get(i).getTip()))
				multime.add(carti.get(i));
			//dacă e frunză sau treflă pot să pun jocher negru
			if (carti.get(i).getNumar() == 0 && (ultima.getTip().equals("F") || ultima.getTip().equals("T")))
				multime.add(carti.get(i));
			//dacă e inimă sau romb pot să pun jocher roșu
			if (carti.get(i).getNumar() == 1 && (ultima.getTip().equals("I") || ultima.getTip().equals("R")))
				multime.add(carti.get(i));
			//dacă e jocher negru pot să pun frunză sau treflă
			if (ultima.getNumar() == 0 && (carti.get(i).getTip().equals("F") || carti.get(i).getTip().equals("T")))
				multime.add(carti.get(i));
			//dacă e jocher roșu pot să pun inimă sau romb
			if (ultima.getNumar() == 1 && (carti.get(i).getTip().equals("I") || carti.get(i).getTip().equals("R")))
				multime.add(carti.get(i));
		}
		////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		////////// trebuie eliminată cartea cu care se schimbă, în alt loc trebuie să pun cartea aia dacă n-am altceva
		return multime;
	}
	protected Vector<Carte> dePusPeste(String tip)
	{
		Vector<Carte> multime = new Vector<Carte>();

		if (tip == null)
			tip = inCeSaSchimb();

		for (int i = 0; i < carti.size(); i++)
			if (carti.get(i).getTip().equals(tip))
				multime.add(carti.get(i));
			else if (carti.get(i).getNumar() == 0 && (tip.equals("F") || tip.equals("T")))
				multime.add(carti.get(i));
			else if (carti.get(i).getNumar() == 1 && (tip.equals("I") || tip.equals("R")))
				multime.add(carti.get(i));
		return multime;
	}
	protected String inCeSaSchimb()
	{
		String[] tipuri = {"I", "R", "F", "T"};
		if (carti.size() == 0) //alege ceva la întâmplare pentru că nu contează
			return tipuri[Joc.randint(0, 3)];
		//aleg tipul unei cărți la întâmplare
		int k = Joc.randint(0, carti.size() - 1);
		if (carti.get(k).getNumar() == 0)
			return tipuri[Joc.randint(2, 3)];
		if (carti.get(k).getNumar() == 1)
			return tipuri[Joc.randint(2, 3)];
		return carti.get(k).getTip();
	}
	protected Vector<Carte> restulDeCarti(Carte c)
	{
		Vector<Carte> multime = new Vector<Carte>();

		for (int i = 0; i < carti.size(); i++)
		{
			if (carti.get(i).getNumar() == c.getNumar() && !carti.get(i).getTip().equals(c.getTip()))
				multime.add(carti.get(i));
			if (c.getNumar() + carti.get(i).getNumar() == 1)
				multime.add(carti.get(i));
		}
		return multime;
	}
	protected Vector<Carte> daMaiDeparte(Carte ultima)
	{
		Vector<Carte> multime = new Vector<Carte>();
		int           un      = ultima.getNumar();
		String        ut      = ultima.getTip();

		for (int i = 0; i < carti.size(); i++)
		{
			int    cn = carti.get(i).getNumar();
			String ct = carti.get(i).getTip();

			if (un == cn) multime.add(carti.get(i)); //dacă are același număr
			if ((cn + un == 5) && ut.equals(ct)) multime.add(carti.get(i)); //cu același tip, merge cealaltă carte
			if (un + cn == 1) multime.add(carti.get(i)); //dacă sunt jocheri
			if (cn == 0 && (ut.equals("F") || ut.equals("T"))) multime.add(carti.get(i)); //pun jocher negru
			if (cn == 1 && (ut.equals("I") || ut.equals("R"))) multime.add(carti.get(i)); //pun jocher roșu
		}
		return multime;
	}
	protected void continuaCuPosibilitatile(Vector<Carte> pos, Mutare m)
	{
		Carte c = pos.get(Joc.randint(0, pos.size() - 1));
		Vector<Carte> toate;
		if (tipJoc.cuCartiMultiple) toate = restulDeCarti(c);
		else toate = new Vector<Carte>();
		toate.add(0, c);
		m.cartiDePus = Carte.vectorToArray(toate);
	}
}
