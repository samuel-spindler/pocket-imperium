import java.util.*;

// Interface de stratégie
public interface Strategy {
    // Méthodes de l'interface :
    public void jouer();

    public Map<String, Integer> definirOrdreCartes();

    public void appliquerExpand(Player player, Expand expandCard, List<Sector> sectors);

    public void appliquerExplore(Player player, Explore exploreCard, List<Sector> sectors);

    public void appliquerExterminate(Player player, Exterminate exterminateCard, List<Sector> sectors);

    public void deployShipsRandomly(Player player, List<Sector> sectors); // Nouvelle méthode pour le déploiement
                                                                          // initial

    public void phaseExploit(Player player, List<Sector> sectors); // Nouvelle méthode pour la phase d'exploitation
}