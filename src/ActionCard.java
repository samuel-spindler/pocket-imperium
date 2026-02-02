import java.util.*;

// Classe pour représenter une carte d'action
public class ActionCard {

    // Attributs de la classe
    public int forceCarte; // Force de la carte
    public EnumCard typeCarte; // Type de carte

    // Constructeur
    public ActionCard(EnumCard typeCarte, int forceCarte) {
        this.forceCarte = forceCarte;
        this.typeCarte = typeCarte;
    }

    // getter de la force de la carte
    public int getForceCarte() {
        return this.forceCarte;
    }

    // getter du type de carte
    public EnumCard getTypeCarte() {
        return typeCarte;
    }

    // Méthode pour obtenir la force de la carte
    public static int getForceCarte(int i) {
        return i;
    }

    // Méthode pour calculer la force des cartes
    public Map<Player, Integer> calculerForces(Map<Player, String> cartesJoueurs) {
        Map<String, Integer> carteCounts = new HashMap<>();
        Map<Player, Integer> forces = new HashMap<>();

        // Compter combien de joueurs ont choisi chaque carte
        for (String carte : cartesJoueurs.values()) {
            carteCounts.put(carte, carteCounts.getOrDefault(carte, 0) + 1);
        }

        // Attribuer la force correspondante à chaque joueur
        for (Map.Entry<Player, String> entry : cartesJoueurs.entrySet()) {
            String carte = entry.getValue();
            int count = carteCounts.get(carte);
            forces.put(entry.getKey(), count);
        }

        return forces;
    }

    // Méthode pour jouer les cartes dans un ordre spécifique
    public void jouer(Map<Player, Integer> forces, String ordre) {
        // Logique pour jouer la carte
        System.out.println("");
    }

    // Méthode pour obtenir le type de carte
    public static EnumCard getTypeCarte(int i) {
        switch (i) {
            case 1 -> {
                return EnumCard.EXPAND;
            }
            case 2 -> {
                return EnumCard.EXPLORE;
            }
            case 3 -> {
                return EnumCard.EXTERMINATE;
            }
            default -> throw new IllegalArgumentException("Type de carte inconnu pour l'index: " + i);
        }
    }

}
