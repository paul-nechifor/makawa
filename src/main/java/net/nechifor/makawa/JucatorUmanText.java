package net.nechifor.makawa;

import java.util.Vector;
import java.lang.reflect.Array;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;

/**
 * Acest jucator este doar o intervață text pentru a citi mutarile unui jocător uman.
 * @author		Paul Nechifor
 * @version		1.0
 */
public class JucatorUmanText extends Jucator
{
	private Jucator[] jucatori;
	private Vector<Carte> carti;
	private Vector<Carte> cartiPuse = new Vector<Carte>();
	private TipJoc tipJoc;
	private Mutare ultimaMutare = null;

	public JucatorUmanText(String nume)
	{
		super(nume);
		jucatorUman = true;
	}
	public void start(TipJoc tipJoc, Jucator[] jucatori, Carte[] carti, Carte primaCarte)
	{
		euSpun("Am început jocul.");
		this.tipJoc = tipJoc;
		this.jucatori = jucatori;
		this.carti = new Vector<Carte>();
		cartiPuse.add(primaCarte);
		for (int i = 0; i < Array.getLength(carti); i++)
			this.carti.add(carti[i]);
	}
	public void mutareEfectuata(Jucator jucator, Mutare mutare)
	{
		if (mutare.cartiDePus != null)
			for (int i = 0; i < Array.getLength(mutare.cartiDePus); i++)
				cartiPuse.add(mutare.cartiDePus[i]);

		euSpun("Jucatorul " + jucator.getNume() + " a făcut o mutare.");
		if (mutare.cartiDePus != null)
		{
			euSpun("    A pus " + mutare.cartiDePus[0].simbol());
			for (int i = 1; i < Array.getLength(mutare.cartiDePus); i++)
				System.out.print(", " + mutare.cartiDePus[i].simbol());
			System.out.println();
		}
		if (mutare.tipDeSchimbat != null)
		{
			String tipuri = "IRFT";
			int tipul = tipuri.indexOf(mutare.tipDeSchimbat);
			euSpun("    Schimbă în " + tipJoc.denumiriTipuri[tipul] + ".");
		}
		if (mutare.numarDeCarti > 0) euSpun("    A luat " + mutare.numarDeCarti + " cărți.");
		if (mutare.spuneMacaua) euSpun("    A spus „macaua“.");
		if (mutare.spuneStopMacaua) euSpun("    A spus „stop macaua“.");
	}
	public Mutare muta()
	{
		if (ultimaMutare != null)
		{
			if (ultimaMutare.cartiDePus != null)
				for (int i = 0; i < Array.getLength(ultimaMutare.cartiDePus); i++)
					Carte.scoate(carti, ultimaMutare.cartiDePus[i]);
		}
		Mutare mutare = new Mutare();
		euSpun("S-au pus: " + Carte.arataCarti(cartiPuse));
		euSpun("Cărțile mele sunt: " + Carte.arataCarti(carti));
		euSpun("Eu fac mutarea: ");
		try
		{
			BufferedReader stdin = new BufferedReader(new InputStreamReader(System.in));
			String[] s = stdin.readLine().toUpperCase().replace(" ", "").split(";");
			for (int i = 0; i < Array.getLength(s); i++)
				if (s[i].equals("MACAUA")) mutare.spuneMacaua = true;
				else if (s[i].equals("STOP MACAUA")) mutare.spuneStopMacaua = true;
				else if (s[i].equals("STAU")) mutare.staTura = true;
				else if (s[i].startsWith("IAU")) mutare.numarDeCarti = (int) Integer.parseInt(s[i].substring(3));
				else if (s[i].startsWith("SCHIMB")) mutare.tipDeSchimbat = s[i].substring(6);
				else if (s[i].startsWith("PUN"))
				{
					String[] cod = s[i].substring(3).split(",");
					Carte[] c = new Carte[Array.getLength(cod)];
					for (int j = 0; j < Array.getLength(cod); j++)
						c[j] = new Carte(cod[j]);
					mutare.cartiDePus = c;
				}
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		ultimaMutare = mutare;

		//////////////////////////////////////////////////////////////////////////////////////////////////
		// nu ar trebui să adaug cărțile aici pentru că mutarea s-ar putea să fie ilegală. Sau aș putea să reversez mutarea în mutareIlegala()
		if (mutare.cartiDePus != null)
			for (int i = 0; i < Array.getLength(mutare.cartiDePus); i++)
				cartiPuse.add(mutare.cartiDePus[i]);

		return mutare;
	}
	public void primireCarti(Carte[] carti)
	{
		euSpun("Am primit cărțile: " + Carte.arataCarti(carti));
		for (int i = 0; i < Array.getLength(carti); i++)
			this.carti.add(carti[i]);
	}
	public void mutareIlegala(String motiv)
	{
		euSpun("Am făcut o mutare ilegală: " + motiv);
	}
	private void euSpun(String ceSpun)
	{
		System.out.println(nume + ">>> " + ceSpun);
	}
}
