import java.io.Serializable;
import java.util.*;

// Classe pour représenter un tour de jeu
public class Round implements Serializable { // Implémenter Serializable pour pouvoir sauvegarder les objets de cette
                                             // classe
    private static final long serialVersionUID = 1L; // Numéro de version pour la sérialisation

    // Attributs de la classe :
    private final int turnNumber; // Numéro du tour
    private Player starter; // Joueur qui commence le tour
    private Player secondPlayer; // Deuxième joueur
    private Player thirdPlayer; // Troisième joueur

    // Constructeur de la classe :
    public Round(int turnNumber, Player starter, Player secondPlayer, Player thirdPlayer) {
        this.turnNumber = turnNumber;
        this.starter = starter;
        this.secondPlayer = secondPlayer;
        this.thirdPlayer = thirdPlayer;
    }

    // Méthode pour afficher un tour
    @Override
    public String toString() {
        return "Tour " + this.turnNumber + " : " + starter.getNom() + " commence";
    }

    // Méthode pour que les joueurs choisissent l'ordre des cartes
    public void phasePlan() {
        try {
            List<Player> players = Arrays.asList(starter, secondPlayer, thirdPlayer);
            @SuppressWarnings("resource")
            Scanner scanner = new Scanner(System.in);
            for (Player joueur : players) {
                Map<String, Integer> ordre;
                if (joueur instanceof BotPlayer botPlayer) {
                    ordre = botPlayer.choisirOrdreCartes();
                } else {
                    if (joueur != null) {
                        ordre = new HashMap<>();
                        System.out.println(
                                "Veuillez entrer l'ordre des cartes (explore, expand, exterminate) pour le joueur "
                                        + joueur.getNom() + ":");
                        for (String carte : new String[] { "explore", "expand", "exterminate" }) {
                            System.out.print("Position de " + carte + ": ");
                            if (scanner.hasNextInt()) {
                                ordre.put(carte, scanner.nextInt());
                            } else {
                                throw new NoSuchElementException("No valid input for " + carte + " position");
                            }
                        }
                        scanner.nextLine(); // Consommer la nouvelle ligne
                    } else {
                        throw new NullPointerException("Le joueur est null");
                    }
                }
                joueur.setOrdreCartes(ordre); // Définir l'ordre des cartes pour chaque joueur
                System.out.println("Ordre des cartes pour " + joueur.getNom() + ": " + ordre);
            }
        } catch (NullPointerException | NoSuchElementException e) {
            System.err.println("Erreur lors de la saisie de l'ordre des cartes: " + e.getMessage());
        }
    }

