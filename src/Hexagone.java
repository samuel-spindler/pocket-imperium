import java.io.*;
import java.util.*;

// Classe pour représenter un hexagone
public class Hexagone implements Serializable {
    private static final long serialVersionUID = 1L;

    // Attributs de la classe :
    private final int q; // Coordonnée axiale q
    private final int r; // Coordonnée axiale r
    private final int s; // Coordonnée axiale s = -q - r
    private int level = 0; // Niveau de l'hexagone
    private Player occupant; // Joueur occupant l'hexagone
    private int nbShips = 0; // Nombre de vaisseaux sur l'hexagone
    private final int ID; // Identifiant de l'hexagone

    // Constructeur
    public Hexagone(int q, int r, int ID) {
        this.q = q; // Coordonnée axiale q
        this.r = r; // Coordonnée axiale r
        this.s = -q - r; // Coordonnée axiale s
        this.ID = ID; // Identifiant de l'hexagone
    }

    // getter des coordonnées q, r et s
    public int getQ() {
        return this.q;
    }

    public int getR() {
        return this.r;
    }

    public int getS() {
        return this.s;
    }

    // getter et setter du niveau de l'hexagone
    public int getNiveau() {
        return this.level;
    }

    public void setNiveau(int newLevel) {
        this.level = newLevel;
    }

    // getter et setter de l'occupant de l'hexagone
    public Player getOccupant() {
        return this.occupant;
    }

    public void setOccupant(Player newOccupant) {
        this.occupant = newOccupant;
    }

    // getter du nombre de vaisseaux sur l'hexagone
    public int getNbShips() {
        return this.nbShips;
    }

    public void setNbShips(int nbShips) {
        this.nbShips = nbShips;
    }

    // getter de l'identifiant de l'hexagone
    public int getID() {
        return this.ID;
    }

    // Méthode pour afficher un hexagone
    @Override
    public String toString() {
        return "Hexagone{" +
                "q=" + q +
                ", r=" + r +
                ", s=" + s +
                ", niveau=" + level +
                '}';
    }

    // Méthode pour ajouter des vaisseaux sur l'hexagone
    public void addShips(Player player, int nbShips) {
        try {
            if (this.occupant == null) {
                this.occupant = player;
            }
            if (this.occupant.equals(player)) {
                this.nbShips += nbShips;
                player.setNbShips(player.getNbShips() + nbShips); // Update player's ship count
            } else {
                throw new IllegalArgumentException("Hexagone occupé par un autre joueur.");
            }
        } catch (IllegalArgumentException e) {
            System.err.println("Erreur lors de l'ajout de vaisseaux: " + e.getMessage());
        }
    }

    // Méthode pour retirer des vaisseaux d'un hexagone appartenant à un joueur
    public void removeShips(int shipsToRemove, Player targetPlayer) {
        try {
            if (this.occupant != null && this.occupant.equals(targetPlayer)) {
                if (this.nbShips >= shipsToRemove) {
                    this.nbShips -= shipsToRemove;
                    targetPlayer.setNbShips(targetPlayer.getNbShips() - shipsToRemove); // Update player's ship count
                    if (this.nbShips == 0) {
                        this.occupant = null;
                    }
                } else {
                    throw new IllegalArgumentException("Nombre de vaisseaux insuffisant.");
                }
            } else {
                throw new IllegalArgumentException("Hexagone occupé par un autre joueur.");
            }
        } catch (IllegalArgumentException e) {
            System.err.println("Erreur lors du retrait de vaisseaux: " + e.getMessage());
        }
    }

    // Méthode pour obtenir un hexagone par ses coordonnées
    private Hexagone getHexagoneByCoordinates(List<Sector> sectors, int q, int r, int s) {
        for (Sector sector : sectors) {
            for (Hexagone hex : sector.getHexagones()) {
                if (hex.getQ() == q && hex.getR() == r && hex.getS() == s) {
                    return hex;
                }
            }
        }
        return null;
    }

