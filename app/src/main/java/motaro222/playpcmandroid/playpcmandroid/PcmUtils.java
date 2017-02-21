package motaro222.playpcmandroid.playpcmandroid;

/**
 * Some utility function to play with pcm audio
 */
public class PcmUtils {
    /**
     * Convert PCM 16bit LSB to/from PCM 16bit MSB
     *
     * @param data source data, and also the result
     */
    public static void convertPcm16bitLSBToAndFromPcm16bitMSB(byte[] data) {
        //
        if (data == null) {
            return;
        }

        // Swap byte position
        for (int i = 0; i < data.length; i += 2) {
            byte temp = data[i];
            data[i] = data[i + 1];
            data[i + 1] = temp;
        }
    }

    /**
     * A very simple algorithm to check if this PCM 16bit data is MSB or LSB
     *
     * @param data audio data of PCM 16bit, otherwise this function will return incorrect result
     * @return {@code true} if data was Pcm16bitMSB, {@code false} otherwise
     */
    public static boolean isPcm16bitMsb(byte[] data) {
        int firstByteOverMediumCount = 0;
        int secondByteOverMediumCount = 0;

        for (int i = 0; i < data.length; i += 2) {
            int first = Math.abs(data[i]);
            int second = Math.abs(data[i + 1]);

            if (first > 93) firstByteOverMediumCount++;
            if (second > 93) secondByteOverMediumCount++;
        }
        return (firstByteOverMediumCount < secondByteOverMediumCount);
    }
}
