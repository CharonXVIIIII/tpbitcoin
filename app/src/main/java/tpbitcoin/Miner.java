package tpbitcoin;

import org.bitcoinj.core.*;
import org.bitcoinj.script.ScriptBuilder;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;


public class Miner {
    private static int txCounter;
    private NetworkParameters params;
    public static final long EASY_DIFFICULTY_TARGET = Utils.encodeCompactBits(Utils.decodeCompactBits(Block.EASIEST_DIFFICULTY_TARGET).divide(BigInteger.valueOf(1024)));
    public static final long SOMEWHAT_HARDER_DIFFICULTY_TARGET = Utils.encodeCompactBits(Utils.decodeCompactBits(Block.EASIEST_DIFFICULTY_TARGET).divide(BigInteger.valueOf(65536)));

    public Miner(NetworkParameters params) {
        this.params = params;
    }

    private static Block setValidNonce(Block block) throws NoSuchAlgorithmException {
        long nonce = 0;
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        while (true) {
            block.setNonce(nonce++);
            byte[] header = block.cloneAsHeader().bitcoinSerialize();
            byte[] hash = digest.digest(header);
            BigInteger hashInt = new BigInteger(1, hash);
            if (hashInt.compareTo(Utils.decodeCompactBits(block.getDifficultyTarget())) < 0) {
                return block;
            }
        }
    }

    private static Transaction generateCoinbase(NetworkParameters params, byte[] pubKey, String amount) {
        Transaction coinbase = new Transaction(params);
        final ScriptBuilder inputBuilder = new ScriptBuilder();
        inputBuilder.data(new byte[]{(byte) txCounter, (byte) (txCounter++ >> 8)});
        coinbase.addInput(new TransactionInput(params, coinbase, inputBuilder.build().getProgram()));
        coinbase.addOutput(new TransactionOutput(params, coinbase, Coin.parseCoin(amount),
                ScriptBuilder.createP2PKOutputScript(ECKey.fromPublicOnly(pubKey)).getProgram()));
        return coinbase;
    }

    private Block createBlock(Block lastBlock, List<Transaction> txs, byte[] pubKey) {
        if (txs.isEmpty()) {
            txs.add(generateCoinbase(params, pubKey, "50"));
        }
        return new Block(params, lastBlock.getVersion(), lastBlock.getHash(), null, lastBlock.getTimeSeconds(), EASY_DIFFICULTY_TARGET, lastBlock.getNonce(), txs);
    }

    public Block mine(Block lastBlock, List<Transaction> txs, byte[] pubKey) throws NoSuchAlgorithmException{
        try {
            Block newBlock = createBlock(lastBlock, txs, pubKey);
            newBlock.setNonce(0);
            newBlock.verifyHeader();
            return setValidNonce(newBlock);
        } catch (VerificationException e) {
            return null;
        }
    }
}




