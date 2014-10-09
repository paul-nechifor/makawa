package net.nechifor.makawa;

/*
 * Inceput: 22.08.2009


 ce se întâmplă când un jucator pune cartea de stat când este singurul în joc (celălalt sta tura). Sta și el tura... sau ce?


 */

public class Makawa
{
	public static void main(String[] args)
	{
		TipJoc tipJoc = new TipJoc();
		int n = 3;
		int[] scor = new int[n];
		Jucator[] jucatori = new Jucator[n];

		for (int i=0; i<n; i++) scor[i] = 0;

		for (int i=0; i<1; i++)
		{
			jucatori[0] = new JucatorUmanText("Tu");
			jucatori[1] = new JucatorSimplu("Paul");
			jucatori[2] = new JucatorSimplu("Nechifor");

			Joc joc = new Joc(tipJoc, jucatori);
			joc.incepeJocul();
			for (int j=0; j<n; j++)
				if (joc.ordineIesire().get(0) == jucatori[j])
					scor[j]++;
		}

		for (int i=0; i<n; i++)
			System.out.printf("%s a câștigat %d jocuri.\n", jucatori[i].getNume(), scor[i]);
	}
}
