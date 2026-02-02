import java.io.Serializable;
import java.util.*;

// Classe pour représenter un secteur
public class Sector implements Serializable { // Implémenter Serializable pour pouvoir sauvegarder les objets de cette
                                              // classe

    // Attributs de la classe :
    private static final long serialVersionUID = 1L; // Numéro de version pour la sérialisation

    // Attributs de la classe :
    private final List<Hexagone> hexagones; // Liste des hexagones du secteur
    private final int number; // Numéro du secteur

    // Constructeur
    public Sector(int number) {
        this.hexagones = new ArrayList<>();
        this.number = number;
    }

    // Méthode pour récupérer le numéro du secteur
    public int getNumber() {
        return this.number;
    }

    // Méthode pour récupérer la liste des hexagones du secteur
    public List<Hexagone> getHexagones() {
        return this.hexagones;
    }

    // Retourne une représentation textuelle pour l'affichage du secteur
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Sector: \n");
        for (Hexagone hex : hexagones) {
            sb.append("  Hexagone ").append(hex).append(" (Niveau: ").append(hex.getNiveau()).append(")\n");
        }
        return sb.toString();
    }

    // Méthode pour ajouter un hexagone à un secteur
    public void addHexagone(Hexagone hexagone) {
        try {
            this.hexagones.add(hexagone);
        } catch (Exception e) {
            System.err.println("Erreur lors de l'ajout de l'hexagone: " + e.getMessage());
        }
    }

    // Assigne les niveaux aux hexagones dans un secteur de manière aléatoire (1
    // niveau 2 et 2 niveaux 1)
    public void assignLevels(Random rand) {
        try {
            int niveau2Index = rand.nextInt(this.hexagones.size());
            this.hexagones.get(niveau2Index).setNiveau(2);

            int niveau1Index1;
            do {
                niveau1Index1 = rand.nextInt(this.hexagones.size());
            } while (niveau1Index1 == niveau2Index);
            this.hexagones.get(niveau1Index1).setNiveau(1);

            int niveau1Index2;
            do {
                niveau1Index2 = rand.nextInt(this.hexagones.size());
            } while (niveau1Index2 == niveau2Index || niveau1Index2 == niveau1Index1);
            this.hexagones.get(niveau1Index2).setNiveau(1);
        } catch (Exception e) {
            System.err.println("Erreur lors de l'assignation des niveaux: " + e.getMessage());
        }
    }

    // Assigne le niveau 3 à tous les hexagones dans un secteur (pour le TriPrime)
    public void assignLevel3() {
        try {
            for (Hexagone hexagone : this.hexagones) {
                hexagone.setNiveau(3);
            }
        } catch (Exception e) {
            System.err.println("Erreur lors de l'assignation du niveau 3: " + e.getMessage());
        }
    }

    // Méthode pour vérifier si un secteur est occupé par un joueur
    public boolean isOccupiedBy(Player player) {
        try {
            for (Hexagone hex : this.hexagones) {
                if (hex.getOccupant() != null && hex.getOccupant().equals(player)) {
                    return true;
                }
            }
        } catch (Exception e) {
            System.err.println("Erreur lors de la vérification de l'occupation: " + e.getMessage());
        }
        return false;
    }

    // Crée les secteurs en utilisant les hexagones générés dans la classe Hexagone
    public static Sector[][] CreateSectors(List<Hexagone> hexagones) {
        try {
            Sector[][] sectors = new Sector[3][3];
            Random rand = new Random();

            // Création des secteurs
            sectors[0][0] = new Sector(1);
            sectors[0][0].addHexagone(hexagones.get(0));
            sectors[0][0].addHexagone(hexagones.get(1));
            sectors[0][0].addHexagone(hexagones.get(6));
            sectors[0][0].addHexagone(hexagones.get(11));
            sectors[0][0].addHexagone(hexagones.get(12));
            sectors[0][0].assignLevels(rand);
            sectors[0][0].addHexagone(hexagones.get(7)); // Ajouter l'hexagone index 7 id 8

            sectors[0][1] = new Sector(2);
            sectors[0][1].addHexagone(hexagones.get(2));
            sectors[0][1].addHexagone(hexagones.get(3));
            sectors[0][1].addHexagone(hexagones.get(8));
            sectors[0][1].addHexagone(hexagones.get(13));
            sectors[0][1].addHexagone(hexagones.get(14));
            sectors[0][1].assignLevels(rand);
            sectors[0][1].addHexagone(hexagones.get(7)); // Ajouter l'hexagone index 7 id 8
            sectors[0][1].addHexagone(hexagones.get(9)); // Ajouter l'hexagone index 9 id 10

            sectors[0][2] = new Sector(3);
            sectors[0][2].addHexagone(hexagones.get(4));
            sectors[0][2].addHexagone(hexagones.get(5));
            sectors[0][2].addHexagone(hexagones.get(10));
            sectors[0][2].addHexagone(hexagones.get(15));
            sectors[0][2].addHexagone(hexagones.get(16));
            sectors[0][2].assignLevels(rand);
            sectors[0][2].addHexagone(hexagones.get(9)); // Ajouter l'hexagone index 9 id 10

            sectors[1][0] = new Sector(4);
            sectors[1][0].addHexagone(hexagones.get(17));
            sectors[1][0].addHexagone(hexagones.get(22));
            sectors[1][0].addHexagone(hexagones.get(23));
            sectors[1][0].addHexagone(hexagones.get(28));
            sectors[1][0].assignLevels(rand);
            sectors[1][0].addHexagone(hexagones.get(18)); // Ajouter l'hexagone index 18 id 19
            sectors[1][0].addHexagone(hexagones.get(29)); // Ajouter l'hexagone index 29 id 30

            sectors[1][1] = new Sector(5);
            sectors[1][1].addHexagone(hexagones.get(19));
            sectors[1][1].addHexagone(hexagones.get(24));
            sectors[1][1].addHexagone(hexagones.get(25));
            sectors[1][1].addHexagone(hexagones.get(30));
            sectors[1][1].assignLevel3();

            sectors[1][2] = new Sector(6);
            sectors[1][2].addHexagone(hexagones.get(21));
            sectors[1][2].addHexagone(hexagones.get(26));
            sectors[1][2].addHexagone(hexagones.get(27));
            sectors[1][2].addHexagone(hexagones.get(32));
            sectors[1][2].assignLevels(rand);
            sectors[1][2].addHexagone(hexagones.get(20)); // Ajouter l'hexagone index 20 id 21
            sectors[1][2].addHexagone(hexagones.get(31)); // Ajouter l'hexagone index 31 id 32

            sectors[2][0] = new Sector(7);
            sectors[2][0].addHexagone(hexagones.get(33));
            sectors[2][0].addHexagone(hexagones.get(34));
            sectors[2][0].addHexagone(hexagones.get(39));
            sectors[2][0].addHexagone(hexagones.get(44));
            sectors[2][0].addHexagone(hexagones.get(45));
            sectors[2][0].assignLevels(rand);
            sectors[2][0].addHexagone(hexagones.get(40)); // Ajouter l'hexagone index 40 id 41

            sectors[2][1] = new Sector(8);
            sectors[2][1].addHexagone(hexagones.get(35));
            sectors[2][1].addHexagone(hexagones.get(36));
            sectors[2][1].addHexagone(hexagones.get(41));
            sectors[2][1].addHexagone(hexagones.get(46));
            sectors[2][1].addHexagone(hexagones.get(47));
            sectors[2][1].assignLevels(rand);
            sectors[2][1].addHexagone(hexagones.get(40)); // Ajouter l'hexagone index 40 id 41
            sectors[2][1].addHexagone(hexagones.get(42)); // Ajouter l'hexagone index 42 id 43

            sectors[2][2] = new Sector(9);
            sectors[2][2].addHexagone(hexagones.get(37));
            sectors[2][2].addHexagone(hexagones.get(38));
            sectors[2][2].addHexagone(hexagones.get(43));
            sectors[2][2].addHexagone(hexagones.get(48));
            sectors[2][2].addHexagone(hexagones.get(49));
            sectors[2][2].assignLevels(rand);
            sectors[2][2].addHexagone(hexagones.get(42)); // Ajouter l'hexagone index 42 id 43

            return sectors;
        } catch (Exception e) {
            System.err.println("Erreur lors de la création des secteurs: " + e.getMessage());
            return null;
        }
    }

}