    // Méthode pour exploiter les cartes
    public void phasePerform() {
        try {
            List<Player> players = Arrays.asList(starter, secondPlayer, thirdPlayer);
            String[] ordreCartes = { "expand", "explore", "exterminate" };

            for (int position = 1; position <= 3; position++) {
                Map<Player, String> cartesJoueurs = new HashMap<>();

                // Dévoiler les cartes des joueurs pour la position actuelle
                for (Player joueur : players) {
                    Map<String, Integer> ordreCartesJoueur = joueur.getOrdreCartes();
                    for (Map.Entry<String, Integer> entry : ordreCartesJoueur.entrySet()) {
                        if (entry.getValue() == position) {
                            cartesJoueurs.put(joueur, entry.getKey());
                            System.out.println(
                                    "La carte " + position + " de " + joueur.getNom() + " est " + entry.getKey());
                        }
                    }
                }

                // Calculer la force des cartes
                ActionCard action = new ActionCard(ActionCard.getTypeCarte(position),
                        ActionCard.getForceCarte(position));
                Map<Player, Integer> forces = action.calculerForces(cartesJoueurs);

                // Jouer les cartes dans l'ordre expand, explore, exterminate
                for (String carte : ordreCartes) {
                    for (Player joueur : players) {
                        if (cartesJoueurs.get(joueur) != null && cartesJoueurs.get(joueur).equals(carte)) {
                            int force = forces.get(joueur);
                            System.out.println("Le joueur " + joueur.getNom() + " joue la carte " + carte
                                    + " avec une force de " + force);
                            switch (carte) {
                                case "expand" -> {
                                    Expand expandAction = new Expand(ActionCard.getTypeCarte(position), force);
                                    if (joueur instanceof BotPlayer botPlayer) {
                                        botPlayer.appliquerExpand(expandAction,
                                                convertSectorsToList(Game.getSectors()));
                                        System.out
                                                .println("Le bot " + joueur.getNom() + " a appliqué la carte Expand.");
                                    } else {
                                        expandAction.appliquerExpand(joueur, convertSectorsToList(Game.getSectors()));
                                    }
                                }

                                case "explore" -> {
                                    Explore exploreAction = new Explore(ActionCard.getTypeCarte(position), force);
                                    if (joueur instanceof BotPlayer botPlayer) {
                                        botPlayer.appliquerExplore(exploreAction,
                                                convertSectorsToList(Game.getSectors()));
                                        System.out
                                                .println("Le bot " + joueur.getNom() + " a appliqué la carte Explore.");
                                    } else {
                                        exploreAction.appliquerExplore(joueur, convertSectorsToList(Game.getSectors()));
                                    }
                                }
                                case "exterminate" -> {
                                    Exterminate exterminateAction = new Exterminate(ActionCard.getTypeCarte(position),
                                            force);
                                    if (joueur instanceof BotPlayer botPlayer) {
                                        botPlayer.appliquerExterminate(exterminateAction,
                                                convertSectorsToList(Game.getSectors()));
                                        System.out.println(
                                                "Le bot " + joueur.getNom() + " a appliqué la carte Exterminate.");
                                    } else {
                                        exterminateAction.appliquerExterminate(joueur,
                                                convertSectorsToList(Game.getSectors()));
                                    }
                                }
                                default -> action.jouer(forces, carte);
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("Erreur lors de la phase de performance: " + e.getMessage());
        }
    }

    // Méthode pour convertir les secteurs en liste
    public static List<Sector> convertSectorsToList(Sector[][] sectors) {
        List<Sector> sectorList = new ArrayList<>();
        for (Sector[] sectorRow : sectors) {
            sectorList.addAll(Arrays.asList(sectorRow));
        }
        return sectorList;
    }

    // Phase d'exploitation pour retirer les vaisseaux en excès et marquer des
    // points
    public void phaseExploit(Player starter) {
        try {
            List<Player> players = Arrays.asList(starter, secondPlayer, thirdPlayer);
            // Retirer les vaisseaux en excès
            for (Sector[] sectorRow : Game.getSectors()) {
                for (Sector sector : sectorRow) {
                    for (Hexagone hex : sector.getHexagones()) {
                        int maxShips = 1 + hex.getNiveau();
                        if (hex.getNbShips() > maxShips) {
                            int shipsToRemove = hex.getNbShips() - maxShips;
                            hex.removeShips(shipsToRemove, hex.getOccupant());
                            System.out
                                    .println("Retiré " + shipsToRemove + " vaisseaux de l'hexagone ID: " + hex.getID());
                        }
                    }
                }
            }

            // Marquer des points
            Set<Integer> chosenSectors = new HashSet<>();
            List<Player> playersInOrder = new ArrayList<>(players);
            playersInOrder.remove(starter);
            playersInOrder.add(0, starter);

            for (Player player : playersInOrder) {
                if (player instanceof BotPlayer botPlayer) {
                    botPlayer.phaseExploit(convertSectorsToList(Game.getSectors()));
                } else {
                    if (player == starter) {
                        // Starter chooses first
                        chooseSectorForScoring(player, chosenSectors);
                    } else {
                        // Other players choose in order
                        chooseSectorForScoring(player, chosenSectors);
                    }
                }
            }

            // Vérifier si le joueur contrôle Tri-Prime
            for (Player player : players) {
                if (controlsTriPrime(player)) {
                    chooseSectorForScoring(player, chosenSectors);
                    break;
                }
            }

            // Calculer les points pour chaque joueur
            for (Player player : players) {
                int points = calculatePointsForPlayer(player, chosenSectors);
                player.setNbPoints(points);
                System.out.println(player.getNom() + " a marqué " + points + " points.");
            }

            // Change le starter
            passStarterRole();
        } catch (Exception e) {
            System.err.println("Erreur lors de la phase d'exploitation: " + e.getMessage());
        }
    }

    private void chooseSectorForScoring(Player player, Set<Integer> chosenSectors) {
        @SuppressWarnings("resource")
        Scanner scanner = new Scanner(System.in);
        try {

            boolean validChoice = false;
            while (!validChoice) {
                System.out.println(player.getNom() + ", choisissez un secteur pour marquer des points.");
                displayOccupiedSectors(chosenSectors);

                System.out.print("Entrez le numéro du secteur: ");
                int sectorNumero = scanner.nextInt();
                scanner.nextLine(); // Consommer la nouvelle ligne

                if (sectorNumero == 5) {
                    System.out.println("Vous ne pouvez pas choisir le secteur central de Tri-Prime.");
                } else if (chosenSectors.contains(sectorNumero)) {
                    System.out.println("Ce secteur a déjà été choisi.");
                } else if (!isSectorOccupied(sectorNumero)) {
                    System.out.println("Vous ne pouvez pas choisir un secteur inoccupé.");
                } else {
                    chosenSectors.add(sectorNumero);
                    validChoice = true;
                }
            }

        } catch (Exception e) {
            System.err.println("Erreur lors du choix du secteur pour marquer des points: " + e.getMessage());
        }
    }

    private void displayOccupiedSectors(Set<Integer> chosenSectors) {
        try {
            for (Sector[] sectorRow : Game.getSectors()) {
                for (Sector sector : sectorRow) {
                    if (!chosenSectors.contains(sector.getNumber()) && isSectorOccupied(sector.getNumber())) {
                        System.out.println("Secteur numéro " + sector.getNumber());
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("Erreur lors de l'affichage des secteurs occupés: " + e.getMessage());
        }
    }

    private boolean isSectorOccupied(int sectorNumero) {
        try {
            for (Sector[] sectorRow : Game.getSectors()) {
                for (Sector sector : sectorRow) {
                    if (sector.getNumber() == sectorNumero) {
                        for (Hexagone hex : sector.getHexagones()) {
                            if (hex.getOccupant() != null) {
                                return true;
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("Erreur lors de la vérification de l'occupation du secteur: " + e.getMessage());
        }
        return false;
    }

    private boolean controlsTriPrime(Player player) {
        try {
            for (Sector[] sectorRow : Game.getSectors()) {
                for (Sector sector : sectorRow) {
                    if (sector.getNumber() == 5) {
                        for (Hexagone hex : sector.getHexagones()) {
                            if (hex instanceof HexagoneTriPrime && hex.getOccupant() == player) {
                                return true;
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("Erreur lors de la vérification du contrôle de Tri-Prime: " + e.getMessage());
        }
        return false;
    }

    private int calculatePointsForPlayer(Player player, Set<Integer> chosenSectors) {
        try {
            int points = 0;
            for (Sector[] sectorRow : Game.getSectors()) {
                for (Sector sector : sectorRow) {
                    if (chosenSectors.contains(sector.getNumber())) {
                        for (Hexagone hex : sector.getHexagones()) {
                            if (hex.getOccupant() == player) {
                                points += hex.getNiveau();
                            }
                        }
                    }
                }
            }
            return points;
        } catch (Exception e) {
            System.err.println("Erreur lors du calcul des points pour le joueur: " + e.getMessage());
        }
        return 0;
    }

    // Méthode pour passer le rôle de starter au joueur suivant
    public void passStarterRole() {
        try {
            Player temp = starter;
            starter = secondPlayer;
            secondPlayer = thirdPlayer;
            thirdPlayer = temp;
            System.out.println("Le rôle de starter passe à " + starter.getNom());
        } catch (Exception e) {
            System.err.println("Erreur lors du passage du rôle de starter: " + e.getMessage());
        }
    }

    // Getter for starter
    public Player getStarter() {
        return starter;
    }

    // Setter for starter
    public void setStarter(Player starter) {
        this.starter = starter;
    }
}
