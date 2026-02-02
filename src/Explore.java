import java.util.*;

// Classe pour représenter une carte Explore
public class Explore extends ActionCard {

    // Constructeur de la classe Explore
    public Explore(EnumCard typeC, int forceC) {
        super(typeC, forceC);
    }

    // Méthode pour le set up de la carte Explore
    public void appliquerExplore(Player player, List<Sector> sectors) {
        @SuppressWarnings("resource")
        Scanner scanner = new Scanner(System.in);
        int movesToMake = 0;

        switch (getForceCarte()) {
            case 1 -> movesToMake = 3;
            case 2 -> movesToMake = 2;
            case 3 -> movesToMake = 1;
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
                    "Vous ne pouvez pas jouer la carte Explore car vous n'avez pas de vaisseaux sur des hexagones de niveau 1, 2 ou 3.");
            return;
        }

        String reponse;
        while (true) {
            System.out.println(player.getNom() + ", voulez-vous appliquer la carte Explore ? (Y/N)");
            reponse = scanner.nextLine();
            if (reponse.equalsIgnoreCase("Y") || reponse.equalsIgnoreCase("N")) {
                break;
            } else {
                System.out.println("Réponse invalide. Veuillez répondre par 'Y' ou 'N'.");
            }
        }

        if (reponse.equalsIgnoreCase("N")) {
            System.out.println("Vous avez choisi de ne pas appliquer la carte Explore.");
            return;
        }

        Map<Hexagone, Integer> movedHexagones = new HashMap<>();

        while (movesToMake > 0) {
            System.out
                    .println(player.getNom() + ", vous avez " + movesToMake + " déplacements de flotte restants.");
            System.out.println("Hexagones contrôlés :");
            List<Hexagone> hexagonesControles = new ArrayList<>();
            for (Sector sector : sectors) {
                for (Hexagone hex : sector.getHexagones()) {
                    if (hex.getOccupant() != null && hex.getOccupant().equals(player) && hex.getNbShips() > 0) {
                        hexagonesControles.add(hex);
                        System.out.println("ID: " + hex.getID() + " - Niveau: " + hex.getNiveau()
                                + ", Nombre de vaisseaux: " + hex.getNbShips());
                    }
                }
            }

            if (hexagonesControles.isEmpty()) {
                System.out
                        .println("Vous ne contrôlez aucun hexagone ou toutes les flottes ont déjà été utilisées.");
                break;
            }

            System.out.print("Entrez l'ID de l'hexagone de départ: ");
            int startHexId = scanner.nextInt();
            scanner.nextLine(); // Consommer la nouvelle ligne

            Hexagone startHex = null;
            for (Hexagone hex : hexagonesControles) {
                if (hex.getID() == startHexId) {
                    startHex = hex;
                    break;
                }
            }

            if (startHex == null) {
                System.out.println("Hexagone de départ non valide ou déjà utilisé. Veuillez réessayer.");
                continue;
            }

            // Afficher les hexagones adjacents que le joueur peut envahir
            List<Hexagone> adjacentHexagones = startHex.getAdjacentHexes(sectors);
            List<Hexagone> validTargets = new ArrayList<>();
            for (Hexagone hex : adjacentHexagones) {
                if ((hex.getOccupant() == null || !hex.getOccupant().equals(player))
                        && !(hex instanceof HexagoneTriPrime)) {
                    validTargets.add(hex);
                }
            }

            // Ajouter les hexagones séparés par un hexagone intermédiaire
            for (Hexagone middleHex : adjacentHexagones) {
                List<Hexagone> secondAdjacentHexagones = middleHex.getAdjacentHexes(sectors);
                for (Hexagone hex : secondAdjacentHexagones) {
                    if ((hex.getOccupant() == null || !hex.getOccupant().equals(player))
                            && !validTargets.contains(hex) && !hex.equals(startHex)
                            && !(hex instanceof HexagoneTriPrime)) {
                        validTargets.add(hex);
                    }
                }
            }

            if (validTargets.isEmpty()) {
                System.out.println("Aucun hexagone adjacent valide à envahir depuis cet hexagone de départ.");
                continue;
            }

            System.out.println("Hexagones adjacents que vous pouvez envahir :");
            for (Hexagone hex : validTargets) {
                System.out.println("ID: " + hex.getID() + " - Niveau: " + hex.getNiveau() + ", Occupant: "
                        + (hex.getOccupant() != null ? hex.getOccupant().getNom() : "Aucun"));
            }

            System.out.print("Entrez l'ID de l'hexagone de destination: ");
            int endHexId = scanner.nextInt();
            scanner.nextLine(); // Consommer la nouvelle ligne

            Hexagone endHex = null;
            for (Hexagone hex : validTargets) {
                if (hex.getID() == endHexId) {
                    endHex = hex;
                    break;
                }
            }

            if (endHex == null) {
                System.out.println("Hexagone de destination non valide. Veuillez réessayer.");
                continue;
            }

            // Déplacer les vaisseaux
            System.out.print("Entrez le nombre de vaisseaux à déplacer: ");
            int nbShips = scanner.nextInt();
            scanner.nextLine(); // Consommer la nouvelle ligne

            if (nbShips > startHex.getNbShips() - movedHexagones.getOrDefault(startHex, 0)) {
                System.out.println("Nombre de vaisseaux insuffisant ou déjà déplacés. Veuillez réessayer.");
                continue;
            }

            try {
                startHex.removeShips(nbShips, player); // Retirer les vaisseaux de l'hexagone de départ
                endHex.addShips(player, nbShips); // Ajouter les vaisseaux à l'hexagone de destination
            } catch (IllegalArgumentException e) {
                System.out.println(e.getMessage());
                continue;
            }

            // Mettre à jour le nombre de vaisseaux déplacés depuis l'hexagone de départ
            movedHexagones.put(startHex, movedHexagones.getOrDefault(startHex, 0) + nbShips);
            movedHexagones.put(endHex, movedHexagones.getOrDefault(endHex, 0) + nbShips);

            movesToMake--;

            // Vérifier si tous les vaisseaux ont été déplacés
            boolean allShipsMoved = true;
            for (Hexagone hex : hexagonesControles) {
                if (hex.getNbShips() > movedHexagones.getOrDefault(hex, 0)) {
                    allShipsMoved = false;
                    break;
                }
            }

            if (allShipsMoved) {
                System.out.println("Tous vos vaisseaux ont été déplacés.");
                break;
            }

            if (movesToMake > 0) {
                while (true) {
                    System.out.println("Voulez-vous faire un autre déplacement ? (Y/N)");
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