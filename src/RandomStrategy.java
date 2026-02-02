import java.io.Serializable;
import java.util.*;

// Implémentation de la stratégie aléatoire
public class RandomStrategy implements Strategy, Serializable {

    // Attribut de la classe :
    private static final long serialVersionUID = 1L; // Numéro de version pour la sérialisation

    // Méthode pour jouer
    @Override
    public void jouer() {
        // Implémentation de la stratégie aléatoire
        System.out.println("Jouer de manière aléatoire");
    }

    // Méthode pour définir l'ordre des cartes
    @Override
    public Map<String, Integer> definirOrdreCartes() {
        List<Integer> positions = new ArrayList<>(List.of(1, 2, 3));
        Collections.shuffle(positions); // Mélange les positions de manière aléatoire

        Map<String, Integer> ordre = new HashMap<>();
        ordre.put("explore", positions.get(0));
        ordre.put("expand", positions.get(1));
        ordre.put("exterminate", positions.get(2));

        return ordre;
    }

    // Méthode pour appliquer la carte Expand
    @Override
    public void appliquerExpand(Player player, Expand expandCard, List<Sector> sectors) {
        Random random = new Random();
        int shipsToAdd = 0;

        switch (expandCard.getForceCarte()) {
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
            System.out.println("Le bot " + player.getNom()
                    + " ne peut pas jouer la carte Expand car il n'a pas de vaisseaux sur des hexagones de niveau 1, 2 ou 3.");
            return;
        }

        while (shipsToAdd > 0) {
            List<Hexagone> hexagonesPossibles = new ArrayList<>();
            for (Sector sector : sectors) {
                for (Hexagone hex : sector.getHexagones()) {
                    if (hex.getOccupant() != null && hex.getOccupant().equals(player) && hex.getNiveau() > 0) {
                        hexagonesPossibles.add(hex);
                    }
                }
            }

            if (hexagonesPossibles.isEmpty()) {
                System.out.println("Le bot " + player.getNom() + " ne contrôle aucun secteur.");
                break;
            }

            Hexagone hexagoneChoisi = hexagonesPossibles.get(random.nextInt(hexagonesPossibles.size()));
            hexagoneChoisi.addShips(player, 1);
            System.out.println(
                    "Le bot " + player.getNom() + " a ajouté un vaisseau à l'hexagone ID: " + hexagoneChoisi.getID());
            shipsToAdd--;
        }
    }

    // Méthode pour appliquer la carte Explore
    @Override
    public void appliquerExplore(Player player, Explore exploreCard, List<Sector> sectors) {
        Random random = new Random();
        int movesToMake = 0;

        switch (exploreCard.getForceCarte()) {
            case 1 -> movesToMake = 3;
            case 2 -> movesToMake = 2;
            case 3 -> movesToMake = 1;
        }

        while (movesToMake > 0) {
            List<Hexagone> hexagonesPossibles = new ArrayList<>();
            for (Sector sector : sectors) {
                for (Hexagone hex : sector.getHexagones()) {
                    if (hex.getOccupant() != null && hex.getOccupant().equals(player) && hex.getNbShips() > 0) {
                        hexagonesPossibles.add(hex);
                    }
                }
            }

            if (hexagonesPossibles.isEmpty()) {
                System.out.println("Le bot " + player.getNom()
                        + " ne contrôle aucun hexagone ou toutes les flottes ont déjà été utilisées.");
                break;
            }

            Hexagone startHex = hexagonesPossibles.get(random.nextInt(hexagonesPossibles.size()));
            List<Hexagone> adjacentHexagones = startHex.getAdjacentHexes(sectors);

            if (adjacentHexagones.isEmpty()) {
                System.out.println("Le bot " + player.getNom()
                        + " ne peut pas explorer car il n'y a pas d'hexagones adjacents valides.");
                break;
            }

            Hexagone targetHex = adjacentHexagones.get(random.nextInt(adjacentHexagones.size()));
            int nbShips = random.nextInt(startHex.getNbShips()) + 1;

            startHex.removeShips(nbShips, player);
            targetHex.addShips(player, nbShips);
            System.out.println("Le bot " + player.getNom() + " a déplacé " + nbShips + " vaisseaux de l'hexagone ID: "
                    + startHex.getID() + " à l'hexagone ID: " + targetHex.getID());
            movesToMake--;
        }
    }

