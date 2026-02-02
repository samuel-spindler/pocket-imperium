// Classe HexagoneTriPrime pour représenter un hexagone de niveau 3 dans le TriPrime
public class HexagoneTriPrime extends Hexagone {

    // Attributs de la classe :
    private static int level = 3;
    private static Player occupant;
    private static int nbShips = 0;

    // Constructeur
    public HexagoneTriPrime(int q, int r, int ID) {
        super(q, r, ID);
    }

    // getter et setter du niveau de l'hexagone
    @Override
    public int getNiveau() {
        return level;
    }

    @Override
    public void setNiveau(int newLevel) {
        level = newLevel;
    }

    // getter et setter de l'occupant de l'hexagone
    @Override
    public Player getOccupant() {
        return occupant;
    }

    @Override
    public void setOccupant(Player newOccupant) {
        occupant = newOccupant;
    }

    // getter du nombre de vaisseaux sur l'hexagone
    @Override
    public int getNbShips() {
        return nbShips;
    }

    @Override
    public void setNbShips(int newNbShips) {
        nbShips = newNbShips;
    }
}
