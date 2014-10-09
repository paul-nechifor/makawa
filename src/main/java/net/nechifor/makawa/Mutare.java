package net.nechifor.makawa;

import java.lang.reflect.Array;

/**
 * Această clasă descrie o mișcare pe care poate s-o facă un jucător. Numele vine de la șah. Verificarea
 * legalității mutării în sine face în clasă. Nu există mutarea „stau“. <code>Joc</code>-ul nu trebuie să
 * întrebe de mutare.
 * @author		Paul Nechifor
 * @version		1.0
 */
public class Mutare
{
	/** Vectorul de cărți pe care vrea să le pună jos, dacă-i <code>null</code>, înseamnă că nu pune. */
	public Carte[] cartiDePus = null;

	/** Tipul cărții în care vrea să schimbe, dacă pune cartea de schimbat. */
	public String tipDeSchimbat = null;

	/** Numărul de cărți pe care-l cere din teanc, dacă-i zero, înseamnă că nu cere. */
	public int numarDeCarti = 0;

	/** Este <code>true</code> dacă jucatorul trebuie să stea tura asta. */
	public boolean staTura = false;

	/** Este <code>true</code> dacă jucatorul trebuie să spună „macaua“ după ce a terminat mutarea asta. */
	public boolean spuneMacaua = false;

	/** Este <code>true</code> dacă jucatorul trebuie să spună „stop macaua“ după ce a terminat mutarea asta. */
	public boolean spuneStopMacaua = false;

	/**
	 * Verifică dacă mutarea asta este posibilă în sine, independent de contextul în care s-a făcut.
	 * @param tipJoc		tipul jocului
	 * @return				întoarce <code>"legal"</code> dacă mutarea este legală, altfel întoarce motivul pentru care
	 *                      nu este legală
	 */
	public String verificareLegalitate(TipJoc tipJoc)
	{
		if (cartiDePus == null && tipDeSchimbat == null && numarDeCarti == 0 && !staTura)
			return "Nu ai spus ce mutare vrei să faci!";

		if (numarDeCarti < 0) return "Numărul cărților de luat nu poate fi negativ!";

		if (cartiDePus != null)
		{
			int len = Array.getLength(cartiDePus);
			if (numarDeCarti != 0) return "Nu ai voie să iei cărți și să pui în același timp!";
			if (staTura) return "Nu ai voie să stai o tură și să pui cărți în același timp!";
			if (tipDeSchimbat == null && cartiDePus[0].getNumar() == tipJoc.carteaDeSchimbat)
				return "Nu ai voie să pui carte de schimbat și să nu alegi în ce să schimbi!";
			if (!tipJoc.cuCartiMultiple && len > 1)
				return "Nu se pot pune mai multe cărți (cuCartiMultiple == false)!";
			if (len >= 2)
			{
				boolean suntDoiJocheri = (len == 2) && (cartiDePus[0].getNumar() + cartiDePus[1].getNumar() == 1);
				int prima = cartiDePus[0].getNumar();
				for (int i = 1; i < len; i++)
					if (cartiDePus[i].getNumar() != prima && !suntDoiJocheri)
						return "Carțile din grup nu se potrivesc!";
			}
		}
		if (tipDeSchimbat != null)
		{
			String posibile = "IRFT";
			if (numarDeCarti != 0) return "Nu ai voie să schimbi și să iei cărți în același timp!";
			if (staTura) return "Nu ai voie să stai o tură și să schimbi în același timp!";
			if (cartiDePus == null) return "Nu ai voie să schimbi fără să nu pui carte!";
			if (cartiDePus[0].getNumar() != tipJoc.carteaDeSchimbat)
				return "Ai ales să schimbi dar nu ai pus cartea de schimbat!";
			if (tipDeSchimbat.length() != 1 || posibile.indexOf(tipDeSchimbat) < 0)
				return "Tipul încare se schimbă trebuie să fie un caracter dintre „IRFT“!";
		}
		if (numarDeCarti >= 1)
		{
			if (staTura) return "Nu poți să stai o tură și să iei cărți în același timp!";
		}
		return "legal";
	}
	public void arata() // pentru depanare ////////////////////////////////////////
	{
		System.out.println("spuneMacaua = " + spuneMacaua);
		System.out.println("spuneStopMacaua = " + spuneStopMacaua);
		System.out.println("tipDeSchimbat = " + tipDeSchimbat);
		System.out.println("numarDeCarti = " + numarDeCarti);
		System.out.println("staTura = " + staTura);
		System.out.println("cartiDePus = " + Carte.arataCarti(cartiDePus));
	}
	
	public String notatie()
	{
		String str = "";
		if (staTura) str += "stau; ";
		if (numarDeCarti > 0) str += "iau " + numarDeCarti + "; ";
		if (cartiDePus != null) str += "pun " + Carte.arataCarti(cartiDePus) + "; ";
		if (tipDeSchimbat != null) str += "schimb " + tipDeSchimbat + "; ";
		if (spuneMacaua) str += "macaua; ";
		if (spuneStopMacaua) str += "stop macaua; ";
		return str.substring(0, str.length() - 2);
	}
}
