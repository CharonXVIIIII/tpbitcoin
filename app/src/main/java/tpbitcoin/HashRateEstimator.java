package tpbitcoin;

import org.bitcoinj.core.Sha256Hash;

import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

public class HashRateEstimator {
    private final int duration; //duration of each experience, in milliseconds
    private final int numberOfTries; // number of experience

    public HashRateEstimator(int duration, int numberOfTries) {
        this.duration = duration;
        this.numberOfTries = numberOfTries;
    }

    public double estimate() {
        long hashCount = 0;
        long startTime = System.currentTimeMillis();
        long endTime = startTime + duration;
        MessageDigest md = Sha256Hash.newDigest();
        Random random = new Random();
        byte[] bytes = new byte[32];

        while (System.currentTimeMillis() < endTime) {
            random.nextBytes(bytes);
            md.update(bytes);
            hashCount++;
        }

        long durationSeconds = (endTime - startTime) / 1000;
        if (durationSeconds == 0) return 0.0;
        return (double) hashCount / durationSeconds;
    }


}
