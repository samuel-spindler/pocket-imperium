import java.util.*;

// Classe pour représenter un joueur bot
public class BotPlayer extends Player {

    // Attribut de la classe :
    private Strategy strategy; // Stratégie du bot

    // Constructeur de la classe
    public BotPlayer(Strategy strategy, String name, EnumColor couleur) {
        super(name, couleur);
        this.strategy = strategy;
    }

    // Méthode pour définir la stratégie
    public void setStrategy(Strategy strategy) {
        this.strategy = strategy;
    }

    // Méthode pour exécuter la stratégie actuelle
    public void jouer() {
        if (strategy != null) {
            strategy.jouer();
        }
    }

    // Redéfinir la méthode definirOrdreCartes pour utiliser la stratégie
    @Override
    public Map<String, Integer> definirOrdreCartes(List<String> ordreCartes) {
        Map<String, Integer> ordre = strategy.definirOrdreCartes();
        setOrdreCartes(ordre);
        return ordre;
    }

    // Redéfinir la méthode choisirOrdreCartes pour utiliser la stratégie
    @Override
    public Map<String, Integer> choisirOrdreCartes() {
        return definirOrdreCartes(null); // Utiliser la stratégie pour définir l'ordre des cartes
    }

    // Méthode pour appliquer la carte Expand en utilisant la stratégie
    public void appliquerExpand(Expand expandCard, List<Sector> sectors) {
        if (strategy != null) {
            strategy.appliquerExpand(this, expandCard, sectors);
        }
    }

    // Méthode pour appliquer la carte Explore en utilisant la stratégie
    public void appliquerExplore(Explore exploreCard, List<Sector> sectors) {
        if (strategy != null) {
            strategy.appliquerExplore(this, exploreCard, sectors);
        }
    }

    // Méthode pour appliquer la carte Exterminate en utilisant la stratégie
    public void appliquerExterminate(Exterminate exterminateCard, List<Sector> sectors) {
        if (strategy != null) {
            strategy.appliquerExterminate(this, exterminateCard, sectors);
        }
    }

    // Méthode pour le déploiement initial des vaisseaux de manière aléatoire
    public void deployShipsRandomly(List<Sector> sectors) {
        if (strategy != null) {
            strategy.deployShipsRandomly(this, sectors);
        }
    }

    // Méthode pour la phase d'exploitation
    public void phaseExploit(List<Sector> sectors) {
        if (strategy != null) {
            strategy.phaseExploit(this, sectors);
        }
    }
}
