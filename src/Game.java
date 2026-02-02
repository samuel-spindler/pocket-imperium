import java.io.*;
import java.util.*;

// Classe Game pour représenter une partie du jeu
public class Game implements Serializable { // Implémente Serializable pour permettre la sauvegarde et le chargement

    private static final long serialVersionUID = 1L; // Numéro de version pour la sérialisation

    // Attributs
    private static Game instance; // Instance unique de la classe qui fait d'elle un singleton
    ArrayList<Player> players; // Liste des Players
    private final ArrayList<Round> rounds; // Liste des Rounds
    public List<Hexagone> hexagones; // Liste des Hexagones
    private static Sector[][] sectors; // Liste des Sectors
    private int currentRoundNumber; // Numéro du round actuel

    // Constructeur privé pour empêcher l'instanciation directe
    private Game() {
        players = new ArrayList<>(); // Initialisation de la liste des joueurs
        rounds = new ArrayList<>(); // Initialisation de la liste des tours
        this.hexagones = Hexagone.generateHexagones(); // on génère les hexagones
        Game.sectors = Sector.CreateSectors(this.hexagones); // on génère les secteurs
        this.currentRoundNumber = 1; // Initialisation du numéro du round
    }

    // Méthode publique et statique pour obtenir l'instance unique
    public static Game getInstance() {
        try {
            if (instance == null) {
                instance = new Game();
            }
            return instance;
        } catch (Exception e) {
            System.err.println("Erreur lors de l'obtention de l'instance de jeu: " + e.getMessage());
            return null;
        }
    }

    // Méthode pour obtenir les secteurs ???
    public static Sector[][] getSectors() {
        try {
            return sectors;
        } catch (Exception e) {
            System.err.println("Erreur lors de l'obtention des secteurs: " + e.getMessage());
            return null;
        }
    }

    // Méthode pour obtenir le numéro du round actuel
    public int getCurrentRoundNumber() {
        return currentRoundNumber;
    }

    // Méthode pour définir le numéro du round actuel
    public void setCurrentRoundNumber(int currentRoundNumber) {
        this.currentRoundNumber = currentRoundNumber;
    }

