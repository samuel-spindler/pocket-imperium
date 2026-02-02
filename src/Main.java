import java.io.IOException;
import java.io.Serializable;
import java.util.*;

// Classe principale pour lancer le jeu
public class Main implements Serializable {

    public static void main(String[] args) throws IOException, ClassNotFoundException {
        Scanner scanner = new Scanner(System.in);
        Game game;

        // Ask the user if they want to load a saved game
        System.out.println("========================================");
        System.out.println("Bienvenue dans Pocket Imperium");
        System.out.println("========================================");
        System.out.print("Voulez-vous charger une partie sauvegardée ? (Y/N): ");
        String response = scanner.nextLine();

        if (response.equalsIgnoreCase("Y")) {
            System.out.print("Entrez le nom du fichier de sauvegarde: ");
            String filename = scanner.nextLine();
            try {
                game = Game.loadGame(filename);
                System.out.println("Partie chargée avec succès.");
                // Display the game map after loading
                game.displaySectors();
            } catch (IOException | ClassNotFoundException e) {
                System.err.println("Erreur lors du chargement de la partie: " + e.getMessage());
                System.out.println("Démarrage d'une nouvelle partie.");
                game = Game.getInstance();
            }
        } else {
            game = Game.getInstance();
        }

        // If the game is newly initialized, set up players
        if (game.getPlayers().isEmpty()) {
            // Création de l'instance de scanner pour pouvoir entrer le nom des joueurs
            scanner = new Scanner(System.in);

            // Demander le nombre de vrais joueurs
            int nombreVraisJoueurs = 0;
            while (true) {
                try {
                    System.out.print("Entrez le nombre de vrais joueurs (1-3): ");
                    if (scanner.hasNextInt()) {
                        nombreVraisJoueurs = scanner.nextInt();
                        scanner.nextLine();
                        if (nombreVraisJoueurs < 1 || nombreVraisJoueurs > 3) {
                            throw new IllegalArgumentException("Le nombre de joueurs doit être entre 1 et 3.");
                        }
                        break;
                    } else {
                        System.out.println("Veuillez entrer un nombre valide.");
                        scanner.next(); // Consommer l'entrée non valide
                    }
                } catch (InputMismatchException e) {
                    System.out.println("Veuillez entrer un nombre valide.");
                    scanner.next(); // Consommer l'entrée non valide
                } catch (IllegalArgumentException e) {
                    System.out.println(e.getMessage());
                }
            }

            // Création des joueurs
            for (int i = 1; i <= nombreVraisJoueurs; i++) {
                System.out.print("Entrez le nom du joueur " + i + ": ");
                if (scanner.hasNextLine()) {
                    String nomJoueur = scanner.nextLine();
                    Player joueur = new Player(nomJoueur, EnumColor.values()[i - 1]);
                    game.addPlayer(joueur);
                }
            }

            // Compléter avec des BotPlayer si nécessaire et définir leur stratégie
            for (int i = nombreVraisJoueurs + 1; i <= 3; i++) {
                BotPlayer bot = new BotPlayer(new RandomStrategy(), "Bot " + i, EnumColor.values()[i - 1]);
                game.addPlayer(bot);
            }

            // Afficher les joueurs
            System.out.println("========================================");
            System.out.println("Liste des joueurs");
            System.out.println("========================================");
            System.out.println(game.players);

            // Affichage de la carte avant le déploiement initial
            System.out.println("========================================");
            System.out.println("Carte avant le déploiement initial");
            System.out.println("========================================");
            game.displaySectors();

            // Phase de déploiement initial
            game.initialDeployment(game.getPlayers());

            // Affichage de la carte après le déploiement initial
            System.out.println("========================================");
            System.out.println("Carte après le déploiement initial");
            System.out.println("========================================");
            game.displaySectors();
        }

        // Loop for up to 9 rounds
        for (int roundNumber = game.getCurrentRoundNumber(); roundNumber <= 9; roundNumber++) {
            System.out.println("========================================");
            System.out.println("Round " + roundNumber);
            System.out.println("========================================");

            // Determine the starting player for this round using Round class methods
            Player starter = game.getPlayers().get((roundNumber - 1) % game.getPlayers().size());
            Player secondPlayer = game.getPlayers().get((roundNumber) % game.getPlayers().size());
            Player thirdPlayer = game.getPlayers().get((roundNumber + 1) % game.getPlayers().size());

            Round round = new Round(roundNumber, starter, secondPlayer, thirdPlayer);

            // Phase Plan
            System.out.println("========================================");
            System.out.println("Phase Plan");
            System.out.println("========================================");
            try {
                round.phasePlan();
            } catch (NoSuchElementException e) {
                System.err.println("Erreur lors de la saisie de l'ordre des cartes: " + e.getMessage());
                break;
            }

            // Phase Perform
            System.out.println("========================================");
            System.out.println("Phase Perform");
            System.out.println("========================================");
            try {
                round.phasePerform();
            } catch (NoSuchElementException e) {
                System.err.println("Erreur lors de la phase de performance: " + e.getMessage());
                break;
            }

            // Affichage de la carte après la phase de Perform
            System.out.println("========================================");
            System.out.println("Carte après la phase de Perform");
            System.out.println("========================================");
            game.displaySectors();

            // Phase Exploit
            System.out.println("========================================");
            System.out.println("Phase Exploit");
            System.out.println("========================================");
            round.phaseExploit(starter);

            // Affichage de la carte après la phase d'exploitation
            System.out.println("========================================");
            System.out.println("Carte après la phase d'exploitation");
            System.out.println("========================================");
            game.displaySectors();

            // Check if any player has no ships left
            boolean gameOver = false;
            for (Player player : game.getPlayers()) {
                if (player.getNbShips() == 0) {
                    System.out.println("Le joueur " + player.getNom() + " n'a plus de vaisseaux. Fin de la partie.");
                    gameOver = true;
                    break;
                }
            }

            if (gameOver) {
                break;
            }

            // Update the current round number in the game instance
            game.setCurrentRoundNumber(roundNumber + 1);

            // Ask if the user wants to save the game after each round
            System.out.print("Voulez-vous sauvegarder la partie ? (Y/N): ");
            if (scanner.hasNextLine()) {
                response = scanner.nextLine();
            } else {
                response = "N";
            }
            if (response.equalsIgnoreCase("Y")) {
                System.out.print("Entrez le nom du fichier de sauvegarde: ");
                if (scanner.hasNextLine()) {
                    String filename = scanner.nextLine();
                    try {
                        game.saveGame(filename);
                        System.out.println("Partie sauvegardée avec succès.");
                    } catch (IOException e) {
                        System.err.println("Erreur lors de la sauvegarde de la partie: " + e.getMessage());
                    }
                }
            }
        }
        // Final scoring
        System.out.println("========================================");
        System.out.println("Décompte final des points");
        System.out.println("========================================");
        game.finalScoring();

        // Fermer le scanner après toutes les opérations
        scanner.close();
    }
}
