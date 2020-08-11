package blockchain;

import java.util.Collections;
import java.util.List;

public class BlockchainUtil {
    public static Block generate(long id, String prevHash, int zeroesNeeded) {
//        System.out.println("start generating block: id=" + id + " prevHash=" + prevHash + " zeroes needed=" + zeroesNeeded);
        return new Block(id, zeroesNeeded, prevHash);
    }
}
