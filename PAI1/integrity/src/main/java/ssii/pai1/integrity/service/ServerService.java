package ssii.pai1.integrity.service;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ssii.pai1.integrity.model.Item;
import ssii.pai1.integrity.repository.ServerRepository;

@Service
public class ServerService {
    

    @Autowired
    private ServerRepository serverRepo;

    public boolean verify(Item entity){
        Optional<Item> foundEntity = serverRepo.findItemByPath(entity.getPath());
        if(foundEntity.isPresent() && foundEntity.get().getHashFile().equals(entity.getHashFile())){
            return true;
        }else{
            return false;
        }
    }

    public String createMAC(String hashFile, String token, String challenge) throws NoSuchAlgorithmException {
        String str = hashFile + token + challenge;
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] encodedhash = digest.digest(str.getBytes(StandardCharsets.UTF_8));
        return toHex(encodedhash);
    }

    public static String toHex(byte[] bytes) {
        BigInteger bi = new BigInteger(1, bytes);
        return String.format("%0" + (bytes.length << 1) + "X", bi);
    }
}
