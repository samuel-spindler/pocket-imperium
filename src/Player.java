import java.io.Serializable;
import java.util.*;

// Classe pour représenter un joueur
public class Player implements Serializable { // Implémenter Serializable pour pouvoir sauvegarder les objets de cette
                                              // classe
    private static final long serialVersionUID = 1L; // Numéro de version pour la sérialisation

    // Attributs
    private final String Nom; // Nom du joueur
    private final EnumColor Couleur; // Couleur du joueur
    private Map<String, Integer> ordreCartes; // Ordre des cartes choisis par le joueur
    private int nbShips; // Nombre de vaisseaux du joueur
    private int nbPoints; // Nombre de points du joueur

    // Constructeur de la classe :
    public Player(String nom, EnumColor couleur) {
        this.Nom = nom;
        this.Couleur = couleur;
        this.ordreCartes = new HashMap<>();
        this.nbShips = 0; // Initialement 0 vaisseaux
        this.nbPoints = 0; // Initialement 0 points
    }

    // Méthode pour récupérer les points du joueur
    public int getNbPoints() {
        return this.nbPoints;
    }

    // Méthode pour modifier les points du joueur
    public void setNbPoints(int nbPoints) {
        this.nbPoints = nbPoints;
    }

    // Méthode pour récupérer le nom du joueur
    public String getNom() {
        return this.Nom;
    }

    // Méthode pour récupérer la couleur du joueur
    public EnumColor getCouleur() {
        return this.Couleur;
    }

    // Méthode pour récupérer l'ordre des cartes choisis par le joueur
    public Map<String, Integer> getOrdreCartes() {
        return this.ordreCartes;
    }

    // Méthode pour modifer l'ordre des cartes choisis par le joueur
    public void setOrdreCartes(Map<String, Integer> ordreCartes) {
        this.ordreCartes = ordreCartes;
    }

    // Méthode pour afficher un joueur
    @Override
    public String toString() {
        return "[Nom=" + this.Nom + ", Couleur=" + this.Couleur + "]";
    }

    // Méthode pour récupérer le nombre de vaisseaux du joueur
    public int getNbShips() {
        int totalShips = 0;
        for (Sector[] sectorRow : Game.getSectors()) {
            for (Sector sector : sectorRow) {
                for (Hexagone hex : sector.getHexagones()) {
                    if (hex.getOccupant() != null && hex.getOccupant().equals(this)) {
                        totalShips += hex.getNbShips();
                    }
                }
            }
        }
        return totalShips;
    }

    // Méthode pour modifier le nombre de vaisseaux du joueur
    public void setNbShips(int nbShips) {
        this.nbShips = nbShips;
    }

    // Méthode pour enlever des vaisseaux au joueur
    public void removeShips(int numberOfShips) {
        if (this.nbShips >= numberOfShips) {
            this.nbShips -= numberOfShips;
        } else {
            System.out.println("Pas assez de vaisseaux disponible.");
        }
    }

    // Méthode pour définir l'ordre des cartes
    public Map<String, Integer> definirOrdreCartes(List<String> ordreCartes) {
        Map<String, Integer> ordre = new HashMap<>();
        for (int i = 0; i < ordreCartes.size(); i++) {
            ordre.put(ordreCartes.get(i), i + 1);
        }
        setOrdreCartes(ordre);
        return ordre;
    }

    // Méthode pour permettre au joueur de décider de l'ordre des cartes
    public Map<String, Integer> choisirOrdreCartes() {
        try (Scanner scanner = new Scanner(System.in)) {
            String[] cartes = { "explore", "expand", "exterminate" };
            Set<Integer> positions = new HashSet<>();

            System.out.println(
                    "Veuillez entrer l'ordre des cartes (explore, expand, exterminate) pour le joueur " + this.Nom + ":");
            for (String carte : cartes) {
                int position;
                while (true) {
                    System.out.print("Position de " + carte + ": ");
                    position = scanner.nextInt();
                    if (position < 1 || position > 3) {
                        System.out.println("Position invalide. Veuillez entrer 1, 2 ou 3.");
                    } else if (!positions.add(position)) {
                        System.out.println("Position déjà utilisée. Veuillez entrer une position unique.");
                    } else {
                        break;
                    }
                }
                this.ordreCartes.put(carte, position);
            }
        }
        return ordreCartes;
    }

    // Méthode pour déployer des vaisseaux
    public void deployShips(Hexagone hex, int numberOfShips) {
        hex.addShips(this, numberOfShips);
    }

}
