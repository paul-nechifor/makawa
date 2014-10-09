package net.nechifor.makawa;

/*
Inceput: 22.08.2009

TODO: ce se întâmplă când un jucator pune cartea de stat când este singurul în
joc (celălalt sta tura). Sta și el tura... sau ce?
*/

public class Makawa {
    public static void main(String[] args) {
        TipJoc gameType = new TipJoc();
        Jucator[] players = createPlayers(args);
        Joc game = new Joc(gameType, players);
        game.incepeJocul();
    }

    private static Jucator[] createPlayers(String[] args) {
        if (args.length % 2 != 0) {
            throw new AssertionError("Invalid args.");
        }

        Jucator[] players = new Jucator[args.length / 2];

        if (players.length < 2) {
            throw new AssertionError("Too few players.");
        }

        for (int i = 0; i < players.length; i++) {
            players[i] = createPlayer(args[2 * i], args[2 * i + 1]);
        }

        return players;
    }

    private static Jucator createPlayer(String type, String name) {
        if ("Human".equals(type)) {
            return new JucatorUmanText(name);
        } else if ("Simple".equals(type)) {
            return new JucatorSimplu(name);
        }
        throw new AssertionError("No such player type: " + type);
    }
}