    // Méthode pour appliquer la carte Exterminate
    @Override
    public void appliquerExterminate(Player player, Exterminate exterminateCard, List<Sector> sectors) {
        Random random = new Random();
        int attacksToMake = 0;

        switch (exterminateCard.getForceCarte()) {
            case 1 -> attacksToMake = 3;
            case 2 -> attacksToMake = 2;
            case 3 -> attacksToMake = 1;
        }

        while (attacksToMake > 0) {
            List<Hexagone> hexagonesPossibles = new ArrayList<>();
            for (Sector sector : sectors) {
                for (Hexagone hex : sector.getHexagones()) {
                    if (hex.getOccupant() != null && hex.getOccupant().equals(player) && hex.getNbShips() > 0) {
                        hexagonesPossibles.add(hex);
                    }
                }
            }

            if (hexagonesPossibles.isEmpty()) {
                System.out.println("Le bot " + player.getNom()
                        + " ne contrôle aucun hexagone ou toutes les flottes ont déjà été utilisées.");
                break;
            }

            Hexagone startHex = hexagonesPossibles.get(random.nextInt(hexagonesPossibles.size()));
            List<Hexagone> adjacentHexagones = startHex.getAdjacentHexes(sectors);

            if (adjacentHexagones.isEmpty()) {
                System.out.println("Le bot " + player.getNom()
                        + " ne peut pas attaquer car il n'y a pas d'hexagones adjacents valides.");
                break;
            }

            Hexagone targetHex = adjacentHexagones.get(random.nextInt(adjacentHexagones.size()));
            int nbShips = random.nextInt(startHex.getNbShips()) + 1;

            startHex.removeShips(nbShips, player);
            targetHex.addShips(player, nbShips);
            System.out.println("Le bot " + player.getNom() + " a déplacé " + nbShips + " vaisseaux de l'hexagone ID: "
                    + startHex.getID() + " à l'hexagone ID: " + targetHex.getID());
            attacksToMake--;
        }
    }

    // Méthode pour déployer des vaisseaux aléatoirement
    @Override
    public void deployShipsRandomly(Player player, List<Sector> sectors) {
        Random random = new Random();
        List<Hexagone> hexagonesPossibles = new ArrayList<>();
        for (Sector sector : sectors) {
            for (Hexagone hex : sector.getHexagones()) {
                if (hex.getNiveau() == 1 && hex.getOccupant() == null) {
                    hexagonesPossibles.add(hex);
                }
            }
        }

        if (hexagonesPossibles.isEmpty()) {
            System.out.println("Le bot " + player.getNom()
                    + " ne trouve aucun hexagone de niveau 1 inoccupé pour déployer des vaisseaux.");
            return;
        }

        Hexagone hexagoneChoisi = hexagonesPossibles.get(random.nextInt(hexagonesPossibles.size()));
        hexagoneChoisi.addShips(player, 2);
        System.out.println(
                "Le bot " + player.getNom() + " a déployé 2 vaisseaux sur l'hexagone ID: " + hexagoneChoisi.getID());
    }

    @Override
    public void phaseExploit(Player player, List<Sector> sectors) {
        // Retirer les vaisseaux en excès
        for (Sector[] sectorRow : Game.getSectors()) {
            for (Sector sector : sectorRow) {
                for (Hexagone hex : sector.getHexagones()) {
                    int maxShips = 1 + hex.getNiveau();
                    if (hex.getNbShips() > maxShips) {
                        int shipsToRemove = hex.getNbShips() - maxShips;
                        hex.removeShips(shipsToRemove, hex.getOccupant());
                        System.out.println("Retiré " + shipsToRemove + " vaisseaux de l'hexagone ID: " + hex.getID());
                    }
                }
            }
        }

        // Choisir le secteur avec le plus de vaisseaux
        Sector chosenSector = null;
        int maxShips = 0;
        for (Sector sector : sectors) {
            int sectorShips = 0;
            for (Hexagone hex : sector.getHexagones()) {
                if (hex.getOccupant() == player) {
                    sectorShips += hex.getNbShips();
                }
            }
            if (sectorShips > maxShips) {
                chosenSector = sector;
                maxShips = sectorShips;
            }
        }

        if (chosenSector != null) {
            int points = 0;
            for (Hexagone hex : chosenSector.getHexagones()) {
                if (hex.getOccupant() == player) {
                    points += hex.getNiveau();
                }
            }
            System.out.println("Le " + player.getNom() + " a choisi le secteur " + chosenSector.getNumber()
                    + " pour marquer des points.");
            player.setNbPoints(player.getNbPoints() + points);
        }

        // Vérifier si le joueur contrôle Tri-Prime
        boolean controlsTriPrime = false;
        for (Sector sector : sectors) {
            for (Hexagone hex : sector.getHexagones()) {
                if (hex instanceof HexagoneTriPrime && hex.getOccupant() == player) {
                    controlsTriPrime = true;
                    break;
                }
            }
            if (controlsTriPrime) {
                break;
            }
        }

        // Si le joueur contrôle Tri-Prime, choisir un autre secteur
        if (controlsTriPrime) {
            Sector secondChosenSector = null;
            maxShips = 0;
            for (Sector sector : sectors) {
                if (sector == chosenSector)
                    continue;
                int sectorShips = 0;
                for (Hexagone hex : sector.getHexagones()) {
                    if (hex.getOccupant() == player) {
                        sectorShips += hex.getNbShips();
                    }
                }
                if (sectorShips > maxShips) {
                    secondChosenSector = sector;
                    maxShips = sectorShips;
                }
            }

            if (secondChosenSector != null) {
                int points = 0;
                for (Hexagone hex : secondChosenSector.getHexagones()) {
                    if (hex.getOccupant() == player) {
                        points += hex.getNiveau();
                    }
                }
                System.out.println("Le " + player.getNom() + " a choisi le secteur " + secondChosenSector.getNumber()
                        + " pour marquer des points grâce à Tri-Prime.");
                player.setNbPoints(player.getNbPoints() + points);
            }
        }
    }
}