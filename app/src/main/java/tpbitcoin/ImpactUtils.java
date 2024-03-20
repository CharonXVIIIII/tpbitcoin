package tpbitcoin;

import java.math.BigInteger;

public class ImpactUtils {





    /**
     * computes the expected time (in seconds) for mining a block
     * @param hashrate: miner hashing capacity (hash/s)
     * @param difficultyAsInteger: block difficulty, as BigInteger (256 bits integer)
     * @return expected time in seconds before finding a correct block
     */
    // TODO
    public static long expectedMiningTime(long hashrate, BigInteger difficultyAsInteger){
        BigInteger hashrateToBigInt = BigInteger.valueOf(hashrate);
        BigInteger twoPower32 = BigInteger.valueOf(2).pow(32);
        BigInteger numerator = twoPower32.multiply(difficultyAsInteger);
        BigInteger expectedTimeBigInt = numerator.divide(hashrateToBigInt);
        return expectedTimeBigInt.longValue();
    }

    public static double expectedYearsToMineABlock(long hashrate, BigInteger difficultyAsInteger) {
        long seconds = expectedMiningTime(hashrate, difficultyAsInteger);
        return seconds / (60.0 * 60 * 24 * 365.25);
    }



    /**
     * Compute the total hashrate of the network given current difficulty level
     * @param difficultyAsInteger: difficulty level as 256bits integer
     * @return hashrate of the network in GH/s
     */
    // TODO
    public static long  globalHashRate(BigInteger difficultyAsInteger){
        return 1L;
    }

    /**
     * Compute the total energy consumption of the network
     * assuming each miner has the same hashrate, and consume the same power
     * @param minerHashrate: the hashrate of each miner, in GH/s
     * @param minerPower: the power consumption of each miner, in Watts
     * @param networkHashrate : the global hashrate of the network
     * @param duration : in second
     * @return energy consumed during duration, in kWh
     */
    // TODO
    public static long globalEnergyConsumption(long minerHashrate, long minerPower, long networkHashrate, long duration){
        return 1L;
    }





}