    // Méthode pour obtenir un secteur par son numéro
    public Sector getSector(int sectorNumber) {
        try {
            for (Sector[] sector : sectors) {
                for (Sector sector1 : sector) {
                    if (sector1.getNumber() == sectorNumber) {
                        return sector1;
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("Erreur lors de l'obtention du secteur: " + e.getMessage());
        }
        return null;
    }

    /*
     * // Méthode pour obtenir un secteur par son numéro à partir d'une liste de
     * secteurs
     * public static Sector getSector(List<Sector> sectors, int sectorNumber) {
     * for (Sector sector : sectors) {
     * if (sector.getNumber() == sectorNumber) {
     * return sector;
     * }
     * }
     * return null; // Retourne null si le secteur n'est pas trouvé
     * }
     */

    // Méthode pour obtenir les hexagones
    public List<Hexagone> getHexagones() {
        return hexagones;
    }

    // Méthode pour obtenir les joueurs
    public ArrayList<Player> getPlayers() {
        return players;
    }

    // Méthode pour obtenir les tours
    public ArrayList<Round> getRounds() {
        return rounds;
    }

    // Ajoute un joueur à la liste des joueurs ???
    public void addPlayer(Player player) {
        try {
            players.add(player);
        } catch (Exception e) {
            System.err.println("Erreur lors de l'ajout du joueur: " + e.getMessage());
        }
    }

    // Ajoute un tour à la liste des tours ???
    public void addRound(Round round) {
        try {
            rounds.add(round);
        } catch (Exception e) {
            System.err.println("Erreur lors de l'ajout du tour: " + e.getMessage());
        }
    }

    // Retourne une représentation textuelle de la partie
    @Override
    public String toString() {
        return "Game [players=" + players + ", rounds=" + rounds + "]";
    }

    // Affiche les secteurs
    public void displaySectors() {
        try {
            for (int i = 0; i < sectors.length; i++) {
                for (int j = 0; j < sectors[i].length; j++) {
                    System.out.println("Sector [" + i + "][" + j + "]:");
                    List<Hexagone> sortedHexagones = new ArrayList<>(sectors[i][j].getHexagones());
                    sortedHexagones.sort(Comparator.comparingInt(Hexagone::getID));
                    for (Hexagone hex : sortedHexagones) {
                        if (hex instanceof HexagoneTriPrime) {
                            System.out.println("  HexagoneTriPrime ID: " + hex.getID() + ", Niveau: " + hex.getNiveau()
                                    + ", Occupant: " + hex.getOccupant() + ", Nombre de vaisseaux: "
                                    + hex.getNbShips());
                        }
                        if (hex != null) {
                            System.out.println("  Hexagone ID: " + hex.getID() + ", Niveau: " + hex.getNiveau());
                            System.out.println("  Hexagone ID: " + hex.getID() + ", Niveau: " + hex.getNiveau()
                                    + ", Occupant: " + hex.getOccupant() + ", Nombre de vaisseaux: "
                                    + hex.getNbShips());
                        }
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("Erreur lors de l'affichage des secteurs: " + e.getMessage());
        }
    }

    // Vérifie si un secteur est inoccupé
    private boolean isSectorUnoccupied(Sector sector) {
        for (Hexagone hex : sector.getHexagones()) {
            if (hex.getOccupant() != null) {
                return false;
            }
        }
        return true;
    }

    // Déploiement initial des vaisseaux par les joueurs
    public void initialDeployment(List<Player> players) {
        try {
            Scanner scanner = new Scanner(System.in);
            // Premier tour de déploiement
            for (Player player : players) {
                if (player instanceof BotPlayer botPlayer) {
                    botPlayer.deployShipsRandomly(Round.convertSectorsToList(sectors));
                } else {
                    deployShipsForPlayer(scanner, player);
                }
            }

            // Deuxième tour de déploiement en ordre inverse
            Collections.reverse(players);
            for (Player player : players) {
                if (player instanceof BotPlayer botPlayer) {
                    botPlayer.deployShipsRandomly(Round.convertSectorsToList(sectors));
                } else {
                    deployShipsForPlayer(scanner, player);
                }
            }
        } catch (Exception e) {
            System.err.println("Erreur lors du déploiement initial: " + e.getMessage());
        }
    }

    // Déploie les vaisseaux pour un joueur
    private void deployShipsForPlayer(Scanner scanner, Player player) {
        try {
            boolean deployed = false;
            while (!deployed) {
                System.out.println(player.getNom() + ", choisissez un secteur pour déployer vos vaisseaux.");

                // Afficher les secteurs disponibles
                List<Sector> availableSectors = new ArrayList<>();
                for (int i = 0; i < Game.sectors.length; i++) {
                    for (int j = 0; j < Game.sectors[i].length; j++) {
                        if (isSectorUnoccupied(Game.sectors[i][j])) {
                            availableSectors.add(Game.sectors[i][j]);
                            System.out.println(
                                    "Secteur numéro " + Game.sectors[i][j].getNumber() + " [" + i + "][" + j + "]");
                        }
                    }
                }

                // Demander le numéro du secteur
                System.out.print("Entrez le numéro du secteur: ");
                int sectorNumero = scanner.nextInt();
                scanner.nextLine(); // Consommer la nouvelle ligne

                Sector selectedSector = null;
                for (Sector sector : availableSectors) {
                    if (sector.getNumber() == sectorNumero) {
                        selectedSector = sector;
                        break;
                    }
                }

                if (selectedSector != null) {
                    List<Hexagone> availableHexagones = new ArrayList<>();
                    for (Hexagone hex : selectedSector.getHexagones()) {
                        if (hex.getNiveau() == 1 && hex.getOccupant() == null) {
                            availableHexagones.add(hex);
                        }
                    }

                    if (availableHexagones.size() >= 2) {
                        System.out.println("Hexagones de niveau 1 disponibles dans ce secteur:");
                        for (Hexagone hex : availableHexagones) {
                            System.out.println("ID: " + hex.getID() + " - " + hex);
                        }

                        System.out.print("Entrez l'ID de l'hexagone: ");
                        int hexId = scanner.nextInt();
                        scanner.nextLine(); // Consommer la nouvelle ligne

                        Hexagone selectedHex = null;
                        for (Hexagone hex : availableHexagones) {
                            if (hex.getID() == hexId) {
                                selectedHex = hex;
                                break;
                            }
                        }

                        if (selectedHex != null) {
                            selectedHex.setOccupant(player);
                            player.deployShips(selectedHex, 2);
                            if (selectedSector.getNumber() == 5) {
                                for (Hexagone hex : selectedSector.getHexagones()) {
                                    hex.setOccupant(player);
                                    hex.setNbShips(2);
                                }
                            }
                            deployed = true;
                        } else {
                            System.out.println("ID d'hexagone invalide.");
                        }
                    } else {
                        System.out.println("Pas assez d'hexagones de niveau 1 disponibles dans ce secteur.");
                    }
                } else {
                    System.out.println("Numéro de secteur invalide.");
                }
            }
        } catch (Exception e) {
            System.err.println("Erreur lors du déploiement des vaisseaux pour le joueur: " + e.getMessage());
        }
    }

    // Méthode pour effectuer le décompte final des points
    public void finalScoring() {
        try {
            Map<Player, Integer> finalScores = new HashMap<>();

            // Initialiser les scores finaux avec les points actuels des joueurs
            for (Player player : players) {
                finalScores.put(player, player.getNbPoints());
            }

            // Parcourir chaque secteur pour le décompte final
            for (Sector[] sectorRow : sectors) {
                for (Sector sector : sectorRow) {
                    for (Hexagone hex : sector.getHexagones()) {
                        if (hex.getNiveau() != 1) {
                            Player occupant = hex.getOccupant();
                            if (occupant != null) {
                                int points = hex.getNiveau() * 2;
                                finalScores.put(occupant, finalScores.get(occupant) + points);
                            }
                        }
                    }
                }
            }

            // Afficher les scores finaux
            for (Map.Entry<Player, Integer> entry : finalScores.entrySet()) {
                Player player = entry.getKey();
                int score = entry.getValue();
                player.setNbPoints(score);
                System.out.println(player.getNom() + " a un score final de " + score + " points.");
            }

            // Déterminer le vainqueur
            Player winner = Collections.max(finalScores.entrySet(), Map.Entry.comparingByValue()).getKey();
            System.out.println("Le vainqueur est " + winner.getNom() + " avec " + winner.getNbPoints() + " points.");
        } catch (Exception e) {
            System.err.println("Erreur lors du décompte final des points: " + e.getMessage());
        }
    }

    // Méthode pour sauvegarder l'état du jeu
    public void saveGame(String filename) throws IOException {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(filename))) {
            out.writeObject(this);
        } catch (IOException e) {
            System.err.println("Erreur lors de la sauvegarde du jeu: " + e.getMessage());
            throw e;
        }
    }

    // Méthode pour charger l'état du jeu
    public static Game loadGame(String filename) throws IOException, ClassNotFoundException {
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(filename))) {
            instance = (Game) in.readObject();
            // Ensure sectors are initialized after loading
            if (Game.sectors == null) {
                Game.sectors = Sector.CreateSectors(instance.hexagones);
            }
            return instance;
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Erreur lors du chargement du jeu: " + e.getMessage());
            throw e;
        }
    }
}
