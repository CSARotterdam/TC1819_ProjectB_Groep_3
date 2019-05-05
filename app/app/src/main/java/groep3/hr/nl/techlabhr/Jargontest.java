package groep3.hr.nl.techlabhr;
import java.util.Arrays;

import static com.kosprov.jargon2.api.Jargon2.*;

public class Jargontest {
    public static void main(String[] args) {
        byte[] password = "this is a password".getBytes();

        // Configure the hasher
        Hasher hasher = jargon2Hasher()
                .type(Type.ARGON2d) // Data-dependent hashing
                .memoryCost(65536)  // 64MB memory cost
                .timeCost(3)        // 3 passes through memory
                .parallelism(4)     // use 4 lanes and 4 threads
                .saltLength(16)     // 16 random bytes salt
                .hashLength(16);    // 16 bytes output hash

        // Set the password and calculate the encoded hash
        String encodedHash = hasher.password(password).encodedHash();

        System.out.printf("Hash: %s%n", encodedHash);

        // Just get a hold on the verifier. No special configuration needed
        Verifier verifier = jargon2Verifier();

        // Set the encoded hash, the password and verify
        boolean matches = verifier.hash(encodedHash).password(password).verifyEncoded();

        System.out.printf("Matches: %s%n", matches);
    }
}
