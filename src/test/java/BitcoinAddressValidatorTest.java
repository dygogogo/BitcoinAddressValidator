import main.BitcoinAddressValidator;
import org.junit.Test;
import static org.junit.Assert.*;

public class BitcoinAddressValidatorTest {
    @Test
    public void testValidateBitcoinAddressBase58() throws Exception {
        String base58Address = "1BoatSLRHtKNngkdXEeobR76b53LETtpyT";
        assertTrue(BitcoinAddressValidator.validateBitcoinAddress(base58Address));
    }

    @Test
    public void testValidateBitcoinAddressBech32() throws Exception {
        String bech32Address = "bc1qar0srrr7xfkvy5l643lydnw9re59gtzzwf5mdq";
        assertTrue(BitcoinAddressValidator.validateBitcoinAddress(bech32Address));
    }

    @Test
    public void testValidateBitcoinAddressInvalid() throws Exception {
        String invalidAddress = "not_a_valid_address";
        assertFalse(BitcoinAddressValidator.validateBitcoinAddress(invalidAddress));
    }

    @Test
    public void testValidateBitcoinAddressInvalidFormat() throws Exception {
        // 使用一个包含非法字符的字符串作为测试用例
        String invalidBech32Address = "bc1qar0srrr7xfkvy5l643lydnw9re59gt!!!";
        assertFalse(BitcoinAddressValidator.validateBitcoinAddress(invalidBech32Address));

        // 使用一个长度不符合Bech32地址规范的字符串作为测试用例
        String tooShortBech32Address = "bc1q";
        assertFalse(BitcoinAddressValidator.validateBitcoinAddress(tooShortBech32Address));
    }

    // 可以继续添加更多的测试用例来覆盖不同的场景
}