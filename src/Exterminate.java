import java.util.*;

public class Exterminate extends ActionCard {

    // Constructeur de la classe Exterminate
    public Exterminate(EnumCard typeC, int forceC) {
        super(typeC, forceC);
    }

    // Méthode pour le set up de la carte Exterminate
    public void appliquerExterminate(Player player, List<Sector> sectors) {
        @SuppressWarnings("resource")
        Scanner scanner = new Scanner(System.in);
        int systemToInvade = 0;

        switch (getForceCarte()) {
            case 1 -> systemToInvade = 3;
            case 2 -> systemToInvade = 2;
            case 3 -> systemToInvade = 1;
        }

        // Vérifier que le joueur a des vaisseaux sur des hexagones
        boolean hasValidHexagone = false;
        for (Sector sector : sectors) {
            for (Hexagone hex : sector.getHexagones()) {
                if (hex.getOccupant() != null && hex.getOccupant().equals(player)) {
                    hasValidHexagone = true;
                    break;
                }
            }
            if (hasValidHexagone)
                break;
        }

        if (!hasValidHexagone) {
            System.out.println("Vous n'avez pas de vaisseaux pour réaliser une invasion.");
            return;
        }

        // Afficher les hexagones adjacents que le joueur peut envahir
        List<Hexagone> potentialTargets = new ArrayList<>();
        for (Sector sector : sectors) {
            for (Hexagone hex : sector.getHexagones()) {
                if ((hex.getOccupant() == null || !hex.getOccupant().equals(player)) && hex.getNiveau() > 0) {
                    List<Hexagone> adjacentHexes = getAdjacentHexes(sectors, hex);
                    for (Hexagone adjHex : adjacentHexes) {
                        if (adjHex.getOccupant() != null && adjHex.getOccupant().equals(player)) {
                            potentialTargets.add(hex);
                            break;
                        }
                    }
                }
            }
        }

        if (potentialTargets.isEmpty()) {
            System.out.println("Aucun hexagone adjacent à envahir.");
            return;
        }

        System.out.println("Hexagones que vous pouvez envahir:");
        for (Hexagone hex : potentialTargets) {
            System.out.println("ID: " + hex.getID() + ", Coordonnées: (" + hex.getQ() + ", " + hex.getR() + ", "
                    + hex.getS() + ")");
        }

        // Sélectionner les hexagones à envahir
        Set<Hexagone> usedHexagones = new HashSet<>();
        while (systemToInvade > 0) {
            System.out.println("Entrer l'identifiant de l'hexagone à envahir (ou -1 pour terminer la sélection):");
            int targetHexId = scanner.nextInt();
            if (targetHexId == -1)
                break;

            Hexagone targetHex = getHexagoneById(sectors, targetHexId);
            if (targetHex == null || targetHex.getOccupant() == player) {
                System.out.println("Hexagone cible invalide.");
                continue;
            }

            // Sélectionner les vaisseaux à déplacer
            List<Hexagone> adjacentHexes = getAdjacentHexes(sectors, targetHex);
            int totalSystemInvade = 0;
            for (Hexagone hex : adjacentHexes) {
                if (hex.getOccupant() == player && hex.getNbShips() > 0 && !usedHexagones.contains(hex)) {
                    System.out.println(
                            "Hexagone ID: " + hex.getID() + ", Nombre de vaisseaux disponibles: "
                                    + hex.getNbShips());
                    System.out.println("Combien de vaisseaux voulez-vous déplacer de cet hexagone?");
                    int shipsToMoveFromHex = scanner.nextInt();
                    shipsToMoveFromHex = Math.min(shipsToMoveFromHex, hex.getNbShips());
                    hex.removeShips(shipsToMoveFromHex, player);
                    totalSystemInvade += shipsToMoveFromHex;
                    usedHexagones.add(hex);
                    if (totalSystemInvade >= systemToInvade)
                        break;
                }
            }

            if (totalSystemInvade == 0) {
                System.out.println("Aucun vaisseau déplacé.");
                continue;
            }

            // Réaliser l'invasion
            Player targetPlayer = targetHex.getOccupant();
            int targetShips = targetHex.getNbShips();
            int invadingShips = totalSystemInvade;

            int shipsToRemove = Math.min(targetShips, invadingShips);
            targetHex.removeShips(shipsToRemove, targetPlayer);
            if (targetPlayer != null) {
                targetPlayer.removeShips(shipsToRemove);
            }

            if (invadingShips > targetShips) {
                targetHex.setOccupant(player);
                targetHex.addShips(player, invadingShips - shipsToRemove);
                System.out.println(
                        "Invasion réussie! " + player.getNom() + " a conquis l'hexagone " + targetHex.getID()
                                + ".");
            } else if (targetShips > invadingShips) {
                targetHex.setOccupant(targetPlayer);
                targetHex.addShips(targetPlayer, targetShips - shipsToRemove);
                if (targetPlayer != null) {
                    System.out.println("Invasion échouée! " + targetPlayer.getNom() + " a défendu l'hexagone "
                            + targetHex.getID() + ".");
                } else {
                    System.out.println("Invasion échouée! L'hexagone " + targetHex.getID() + " a été défendu.");
                }
            } else {
                targetHex.setOccupant(null);
                System.out.println("Invasion terminée! Les deux flottes se sont détruites mutuellement. L'hexagone "
                        + targetHex.getID() + " est maintenant inoccupé.");
            }

            systemToInvade--;
        }
    }

    // Méthode pour obtenir les hexagones adjacents
    private List<Hexagone> getAdjacentHexes(List<Sector> sectors, Hexagone targetHex) {
        List<Hexagone> adjacentHexes = new ArrayList<>();
        int[][] directions = {
                { 1, -1, 0 }, { -1, 1, 0 }, { 1, 0, -1 }, { -1, 0, 1 }, { 0, 1, -1 }, { 0, -1, 1 }
        };
        for (int[] dir : directions) {
            Hexagone hex = getHexagoneByCoordinates(sectors, targetHex.getQ() + dir[0], targetHex.getR() + dir[1],
                    targetHex.getS() + dir[2]);
            if (hex != null) {
                adjacentHexes.add(hex);
            }
        }
        return adjacentHexes;
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

    // Méthode pour obtenir un hexagone par son identifiant
    private Hexagone getHexagoneById(List<Sector> sectors, int id) {
        for (Sector sector : sectors) {
            for (Hexagone hex : sector.getHexagones()) {
                if (hex.getID() == id) {
                    return hex;
                }
            }
        }
        return null;
    }

}
