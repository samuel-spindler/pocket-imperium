import java.util.*;

// Classe pour représenter la carte Expand
public class Expand extends ActionCard {

    // Constructeur de la classe Expand
    public Expand(EnumCard typeC, int forceC) {
        super(typeC, forceC);
    }

    // Méthode pour le set up de la carte Expand
    public void appliquerExpand(Player player, List<Sector> sectors) {
        @SuppressWarnings("resource")
        Scanner scanner = new Scanner(System.in);
        int shipsToAdd = 0;

        switch (getForceCarte()) {
            case 1 -> shipsToAdd = 3;
            case 2 -> shipsToAdd = 2;
            case 3 -> shipsToAdd = 1;
        }

        // Vérifier que le joueur a des vaisseaux sur des hexagones de niveau 1, 2 ou 3
        boolean hasValidHexagone = false;
        for (Sector sector : sectors) {
            for (Hexagone hex : sector.getHexagones()) {
                if (hex.getOccupant() != null && hex.getOccupant().equals(player) && hex.getNiveau() > 0) {
                    hasValidHexagone = true;
                    break;
                }
            }
            if (hasValidHexagone) {
                break;
            }
        }

        if (!hasValidHexagone) {
            System.out.println(
                    "Vous ne pouvez pas jouer la carte Expand car vous n'avez pas de vaisseaux sur des hexagones de niveau 1, 2 ou 3.");
            return;
        }

        String reponse;
        while (true) {
            System.out.println(player.getNom() + ", voulez-vous appliquer la carte Expand ? (Y/N)");
            reponse = scanner.nextLine();
            if (reponse.equalsIgnoreCase("Y") || reponse.equalsIgnoreCase("N")) {
                break;
            } else {
                System.out.println("Réponse invalide. Veuillez répondre par 'Y' ou 'N'.");
            }
        }

        if (reponse.equalsIgnoreCase("N")) {
            System.out.println("Vous avez choisi de ne pas appliquer la carte Expand.");
            return;
        }

        while (shipsToAdd > 0) {
            System.out.println("Vous avez " + shipsToAdd + " vaisseaux à ajouter.");
            System.out.println("Secteurs contrôlés :");
            List<Sector> secteursControles = new ArrayList<>();
            for (Sector sector : sectors) {
                for (Hexagone hex : sector.getHexagones()) {
                    if (hex.getOccupant() != null && hex.getOccupant().equals(player)) {
                        secteursControles.add(sector);
                        break;
                    }
                }
            }

            if (secteursControles.isEmpty()) {
                System.out.println("Vous ne contrôlez aucun secteur.");
                break;
            }

            for (Sector sector : secteursControles) {
                System.out.println("Secteur " + sector.getNumber() + " :");
                List<Hexagone> sortedHexagones = new ArrayList<>(sector.getHexagones());
                sortedHexagones.sort(Comparator.comparingInt(Hexagone::getID));
                for (Hexagone hex : sortedHexagones) {
                    if (hex.getOccupant() != null && hex.getOccupant().equals(player) && hex.getNiveau() > 0) {
                        System.out.println("  Hexagone ID: " + hex.getID() + ", Niveau: " + hex.getNiveau()
                                + ", Nombre de vaisseaux: " + hex.getNbShips());
                    }
                }
            }

            System.out.print("Entrez l'ID de l'hexagone où vous voulez ajouter un vaisseau: ");
            int hexId = scanner.nextInt();
            scanner.nextLine(); // Consommer la nouvelle ligne

            Hexagone hexagoneChoisi = null;
            for (Sector sector : secteursControles) {
                for (Hexagone hex : sector.getHexagones()) {
                    if (hex.getID() == hexId && hex.getOccupant() != null && hex.getOccupant().equals(player)
                            && hex.getNiveau() > 0) {
                        hexagoneChoisi = hex;
                        break;
                    }
                }
                if (hexagoneChoisi != null) {
                    break;
                }
            }

            if (hexagoneChoisi != null) {
                hexagoneChoisi.addShips(player, 1);
                shipsToAdd--;
            } else {
                System.out.println("Hexagone non valide. Veuillez réessayer.");
            }

            if (shipsToAdd > 0) {
                while (true) {
                    System.out.println("Voulez-vous ajouter un autre vaisseau ? (Y/N)");
                    reponse = scanner.nextLine();
                    if (reponse.equalsIgnoreCase("Y") || reponse.equalsIgnoreCase("N")) {
                        break;
                    } else {
                        System.out.println("Réponse invalide. Veuillez répondre par 'Y' ou 'N'.");
                    }
                }
                if (!reponse.equalsIgnoreCase("Y")) {
                    break;
                }
            }
        }
    }
}
