package motaro222.playpcmandroid.playpcmandroid;

import android.app.Activity;
import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;
import android.os.Bundle;
import android.view.View;

import java.io.IOException;
import java.io.InputStream;

/**
 * A very simple example demonstrate how to play a pcm data
 */
public class MainActivity extends Activity {

    // Sample rate of our two example pcm files
    private static final int SAMPLE_RATE = 16000; // Hz
    // Number of chanel of our two example pcm files
    private static final int CHANEL_OUT = AudioFormat.CHANNEL_OUT_MONO; // One chanel
    // Number of bit per sample of our two example pcm files
    private static final int ENCODING = AudioFormat.ENCODING_PCM_16BIT; // 1 sample take 16bit of data

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void playMSB(View view) {
        // Take audio data
        byte[] audioData = getExampleMsbFileAsByteArray();
        // Convert to MSB if need
        if (PcmUtils.isPcm16bitMsb(audioData)) {
            PcmUtils.convertPcm16bitLSBToAndFromPcm16bitMSB(audioData);
        }
        // Play
        try {
            playLsbPcm(audioData);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void playLSB(View view) {
        // Take audio data
        byte[] audioData = getExampleLsbFileAsByteArray();
        // Play
        try {
            playLsbPcm(audioData);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Play PCM(LSB) audio data
     * With
     * - Sample rate: {@link MainActivity#SAMPLE_RATE}
     * - Number of chanel: {@link MainActivity#CHANEL_OUT}
     * - Encoding: {@link MainActivity#ENCODING}
     *
     * @param lsbAudioData audio data in byte array
     * @throws IOException
     */
    private void playLsbPcm(final byte[] lsbAudioData) throws IOException {

        if (lsbAudioData == null)
            return;

        new Thread(new Runnable() {
            @Override
            public void run() {
                // Calculate minimum buffer size need to play this kind of audio file
                int intSize = android.media.AudioTrack.getMinBufferSize(SAMPLE_RATE, CHANEL_OUT, ENCODING);
                /**
                 * {@link AudioTrack#AudioTrack(int, int, int, int, int, int)}
                 */
                AudioTrack at = new AudioTrack(AudioManager.STREAM_MUSIC, SAMPLE_RATE, CHANEL_OUT, ENCODING, intSize, AudioTrack.MODE_STREAM);

                // Play audio byte array
                at.play();
                // NOTE: This will block thread until the end of audio data
                at.write(lsbAudioData, 0, lsbAudioData.length);
                at.stop();
                //
                at.release();
            }
        }).start();

    }

    /**
     * Get our example PCM(MSB) audio file as byte array
     *
     * @return example PCM(MSB) audio file as byte array
     */
    private byte[] getExampleMsbFileAsByteArray() {
        return getAssetsFileAsByteArray("ex_msb.pcm");
    }

    /**
     * Get our example PCM(LSB) audio file as byte array
     *
     * @return example PCM(LSB) audio file as byte array
     */
    private byte[] getExampleLsbFileAsByteArray() {
        return getAssetsFileAsByteArray("ex_lsb.pcm");
    }

    /**
     * Get file from assets as byte array
     *
     * @param name file name
     * @return byte array of the file
     */
    private byte[] getAssetsFileAsByteArray(String name) {
        try {
            InputStream is = getAssets().open(name);
            byte[] fileBytes = new byte[is.available()];
            is.read(fileBytes);
            is.close();
            return fileBytes;
        } catch (Exception ex) {
            ex.printStackTrace();
            return new byte[]{};
        }
    }

}
