package exnihilo.registries.helpers;

import exnihilo.utils.ItemInfo;

public class SiftingResult {

    public final ItemInfo drop;
    public final ProbabilityType type;
    public final int paramN; // For binomial: number of trials
    public final float paramP; // For binomial: probability or for fixed: max amount

    // Fixed amount (always drops exact amount)
    public static SiftingResult fixed(ItemInfo drop, int amount) {
        return new SiftingResult(drop, ProbabilityType.FIXED, amount, 0);
    }

    // Simple chance: 1 in N
    public static SiftingResult chance(ItemInfo drop, int rarity) {
        return new SiftingResult(drop, ProbabilityType.CHANCE, rarity, 0);
    }

    // Binomial: paramN trials, each with probability paramP (0-1)
    public static SiftingResult binomial(ItemInfo drop, int n, float p) {
        return new SiftingResult(drop, ProbabilityType.BINOMIAL, n, p);
    }

    // Legacy constructor (simple rarity)
    public SiftingResult(ItemInfo drop, int rarity) {
        this(drop, ProbabilityType.CHANCE, rarity, 0);
    }

    private SiftingResult(ItemInfo drop, ProbabilityType type, int paramN, float paramP) {
        this.drop = drop;
        this.type = type;
        this.paramN = paramN;
        this.paramP = paramP;
    }

    public enum ProbabilityType {
        FIXED, // Always drops paramN items
        CHANCE, // 1 in paramN chance
        BINOMIAL // Binomial distribution with n=paramN, p=paramP
    }
}
