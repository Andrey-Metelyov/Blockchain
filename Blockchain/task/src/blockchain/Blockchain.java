package blockchain;

import java.util.*;

class Block {
    long id;
    long timestamp;
    long magic;
    String hashPrevious;
    String hash;
    //        long generationTime;
    final Random random = new Random();
    List<String> messages;

    Block(long id, int zeroesNeeded, String hashPrevious) {
        this.id = id;
        this.hashPrevious = hashPrevious;
//        this.messages = messages;
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
        StringBuilder messages = new StringBuilder();
        for (String message : this.messages) {
            messages.append(message).append("\n");
        }
        return "Id: " + id +
                "\nTimestamp: " + timestamp +
                "\nMagic number: " + magic +
                "\nHash of the previous block:\n" + hashPrevious +
                "\nHash of the block:\n" + hash +
                "\nBlock data:" + (messages.length() > 0 ? "\n" + messages : " no messages\n");
//                    "\nBlock was generating for " + (generationTime / 1000) + " seconds" +
    }

    public int getHashZeroes() {
        int counter = 0;
        while (hash.charAt(counter) == '0') {
            counter++;
        }
        return counter;
    }

    public void setMessages(List<String> messages) {
        this.messages = List.copyOf(messages);
    }
}

public class Blockchain {
    LinkedList<Block> blocks = new LinkedList<>();
    List<String> messages = new ArrayList<>();
    int zeroesNeeded = 0;
    long lastGenerationStartTimestamp;

    synchronized public int size() {
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

    synchronized void sendMessage(String message) {
//        System.out.println("Send message: " + message);
        messages.add(message);
    }

    synchronized boolean addBlock(String miner, Block block) {
        if (getLashHash().equals(block.hashPrevious) && getZeroesNeeded() >= block.getHashZeroes()) {
            StringBuilder blockDesc = new StringBuilder();
//            System.out.println("Block:");
            blockDesc.append("Block:\n");
            miner = "miner" + miner.substring(miner.length() - 1);
//            System.out.println("Created by " + miner);
            blockDesc.append("Created by " + miner + "\n");
//            System.out.println(miner + " gets 100 VC");
            blockDesc.append(miner + " gets 100 VC\n");
            block.setMessages(messages);
            messages.clear();
            blocks.add(block);
//            System.out.print(block.toString());
            blockDesc.append(block.toString());
            long generationTime = block.timestamp - lastGenerationStartTimestamp;
//            System.out.println("Block was generating for " + generationTime / 1000 + " seconds");
            blockDesc.append("Block was generating for " + generationTime / 1000 + " seconds\n");
//            System.out.println("Block was generating for " + generationTime + " ms");
            if (generationTime < 100) {
                zeroesNeeded++;
//                System.out.println("N was increased to " + zeroesNeeded);
                blockDesc.append("N was increased to " + zeroesNeeded + "\n");
            } else if (generationTime > 400) {
                zeroesNeeded--;
//                System.out.println("N was decreased by 1");
                blockDesc.append("N was decreased by 1\n");
            } else {
//                System.out.println("N stays the same");
                blockDesc.append("N stays the same\n");
            }
            if (block.id <= 15) {
                System.out.println(blockDesc.toString() + "\n");
            }
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
        while (true) {
            Blockchain.BlockParameters parameters = blockchain.getNextBlockParameters();
            //        System.out.println("start generation " + miner + " id=" + parameters.id + " prevHash=" + parameters.prevHash + " zeroesNeeded=" + parameters.zeroesNeeded);
            Block block = BlockchainUtil.generate(parameters.id, parameters.prevHash, parameters.zeroesNeeded);
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
//                e.printStackTrace();
                break;
            }
            if (blockchain.addBlock(miner, block)) {
                // block accepted
            }
        }
    }
}