    // Méthode pour obtenir les hexagones adjacents
    public List<Hexagone> getAdjacentHexes(List<Sector> sectors) {
        try {
            List<Hexagone> adjacentHexes = new ArrayList<>();
            int[][] directions = {
                    { 1, -1, 0 }, { -1, 1, 0 }, { 1, 0, -1 }, { -1, 0, 1 }, { 0, 1, -1 }, { 0, -1, 1 }
            };
            for (int[] dir : directions) {
                Hexagone hex = getHexagoneByCoordinates(sectors, this.q + dir[0], this.r + dir[1], this.s + dir[2]);
                if (hex != null) {
                    adjacentHexes.add(hex);
                }
            }
            return adjacentHexes;
        } catch (Exception e) {
            System.err.println("Erreur lors de l'obtention des hexagones adjacents: " + e.getMessage());
            return Collections.emptyList();
        }
    }

    // Génère les hexagones et les stocke dans une liste
    public static List<Hexagone> generateHexagones() {
        try {
            List<Hexagone> hexagones = new ArrayList<>();
            hexagones.add(new Hexagone(0, 0, 1));
            hexagones.add(new Hexagone(1, 0, 2));
            hexagones.add(new Hexagone(2, 0, 3));
            hexagones.add(new Hexagone(3, 0, 4));
            hexagones.add(new Hexagone(4, 0, 5));
            hexagones.add(new Hexagone(5, 0, 6));
            hexagones.add(new Hexagone(1, -1, 7));
            hexagones.add(new Hexagone(2, -1, 8));
            hexagones.add(new Hexagone(3, -1, 9));
            hexagones.add(new Hexagone(4, -1, 10));
            hexagones.add(new Hexagone(5, -1, 11));
            hexagones.add(new Hexagone(1, -2, 12));
            hexagones.add(new Hexagone(2, -2, 13));
            hexagones.add(new Hexagone(3, -2, 14));
            hexagones.add(new Hexagone(4, -2, 15));
            hexagones.add(new Hexagone(5, -2, 16));
            hexagones.add(new Hexagone(6, -2, 17));
            hexagones.add(new Hexagone(2, -3, 18));
            hexagones.add(new Hexagone(3, -3, 19));
            hexagones.add(new HexagoneTriPrime(4, -3, 20));// TriPrime
            hexagones.add(new Hexagone(5, -3, 21));
            hexagones.add(new Hexagone(6, -3, 22));
            hexagones.add(new Hexagone(2, -4, 23));
            hexagones.add(new Hexagone(3, -4, 24));
            hexagones.add(new HexagoneTriPrime(4, -4, 25));// TriPrime
            hexagones.add(new HexagoneTriPrime(5, -4, 26));// TriPrime
            hexagones.add(new Hexagone(6, -4, 27));
            hexagones.add(new Hexagone(7, -4, 28));
            hexagones.add(new Hexagone(3, -5, 29));
            hexagones.add(new Hexagone(4, -5, 30));
            hexagones.add(new HexagoneTriPrime(5, -5, 31));// TriPrime
            hexagones.add(new Hexagone(6, -5, 32));
            hexagones.add(new Hexagone(7, -5, 33));
            hexagones.add(new Hexagone(3, -6, 34));
            hexagones.add(new Hexagone(4, -6, 35));
            hexagones.add(new Hexagone(5, -6, 36));
            hexagones.add(new Hexagone(6, -6, 37));
            hexagones.add(new Hexagone(7, -6, 38));
            hexagones.add(new Hexagone(8, -6, 39));
            hexagones.add(new Hexagone(4, -7, 40));
            hexagones.add(new Hexagone(5, -7, 41));
            hexagones.add(new Hexagone(6, -7, 42));
            hexagones.add(new Hexagone(7, -7, 43));
            hexagones.add(new Hexagone(8, -7, 44));
            hexagones.add(new Hexagone(4, -8, 45));
            hexagones.add(new Hexagone(5, -8, 46));
            hexagones.add(new Hexagone(6, -8, 47));
            hexagones.add(new Hexagone(7, -8, 48));
            hexagones.add(new Hexagone(8, -8, 49));
            hexagones.add(new Hexagone(9, -8, 50));
            return hexagones;
        } catch (Exception e) {
            System.err.println("Erreur lors de la génération des hexagones: " + e.getMessage());
            return Collections.emptyList();
        }
    }


}
