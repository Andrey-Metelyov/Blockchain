package blockchain;

import java.util.Date;
import java.util.LinkedList;
import java.util.Random;

class Block {
    long id;
    long timestamp;
    long magic;
    String hashPrevious;
    String hash;
    //        long generationTime;
    final Random random = new Random();
//        String content;

    Block(long id, int zeroesNeeded, String hashPrevious, String content) {
        this.id = id;
        this.hashPrevious = hashPrevious;
//            this.content = content;
        this.hash = getHash(zeroesNeeded);
        this.timestamp = new Date().getTime();
//            generationTime = new Date().getTime() - timestamp;
    }

    private String getHash(int zeroesNeeded) {
        String hash;
        StringBuilder zeroes = new StringBuilder();
        zeroes.append("0".repeat(Math.max(0, zeroesNeeded)));
        do {
            magic = random.nextLong();
            hash = StringUtil.applySha256(getHashString());
        } while (!hash.startsWith(zeroes.toString()));
        return hash;
    }

    private String getHashString() {
        return id +
                String.valueOf(timestamp) +
                magic +
                hashPrevious;
    }

    @Override
    public String toString() {
        return "Id: " + id +
                "\nTimestamp: " + timestamp +
                "\nMagic number: " + magic +
                "\nHash of the previous block:\n" + hashPrevious +
                "\nHash of the block:\n" + hash;
//                    "\nBlock was generating for " + (generationTime / 1000) + " seconds" +
    }

    public int getHashZeroes() {
        int counter = 0;
        while (hash.charAt(counter) == '0') {
            counter++;
        }
        return counter;
    }
}

public class Blockchain {
    LinkedList<Block> blocks = new LinkedList<>();
    int zeroesNeeded = 0;
    long lastGenerationStartTimestamp;

    public int size() {
        return blocks.size();
    }

    public static class BlockParameters {
        long id;
        String prevHash;
        int zeroesNeeded;
    }

    synchronized public BlockParameters getNextBlockParameters() {
        BlockParameters parameters = new BlockParameters();
        if (blocks.size() > 0) {
            Block lastBlock = blocks.getLast();
            parameters.id = lastBlock.id + 1;
            parameters.prevHash = lastBlock.hash;
        } else {
            parameters.id = 1;
            parameters.prevHash = "0";
        }
        parameters.zeroesNeeded = zeroesNeeded;
        if (lastGenerationStartTimestamp == 0) {
            lastGenerationStartTimestamp = new Date().getTime();
//            System.out.println("lastGenerationStartTimestamp = " + lastGenerationStartTimestamp);
        }
        return parameters;
    }

    private static final Blockchain instance = new Blockchain();

    private Blockchain() {
    }

    public static Blockchain getInstance() {
        return instance;
    }

    synchronized boolean addBlock(String miner, Block block) {
        if (getLashHash().equals(block.hashPrevious) && getZeroesNeeded() >= block.getHashZeroes()) {
            System.out.println("Block:");
            System.out.println("Created by miner # " + miner.substring(miner.length() - 1));
            blocks.add(block);
            System.out.println(block.toString());
            long generationTime = block.timestamp - lastGenerationStartTimestamp;
            System.out.println("Block was generating for " + generationTime / 1000 + " seconds");
//            System.out.println("Block was generating for " + generationTime + " ms");
            if (generationTime < 15_000) {
                zeroesNeeded++;
                System.out.println("N was increased to " + zeroesNeeded);
            } else if (generationTime > 60_000) {
                zeroesNeeded--;
                System.out.println("N was decreased by 1");
            } else {
                System.out.println("N stays the same");
            }
            System.out.println();
            lastGenerationStartTimestamp = 0;
            return true;
        } else {
            return false;
        }
    }

    private int getZeroesNeeded() {
        return zeroesNeeded;
    }

    private String getLashHash() {
        return blocks.size() > 0 ? blocks.getLast().hash : "0";
    }

    boolean isValidChain() {
        String hashPrevious = "0";
        for (Block block : blocks) {
            if (!block.hashPrevious.equals(hashPrevious)) {
                return false;
            }
            hashPrevious = block.hash;
        }
        return true;
    }

//    void showBlocks() {
//        for (Block block : blocks) {
//            System.out.println(block.toString());
//            System.out.println();
//        }
//    }
}

class BlockGenerator implements Runnable {

    public BlockGenerator() {
    }

    @Override
    public void run() {
        String miner = Thread.currentThread().getName();
        Blockchain blockchain = Blockchain.getInstance();
        Blockchain.BlockParameters parameters = blockchain.getNextBlockParameters();
//        System.out.println("start generation " + miner + " id=" + parameters.id + " prevHash=" + parameters.prevHash + " zeroesNeeded=" + parameters.zeroesNeeded);
        Block block = BlockchainUtil.generate(parameters.id, parameters.prevHash, parameters.zeroesNeeded);
        if (blockchain.addBlock(miner, block)) {

        }
//        System.out.println("end generation " + miner);
    }
}