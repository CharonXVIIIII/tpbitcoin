/*
 * This Java source file was generated by the Gradle 'init' task.
 */
package tpbitcoin;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import org.bitcoinj.core.*;
import org.bitcoinj.params.UnitTestParams;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.net.URL;
import java.security.NoSuchAlgorithmException;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class App {


    public static void main(String[] args) throws NoSuchAlgorithmException {

        //Q1  hashrate
        double localHashrate = new HashRateEstimator(5000, 5).estimate();
        System.out.println("Local hashrate: " + localHashrate);

        // Q2: latest  block  from mainet (bitcoin blockchain) and its predecessor && Q3 RECUPERER LES TRANSACTIONS D'UN BLOCK
        Context context = new Context(new UnitTestParams());
        Explorer explorer = new Explorer();
        String latestBlockHash = explorer.getLatestHash();
        Block latestBlock = explorer.getBlockFromHash(context.getParams(), latestBlockHash);

        System.out.println("Latest block hash: " + latestBlockHash);
        System.out.println("Nonce: " + latestBlock.getNonce());
        System.out.println("Difficulty Target (bits): " + latestBlock.getDifficultyTarget());
        System.out.println("Difficulty Target (as BigInteger): " + Utils.decodeCompactBits(latestBlock.getDifficultyTarget()));
        System.out.println("Transactions in the latest block:");

        System.out.println("First transaction in the block:");
        System.out.println(latestBlock.getTransactions().get(0));

        // Q4 Mine a new block
        Miner miner = new Miner(context.getParams());
        ArrayList<Transaction> txs = new ArrayList<>();
        //    public Block mine(Block lastBlock, List<Transaction> txs, byte[] pubKey) throws NoSuchAlgorithmException {
        byte[] pubKey = new ECKey().getPubKey();
        miner.mine(latestBlock, txs, pubKey);

        System.out.println("\n");

        // Q9/Q10 energy w/ most profitable hardware
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(YearMonth.class, new YearMonthAdapter())
                .create();
        List<MiningHardware> hardwares = new ArrayList<>();

        URL resource = App.class.getClassLoader().getResource("hardware.json");
        try (BufferedReader reader = new BufferedReader(new FileReader(resource.getFile()))) {
            Type listType = new TypeToken<ArrayList<MiningHardware>>() {
            }.getType();
            hardwares = gson.fromJson(reader, listType);
        } catch (Exception e) {
            System.err.println("error opening/reading hardware.json " + e.getMessage());
        }


        }


    }

