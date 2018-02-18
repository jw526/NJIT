package hw1;

import java.util.Arrays;
import java.util.HashMap;

public class SlotMachine {

    public enum Symbol {
        BELLS("Bells", 10), FLOWERS("Flowers", 5), FRUITS("Fruits", 3),
        HEARTS("Hearts", 2), SPADES("Spades", 1);

        // symbol name
        private final String name;

        // payout factor (i.e. multiple of wager) when matching symbols of this type
        private final int payoutFactor;

        Symbol(String name, int payoutFactor) {
            this.name = name;
            this.payoutFactor = payoutFactor;
        }

        public String getName() {
            return name;
        }

        public int getPayoutFactor() {
            return payoutFactor;
        }

    }

    private final int numReels, wagerUnitValue;
    private final double [] odds;
    private double totalPayout, totalWager;
    /**
     * Constructor
     * @param numReels number of reels in slot machine
     * @param odds odds for each symbol in a reel, indexed by its enum ordinal value; odds value is non-zero and sums to 1
     * @param wagerUnitValue unit value in cents of a wager
     */
    public SlotMachine(int numReels, double [] odds, int wagerUnitValue) {
        this.odds = Arrays.copyOf(odds, odds.length); //copy array so that it won't be affected by changes outside object
        this.numReels = numReels;
        this.wagerUnitValue = wagerUnitValue;
    }

    /**
     * Get symbol for a reel when the user pulls slot machine lever
     * @return symbol type based on odds (use Math.random())
     */
    public Symbol getSymbolForAReel() {
        double rand = Math.random();
        if(rand < odds[0]) return Symbol.BELLS;
        if(( rand >= odds[0]) && (rand <(odds[0]+odds[1]))) return Symbol.FLOWERS;
        if((rand >=(odds[0]+odds[1])) && (rand < (odds[0]+odds[1]+odds[2]))) return Symbol.FRUITS;
        if((rand >= (odds[0]+odds[1]+odds[2])) && (rand < (odds[0]+odds[1]+odds[2]+odds[3]))) return Symbol.HEARTS;
        return Symbol.SPADES;

    }


    /**
     * Calculate the payout for reel symbols based on the following rules:
     * 1. If more than half but not all of the reels have the same symbol then payout factor is same as payout factor of the symbol
     * 2. If all of the reels have the same symbol then payout factor is twice the payout factor of the symbol
     * 3. Otherwise payout factor is 0
     * Payout is then calculated as wagerValue multiplied by payout factor
     * @param reelSymbols array of symbols one for each reel
     * @param wagerValue value of wager given by the user
     * @return calculated payout
     */
    public long calcPayout(Symbol[] reelSymbols, int wagerValue) {
        HashMap<Symbol, Integer> map = new HashMap<Symbol, Integer>();
        Integer count;
        long payout = 0;
        for (int i=0; i<numReels; i++){
            if (map.containsKey(reelSymbols[i])){
                count = map.get(reelSymbols[i])+1;
                if ((count > numReels/2) && (count < numReels))
                    payout = reelSymbols[i].getPayoutFactor()*wagerValue;
                if (count == numReels)
                    payout = reelSymbols[i].getPayoutFactor()*wagerValue*2;
                else{
                    //payout = 0;
                    map.put(reelSymbols[i],count );
                }
            }
            else map.put(reelSymbols[i], 1);
        }
      return payout;
    }

    /**
     * Called when the user pulls the lever after putting wager tokens
     * 1. Get symbols for the reels using getSymbolForAReel()
     * 2. Calculate payout using calcPayout()
     * 3. Display the symbols, e.g. Bells Flowers Flowers..
     * 4. Display the payout in dollars and cents e.g. $2.50
     * 5. Keep track of total payout and total receipts from wagers
     * @param numWagerUnits number of wager units given by the user
     */
    public void pullLever(int numWagerUnits) {
        Symbol[] arr = new Symbol[numReels];
        for (int i = 0; i < numReels; i++){
            arr[i] = this.getSymbolForAReel();
            System.out.print(arr[i]+ " ");
        }
        double dollarPayout = (double) this.calcPayout(arr, numWagerUnits*wagerUnitValue)/100;
        System.out.println();
        System.out.println("payout: $" + dollarPayout);
        totalPayout += dollarPayout;
        totalWager += (double) numWagerUnits*wagerUnitValue/100;

    }

    /**
     * Get total payout to the user as percent of total wager value
     * @return e.g. 85.5
     */
    public double getPayoutPercent() {
        return (totalPayout/totalWager*100);
    }

    /**
     * Clear the total payout and wager value
     */
    public void reset() {
        totalPayout = 0;
        totalWager = 0;
    }

    public static void main(String [] args) {
        double [] odds = new double[Symbol.values().length];
        // sum of odds array values must equal 1.0
        odds[Symbol.HEARTS.ordinal()] = 0.3;
        odds[Symbol.SPADES.ordinal()] = 0.25;
        odds[Symbol.BELLS.ordinal()] = 0.05;
        odds[Symbol.FLOWERS.ordinal()] = 0.2;
        odds[Symbol.FRUITS.ordinal()] = 0.2;

        SlotMachine sm = new SlotMachine(3, odds, 25); // quarter slot machine
        sm.pullLever(2);
        sm.pullLever(1);
        sm.pullLever(3);
        System.out.println("Pay out percent to user = " + sm.getPayoutPercent());
        sm.reset();
        sm.pullLever(4);
        sm.pullLever(1);
        sm.pullLever(1);
        sm.pullLever(2);
        System.out.println("Pay out percent to user = " + sm.getPayoutPercent());
    }



}
