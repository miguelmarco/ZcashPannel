package marco_buzunariz.miguel.zcashpannel;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Display;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;

import static java.lang.Math.min;


public class SendActivity extends AppCompatActivity {

    private static final int WIDTH = 700;
    private static final int BLACK = 0xFF000000;
    private static final int WHITE = 0xFFFFFFFF;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send);

        Intent intent = getIntent();
        String address = intent.getStringExtra("address");
        String amount = intent.getStringExtra("value");
        String url = "zcash:"+address+"?amount="+amount;
        TextView urltextview = (TextView)findViewById(R.id.textViewReceiveURL);
        urltextview.setText(url);

        try {
            Bitmap bmp =  encodeAsBitmap(url);
            ImageView view = (ImageView)findViewById(R.id.imageViewReceive);
            view.setImageBitmap(bmp);
        } catch (WriterException e) {
            e.printStackTrace();
        }

    }


    Bitmap encodeAsBitmap(String str) throws WriterException {
    BitMatrix result;
    try {
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width = size.x;
        int height = size.y;
        int WIDTH = min(width,height)*3/4;
        result = new MultiFormatWriter().encode(str,
            BarcodeFormat.QR_CODE, WIDTH, WIDTH, null);
    } catch (IllegalArgumentException iae) {
        // Unsupported format
        return null;
    }
    int w = result.getWidth();
    int h = result.getHeight();
    int[] pixels = new int[w * h];
    for (int y = 0; y < h; y++) {
        int offset = y * w;
        for (int x = 0; x < w; x++) {
            pixels[offset + x] = result.get(x, y) ? BLACK : WHITE;
        }
    }
    Display display = getWindowManager().getDefaultDisplay();
    Point size = new Point();
    display.getSize(size);
    int width = size.x;
    int height = size.y;
    int WIDTH = min(width,height)*3/4;
    Bitmap bitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
    bitmap.setPixels(pixels, 0, WIDTH, 0, 0, w, h);
    return bitmap;
}
}
